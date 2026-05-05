# Novel Factory - Operations

## Current State
No monitoring, alerting, or operational runbooks were found in the repository yet.

## Recommended Operational Focus
- Generation queue depth and failure rate
- Chapter approval backlog
- Publishing success rate and platform rejection rate
- Daily per-book retention and shelf-add metrics
- LLM token usage and cost per book

## Alerts
- Generation retries exceeded threshold
- Publishing job failed or stalled
- Metrics import missing for expected reporting window
- API cost spike beyond configured budget

## Runbooks
- Re-run failed generation for a single chapter
- Manually approve, reject, or patch a chapter before publish
- Pause a book after poor retention signals
- Rotate platform and LLM credentials

## Open Items
- Select metrics stack and log aggregation
- Define notification endpoints
- Establish backup and restore procedures
