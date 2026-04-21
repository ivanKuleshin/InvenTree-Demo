---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PATCH /api/company/{id}/ — Update Company (Partial)
fetched: 2026-04-17
---

# `PATCH /api/company/{id}/` — Update Company (Partial)

**Operation ID:** `company_partial_update`

**Tags:** `company`

**Description:** Partially update an existing Company object (only provided fields are updated).

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

**Optional.** Only include fields that need to be updated.

| Content-Type                        | Schema |
|-------------------------------------|--------|
| `application/json`                  | [PatchedCompany](../schemas/patched-company.md) |
| `application/x-www-form-urlencoded` | [PatchedCompany](../schemas/patched-company.md) |
| `multipart/form-data`               | [PatchedCompany](../schemas/patched-company.md) |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [Company](../schemas/company.md) |
| 404         | Not Found   | Error response |

## Notes

- PATCH allows partial updates; only the fields included in the request body will be modified.
- Fields not included in the request remain unchanged.
- For full replacement (requiring all required fields), use the PUT endpoint instead.
- See the Company schema documentation for field types and constraints.
