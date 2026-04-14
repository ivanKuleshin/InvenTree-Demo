# AGENTS.md

Test Automation Framework (TAF) for [InvenTree](https://demo.inventree.org/) targeting the **Parts** domain via Java API tests and TypeScript UI tests. New test cases are written in Markdown first (`test-cases/`), then automated 1-to-1.

## Repository Layout

```
api/src/main/java/com/inventree/   # Java main (non-default Maven root — pom.xml maps it)
  auth/       AuthManager (token cache), Role enum (ADMIN, ALLACCESS, READER, ENGINEER)
  client/     BaseClient (executeGet/Post/…Raw), SpecBuilder (RequestSpec + AllureFilter)
  config/     ApiConfig + ConfigManager → api/src/main/resources/config.properties
  model/      POJOs: Part, PartRequest, PartCategory, PaginatedResponse<T>, …
  service/    PartService, PartCategoryService, StockService, CompanyService
  util/       ApiConstants (endpoint paths), HttpStatus, ResponseValidator

api/src/test/java/com/inventree/
  base/       BaseTest (@BeforeSuite pre-caches all role tokens)
  testdata/   PartTestData, CategoryTestData, FilteringTestData (static factory methods + constants)
  tests/      PartCrudTest, PartCategoryCrudTest, PartFilteringPaginationSearchTest, SmokeTest

ui/
  framework/core/      BasePage, BaseComponent, BaseTable, BaseTableRow, ElementHolder
  framework/components/ NavigationBar, PartDetailTabBar, tab panels
  framework/pages/     PartsPage, PartDetailPage, CreatePartModal, EditPartModal, …
  fixtures/            parts.fixtures.ts → extended test + expect (import from here, not @playwright/test)
  config/              storageState.ts (auth file paths), users.ts (role definitions from env vars)
  data/                parts.ts (static test data)
  tests/setup/         auth.setup.ts — logs in all roles, writes .auth/<role>.json
  tests/ui/parts/      Spec files

test-cases/index.md    # Authoritative traceability map — add here before automating
docs/                  # API/UI snapshots used as documentation database
```

## Build Commands

```bash
# API (Java) — run from project root
mvn test                          # Run all API tests (JDK 21 required)
mvn test -Dgroups=regression      # Run by TestNG group
# testng.xml: api/src/test/resources/testng.xml  (scans com.inventree.tests)

# UI (TypeScript) — run from ui/
cp ui/.env.example ui/.env        # fill passwords before first run
npm install && npx playwright install chromium
npm test                          # headless
npm run test:headed
npx playwright test --grep "TC-UI-PC-001"   # single test by ID
npm run type-check && npm run lint
```

## API Layer Conventions

- **Service dual API**: every operation has a typed happy-path method (`createPart`) and a raw `Response` variant (`createPartRaw`) for error-path tests.
- **Roles**: use `Role.READER` for read-only assertions, `Role.ADMIN` for setup/cleanup, `Role.ENGINEER` for write operations that don't need admin.
- **Credentials**: hardcoded in `api/src/main/resources/config.properties`; auth strategy defaults to `TOKEN` (fetched once and cached per role per suite).
- **Cleanup pattern**: tests collect created PKs in `List<Integer> createdPartIds`; `@AfterMethod` deactivates (`patchActiveOnly(false)`) then deletes — deletion of an active part returns 400.
- **TestData**: names use `PartTestData.testPartName("TC-APCRUD-004", "suffix")` → `"TC-APCRUD-004-suffix-<RUN_ID>"` to avoid collisions across parallel runs.
- **Known fixture data**: `KNOWN_PART_PK = 82` ("1551ABK"), `DEFAULT_CATEGORY_PK = 17`.
- **Write-only fields** (`initial_stock`, `initial_supplier`, `duplicate`): absent from GET/POST responses — assert `null` on response body.
- **Allure**: annotate test classes with `@Epic`/`@Feature`, test methods with `@Story` and `@Severity`.

## UI Layer Conventions

- **Path aliases** (defined in `ui/tsconfig.json`): `@framework/*`, `@fixtures/*`, `@config/*`, `@data/*`.
- **Import test from fixtures**: `import { test, expect } from "@fixtures/parts.fixtures"` — never from `@playwright/test` directly in spec files.
- **Auth**: `test.use({ storageState: STORAGE_STATE.ENGINEER })` at the top of each `describe` block; setup project populates `.auth/<role>.json`.
- **Credentials**: from env vars (`USER_ENGINEER_USERNAME` / `USER_ENGINEER_PASSWORD`), not hardcoded.
- **Dynamic-URL pages** (e.g. `/part/42/`): declare `url` as `RegExp` and override `navigate(path)`; static pages use a string path.
- **Structure**: Given/When/Then using `test.step()`; describe block named with underscore form of TC prefix (e.g. `TC_UI_PART_CREATE`); individual test titles start with full TC ID (`TC-UI-PC-001:`).

## Traceability Rules

1. `test-cases/index.md` is the single source of truth; automation must implement **exactly** the TC IDs listed there.
2. New functionality → create MD test case in `test-cases/` first, then add to `index.md`, then automate.
3. TC ID → file mapping: `TC-APCRUD-*` → `PartCrudTest.java`; `TC-APFLT-*` → `PartFilteringPaginationSearchTest.java`; `TC-UI-PC-*` → `create-part.spec.ts`.

| Suite prefix    | File location                                      |
|-----------------|----------------------------------------------------|
| `TC-APCRUD-*`   | `api/.../tests/PartCrudTest.java`                  |
| `TC-ACCRUD-*`   | `api/.../tests/PartCategoryCrudTest.java`          |
| `TC-APFLT-*`    | `api/.../tests/PartFilteringPaginationSearchTest.java` |
| `TC-APPRICE-*`  | `api/.../tests/PartPricingTest.java`               |
| `TC-APSPRICE-*` | `api/.../tests/PartPricingTest.java`               |
| `TC-APAGPRICE-*`| `api/.../tests/PartPricingTest.java`               |
| `TC-UI-PC-*`    | `ui/tests/ui/parts/create-part.spec.ts`            |

## Coding Rules

- **No code comments** — no inline, block, or Javadoc comments unless explicitly requested.
- **Terse responses** — focus on what was done; skip trailing summaries.

