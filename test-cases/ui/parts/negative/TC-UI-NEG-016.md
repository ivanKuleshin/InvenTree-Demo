### TC-UI-NEG-016: Locked assembly part — BOM tab shows read-only message, no edit controls

**Type**: UI / Negative / Attribute Constraint
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/911/bom`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part pk=911 ("Blue Furniture Set") exists with `locked: true` and `assembly: true`
- Navigate to `https://demo.inventree.org/web/part/911/bom`

**Steps**:

1. Navigate to `https://demo.inventree.org/web/part/911/bom`
2. Wait for the page to fully load — page title reads "InvenTree Demo Server | Part: Blue Furniture Set"
3. Observe the BOM tab content area
4. Look for any "Add BOM Item" button, add action menu, or edit/delete controls in the BOM table toolbar
5. Read the informational message displayed in the BOM panel

**Expected Result**:

- The BOM panel displays an informational banner or message reading "Part is Locked"
- A secondary message reads "Bill of materials cannot be edited, as the part is locked"
- No "Add BOM Item" button or add-bom-item action menu is present in the BOM table toolbar
- Existing BOM line items are visible but no edit or delete controls appear for individual rows
- The BOM is read-only — no mutation of BOM data is possible through the UI

**Observed** (filled during exploration):

- Page title confirmed: `InvenTree Demo Server | Part: Blue Furniture Set`
- Text confirmed on page: `"Part is Locked"` and `"Bill of materials cannot be edited, as the part is locked"`
- Navigation panel label confirmed: `nav-panel-part-bom` with `bom-validation-info`
- No add/edit/delete BOM buttons found in the DOM — only `action-menu-barcode-actions`, `action-menu-printing-actions`, `action-menu-stock-actions`, `action-menu-part-actions` (part-level actions, not BOM-level)
- Matches docs: Yes — locked parts have read-only BOM; BOM items cannot be created, edited, or deleted

**Notes**: This constraint protects production-ready assemblies from inadvertent BOM changes. The locked state is set by the `locked` boolean flag on the part record.
