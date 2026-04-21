---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: DELETE /api/company/{id}/ — Delete Company
fetched: 2026-04-17
---

# `DELETE /api/company/{id}/` — Delete Company

**Operation ID:** `company_destroy`

**Tags:** `company`

**Description:** Delete an existing Company object.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Path Parameters

| Name | Required | Type    | Description |
|------|----------|---------|-------------|
| `id` | Yes      | integer | The unique identifier (primary key) of the Company object to delete. |

## Responses

| Status Code | Description |
|-------------|-------------|
| 204         | No Content (deletion successful) |
| 404         | Not Found   |

## Notes

- Upon successful deletion, a 204 No Content response is returned with no body.
- Attempting to delete a non-existent company returns a 404 error.
- Ensure appropriate permissions are granted before attempting deletion.
- Consider any dependencies or references to this company before deletion (e.g., purchase orders, contacts associated with the company).
