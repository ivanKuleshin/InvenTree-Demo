# UI Manual Test Plan — Part Creation

**Suite:** `TC_UI_PART_CREATE`
**Date:** 2026-04-14
**Auth:** admin / inventree

## Coverage Areas

| Coverage Area                        | Page URL(s)                                        | TC IDs               | Auth Required | Mutating?  |
| ------------------------------------ | -------------------------------------------------- | -------------------- | ------------- | ---------- |
| Parts list page — entry points       | `/web/part/`                                       | TC-UI-PC-001 (setup) | Yes           | No         |
| New Part form — required fields only | `/web/part/` → "New Part" button                   | TC-UI-PC-001         | Yes           | [MUTATING] |
| New Part form — all optional fields  | `/web/part/` → "New Part" button                   | TC-UI-PC-002         | Yes           | [MUTATING] |
| New Part form — category selection   | `/web/part/` → "New Part" button                   | TC-UI-PC-003         | Yes           | [MUTATING] |
| Boolean attribute toggles            | Part detail page → right panel                     | TC-UI-PC-004         | Yes           | [MUTATING] |
| Duplicate Part flow                  | Part detail page → actions menu → "Duplicate Part" | TC-UI-PC-005         | Yes           | [MUTATING] |
| Validation errors on creation form   | `/web/part/` → "New Part" button → submit empty    | TC-UI-PC-006         | Yes           | No         |
| CSV import wizard — happy path       | `/web/part/` → import button                       | TC-UI-PC-007         | Yes           | [MUTATING] |
| CSV import wizard — error rows       | `/web/part/` → import button → upload bad CSV      | TC-UI-PC-008         | Yes           | [MUTATING] |

## Pages to Fetch

1. `https://demo.inventree.org/web/part/` — Parts list view
2. New Part modal/form — triggered from Parts list
3. Part detail page — for an existing part (boolean toggles, duplicate action)
4. Import wizard pages — step 1 (upload), step 2 (field mapping), step 3 (process rows)

## Notes

- Mutating flows will create real parts on the demo server; demo data resets periodically.
- Import tests require a sample CSV file to be referenced in steps.
- TC IDs use prefix `TC-UI-PC-` as specified in the task (not `TC-CRE-` from the skill template).
