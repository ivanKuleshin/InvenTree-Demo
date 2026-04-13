---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/category/parameters/{id}/ — Partial Update Category Parameter Template
fetched: 2026-04-13
---

# `PATCH /api/part/category/parameters/{id}/` — Partial Update Category Parameter Template

**Operation ID:** `part_category_parameters_partial_update`

**Tags:** `part`

**Description:** Detail endpoint for the PartCategoryParameterTemplate model.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Optional.**

| Content-Type                        | Schema                                                                                     |
| ----------------------------------- | ------------------------------------------------------------------------------------------ |
| `application/json`                  | [PatchedCategoryParameterTemplate](../part-api-schema.md#patchedcategoryparametertemplate) |
| `application/x-www-form-urlencoded` | [PatchedCategoryParameterTemplate](../part-api-schema.md#patchedcategoryparametertemplate) |
| `multipart/form-data`               | [PatchedCategoryParameterTemplate](../part-api-schema.md#patchedcategoryparametertemplate) |

## Responses

| Status Code | Description | Schema                                                                       |
| ----------- | ----------- | ---------------------------------------------------------------------------- |
| 200         | OK          | [CategoryParameterTemplate](../part-api-schema.md#categoryparametertemplate) |
