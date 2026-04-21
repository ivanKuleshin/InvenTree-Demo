# API Manual Test Plan — Part Categories CRUD

**Suite prefix:** TC-ACCRUD  
**Target:** `https://demo.inventree.org/api/part/category/`  
**Planned date:** 2026-04-18  
**Source docs:** `docs/api/endpoints/part-category-*.md`, `docs/api/schemas/category.md`

## Coverage Plan

| Coverage Area       | Endpoint                     | HTTP Method | TC ID          | Mutating? |
|---------------------|------------------------------|-------------|----------------|-----------|
| List categories     | `/api/part/category/`        | GET         | TC-ACCRUD-001  | No        |
| Get single category | `/api/part/category/{id}/`   | GET         | TC-ACCRUD-002  | No        |
| Create category     | `/api/part/category/`        | POST        | TC-ACCRUD-003  | [MUTATING]|
| Rename category     | `/api/part/category/{id}/`   | PATCH       | TC-ACCRUD-004  | [MUTATING]|
| Delete category     | `/api/part/category/{id}/`   | DELETE      | TC-ACCRUD-005  | [MUTATING]|

## Notes

- All mutating calls (POST, PATCH, DELETE) confirmed by user prompt.
- Auth: HTTP Basic (admin / inventree) for write operations; Basic auth also usable for reads.
- Order of execution: GET list → GET single → POST create → PATCH rename → DELETE remove.
- The PATCH and DELETE calls will target the category created in the POST step.
