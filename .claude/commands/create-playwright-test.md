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
   - Use `import { test, expect } from '@playwright/test'`
   - Use existing Page Objects from `tests/pages/` if available
   - Group related actions with `test.step()`. Example:
     await test.step(`GIVEN user is on Book store page`, async () => {
     await bookStorePage.open();
     await bookStorePage.verifyPageOpened();
     });

   await test.step(`WHEN user selects a book`, async () => {
   await bookStorePage.getBookRow(0).productTitle.click();
   });

   await test.step(`THEN Book details page should be opened`, async () => {
   await bookDetailsPage.verifyPageOpenedFor(bookForTest.isbn);
   });
   - Each test must be independent — no shared state between tests

3. **Test structure**:
   - `test.describe` block for the feature
   - `test.beforeEach` for common setup (e.g. login, navigation)
   - Individual `test()` cases for each scenario

4. **Output**:
   - Full TypeScript test file
   - Filename suggestion: `{feature}.spec.ts` in `tests/`
   - List of `data-testid` values needed in the UI

## Error Handling

- If no arguments provided, ask for feature name and URL
- Warn if a Page Object for this page doesn't exist yet (suggest running /create-page-object first)
- Flag if test scenarios seem too broad (suggest splitting)

## Notes

- Tests must not contain implementation details — test behavior, not code
- Assertions go in tests, not Page Objects
- Prefer `toBeVisible()` over `toExist()`
- Avoid `page.waitForTimeout()` — use proper await patterns, typically using expect(element).toBeVisible()
