---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: POST /api/part/internal-price/ — Create Part Internal Price Break
fetched: 2026-04-13
---

# `POST /api/part/internal-price/` — Create Part Internal Price Break

**Operation ID:** `part_internal_price_create`

**Tags:** `part`

**Description:** API endpoint for list view of PartInternalPriceBreak model.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:add:part`

## Request Body

**Required.**

| Content-Type                        | Schema                                                       |
| ----------------------------------- | ------------------------------------------------------------ |
| `application/json`                  | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
| `application/x-www-form-urlencoded` | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
| `multipart/form-data`               | [PartInternalPrice](../part-api-schema.md#partinternalprice) |

## Responses

| Status Code | Description | Schema                                                       |
| ----------- | ----------- | ------------------------------------------------------------ |
| 201         | Created     | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
