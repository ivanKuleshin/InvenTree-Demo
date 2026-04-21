# UI Manual Test Plan — Negative / Boundary Scenarios (Parts Domain)

**Suite prefix:** `TC-UI-NEG-`
**Target app:** https://demo.inventree.org
**Auth:** admin / inventree [AUTH REQUIRED]
**Created:** 2026-04-14

---

## Coverage Areas

| Coverage Area | Page URL(s) | TC IDs | Auth Required | Mutating? |
|---|---|---|---|---|
| Required field validation (empty Name submit) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-001 | Yes | No (dialog closed before submit reaches server) |
| Name field at max boundary (100 chars valid, 101 chars invalid) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-002, TC-UI-NEG-003 | Yes | [MUTATING] TC-UI-NEG-002 creates a part |
| IPN at max boundary (100 chars valid, 101 chars invalid) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-004, TC-UI-NEG-005 | Yes | [MUTATING] TC-UI-NEG-004 creates a part |
| Description at max boundary (250 chars valid, 251 chars invalid) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-006 | Yes | No (validation error) |
| Units field — invalid unit string | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-007 | Yes | No (validation error) |
| Units field — case sensitivity (KG vs kg) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-008 | Yes | No (validation error) |
| Units field — exceeding 20 chars maxLength | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-009 | Yes | No (validation error) |
| Link field — invalid URL format (no scheme) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-010 | Yes | No (validation error) |
| Link field — XSS/javascript: scheme | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-011 | Yes | No (validation error) |
| Default Expiry at -1 (below minimum 0) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-012 | Yes | No (validation error) |
| Default Expiry at max (2147483647) and above max (2147483648) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-013 | Yes | [MUTATING] TC-UI-NEG-013 creates a part at boundary |
| Minimum Stock at negative value (-1) | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-014 | Yes | Exploratory |
| Virtual part + attempt to add stock | `https://demo.inventree.org/web/part/{id}/details` (existing virtual part) | TC-UI-NEG-015 | Yes | No (stock UI hidden) |
| Template part + attempt to set revision_of in Edit | `https://demo.inventree.org/web/part/{id}/details` (existing template part) | TC-UI-NEG-016 | Yes | No (validation error) |
| Duplicate name+IPN+revision within same category | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-017 | Yes | [MUTATING] |
| Whitespace-only Name field | `https://demo.inventree.org/web/part/category/index/parts` | TC-UI-NEG-018 | Yes | No (validation error) |

---

## Mutating Flows Summary

The following TCs will submit forms that create or modify data on the demo server:

- **TC-UI-NEG-002** — Creates a part with a 100-character name (must be cleaned up)
- **TC-UI-NEG-004** — Creates a part with a 100-character IPN (must be cleaned up)
- **TC-UI-NEG-013** — Creates a part with `default_expiry = 2147483647`
- **TC-UI-NEG-017** — Attempts to create a duplicate part (will fail with error — no data persisted)

All other TCs trigger client-side or server-side validation errors and do not persist data.

---

## Confirmation: Proceed with plan?

- 18 TCs to produce across the negative/boundary suite
- 3 mutating TCs that create parts on the demo server
- All pages require admin authentication
