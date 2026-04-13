---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/sale-price/ — List Part Sale Price Breaks
fetched: 2026-04-13
---

# `GET /api/part/sale-price/` — List Part Sale Price Breaks

**Operation ID:** `part_sale_price_list`

**Tags:** `part`

**Description:** Override the GET method to determine export options.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Query Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `limit` | query | integer | **yes** | Number of results to return per page. |
| `offset` | query | integer | no | The initial index from which to return the results. |
| `ordering` | query | enum | no | Which field to use when ordering the results. Values: `quantity`, `-quantity`, `price`, `-price` |
| `part` | query | integer | no |  |
| `search` | query | string | no | A search term. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PaginatedPartSalePriceList](../part-api-schema.md#paginatedpartsalepricelist) |
