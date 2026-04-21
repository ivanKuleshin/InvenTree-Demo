---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/ — Create Part
fetched: 2026-04-13
---

# `POST /api/part/` — Create Part

**Operation ID:** `part_create`

**Tags:** `part`

**Description:** API endpoint for accessing a list of Part objects, or creating a new Part instance.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part`, `r:add:build`

## Request Body

**Required.**

| Content-Type                        | Schema                             |
| ----------------------------------- | ---------------------------------- |
| `application/json`                  | [Part](../part-api/part-api-schema.md#part) |
| `application/x-www-form-urlencoded` | [Part](../part-api/part-api-schema.md#part) |
| `multipart/form-data`               | [Part](../part-api/part-api-schema.md#part) |

## Responses

| Status Code | Description | Schema                             |
| ----------- | ----------- | ---------------------------------- |
| 201         | Created     | [Part](../part-api/part-api-schema.md#part) |
