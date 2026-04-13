---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/stocktake/{id}/ — Retrieve Part Stocktake
fetched: 2026-04-13
---

# `GET /api/part/stocktake/{id}/` — Retrieve Part Stocktake

**Operation ID:** `part_stocktake_retrieve`

**Tags:** `part`

**Description:** Detail API endpoint for a single PartStocktake instance.

> **Note:** Only staff (admin) users can access this endpoint.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartStocktake](../part-api-schema.md#partstocktake) |
