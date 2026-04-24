---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PUT /api/part/{id}/bom-validate/ — Update BOM Validation
fetched: 2026-04-13
---

# `PUT /api/part/{id}/bom-validate/` — Update BOM Validation

**Operation ID:** `part_bom_validate_update`

**Tags:** `part`

**Description:** API endpoint for 'validating' the BOM for a given Part.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:change:part`, `r:change:build`

## Path Parameters

| Name | In   | Type    | Required | Description |
| ---- | ---- | ------- | -------- | ----------- |
| `id` | path | integer | **yes**  |             |

## Request Body

**Optional.**

| Content-Type                        | Schema                                                   |
| ----------------------------------- | -------------------------------------------------------- |
| `application/json`                  | [PartBomValidate](../part-api/part-api-schema.md#partbomvalidate) |
| `application/x-www-form-urlencoded` | [PartBomValidate](../part-api/part-api-schema.md#partbomvalidate) |
| `multipart/form-data`               | [PartBomValidate](../part-api/part-api-schema.md#partbomvalidate) |

## Responses

| Status Code | Description | Schema                                                   |
| ----------- | ----------- | -------------------------------------------------------- |
| 200         | OK          | [PartBomValidate](../part-api/part-api-schema.md#partbomvalidate) |
