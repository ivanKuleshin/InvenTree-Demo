# InvenTree TAF — AI-Assisted Test Automation Framework

Automated test suite for [InvenTree](https://demo.inventree.org/), an open-source inventory management product.
Covers the **Parts** domain via API (Java) and UI (JavaScript) automation, driven by an AI agentic workflow.

- [InvenTree feature docs](https://docs.inventree.org/en/stable/part/#part-attributes)
- [InvenTree API docs](https://docs.inventree.org/en/stable/api/)

---

## AI Agentic Workflow

The framework is built through a four-phase pipeline where each phase is executed by a dedicated Claude agent:

```
                    ┌──────────────────────────────┐
                    │  🔵  Requirements Researcher │
                    └──────────────┬───────────────┘
                                   │  documentation
                                   ▼
                    ┌───────────────────────────────┐
                    │      🟢  Manual QA Agent      │
                    └──────┬───────────────┬────────┘
                           │               │
                   API test cases     UI test cases
                           │               │
                           ▼               ▼
          ┌────────────────────┐   ┌──────────────────────┐
          │ 🟣  API Automation │   │  ⬜  UI Automation   │
          │       Agent        │   │    Agent  (TODO)     │
          └────────┬───────────┘   └──────────────────────┘
                   │  Java tests
                   ▼
          ┌────────────────────┐
          │  🔴  Code Reviewer │
          └────────────────────┘
```

### Agent Responsibilities

| Agent                          | Model  | Role                                                                               | Output                 |
|--------------------------------|--------|------------------------------------------------------------------------------------|------------------------|
| `requirements-researcher`      | Haiku  | Fetches and snapshots InvenTree docs from official sources into local MD files     | `docs/**/*.md`         |
| `manual-qa-agent`              | Sonnet | Reads doc snapshots and produces structured, tool-agnostic manual test cases       | `test-cases/**/*.md`   |
| `api-automation-agent`         | Sonnet | Implements high-priority manual API test cases as Java (TestNG + REST Assured)     | `api/src/test/java/**` |
| `api-automation-code-reviewer` | Sonnet | Reviews generated Java tests against coding standards; reports issues, no auto-fix | review report          |
| `ui-automation-agent`          | —      | *(planned)* Implements UI test cases as JavaScript (Playwright)                    | `ui/src/test/**`       |

### Data Flow

1. **`requirements-researcher`** crawls `docs.inventree.org`, creates structured MD snapshots under `docs/`.
2. **`manual-qa-agent`** reads those snapshots and produces one MD file per test case under `test-cases/api/` or
   `test-cases/ui/`, each prefixed with a traceability ID (e.g. `TC-APFLT-001`).
3. **`api-automation-agent`** reads manual test cases and generates Java test classes — one class per suite, one method
   per test case — using the project's service layer and TestData constants.
4. **`api-automation-code-reviewer`** audits the generated code for hardcoded values, missing cleanup hooks, and Allure
   integration gaps, then returns a structured report.

---

## Test Coverage

### API Tests (Java — TestNG + REST Assured)

| Suite                                        | Traceability prefix | Status  |
|----------------------------------------------|---------------------|---------|
| Parts CRUD (`/api/part/`)                    | `TC-APCRUD-`        | ✅ Done  |
| Part Categories CRUD (`/api/part/category/`) | `TC-ACCRUD-`        | ✅ Done  |
| Filtering, Pagination & Search               | `TC-APFLT-`         | ✅ Done  |
| Field-level Validation                       | `TC-APVAL-`         | 🔲 TODO |
| Relational Integrity                         | `TC-APREL-`         | 🔲 TODO |
| Edge Cases (401/403, conflicts)              | `TC-APEDGE-`        | 🔲 TODO |

### UI Tests (JavaScript — Playwright)

| Suite                                          | Traceability prefix | Status  |
|------------------------------------------------|---------------------|---------|
| Part creation (manual entry & import)          | `TC-CRE-`           | 🔲 TODO |
| Part detail tabs (Stock, BOM, Attachments…)    | `TC-TAB-`           | 🔲 TODO |
| Part categories (hierarchy, filtering)         | `TC-CAT-`           | 🔲 TODO |
| Part attributes (Virtual, Template, Assembly…) | `TC-ATR-`           | 🔲 TODO |
| Units of measure                               | `TC-UOM-`           | 🔲 TODO |
| Part revisions                                 | `TC-REV-`           | 🔲 TODO |
| Negative & boundary scenarios                  | `TC-NEG-`           | 🔲 TODO |

---

## Project Structure

```
InvenTree-Demo/
├── api/                        # Java API test module (Maven)
│   └── src/
│       ├── main/java/com/inventree/
│       │   ├── auth/           # Auth strategies and credentials
│       │   ├── client/         # REST Assured base client & spec builder
│       │   ├── config/         # Config manager & API config
│       │   ├── model/          # POJOs (Part, PartCategory, etc.)
│       │   ├── service/        # Service layer (PartService, PartCategoryService)
│       │   └── util/           # HttpStatus constants, ResponseValidator
│       └── test/java/com/inventree/
│           ├── base/           # BaseTest
│           ├── testdata/       # TestData constant classes
│           └── tests/          # Test classes
├── docs/                       # Documentation snapshots (agent-generated)
├── test-cases/
│   ├── api/                    # Manual API test cases (MD)
│   └── ui/                     # Manual UI test cases (MD)
└── .claude/
    └── agents/                 # Agent definitions
```

## Tech Stack

### API Automation (Java)

|                    |                           |
|--------------------|---------------------------|
| Language           | Java 21 (Amazon Corretto) |
| Build              | Maven 3                   |
| Test runner        | TestNG 7.10               |
| HTTP / API testing | REST Assured 5.5          |
| Reporting          | Allure 2.32               |
| Serialization      | Jackson 2.18              |
| Logging            | Log4j2 2.25               |

### UI Automation (planned)

|           |                         |
|-----------|-------------------------|
| Language  | JavaScript / TypeScript |
| Framework | Playwright              |

## Build & Run

```bash
mvn clean install                        # build + run all tests
mvn test                                 # run all tests via testng.xml
mvn test -Dtest=PartCategoryCrudTest     # run a single test class
mvn test -Dsurefire.suiteXmlFiles=api/src/test/resources/testng.xml  # explicit suite file
mvn allure:report                        # generate Allure HTML report
```

Requires JDK 21 (Amazon Corretto 21).
