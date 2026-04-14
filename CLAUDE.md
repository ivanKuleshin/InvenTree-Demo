# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Test Automation Framework (TAF) for [InvenTree](https://demo.inventree.org/) — an open-source inventory management
product. The framework targets the **Parts** domain via API (Java) and UI (JavaScript) automation, driven by an
AI-assisted agentic workflow.

- InvenTree docs: https://docs.inventree.org/en/stable/part/#part-attributes
- InvenTree API docs: https://docs.inventree.org/en/stable/api/

## Build Commands

```bash
mvn clean install                        # Full build + all tests
mvn test                                 # Run all tests (via testng.xml)
mvn test -Dtest=PartCategoryCrudTest     # Run a single test class
mvn test -Dtest=PartCategoryCrudTest#tc_ACCRUD_001  # Run a single test method
mvn test -Dsurefire.suiteXmlFiles=api/src/test/resources/testng.xml  # Explicit suite file
mvn allure:report                        # Generate Allure HTML report after tests
```

Java version: JDK 21 (Amazon Corretto 21). CheckStyle is configured with Google/Sun profiles (v13.4.0).

The default test suite is `api/src/test/resources/testng.xml` — it discovers all classes under `com.inventree.tests`
by package. The `-Dsurefire.suiteXmlFiles` override is useful for pointing at a custom suite XML (e.g. a subset of
tests or a different parallel configuration) without changing `pom.xml`.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 (Amazon Corretto) |
| Build | Maven 3 |
| Test runner | TestNG 7.10 |
| HTTP / API testing | REST Assured 5.5 |
| Serialization | Jackson 2.18 (Databind, XML, JSR-310) |
| Reporting | Allure 2.32 (allure-testng + allure-rest-assured filter) |
| Logging | Log4j2 2.25 + SLF4J bridge |
| Boilerplate reduction | Lombok 1.18 |

## Architecture

### Project Layout

The root `pom.xml` wires `api/src/main/java` and `api/src/test/java` as source roots — there is no Maven
sub-module. All Java production code lives under `api/src/main/java/com/inventree/` and tests under
`api/src/test/java/com/inventree/`.

```
api/src/main/java/com/inventree/
  auth/          AuthManager (token cache), AuthStrategy enum, Role enum, Credentials
  client/        BaseClient (REST Assured executor), SpecBuilder
  config/        ApiConfig (typed getters), ConfigManager (loads config.properties)
  model/         POJOs: Part, PartCategory, PartRequest, PartCategoryRequest, PaginatedResponse<T>
  service/       PartService, PartCategoryService — service layer over BaseClient
  util/          ApiConstants (endpoint paths, property keys), HttpStatus, ResponseValidator

api/src/test/java/com/inventree/
  base/          BaseTest — @BeforeSuite wires services + pre-warms auth tokens; @AfterSuite clears cache
  testdata/      CategoryTestData, FilteringTestData — all constants and name-builder helpers
  tests/         PartCategoryCrudTest, PartFilteringPaginationSearchTest, SmokeTest
```

### Service Layer Pattern

Each `*Service` extends `BaseClient` and exposes two method variants:
- **Typed** (`createPart`, `listParts`, …) — asserts status + deserializes to POJO; use for happy-path steps.
- **Raw** (`createPartRaw`, `listPartsRaw`, …) — returns `Response` as-is; use when the test needs to inspect
  status codes, headers, or error bodies directly.

### Configuration

Runtime config is loaded from `api/src/main/resources/config.properties`. Key properties:

| Property | Default |
|---|---|
| `api.base.url` | `https://demo.inventree.org` |
| `api.auth.strategy` | `TOKEN` |
| `api.credentials.<role>.username/password` | per-role demo credentials |

Roles available: `ADMIN`, `ALLACCESS`, `READER`, `ENGINEER`.

### Test Data Pattern

All literals (status codes, query params, expected messages, page limits) must live in `*TestData` classes, never
inline in test methods. Test names use helper builders, e.g. `CategoryTestData.testCategoryName("TC-001", "label")`.

### Cleanup Pattern

Track created entity IDs in a `List<Integer>` field, delete them in `@AfterMethod` inside a try-catch. Never put
cleanup inline at the end of a test method — it will be skipped on assertion failure.

## Agentic Workflow

Four dedicated Claude agents build the framework end-to-end:

| Agent | Role |
|---|---|
| `requirements-researcher` | Fetches InvenTree docs → `docs/` |
| `manual-qa-agent` | Produces manual test cases → `test-cases/` |
| `api-automation-agent` | Implements Java tests from manual cases |
| `api-automation-code-reviewer` | Reviews generated tests; reports only, does not auto-fix |

UI automation agent is **TODO** — manual UI test cases exist in `test-cases/ui/` but no automation yet.

## Coverage Status

**API (Java — done):** Parts CRUD, Part Categories CRUD, Filtering/Pagination/Search.  
**API (TODO):** Field-level Validation, Relational Integrity, Edge Cases.  
**UI (TODO):** All suites (Part creation, detail tabs, categories, attributes, UoM, revisions, negative).

## Coding Rules

- **No comments in code.** No inline comments, block comments, or Javadoc unless explicitly requested.

## Response Rules

- **Terse responses.** Brief and direct — skip trailing summaries, let the code speak.
