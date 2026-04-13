---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/related/{id}/ — Retrieve Part Relation
fetched: 2026-04-13
---

# `GET /api/part/related/{id}/` — Retrieve Part Relation

**Operation ID:** `part_related_retrieve`

**Tags:** `part`

**Description:** API endpoint for accessing detail view of a PartRelated object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description | Schema                                             |
| ----------- | ----------- | -------------------------------------------------- |
| 200         | OK          | [PartRelation](../part-api-schema.md#partrelation) |
