---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/category/{id}/ — Retrieve Part Category
fetched: 2026-04-13
---

# `GET /api/part/category/{id}/` — Retrieve Part Category

**Operation ID:** `part_category_retrieve`

**Tags:** `part`

**Description:** Custom get method to pass kwargs.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part_category`, `r:view:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Query Parameters

| Name          | In    | Type    | Required | Description        |
| ------------- | ----- | ------- | -------- | ------------------ |
| `path_detail` | query | boolean | no       | (default: `False`) |

## Responses

| Status Code | Description | Schema                                     |
| ----------- | ----------- | ------------------------------------------ |
| 200         | OK          | [Category](../part-api/part-api-schema.md#category) |
