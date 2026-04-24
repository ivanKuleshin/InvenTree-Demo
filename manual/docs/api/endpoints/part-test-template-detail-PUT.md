---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/test-template/{id}/ — Update Part Test Template
fetched: 2026-04-13
---

# `PUT /api/part/test-template/{id}/` — Update Part Test Template

**Operation ID:** `part_test_template_update`

**Tags:** `part`

**Description:** Detail endpoint for PartTestTemplate model.

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

| Content-Type                        | Schema                                                     |
| ----------------------------------- | ---------------------------------------------------------- |
| `application/json`                  | [PartTestTemplate](../part-api/part-api-schema.md#parttesttemplate) |
| `application/x-www-form-urlencoded` | [PartTestTemplate](../part-api/part-api-schema.md#parttesttemplate) |
| `multipart/form-data`               | [PartTestTemplate](../part-api/part-api-schema.md#parttesttemplate) |

## Responses

| Status Code | Description | Schema                                                     |
| ----------- | ----------- | ---------------------------------------------------------- |
| 200         | OK          | [PartTestTemplate](../part-api/part-api-schema.md#parttesttemplate) |
