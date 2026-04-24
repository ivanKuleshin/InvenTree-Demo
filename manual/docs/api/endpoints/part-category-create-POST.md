---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/category/ — Create Part Category
fetched: 2026-04-13
---

# `POST /api/part/category/` — Create Part Category

**Operation ID:** `part_category_create`

**Tags:** `part`

**Description:** API endpoint for accessing a list of PartCategory objects.

- GET: Return a list of PartCategory objects
- POST: Create a new PartCategory object

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part_category`, `r:add:build`

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
| 201         | Created     | [Category](../part-api/part-api-schema.md#category) |
