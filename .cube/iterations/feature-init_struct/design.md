# 技术骨架搭建迭代设计文档

## 1. 概述

本次设计的目标是在当前仅有产品和业务文档、尚无后端代码的仓库中，建立一个可启动、可扩展、可继续进入 TDD 开发流程的 Java 后端技术骨架。该骨架面向 Novel Factory 的第一批核心业务域，优先保证目录规划、模块边界、接口契约、最小数据模型和最小流水线闭环清晰可用。

整体方案选择单体 Spring Boot Web 应用，采用 Gradle Groovy DSL 构建，顶层按业务域分包，域内按分层组织。这样可以在仓库早期避免过早引入多模块构建复杂度，同时保留未来拆分服务或 Gradle 子模块的演进空间。

本次设计的核心约束如下：

- 项目必须可启动，并具备最小运行验证能力
- 顶层代码结构必须按业务域划分，而不是按 Agent 类型或技术分层横切
- `book -> pipeline -> agent` 必须形成一个最小可跑通的占位链路
- 持久层仅覆盖 `book` 和 `pipeline_task` 两张核心表
- 真实 AI 执行、调度器、消息队列、安全鉴权和复杂业务表不进入本轮设计
- 骨架代码必须可支持下一阶段测试文件编译，不在本阶段承载业务实现

## Impact Analysis

| 模块/资产 | 影响类型 | 影响程度 | 说明 |
|---|---|---|---|
| 仓库根目录构建结构 | 新增 | 高 | 新增 `settings.gradle`、`build.gradle`、Gradle 项目配置 |
| Spring Boot 应用入口 | 新增 | 高 | 新增主启动类与基础资源配置 |
| `common` 公共层 | 新增 | 高 | 新增统一响应、错误码、异常基类、全局异常处理、配置类 |
| `book` 域 | 新增 | 高 | 新增书籍立项、列表、详情接口与持久层骨架 |
| `pipeline` 域 | 新增 | 高 | 新增流水线任务创建、查询接口与持久层骨架 |
| `agent` 域 | 新增 | 中 | 新增统一编排接口与占位执行器 |
| MyBatis-Plus / MySQL 配置 | 新增 | 高 | 提供真实持久层技术栈入口，但不完成完整业务落地 |
| OpenAPI / Swagger | 新增 | 中 | 供开发期接口可视化与契约检查 |
| Actuator | 新增 | 中 | 提供健康检查与运行自检入口 |
| Dockerfile / 本地配置 | 新增 | 中 | 提供容器化和本地开发基础入口 |

### 现有接口兼容性

当前仓库不存在后端 API，因此本次 API 设计不存在向后兼容压力。所有接口均作为新基线建立，后续迭代应基于本次接口语义扩展，避免在 03 阶段之后轻易推翻资源模型。

### 现有数据兼容性

当前仓库不存在数据库表结构和生产数据，因此本次新增 `book` 与 `pipeline_task` 表不存在历史数据迁移问题。后续扩展子表时需要遵守“主表稳定、扩展表逐步增加”的策略，避免修改本轮字段含义。

### 方案取舍

- 未选择按技术分层横切的顶层目录，是为了避免 `controller/service/mapper` 顶层结构在业务扩展后失去边界感
- 未选择一开始做 Gradle 多模块，是为了降低初始工程和测试阶段的复杂度
- 未选择将“接入流水线”建模为 `book` 子资源动作接口，是为了让流水线任务成为独立资源，后续支持多次执行、重试和不同任务类型时更自然

## Flow Design

### 1. 书籍立项与管理流程

1. 客户端调用 `POST /api/v1/books`
2. `BookController` 接收请求并交给 `BookApplicationService`
3. `BookApplicationService` 校验基础输入并调用 `BookRepository`
4. `BookRepository` 通过 `BookMapper` 持久化 `book` 主表
5. 服务返回统一响应对象

异常流程：

