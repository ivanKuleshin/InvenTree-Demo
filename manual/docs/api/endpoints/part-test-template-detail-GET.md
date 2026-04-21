---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/test-template/{id}/ — Retrieve Part Test Template
fetched: 2026-04-13
---

# `GET /api/part/test-template/{id}/` — Retrieve Part Test Template

**Operation ID:** `part_test_template_retrieve`

**Tags:** `part`

**Description:** Detail endpoint for PartTestTemplate model.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description | Schema                                                     |
| ----------- | ----------- | ---------------------------------------------------------- |
| 200         | OK          | [PartTestTemplate](../part-api/part-api-schema.md#parttesttemplate) |
