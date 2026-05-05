# Novel Factory 迭代 Design

## 1. 概述

本次设计解决的问题是：在现有 `book`、`pipeline`、`agent`、`common` 技术骨架基础上，落地 Novel Factory 的第一条真实创作业务主链，并保证这条主链既能支撑本次同步执行的最小闭环，也不会阻断后续完整产品蓝图中的多 Agent 协同、评审打回、三层记忆和统一 LLM 路由。

整体方案采用三条核心原则：

- 最小改动优先：复用现有 `BookController`、`PipelineTaskController`、`BookRepository`、`PipelineTaskRepository`、`AgentOrchestrator` 等骨架，不引入重型编排框架。
- 面向接口设计：将“生成什么”和“如何调度”拆开，形成 `AgentOrchestrator`、`AgentScheduler`、`TaskStateMachine`、`LlmGateway` 等稳定接口。
- 长期蓝图兼容：本次虽然只实现正向同步链路，但任务状态、步骤结果、模型路由配置和产物表结构必须兼容未来 `ReviewAgent` 打回、异步化、Spring Statemachine、Layer1/Layer2/Layer3 记忆体系。

本次设计的关键约束如下：

- 执行语义：本次通过 `POST /api/v1/pipeline-tasks` 同步跑完任务。
- 流程控制：本次正向执行路径线性，但架构必须是“调度器 + 状态机”而不是硬编码顺序方法调用。
- Agent 语义：本次对齐 `F1 AddictionDesignerAgent`、`B1 WorldAgent`、`B2 CharacterAgent`、`B3 OutlineAgent`，但不实现完整多 Agent 自主协同平台。
- LLM 接入：本次实现项目内统一 `LlmGateway` 抽象，底层优先支持 OpenAI-compatible provider，并预留后续系统级/Agent 级/任务级模型路由扩展位。

## 2. Impact Analysis

### 2.1 受影响模块

| 模块 | 影响程度 | 类型 | 说明 |
|---|---|---|---|
| `book` | 中 | 修改 | 补齐书籍输入输出字段，新增书籍维度的创作产物查询接口 |
| `pipeline` | 高 | 修改 | 扩展任务请求、任务状态、阶段流转、任务结果输出 |
| `agent` | 高 | 修改 | `StubAgentOrchestrator` 升级为真实编排入口，新增调度器、状态机、LLM 网关、执行日志接口 |
| `creative` | 高 | 新增 | 承载上瘾画布、世界观、人物设定三类中间产物模型、仓储和查询 |
| `plan` | 中 | 新增 | 承载策划卡聚合产物模型、仓储和查询 |
| `outline` | 中 | 新增 | 承载大纲草案模型、仓储和查询 |
| `common` | 中 | 修改 | 扩展错误码、补充统一配置承载能力 |

### 2.2 新增 vs 复用

推荐的最小改动方案如下：

- 复用：
  - 现有 `BookController` / `BookApplicationService`
  - 现有 `PipelineTaskController` / `PipelineTaskApplicationService`
  - 现有 `BookRepository` / `PipelineTaskRepository`
  - 现有 `ApiResponse` / `BusinessException` / `GlobalExceptionHandler`
- 扩展：
  - `AgentExecutionRequest`：从“bookId + taskType”扩展为任务级上下文对象
  - `PipelineTaskEntity`：新增 `generation_target`、`current_stage`、`failure_reason`
  - `PipelineTaskStatus`：细化为多阶段状态
- 新增：
  - `AgentScheduler`
  - `TaskStateMachine`
  - `LlmGateway` / `LlmProvider`
  - 5 张业务产物表
  - 1 张执行日志表
  - 书籍维度的产物查询 controller / service / repository

### 2.3 数据模型影响

- 现有 `book` 表只需兼容性扩展，不需要破坏性重建。
- `pipeline_task` 需要新增任务阶段和失败原因等字段，但仍保留任务主表角色。
- 新增独立业务产物表：
  - `addiction_canvas`
  - `world_setting`
  - `character_profile`
  - `book_plan_card`
  - `book_outline_draft`
- 新增 `agent_execution_log` 用于排查真实调用问题。

兼容性策略：

