---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/{id}/requirements/ — Retrieve Part Requirements
fetched: 2026-04-13
---

# `GET /api/part/{id}/requirements/` — Retrieve Part Requirements

**Operation ID:** `part_requirements_retrieve`

**Tags:** `part`

**Description:** API endpoint detailing 'requirements' information for a particular part.

This endpoint returns information on upcoming requirements for:

- Sales Orders
- Build Orders
- Total requirements
- How many of this part can be assembled with available stock

As this data is somewhat complex to calculate, it is not included in the default API response.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`, `r:view:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Responses

| Status Code | Description | Schema                                                     |
| ----------- | ----------- | ---------------------------------------------------------- |
| 200         | OK          | [PartRequirements](../part-api/part-api-schema.md#partrequirements) |