- 请求体缺少必要字段时，返回 `VALIDATION_ERROR`
- 数据不存在时，返回 `BOOK_NOT_FOUND`
- 持久层异常时，统一转换为 `INTERNAL_ERROR`

### 2. 书籍接入流水线流程

组件链路：`BookController -> BookApplicationService -> PipelineTaskApplicationService -> AgentOrchestrator -> PipelineTaskRepository`

1. 客户端调用 `POST /api/v1/pipeline-tasks`，请求体包含 `bookId`
2. `PipelineTaskController` 将请求交给 `PipelineTaskApplicationService`
3. `PipelineTaskApplicationService` 先通过 `BookRepository` 确认书籍存在
4. 服务创建一条状态为 `CREATED` 的 `pipeline_task`
5. 服务调用 `AgentOrchestrator.execute()` 发起一次同步占位执行
6. 编排器返回占位结果后，服务更新任务状态为 `COMPLETED` 或 `FAILED`
7. 返回任务标识、状态和结果消息

异常流程：

- `bookId` 不存在时返回 `BOOK_NOT_FOUND`
- 创建任务失败时返回 `PIPELINE_TASK_CREATE_FAILED`
- 编排执行失败时返回 `AGENT_EXECUTION_FAILED`，并将任务状态更新为 `FAILED`

### 3. 流水线任务查询流程

1. 客户端调用 `GET /api/v1/pipeline-tasks/{taskId}`
2. `PipelineTaskController` 调用 `PipelineTaskApplicationService`
3. 服务通过 `PipelineTaskRepository` 查询任务
4. 若存在则返回状态详情；不存在则返回 `PIPELINE_TASK_NOT_FOUND`

### 4. Agent 占位编排流程

1. `PipelineTaskApplicationService` 构造 `AgentExecutionRequest`
2. `AgentOrchestrator` 使用固定同步流程执行
3. 本轮由 `StubAgentOrchestrator` 返回占位结果，不调用真实 LLM 或外部平台

异常流程：

- 参数不完整时，抛出业务异常并由全局异常处理器映射为统一错误响应
- 占位执行器异常时，向上抛出 `AGENT_EXECUTION_FAILED`

## Table Design

### 1. `book`

```sql
CREATE TABLE book (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  genre VARCHAR(64) NOT NULL,
  platform VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  description VARCHAR(1024) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | `BIGINT` | PK, Auto Increment | 书籍主键 |
| `title` | `VARCHAR(255)` | Not Null | 书名 |
| `genre` | `VARCHAR(64)` | Not Null | 题材 |
| `platform` | `VARCHAR(64)` | Not Null | 目标平台 |
| `status` | `VARCHAR(32)` | Not Null | 书籍状态，初期建议 `DRAFT`、`ACTIVE` |
| `description` | `VARCHAR(1024)` | Nullable | 立项描述 |
| `created_at` | `DATETIME` | Not Null | 创建时间 |
| `updated_at` | `DATETIME` | Not Null | 更新时间 |

索引建议：

- `idx_book_status(status)`
- `idx_book_platform(platform)`

### 2. `pipeline_task`

```sql
CREATE TABLE pipeline_task (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  book_id BIGINT NOT NULL,
  task_type VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  trigger_source VARCHAR(32) NOT NULL,
  result_message VARCHAR(1024) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_pipeline_task_book FOREIGN KEY (book_id) REFERENCES book(id)
);
```

| 字段 | 类型 | 约束 | 说明 |
|---|---|---|---|
| `id` | `BIGINT` | PK, Auto Increment | 任务主键 |
| `book_id` | `BIGINT` | Not Null, FK | 关联书籍 |
| `task_type` | `VARCHAR(64)` | Not Null | 任务类型，本轮固定支持 `BOOK_ONBOARDING` |
| `status` | `VARCHAR(32)` | Not Null | 任务状态：`CREATED`、`RUNNING`、`COMPLETED`、`FAILED` |
| `trigger_source` | `VARCHAR(32)` | Not Null | 触发来源，本轮支持 `MANUAL` |
| `result_message` | `VARCHAR(1024)` | Nullable | 执行结果或错误摘要 |
| `created_at` | `DATETIME` | Not Null | 创建时间 |
| `updated_at` | `DATETIME` | Not Null | 更新时间 |

索引建议：

- `idx_pipeline_task_book_id(book_id)`
- `idx_pipeline_task_status(status)`
- `idx_pipeline_task_task_type(task_type)`

### 数据兼容性说明

- 本轮不设计世界观、章节、Agent 配置等子表
- `pipeline_task` 作为独立资源表，不依赖未来具体流水线阶段子表即可工作
- 后续如扩展任务执行日志，应增加新表而不是修改 `pipeline_task` 基础语义

## API Design

所有 API 统一遵循 `.cube/config/api-spec.md` 中的响应封装约定，统一使用 `ApiResponse<T>` 返回。

### 1. 新增书籍

- Method + Path: `POST /api/v1/books`
- Request Body:

```json
{
  "title": "书名",
  "genre": "都市系统流",
  "platform": "FANQIE",
  "status": "DRAFT",
  "description": "立项说明"
}
```

- Response Body:

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": 1,
    "title": "书名",
    "genre": "都市系统流",
    "platform": "FANQIE",
    "status": "DRAFT",
    "description": "立项说明"
  }
}
```

