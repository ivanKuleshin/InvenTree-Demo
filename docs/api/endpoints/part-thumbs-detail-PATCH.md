---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/thumbs/{id}/ — Partial Update Part Thumbnail
fetched: 2026-04-13
---

# `PATCH /api/part/thumbs/{id}/` — Partial Update Part Thumbnail

**Operation ID:** `part_thumbs_partial_update`

**Tags:** `part`

**Description:** API endpoint for updating Part thumbnails.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`, `r:change:build`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Request Body

**Optional.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PatchedPartThumbSerializerUpdate](../part-api-schema.md#patchedpartthumbserializerupdate) |
| `application/x-www-form-urlencoded` | [PatchedPartThumbSerializerUpdate](../part-api-schema.md#patchedpartthumbserializerupdate) |
| `multipart/form-data` | [PatchedPartThumbSerializerUpdate](../part-api-schema.md#patchedpartthumbserializerupdate) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |
