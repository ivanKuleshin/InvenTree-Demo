---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/stocktake/ — List Part Stocktakes
fetched: 2026-04-13
---

# `GET /api/part/stocktake/` — List Part Stocktakes

**Operation ID:** `part_stocktake_list`

**Tags:** `part`

**Description:** Override the GET method to determine export options.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Query Parameters

| Name       | In    | Type    | Required | Description                                                                                                                                                                |
| ---------- | ----- | ------- | -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `limit`    | query | integer | **yes**  | Number of results to return per page.                                                                                                                                      |
| `offset`   | query | integer | no       | The initial index from which to return the results.                                                                                                                        |
| `ordering` | query | enum    | no       | Which field to use when ordering the results. Values: `part`, `-part`, `item_count`, `-item_count`, `quantity`, `-quantity`, `date`, `-date`, `user`, `-user`, `pk`, `-pk` |
| `part`     | query | integer | no       |                                                                                                                                                                            |

## Responses

| Status Code | Description | Schema                                                                         |
| ----------- | ----------- | ------------------------------------------------------------------------------ |
| 200         | OK          | [PaginatedPartStocktakeList](../part-api/part-api-schema.md#paginatedpartstocktakelist) |
