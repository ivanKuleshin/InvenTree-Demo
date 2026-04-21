---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/category/ — Bulk Update Part Categories
fetched: 2026-04-13
---

# `PUT /api/part/category/` — Bulk Update Part Categories

**Operation ID:** `part_category_bulk_update`

**Tags:** `part`

**Description:** Perform a PUT operation against this list endpoint. Simply redirects to the PATCH method.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`, `r:change:build`

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
