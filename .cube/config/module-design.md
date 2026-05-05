# Novel Factory - Module Design

## Module Design Positioning
This document is a delivery-oriented summary of the long-term product blueprint defined in:

- `novel_factory_prd.md`
- `novel_factory_business_architecture.md`

Those two documents describe the final target state. The module model below should therefore be interpreted as a staged implementation map for that target, not just a snapshot of currently implemented code.

## Product-Level Module Map
At the product level, Novel Factory is not a generic content system. It is a novel-production operations backend centered on one business objective: continuously producing, reviewing, publishing, measuring, and optimizing multiple novels in parallel.

The long-term business capability map is:

- topic discovery and book initiation
- worldbuilding and character asset generation
- outline and chapter-script planning
- chapter drafting, review, and revision
- publish planning and platform delivery
- metrics ingestion, diagnostics, and portfolio strategy
- Agent configuration, memory management, and execution routing

Current iterations can land only a subset, but new modules should be evaluated against this full capability map.

## Module List
| Module | Package | Purpose | Key Classes |
|--------|---------|---------|-------------|
| Topic And Planning | `com.novelfactory.topic` | Discover topics, analyze competitors, select project direction, and bootstrap new book proposals | `MarketScannerService`, `CompetitorAnalysisService`, `TopicSelectionService` |
| Book Management | `com.novelfactory.book` | Manage books, topics, world settings, and lifecycle state | `BookController`, `BookService`, `BookRepository` |
| Creative Assets | `com.novelfactory.creative` | Generate and manage world settings, character profiles, addiction canvas, and static knowledge assets | `WorldService`, `CharacterService`, `AddictionDesignService` |
| Outline Management | `com.novelfactory.outline` | Maintain arc outlines and chapter scripts | `OutlineController`, `ArcService`, `ChapterScriptService` |
| Chapter Production | `com.novelfactory.chapter` | Generate chapter drafts and coordinate retries | `ChapterGenerationController`, `ChapterGenerationService`, `GenerationTaskRunner` |
| Review and Approval | `com.novelfactory.review` | Support operator review and revisions before publish | `ReviewController`, `ReviewService`, `RevisionService` |
| Publishing | `com.novelfactory.publish` | Build publish plans and submit chapters to target platforms | `PublishingController`, `PublishingService`, `PlatformClient` |
| Metrics and Strategy | `com.novelfactory.metrics` | Import platform data and compute keep/optimize/suspend decisions | `MetricsIngestionService`, `PerformanceAnalysisService`, `StrategyService` |
| Portfolio Management | `com.novelfactory.portfolio` | Coordinate multi-book prioritization, lifecycle transitions, and resource allocation decisions | `PortfolioService`, `LifecyclePolicyService`, `ResourceAllocationService` |
| Agent Runtime | `com.novelfactory.agent` | Provide unified Agent execution, routing, prompt assembly, execution logs, and fallback handling | `AgentOrchestrator`, `AgentRoutingService`, `AgentExecutionLogService` |
| Memory System | `com.novelfactory.memory` | Maintain Layer1 static knowledge, Layer2 dynamic summaries, and Layer3 recent chapter context | `StaticKnowledgeService`, `DynamicSummaryService`, `ContextAssemblyService` |
| Platform Bridge | `com.novelfactory.platform` | Coordinate browser-plugin interactions for publishing assistance and metrics collection | `PlatformBridgeService`, `PublishQueueService`, `MetricsCollectionService` |
| Common Infrastructure | `com.novelfactory.common` | Shared response, persistence, queue, and configuration utilities | `BaseEntity`, `BaseResponse`, `JobConfig` |

## Module Dependencies
The planned dependency flow is centered on the full book lifecycle and the upstream business architecture:

- `topic` proposes directions and bootstraps `book`
- `book` owns core metadata and lifecycle state
- `creative` produces foundational assets consumed by `outline`
- `outline` provides structured context to `chapter`
- `memory` assembles long-context inputs for `chapter` and review-related flows
- `chapter` hands drafts to `review`
- `review` either approves content for `publish` or routes revision work back upstream
- `publish` and `platform` cooperate to deliver chapters and collect external results
- `metrics` evaluates outcomes and emits strategy signals
- `portfolio` converts those signals into multi-book resource decisions
- `agent` is a cross-cutting runtime capability used by `topic`, `creative`, `outline`, `chapter`, `review`, `metrics`, and `portfolio`

This means the intended system shape is a coordinated production loop, not a one-way content generator.

## Agent-To-Module Mapping
The long-term Agent blueprint should map into modules roughly as follows:

- `A*` market Agents -> `topic`, `book`, `portfolio`
- `B1/B2/B3/B4/B5/B6/B7/B8/B9/B10` -> `creative`, `outline`, `chapter`, `review`, `memory`
- `C1/C2/C3` -> `metrics`, `portfolio`, `book`
- `D1` -> `publish`, `platform`
- `E1/F1/F2/S*/R*/SR*` -> cross-cutting support for `agent`, `memory`, `review`, `metrics`, and `chapter`

Current code does not need to expose these as separate deployable services yet, but module boundaries should leave room for these responsibilities.

## Data Model
| Entity | Table | Key Fields |
|--------|-------|------------|
| `Book` | `book` | `id`, `title`, `genre`, `platform`, `status`, `daily_word_target` |
| `BookWorldSetting` | `book_world_setting` | `id`, `book_id`, `setting_type`, `content`, `version` |
| `ArcOutline` | `arc_outline` | `id`, `book_id`, `name`, `chapter_start`, `chapter_end`, `conflict`, `hook` |
| `ChapterScript` | `chapter_script` | `id`, `book_id`, `chapter_no`, `major_events`, `payoff_point`, `ending_hook` |
| `ChapterDraft` | `chapter_draft` | `id`, `book_id`, `chapter_no`, `content`, `status`, `retry_count` |
| `ReviewRecord` | `review_record` | `id`, `chapter_draft_id`, `decision`, `comment`, `reviewed_at` |
| `PublishTask` | `publish_task` | `id`, `book_id`, `chapter_no`, `scheduled_at`, `published_at`, `status` |
| `BookMetricSnapshot` | `book_metric_snapshot` | `id`, `book_id`, `date`, `retention_rate`, `completion_rate`, `shelf_add_rate` |

Additional long-term entities implied by the upstream documents include:

- `BookPlanCard`
- `BookOutlineDraft`
- `CharacterProfile`
- `StaticKnowledgeEntry`
- `DynamicStorySummary`
- `PortfolioDecision`
- `RevisionRequest`
- `AgentExecutionLog`
- `ReaderSignalSnapshot`

These do not all need to exist in the current iteration, but they are part of the intended target model and should inform subsequent design work.
