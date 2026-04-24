---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: DELETE /api/company/address/{id}/ — Delete Address
fetched: 2026-04-17
---

# `DELETE /api/company/address/{id}/` — Delete Address

**Operation ID:** `company_address_destroy`

**Tags:** `company`

**Description:** Delete an existing Address object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Address object to delete. |

## Responses

| Status Code | Description |
|-------------|-------------|
| 204         | No Content (deletion successful) |
| 404         | Not Found   |

## Notes

- Upon successful deletion, a 204 No Content response is returned with no body.
- Attempting to delete a non-existent address returns a 404 error.
- Ensure appropriate permissions are granted before attempting deletion.
