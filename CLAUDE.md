# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Test Automation Framework (TAF) for [InvenTree](https://demo.inventree.org/) — an open-source inventory management product. The framework targets the **Parts** domain via both UI and API automation, driven by an AI-assisted agentic workflow.

- InvenTree docs: https://docs.inventree.org/en/stable/part/#part-attributes
- InvenTree API docs: https://docs.inventree.org/en/stable/api/

## Build Commands

### API (Java/Maven) — runs from repo root

```bash
mvn clean install        # Clean build
mvn test                 # Run all tests via testng.xml
mvn package              # Package
# Run a single test class:
mvn test -Dtest=PartApiTest
```

Java version: JDK 21 (Amazon Corretto 21). CheckStyle is configured with Google/Sun profiles (v13.4.0). Tests are wired through `src/test/resources/testng.xml`.

### UI (TypeScript/Playwright) — runs from `ui/`

```bash
cd ui
npm install
npm test                       # Run all tests (headless)
npm run test:headed            # Run with browser visible
npm run test:debug             # Run with Playwright Inspector
npm run test:report            # Open last HTML report
npm run lint                   # ESLint
npx playwright test tests/ui/parts/part-creation.spec.ts   # Single spec file
```

**Authentication setup** — credentials are read from environment variables. Copy `.env.example` to `.env` and populate before running:

```
USER_ALLACCESS_USERNAME=...
USER_ALLACCESS_PASSWORD=...
USER_READER_USERNAME=...
USER_READER_PASSWORD=...
USER_ENGINEER_USERNAME=...
USER_ENGINEER_PASSWORD=...
USER_ADMIN_USERNAME=...
USER_ADMIN_PASSWORD=...
```

The `setup` project (`tests/setup/auth.setup.ts`) runs first and writes session files to `ui/.auth/<role>.json`. These are referenced by tests via `STORAGE_STATE` from `ui/config/storageState.ts`.

## Architecture

The project follows a three-phase agentic workflow:

1. **Documentation phase** — An AI agent reads InvenTree docs and writes `.md` snapshot files to `docs/` to serve as a local documentation database for downstream agents.

2. **Manual test case phase** — A manual tester AI agent (with UI + API testing skills) reads those docs and generates test cases in Markdown format. Each functional area is a separate test suite, mirroring the coverage areas below.

3. **Automation phase** — Two automation agents implement the manual test cases in code, maintaining 1:1 structural traceability:
   - **API automation agent** → Java (Maven, TestNG, REST Assured) under `src/`
   - **UI automation agent** → TypeScript (Playwright) under `ui/`

## UI Framework Structure (`ui/`)

```
ui/
├── framework/
│   ├── core/           # Base classes: BasePage, BaseComponent, BaseTable, BaseTableRow, ElementHolder
│   ├── pages/          # Page Objects (auth/, parts/)
│   └── components/     # Shared components (NavigationBar, parts/)
├── fixtures/index.ts   # Playwright fixture extensions — inject page objects into tests
├── config/
│   ├── storageState.ts # STORAGE_STATE map: role → .auth/<role>.json path
│   └── users.ts        # UserRole type, getUser(), WRITE_ROLES, READ_ONLY_ROLES
├── data/               # Static test data (parts.ts)
├── tests/
│   ├── setup/          # auth.setup.ts — logs in each role, saves session
│   └── ui/parts/       # Spec files, mirroring coverage areas
└── playwright.config.ts
```

**Key patterns:**

- All page objects extend `BasePage` (string or RegExp `url`, `navigate()`, `waitForLoad()`).
- All component objects extend `BaseComponent` (scoped `locator()` via `ElementHolder`).
- Table helpers live in `BaseTable` (rows, cells, column values, empty-state).
- Tests import `{ test, expect }` from `fixtures/index.ts`, **not** from `@playwright/test` directly, to get typed page object fixtures.
- Each `test.describe` block sets its own `test.use({ storageState: STORAGE_STATE.<ROLE> })` — auth is not set globally.

## API Framework Structure (`src/`)

The Java test source does not exist yet (`src/test/` is absent). The only Java files are a Maven Archetype template under `src/main/resources/archetype-resources/` — this is **not** production source code and is excluded from compilation.

Stack: TestNG 7.10.2 · REST Assured 5.5.0 · Allure TestNG 2.32.0 · Jackson 2.18.x · Lombok · Log4j2.

## Coverage Areas

**UI**: Part creation (manual entry and import), Part detail tabs (Stock, BOM, Allocated, Build Orders, Parameters, Variants, Revisions, Attachments, Related Parts, Test Templates), Part categories (hierarchy, filtering, parametric tables), Part attributes (Virtual, Template, Assembly, Component, Trackable, Purchasable, Salable, Active/Inactive), Units of measure, Part revisions, negative/boundary scenarios.

**API**: CRUD on Parts and Part Categories, filtering/pagination/search, field-level validation, relational integrity (category, default location, supplier linkage), edge cases (invalid payloads, unauthorized access, conflicts).
