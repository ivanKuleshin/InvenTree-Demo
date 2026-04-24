# UI Manual Action Log — Part Detail Tabs

## Session Info

- Base URL: `https://demo.inventree.org`
- Auth: admin / inventree (demo)
- Exploration date: 2026-04-14
- InvenTree version: 1.4.0 dev | 479

## Part 77 — Widget Assembly (WID-TEMPLATE)

### Navigation to Part Detail

1. Navigated to `https://demo.inventree.org/web/part/77/details`
2. Page title confirmed: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
3. Breadcrumb: Parts > Widgets
4. Part header shows: "Part: WID-TEMPLATE | Widget Assembly", description "An assembled widget"
5. Part stats visible in header: "In Stock: 209", "Available: 204", "Allocated: 5", "Required: 277", "In Production: 215", "Deficit: 68"

### Tab Bar — Confirmed Tab Labels

Confirmed via accessibility snapshot (`tablist[aria-label="panel-tabs-part"]`):
- "Part Details" — URL: `/web/part/77/details`
- "Stock" — URL: `/web/part/77/stock`
- "Variants" — URL: `/web/part/77/variants`
- "Allocations" — URL: `/web/part/77/allocations`
- "Bill of Materials" — URL: `/web/part/77/bom`
- "Used In" — URL: `/web/part/77/used_in`
- "Part Pricing" — URL: `/web/part/77/pricing`
- "Sales Orders" — URL: `/web/part/77/sales_orders`
- "Return Orders" — URL: `/web/part/77/return_orders`
- "Build Orders" — URL: `/web/part/77/builds`
- "Stock History" — URL: `/web/part/77/stocktake`
- "Test Templates" — URL: `/web/part/77/test_templates`
- "Test Results" — URL: `/web/part/77/test_results`
- "Related Parts" — URL: `/web/part/77/related_parts`
- "Parameters" — URL: `/web/part/77/parameters`
- "Attachments" — URL: `/web/part/77/attachments`
- "Notes" — URL: `/web/part/77/notes`

**Note:** Documentation refers to the BOM tab as "BOM Tab" — actual label is "Bill of Materials". Documentation refers to "Allocated" tab — actual label is "Allocations".

### Stock Tab — `/web/part/77/stock`

- Clicked "Stock" tab in `panel-tabs-part` tablist
- URL changed to: `https://demo.inventree.org/web/part/77/stock`
- Table column headers (confirmed via `th`/`[role="columnheader"]`):
  `Part | IPN | Description | Stock | Status | Batch Code | Location | Stock Value | Last Updated | Stocktake Date`
- Sample row data visible: "Widget Assembly Variant", "WID-REV-B", serial "#200", status "OK", location "Factory"
- Toolbar action buttons confirmed via `button[aria-label]`:
  - `action-menu-stock-actions` (Stock Actions dropdown)
  - `action-button-order-items`
  - `action-button-add-stock-item`
  - `table-refresh`
  - `table-select-columns`
  - `table-select-filters`
  - `table-export-data`
- Pagination buttons visible: "Previous page", "Next page"

### Bill of Materials Tab — `/web/part/77/bom`

- Navigated to `https://demo.inventree.org/web/part/77/bom`
- Page title: `InvenTree Demo Server | Part: WID-TEMPLATE | Widget Assembly`
- BOM table shows "No records found" for part 77 (template part has no direct BOM items in its own flat BOM at this level — variants inherit)
- Table column headers: `Component | IPN | Description | Reference | Quantity | Total Price | Available Stock | Can Build | Note`
- Toolbar buttons confirmed:
  - `action-button-validate-bom`
  - `bom-validation-info`
  - `action-button-delete-selected-records`
  - `action-menu-add-bom-items`
  - `table-refresh`
  - `table-select-columns`
  - `table-select-filters`
  - `table-export-data`
- Part 87 (Doohickey) confirmed to have 4 BOM items via API

### Part Attributes Confirmed (from Part Details tab)

- Template Part: Yes
- Assembled Part: Yes
- Component Part: Yes
- Testable Part: Yes
- Trackable Part: Yes
- Purchaseable Part: No
- Saleable Part: Yes
- Virtual Part: No

## Part 87 — Doohickey

- assembly=true, component=true, trackable=true, is_template=false, salable=false
- BOM count: 4 items
- Stock count: 20 items
- Test templates: "Checksum" (required, requires_value), "Color" (not required), "Firmware" (required, requires_value)

## Part 82 — 1551ABK

- assembly=false, component=true, trackable=false, is_template=false
- revision_count=1
- Revision part: pk=2005, name="RevisionPart-TC007-1776160457095-ptp9z", revision="B", revision_of=82

## Divergences from Documentation

1. **Tab label**: Docs say "BOM tab" — UI shows "Bill of Materials"
2. **Tab label**: Docs say "Allocated tab" — UI shows "Allocations"
3. **Revisions tab**: Docs describe a "Revisions" tab — not observed in part 77 tab list. Revision navigation appears as a dropdown selector at the top of the part page when multiple revisions exist. Parts that ARE revisions (or have revisions) show this selector.
4. **Stock column**: Docs describe "Quantity" column — UI shows "Stock" column
5. **BOM empty state**: Part 77 (template) BOM shows "No records found" — confirmed expected since BOM is defined on variant parts, not the template itself
6. **"Return Orders" tab**: Present in UI — not mentioned in documentation scope
7. **"Test Results" tab**: Separate from "Test Templates" tab — both present on testable parts
