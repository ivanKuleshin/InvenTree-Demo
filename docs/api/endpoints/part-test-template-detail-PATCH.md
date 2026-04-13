---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/test-template/{id}/ — Partial Update Part Test Template
fetched: 2026-04-13
---

# `PATCH /api/part/test-template/{id}/` — Partial Update Part Test Template

**Operation ID:** `part_test_template_partial_update`

**Tags:** `part`

**Description:** Detail endpoint for PartTestTemplate model.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PatchedPartTestTemplate](../part-api-schema.md#patchedparttesttemplate) |
| `application/x-www-form-urlencoded` | [PatchedPartTestTemplate](../part-api-schema.md#patchedparttesttemplate) |
| `multipart/form-data` | [PatchedPartTestTemplate](../part-api-schema.md#patchedparttesttemplate) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartTestTemplate](../part-api-schema.md#parttesttemplate) |
