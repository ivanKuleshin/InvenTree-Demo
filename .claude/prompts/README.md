# This folder contains prompts that we used during development. Typically we won't save them because they are one-time, but Hackathon rules require to provide them

## Project Structure

```
.
├── api/                        # Java TAF for API tests (REST Assured + TestNG)
├── ui/                         # JavaScript TAF for UI tests (Playwright)
├── docs/                       # Documentation collected by the requirements-researcher agent
├── test-cases/                 # Manual test cases produced by the manual-qa-agent
├── evidences/
│   ├── api/                    # Test execution reports/screenshots for API tests
│   └── ui/                     # Test execution reports/screenshots for UI tests
└── .claude/prompts/            # All prompts used during AI-assisted development (this folder)
    ├── api/                    # Prompts related to API automation
    ├── ui/                     # Prompts related to UI automation
    └── manual/                 # Prompts related to manual test case generation
```

### Folder Details

| Folder | Purpose |
|--------|---------|
| `api/` | Java-based API test automation framework. Uses REST Assured, TestNG, Allure, and Jackson. |
| `ui/` | JavaScript-based UI test automation framework. Uses Playwright. |
| `docs/` | InvenTree documentation snapshots fetched by the `requirements-researcher` agent. Used as input for test case generation. |
| `test-cases/` | Markdown manual test cases written by the `manual-qa-agent`. Serve as the source for API and UI automation. |
| `evidences/api/` | Execution reports and artifacts produced by API test runs. |
| `evidences/ui/` | Execution reports and artifacts produced by UI test runs. |
| `.claude/prompts/` | Prompts used by each AI agent during development, preserved per Hackathon requirements. |