- 所有新增表均通过 `book_id` 和 `pipeline_task_id` 关联，不回写到 `book` 主表中覆盖式存储。
- `pipeline_task.status` 保留任务最终状态语义；`current_stage` 承载细粒度执行进度。
- 未来引入版本化、评审打回、RevisionRequest 时，可在现有表上增量扩展，不需要推翻模型。

### 2.4 接口影响

- `POST /api/v1/books`、`GET /api/v1/books`、`GET /api/v1/books/{bookId}` 保持路径不变，仅扩展字段。
- `POST /api/v1/pipeline-tasks` 请求体和响应体需要扩展：
  - 新增 `generationTarget`
  - 返回 `currentStage`、`failureReason`、`artifactRefs`
- 新增书籍维度查询接口：
  - `GET /api/v1/books/{bookId}/addiction-canvas/latest`
  - `GET /api/v1/books/{bookId}/world-setting/latest`
  - `GET /api/v1/books/{bookId}/character-profiles/latest`
  - `GET /api/v1/books/{bookId}/plan-card/latest`
  - `GET /api/v1/books/{bookId}/outline-drafts/latest`

兼容性结论：

- 现有 API 调用方数量为零或极少，扩展风险可控。
- 接口路径保持 REST 风格一致，和现有 `BookController`、`PipelineTaskController` 风格一致。

### 2.5 设计路径比较

| 方案 | 描述 | 优点 | 风险 |
|---|---|---|---|
| A | 硬编码顺序调用 | 实现最快 | 无法兼容 Review 打回和调度演进，不推荐 |
| B | 自实现轻量状态机 + 调度器 | 改动适中，长期兼容性好 | 需要多定义一层状态/事件模型，推荐 |
| C | 直接引入 Spring Statemachine | 状态表达更规范 | 本次范围过重，调试和接线成本高，不推荐起步 |

本次采用 **方案 B**。但状态定义、事件命名、guard/action 语义会与 Spring Statemachine 兼容，为后续升级保留迁移路径。

## 3. Flow Design

### 3.1 核心组件链路

本次 feature 的核心组件链路如下：

`PipelineTaskController -> PipelineTaskApplicationService -> AgentOrchestrator -> AgentScheduler -> TaskStateMachine -> AgentStepExecutor -> LlmGateway -> LlmProvider`

产物落库与查询链路如下：

`PipelineTaskApplicationService -> ArtifactRepository / QueryRepository -> BookArtifactController`

这是典型跨组件功能，03/04/05 阶段需要按 `integration` 和 `web-e2e` 规范编写测试，核心链路为：

- `PipelineTaskController -> DefaultPipelineTaskApplicationService -> DefaultAgentOrchestrator -> DefaultAgentScheduler`
- `BookArtifactController -> ArtifactQueryService -> ArtifactRepository`

### 3.2 同步正向执行主流程

#### 3.2.1 `PLAN_CARD`

1. 调用方提交 `CreatePipelineTaskRequest`
2. `PipelineTaskApplicationService` 校验 `book` 存在
3. 创建 `pipeline_task`，初始状态 `CREATED`
4. 调用 `AgentOrchestrator.execute(...)`
5. `AgentScheduler` 查询当前状态并推进到 `RUNNING`
6. 依次执行：
   - `F1` 上瘾画布生成
   - `B1` 世界观设定生成
   - `B2` 人物设定生成
   - `PlanCard` 聚合生成
7. 每一步成功后：
   - 写入对应业务产物表
   - 写入 `agent_execution_log`
   - 推进 `current_stage`
8. `PlanCard` 完成后推进 `COMPLETED`
9. 返回包含任务状态与产物引用的 `PipelineTaskResponse`

#### 3.2.2 `PLAN_CARD_AND_OUTLINE`

在上述流程基础上继续执行：

10. 以书籍、上瘾画布、世界观、人物设定、策划卡为输入，执行 `B3 OutlineAgent`
11. 写入 `book_outline_draft`
12. 推进 `OUTLINE_DONE -> COMPLETED`
13. 返回最终任务结果

### 3.3 调度器与状态机设计

本次不把流程写死在 application service 中，而是分成：

- `AgentScheduler`：决定“下一步该执行哪个逻辑步骤”
- `TaskStateMachine`：决定“当前状态是否允许迁移到下一状态”

建议状态机模型：

