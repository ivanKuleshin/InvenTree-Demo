---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/stocktake/ — Create Part Stocktake
fetched: 2026-04-13
---

# `POST /api/part/stocktake/` — Create Part Stocktake

**Operation ID:** `part_stocktake_create`

**Tags:** `part`

**Description:** API endpoint for listing part stocktake information.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part`

## Request Body

**Required.**

| Content-Type                        | Schema                                               |
| ----------------------------------- | ---------------------------------------------------- |
| `application/json`                  | [PartStocktake](../part-api/part-api-schema.md#partstocktake) |
| `application/x-www-form-urlencoded` | [PartStocktake](../part-api/part-api-schema.md#partstocktake) |
| `multipart/form-data`               | [PartStocktake](../part-api/part-api-schema.md#partstocktake) |

## Responses

| Status Code | Description | Schema                                               |
| ----------- | ----------- | ---------------------------------------------------- |
| 201         | Created     | [PartStocktake](../part-api/part-api-schema.md#partstocktake) |
