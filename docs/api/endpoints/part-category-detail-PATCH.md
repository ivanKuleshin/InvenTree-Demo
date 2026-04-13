---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/category/{id}/ — Partial Update Part Category
fetched: 2026-04-13
---

# `PATCH /api/part/category/{id}/` — Partial Update Part Category

**Operation ID:** `part_category_partial_update`

**Tags:** `part`

**Description:** Custom patch method to pass kwargs.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part_category`, `r:change:build`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PatchedCategory](../part-api-schema.md#patchedcategory) |
| `application/x-www-form-urlencoded` | [PatchedCategory](../part-api-schema.md#patchedcategory) |
| `multipart/form-data` | [PatchedCategory](../part-api-schema.md#patchedcategory) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [Category](../part-api-schema.md#category) |
