# AGENTS.md — Emberweald

## Mission

Build Emberweald: a cozy, self-hostable companion for organizing persistent multiplayer game runs and reducing setup
friction.

Optimize for a useful vertical slice, clear boundaries, and maintainable Kotlin. `DESIGN.md` owns product and
architectural decisions; do not duplicate it here.

## Engineering principles

- Write idiomatic, modern Kotlin. Prefer the Kotlin standard library and Kotlinx libraries.
- Favor explicit, data-driven models and behavior over condition-heavy, game-specific branches.
- Use domain-driven design where it clarifies ownership and invariants; do not manufacture aggregates or patterns.
- Apply hexagonal architecture at meaningful boundaries: domain and application logic must not depend on transport, UI,
  persistence, or external providers.
- Prefer composition, small interfaces, constructor injection, immutable data, and pure transformations.
- Keep code self-documenting: precise names, short functions, readable whitespace, and one responsibility per type.
- No explanatory prose or narrating comments in source code. Document non-obvious external constraints only when code
  cannot express them.
- Avoid generic `Manager`, `Helper`, `Util`, service locators, global mutable state, reflection-driven frameworks, and
  speculative abstractions.
- Do not introduce indirection merely to satisfy an architectural diagram.

## Kotlin conventions

- Prefer `data class`, `data object`, `value class`, and sealed hierarchies when they express real domain distinctions.
- Model expected failures explicitly; exceptions are for exceptional conditions and adapter failures.
- Use exhaustive `when` expressions without redundant `else` branches.
- Use coroutines and `Flow` for asynchronous and streaming operations; preserve cancellation.
- Prefer `kotlinx.serialization` for contracts and `kotlinx.datetime` / `kotlin.time` for time modeling.
- Avoid leaking JVM-only APIs into common KMP source sets.
- Keep functions visually aerated: separate validation, transformation, effects, and result construction with blank
  lines.
- Prefer named intermediate values to deeply nested expressions or dense chains.
- Use one logical element per line when it improves scanning; avoid clever one-liners and gratuitous comments.
- Follow the formatter and neighboring conventions. Fix warnings rather than suppressing them.

## Boundaries and ownership

- **Domain:** rules, identities, value objects, invariants, and pure decisions. No framework types.
- **Application:** use cases and orchestration through ports. No Ktor routes, Compose UI, SQL, or
  Pelican/Knitty-specific DTOs.
- **Driving adapters:** HTTP endpoints and client UI translate requests, display results, and manage presentation state;
  they do not own business policy.
- **Driven adapters:** persistence, Pelican, Knitty, Discord, and other integrations implement narrow application ports.
- Modules own their models and persistence. Communicate through explicit contracts; avoid reaching into another module's
  tables or internals.
- Keep infrastructure resources independent from game runs. A run references resources; it does not redefine their
  lifecycle.
- Start with compile-time module composition in a modular monolith. Runtime plugin loading is not a requirement.
- Introduce capability contracts when two real implementations or consumers justify them, not in anticipation of
  hypothetical plugins.

## Multiplatform

- Share domain logic, application behavior, serialization contracts, networking, and UI where sharing remains natural.
- Use Compose Multiplatform for shared UI, with platform-specific adapters for filesystem, process execution,
  notifications, and other native capabilities.
- Keep common code free of JVM, Android, and browser-specific dependencies.
- Do not force identical interaction patterns on desktop, mobile, and web when their ergonomics differ.
- Treat the backend as authoritative for shared run state and permissions. Client-local state and observations must be
  explicit.

## Integrations

- Pelican remains responsible for game-server infrastructure. Integrate through its APIs and WebSocket facilities; do
  not couple to its PHP/Laravel internals.
- Knitty remains responsible for mod planning, profiles, lockfiles, reconciliation, and deployment. Reuse its existing
  capabilities; do not reimplement its engine.
- Choose an integration boundary based on deployment needs: an application-level JVM API or structured CLI protocol. Do
  not parse human-oriented terminal output.
- Keep credentials server-side or in appropriately secured local stores. Never publish Pelican credentials to web
  clients.
- Model long-running and destructive operations as explicit jobs with progress, authorization, cancellation policy, and
  per-resource concurrency control.
- Prefer plan → inspect → confirm → apply for meaningful mutations. Handle partial failure and recovery explicitly.

## Delivery and tests

- Build thin end-to-end slices that are useful on their own.
- Start with the smallest real implementation; extract generalizations only after a second use case creates pressure.
- Test domain rules and application use cases with fakes for ports. Add adapter contract and integration tests for real
  infrastructure boundaries.
- Cover serialization, authorization, state transitions, idempotency, failure recovery, and concurrent mutations where
  relevant.
- Keep live network/infrastructure tests separate from fast deterministic tests.
- Run the project's existing build, formatting, and test commands before considering work complete.
- Do not change the build system or add dependencies without a concrete need. Use the repository's Kotlin Toolchain
  setup.

## Documentation discipline

- `DESIGN.md` is the single source for product scope, domain language, architecture, and roadmap.
- `AGENTS.md` is the single source for implementation conventions.
- Update an existing document rather than creating overlapping design notes.
- Keep documentation concise, factual, and decision-oriented. No speculative prose, repeated explanations, or
  implementation diaries.
- Keep domain names explicit in code (`GameRun`, `ModProfile`, `ServerProvider`); reserve cozy thematic language for
  product presentation.

## Before implementing

1. Identify the user-visible outcome and the owning module.
2. Define the smallest domain model and application port that support it.
3. Keep external systems behind adapters and mutations behind explicit plans when appropriate.
4. Implement and test the vertical slice across relevant targets.
5. Remove speculative abstractions and update `DESIGN.md` only if a real decision changes.
