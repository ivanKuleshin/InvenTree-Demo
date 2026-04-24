---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: PaginatedCompanyList
fetched: 2026-04-17
---

# PaginatedCompanyList

Paginated response wrapper for Company list queries.

Used when returning paginated results from the Company list endpoint.

**Type:** object

## Properties

| Field    | Type                          | Flags               | Description                                                  |
|----------|-------------------------------|---------------------|--------------------------------------------------------------|
| `count`  | integer                       | required, read-only | Total number of Company objects matching the query.          |
| `next`   | string (uri)                  | nullable            | URL to fetch the next page of results (null if on last page) |
| `previous` | string (uri)                | nullable            | URL to fetch the previous page of results (null if on first page) |
| `results` | array of [Company](company.md) | required            | Array of Company objects for this page.                      |

## Example Response

```json
{
  "count": 42,
  "next": "https://demo.inventree.org/api/company/?limit=10&offset=10",
  "previous": null,
  "results": [
    {
      "id": 1,
      "name": "ACME Corp",
      "description": "Leading supplier of widgets",
      "website": "https://acmecorp.example.com",
      "address": "123 Main St",
      "phone": "+1-555-0100",
      "email": "contact@acmecorp.example.com",
      "contact": "John Smith",
      "is_supplier": true,
      "is_manufacturer": false,
      "is_customer": false,
      "active": true,
      "created": "2024-01-15T10:30:00Z",
      "updated": "2024-01-15T10:30:00Z"
    }
  ]
}
```

## Notes

- Pagination is controlled by the `limit` and `offset` query parameters in the original request.
- The `next` and `previous` fields contain complete URLs ready for fetching the next/previous page.
- The `results` array contains zero or more Company objects depending on pagination parameters.
