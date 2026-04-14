### TC-UI-TABS-001: Stock Tab loads and displays stock items for an assembly part

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/stock`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part detail page.
2. Click the "Stock" tab in the tab bar.
3. Observe the URL, page title, and tab content area.
4. Observe the table column headers in the stock item table.
5. Observe that at least one row is present in the table.

**Expected Result**:
- URL changes to `https://demo.inventree.org/web/part/77/stock`.
- The "Stock" tab becomes the selected/active tab.
- A table is displayed with the following column headers: "Part", "IPN", "Description", "Stock", "Status", "Batch Code", "Location", "Stock Value", "Last Updated", "Stocktake Date".
- At least one stock item row is visible in the table.
- The toolbar above the table contains the buttons: `action-button-add-stock-item`, `action-menu-stock-actions`, `table-export-data`, `table-select-filters`.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: "Stock" tab — present
- Element confirmed: table columns "Part | IPN | Description | Stock | Status | Batch Code | Location | Stock Value | Last Updated | Stocktake Date" — present
- Element confirmed: `action-button-add-stock-item` button — present
- Actual behavior: Stock tab loaded with 209 stock items for part 77; sample row shows serial "#200", status "OK", location "Factory"
- Matches docs: Yes (docs describe stock table with location, quantity, batch, serial, status, last updated)

**Notes**: UI column label is "Stock" not "Quantity" as described in the documentation. The tab URL path is `/stock` not `/stock_items`.
