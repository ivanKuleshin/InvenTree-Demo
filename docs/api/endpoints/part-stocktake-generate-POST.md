---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/stocktake/generate/ — Generate Part Stocktake
fetched: 2026-04-13
---

# `POST /api/part/stocktake/generate/` — Generate Part Stocktake

**Operation ID:** `part_stocktake_generate_create`

**Tags:** `part`

**Description:** Perform stocktake generation on POST request.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PartStocktakeGenerate](../part-api-schema.md#partstocktakegenerate) |
| `application/x-www-form-urlencoded` | [PartStocktakeGenerate](../part-api-schema.md#partstocktakegenerate) |
| `multipart/form-data` | [PartStocktakeGenerate](../part-api-schema.md#partstocktakegenerate) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201 | Created | [PartStocktakeGenerate](../part-api-schema.md#partstocktakegenerate) |
