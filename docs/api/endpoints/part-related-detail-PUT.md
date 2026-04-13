---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/related/{id}/ — Update Part Relation
fetched: 2026-04-13
---

# `PUT /api/part/related/{id}/` — Update Part Relation

**Operation ID:** `part_related_update`

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

**Required.**

| Content-Type                        | Schema                                             |
| ----------------------------------- | -------------------------------------------------- |
| `application/json`                  | [PartRelation](../part-api-schema.md#partrelation) |
| `application/x-www-form-urlencoded` | [PartRelation](../part-api-schema.md#partrelation) |
| `multipart/form-data`               | [PartRelation](../part-api-schema.md#partrelation) |

## Responses

| Status Code | Description | Schema                                             |
| ----------- | ----------- | -------------------------------------------------- |
| 200         | OK          | [PartRelation](../part-api-schema.md#partrelation) |
