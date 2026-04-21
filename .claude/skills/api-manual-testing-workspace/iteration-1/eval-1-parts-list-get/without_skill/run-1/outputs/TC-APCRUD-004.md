# TC-APCRUD-004 — GET /api/part/ Returns 401 Without Authentication

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P1
- **Auth**: None (negative test)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that unauthenticated requests to the parts list endpoint are rejected with HTTP 401.

## Preconditions
- No Authorization header is sent

## Request
```
GET /api/part/?format=json
```
(No Authorization header)

## Expected Response

**Status**: 401 Unauthorized

## Assertions
1. Status code is 401.
2. Response body contains an error indicator (e.g., `{"detail": "Authentication credentials were not provided."}`).
3. No part data is returned.

## Observed
- HTTP 401 returned for unauthenticated request to GET /api/part/
