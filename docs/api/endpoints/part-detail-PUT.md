---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/{id}/ — Update Part
fetched: 2026-04-13
---

# `PUT /api/part/{id}/` — Update Part

**Operation ID:** `part_update`

**Tags:** `part`

**Description:** API endpoint for detail view of a single Part object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`, `r:change:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

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
| 200         | OK          | [Part](../part-api/part-api-schema.md#part) |
