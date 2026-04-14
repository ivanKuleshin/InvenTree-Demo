# API Manual Test Plan — Parts & Categories Full CRUD

**Date:** 2026-04-14
**Base URL:** https://demo.inventree.org
**Auth:** HTTP Basic (credentials not stored here)

## Scope

This plan covers all remaining CRUD suites not already captured in `api-manual-plan.md` (Part Creation TC-AP-PC-\*).

## Coverage Areas

| Coverage Area                               | Endpoint                   | HTTP Methods | TC IDs        | Mutating?     |
| ------------------------------------------- | -------------------------- | ------------ | ------------- | ------------- |
| List categories                             | `/api/part/category/`      | GET          | TC-ACCRUD-001 | No            |
| Retrieve single category                    | `/api/part/category/{id}/` | GET          | TC-ACCRUD-002 | No            |
| Create category (minimal)                   | `/api/part/category/`      | POST         | TC-ACCRUD-003 | [MUTATING]    |
| Create category (with parent)               | `/api/part/category/`      | POST         | TC-ACCRUD-004 | [MUTATING]    |
| Full update category (PUT)                  | `/api/part/category/{id}/` | PUT          | TC-ACCRUD-005 | [MUTATING]    |
| Partial update category (PATCH)             | `/api/part/category/{id}/` | PATCH        | TC-ACCRUD-006 | [MUTATING]    |
| Delete category                             | `/api/part/category/{id}/` | DELETE       | TC-ACCRUD-007 | [MUTATING]    |
| List parts                                  | `/api/part/`               | GET          | TC-APCRUD-001 | No            |
| Retrieve single part                        | `/api/part/{id}/`          | GET          | TC-APCRUD-002 | No            |
| Retrieve part with query flags              | `/api/part/{id}/`          | GET          | TC-APCRUD-003 | No            |
| Full update part (PUT)                      | `/api/part/{id}/`          | PUT          | TC-APCRUD-004 | [MUTATING]    |
| Partial update part (PATCH)                 | `/api/part/{id}/`          | PATCH        | TC-APCRUD-005 | [MUTATING]    |
| Delete part                                 | `/api/part/{id}/`          | DELETE       | TC-APCRUD-006 | [MUTATING]    |
| List parts — limit/offset pagination        | `/api/part/`               | GET          | TC-APFLT-001  | No            |
| List parts — search by name                 | `/api/part/`               | GET          | TC-APFLT-002  | No            |
| List parts — filter by category             | `/api/part/`               | GET          | TC-APFLT-003  | No            |
| List parts — filter by boolean (active)     | `/api/part/`               | GET          | TC-APFLT-004  | No            |
| List parts — ordering ascending/descending  | `/api/part/`               | GET          | TC-APFLT-005  | No            |
| List categories — search & filter by parent | `/api/part/category/`      | GET          | TC-APFLT-006  | No            |
| Category: missing required name → 400       | `/api/part/category/`      | POST         | TC-APVAL-001  | No (rejected) |
| Category: duplicate name+parent → 400       | `/api/part/category/`      | POST         | TC-APVAL-002  | [MUTATING]    |
| Category: name exceeds max length → 400     | `/api/part/category/`      | POST         | TC-APVAL-003  | No (rejected) |
| Part: name exceeds max length → 400         | `/api/part/`               | POST         | TC-APVAL-004  | No (rejected) |
| Part: IPN exceeds max length → 400          | `/api/part/`               | POST         | TC-APVAL-005  | No (rejected) |
| Part: non-integer category → 400            | `/api/part/`               | POST         | TC-APVAL-006  | No (rejected) |
| Part: category FK invalid → 400             | `/api/part/category/{id}/` | POST         | TC-APREL-001  | No (rejected) |
| Part category default_location FK           | `/api/part/category/`      | POST         | TC-APREL-002  | [MUTATING]    |
| Part default_location FK                    | `/api/part/`               | POST         | TC-APREL-003  | [MUTATING]    |
| Part variant_of FK                          | `/api/part/`               | POST         | TC-APREL-004  | [MUTATING]    |
| Unauthenticated GET → 401                   | `/api/part/`               | GET          | TC-APEDGE-001 | No            |
| Read-only user cannot POST → 403            | `/api/part/`               | POST         | TC-APEDGE-002 | No (rejected) |
| GET non-existent part → 404                 | `/api/part/{id}/`          | GET          | TC-APEDGE-003 | No            |
| GET non-existent category → 404             | `/api/part/category/{id}/` | GET          | TC-APEDGE-004 | No            |
| Part uniqueness conflict → 400              | `/api/part/`               | POST         | TC-APEDGE-005 | [MUTATING]    |
| DELETE non-existent part → 404              | `/api/part/{id}/`          | DELETE       | TC-APEDGE-006 | No            |

## Probe Order

1. GET `/api/part/category/?limit=10` — observe list shape, collect PKs
2. GET `/api/part/category/{id}/` — observe single retrieve shape
3. GET `/api/part/?limit=5` — observe list shape, collect part PKs
4. GET `/api/part/{id}/` — observe single retrieve shape
5. GET `/api/part/{id}/?category_detail=true&parameters=true&tags=true` — observe expanded fields
6. POST `/api/part/category/` — minimal payload
7. POST `/api/part/category/` — with parent
8. PUT `/api/part/category/{id}/` — full update
9. PATCH `/api/part/category/{id}/` — partial update
10. DELETE `/api/part/category/{id}/` — delete test category
11. POST `/api/part/` (for CRUD target) — create test part
12. PUT `/api/part/{id}/` — full update
13. PATCH `/api/part/{id}/` — partial update description
14. DELETE `/api/part/{id}/` — delete test part
15. GET `/api/part/?limit=5&offset=5` — pagination
16. GET `/api/part/?search=resistor&limit=10` — search
17. GET `/api/part/?category=1&limit=5` — filter by category
18. GET `/api/part/?active=true&limit=5` — filter boolean
19. GET `/api/part/?ordering=name&limit=5` + `ordering=-name` — ordering
20. GET `/api/part/category/?search=&limit=5` + filter by parent
21. POST `/api/part/category/` — missing name → 400
22. POST `/api/part/category/` — name 101 chars → 400
23. POST `/api/part/` — name 101 chars → 400
24. POST `/api/part/` — IPN 101 chars → 400
25. POST `/api/part/` — category as string → 400
26. POST `/api/part/` — invalid default_location FK → 400
27. POST `/api/part/category/` — invalid default_location FK → 400
28. POST `/api/part/` — with valid default_location
29. POST `/api/part/` — with variant_of
30. GET `/api/part/` — no auth → 401
31. POST `/api/part/` — reader credentials → 403
32. GET `/api/part/99999999/` → 404
33. GET `/api/part/category/99999999/` → 404
34. POST duplicate part → 400
35. DELETE `/api/part/99999999/` → 404
