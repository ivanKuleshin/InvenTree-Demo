---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/category/{id}/ — Update Part Category
fetched: 2026-04-13
---

# `PUT /api/part/category/{id}/` — Update Part Category

**Operation ID:** `part_category_update`

**Tags:** `part`

**Description:** Custom put method to pass kwargs.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`, `r:change:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Required.**

| Content-Type                        | Schema                                     |
| ----------------------------------- | ------------------------------------------ |
| `application/json`                  | [Category](../part-api/part-api-schema.md#category) |
| `application/x-www-form-urlencoded` | [Category](../part-api/part-api-schema.md#category) |
| `multipart/form-data`               | [Category](../part-api/part-api-schema.md#category) |

## Responses

| Status Code | Description | Schema                                     |
| ----------- | ----------- | ------------------------------------------ |
| 200         | OK          | [Category](../part-api/part-api-schema.md#category) |
