# DESIGN.md — Emberweald

> **Same fire. Different worlds.**

## Product

Emberweald is a cozy, self-hostable gaming companion for groups of friends who rotate through persistent multiplayer
playthroughs.

It is not a game launcher, generic hosting panel, replacement mod manager, or session scheduler.

## Domain

### GameRun

A **GameRun** is one persistent shared playthrough of a game, lasting days or weeks, with participants who may join,
leave, or play at different times. It is **not** a single evening's session.

A run has an identity, game, name, participants, and lifecycle. It may reference a dedicated server, a mod profile,
backups, and other resources through the modules that own them.

Initial lifecycle:

`Proposed → Preparing → Active → Archived`

A dormant/paused state may be introduced if actual usage requires it. Operational readiness is separate from lifecycle:
an active run can have an offline server or unsynchronized players.

Runs normally begin with fresh worlds. Reusing an old run's configuration without reusing its save is a useful future
workflow.

### Other concepts

- **Participant:** a person associated with a run; participation can change during its lifetime.
- **Game:** the title being played, independent of any particular run.
- **Resource association:** a reference to independently managed infrastructure or configuration.
- **Readiness:** derived from requirements and observations, never a permanent boolean. Evidence must identify the
  state/revision it verifies and may become stale.
- **Group:** a possible future container for recurring friends and multiple runs; defer until needed.

A run coordinates capabilities; it does not own Pelican's infrastructure model or Knitty's mod-management domain.

## Architecture

**Kotlin Multiplatform + Compose Multiplatform** clients, **Ktor** backend, **PostgreSQL** persistence, and a **modular
monolith** with explicit hexagonal boundaries.

- Web-first experience; desktop and Android are meaningful targets.
- Share KMP domain/application logic, serialization, networking, and Compose UI where practical.
- Platform-specific capabilities live behind adapters.
- Server owns shared run state, authorization, and orchestration.
- Start with statically composed modules. Dynamic third-party plugin loading and remote UI injection are out of scope.
- Prefer narrowly defined capabilities over a single all-purpose integration interface.
- Keep the current Kotlin Toolchain build and repository structure (`app/`, `core/`, `server/`); evolve only when slices
  justify it.

### Module responsibilities

| Module / area       | Responsibility                                                   |
|---------------------|------------------------------------------------------------------|
| Runs (core)         | Playthrough lifecycle, participants, associations, coordination  |
| Servers             | Game-server status, controls, console, backups through providers |
| Modding             | Profiles, plans, deployment, sync status through Knitty          |
| Jobs (when needed)  | Long-running orchestration, progress, recovery, concurrency      |
| Future capabilities | Invitations/Discord, run history, shared objectives, diagnostics |

Modules own their domain logic and persistence. Cross-module workflows orchestrate exposed application contracts rather
than coupling internals.

## Existing systems

### Pelican

A Pelican host remains the game-server control plane: containers, lifecycle, console, resources, and backups.

Emberweald integrates using Pelican's APIs and WebSocket facilities through a server-side adapter. Client applications
never receive privileged Pelican credentials. Game-specific console commands are not a general-purpose shell execution
mechanism.

### Knitty

`github.com/Naliwe/knitty` remains the mod-management engine.

Existing capabilities include CLI/TUI, portable profiles and lockfiles, planning, transactional sync, local deployment,
and remote Pelican-target synchronization using SFTP and server lifecycle operations. The Linux ↔ Pelican ↔ Windows
multiplayer workflow has been exercised successfully.

Emberweald should expose those capabilities through a programmatic JVM boundary or structured command protocol, chosen
when implementing the integration. Do not duplicate Knitty's resolver or deployment engine. Individual game/provider
support remains limited to what Knitty actually implements.

## Representative workflow

1. Someone proposes a new game run and shares it with friends, initially through a normal Discord link.
2. Participants express interest or join over time.
3. The organizer associates a Pelican server and a Knitty profile as needed.
4. Server and mod configuration are prepared, inspected, and synchronized.
5. Each player's local installation can report readiness against a specific profile revision.
6. The run remains active while players rotate in and out; changes may invalidate earlier readiness.
7. The run is archived when finished or abandoned; configuration and relevant history can be retained for future
   reference.

The experience must remain usable for unmodded games, manually managed servers, and players without the desktop
companion. Readiness observations should distinguish verified state from self-reported state.

## Delivery slices

1. **Foundation:** KMP clients, Ktor backend, basic run creation/list/detail, persistence, and minimal identity/access
   appropriate for a small friend group.
2. **Pelican:** associate an existing server; show status; support authorized power actions.
3. **Live operations:** console and resource streaming, connection lifecycle, operational feedback.
4. **Knitty:** inspect profiles and change plans; trigger controlled synchronization; show job progress and outcomes.
5. **Group readiness:** publish a profile revision; track server/client observations and stale installations; support
   self-service local sync.
6. **Optional additions:** Discord automation, shared objectives, run history, diagnostics, backups and reproducible run
   templates.

Each slice must work end to end and be useful without completing later slices. Introduce a second real module early
enough to validate module boundaries.

## Deferred decisions

- Authentication mechanism and invitation/account onboarding.
- Exact common UI split among web, desktop, and Android.
- Knitty library API versus structured CLI/runner integration.
- Generic Groups, game-specific adapters, runtime plugin loading, and extension discovery.
- Run cloning, world backup/restore, and archival retention policy.
- Event delivery and job persistence mechanisms beyond demonstrated needs.

## Non-goals

- Replacing Pelican or Knitty.
- Building a general-purpose game hosting service.
- Designing a plugin marketplace before there are real extension needs.
- Requiring every game or player to support automated readiness checks.
- Building infrastructure before a user-facing vertical slice needs it.
