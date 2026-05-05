# Novel Factory - System Design

## Technology Stack
| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | TBD |
| Framework | Spring Boot | TBD |
| Build | Maven | TBD |
| Database | MySQL or PostgreSQL | TBD |
| AI Integration | LLM API provider | TBD |

## Architecture Pattern
Planned layered architecture for a single deployable service. The product documents describe a work-focused backend that coordinates domain workflows, asynchronous agent jobs, manual review checkpoints, and platform publishing operations.

## Layer Structure
`Controller -> Application Service -> Domain Service / Agent Orchestrator -> Repository / External Client`

Expected supporting components:

- Scheduler for daily chapter-generation and publishing plans
- Queue or job runner for long-running AI tasks
- Platform adapter for novel-site publishing and metrics ingestion
- Prompt and memory management for book, arc, and chapter context

## Key Base Classes
No concrete base classes exist in the repository yet. Recommended shared abstractions once implementation starts:

| Class | Package | Purpose | Subclasses Must |
|-------|---------|---------|----------------|
| `BaseEntity` | `com.novelfactory.common.entity` | Common persistence fields such as id, timestamps, and status | Define domain fields and mapping metadata |
| `BaseResponse` | `com.novelfactory.common.api` | Unified API response wrapper | Supply typed payload and error details |
| `BaseAgentTask` | `com.novelfactory.agent.task` | Shared task execution lifecycle for AI jobs | Implement prompt assembly and result handling |

## Public Components
Planned public components inferred from the PRD and business architecture:

| Component | Package | Purpose |
|-----------|---------|---------|
| `BookService` | `com.novelfactory.book` | Manage books, lifecycle state, and portfolio metadata |
| `OutlineService` | `com.novelfactory.outline` | Maintain arc-level and chapter-level outlines |
| `ChapterGenerationService` | `com.novelfactory.chapter` | Trigger chapter drafting, retries, and progress tracking |
| `ReviewService` | `com.novelfactory.review` | Support operator review, reject, revise, and approve flows |
| `PublishingService` | `com.novelfactory.publish` | Coordinate publish plans and platform-side submission |
| `MetricsService` | `com.novelfactory.metrics` | Ingest retention, completion, and engagement signals |

## Configuration
- Config location: `src/main/resources/application.yml` or `application-{profile}.yml`
- Profiles: expected `dev`, `test`, and `prod`
- Database: relational database connection with environment-specific credentials
- External integrations: LLM provider credentials, publishing automation credentials, scheduler settings, and notification channels
