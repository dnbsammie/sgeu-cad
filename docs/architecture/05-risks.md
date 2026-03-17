## Technical risks & mitigations

## 1) Double-booking / inconsistent allocation under concurrency
- **Risk**: two operators dispatch the same unit concurrently; availability becomes incorrect.
- **Mitigations**
  - Use **transactional** dispatch confirmation with DB constraints.
  - Apply **pessimistic locking** on `Unit` rows during confirm, or optimistic locking with retries.
  - Represent assignments explicitly (`Assignment` with status lifecycle) and enforce uniqueness: one active assignment per unit.

## 2) “Recommended allocation” becomes stale quickly
- **Risk**: proposal computed from old telemetry/status; operator confirms invalid plan.
- **Mitigations**
  - Include `proposalVersion` / `computedAt` and validate freshness on confirm.
  - Re-check availability in confirm transaction; reject with a clear diff.
  - Push realtime unit updates via WebSocket to reduce stale UI state.

## 3) Latency spikes during incident bursts
- **Risk**: allocation computation + dashboard queries overload DB.
- **Mitigations**
  - Separate command/query code paths; add read-optimized indexes.
  - Cache read models (short TTL) for dashboards.
  - Consider async outbox + projections if load grows.

## 4) Auditability gaps
- **Risk**: cannot reconstruct who did what, when, and why.
- **Mitigations**
  - Append-only `IncidentEvent` for all lifecycle changes.
  - Store actor identity and immutable payloads (JSON) for diffs.
  - Enforce “no hard deletes” for core operational data.

## 5) Incorrect ETA/coverage logic
- **Risk**: naive distance/ETA leads to wrong dispatch decisions.
- **Mitigations**
  - Start with zone-based heuristics; validate with real data.
  - Make ETA logic a replaceable `Strategy` with configuration.
  - Add simulation tests with known scenarios.

## 6) WebSocket scalability and delivery guarantees
- **Risk**: many clients; dropped messages; inconsistent UI.
- **Mitigations**
  - Keep WS messages idempotent; include current version/state snapshots.
  - Fall back to polling for reconciliation.
  - If scaling horizontally: add a broker (Redis/Rabbit) later; keep interface stable now.

## 7) Data growth (event log + telemetry)
- **Risk**: tables grow quickly; queries slow.
- **Mitigations**
  - Partition telemetry by date; roll up summaries.
  - Archive old events; retain immutable core incident timeline.
  - Add retention policies and scheduled maintenance.

## 8) Container/dev-prod drift
- **Risk**: “works on my machine” due to mismatched DB/config.
- **Mitigations**
  - Provide Podman compose for local parity.
  - All schema via Flyway; no manual DB changes.
  - Explicit configuration via env vars.

