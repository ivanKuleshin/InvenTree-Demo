---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/{id}/pricing/ — Update Part Pricing
fetched: 2026-04-13
---

# `PUT /api/part/{id}/pricing/` — Update Part Pricing

**Operation ID:** `part_pricing_update`

**Tags:** `part`

**Description:** API endpoint for viewing part pricing data.

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

| Content-Type                        | Schema                                           |
| ----------------------------------- | ------------------------------------------------ |
| `application/json`                  | [PartPricing](../part-api/part-api-schema.md#partpricing) |
| `application/x-www-form-urlencoded` | [PartPricing](../part-api/part-api-schema.md#partpricing) |
| `multipart/form-data`               | [PartPricing](../part-api/part-api-schema.md#partpricing) |

## Responses

| Status Code | Description | Schema                                           |
| ----------- | ----------- | ------------------------------------------------ |
| 200         | OK          | [PartPricing](../part-api/part-api-schema.md#partpricing) |
