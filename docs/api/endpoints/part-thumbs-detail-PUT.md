---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/thumbs/{id}/ — Update Part Thumbnail
fetched: 2026-04-13
---

# `PUT /api/part/thumbs/{id}/` — Update Part Thumbnail

**Operation ID:** `part_thumbs_update`

**Tags:** `part`

**Description:** API endpoint for updating Part thumbnails.

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

**Required.**

| Content-Type                        | Schema                                                                       |
| ----------------------------------- | ---------------------------------------------------------------------------- |
| `application/json`                  | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |
| `application/x-www-form-urlencoded` | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |
| `multipart/form-data`               | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |

## Responses

| Status Code | Description | Schema                                                                       |
| ----------- | ----------- | ---------------------------------------------------------------------------- |
| 200         | OK          | [PartThumbSerializerUpdate](../part-api-schema.md#partthumbserializerupdate) |
