---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/internal-price/{id}/ — Retrieve Part Internal Price Break
fetched: 2026-04-13
---

# `GET /api/part/internal-price/{id}/` — Retrieve Part Internal Price Break

**Operation ID:** `part_internal_price_retrieve`

**Tags:** `part`

**Description:** Detail endpoint for PartInternalPriceBreak model.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description | Schema                                                       |
| ----------- | ----------- | ------------------------------------------------------------ |
| 200         | OK          | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
