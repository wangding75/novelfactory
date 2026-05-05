# Novel Factory - Module Design

## Module List
| Module | Package | Purpose | Key Classes |
|--------|---------|---------|-------------|
| Book Management | `com.novelfactory.book` | Manage books, topics, world settings, and lifecycle state | `BookController`, `BookService`, `BookRepository` |
| Outline Management | `com.novelfactory.outline` | Maintain arc outlines and chapter scripts | `OutlineController`, `ArcService`, `ChapterScriptService` |
| Chapter Production | `com.novelfactory.chapter` | Generate chapter drafts and coordinate retries | `ChapterGenerationController`, `ChapterGenerationService`, `GenerationTaskRunner` |
| Review and Approval | `com.novelfactory.review` | Support operator review and revisions before publish | `ReviewController`, `ReviewService`, `RevisionService` |
| Publishing | `com.novelfactory.publish` | Build publish plans and submit chapters to target platforms | `PublishingController`, `PublishingService`, `PlatformClient` |
| Metrics and Strategy | `com.novelfactory.metrics` | Import platform data and compute keep/optimize/suspend decisions | `MetricsIngestionService`, `PerformanceAnalysisService`, `StrategyService` |
| Common Infrastructure | `com.novelfactory.common` | Shared response, persistence, queue, and configuration utilities | `BaseEntity`, `BaseResponse`, `JobConfig` |

## Module Dependencies
The planned dependency flow is centered on the book lifecycle:

- `book` owns core book metadata and feeds `outline`
- `outline` provides structured context to `chapter`
- `chapter` hands drafts to `review`
- `review` passes approved chapters to `publish`
- `publish` and platform adapters emit operational results to `metrics`
- `metrics` informs strategy decisions that update `book`, `outline`, and publish frequency

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
