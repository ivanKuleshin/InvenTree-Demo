---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/sale-price/{id}/ — Update Part Sale Price Break
fetched: 2026-04-13
---

# `PUT /api/part/sale-price/{id}/` — Update Part Sale Price Break

**Operation ID:** `part_sale_price_update`

**Tags:** `part`

**Description:** Detail endpoint for PartSellPriceBreak model.

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
| `application/json`                  | [PartSalePrice](../part-api-schema.md#partsaleprice) |
| `application/x-www-form-urlencoded` | [PartSalePrice](../part-api-schema.md#partsaleprice) |
| `multipart/form-data`               | [PartSalePrice](../part-api-schema.md#partsaleprice) |

## Responses

| Status Code | Description | Schema                                               |
| ----------- | ----------- | ---------------------------------------------------- |
| 200         | OK          | [PartSalePrice](../part-api-schema.md#partsaleprice) |
