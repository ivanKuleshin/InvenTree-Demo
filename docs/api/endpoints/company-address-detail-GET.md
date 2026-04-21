---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: GET /api/company/address/{id}/ — Get Address Detail
fetched: 2026-04-17
---

# `GET /api/company/address/{id}/` — Get Address Detail

**Operation ID:** `company_address_retrieve`

**Tags:** `company`

**Description:** Retrieve details of a single Address object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Address object. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [Address](../schemas/address.md) |
| 404         | Not Found   | Error response |

## Notes

- Returns the complete Address object including all fields and metadata.
- Use this endpoint to fetch detailed information about a specific company address by its ID.
