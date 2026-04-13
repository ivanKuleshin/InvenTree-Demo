---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/related/{id}/ — Partial Update Part Relation
fetched: 2026-04-13
---

# `PATCH /api/part/related/{id}/` — Partial Update Part Relation

**Operation ID:** `part_related_partial_update`

**Tags:** `part`

**Description:** API endpoint for accessing detail view of a PartRelated object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Optional.**

| Content-Type                        | Schema                                                           |
| ----------------------------------- | ---------------------------------------------------------------- |
| `application/json`                  | [PatchedPartRelation](../part-api-schema.md#patchedpartrelation) |
| `application/x-www-form-urlencoded` | [PatchedPartRelation](../part-api-schema.md#patchedpartrelation) |
| `multipart/form-data`               | [PatchedPartRelation](../part-api-schema.md#patchedpartrelation) |

## Responses

| Status Code | Description | Schema                                             |
| ----------- | ----------- | -------------------------------------------------- |
| 200         | OK          | [PartRelation](../part-api-schema.md#partrelation) |
