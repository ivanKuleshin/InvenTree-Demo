---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/{id}/bom-validate/ — Retrieve BOM Validation Status
fetched: 2026-04-13
---

# `GET /api/part/{id}/bom-validate/` — Retrieve BOM Validation Status

**Operation ID:** `part_bom_validate_retrieve`

**Tags:** `part`

**Description:** API endpoint for 'validating' the BOM for a given Part.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`, `r:view:build`

## Path Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `id` | path | integer | **yes** |  |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PartBomValidate](../part-api-schema.md#partbomvalidate) |
