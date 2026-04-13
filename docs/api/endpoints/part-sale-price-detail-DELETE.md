---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/sale-price/{id}/ — Delete Part Sale Price Break
fetched: 2026-04-13
---

# `DELETE /api/part/sale-price/{id}/` — Delete Part Sale Price Break

**Operation ID:** `part_sale_price_destroy`

**Tags:** `part`

**Description:** Detail endpoint for PartSellPriceBreak model.

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
