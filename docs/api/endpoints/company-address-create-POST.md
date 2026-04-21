---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: POST /api/company/address/ — Create Company Address
fetched: 2026-04-17
---

# `POST /api/company/address/` — Create Company Address

**Operation ID:** `company_address_create`

**Tags:** `company`

**Description:** Create a new Address object for a company.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Request Body

**Required.**

| Content-Type                        | Schema |
|-------------------------------------|--------|
| `application/json`                  | [Address](../schemas/address.md) |
| `application/x-www-form-urlencoded` | [Address](../schemas/address.md) |
| `multipart/form-data`               | [Address](../schemas/address.md) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201         | Created     | [Address](../schemas/address.md) |

## Notes

- The request body must include all required fields as defined in the [Address](../schemas/address.md) schema.
- Addresses are associated with a company via the company ID field.
- Upon successful creation, the response includes the newly created Address object with its assigned `id`.
- See the Address schema documentation for field requirements and constraints.
