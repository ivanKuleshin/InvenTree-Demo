# API Manual Test Plan — Part Creation

**Suite:** `TC_AP_PART_CREATE`
**Date:** 2026-04-14
**Base URL:** https://demo.inventree.org
**Auth:** HTTP Basic (credentials not stored here)

## Coverage Areas

| Coverage Area                        | Endpoint                 | Method     | TC IDs       | Mutating?     |
| ------------------------------------ | ------------------------ | ---------- | ------------ | ------------- |
| GET part list — baseline             | `/api/part/`             | GET        | (setup)      | No            |
| GET existing category IDs            | `/api/part/category/`    | GET        | (setup)      | No            |
| Minimal valid payload (name only)    | `/api/part/`             | POST       | TC-AP-PC-001 | [MUTATING]    |
| Full optional fields payload         | `/api/part/`             | POST       | TC-AP-PC-002 | [MUTATING]    |
| Duplicate write-only field           | `/api/part/`             | POST       | TC-AP-PC-003 | [MUTATING]    |
| initial_stock write-only field       | `/api/part/`             | POST       | TC-AP-PC-004 | [MUTATING]    |
| initial_supplier write-only field    | `/api/part/`             | POST       | TC-AP-PC-005 | [MUTATING]    |
| Missing required field (name) → 400  | `/api/part/`             | POST       | TC-AP-PC-006 | No (rejected) |
| Invalid category FK → 400/404        | `/api/part/`             | POST       | TC-AP-PC-007 | No (rejected) |
| Unauthenticated request → 401/403    | `/api/part/`             | POST       | TC-AP-PC-008 | No (rejected) |
| Import session API — structure check | `/api/importer/session/` | GET + POST | TC-AP-PC-009 | [MUTATING]    |

## Probe Order

1. GET `/api/part/?limit=5` — confirm auth works, observe response shape
2. GET `/api/part/category/?limit=5` — collect valid category PKs for use in payloads
3. POST `/api/part/` — minimal payload
4. POST `/api/part/` — full payload
5. POST `/api/part/` — duplicate field
6. POST `/api/part/` — initial_stock
7. POST `/api/part/` — initial_supplier
8. POST `/api/part/` — missing name (expect 400)
9. POST `/api/part/` — invalid category (expect 400/404)
10. POST `/api/part/` — no auth header (expect 401/403)
11. GET `/api/importer/session/` — confirm endpoint exists, observe schema
