---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/sale-price/ — Create Part Sale Price Break
fetched: 2026-04-13
---

# `POST /api/part/sale-price/` — Create Part Sale Price Break

**Operation ID:** `part_sale_price_create`

**Tags:** `part`

**Description:** API endpoint for list view of PartSalePriceBreak model.

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
| `application/json`                  | [PartSalePrice](../part-api-schema.md#partsaleprice) |
| `application/x-www-form-urlencoded` | [PartSalePrice](../part-api-schema.md#partsaleprice) |
| `multipart/form-data`               | [PartSalePrice](../part-api-schema.md#partsaleprice) |

## Responses

| Status Code | Description | Schema                                               |
| ----------- | ----------- | ---------------------------------------------------- |
| 201         | Created     | [PartSalePrice](../part-api-schema.md#partsaleprice) |
