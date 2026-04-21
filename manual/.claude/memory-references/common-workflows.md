# Common Workflows

## Create Manual UI Test Cases

Use the **`ui-manual-testing` skill** to explore the InvenTree web UI and create test cases:

```bash
/ui-manual-testing [feature area name or description]
```

### What the Skill Does

1. **Load Knowledge** — Reads docs from `docs/` and InvenTree's official documentation
2. **Plan Coverage** — Creates a probe plan (pages, flows, auth requirements), asks for confirmation
3. **Explore** — Fetches live pages via Playwright MCP, records all UI elements (labels, field names, buttons, validation messages)
4. **Document** — Writes grounded test cases to `test-cases/ui/{suite}/` with evidence from exploration

### Test Case Output

- **Format**: See `.claude/skills/ui-manual-testing/references/tc-template.md`
- **Location**: `test-cases/ui/{suite_name}/TC_UI_<AREA>_<NUMBER>.md`
- **ID Pattern**: `TC_UI_<AREA>_<NUMBER>` (e.g., `TC_UI_PC_001` = UI Part Creation)
- **Includes**: Preconditions, steps, expected results, and **Observed** section with actual UI behavior

## Create Manual API Test Cases

Use the **`api-manual-testing` skill** to explore the InvenTree REST API and create test cases:

```bash
/api-manual-testing [endpoint or feature area]
```

### What the Skill Does

1. **Load Knowledge** — Reads API docs from `docs/api/`
2. **Plan Coverage** — Creates a probe plan, asks for confirmation
3. **Explore** — Makes live requests to the demo API, records response structures, status codes, error shapes
4. **Document** — Writes grounded test cases to `test-cases/api/{suite}/` with evidence from live requests

### Test Case Output

- **Format**: See `.claude/skills/api-manual-testing/references/tc-template.md`
- **Location**: `test-cases/api/{suite_name}/TC_AP<TYPE>_<NUMBER>.md`
- **ID Pattern**: `TC_AP<TYPE>_<NUMBER>` (e.g., `TC_APCRUD_001` = API Part CRUD)
- **Includes**: Preconditions, steps (API requests), expected results, and actual response structures

## Load Documentation Snapshots

Use the **`requirements-researcher` agent** to analyze InvenTree docs and create snapshots:

```bash
/requirements-researcher [search term or topic]
```

### What the Agent Does

1. Queries InvenTree's official documentation
2. Creates Markdown snapshots of endpoints, schemas, and features
3. Saves results to `docs/api/` for reuse by other agents and skills

**Note**: These snapshots are used by ui-manual-testing and api-manual-testing skills as reference material.

## Best Practices for Workflows

- **Before creating test cases** — Check `test-cases/` to see if coverage already exists
- **When exploring** — Log your steps in a `-ui-manual-action-log.md` or `-api-manual-log.md` file; reuse that for evidence in test cases
- **Evidence matters** — Always include **Observed** sections grounded in real exploration
- **Auth context** — UI tests should note whether admin or read-only user is required
- **Cleanup** — Some flows are mutating (create/edit/delete) — flag these in plans and avoid polluting the demo
- **Traceability** — Test case ID matches the actual file name for easy lookup