---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/stocktake/{id}/ — Partial Update Part Stocktake
fetched: 2026-04-13
---

# `PATCH /api/part/stocktake/{id}/` — Partial Update Part Stocktake

**Operation ID:** `part_stocktake_partial_update`

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

**Optional.**

| Content-Type                        | Schema                                                             |
| ----------------------------------- | ------------------------------------------------------------------ |
| `application/json`                  | [PatchedPartStocktake](../part-api-schema.md#patchedpartstocktake) |
| `application/x-www-form-urlencoded` | [PatchedPartStocktake](../part-api-schema.md#patchedpartstocktake) |
| `multipart/form-data`               | [PatchedPartStocktake](../part-api-schema.md#patchedpartstocktake) |

## Responses

| Status Code | Description | Schema                                               |
| ----------- | ----------- | ---------------------------------------------------- |
| 200         | OK          | [PartStocktake](../part-api-schema.md#partstocktake) |