| 当前状态 | 事件 | 下一状态 |
|---|---|---|
| `CREATED` | `START` | `RUNNING` |
| `RUNNING` | `ADDICTION_CANVAS_SUCCEEDED` | `ADDICTION_CANVAS_DONE` |
| `ADDICTION_CANVAS_DONE` | `WORLD_SETTING_SUCCEEDED` | `WORLD_SETTING_DONE` |
| `WORLD_SETTING_DONE` | `CHARACTER_PROFILE_SUCCEEDED` | `CHARACTER_PROFILE_DONE` |
| `CHARACTER_PROFILE_DONE` | `PLAN_CARD_SUCCEEDED` | `PLAN_CARD_DONE` |
| `PLAN_CARD_DONE` | `OUTLINE_REQUIRED` | `PLAN_CARD_DONE` |
| `PLAN_CARD_DONE` | `OUTLINE_SUCCEEDED` | `OUTLINE_DONE` |
| `PLAN_CARD_DONE` | `COMPLETE_WITHOUT_OUTLINE` | `COMPLETED` |
| `OUTLINE_DONE` | `COMPLETE` | `COMPLETED` |
| `*` | `STEP_FAILED` | `FAILED` |

调度器最小职责：

- 读取当前 `generationTarget`
- 读取当前 `currentStage`
- 计算下一步骤 `AgentStepType`
- 构造该步骤输入
- 调用对应 `AgentStepExecutor`
- 根据 `AgentStepResult` 触发状态迁移

长期兼容位：

- `AgentStepResult` 中预留 `decision` 字段，支持未来值：
  - `NEXT`
  - `REVISE_WORLD`
  - `REVISE_CHARACTER`
  - `REVISE_OUTLINE`
  - `FAIL`
- 本次只实现 `NEXT` 和 `FAIL`
- 后续升级到 Spring Statemachine 时，`status + event + decision` 可直接映射为 state/event/guard

### 3.4 异常流程

#### 3.4.1 书籍不存在

- `BookRepository.findById` 返回空
- 直接抛出 `BOOK_NOT_FOUND`
- 不创建下游产物，不调用 LLM

#### 3.4.2 LLM 调用失败

- `LlmGateway.generate` 抛出异常或返回失败结果
- 记录 `agent_execution_log`
- 任务进入 `FAILED`
- `failureReason` 保存结构化失败摘要
- 返回对应业务错误码

#### 3.4.3 输出解析失败

- LLM 返回文本不能映射为目标 DTO
- 记录步骤失败
- 任务进入 `FAILED`
- 返回 `*_GENERATION_FAILED` 或 `LLM_CALL_FAILED`

#### 3.4.4 中间步骤持久化失败

- 任意产物 repository 保存失败
- 当前步骤视为失败
- 任务进入 `FAILED`
- 已成功落库的前序产物保留，供排查使用

## 4. Table Design

### 4.1 `book`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `title` | varchar(255) | not null | 书名 |
| `genre` | varchar(100) | not null | 题材 |
| `platform` | varchar(100) | not null | 目标平台 |
| `status` | varchar(50) | not null | 生命周期状态 |
| `description` | text | null | 简介/故事摘要 |
| `source` | varchar(50) | null | 创建来源，默认 `MANUAL` |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

兼容性：在现有表基础上新增 `source` 即可，若本次实现不改表也可先在代码中默认为 `MANUAL`。

### 4.2 `pipeline_task`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `task_type` | varchar(50) | not null | 任务类型，保留 `BOOK_ONBOARDING`/`BOOK_CREATION` 兼容 |
| `generation_target` | varchar(50) | not null | `PLAN_CARD` / `PLAN_CARD_AND_OUTLINE` |
| `status` | varchar(50) | not null | 总体状态 |
| `current_stage` | varchar(50) | not null | 当前执行阶段 |
| `trigger_source` | varchar(50) | not null | 触发来源 |
| `result_message` | text | null | 结果摘要 |
| `failure_reason` | text | null | 失败原因 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

建议索引：

- `idx_pipeline_task_book_id_created_at (book_id, created_at desc)`
- `idx_pipeline_task_status (status)`

