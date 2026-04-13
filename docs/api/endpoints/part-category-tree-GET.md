---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/category/tree/ — List Part Category Tree
fetched: 2026-04-13
---

# `GET /api/part/category/tree/` — List Part Category Tree

**Operation ID:** `part_category_tree_list`

**Tags:** `part`

**Description:** API endpoint for accessing a list of PartCategory objects ready for rendering a tree.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part_category`, `r:view:build`

## Query Parameters

| Name       | In    | Type    | Required | Description                                                                                                                 |
| ---------- | ----- | ------- | -------- | --------------------------------------------------------------------------------------------------------------------------- |
| `limit`    | query | integer | **yes**  | Number of results to return per page.                                                                                       |
| `offset`   | query | integer | no       | The initial index from which to return the results.                                                                         |
| `ordering` | query | enum    | no       | Which field to use when ordering the results. Values: `level`, `-level`, `name`, `-name`, `subcategories`, `-subcategories` |

## Responses

| Status Code | Description | Schema                                                                       |
| ----------- | ----------- | ---------------------------------------------------------------------------- |
| 200         | OK          | [PaginatedCategoryTreeList](../part-api-schema.md#paginatedcategorytreelist) |
