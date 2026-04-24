### TC-UI-TABS-003: Export stock data from the Stock tab

**Type**: UI
**Priority**: P3
**Page URL**: `https://demo.inventree.org/web/part/77/stock`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/stock`
- [ ] The "Stock" tab is active and the stock table contains at least one row

**Steps**:

1. Locate the `table-export-data` button in the toolbar above the stock item table.
2. Click the `table-export-data` button.
3. Observe that an export dialog or file download prompt appears.
4. If a dialog appears, select a file format (e.g., CSV) and click the confirm/download button.
5. Observe the browser download or the success notification.

**Expected Result**:
- Clicking `table-export-data` opens an export options dialog or triggers a file download.
- A file is downloaded to the local filesystem containing stock data rows with column data matching the table (Part, IPN, Description, Stock, Status, Batch Code, Location, etc.).
- No error message or toast notification indicating failure.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: `table-export-data` — present
- Actual behavior: Export button confirmed present in the toolbar; download/dialog behavior not directly exercised
- Matches docs: Yes (docs describe an "Export" function from the Part Stock view)

**Notes**: Docs describe exporting "stocktake data". The export button label is `table-export-data` (aria-label). Actual exported file format options (CSV, Excel, etc.) must be verified on first test run.
