---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/internal-price/{id}/ — Partial Update Part Internal Price Break
fetched: 2026-04-13
---

# `PATCH /api/part/internal-price/{id}/` — Partial Update Part Internal Price Break

**Operation ID:** `part_internal_price_partial_update`

**Tags:** `part`

**Description:** Detail endpoint for PartInternalPriceBreak model.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Optional.**

| Content-Type                        | Schema                                                                     |
| ----------------------------------- | -------------------------------------------------------------------------- |
| `application/json`                  | [PatchedPartInternalPrice](../part-api-schema.md#patchedpartinternalprice) |
| `application/x-www-form-urlencoded` | [PatchedPartInternalPrice](../part-api-schema.md#patchedpartinternalprice) |
| `multipart/form-data`               | [PatchedPartInternalPrice](../part-api-schema.md#patchedpartinternalprice) |

## Responses

| Status Code | Description | Schema                                                       |
| ----------- | ----------- | ------------------------------------------------------------ |
| 200         | OK          | [PartInternalPrice](../part-api-schema.md#partinternalprice) |
