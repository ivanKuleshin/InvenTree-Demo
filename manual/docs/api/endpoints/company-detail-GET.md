---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: GET /api/company/{id}/ — Get Company Detail
fetched: 2026-04-17
---

# `GET /api/company/{id}/` — Get Company Detail

**Operation ID:** `company_retrieve`

**Tags:** `company`

**Description:** Retrieve details of a single Company object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Company object. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [Company](../schemas/company.md) |
| 404         | Not Found   | Error response |

## Notes

- Returns the complete Company object including all fields and metadata.
- Use this endpoint to fetch detailed information about a specific company by its ID.
