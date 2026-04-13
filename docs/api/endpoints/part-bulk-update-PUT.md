---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/ — Bulk Update Parts
fetched: 2026-04-13
---

# `PUT /api/part/` — Bulk Update Parts

**Operation ID:** `part_bulk_update`

**Tags:** `part`

**Description:** Perform a PUT operation against this list endpoint. Simply redirects to the PATCH method.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`, `r:change:build`

## Request Body

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [Part](../part-api-schema.md#part) |
| `application/x-www-form-urlencoded` | [Part](../part-api-schema.md#part) |
| `multipart/form-data` | [Part](../part-api-schema.md#part) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [Part](../part-api-schema.md#part) |
