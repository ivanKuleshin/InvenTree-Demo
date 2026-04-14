# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Test Automation Framework (TAF) for [InvenTree](https://demo.inventree.org/) — an open-source inventory management
product. The framework targets the **Parts** domain via both UI and API automation, driven by an AI-assisted agentic
workflow.

- InvenTree docs: https://docs.inventree.org/en/stable/part/#part-attributes
- InvenTree API docs: https://docs.inventree.org/en/stable/api/

## Build Commands

### Java (API tests)

```bash
mvn clean install     # Clean build
mvn test              # Run tests
mvn package           # Package
```

Java version: JDK 21 (Amazon Corretto 21). CheckStyle is configured with Google/Sun profiles (v13.4.0).

### UI tests (Playwright / TypeScript)

All commands run from the `ui/` directory.

```bash
npm install                          # Install dependencies (first time)
npx playwright install chromium      # Install browser binary (first time)
npm test                             # Run all tests (headless)
npm run test:headed                  # Run with visible browser
npm run test:debug                   # Open Playwright inspector
npm run test:report                  # Open HTML report from last run
npm run type-check                   # TypeScript type check
npm run lint                         # ESLint
```

Run a single test file:

```bash
npx playwright test tests/ui/parts/create-part.spec.ts
```

Run a specific test by title substring:

```bash
npx playwright test --grep "TC-UI-PC-001"
```

Environment: copy `ui/.env.example` to `ui/.env` and set `BASE_URL` (defaults to `https://demo.inventree.org`).

## Architecture

**Just for information and flow description. Not rules to follow.**

The project follows a four-phase agentic workflow:

1. **Documentation phase** — An AI agent(or researcher agent) reads InvenTree `/docs` folder for needed docs. If no
   desired docs found, then researcher should follow the flow when it needs to fetch docs from remote web resources.

2. **Manual test case phase** — A manual tester AI agent (with UI + API testing skills) reads those docs and generates
   test cases in Markdown format. Each functional area is a separate test suite, mirroring the coverage areas below.

3. **Automation phase** — Two automation agents implement the manual test cases in code, maintaining 1:1 structural
   traceability:
    - **API automation agent** → Java (Maven, REST Assured or equivalent)
    - **UI automation agent** → JavaScript (Playwright or equivalent)

4. **Review phase** — A reviewer agent checks the generated code for correctness, style, and adherence to coding rules.

## Repository Layout (non-code)

```
docs/
├── api/endpoints/      # Per-endpoint API snapshots (method-named .md files)
├── api/schemas/        # API schema snapshots
└── api/part-api-schema.md

test-cases/
├── index.md            # Master index — all TC IDs, titles, priorities
├── api/
│   ├── parts/          # TC_APCRUD_*.md
│   ├── categories/     # TC_ACCRUD_*.md
│   ├── filtering/      # TC_APFLT_*.md
│   ├── validation/     # TC_APVAL_*.md
│   ├── relational/     # TC_APREL_*.md
│   └── edge-cases/     # TC_APEDGE_*.md
└── ui/
    └── parts/          # TC_UI_PART_CREATE.md (source for create-part.spec.ts)
```

`test-cases/index.md` is the authoritative traceability map. Each automated spec must implement exactly the TC IDs
listed there. New test cases go to `test-cases/` first; automation follows.

## Coverage Areas

**UI**: Part creation (manual entry and import), Part detail tabs (Stock, BOM, Allocated, Build Orders, Parameters,
Variants, Revisions, Attachments, Related Parts, Test Templates), Part categories (hierarchy, filtering, parametric
tables), Part attributes (Virtual, Template, Assembly, Component, Trackable, Purchasable, Salable, Active/Inactive),
Units of measure, Part revisions, negative/boundary scenarios.

**API**: CRUD on Parts and Part Categories, filtering/pagination/search, field-level validation, relational integrity (
category, default location, supplier linkage), edge cases (invalid payloads, unauthorized access, conflicts).

## Test Case ID Conventions

| Suite                      | Prefix          | Example         |
|----------------------------|-----------------|-----------------|
| API — Parts CRUD           | `TC-APCRUD-NNN` | `TC-APCRUD-001` |
| API — Categories CRUD      | `TC-ACCRUD-NNN` | `TC-ACCRUD-003` |
| API — Filtering            | `TC-APFLT-NNN`  | `TC-APFLT-002`  |
| API — Field Validation     | `TC-APVAL-NNN`  | `TC-APVAL-001`  |
| API — Relational Integrity | `TC-APREL-NNN`  | `TC-APREL-004`  |
| API — Edge Cases           | `TC-APEDGE-NNN` | `TC-APEDGE-007` |
| UI — Part Creation         | `TC-UI-PC-NNN`  | `TC-UI-PC-001`  |

Spec `describe` blocks use the underscore form of the prefix (e.g. `TC_UI_PART_CREATE`, `TC_APCRUD`). Individual `test`/
`it` titles start with the full TC ID.

## Coding Rules

- **No comments in code.** Do not add any inline comments, block comments, or Javadoc to generated or edited code unless
  the user explicitly requests them.

## Response rules

- **Terse responses.** Keep responses brief and direct—focus on what was done or decided, not explanations of how to
  read diffs or tool output. Skip trailing summaries; let the code changes speak for themselves.

## UI Test Architecture (`ui/`)

TypeScript + Playwright. All source lives under `ui/`.

```
ui/
├── framework/
│   ├── core/           # Base classes: BasePage, BaseComponent, BaseTable, BaseTableRow, ElementHolder
│   ├── components/     # Reusable UI components (NavigationBar, PartDetailTabBar, tab panels)
│   └── pages/          # Page Objects (PartsPage, PartsDetailViewPage, CreatePartModal, EditPartModal, …)
├── fixtures/           # Playwright fixture extensions (parts.fixtures.ts → partsPage, partDetailPage)
├── config/             # storageState.ts (auth file paths), users.ts (role definitions)
├── data/               # Static test data (parts.ts)
├── tests/
│   ├── setup/          # auth.setup.ts — logs in all roles, writes .auth/<role>.json
│   └── ui/parts/       # Spec files (create-part.spec.ts, …)
└── playwright.config.ts
```

**Key conventions:**

- `BasePage` validates the current URL on `waitForLoad()`. Pages with dynamic URLs (e.g. `/part/42/`) declare `url` as a
  `RegExp` and override `navigate()`.
- Auth is handled via a `setup` project that runs before `chromium`. Tests select a role with
  `test.use({ storageState: STORAGE_STATE.ENGINEER })`.
- Fixtures extend `@playwright/test` with typed page-object instances. Import from `@fixtures/parts.fixtures` (not
  directly from `@playwright/test`) to get the extended `test` and `expect`.
- Path aliases (`@framework/*`, `@fixtures/*`, `@config/*`, `@data/*`) are defined in `tsconfig.json`.
- Tests follow Given/When/Then using `test.step()`.