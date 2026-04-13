---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/category/parameters/{id}/ — Update Category Parameter Template
fetched: 2026-04-13
---

# `PUT /api/part/category/parameters/{id}/` — Update Category Parameter Template

**Operation ID:** `part_category_parameters_update`

**Tags:** `part`

**Description:** Detail endpoint for the PartCategoryParameterTemplate model.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

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
| 200 | OK | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |
