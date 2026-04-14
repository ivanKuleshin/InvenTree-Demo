# TC-UI-UNIT Action Log — Units of Measure UI Exploration

**Date:** 2026-04-14
**Tester:** QA Agent (API exploration + docs)
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Phase 0 — Knowledge Sources Used

- `docs/ui/units-of-measure.md` — primary UoM reference (pre-existing)
- Live API at `https://demo.inventree.org/api/` — used to confirm field schema, validation errors, part IDs
- Existing `test-cases/ui/parts/TC_UI_PART_CREATE.md` — aria-label reference for the Create Part dialog

---

## Phase 1 — API Schema Confirmation (OPTIONS /api/part/)

Field: `units`
- Type: string
- Required: false
- Read-only: false
- Label: "Units"
- Help text: "Units of measure for this part"
- Max length: 20
- Allow blank: true
- Allow null: true
- Default: ""
- aria-label in form: `text-field-units` (confirmed from TC-UI-PC-002 action log)

Field: `pack_quantity` on supplier part (OPTIONS /api/company/part/)
- Type: string
- Required: false
- Read-only: false
- Label: "Pack Quantity"
- Help text: "Total quantity supplied in a single pack. Leave empty for single items."
- Max length: 25
- Allow blank: true
- Allow null: false

---

## Phase 2 — Live Validation Behavior (API probes)

### Probe 1 — Invalid unit string
```
POST /api/part/ {"name":"TC-UNIT-TEST-INVALID","units":"INVALIDUNIT123"}
Response 400: {"units":["Invalid physical unit"]}
```

### Probe 2 — Wrong-case unit (KG instead of kg)
```
POST /api/part/ {"name":"TC-UNIT-TEST-CASE","units":"KG"}
Response 400: {"units":["Invalid physical unit"]}
```

### Probe 3 — Valid SI unit (kg, correct case)
```
POST /api/part/ {"name":"TC-UNIT-TEST-kg","units":"kg"}
Response 201: pk=3875, units="kg"
```

### Probe 4 — Blank unit
```
POST /api/part/ {"name":"TC-UNIT-TEST-blank","units":""}
Response 201: pk=3876, units=""
```

### Probe 5 — Dimensionless units
```
POST /api/part/ {"units":"piece"} -> pk=3877, units="piece"
POST /api/part/ {"units":"each"}  -> pk=3878, units="each"
POST /api/part/ {"units":"dozen"} -> pk=3879, units="dozen"
```

### Probe 6 — Unit string exceeding 20 chars (21 chars)
```
POST /api/part/ {"units":"aaaaaaaaaaaaaaaaaaaaa"} (21 chars)
Response 400: {"units":["Invalid physical unit","Ensure this field has no more than 20 characters."]}
```

### Probe 7 — Unit string exactly 20 chars (invalid content)
```
POST /api/part/ {"units":"aaaaaaaaaaaaaaaaaaaa"} (20 chars)
Response 400: {"units":["Invalid physical unit"]}  <- only length-invalid error absent
```

### Probe 8 — PATCH existing part to change unit
```
PATCH /api/part/3875/ {"units":"metres"}
Response 200: pk=3875, units="metres"
```

### Probe 9 — Valid SI units accepted
```
POST /api/part/ {"units":"metres"} -> pk=3924, units="metres"
POST /api/part/ {"units":"litres"} -> pk=3925, units="litres"
```

### Probe 10 — Supplier part with incompatible unit
```
POST /api/company/part/ {"part":896,"supplier":3,"SKU":"TC-INCOMPAT-KG","pack_quantity":"5 kg"}
Part 896 units = 'm'
Response 400: {"pack_quantity":["Could not convert 5 kg to m"]}
```

### Probe 11 — Supplier part with compatible unit (feet for metres part)
```
POST /api/company/part/ {"part":896,"supplier":3,"SKU":"TC-TEST-COMPAT-FEET","pack_quantity":"10 feet"}
Part 896 units = 'm'
Response 201: pk=1085, pack_quantity="10 feet", pack_quantity_native=3.048
```

---

## Phase 3 — Confirmed Part Data

| Role in TCs | pk | Name | units | purchaseable | supplier |
|---|---|---|---|---|---|
| Wire part with metres unit | 896 | Silicon Wire 10AWG Black | 'm' | true | pk=42 (Wirey) |
| Wire part with metres unit | 895 | Silicon Wire 10AWG Red | 'm' | true | - |
| Test part (kg created) | 3875 | TC-UNIT-TEST-kg | 'metres' (patched) | false | - |
| Test part (blank unit) | 3876 | TC-UNIT-TEST-blank | '' | false | - |
| Test part (piece) | 3877 | TC-UNIT-TEST-piece | 'piece' | false | - |

Supplier parts for pk=896:
- pk=1018, SKU=WIRE.BLK.10AWG, pack_quantity='1 metre', supplier=42 (Wirey)
- pk=1019, SKU=WIRE.BLK.10AWG.100M, pack_quantity='100m', supplier=42 (Wirey)
- pk=1020, SKU=WIRE.BLK.10AWG.500M, pack_quantity='500m', supplier=42 (Wirey)
- pk=1085, SKU=TC-TEST-COMPAT-FEET, pack_quantity='10 feet', supplier=3 (Arrow)

---

## Phase 4 — UI Navigation Notes

UI URLs confirmed from docs and pattern matching:
- Parts list: `https://demo.inventree.org/web/part/category/index/parts`
- Part detail: `https://demo.inventree.org/web/part/{pk}/details`
- Part suppliers tab: `https://demo.inventree.org/web/part/{pk}/suppliers`
- Settings (physical units): `https://demo.inventree.org/web/settings/`
  - Physical Units is a sub-tab under System Settings
- Create Part modal: triggered via `action-menu-add-parts` → "Create Part"
- Edit Part dialog: triggered via `action-menu-part-actions` → "Edit"
- Units field in dialog: aria-label `text-field-units` (confirmed from TC-UI-PC-002)
- Error rendering: API errors surfaced inline below the field (matching InvenTree pattern from TC-UI-PC-006)

---

## Divergences from Docs

- Docs state the UI label reads "Units of measure (UoM) for this Part. The default is 'pcs'".
  API OPTIONS shows label as simply "Units". The full description text appears as help_text only.
- Docs show the unit for part 896 as 'metres'; live API returns 'm' (abbreviated).
- The "Physical Units" settings page is accessible via UI at Settings → System Settings → Physical Units tab.
  No dedicated API endpoint for custom units listing was found at `/api/part/units/` or `/api/inventree/units/`.
- Changing a base part unit via PATCH does NOT cascade-check existing supplier parts (system allows it silently).
