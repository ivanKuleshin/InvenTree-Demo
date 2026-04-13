---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/test-template/ — Create Part Test Template
fetched: 2026-04-13
---

# `POST /api/part/test-template/` — Create Part Test Template

**Operation ID:** `part_test_template_create`

**Tags:** `part`

**Description:** API endpoint for listing (and creating) a PartTestTemplate.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part`

## Request Body

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PartTestTemplate](../part-api-schema.md#parttesttemplate) |
| `application/x-www-form-urlencoded` | [PartTestTemplate](../part-api-schema.md#parttesttemplate) |
| `multipart/form-data` | [PartTestTemplate](../part-api-schema.md#parttesttemplate) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201 | Created | [PartTestTemplate](../part-api-schema.md#parttesttemplate) |
