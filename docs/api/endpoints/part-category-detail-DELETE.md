---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/category/{id}/ — Delete Part Category
fetched: 2026-04-13
---

# `DELETE /api/part/category/{id}/` — Delete Part Category

**Operation ID:** `part_category_destroy`

**Tags:** `part`

**Description:** Custom delete method to pass kwargs.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part_category`, `r:delete:build`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 204 | No response body |  |
