### TC-UI-TABS-028: Test Templates tab loads and displays existing test templates for a testable part

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/test_templates`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `testable=true` (shown as "Testable Part: Yes" on Part Details tab)
- [ ] Part 77 has 4 test templates: "Commissioning", "Optional", "Paint", "Sticker" (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Verify that the "Test Templates" tab is present in the tab bar.
3. Click the "Test Templates" tab.
4. Observe the URL and the content of the Test Templates panel.
5. Count the number of test template rows.
6. Observe the column headers in the test templates table.
7. Identify the "Required", "Requires Value", and "Requires Attachment" column values for each template row.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/test_templates`.
- A table of test templates is displayed with 4 rows.
- Column headers include: "Test Name" (or "Name"), "Test Key", "Description", "Required", "Requires Value", "Requires Attachment", "Enabled".
- Row data matches:
  - "Commissioning" — required=true, requires_value=false, requires_attachment=false
  - "Optional" — required=false, requires_value=false, requires_attachment=false
  - "Paint" — required=true, requires_value=true, requires_attachment=false
  - "Sticker" — required=true, requires_value=false, requires_attachment=false
- An "Add Test Template" or equivalent add action button is visible in the toolbar.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Test Templates" tab — present (URL: `/web/part/77/test_templates`)
- Actual behavior: Tab confirmed present; test template data confirmed via API (4 templates with keys: commissioning, optional, paint, sticker)
- Matches docs: Yes (docs describe Test Templates tab with Name, Key, Description, Required, Requires Value, Requires Attachment, Enabled columns)

**Notes**: The "Testable Part: Yes" label is visible in the Part Details tab on part 77. The tab is only visible when testable=true per documentation.
