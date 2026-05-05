# Test Scope

本次测试覆盖技术骨架迭代的 Java/Gradle 后端骨架，重点验证以下范围：

- 构建与编译入口：Gradle 工程骨架、Spring Boot 应用上下文、基础资源文件。
- Web/API 契约：`/api/v1/system/ping`、`/api/v1/books`、`/api/v1/pipeline-tasks` 的统一响应结构、成功路径、参数校验失败、领域失败映射。
- 多组件集成：`book -> pipeline -> agent` 的最小调用链、错误码映射、占位执行结果回传。
- 配置与部署骨架：`application.yml`、`Dockerfile`。

识别到的项目/功能类型与对应规范：

- `web-e2e`：`standards/testing/web-e2e.md`
- `integration`：`standards/testing/integration.md`

本次采用的执行形式：

- Framework-managed test environment：`@SpringBootTest` 与 `@WebMvcTest`
- In-process integration test：JUnit + Mockito

未采用真实服务进程 + 外部 HTTP 客户端 + MySQL 实例。原因见后文 `Standards Evidence` 与 `Known Issues`。

# Test Results

已实际执行命令：

```bash
./gradlew compileJava -q
./gradlew test -q
./gradlew bootRun
./gradlew jacocoTestReport
```

结果统计：

- 编译：通过
- 测试：`22` 通过，`0` 失败，`0` 跳过
- 覆盖率：无法获取

测试套件明细：

- `NovelFactoryApplicationTest`：`1/1` 通过
- `CommonWebContractTest`：`4/4` 通过
- `BookControllerWebTest`：`4/4` 通过
- `PipelineTaskControllerWebTest`：`4/4` 通过
- `StubAgentOrchestratorTest`：`3/3` 通过
- `PipelineTaskFlowIntegrationTest`：`4/4` 通过
- `ApplicationConfigContractTest`：`2/2` 通过

证据文件：

- `build/test-results/test/TEST-com.novelfactory.NovelFactoryApplicationTest.xml`
- `build/test-results/test/TEST-com.novelfactory.common.CommonWebContractTest.xml`
- `build/test-results/test/TEST-com.novelfactory.book.controller.BookControllerWebTest.xml`
- `build/test-results/test/TEST-com.novelfactory.pipeline.controller.PipelineTaskControllerWebTest.xml`
- `build/test-results/test/TEST-com.novelfactory.agent.service.StubAgentOrchestratorTest.xml`
- `build/test-results/test/TEST-com.novelfactory.integration.PipelineTaskFlowIntegrationTest.xml`
- `build/test-results/test/TEST-com.novelfactory.config.ApplicationConfigContractTest.xml`

失败命令与原因：

- `./gradlew bootRun`：失败。当前仓库中的 `gradlew` 是本地测试 shim，只支持 `compileJava` 与 `test`，不支持真实运行任务。
- `./gradlew jacocoTestReport`：失败。同样受 shim 限制，不支持覆盖率任务。

类型化测试结果：

- `web-e2e`：已通过框架内 `MockMvc` 验证统一响应结构、成功请求、校验失败、业务失败映射；未达到“真实服务进程 + 外部 HTTP 请求 + 真实依赖链”级别。
- `integration`：已通过 `PipelineTaskFlowIntegrationTest` 覆盖跨组件调用签名与错误传播；未达到“真实 Repository/Mapper/MySQL”级别。

# Pass Criteria

逐条对照 PRD 验收标准：

| 验收标准 | 结果 | 说明 |
|---|---|---|
| 工程可通过 Gradle 完成启动与基础构建 | 部分达标 | `compileJava`、`test` 通过；`bootRun` 无法执行，当前 `gradlew` 非真实 Gradle wrapper。 |
| 项目存在清晰的按业务域划分的代码目录结构，至少覆盖 `book`、`pipeline`、`agent`、`common` | 达标 | 目录与包结构已具备。 |
| 应用启动后可通过健康检查或验证接口证明服务可用 | 部分达标 | `SystemController` 在框架测试内通过；未完成真实启动后的 HTTP 验证。 |
| 系统存在书籍立项与基础查询的最小能力入口 | 未达标 | Controller 存在，但 `DefaultBookApplicationService` 仍为 `UnsupportedOperationException`。 |
| 系统存在从书籍接入流水线的能力入口 | 部分达标 | 入口与服务签名存在，但真实持久层未实现，无法完成真实链路。 |
| 系统存在最小空流水线任务创建与任务状态查询能力 | 部分达标 | 服务内调用链与测试存在，但 `PipelineTaskRepository` 未实现。 |
| 系统存在供流水线调用的 Agent 编排统一入口，且使用占位执行逻辑 | 达标 | `AgentOrchestrator` 与占位执行器存在，相关测试通过。 |
| 系统已接入 MySQL 与 MyBatis-Plus 基础配置 | 部分达标 | 配置与 Mapper 骨架存在，但 Repository/Mapper 未形成可执行持久化闭环。 |
| 系统已提供统一返回结构、全局异常处理和基础日志配置 | 部分达标 | 统一返回与异常处理已验证；日志配置未见独立执行证据。 |
| 系统已接入 OpenAPI/Swagger 与 Actuator | 部分达标 | 配置存在；未完成运行态访问验证。 |
| 项目包含基础 Dockerfile 和本地开发配置 | 达标 | 文件存在且配置契约测试通过。 |