### 4.3 `addiction_canvas`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `core_emotional_hook` | varchar(255) | not null | 核心情绪钩子 |
| `emotional_fuel` | text | not null | 主要情绪燃料 |
| `reader_fantasy` | text | not null | 目标读者幻想 |
| `suppression_release_rhythm` | text | not null | 压抑-爆发节律 |
| `forbidden_rules` | text | not null | 红线/禁止项 |
| `raw_content` | longtext | not null | 原始生成内容 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

### 4.4 `world_setting`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `positioning` | varchar(255) | not null | 作品定位 |
| `world_summary` | text | not null | 世界观摘要 |
| `core_rules` | text | not null | 核心规则/力量体系 |
| `faction_structure` | text | null | 势力结构 |
| `forbidden_items` | text | not null | 禁止项清单 |
| `raw_content` | longtext | not null | 原始生成内容 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

### 4.5 `character_profile`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `role_type` | varchar(50) | not null | `PROTAGONIST` / `SUPPORTING` / `ANTAGONIST` |
| `name` | varchar(100) | not null | 角色名称 |
| `role_label` | varchar(100) | not null | 角色定位 |
| `background` | text | not null | 背景 |
| `motivation` | text | not null | 核心动机 |
| `personality_traits` | text | not null | 性格特征 |
| `growth_arc` | text | null | 成长/关系张力 |
| `raw_content` | longtext | not null | 原始生成内容 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

建议索引：

- `idx_character_profile_book_task (book_id, pipeline_task_id)`

### 4.6 `book_plan_card`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `title` | varchar(255) | not null | 书名 |
| `genre` | varchar(100) | not null | 题材 |
| `platform` | varchar(100) | not null | 平台 |
| `one_line_hook` | varchar(255) | not null | 一句话卖点 |
| `core_setting` | text | not null | 核心设定 |
| `target_readers` | text | not null | 目标读者 |
| `core_conflict_summary` | text | not null | 核心冲突摘要 |
| `creative_direction_summary` | text | not null | 创作方向摘要 |
| `raw_content` | longtext | not null | 原始生成内容 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

### 4.7 `book_outline_draft`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `positioning` | varchar(255) | not null | 作品定位 |
| `world_summary` | text | not null | 世界观摘要 |
| `protagonist_profile` | text | not null | 主角设定摘要 |
| `main_conflict` | text | not null | 主线冲突 |
| `stage_plot_overview` | text | not null | 阶段剧情概览 |
| `arc_structure` | text | not null | 弧线结构 |
| `raw_content` | longtext | not null | 原始生成内容 |
| `created_at` | datetime | not null | 创建时间 |
| `updated_at` | datetime | not null | 更新时间 |

### 4.8 `agent_execution_log`

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | bigint | PK | 主键 |
| `pipeline_task_id` | bigint | not null, idx | 任务 ID |
| `book_id` | bigint | not null, idx | 书籍 ID |
| `step_type` | varchar(50) | not null | 步骤类型 |
| `provider_name` | varchar(100) | null | 模型提供方 |
| `model_name` | varchar(100) | null | 模型名称 |
| `request_snapshot` | longtext | null | 输入摘要 |
| `response_snapshot` | longtext | null | 输出摘要 |
| `success` | tinyint(1) | not null | 是否成功 |
| `failure_reason` | text | null | 失败原因 |
| `started_at` | datetime | not null | 开始时间 |
| `finished_at` | datetime | not null | 结束时间 |
| `created_at` | datetime | not null | 创建时间 |

### 4.9 设计原则

- 所有产物表独立存储，避免将中间产物混入 `book_plan_card.raw_content`。
- 所有业务产物都通过 `(book_id, pipeline_task_id)` 可双向追踪。
- `raw_content` 保留原始生成内容，用于调试、Prompt 调整和未来版本化。
- 本次表结构不显式建“版本号”字段，但通过多条记录 + `created_at` 实现最新版本查询。

## 5. API Design

### 5.1 `POST /api/v1/books`

- 功能：创建书籍
- Request：`CreateBookRequest`
- Response：`ApiResponse<BookDetailResponse>`
- 兼容性：路径不变，新增 `source` 字段时可默认填充

错误码：

- `VALIDATION_ERROR`
- `BOOK_CREATE_FAILED`

五类契约：

