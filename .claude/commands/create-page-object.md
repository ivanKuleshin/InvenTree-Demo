Create a Page Object Model (POM) for a specific page or component.

**Steps:**

1. Ask for: URL, parts of the page to focus on
2. Explore URL using playwright-CLI (preferred) or Playwright MCP
3. Generate a TypeScript class with:
   - Extends BasePage.ts
   - Has locators that are built using best practices. Do NOT use CSS locators
   - Constructor accepting the `Page` object
   - `goto()` method for navigation

**Output format:**
TypeScript class with proper types following the Page Object pattern.

**Arguments:** $ARGUMENTS (page name or URL)
