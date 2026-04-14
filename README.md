# This a TAF for UI and API testing using Agentic AI Workflow for InvenTree product - https://demo.inventree.org/.

- https://docs.inventree.org/en/stable/part/#part-attributes - main documentation link
- https://docs.inventree.org/en/stable/api/ - API documentation link

## Main idea is:

- Using AI agent analyze documentation and create md files with snapshot for affective use as a documentation database
- Using manual tester agent with UI and API testing skills develop and create test cases in MD format to cover main
  functionality.
- Each functionality line should be represented as a separate test suite.
- UI main functionality:
  - Part creation (manual entry and import flows)
  - Part detail view — all tabs (Stock, BOM, Allocated, Build Orders, Parameters, Variants, Revisions,
  - Attachments, Related Parts, Test Templates)
  - Part categories — hierarchy, filtering, parametric tables
  - Part attributes — Virtual, Template, Assembly, Component, Trackable, Purchasable, Salable, Active/Inactive
  - Units of measure configuration
  - Part revisions — creation, constraints (circular references, unique codes, template restrictions)
  - Negative and boundary scenarios (e.g., duplicate IPN, inactive part restrictions, revision-of-revision prevention)
- API main functionality:
  - CRUD operations on Parts and Part Categories
  - Filtering, pagination, and search on the Parts list endpoint
  - Field-level validation (required fields, max lengths, nullable constraints, read-only fields)
  - Relational integrity (category assignment, default locations, supplier linkage)
  - Edge cases (invalid payloads, unauthorized access, conflict scenarios)
- After manual test cases are created, UI automation agent (JS) and API automation agent (Java) will implement them in
  code, following the same structure and organization as the manual test cases for traceability.

## UI Test Automation

The UI automation layer (`ui/`) uses **Playwright + TypeScript** and follows the Page Object Model pattern.

### How the UI tests were created

UI tests were generated through a two-phase agentic workflow driven by direct prompts (no subagent spawning — subagent
delegation is a planned future improvement):

1. **Manual testing phase** — the `manual-qa-agent` skill was invoked. It used the **Playwright MCP server** to
   navigate the live InvenTree demo site, interact with real UI elements, and observe actual application behavior. From
   those live observations it produced grounded Markdown test cases in `test-cases/`.

2. **Automation phase** — the `playwright-generate-test` and `create-page-object` skills were invoked against the
   manual test cases to produce TypeScript spec files and Page Object classes that mirror the manual test structure
   1-to-1 for full traceability.

### Running UI tests

```bash
cd ui
npm install                          # first time
npx playwright install chromium      # first time
npm test                             # headless run
npm run test:headed                  # visible browser
npx playwright test --grep "TC-UI-PC-001"   # single test by ID
```

Copy `ui/.env.example` → `ui/.env` and set `BASE_URL` (defaults to `https://demo.inventree.org`).
