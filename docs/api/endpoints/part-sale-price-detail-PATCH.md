---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/sale-price/{id}/ — Partial Update Part Sale Price Break
fetched: 2026-04-13
---

# `PATCH /api/part/sale-price/{id}/` — Partial Update Part Sale Price Break

**Operation ID:** `part_sale_price_partial_update`

**Tags:** `part`

**Description:** Detail endpoint for PartSellPriceBreak model.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PatchedPartSalePrice](../part-api-schema.md#patchedpartsaleprice) |
| `application/x-www-form-urlencoded` | [PatchedPartSalePrice](../part-api-schema.md#patchedpartsaleprice) |
| `multipart/form-data` | [PatchedPartSalePrice](../part-api-schema.md#patchedpartsaleprice) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartSalePrice](../part-api-schema.md#partsaleprice) |