1. Functional Contract：见 `CreateBookRequest` / `BookDetailResponse`
2. SLO Contract：P50 < 50ms，P99 < 300ms
3. Observability Contract：`trace_id`、`title`、`platform`、`duration_ms`
4. Degradation Contract：数据库失败直接返回 `BOOK_CREATE_FAILED`
5. Security Contract：参数校验 + 无敏感数据写日志

### 5.2 `GET /api/v1/books`

- 功能：查询书籍列表
- Request：query `status`、`platform`
- Response：`ApiResponse<List<BookSummaryResponse>>`

错误码：

- `VALIDATION_ERROR`

### 5.3 `GET /api/v1/books/{bookId}`

- 功能：查询书籍详情
- Request：path `bookId`
- Response：`ApiResponse<BookDetailResponse>`

错误码：

- `BOOK_NOT_FOUND`

### 5.4 `POST /api/v1/pipeline-tasks`

- 功能：同步创建并执行创作任务
- Request：`CreatePipelineTaskRequest`
- Response：`ApiResponse<PipelineTaskResponse>`

请求字段：

- `bookId`
- `taskType`
- `generationTarget`
- `triggerSource`

响应字段：

- `taskId`
- `bookId`
- `taskType`
- `generationTarget`
- `status`
- `currentStage`
- `failureReason`
- `resultMessage`
- `artifactRefs`

错误码：

- `BOOK_NOT_FOUND`
- `INVALID_GENERATION_TARGET`
- `PIPELINE_TASK_CREATE_FAILED`
- `LLM_CALL_FAILED`
- `ADDICTION_CANVAS_GENERATION_FAILED`
- `WORLD_SETTING_GENERATION_FAILED`
- `CHARACTER_PROFILE_GENERATION_FAILED`
- `PLAN_CARD_GENERATION_FAILED`
- `OUTLINE_GENERATION_FAILED`

五类契约：

1. Functional Contract：见 `CreatePipelineTaskRequest` / `PipelineTaskResponse`
2. SLO Contract：同步生成接口 P50 < 8s，P99 < 30s
3. Observability Contract：`trace_id`、`task_id`、`book_id`、`generation_target`、`current_stage`、`duration_ms`、`provider_name`、`model_name`
4. Degradation Contract：LLM 主 provider 失败时可切换 fallback；fallback 不可用则返回显式失败错误码
5. Security Contract：参数校验，敏感配置不出现在日志或响应中

### 5.5 `GET /api/v1/pipeline-tasks/{taskId}`

- 功能：查询任务状态与结果摘要
- Request：path `taskId`
- Response：`ApiResponse<PipelineTaskResponse>`

错误码：

- `PIPELINE_TASK_NOT_FOUND`

### 5.6 `GET /api/v1/books/{bookId}/addiction-canvas/latest`

- 功能：查询最新上瘾画布
- Response：`ApiResponse<AddictionCanvasResponse>`

错误码：

- `BOOK_NOT_FOUND`
- `ADDICTION_CANVAS_NOT_FOUND`

### 5.7 `GET /api/v1/books/{bookId}/world-setting/latest`

- 功能：查询最新世界观设定
- Response：`ApiResponse<WorldSettingResponse>`

错误码：

- `BOOK_NOT_FOUND`
- `WORLD_SETTING_NOT_FOUND`

### 5.8 `GET /api/v1/books/{bookId}/character-profiles/latest`

- 功能：查询最新人物设定集合
- Response：`ApiResponse<CharacterProfileListResponse>`

错误码：

- `BOOK_NOT_FOUND`
- `CHARACTER_PROFILE_NOT_FOUND`

### 5.9 `GET /api/v1/books/{bookId}/plan-card/latest`

- 功能：查询最新策划卡
- Response：`ApiResponse<BookPlanCardResponse>`

错误码：

- `BOOK_NOT_FOUND`
- `PLAN_CARD_NOT_FOUND`

### 5.10 `GET /api/v1/books/{bookId}/outline-drafts/latest`

- 功能：查询最新大纲草案
- Response：`ApiResponse<BookOutlineDraftResponse>`

错误码：

- `BOOK_NOT_FOUND`
- `OUTLINE_DRAFT_NOT_FOUND`

## 6. Module Design

### 6.1 模块划分与职责

