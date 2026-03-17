## Data model (entities, patterns, relationships)

## Main entities

### `Emergency` (log/intake)
- **Key fields**
  - `id` (UUID)
  - `type` (enum; e.g., FIRE, MEDICAL, POLICE)
  - `severity` (enum: LOW, MEDIUM, HIGH)
  - `location` (value object: lat/lon or address + zone)
  - `reportedAt` (Instant)
  - `estimatedInitialResponseMinutes` (int)
  - `notes` (text)
- **Relationships**
  - 1 → 1 `Incident` (created when confirmed/accepted)

### `Incident` (operational lifecycle)
- **Key fields**
  - `id` (UUID)
  - `emergencyId` (UUID)
  - `status` (enum: LOGGED, TRIAGED, ALLOCATION_PROPOSED, DISPATCHED, ONGOING, RESOLVED, CANCELLED)
  - `createdAt`, `resolvedAt`
  - `priorityScore` (computed)
- **Relationships**
  - 1 → N `Dispatch` (can be multiple waves)
  - 1 → N `IncidentEvent` (append-only timeline)

### `Resource` (abstract: anything dispatchable)
Model resources as a **Unit** with personnel + vehicle/asset + capabilities.

#### `Unit`
- **Key fields**
  - `id` (UUID)
  - `agency` (enum: FIRE, EMS, POLICE)
  - `callsign` (string)
  - `homeStationId` (UUID)
  - `status` (enum: AVAILABLE, ENROUTE, ONSCENE, UNAVAILABLE, OUT_OF_SERVICE)
  - `activePersonnel` (int)
  - `fuelPercent` (0..100)
  - `capabilities` (set; e.g., ALS, WATER_PUMP, K9)
  - `lastKnownLocation` (Location)
  - `availableFrom` (Instant; next availability)
- **Relationships**
  - N ↔ N `Incident` via `Assignment`

#### `Station`
- **Key fields**: `id`, `name`, `location`, `coverageZone`
- **Relationships**: 1 → N `Unit`

### `Assignment` (join: incident ↔ unit)
- **Key fields**
  - `id` (UUID)
  - `incidentId`, `unitId`
  - `role` (enum: PRIMARY, SUPPORT)
  - `assignedAt`, `releasedAt`
  - `status` (enum: PROPOSED, CONFIRMED, DISPATCHED, ACTIVE, RELEASED, CANCELLED)

### `Dispatch` (a confirmed dispatch action / wave)
- **Key fields**
  - `id` (UUID)
  - `incidentId`
  - `confirmedBy` (user id)
  - `confirmedAt`
  - `etaMinutes` (int; aggregated)
- **Relationships**
  - 1 → N `Assignment` (confirmed in that wave)

### `IncidentEvent` (audit timeline)
- **Key fields**
  - `id` (UUID)
  - `incidentId`
  - `type` (enum: LOGGED, SEVERITY_CHANGED, ALLOCATION_ADJUSTED, DISPATCH_CONFIRMED, UNIT_STATUS_CHANGED, RESOLVED, ...)
  - `at` (Instant)
  - `actor` (user/system)
  - `payload` (JSON) (what changed; immutable)

## Reporting model (end-of-day)

### `DailySummary`
- **Key fields**
  - `date`
  - `incidentsTotal`
  - `incidentsResolved`
  - `avgResponseTimeSeconds`
  - `resourceHoursByAgency`
  - `fuelSpentByAgency`

Implementation note: either materialize nightly (batch) or compute on demand from `IncidentEvent` + `Assignment`.

## Design patterns (where to use them)

### Factory — `EmergencyFactory`
Creates domain instances from intake DTOs and type-specific defaults/validation.
- Inputs: `EmergencyRequest` (location, severity, type, notes…)
- Output: `Emergency` + optionally initial `Incident` scaffold

### Singleton — `AllocationPolicyRegistry` (Spring-managed singleton)
Registry that holds active strategies/config by emergency type/agency.
- In Spring, singleton scope is default; keep it stateless + config-driven.

### Strategy — allocation & prioritization
- `AllocationStrategy`: given incident + available units, returns **recommended** unit set (proposed assignments).
  - Implementations by `EmergencyType` and/or `Severity` (e.g., FIRE_HIGH vs MEDICAL_LOW).
- `PrioritizationStrategy`: computes incident priority ordering for operator dashboard.

## Key relationships (summary)
- `Emergency` 1—1 `Incident`
- `Incident` 1—N `Dispatch`
- `Incident` 1—N `IncidentEvent`
- `Incident` N—N `Unit` via `Assignment`
- `Station` 1—N `Unit`

