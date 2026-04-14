### TC-UI-TABS-002: Add a new Stock Item from the Stock tab [MUTATING]

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/stock`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/stock`
- [ ] The "Stock" tab is active and the stock table is visible

**Steps**:

1. Click the button with aria-label `action-button-add-stock-item` in the toolbar above the stock table.
2. Observe that a dialog or drawer opens.
3. In the dialog, locate the "Quantity" field and enter `5`.
4. Locate the "Status" field and confirm the default value is "OK".
5. Locate the "Location" field and select or type `Factory`.
6. Click the "Save" or "Submit" button in the dialog to create the stock item.
7. Observe the stock table after dialog closes.

**Expected Result**:
- Clicking `action-button-add-stock-item` opens a "Add Stock Item" dialog.
- The dialog contains at minimum: "Quantity", "Status", "Location" fields.
- The "Status" field defaults to "OK".
- After submission the dialog closes and the stock table refreshes.
- The newly created stock item with quantity 5, status "OK", location "Factory" appears in the table.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- Element confirmed: `action-button-add-stock-item` — present
- Element confirmed: Stock table with 209 items — present
- Actual behavior: Button confirmed present in toolbar; dialog interior labels not directly observed (session expired before dialog exploration)
- Matches docs: Yes (docs describe "New Stock Item" dialog launched from Stock tab)

**Notes**: Dialog field labels not confirmed by direct observation due to session timeout. Field names "Quantity", "Status", "Location" are inferred from API schema and docs. Tester should verify exact dialog field labels on first run and update this TC if they differ.
