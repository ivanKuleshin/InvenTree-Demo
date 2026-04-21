---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PUT /api/company/{id}/ — Update Company (Full)
fetched: 2026-04-17
---

# `PUT /api/company/{id}/` — Update Company (Full)

**Operation ID:** `company_update`

**Tags:** `company`

**Description:** Update all fields of an existing Company object (full replacement).

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Company object to update. |

## Request Body

**Required.** All required fields must be provided.

| Content-Type                        | Schema |
|-------------------------------------|--------|
| `application/json`                  | [Company](../schemas/company.md) |
| `application/x-www-form-urlencoded` | [Company](../schemas/company.md) |
| `multipart/form-data`               | [Company](../schemas/company.md) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [Company](../schemas/company.md) |
| 404         | Not Found   | Error response |

## Notes

- PUT requires all required fields to be provided in the request body.
- Any fields not included in the request will be reset to their default values or become null (if nullable).
- For partial updates, use the PATCH endpoint instead.
- See the Company schema documentation for field requirements and constraints.