- Error Codes:
  - `VALIDATION_ERROR`
  - `BOOK_CREATE_FAILED`
  - `INTERNAL_ERROR`

### 2. 查询书籍列表

- Method + Path: `GET /api/v1/books`
- Query Parameters:
  - `status` 可选
  - `platform` 可选
- Response Body: `ApiResponse<List<BookSummaryResponse>>`
- Error Codes:
  - `VALIDATION_ERROR`
  - `INTERNAL_ERROR`

### 3. 查询书籍详情

- Method + Path: `GET /api/v1/books/{bookId}`
- Path Parameters:
  - `bookId`: `Long`
- Response Body: `ApiResponse<BookDetailResponse>`
- Error Codes:
  - `BOOK_NOT_FOUND`
  - `VALIDATION_ERROR`
  - `INTERNAL_ERROR`

### 4. 创建流水线任务

- Method + Path: `POST /api/v1/pipeline-tasks`
- Request Body:

```json
{
  "bookId": 1,
  "taskType": "BOOK_ONBOARDING",
  "triggerSource": "MANUAL"
}
```

- Response Body:

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "taskId": 1001,
    "bookId": 1,
    "taskType": "BOOK_ONBOARDING",
    "status": "COMPLETED",
    "resultMessage": "stub execution completed"
  }
}
```

- Error Codes:
  - `VALIDATION_ERROR`
  - `BOOK_NOT_FOUND`
  - `PIPELINE_TASK_CREATE_FAILED`
  - `AGENT_EXECUTION_FAILED`
  - `INTERNAL_ERROR`

### 5. 查询流水线任务状态

- Method + Path: `GET /api/v1/pipeline-tasks/{taskId}`
- Path Parameters:
  - `taskId`: `Long`
- Response Body: `ApiResponse<PipelineTaskResponse>`
- Error Codes:
  - `PIPELINE_TASK_NOT_FOUND`
  - `VALIDATION_ERROR`
  - `INTERNAL_ERROR`

### 6. 运行验证入口

- Method + Path: `GET /api/v1/system/ping`
- Response Body:

```json
{
  "code": "OK",
  "message": "success",
  "data": "pong"
}
```

- Error Codes:
  - `INTERNAL_ERROR`

## Module Design

### 1. 模块划分

#### `com.novelfactory`

- `NovelFactoryApplication`
  作用：Spring Boot 主启动类

#### `com.novelfactory.common`

- `api`
  作用：统一响应对象、错误码约定
- `config`
  作用：OpenAPI、MyBatis-Plus 等基础配置
- `exception`
  作用：业务异常与全局异常处理
- `controller`
  作用：系统级验证接口

#### `com.novelfactory.book`

- `controller`
  作用：暴露书籍新增、列表、详情接口
- `service`
  作用：承载书籍应用层接口
- `model`
  作用：书籍实体、枚举、请求响应 DTO
- `mapper`
  作用：MyBatis-Plus Mapper
- `repository`
  作用：封装持久化边界

#### `com.novelfactory.pipeline`

- `controller`
  作用：暴露流水线任务创建与查询接口
- `service`
  作用：协调书籍校验、任务创建、Agent 调用与状态更新
- `model`
  作用：任务实体、状态枚举、请求响应 DTO
- `mapper`
  作用：MyBatis-Plus Mapper
- `repository`
  作用：封装任务持久化边界

#### `com.novelfactory.agent`

- `service`
  作用：统一编排接口与占位执行器
- `model`
  作用：Agent 执行请求与结果 DTO

### 2. 模块接口定义

#### `BookApplicationService`

- `BookDetailResponse createBook(CreateBookRequest request)`
- `List<BookSummaryResponse> listBooks(String status, String platform)`
- `BookDetailResponse getBookById(Long bookId)`

异常：

- 输入非法时抛出 `BusinessException`
- 书籍不存在时抛出 `NotFoundException`

#### `PipelineTaskApplicationService`

- `PipelineTaskResponse createPipelineTask(CreatePipelineTaskRequest request)`
- `PipelineTaskResponse getPipelineTask(Long taskId)`

异常：

- 书籍不存在时抛出 `NotFoundException`
- 任务创建失败或执行失败时抛出 `BusinessException`

#### `AgentOrchestrator`

- `AgentExecutionResult execute(AgentExecutionRequest request)`

异常：

- 调用参数非法时抛出 `BusinessException`
- 占位执行失败时抛出 `BusinessException`

### 3. 模块依赖关系

- `book` 依赖 `common`
- `pipeline` 依赖 `common`、`book`、`agent`
- `agent` 依赖 `common`
- 所有 Web 入口统一通过 `common.api.ApiResponse` 返回

### 4. 集成方式

- `book` 不直接依赖 `agent`
- `pipeline` 通过 `BookRepository` 检查书籍，再通过 `AgentOrchestrator` 执行占位编排
- `repository` 层对外暴露接口，内部适配 MyBatis-Plus `Mapper`

## Output Contract

| 名称 | 业务描述 | type id | 是否跨组件 | 组件链路 | 测试规范 |
|---|---|---|---|---|---|
| `POST /api/v1/books` | 创建书籍立项记录 | `web-e2e` | 是 | `BookController -> BookApplicationService -> BookRepository` | `standards/testing/web-e2e.md`, `standards/testing/integration.md` |
| `GET /api/v1/books` | 查询书籍列表 | `web-e2e` | 是 | `BookController -> BookApplicationService -> BookRepository` | `standards/testing/web-e2e.md`, `standards/testing/integration.md` |
| `GET /api/v1/books/{bookId}` | 查询书籍详情 | `web-e2e` | 是 | `BookController -> BookApplicationService -> BookRepository` | `standards/testing/web-e2e.md`, `standards/testing/integration.md` |
| `POST /api/v1/pipeline-tasks` | 创建书籍接入流水线任务 | `web-e2e` | 是 | `PipelineTaskController -> PipelineTaskApplicationService -> BookRepository -> AgentOrchestrator -> PipelineTaskRepository` | `standards/testing/web-e2e.md`, `standards/testing/integration.md` |
| `GET /api/v1/pipeline-tasks/{taskId}` | 查询流水线任务状态 | `web-e2e` | 是 | `PipelineTaskController -> PipelineTaskApplicationService -> PipelineTaskRepository` | `standards/testing/web-e2e.md`, `standards/testing/integration.md` |
| `GET /api/v1/system/ping` | 验证系统基础可用性 | `web-e2e` | 否 | `SystemController` | `standards/testing/web-e2e.md` |
| `BookMapper` / `PipelineTaskMapper` 基础查询与保存 | 持久层 SQL 映射骨架 | `integration` | 是 | `Service -> Repository -> Mapper -> MySQL` | `standards/testing/integration.md` |

### 正确性规则

- 所有 API 必须返回统一的 `ApiResponse<T>` 结构
- 所有错误响应必须输出明确错误码，不允许裸字符串错误
- `createPipelineTask` 的成功结果必须至少包含 `taskId`、`bookId`、`taskType`、`status`
- `PipelineTaskStatus` 只允许 `CREATED`、`RUNNING`、`COMPLETED`、`FAILED`
- `book.status` 在本轮至少允许 `DRAFT` 和 `ACTIVE`
- 占位执行器不得引入真实外部调用、副作用队列或远程依赖

## Change Log

| 类型 | 路径/模块 | 变更说明 |
|---|---|---|
| 新增 | 根目录构建文件 | 新增 `settings.gradle`、`build.gradle` |
| 新增 | `src/main/java/com/novelfactory` | 新增 Spring Boot 主启动类 |
| 新增 | `common` 模块 | 新增统一响应、错误码、异常处理、基础配置、系统验证接口 |
| 新增 | `book` 模块 | 新增书籍 Controller、Service、Model、Mapper、Repository 骨架 |
| 新增 | `pipeline` 模块 | 新增流水线任务 Controller、Service、Model、Mapper、Repository 骨架 |
| 新增 | `agent` 模块 | 新增统一编排接口和占位执行器 |
| 新增 | `src/main/resources` | 新增应用配置 |
| 新增 | 根目录部署文件 | 新增基础 `Dockerfile` |

## Development Tasks

1. 新建 Gradle Groovy DSL 的 Spring Boot 单体工程骨架，接入 Web、Validation、Actuator、OpenAPI、MyBatis-Plus 和 MySQL 依赖，并提供应用启动入口与基础资源配置。
   输入：当前空仓库与本次设计文档。
   输出：可识别的构建文件、主启动类和资源目录结构。
   产出类型：`none`

2. 建立 `common` 公共层骨架，提供统一响应对象、错误码枚举、业务异常基类、全局异常处理器、系统验证接口以及基础配置类。
   输入：统一 API 规范与错误处理约定。
   输出：可被各业务域直接引用的公共类型和基础配置骨架。
   产出类型：`library`

3. 建立 `book` 域骨架，覆盖书籍新增、列表、详情的 Controller、Service、Repository、Mapper、Entity、DTO 和状态枚举声明。
   输入：书籍 API 契约和 `book` 表设计。
   输出：完整的 `book` 域接口与数据模型骨架。
   产出类型：`web-e2e`

4. 建立 `pipeline` 域骨架，覆盖流水线任务创建、任务查询的 Controller、Service、Repository、Mapper、Entity、DTO、任务类型和状态枚举声明。
   输入：流水线任务 API 契约和 `pipeline_task` 表设计。
   输出：完整的 `pipeline` 域接口与数据模型骨架。
   产出类型：`web-e2e`

5. 建立 `agent` 域骨架，提供 `AgentOrchestrator` 接口、占位执行器以及执行请求/结果模型，供 `pipeline` 域同步调用。
   输入：同步占位编排设计。
   输出：可被流水线服务依赖的统一编排入口骨架。
   产出类型：`integration`

6. 建立 `book -> pipeline -> agent` 的最小跨组件调用签名，使“创建流水线任务”链路在代码结构上完整连通，但不实现业务逻辑。
   输入：模块接口定义与流程设计。
   输出：跨域方法签名、依赖注入关系和占位返回/异常骨架。
   产出类型：`integration`

7. 补充本地开发与部署骨架，提供 `application.yml`、开发期数据库配置占位以及基础 `Dockerfile`。
   输入：运行与部署约定。
   输出：本地与容器运行入口配置骨架。
   产出类型：`none`
