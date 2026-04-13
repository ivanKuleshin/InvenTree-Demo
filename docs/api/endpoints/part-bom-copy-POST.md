---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/{id}/bom-copy/ — Copy BOM
fetched: 2026-04-13
---

# `POST /api/part/{id}/bom-copy/` — Copy BOM

**Operation ID:** `part_bom_copy_create`

**Tags:** `part`

**Description:** API endpoint for duplicating a BOM.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Request Body

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PartCopyBOM](../part-api-schema.md#partcopybom) |
| `application/x-www-form-urlencoded` | [PartCopyBOM](../part-api-schema.md#partcopybom) |
| `multipart/form-data` | [PartCopyBOM](../part-api-schema.md#partcopybom) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201 | Created | [PartCopyBOM](../part-api-schema.md#partcopybom) |
