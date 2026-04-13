---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/category/parameters/ — Create Category Parameter Template
fetched: 2026-04-13
---

# `POST /api/part/category/parameters/` — Create Category Parameter Template

**Operation ID:** `part_category_parameters_create`

**Tags:** `part`

**Description:** API endpoint for accessing a list of PartCategoryParameterTemplate objects.

- GET: Return a list of PartCategoryParameterTemplate objects

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part_category`

## Request Body

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |
| `application/x-www-form-urlencoded` | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |
| `multipart/form-data` | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201 | Created | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |
