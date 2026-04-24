---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PUT /api/company/address/{id}/ — Update Address (Full)
fetched: 2026-04-17
---

# `PUT /api/company/address/{id}/` — Update Address (Full)

**Operation ID:** `company_address_update`

**Tags:** `company`

**Description:** Update all fields of an existing Address object (full replacement).

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Address object to update. |

## Request Body

**Required.** All required fields must be provided.

| Content-Type                        | Schema |
|-------------------------------------|--------|
| `application/json`                  | [Address](../schemas/address.md) |
| `application/x-www-form-urlencoded` | [Address](../schemas/address.md) |
| `multipart/form-data`               | [Address](../schemas/address.md) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [Address](../schemas/address.md) |
| 404         | Not Found   | Error response |

## Notes

- PUT requires all required fields to be provided in the request body.
- Any fields not included in the request will be reset to their default values or become null (if nullable).
- For partial updates, use the PATCH endpoint instead.
- See the Address schema documentation for field requirements and constraints.
