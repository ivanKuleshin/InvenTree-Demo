---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/related/ — Create Part Relation
fetched: 2026-04-13
---

# `POST /api/part/related/` — Create Part Relation

**Operation ID:** `part_related_create`

**Tags:** `part`

**Description:** API endpoint for accessing a list of PartRelated objects.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part`

## Request Body

**Required.**

| Content-Type                        | Schema                                             |
| ----------------------------------- | -------------------------------------------------- |
| `application/json`                  | [PartRelation](../part-api/part-api-schema.md#partrelation) |
| `application/x-www-form-urlencoded` | [PartRelation](../part-api/part-api-schema.md#partrelation) |
| `multipart/form-data`               | [PartRelation](../part-api/part-api-schema.md#partrelation) |

## Responses

| Status Code | Description | Schema                                             |
| ----------- | ----------- | -------------------------------------------------- |
| 201         | Created     | [PartRelation](../part-api/part-api-schema.md#partrelation) |
