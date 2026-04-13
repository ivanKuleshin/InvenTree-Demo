---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/category/parameters/{id}/ — Delete Category Parameter Template
fetched: 2026-04-13
---

# `DELETE /api/part/category/parameters/{id}/` — Delete Category Parameter Template

**Operation ID:** `part_category_parameters_destroy`

**Tags:** `part`

**Description:** Detail endpoint for the PartCategoryParameterTemplate model.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part_category`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 204 | No response body |  |
