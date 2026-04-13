---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: DELETE /api/part/related/{id}/ — Delete Part Relation
fetched: 2026-04-13
---

# `DELETE /api/part/related/{id}/` — Delete Part Relation

**Operation ID:** `part_related_destroy`

**Tags:** `part`

**Description:** API endpoint for accessing detail view of a PartRelated object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:delete:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description      | Schema |
| ----------- | ---------------- | ------ |
| 204         | No response body |        |
