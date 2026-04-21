### TC-UI-TABS-007: Allocations tab loads and shows allocation data for a salable component part

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/allocations`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `component=true` and `salable=true`
- [ ] Part 77 header shows "Allocated: 5" (confirmed during exploration)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Verify that the "Allocations" tab is present in the tab bar.
3. Click the "Allocations" tab.
4. Observe the URL and content of the Allocations tab panel.
5. Check whether allocation entries are listed and identify the column headers.

**Expected Result**:
- The "Allocations" tab is visible (part is both component and salable).
- URL changes to `https://demo.inventree.org/web/part/77/allocations`.
- The tab panel loads and displays allocation entries.
- Allocation entries link to the associated build orders or sales orders.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Allocations" tab — present in the tab bar
- Actual behavior: Tab confirmed present; full tab content not directly observed due to session expiry during navigation
- Matches docs: Partial — docs call this "Allocated Tab" but UI label is "Allocations"

**Notes**: UI tab label is "Allocations", not "Allocated Tab" as referenced in the documentation. The tab is only visible when the part is a component OR salable. The header shows "Allocated: 5" so allocation records exist and should appear in this tab.
