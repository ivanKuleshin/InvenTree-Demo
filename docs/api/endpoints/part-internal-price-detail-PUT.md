---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/internal-price/{id}/ — Update Part Internal Price Break
fetched: 2026-04-13
---

# `PUT /api/part/internal-price/{id}/` — Update Part Internal Price Break

**Operation ID:** `part_internal_price_update`

**Tags:** `part`

**Description:** Detail endpoint for PartInternalPriceBreak model.

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

**Required.**

| Content-Type | Schema |
|---|---|
| `application/json` | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
| `application/x-www-form-urlencoded` | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
| `multipart/form-data` | [PartInternalPrice](../part-api-schema.md#partinternalprice) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
