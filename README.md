# Emberweald

Companion for organizing persistent multiplayer game runs. See [DESIGN.md](./docs/DESIGN.md) for product scope and
architecture.

## Project structure

- [/core](./core) — Shared KMP library (domain and common logic).
- [/app/shared](./app/shared) — Shared Compose UI and platform-specific implementations.
- [/app/desktopApp](./app/desktopApp) — Desktop (JVM) application.
- [/app/webApp](./app/webApp) — Web application compiled to WebAssembly.
- [/server](./server) — Ktor backend application.

## Prerequisites

- No separate Kotlin installation required — the Kotlin Toolchain bootstraps automatically.
- For the server: **PostgreSQL**.

## Build and run

Build the entire project:

```bash
./kotlin build
```

Run individual modules:

```bash
./kotlin run -m desktopApp
./kotlin run -m webApp
./kotlin run -m server
```

Run all tests, or test a single module:

```bash
./kotlin test
./kotlin test -m <module>
```

## Contributing

- Guidelines for contributors: [CONTRIBUTING.md](./CONTRIBUTING.md).
- Product decisions and roadmap: [DESIGN.md](./docs/DESIGN.md).
