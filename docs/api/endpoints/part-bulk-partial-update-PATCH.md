---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/ — Bulk Partial Update Parts
fetched: 2026-04-13
---

# `PATCH /api/part/` — Bulk Partial Update Parts

**Operation ID:** `part_bulk_partial_update`

**Tags:** `part`

**Description:** Perform a PATCH operation against this list endpoint.

Note that the typical DRF list endpoint does not support PATCH, so this method is provided as a custom implementation.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`, `r:change:build`

## Request Body

**Optional.**

| Content-Type                        | Schema                                           |
| ----------------------------------- | ------------------------------------------------ |
| `application/json`                  | [PatchedPart](../part-api-schema.md#patchedpart) |
| `application/x-www-form-urlencoded` | [PatchedPart](../part-api-schema.md#patchedpart) |
| `multipart/form-data`               | [PatchedPart](../part-api-schema.md#patchedpart) |

## Responses

| Status Code | Description | Schema                             |
| ----------- | ----------- | ---------------------------------- |
| 200         | OK          | [Part](../part-api-schema.md#part) |
