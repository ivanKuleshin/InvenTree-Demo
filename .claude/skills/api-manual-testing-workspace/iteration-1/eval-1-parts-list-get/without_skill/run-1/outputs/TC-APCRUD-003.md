# TC-APCRUD-003 — GET /api/part/{id}/ Returns 404 for Non-Existent Part

## Metadata
- **Endpoint**: GET /api/part/{id}/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that requesting a non-existent part ID returns 404 with a descriptive error message.

## Preconditions
- Authenticated as `reader` role
- The requested PK (99999999) does not exist in the database

## Request
```
GET /api/part/99999999/?format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 404 Not Found
**Content-Type**: application/json

**Body**:
```json
{"detail": "No Part matches the given query."}
```

## Assertions
1. Status code is 404.
2. Response body is a JSON object with a `detail` key.
3. `detail` value is a non-empty string describing the not-found condition.
4. Response body does NOT contain any part data fields (`pk`, `name`, etc.).

## Observed Response
```json
{"detail": "No Part matches the given query."}
```

## Notes
- A non-numeric ID (e.g., `/api/part/notanumber/`) also returns 404.
