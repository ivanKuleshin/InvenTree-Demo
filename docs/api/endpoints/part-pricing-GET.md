---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/{id}/pricing/ — Retrieve Part Pricing
fetched: 2026-04-13
---

# `GET /api/part/{id}/pricing/` — Retrieve Part Pricing

**Operation ID:** `part_pricing_retrieve`

**Tags:** `part`

**Description:** API endpoint for viewing part pricing data.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartPricing](../part-api-schema.md#partpricing) |
