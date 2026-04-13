---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/stocktake/ — Bulk Delete Part Stocktakes
fetched: 2026-04-13
---

# `DELETE /api/part/stocktake/` — Bulk Delete Part Stocktakes

**Operation ID:** `part_stocktake_bulk_destroy`

**Tags:** `part`

**Description:** Perform a DELETE operation against this list endpoint.

Note that the typical DRF list endpoint does not support DELETE, so this method is provided as a custom implementation.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part`

## Request Body

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [BulkRequest](../part-api-schema.md#bulkrequest) |
| `application/x-www-form-urlencoded` | [BulkRequest](../part-api-schema.md#bulkrequest) |
| `multipart/form-data` | [BulkRequest](../part-api-schema.md#bulkrequest) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 204 | No response body |  |