结论：

- 当前 Stage 05 测试执行完成，但本次迭代**不满足最终验收通过条件**。
- 主要阻塞来自未实现的业务服务/持久层，以及缺失真实运行态与真实依赖链验证。

# Coverage

代码覆盖率数据：

- 无法获取。当前环境中的 `gradlew` shim 不支持 `jacocoTestReport` 或等价覆盖率任务。

覆盖情况说明：

- 已覆盖：Spring 上下文加载、统一响应契约、控制器参数校验与错误映射、占位编排器、`pipeline` 服务级跨组件调用签名、配置文件与 Dockerfile 合同。
- 未覆盖：真实 HTTP 服务启动、真实 MySQL/MyBatis-Plus 持久化、真实 OpenAPI/Actuator 运行态访问、真实 `book -> pipeline -> agent -> repository` 全链路。

覆盖缺口：

- `BookController -> DefaultBookApplicationService -> MyBatisBookRepository` 未执行真实链路。
- `PipelineTaskController -> DefaultPipelineTaskApplicationService -> MyBatisPipelineTaskRepository` 未执行真实链路。
- `Service -> Repository -> Mapper -> MySQL` 的 `integration` 标准未完成真实数据层验证。
- OpenAPI 与 Actuator 仅有静态配置，无运行证据。
- 日志可观测性缺少专门测试证据。

# Standards Evidence

| 规范 | 执行命令 | 证据 | 结果 |
|---|---|---|---|
| `standards/testing/web-e2e.md` | `./gradlew test -q` | `BookControllerWebTest`、`PipelineTaskControllerWebTest`、`CommonWebContractTest` 的 XML 结果文件 | 部分通过 |
| `standards/testing/integration.md` | `./gradlew test -q` | `PipelineTaskFlowIntegrationTest`、`StubAgentOrchestratorTest` 的 XML 结果文件 | 部分通过 |

规范执行说明：

- `web-e2e`
  - 已验证入口：`GET /api/v1/system/ping`、`POST /api/v1/books`、`GET /api/v1/books/{bookId}`、`GET /api/v1/books`、`POST /api/v1/pipeline-tasks`、`GET /api/v1/pipeline-tasks/{taskId}`
  - 已验证内容：状态码、统一响应结构、成功字段、校验失败、领域错误码映射
  - 未验证内容：真实服务进程、外部 HTTP 客户端、真实依赖链、运行态 OpenAPI/Actuator
  - 风险结论：阻塞验收

- `integration`
  - 已验证链路：`DefaultPipelineTaskApplicationService -> BookRepository -> AgentOrchestrator -> PipelineTaskRepository` 的调用签名与错误传播
  - 未验证内容：真实 Repository/Mapper/MySQL 的集成、真实数据读写、副作用持久化结果
  - 风险结论：阻塞验收

未执行规范及原因：

- 无其他命中类型规范。

替代验证方式：

- 使用 `@WebMvcTest` + `MockMvc`
- 使用 Mockito 驱动的服务级集成测试
- 使用 `SpringBootTest` 验证基础上下文可加载

是否阻塞验收：

- 是。根据 Stage 05 规则，Web/API 与多组件核心链路缺少真实全链路验证，默认阻塞验收。

# Known Issues

- `src/main/java/com/novelfactory/book/service/DefaultBookApplicationService.java` 的 `createBook`、`listBooks`、`getBookById` 仍直接抛出 `UnsupportedOperationException`，书籍立项与查询能力未实现。
- `src/main/java/com/novelfactory/book/repository/MyBatisBookRepository.java` 的 `save`、`findAll`、`findById` 仍直接抛出 `UnsupportedOperationException`，`book` 持久层未实现。
- `src/main/java/com/novelfactory/pipeline/repository/MyBatisPipelineTaskRepository.java` 的 `save`、`findById` 仍直接抛出 `UnsupportedOperationException`，流水线任务持久层未实现。
- `BookControllerWebTest` 与 `PipelineTaskControllerWebTest` 使用 `@WebMvcTest` + `@MockBean`，验证了控制器契约，但没有穿透到真实 Service/Repository。
- `PipelineTaskFlowIntegrationTest` 使用 Mockito 模拟 `BookRepository`、`PipelineTaskRepository`、`AgentOrchestrator`，不构成真实数据层集成验证。
- 当前 `gradlew` 为本地 shim，无法执行 `bootRun` 与覆盖率任务，导致运行态验证与覆盖率统计缺失。
