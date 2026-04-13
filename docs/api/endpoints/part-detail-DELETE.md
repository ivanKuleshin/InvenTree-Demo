---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/{id}/ — Delete Part
fetched: 2026-04-13
---

# `DELETE /api/part/{id}/` — Delete Part

**Operation ID:** `part_destroy`

**Tags:** `part`

**Description:** API endpoint for detail view of a single Part object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part`, `r:delete:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description      | Schema |
| ----------- | ---------------- | ------ |
| 204         | No response body |        |
