---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/category/ — Bulk Partial Update Part Categories
fetched: 2026-04-13
---

# `PATCH /api/part/category/` — Bulk Partial Update Part Categories

**Operation ID:** `part_category_bulk_partial_update`

**Tags:** `part`

**Description:** Perform a PATCH operation against this list endpoint.

Note that the typical DRF list endpoint does not support PATCH, so this method is provided as a custom implementation.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`, `r:change:build`

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PatchedCategory](../part-api-schema.md#patchedcategory) |
| `application/x-www-form-urlencoded` | [PatchedCategory](../part-api-schema.md#patchedcategory) |
| `multipart/form-data` | [PatchedCategory](../part-api-schema.md#patchedcategory) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [Category](../part-api-schema.md#category) |
