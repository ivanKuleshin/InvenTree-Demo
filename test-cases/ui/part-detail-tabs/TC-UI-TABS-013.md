### TC-UI-TABS-013: Parameters tab loads and displays part parameters

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/parameters`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Click the "Parameters" tab.
3. Observe the URL and the content of the Parameters panel.
4. Observe the table column headers in the parameters table.
5. Note any existing parameter rows, or confirm the empty state message if no parameters exist.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/parameters`.
- The "Parameters" tab becomes active.
- A parameters table is displayed with column headers including at minimum: "Parameter", "Value", "Units" (or equivalent column labels).
- An "Add Parameter" or equivalent action button is visible in the toolbar.
- If no parameters exist, an empty state indicator is shown (e.g., "No records found").

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Parameters" tab — present (URL: `/web/part/77/parameters`)
- Actual behavior: Tab confirmed present via tab bar snapshot; content not loaded (session expired during navigation)
- Matches docs: Yes (docs describe Parameters tab for managing parameter instances)

**Notes**: Parameters are "flexible metadata attributes" per docs. The tab is visible for all parts regardless of assembly/template status. Tester should confirm actual column header labels on first run and update if they differ from "Parameter", "Value", "Units".