| 模块 | 职责 | 主要接口 |
|---|---|---|
| `book` | 书籍基础信息的创建和查询 | `BookApplicationService` |
| `pipeline` | 任务创建、状态查询、任务结果聚合 | `PipelineTaskApplicationService` |
| `agent` | 调度、状态流转、步骤执行、LLM 调用 | `AgentOrchestrator`、`AgentScheduler`、`TaskStateMachine`、`LlmGateway` |
| `creative` | 上瘾画布、世界观、人物设定的持久化与查询 | `CreativeArtifactQueryService` |
| `plan` | 策划卡持久化与查询 | `PlanCardQueryService` |
| `outline` | 大纲草案持久化与查询 | `OutlineDraftQueryService` |
| `common` | 错误码、响应封装、异常处理、配置 | `ApiResponse`、`ErrorCode` |

### 6.2 关键接口设计

- `AgentOrchestrator`
  - `AgentExecutionResult execute(AgentExecutionRequest request)`
- `AgentScheduler`
  - `AgentExecutionResult run(AgentExecutionContext context)`
- `TaskStateMachine`
  - `PipelineTaskStatus transit(PipelineTaskStatus current, PipelineTaskEvent event, GenerationTarget target)`
- `AgentStepExecutor`
  - `AgentStepType supports()`
  - `AgentStepResult execute(AgentStepContext context)`
- `LlmGateway`
  - `LlmGenerationResult generate(LlmGenerationRequest request)`
- `BookArtifactQueryService`
  - 查询最新上瘾画布 / 世界观 / 人物 / 策划卡 / 大纲

### 6.3 与现有模块的集成方式

- `DefaultPipelineTaskApplicationService` 仍然是任务入口，但不再直接只调用一个黑盒 stub，而是委托 `AgentOrchestrator`
- `AgentOrchestrator` 内部组合 `AgentScheduler + TaskStateMachine + AgentStepExecutor` 列表
- 书籍维度产物查询采用新增 controller，但复用 `BookRepository` 先校验书籍存在

### 6.4 技术选型说明

#### 多 Agent 协同

本次不引入 LangGraph、AutoGen 等通用自治 Agent 框架，原因是：

- 当前业务更像“强约束生产流水线”，不是开放式对话协商
- 任务状态、产物结构和失败定位要求高
- 本次同步执行闭环不需要重型框架

本次采用：

- Spring Boot 应用服务编排
- 自实现 `AgentScheduler`
- 自实现轻量 `TaskStateMachine`

后续升级路径：

- 当评审打回、循环修订、异步分布式执行需求成熟后，可将 `TaskStateMachine` 实现替换为 Spring Statemachine，保留相同的状态/事件/决策模型。

#### 统一 LLM 接入

本次采用“核心抽象自实现，provider 适配器可插拔”的方案：

- 上层自定义 `LlmGateway`
- 中层定义 `ModelRoutingPolicy`
- 底层先实现 `OpenAiCompatibleLlmProvider`

不建议本次直接将 Spring AI / LangChain4j 作为核心架构抽象，原因是长期需要：

- 系统级/Agent 级/任务级路由
- 精细化执行日志
- 结构化输出解析
- 与业务状态机紧耦合的失败语义

框架可作为后续 provider 实现参考，但不作为本项目的边界抽象。

## 7. Output Contract

### 7.1 `POST /api/v1/pipeline-tasks`

- 业务描述：创建并同步执行创作主链
- type id：`web-e2e`
- 是否跨组件：是
- 组件链路：`PipelineTaskController -> DefaultPipelineTaskApplicationService -> DefaultAgentOrchestrator -> DefaultAgentScheduler -> AgentStepExecutor -> LlmGateway -> Repository`
- 引用测试规范：
  - `standards/testing/web-e2e.md`
  - `standards/testing/integration.md`
- 输入：
  - `bookId`
  - `taskType`
  - `generationTarget`
  - `triggerSource`
- 输出：
  - `PipelineTaskResponse`
- 产出类型：
  - `pipeline_task`
  - `addiction_canvas`
  - `world_setting`
  - `character_profile`
  - `book_plan_card`
  - 可选 `book_outline_draft`
- 正确性规则：
  - `PLAN_CARD` 必须产出前四类资产
  - `PLAN_CARD_AND_OUTLINE` 必须额外产出大纲草案
  - 失败时不得返回伪成功状态

