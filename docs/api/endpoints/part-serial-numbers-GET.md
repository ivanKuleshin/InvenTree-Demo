---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/{id}/serial-numbers/ — Retrieve Part Serial Number Info
fetched: 2026-04-13
---

# `GET /api/part/{id}/serial-numbers/` — Retrieve Part Serial Number Info

**Operation ID:** `part_serial_numbers_retrieve`

**Tags:** `part`

**Description:** API endpoint for returning extra serial number information about a particular part.

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
| 200         | OK          | [PartSerialNumber](../part-api-schema.md#partserialnumber) |
