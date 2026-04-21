### TC-UI-TABS-004: Bill of Materials tab visible only for assembly parts and displays correct columns

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/87/bom`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] Part 87 ("Doohickey") must have `assembly=true` and at least 4 BOM items (confirmed via API)
- [ ] Navigate to: `https://demo.inventree.org/web/part/87/details`

**Steps**:

1. Locate the tab bar labeled `panel-tabs-part` on the Part 87 detail page.
2. Verify that the "Bill of Materials" tab is present in the tab bar.
3. Click the "Bill of Materials" tab.
4. Observe the URL, table column headers, and number of rows.

**Expected Result**:
- The "Bill of Materials" tab is visible in the tab bar (because the part is an assembly).
- URL changes to `https://demo.inventree.org/web/part/87/bom`.
- A table is displayed with column headers: "Component", "IPN", "Description", "Reference", "Quantity", "Total Price", "Available Stock", "Can Build", "Note".
- At least 4 BOM line item rows are visible in the table.
- Toolbar contains: `action-button-validate-bom`, `bom-validation-info`, `action-menu-add-bom-items`, `action-button-delete-selected-records`, `table-export-data`.

**Observed** (filled during exploration):

- Page title seen: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly` (part 77 explored; tab label confirmed)
- Element confirmed: "Bill of Materials" tab — present
- Element confirmed: columns "Component | IPN | Description | Reference | Quantity | Total Price | Available Stock | Can Build | Note" — present (confirmed on part 77 BOM)
- Element confirmed: `action-button-validate-bom`, `bom-validation-info`, `action-menu-add-bom-items` buttons — present
- Actual behavior: Part 77 BOM showed "No records found" (template part — expected); Part 87 confirmed to have 4 BOM items via API
- Matches docs: Partial — docs call tab "BOM Tab" but UI label is "Bill of Materials"

**Notes**: The UI tab label is "Bill of Materials", not "BOM Tab" as described in documentation. Part 77 (template) BOM is empty by design; use Part 87 for a BOM with content.
