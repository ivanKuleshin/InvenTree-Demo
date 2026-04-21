---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PaginatedAddressList
fetched: 2026-04-17
---

# PaginatedAddressList

Paginated response wrapper for Address list queries.

Used when returning paginated results from the Address list endpoint.

**Type:** object

## Properties

| Field    | Type                          | Flags               | Description                                                  |
|----------|-------------------------------|---------------------|--------------------------------------------------------------|
| `count`  | integer                       | required, read-only | Total number of Address objects matching the query.          |
| `next`   | string (uri)                  | nullable            | URL to fetch the next page of results (null if on last page) |
| `previous` | string (uri)                | nullable            | URL to fetch the previous page of results (null if on first page) |
| `results` | array of [Address](address.md) | required            | Array of Address objects for this page.                      |

## Example Response

```json
{
  "count": 12,
  "next": null,
  "previous": null,
  "results": [
    {
      "id": 1,
      "company": 1,
      "type": "billing",
      "title": "Main Office",
      "line1": "123 Main Street",
      "line2": "Suite 100",
      "city": "Springfield",
      "state": "IL",
      "postal_code": "62701",
      "country": "United States",
      "phone": "+1-555-0100",
      "email": "billing@acmecorp.example.com",
      "notes": "Primary billing address",
      "is_default": true,
      "created": "2024-01-15T10:30:00Z",
      "updated": "2024-01-15T10:30:00Z"
    },
    {
      "id": 2,
      "company": 1,
      "type": "shipping",
      "title": "Warehouse",
      "line1": "456 Industrial Blvd",
      "line2": null,
      "city": "Springfield",
      "state": "IL",
      "postal_code": "62702",
      "country": "United States",
      "phone": "+1-555-0101",
      "email": "warehouse@acmecorp.example.com",
      "notes": "Shipping and receiving",
      "is_default": false,
      "created": "2024-01-20T14:00:00Z",
      "updated": "2024-01-20T14:00:00Z"
    }
  ]
}
```

## Notes

- Pagination is controlled by the `limit` and `offset` query parameters in the original request.
- The `next` and `previous` fields contain complete URLs ready for fetching the next/previous page.
- The `results` array contains zero or more Address objects depending on pagination parameters.
