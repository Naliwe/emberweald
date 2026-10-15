# Contributing to Emberweald

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

## Architecture boundaries

- **Domain:** rules, identities, value objects, invariants, and pure decisions. No framework types.
- **Application:** use cases and orchestration through ports. No Ktor routes, Compose UI, SQL, or
  Pelican/Knitty-specific DTOs.
- **Driving adapters:** HTTP endpoints and client UI translate requests, display results, and manage presentation state;
  they do not own business policy.
- **Driven adapters:** persistence, Pelican, Knitty, Discord, and other integrations implement narrow application ports.
- Modules own their models and persistence. Communicate through explicit contracts; avoid reaching into another module's
  tables or internals.

## Multiplatform

- Share domain logic, application behavior, serialization contracts, networking, and UI where sharing remains natural.
- Use Compose Multiplatform for shared UI, with platform-specific adapters for native capabilities.
- Keep common code free of JVM, Android, and browser-specific dependencies.

## Integrations

- Pelican remains responsible for game-server infrastructure. Integrate through its APIs; do not couple to its
  internals.
- Knitty remains responsible for mod management. Reuse its existing capabilities; do not reimplement its engine.
- Keep credentials server-side or in appropriately secured local stores.
- Model long-running and destructive operations as explicit jobs with progress, authorization, and cancellation support.

## Tests and delivery

- Build thin end-to-end slices that are useful on their own.
- Start with the smallest real implementation; extract generalizations only after a second use case creates pressure.
- Test domain rules and application use cases with fakes for ports.
- Run the full build and test suite before submitting changes:

```bash
./kotlin build
./kotlin test
```

## Documentation

- `DESIGN.md` is the single source for product scope, domain language, architecture, and roadmap.
- `AGENTS.md` covers implementation conventions for automated agents.
- Update existing documents rather than creating overlapping notes.
- Keep documentation concise, factual, and decision-oriented.
