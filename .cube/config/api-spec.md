# Novel Factory - API Specification

## API Style
Planned RESTful API for operator-facing administration, with asynchronous job endpoints for generation and publishing workflows.

## Base URL Pattern
Recommended base path: `/api/v1/{module}`

Representative resource groups:

- `/api/v1/books`
- `/api/v1/outlines`
- `/api/v1/chapters`
- `/api/v1/reviews`
- `/api/v1/publishing`
- `/api/v1/metrics`
- `/api/v1/strategy`

## Response Format
Recommended unified response wrapper:

```json
{
  "code": "OK",
  "message": "success",
  "data": {}
}
```

For asynchronous operations, return a task identifier and status summary:

```json
{
  "code": "ACCEPTED",
  "message": "generation started",
  "data": {
    "taskId": "gen_123",
    "status": "QUEUED"
  }
}
```

## Error Code Convention
Recommended error categories:

- `VALIDATION_ERROR`: malformed request or missing required fields
- `NOT_FOUND`: requested book, outline, chapter, or task does not exist
- `CONFLICT`: invalid lifecycle transition, such as publishing an unapproved chapter
- `EXTERNAL_PLATFORM_ERROR`: publish or metrics sync failure
- `LLM_GENERATION_ERROR`: AI generation failure after retries
- `INTERNAL_ERROR`: unexpected server-side failure

## Authentication
Not specified in the current documents. For the operator-only Phase 1 scope, a simple authenticated admin session or token-based internal API is sufficient. Production hardening should add audit logging, role separation for automation credentials, and secret isolation for platform and LLM integrations.
