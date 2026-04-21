---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: POST /api/company/ — Create Company
fetched: 2026-04-17
---

# `POST /api/company/` — Create Company

**Operation ID:** `company_create`

**Tags:** `company`

**Description:** Create a new Company object.

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
| `application/json`                  | [Company](../schemas/company.md) |
| `application/x-www-form-urlencoded` | [Company](../schemas/company.md) |
| `multipart/form-data`               | [Company](../schemas/company.md) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 201         | Created     | [Company](../schemas/company.md) |

## Notes

- The request body must include all required fields as defined in the [Company](../schemas/company.md) schema.
- Upon successful creation, the response includes the newly created Company object with its assigned `id`.
- See the Company schema documentation for field requirements, constraints, and default values.