### 7.2 `GET /api/v1/pipeline-tasks/{taskId}`

- 业务描述：查询任务状态和产物引用
- type id：`web-e2e`
- 是否跨组件：是
- 组件链路：`PipelineTaskController -> DefaultPipelineTaskApplicationService -> PipelineTaskRepository`
- 引用测试规范：
  - `standards/testing/web-e2e.md`
  - `standards/testing/integration.md`
- 输入：`taskId`
- 输出：`PipelineTaskResponse`
- 产出类型：`none`
- 正确性规则：返回状态、当前阶段、失败信息和引用信息一致

### 7.3 书籍维度产物查询接口

适用接口：

- `/books/{bookId}/addiction-canvas/latest`
- `/books/{bookId}/world-setting/latest`
- `/books/{bookId}/character-profiles/latest`
- `/books/{bookId}/plan-card/latest`
- `/books/{bookId}/outline-drafts/latest`

统一契约：

- type id：`web-e2e`
- 是否跨组件：是
- 组件链路：`BookArtifactController -> ArtifactQueryService -> Repository`
- 引用测试规范：
  - `standards/testing/web-e2e.md`
  - `standards/testing/integration.md`
- 输入：`bookId`
- 输出：对应产物 response DTO
- 产出类型：`none`
- 正确性规则：
  - 返回最新一条成功产物
  - 书籍不存在时返回 `BOOK_NOT_FOUND`
  - 产物不存在时返回对应 `*_NOT_FOUND`

### 7.4 `TaskStateMachine.transit`

- 业务描述：执行任务状态迁移
- type id：`library`
- 是否跨组件：否
- 组件链路：`none`
- 引用测试规范：
  - `standards/testing/library.md`
- 输入：
  - 当前状态
  - 事件
  - 生成目标
- 输出：下一状态
- 产出类型：`none`
- 正确性规则：
  - 非法迁移必须抛出业务异常
  - `PLAN_CARD` 不允许进入 `OUTLINE_DONE`
  - `PLAN_CARD_AND_OUTLINE` 不允许从 `PLAN_CARD_DONE` 直接结束，除非显式完成策略允许

### 7.5 `LlmGateway.generate`

- 业务描述：统一模型调用入口
- type id：`library`
- 是否跨组件：是
- 组件链路：`LlmGateway -> LlmProvider`
- 引用测试规范：
  - `standards/testing/library.md`
  - `standards/testing/integration.md`
- 输入：
  - provider/model 路由信息
  - prompt 模板
  - 结构化输出 schema 名称
- 输出：
  - `LlmGenerationResult`
- 产出类型：`none`
- 正确性规则：
  - 调用成功时返回原始文本和解析后结构化结果
  - 调用失败时返回或抛出显式错误
  - 记录 provider/model 元信息

## 8. Change Log

### `book`

- 修改 `CreateBookRequest`：补充 `source` 或兼容默认来源
- 修改 `BookDetailResponse` / `BookSummaryResponse`：补充产品所需字段
- 新增 `BookArtifactController`：承载书籍维度创作产物查询接口

### `pipeline`

- 修改 `CreatePipelineTaskRequest`：新增 `generationTarget`
- 修改 `PipelineTaskEntity`：新增 `generationTarget`、`currentStage`、`failureReason`
- 修改 `PipelineTaskStatus`：细化为多阶段状态
- 修改 `PipelineTaskResponse`：新增阶段、失败原因、产物引用
- 修改 `DefaultPipelineTaskApplicationService`：接入真实编排

### `agent`

- 修改 `AgentExecutionRequest`：扩展为任务执行上下文
- 修改 `AgentExecutionResult`：补充产物引用和失败语义
- 修改 `AgentOrchestrator`：保持统一入口
- 新增 `AgentScheduler`
- 新增 `TaskStateMachine`
- 新增 `AgentStepExecutor`
- 新增 `LlmGateway`、`LlmProvider`、`ModelRoutingPolicy`
- 新增 `AgentExecutionLogEntity` 及仓储接口

### `creative` / `plan` / `outline`

- 新增中间产物实体、响应 DTO、仓储接口、查询服务
- 新增策划卡实体与查询服务
- 新增大纲草案实体与查询服务

