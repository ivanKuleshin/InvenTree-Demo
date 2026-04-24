# TC-UI-PA Action Log — Part Attribute Toggles UI Exploration

**Date:** 2026-04-14
**Tester:** QA Agent (automated exploration via API + docs)
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Phase 0 — Knowledge Sources Used

- `docs/ui/part-attributes.md` — primary attribute reference (pre-existing)
- Live API at `https://demo.inventree.org/api/` — used to confirm attribute values, part IDs, and data
- Existing `test-cases/ui/parts/TC_UI_PART_CREATE.md` — aria-label reference for form elements

---

## Phase 1 — Confirmed Part Data (Live API)

All part records confirmed via `GET /api/part/{pk}/` with admin credentials.

| Role in TCs | pk | Name | Key Attributes |
|---|---|---|---|
| Virtual part | 914 | CRM license | virtual=true, in_stock=0, stock_item_count=0 |
| Template + Variants | 106 | Chair | is_template=true, assembly=true, variant_stock=43.0, total_in_stock=43.0, in_stock=0 |
| Variant of 106 | 107 | Red Chair | variant_of=106, in_stock=25.0 |
| Variant of 106 | 108 | Blue Chair | variant_of=106, salable=true, assembly=true |
| Variant of 106 | 109 | Green Chair | variant_of=106, in_stock=5.0 |
| Assembly (editable BOM) | 77 | Widget Assembly | assembly=true, locked=false, BOM lines=276 |
| Assembly + Locked | 1934 | AllFlagsPart-TC004-1776159658932-dk4zc | assembly=true, locked=true, all other flags=true |
| Trackable | 1217 | AUTO_QA_TRACKABLE_PART | trackable=true, in_stock=0, stock_item_count=0 |
| Purchaseable + Suppliers | 43 | R_100K_0402_1% | purchasable=true, 10 supplier parts linked |
| Salable + on SO | 108 | Blue Chair | salable=true |
| Inactive | 84 | 1551ACLR | active=false, purchasable=true, component=true |
| Testable | 74 | Blue Widget | testable=true, trackable=true, is_template=true, salable=true, in_stock=145.0, 4 test templates |

---

## Phase 2 — Confirmed Attribute Defaults (from TC-UI-PC-001 Observed block)

Defaults confirmed from "Add Part" dialog on first open:
- `boolean-field-component` = **checked** (true)
- `boolean-field-purchasable` = **checked** (true)
- `boolean-field-active` = **checked** (true)
- `boolean-field-copy_category_parameters` = **checked** (true)
- `boolean-field-assembly` = unchecked (false)
- `boolean-field-is_template` = unchecked (false)
- `boolean-field-testable` = unchecked (false)
- `boolean-field-trackable` = unchecked (false)
- `boolean-field-salable` = unchecked (false)
- `boolean-field-virtual` = unchecked (false)
- `boolean-field-locked` = unchecked (false)

---

## Phase 3 — Confirmed UI Labels (from TC-UI-PC-004 Observed block)

Labels confirmed from `querySelectorAll('label')` in the Add Part dialog:
- "Component" → aria-label: `boolean-field-component`
- "Assembly" → aria-label: `boolean-field-assembly`
- "Is Template" → aria-label: `boolean-field-is_template`
- "Testable" → aria-label: `boolean-field-testable`
- "Trackable" → aria-label: `boolean-field-trackable`
- "Purchaseable" → aria-label: `boolean-field-purchasable`
- "Salable" → aria-label: `boolean-field-salable`
- "Virtual" → aria-label: `boolean-field-virtual`
- "Locked" → aria-label: `boolean-field-locked`
- "Active" → aria-label: `boolean-field-active`

Edit form opened via: `action-menu-part-actions` → "Edit" (aria-label: `action-menu-part-actions-edit`)

---

## Phase 4 — Confirmed Page Navigation

- Part detail page URL pattern: `https://demo.inventree.org/web/part/{pk}/details`
- Part BOM tab URL pattern: `https://demo.inventree.org/web/part/{pk}/bom`
- Part variants tab URL pattern: `https://demo.inventree.org/web/part/{pk}/variants`
- Part stock tab URL pattern: `https://demo.inventree.org/web/part/{pk}/stock`
- Part suppliers tab URL pattern: `https://demo.inventree.org/web/part/{pk}/suppliers`
- Part sales orders tab URL pattern: `https://demo.inventree.org/web/part/{pk}/sales_orders`
- Part test templates tab URL pattern: `https://demo.inventree.org/web/part/{pk}/test-templates`
- Part build orders tab URL pattern: `https://demo.inventree.org/web/part/{pk}/build_orders`

---

## Phase 5 — Key Behavioral Notes

1. **Virtual + Stock:** `virtual=true` → `in_stock=0`, `stock_item_count=0` confirmed on part 914.
   The docs state stock UI elements are hidden for virtual parts.

2. **Template + Stock Aggregation:** Part 106 (Chair) has `in_stock=0` own stock but `total_in_stock=43.0`
   and `variant_stock=43.0`, proving stock aggregates from variants (107=25, 108=0, 109=5, others=13).

3. **Assembly BOM tab:** Part 77 (Widget Assembly) has `assembly=true`, `locked=false`, and 276 BOM lines.
   BOM should be editable (add/edit/delete allowed).

4. **Assembly + Locked BOM:** Part 1934 has `assembly=true` AND `locked=true`.
   Docs state BOM items cannot be created, edited, or deleted when locked.

5. **Trackable:** Part 1217 has `trackable=true`. Docs state stock items MUST have batch or serial number.
   Stock tab should require batch/serial on stock creation form.

6. **Purchaseable:** Part 43 has `purchasable=true` and 10 supplier parts linked.
   The Suppliers tab should be visible and list those supplier parts.

7. **Salable:** Part 108 (Blue Chair) has `salable=true` and appears on sales order lines.
   The Sales Orders tab should be visible and accessible.

8. **Inactive:** Part 84 has `active=false`. Docs state inactive parts are excluded from BOM selection,
   PO line creation, SO line creation, and build order creation.

9. **Testable:** Part 74 (Blue Widget) has `testable=true` and 4 test templates.
   The Test Templates tab should be visible and list those templates.
