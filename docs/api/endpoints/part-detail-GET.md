---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/{id}/ — Retrieve Part
fetched: 2026-04-13
---

# `GET /api/part/{id}/` — Retrieve Part

**Operation ID:** `part_retrieve`

**Tags:** `part`

**Description:** API endpoint for detail view of a single Part object.

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

## Query Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `category_detail` | query | boolean | no |  (default: `False`) |
| `location_detail` | query | boolean | no | Include detailed information about the stock location in the response (default: `False`) |
| `parameters` | query | boolean | no | Include part parameters in response (default: `False`) |
| `path_detail` | query | boolean | no |  (default: `False`) |
| `price_breaks` | query | boolean | no |  (default: `False`) |
| `tags` | query | boolean | no |  (default: `False`) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [Part](../part-api-schema.md#part) |
