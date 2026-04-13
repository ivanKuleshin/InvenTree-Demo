---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/category/parameters/ — List Category Parameter Templates
fetched: 2026-04-13
---

# `GET /api/part/category/parameters/` — List Category Parameter Templates

**Operation ID:** `part_category_parameters_list`

**Tags:** `part`

**Description:** Override the GET method to determine export options.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part_category`

## Query Parameters

| Name     | In    | Type    | Required | Description                                         |
| -------- | ----- | ------- | -------- | --------------------------------------------------- |
| `limit`  | query | integer | **yes**  | Number of results to return per page.               |
| `offset` | query | integer | no       | The initial index from which to return the results. |

## Responses

| Status Code | Description | Schema                                                                                                 |
| ----------- | ----------- | ------------------------------------------------------------------------------------------------------ |
| 200         | OK          | [PaginatedCategoryParameterTemplateList](../part-api-schema.md#paginatedcategoryparametertemplatelist) |
