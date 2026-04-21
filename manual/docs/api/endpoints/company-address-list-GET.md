---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: GET /api/company/address/ — List Company Addresses
fetched: 2026-04-17
---

# `GET /api/company/address/` — List Company Addresses

**Operation ID:** `company_address_list`

**Tags:** `company`

**Description:** List API endpoint for Address objects.

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
| `ordering` | No       | string  | Which field to use when ordering the results. |
| `search`   | No       | string  | A search term for filtering addresses. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200         | OK          | [PaginatedAddressList](../schemas/paginated-address-list.md) |

## Notes

- Returns a paginated list of all company addresses.
- Use `limit` and `offset` parameters to control pagination.
- The `search` parameter can be used to filter addresses by company name, location, or other relevant fields.
- Use `ordering` to sort results.
