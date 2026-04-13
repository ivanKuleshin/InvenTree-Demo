---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/thumbs/{id}/ — Retrieve Part Thumbnail
fetched: 2026-04-13
---

# `GET /api/part/thumbs/{id}/` — Retrieve Part Thumbnail

**Operation ID:** `part_thumbs_retrieve`

**Tags:** `part`

**Description:** API endpoint for updating Part thumbnails.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`, `r:view:build`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |
