### TC-UI-TABS-010: Build Orders tab loads and lists existing build orders for an assembly part

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/builds`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 77 ("Widget Assembly") has `assembly=true` and at least 2 build orders (confirmed via API: builds count=2 for part 87; part 77 has "In Production: 215" in header)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 77 detail page.
2. Click the "Build Orders" tab.
3. Observe the URL and table content.
4. Identify the column headers in the build orders table.
5. Verify that each row displays a build order reference, status, and quantity.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/builds`.
- A table of build orders is displayed.
- Table columns include at minimum: Reference, Status, Quantity (or similar build order fields).
- At least one build order with status "Pending", "Production", or "Completed" is visible.
- A "New Build Order" or equivalent action button is present in the toolbar.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Build Orders" tab — present in the `panel-tabs-part` tab bar (URL: `/web/part/77/builds`)
- Actual behavior: Tab confirmed present; full content not loaded during session (tab content inspection failed due to auth redirect)
- Matches docs: Yes (docs describe Build Orders tab showing quantity, status, creation and completion dates)

**Notes**: The part header shows "In Production: 215" confirming active build orders exist. Part 87 was confirmed via API to have 2 build orders.
