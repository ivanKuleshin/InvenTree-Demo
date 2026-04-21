# API Manual Test Plan — POST /api/part/ Validation & Negative Cases

**Suite**: eval3-validation-negative
**TC Prefix**: TC-APVAL
**Date**: 2026-04-18
**Source**: docs/api/endpoints/part-create-POST.md + docs/api/schemas/part.md

## Coverage Table

| # | Coverage Area | Endpoint | Method | TC IDs | Notes |
|---|---|---|---|---|---|
| 1 | Missing required field (`name`) | `POST /api/part/` | POST | TC-APVAL-001 | [MUTATING] — empty body |
| 2 | Missing required field (body omitted entirely) | `POST /api/part/` | POST | TC-APVAL-002 | [MUTATING] — `{}` body |
| 3 | Invalid data type — `category` as non-integer string | `POST /api/part/` | POST | TC-APVAL-003 | [MUTATING] |
| 4 | Invalid data type — `default_expiry` as non-integer string | `POST /api/part/` | POST | TC-APVAL-004 | [MUTATING] |
| 5 | Duplicate name (uniqueness check) | `POST /api/part/` | POST | TC-APVAL-005 | [MUTATING] — create then re-create same name |
| 6 | `name` exceeds max length (100 chars) | `POST /api/part/` | POST | TC-APVAL-006 | [MUTATING] |
| 7 | Unauthenticated request | `POST /api/part/` | POST | TC-APVAL-007 | [MUTATING] — no auth header |

## Pre-probe: GET /api/part/ first to confirm endpoint state.

**Confirmation**: Pre-confirmed by user. All mutating calls approved.
