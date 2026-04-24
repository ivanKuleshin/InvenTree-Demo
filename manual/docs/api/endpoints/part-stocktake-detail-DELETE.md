---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/stocktake/{id}/ — Delete Part Stocktake
fetched: 2026-04-13
---

# `DELETE /api/part/stocktake/{id}/` — Delete Part Stocktake

**Operation ID:** `part_stocktake_destroy`

**Tags:** `part`

**Description:** Detail API endpoint for a single PartStocktake instance.

> **Note:** Only staff (admin) users can access this endpoint.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description      | Schema |
| ----------- | ---------------- | ------ |
| 204         | No response body |        |
