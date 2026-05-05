# Novel Factory - Deployment

## Current State
No deployable application artifacts exist in the repository yet, and no `Dockerfile`, CI pipeline, or infrastructure manifests were found.

## Initial Deployment Recommendation
- Package the Java service as a single Spring Boot application
- Externalize database, queue, and secret configuration through environment variables
- Separate worker execution for long-running generation and publishing jobs if queue load grows

## Build and Release
- Build command: `mvn clean package`
- Artifact: executable JAR
- Containerization: TBD
- CI/CD: TBD

## Runtime Dependencies
- Relational database
- LLM API provider
- Scheduling/queue mechanism
- Notification channel for failures and manual review prompts

## Open Items
- Choose deployment target: VM, container platform, or managed PaaS
- Define production database engine and backup policy
- Define publishing automation runtime and browser/tooling requirements
