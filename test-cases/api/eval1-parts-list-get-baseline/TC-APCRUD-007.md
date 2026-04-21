# TC-APCRUD-007 — GET /api/part/{id}/ With ?parameters=true Embeds Parameters on Detail

## Metadata
- **Endpoint**: GET /api/part/{id}/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that `parameters=true` also works on the single part detail endpoint, embedding the parameters array into the response — same structure as in TC-APCRUD-005 for the list endpoint.

## Preconditions
- Authenticated as `reader` role
- Part pk=82 exists and has 4 parameters (Length, Width, Height, Color)

## Request
```
GET /api/part/82/?parameters=true&format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmX5
```

## Expected Response

**Status**: 200 OK

**Body**: Single part object with `parameters` array appended.

## Assertions
1. Status code is 200.
2. Response contains all standard detail fields (including `notes`).
3. `parameters` key is present and is an array.
4. Each parameter entry has the same structure as described in TC-APCRUD-005.
5. `model_id` in each parameter matches the part's `pk`.

## Observed Response
- `parameters` array with 4 items (Length=35mm, Width=20mm, Height=15mm, Color="Black")
- Same structure as returned by the list endpoint with `parameters=true`
- `notes` field is also present (null for this part) — unique to detail endpoint
