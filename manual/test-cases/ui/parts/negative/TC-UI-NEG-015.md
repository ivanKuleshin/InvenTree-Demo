### TC-UI-NEG-015: Virtual part detail page — Stock tab is absent

**Type**: UI / Negative / Attribute Constraint
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/914/details`
**Preconditions**:

- [AUTH REQUIRED] Logged in as `admin` at `https://demo.inventree.org`
- Part pk=914 ("CRM license") exists with `virtual: true`
- Navigate to `https://demo.inventree.org/web/part/914/details`

**Steps**:

1. Navigate to `https://demo.inventree.org/web/part/914/details`
2. Wait for the page to fully load — page title reads "InvenTree Demo Server | Part: CRM license"
3. Observe the left navigation panel tabs
4. Verify whether a "Stock" tab is present in the navigation panel
5. Verify whether an "Allocations" tab is present in the navigation panel
6. Observe all visible navigation tabs in the panel

**Expected Result**:

- No "Stock" tab is present in the part detail navigation panel
- No "Allocations" tab is present
- The visible tabs are: Part Details, Part Pricing, Suppliers, Purchase Orders, Related Parts, Parameters, Attachments, Notes
- There is no way to add stock to a virtual part — the Stock UI is completely hidden

**Observed** (filled during exploration):

- Page title confirmed: `InvenTree Demo Server | Part: CRM license`
- Navigation panels confirmed present: `nav-panel-part-details`, `nav-panel-part-pricing`, `nav-panel-part-suppliers`, `nav-panel-part-purchase-orders`, `nav-panel-part-related-parts`, `nav-panel-part-parameters`, `nav-panel-part-attachments`, `nav-panel-part-notes`
- Navigation panels confirmed absent: no `nav-panel-part-stock`, no `nav-panel-part-allocations`
- Tab button labels confirmed: "Part Details", "Part Pricing", "Suppliers", "Purchase Orders", "Related Parts", "Parameters", "Attachments", "Notes"
- Matches docs: Yes — virtual parts cannot have stock items; stock UI elements are hidden

**Notes**: This is a pure UI state verification — no form submission occurs. The virtual attribute constraint is enforced at the UI level by hiding the Stock tab entirely, not by showing it in a disabled state.
