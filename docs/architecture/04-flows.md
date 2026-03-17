## Flowcharts (key workflows)

## 1) Emergency intake → allocation → dispatch

```mermaid
flowchart TD
  A[Operator creates Emergency Log] --> B[Validate details\nlocation, severity, type, ETA]
  B --> C[Build incident summary]
  C --> D{Confirm log?}
  D -- No --> A
  D -- Yes --> E[Persist Emergency + Incident\nstatus=LOGGED]
  E --> F[Compute priority score]
  F --> G[Availability check\nby time + location + status]
  G --> H[AllocationStrategy returns\nProposed Assignments]
  H --> I[UI shows proposal\nand unit states]
  I --> J{Operator adjusts?}
  J -- Yes --> K[Recalculate + validate\n(no double-booking)]
  K --> I
  J -- No --> L[Confirm dispatch]
  L --> M[Transactional commit:\nDispatch + Assignments\nIncident status=DISPATCHED]
  M --> N[Publish realtime update\n(WebSocket)]
  N --> O[Units update status\nenroute/onscene]
```

## 2) Resource telemetry/status update

```mermaid
sequenceDiagram
  autonumber
  participant Unit as Unit device/system
  participant API as Backend API
  participant DB as PostgreSQL
  participant WS as WebSocket Hub
  Unit->>API: POST /units/{id}/telemetry (fuel, location, status)
  API->>DB: TX: persist telemetry + update unit status
  API->>DB: append IncidentEvent (if status changed)
  API-->>WS: broadcast unit update + affected incident changes
```

## 3) End-of-day summary

```mermaid
flowchart TD
  A[End of day trigger] --> B{Compute on demand\nor materialize?}
  B -- On demand --> C[Aggregate from IncidentEvent + Assignment]
  B -- Materialize --> D[Batch job writes DailySummary table]
  C --> E[Return metrics to UI]
  D --> E
```

