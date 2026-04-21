---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: DELETE /api/company/address/ — Bulk Delete Addresses
fetched: 2026-04-17
---

# `DELETE /api/company/address/` — Bulk Delete Addresses

**Operation ID:** `company_address_bulk_destroy`

**Tags:** `company`

**Description:** Perform a bulk DELETE operation on multiple Address objects.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Request Body

**Required.** An array of Address IDs to delete.

| Content-Type       | Schema |
|--------------------|--------|
| `application/json` | Array of integers (address IDs) |

## Responses

| Status Code | Description |
|-------------|-------------|
| 204         | No Content (bulk deletion successful) |
| 400         | Bad Request   |

## Notes

- This endpoint allows deletion of multiple addresses in a single request.
- The request body should contain a list of address IDs to be deleted.
- Upon successful bulk deletion, a 204 No Content response is returned.
- Failed deletions may result in a 400 Bad Request error with details about which deletions failed.
- Use with caution as this operation affects multiple records.
