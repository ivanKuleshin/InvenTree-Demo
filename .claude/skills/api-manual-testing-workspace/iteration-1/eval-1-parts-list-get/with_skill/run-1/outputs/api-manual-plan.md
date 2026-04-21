# API Manual Test Plan — Parts List & Detail (Read-Only)

**Suite**: eval1-parts-list-get
**TC Prefix**: TC-APCRUD
**Date**: 2026-04-18
**Source**: demo.inventree.org

## Coverage

| Coverage Area | Endpoint | Methods | TC IDs |
|---|---|---|---|
| List parts — baseline paginated response | `GET /api/part/` | GET | TC-APCRUD-001 |
| List parts — `parameters=true` flag | `GET /api/part/` | GET | TC-APCRUD-002 |
| List parts — `category_detail=true` flag | `GET /api/part/` | GET | TC-APCRUD-003 |
| List parts — `path_detail=true` flag | `GET /api/part/` | GET | TC-APCRUD-004 |
| List parts — without `limit` (unbounded) | `GET /api/part/` | GET | TC-APCRUD-005 |
| Single part detail — baseline | `GET /api/part/{id}/` | GET | TC-APCRUD-006 |
| Single part detail — `parameters=true` flag | `GET /api/part/{id}/` | GET | TC-APCRUD-007 |
| Single part detail — `category_detail=true` flag | `GET /api/part/{id}/` | GET | TC-APCRUD-008 |
| Single part detail — combined flags | `GET /api/part/{id}/` | GET | TC-APCRUD-009 |
| Single part detail — non-existent ID | `GET /api/part/{id}/` | GET | TC-APCRUD-010 |
| Unauthenticated access — list | `GET /api/part/` | GET | TC-APCRUD-011 |
| Unauthenticated access — detail | `GET /api/part/{id}/` | GET | TC-APCRUD-012 |

## Mutating Calls

None. This plan is read-only.
