# Create Playwright Test Command

Create a complete Playwright E2E test for a given feature or user flow.

## Usage

/create-playwright-test [reference to functional test case]

## Instructions for Claude

When this command is invoked:

1. **Gather information**:
   - Feature/page name from $ARGUMENTS
   - URL or route being tested
   - User actions to perform
   - Expected outcomes/assertions

2. **Generate test file** following these rules:
   - Import `test` and `expect` from the relevant fixtures file in `@fixtures/`, not from `@playwright/test` directly
   - Use existing Page Objects from `framework/pages/` if available — never instantiate page objects inside test bodies or `beforeEach`; rely on fixtures instead
   - Filename: `{feature}.spec.ts` (kebab-case, no test-ID prefix)
   - Place file under `tests/ui/` or `tests/api/` as appropriate
   - Do not add `// ── TC-UI-xxx ──` separator comments between tests — the test ID is already in the test name
   - Group related actions with `test.step()`:
     ```
     await test.step(`GIVEN user is on Parts list page`, async () => { ... });
     await test.step(`WHEN user opens Add Part dialog`, async () => { ... });
     await test.step(`THEN part detail page loads`, async () => { ... });
     ```
   - Each test must be independent — no shared state between tests

3. **Test structure**:
   - `test.describe` block for the feature
   - `test.use({ storageState: STORAGE_STATE.<ROLE> })` inside the describe block
   - `test.beforeEach` for navigation only — no page object construction
   - Individual `test()` cases with fixtures destructured from the function signature
   - Preserve test IDs in test names (e.g. `"TC-UI-PC-001: ..."`)

4. **Fixtures**:
   - If no matching fixtures file exists yet, create one at `fixtures/{domain}.fixtures.ts`
   - Extend `base` from `@playwright/test` and export `test` and `expect`
   - One fixture per page object; fixtures receive `{ page }` and call `use(new PageObject(page))`

5. **Output**:
   - Full TypeScript test file
   - Create or reference the fixtures file

## Error Handling

- If no arguments provided, ask for feature name and URL
- Warn if a Page Object for this page doesn't exist yet (suggest running /create-page-object first)
- Flag if test scenarios seem too broad (suggest splitting)

## Notes

- Tests must not contain implementation details — test behavior, not code
- Assertions go in tests, not Page Objects
- Prefer `toBeVisible()` over `toExist()`
- Avoid `page.waitForTimeout()` — use proper await patterns, typically `expect(element).toBeVisible()`
