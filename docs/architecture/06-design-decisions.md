## Design decisions (ADR-style)

## DD-001: PostgreSQL + Flyway as the system of record
- **Decision**: Use PostgreSQL for operational state and reporting aggregates; manage schema via Flyway migrations.
- **Why**: strong transactional guarantees for dispatch confirmation + mature operational tooling.
- **Consequences**: allocation/dispatch logic must be transaction-aware; long-running reports should be optimized (indexes/rollups).

## DD-002: Append-only incident timeline (`IncidentEvent`)
- **Decision**: Persist incident lifecycle and key changes as immutable events (who/what/when).
- **Why**: auditability, troubleshooting, and accurate end-of-day metrics.
- **Consequences**: storage grows; requires retention/partition strategy for telemetry-heavy data.

## DD-003: “Propose then confirm” allocation workflow
- **Decision**: allocation engine produces a **proposal**; only a confirmation transaction creates a `Dispatch` and active `Assignments`.
- **Why**: supports operator adjustment and avoids optimistic assumptions becoming authoritative.
- **Consequences**: must validate proposal freshness; UI needs clear diffs when confirm fails.

## DD-004: Strategy pattern for allocation and prioritization
- **Decision**: encapsulate allocation and prioritization rules behind interfaces and select by emergency type/severity/agency.
- **Why**: rules evolve; enables configuration and A/B of policies without rewriting orchestration.
- **Consequences**: needs a registry (Spring singleton) and strong test coverage of each strategy.

## DD-005: WebSocket for realtime state propagation
- **Decision**: push incident/unit state changes to clients via WebSocket; REST remains source for reconciliation.
- **Why**: operator dashboards require low-latency updates.
- **Consequences**: messages must be idempotent and versioned; clients should reconcile periodically.

## DD-006: Podman as the default local runtime
- **Decision**: ship a `compose.yaml` that runs PostgreSQL with Podman.
- **Why**: dev/prod parity and quick onboarding.
- **Consequences**: document ports/credentials; avoid hardcoding secrets for real deployments.

