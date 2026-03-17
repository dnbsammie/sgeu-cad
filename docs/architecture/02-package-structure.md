## Project package structure

Repository layout (after refactor):

- `backend/`: Spring Boot backend (Maven)
- `docs/`: architecture + flowcharts + decisions
- `scripts/`: helper scripts (optional)

Recommended Java package structure (hexagonal-ish, Spring-friendly):

- `com.sgeu.cad`
  - `Application` (Spring Boot entrypoint)
  - `config/`
    - Web MVC, Jackson, OpenAPI (optional), WebSocket/STOMP config, security config
  - `api/`
    - `rest/`
      - Controllers + request/response DTOs
    - `ws/`
      - WebSocket message endpoints and payload DTOs
  - `application/` (use-cases)
    - `command/` (write use-cases)
      - `LogEmergencyCommand`, `ProposeAllocationCommand`, `ConfirmDispatchCommand`, ...
    - `query/` (read use-cases)
      - `GetIncidentSummaryQuery`, `ListAvailableResourcesQuery`, `EndOfDayReportQuery`, ...
  - `domain/` (pure domain model)
    - `model/` (entities + value objects)
      - `Emergency`, `Incident`, `Resource`, `Unit`, `Station`, `Dispatch`, `Assignment`, `Telemetry`, ...
    - `policy/` (strategies)
      - `AllocationStrategy`, `PrioritizationStrategy`, `ReassignmentPolicy`
    - `factory/`
      - `EmergencyFactory` (create domain objects by type)
    - `events/`
      - `DomainEvent` (incident timeline events)
    - `services/`
      - `AllocationService`, `AvailabilityService`, `IncidentLifecycleService`
  - `infrastructure/` (adapters)
    - `persistence/`
      - JPA entities (if separate from domain), repositories, mappers
    - `messaging/`
      - WebSocket publishers, async outbox (future)
    - `time/`
      - clock abstraction
  - `shared/`
    - `errors/`, `validation/`, `util/`

Notes:
- **DTOs vs domain**: keep REST/WS DTOs separate from `domain.model` to avoid leaking persistence/web concerns.
- **Transactions**: application commands are transactional; allocation/dispatch must be atomic.

