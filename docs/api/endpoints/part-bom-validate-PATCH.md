---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: PATCH /api/part/{id}/bom-validate/ — Partial Update BOM Validation
fetched: 2026-04-13
---

# `PATCH /api/part/{id}/bom-validate/` — Partial Update BOM Validation

**Operation ID:** `part_bom_validate_partial_update`

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

| Content-Type                        | Schema                                                                 |
| ----------------------------------- | ---------------------------------------------------------------------- |
| `application/json`                  | [PatchedPartBomValidate](../part-api/part-api-schema.md#patchedpartbomvalidate) |
| `application/x-www-form-urlencoded` | [PatchedPartBomValidate](../part-api/part-api-schema.md#patchedpartbomvalidate) |
| `multipart/form-data`               | [PatchedPartBomValidate](../part-api/part-api-schema.md#patchedpartbomvalidate) |

## Responses

| Status Code | Description | Schema                                                   |
| ----------- | ----------- | -------------------------------------------------------- |
| 200         | OK          | [PartBomValidate](../part-api/part-api-schema.md#partbomvalidate) |
