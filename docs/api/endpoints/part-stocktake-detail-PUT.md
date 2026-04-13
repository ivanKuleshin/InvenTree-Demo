---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/stocktake/{id}/ — Update Part Stocktake
fetched: 2026-04-13
---

# `PUT /api/part/stocktake/{id}/` — Update Part Stocktake

**Operation ID:** `part_stocktake_update`

**Tags:** `part`

**Description:** Detail API endpoint for a single PartStocktake instance.

> **Note:** Only staff (admin) users can access this endpoint.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Required.**

| Content-Type                        | Schema                                               |
| ----------------------------------- | ---------------------------------------------------- |
| `application/json`                  | [PartStocktake](../part-api-schema.md#partstocktake) |
| `application/x-www-form-urlencoded` | [PartStocktake](../part-api-schema.md#partstocktake) |
| `multipart/form-data`               | [PartStocktake](../part-api-schema.md#partstocktake) |

## Responses

| Status Code | Description | Schema                                               |
| ----------- | ----------- | ---------------------------------------------------- |
| 200         | OK          | [PartStocktake](../part-api-schema.md#partstocktake) |
