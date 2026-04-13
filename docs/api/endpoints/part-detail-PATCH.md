---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/{id}/ — Partial Update Part
fetched: 2026-04-13
---

# `PATCH /api/part/{id}/` — Partial Update Part

**Operation ID:** `part_partial_update`

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

**Optional.**

| Content-Type                        | Schema                                           |
| ----------------------------------- | ------------------------------------------------ |
| `application/json`                  | [PatchedPart](../part-api-schema.md#patchedpart) |
| `application/x-www-form-urlencoded` | [PatchedPart](../part-api-schema.md#patchedpart) |
| `multipart/form-data`               | [PatchedPart](../part-api-schema.md#patchedpart) |

## Responses

| Status Code | Description | Schema                             |
| ----------- | ----------- | ---------------------------------- |
| 200         | OK          | [Part](../part-api-schema.md#part) |