### `common`

- 修改 `ErrorCode`：新增中间产物和 LLM 相关错误码
- 新增最小模型配置对象

## 9. Development Tasks

1. `扩展 pipeline_task 数据模型与请求响应契约，支持 generationTarget、currentStage、failureReason 和 artifactRefs 输出`
   - 所属模块：`pipeline`
   - 涉及接口/方法：`CreatePipelineTaskRequest`、`PipelineTaskEntity`、`PipelineTaskResponse`
   - 输入：任务创建请求
   - 输出：扩展后的任务模型
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

2. `定义任务状态机与事件模型，支持同步正向链路并兼容未来 Spring Statemachine 升级`
   - 所属模块：`agent`
   - 涉及接口/方法：`TaskStateMachine.transit`
   - 输入：当前状态、事件、生成目标
   - 输出：下一状态
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

3. `定义 AgentScheduler、AgentStepExecutor 和 AgentStepResult 骨架，建立可调度的 Agent 执行内核`
   - 所属模块：`agent`
   - 涉及接口/方法：`AgentScheduler.run`、`AgentStepExecutor.execute`
   - 输入：任务执行上下文、步骤上下文
   - 输出：任务执行结果、步骤执行结果
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

4. `定义统一 LlmGateway、OpenAI-compatible provider、模型路由配置和 Prompt 模板配置骨架`
   - 所属模块：`agent`
   - 涉及接口/方法：`LlmGateway.generate`
   - 输入：模型调用请求、路由配置、Prompt 模板
   - 输出：结构化模型调用结果
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：是

5. `新增 addiction_canvas、world_setting、character_profile、book_plan_card、book_outline_draft、agent_execution_log 的实体与仓储骨架`
   - 所属模块：`creative`、`plan`、`outline`、`agent`
   - 涉及接口/方法：实体类、repository 接口
   - 输入：持久化对象
   - 输出：仓储契约
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

6. `扩展 AgentExecutionRequest 和 AgentExecutionResult，使任务执行链路能够传递 generationTarget、阶段结果和产物引用`
   - 所属模块：`agent`
   - 涉及接口/方法：`AgentExecutionRequest`、`AgentExecutionResult`
   - 输入：任务上下文
   - 输出：执行结果上下文
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

7. `扩展 DefaultPipelineTaskApplicationService，使 POST /pipeline-tasks 通过 AgentOrchestrator 同步执行真实创作任务`
   - 所属模块：`pipeline`
   - 涉及接口/方法：`createPipelineTask`
   - 输入：`CreatePipelineTaskRequest`
   - 输出：`PipelineTaskResponse`
   - 产出类型：`web-e2e`
   - type id：`web-e2e`
   - 是否跨组件：是

8. `定义上瘾画布、世界观设定、人物设定、策划卡和大纲草案的响应 DTO 与查询服务骨架`
   - 所属模块：`creative`、`plan`、`outline`
   - 涉及接口/方法：QueryService / Response DTO
   - 输入：`bookId`
   - 输出：产物响应对象
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

9. `新增 BookArtifactController，提供按书籍查询最新中间产物与最终产物的只读接口`
   - 所属模块：`book`
   - 涉及接口/方法：新增 5 个 GET 接口
   - 输入：`bookId`
   - 输出：各类产物 response
   - 产出类型：`web-e2e`
   - type id：`web-e2e`
   - 是否跨组件：是

10. `扩展 ErrorCode 和任务阶段枚举，补齐中间产物、生成失败和查询失败的错误码契约`
   - 所属模块：`common`、`pipeline`
   - 涉及接口/方法：`ErrorCode`、相关枚举
   - 输入：无
   - 输出：错误码与枚举定义
   - 产出类型：`library`
   - type id：`library`
   - 是否跨组件：否

11. `定义本次 feature 的 Controller -> Service -> Repository 集成链路测试入口所需骨架，确保后续 03 阶段可按 integration 规范编写测试`
   - 所属模块：`book`、`pipeline`、`creative`、`plan`、`outline`
   - 涉及接口/方法：controller、service、repository 公共签名
   - 输入：HTTP 请求 / repository 交互
   - 输出：可编译的集成链路
   - 产出类型：`integration`
   - type id：`integration`
   - 是否跨组件：是
