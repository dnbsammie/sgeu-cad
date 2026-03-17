## SGEU CAD (Computer-Aided Dispatch) — Technical Architecture

## Scope
Web-based CAD backend that supports:

- **Emergency log**: intake requests with location, severity (low/medium/high), and an estimated initial response time; produce an incident summary and confirm logging.
- **Resource allocation**: compute recommended resources (fire, EMS, police) from severity + location + time; check availability; allow user confirmation/adjustment before dispatch.
- **Resource management**: track real-time status (available/enroute/onscene/unavailable), active personnel, remaining fuel/consumables; support reassignment when depleted or unexpected events occur.
- **Monitoring & reporting**: monitor emergency state transitions; produce end-of-day summary (emergencies attended, resources spent, average response time) and performance evaluation.

## Non-functional goals
- **Scalable**: handle bursts (e.g., multiple incidents) and concurrent dispatch/operator actions.
- **Correctness first**: avoid double-dispatch / overbooking resources; preserve an auditable timeline.
- **Observability**: trace a full incident lifecycle (intake → dispatch → resolve) with metrics and logs.
- **Security**: role-based access; immutable audit events.

## High-level architecture
- **Backend**: Java 21 + Spring Boot (REST + WebSocket), Lombok, JPA, Flyway.
- **Database**: PostgreSQL (transactions for allocation/dispatch), Flyway migrations for schema.
- **Realtime updates**: WebSocket for live incident/resource state to the web UI.
- **Containerization**: Podman for local dev and deployment.

## Core building blocks
- **Command APIs (write paths)**: create emergency, propose allocation, confirm/dispatch, update resource telemetry/status, resolve incident.
- **Query APIs (read paths)**: list incidents by status, resource roster/availability, allocation recommendations, dashboards & summaries.
- **Domain services**: allocation engine, availability checks, reassignment logic, metrics aggregation.
- **Event log / audit**: append-only incident timeline (status changes, dispatch actions, adjustments).

