---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: GET /api/company/ — List Companies
fetched: 2026-04-17
---

# `GET /api/company/` — List Companies

**Operation ID:** `company_list`

**Tags:** `company`

**Description:** List API endpoint for Company objects.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Query Parameters

| Name       | Required | Type    | Description |
|------------|----------|---------|-------------|
| `limit`    | Yes      | integer | Number of results to return per page. |
| `offset`   | No       | integer | The initial index from which to return the results. |
| `ordering` | No       | string  | Which field to use when ordering the results. Allowed values: `name`, `-name`, `created`, `-created`, `updated`, `-updated` |
| `search`   | No       | string  | A search term. Searched fields: name, description. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [PaginatedCompanyList](../schemas/paginated-company-list.md) |

## Notes

- Results are paginated. Use `limit` and `offset` parameters to control pagination.
- The `search` parameter performs a full-text search across the `name` and `description` fields.
- Use `ordering` to sort results by name or date (created/updated). Prefix with `-` for descending order.
