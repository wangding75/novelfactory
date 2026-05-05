# Novel Factory - System Design

## Project Introduction
Novel Factory is an operator-facing novel production and publishing backend. Its long-term product goal is to turn online-fiction creation into an industrialized pipeline: run multiple books in parallel, use AI to generate and revise content, publish through platform tooling, collect reader data, and continuously reallocate resources based on retention and completion metrics.

This project has two authoritative upstream reference documents:

- `novel_factory_prd.md`: product vision, phase boundaries, feature scope, and non-functional goals
- `novel_factory_business_architecture.md`: business layers, end-to-end workflow, memory system, Agent responsibilities, and long-term operating model

All future iterations should treat those two documents as the final target state. The documents under `.cube/config/` are working summaries for iteration execution and should stay aligned with that target, while allowing the current iteration to implement only a subset of the final system.

## Product Goal And Delivery Boundary
The final target is not a reader-facing app. It is a production operations system for managing the full lifecycle of web novels:

- topic and project initiation
- worldbuilding and character design
- outline planning and chapter scripting
- AI chapter generation and review
- publishing assistance and platform integration
- metrics ingestion, strategy decisions, and portfolio management

Current iterations may only implement a thin vertical slice of this blueprint, but architectural decisions should preserve the path toward:

- multi-book parallel production
- an Agent-driven workflow with unified LLM routing
- a three-layer memory system for long-form coherence
- platform-side publishing and data collection through plugin/bridge integration
- strategy automation based on retention, completion, and growth signals

## Technology Stack
| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | TBD |
| Framework | Spring Boot | TBD |
| Build | Maven | TBD |
| Database | MySQL or PostgreSQL | TBD |
| AI Integration | LLM API provider | TBD |

## Architecture Pattern
Planned layered architecture for a single deployable service. The long-term design is an operator workflow backend that coordinates domain workflows, asynchronous Agent jobs, manual review checkpoints, platform publishing operations, and post-publication data feedback loops.

## Layer Structure
`Controller -> Application Service -> Domain Service / Agent Orchestrator -> Repository / External Client`

Expected supporting components:

- Scheduler for daily chapter-generation and publishing plans
- Queue or job runner for long-running AI tasks
- Platform adapter for novel-site publishing and metrics ingestion
- Prompt and memory management for book, arc, and chapter context

Long-term business layers from the architecture blueprint:

- External Platform Layer: Tomato Novel and Qimao author consoles as publishing and metrics sources
- Operations Access Layer: browser plugin for publish assist and data collection
- Business Service Layer: portfolio management, book lifecycle management, publishing queue, analytics, and decision execution
- Agent Execution Layer: market research, creative generation, review, publishing strategy, analytics, and optimization agents
- LLM Routing Layer: unified model gateway with system-level, agent-level, and task-level routing

## End-To-End Business Flow
The target operating flow is:

`Market research -> Topic selection -> Addiction design -> Worldbuilding -> Character design -> Outline planning -> Chapter scripting -> Chapter writing -> Review -> Summary update -> Publish planning -> Platform publishing -> Metrics ingestion -> Strategy decision -> Optimization / restart`

This flow is described in detail in `novel_factory_business_architecture.md`. Iteration-level design should map any new feature to this lifecycle rather than treating modules as isolated tools.

## Agent Blueprint
The final target architecture includes a broad Agent system rather than a single-purpose generator. According to `novel_factory_business_architecture.md`, the current blueprint contains 32 Agents in total, with 18 marked as MVP and 14 deferred.

Representative groups:

- Market Intelligence: `A1 MarketScanner`, `A2 CompetitorAnalyst`, `A3 TopicSelector`, `A4 KnowledgeDistillAgent`
- Creative Production: `B1 WorldAgent`, `B2 CharacterAgent`, `B3 OutlineAgent`, `B4 PlotAgent`, `B5 WritingAgent`, `B6 ReviewAgent`, `B7 SummaryAgent`
- Quality And Repair: `B8 ConsistencyAgent`, `B9 PlotAdjustAgent`, `B10 OpeningOptimizeAgent`, `R1 RevisionRouter`, `R2 ConflictResolver`
- Operations Strategy: `C1 PerformanceAnalyst`, `C2 StrategyAgent`, `C3 RestartAgent`, `D1 PublishStrategyAgent`
- Optimization And Signal Systems: `E1 MetaEvaluatorAgent`, `F1 AddictionDesignerAgent`, `F2 EmotionDebtTracker`, `S1 ChapterHealthScorer`, `S2 DropoutDetector`, `S3 SignalDispatcher`, `SR1 ReaderPersonaAgent`, `SR2 ReaderSimulatorAgent`, `SR3 ReaderCalibrator`

Current code may temporarily expose only a unified `AgentOrchestrator` or a narrow subset of these responsibilities. That does not change the target architecture recorded here.

## Memory And Control Model
The long-term content-generation design depends on:

- Layer1 static knowledge: world rules, settings, character archives, forbidden items
- Layer2 dynamic summary: active suspense, character state, arc progress, previous-chapter hook
- Layer3 recent text window: recent chapter bodies for style and continuity

It also assumes:

- unified LLM routing with fallback
- task execution records for debugging and replay
- revision requests between Agents instead of hard coupling
- optional multi-version generation and voting for quality stability

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

These components should be understood as business-facing service surfaces. Over time they are expected to coordinate with the fuller Agent system and platform/plugin integrations defined in the upstream product and architecture documents, not remain as isolated CRUD modules.

## Configuration
- Config location: `src/main/resources/application.yml` or `application-{profile}.yml`
- Profiles: expected `dev`, `test`, and `prod`
- Database: relational database connection with environment-specific credentials
- External integrations: LLM provider credentials, publishing automation credentials, scheduler settings, and notification channels
