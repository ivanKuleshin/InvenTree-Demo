---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/category/ — List Part Categories
fetched: 2026-04-13
---

# `GET /api/part/category/` — List Part Categories

**Operation ID:** `part_category_list`

**Tags:** `part`

**Description:** Override the GET method to determine export options.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part_category`, `r:view:build`

## Query Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `cascade` | query | boolean | no | Include sub-categories in filtered results |
| `depth` | query | number | no | Filter by category depth |
| `exclude_tree` | query | integer | no |  |
| `limit` | query | integer | **yes** | Number of results to return per page. |
| `name` | query | string | no |  |
| `offset` | query | integer | no | The initial index from which to return the results. |
| `ordering` | query | enum | no | Which field to use when ordering the results. Values: `name`, `-name`, `pathstring`, `-pathstring`, `level`, `-level`, `tree_id`, `-tree_id`, `lft`, `-lft`, `part_count`, `-part_count` |
| `parent` | query | integer | no | Filter by parent category |
| `path_detail` | query | boolean | no |  (default: `False`) |
| `search` | query | string | no | A search term. Searched fields: description, name, pathstring. |
| `starred` | query | boolean | no | Filter by starred categories |
| `structural` | query | boolean | no |  |
| `top_level` | query | boolean | no | Filter by top-level categories |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PaginatedCategoryList](../part-api-schema.md#paginatedcategorylist) |
