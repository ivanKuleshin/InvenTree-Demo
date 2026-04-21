---
source: https://docs.inventree.org/en/stable/api/schema/company/
component: api
topic: Company Management API
fetched: 2026-04-17
---

# Company Management API

The Company Management API provides endpoints for managing company information, including details about suppliers, manufacturers, customers, and their associated addresses.

## Overview

InvenTree uses the Company Management API to:
- Maintain records of organizations (suppliers, manufacturers, customers)
- Manage company contact information and addresses
- Associate companies with purchase orders, sales orders, and other business relationships

## Authentication

All Company API endpoints require authentication. Supported authentication methods:

- **Token Authentication**: `Authorization: Token <token>` header
- **Basic Authentication**: HTTP Basic auth (username:password)
- **Cookie Authentication**: `sessionid` cookie (from web login)
- **OAuth2**: With appropriate scopes (e.g., `g:read`)

## Endpoints

| Method | Path | Operation ID | Summary |
|--------|------|--------------|---------|
| GET | `/api/company/` | `company_list` | [List Companies](../endpoints/company-list-GET.md) |
| POST | `/api/company/` | `company_create` | [Create Company](../endpoints/company-create-POST.md) |
| GET | `/api/company/{id}/` | `company_retrieve` | [Get Company Detail](../endpoints/company-detail-GET.md) |
| PUT | `/api/company/{id}/` | `company_update` | [Update Company (Full)](../endpoints/company-detail-PUT.md) |
| PATCH | `/api/company/{id}/` | `company_partial_update` | [Update Company (Partial)](../endpoints/company-detail-PATCH.md) |
| DELETE | `/api/company/{id}/` | `company_destroy` | [Delete Company](../endpoints/company-detail-DELETE.md) |
| GET | `/api/company/address/` | `company_address_list` | [List Company Addresses](../endpoints/company-address-list-GET.md) |
| POST | `/api/company/address/` | `company_address_create` | [Create Company Address](../endpoints/company-address-create-POST.md) |
| GET | `/api/company/address/{id}/` | `company_address_retrieve` | [Get Address Detail](../endpoints/company-address-detail-GET.md) |
| PUT | `/api/company/address/{id}/` | `company_address_update` | [Update Address (Full)](../endpoints/company-address-detail-PUT.md) |
| PATCH | `/api/company/address/{id}/` | `company_address_partial_update` | [Update Address (Partial)](../endpoints/company-address-detail-PATCH.md) |
| DELETE | `/api/company/address/{id}/` | `company_address_destroy` | [Delete Address](../endpoints/company-address-detail-DELETE.md) |
| DELETE | `/api/company/address/` | `company_address_bulk_destroy` | [Bulk Delete Addresses](../endpoints/company-address-bulk-destroy-DELETE.md) |

## Component Schemas

The Company API uses the following schema components:

| Schema | Description |
|--------|-------------|
| [Company](../schemas/company.md) | Complete company object with all fields and metadata |
| [PatchedCompany](../schemas/patched-company.md) | Partial company object for PATCH requests (all fields optional) |
| [Address](../schemas/address.md) | Complete address object for a company |
| [PatchedAddress](../schemas/patched-address.md) | Partial address object for PATCH requests (all fields optional) |
| [PaginatedCompanyList](../schemas/paginated-company-list.md) | Paginated response wrapper for company list queries |
| [PaginatedAddressList](../schemas/paginated-address-list.md) | Paginated response wrapper for address list queries |

## Common Query Parameters

### List Endpoints

All list endpoints support the following query parameters:

- **`limit`** (required, integer): Number of results to return per page
- **`offset`** (optional, integer): Zero-indexed starting position for pagination (default: 0)
- **`ordering`** (optional, string): Field to order results by, prefix with `-` for descending (e.g., `-name`, `created`)
- **`search`** (optional, string): Free-text search across relevant fields

### Example List Request

```bash
GET https://demo.inventree.org/api/company/?limit=10&offset=0&ordering=-created&search=supplier
Authorization: Token <your-token>
```

## Company Types

A single company record can have multiple roles:

- **`is_supplier`**: Company can supply parts or materials
- **`is_manufacturer`**: Company manufactures products
- **`is_customer`**: Company can purchase products from you

## Example Workflows

### Create a New Supplier

```bash
POST /api/company/
Content-Type: application/json
Authorization: Token <token>

{
  "name": "ABC Electronics",
  "description": "Electronics supplier",
  "email": "contact@abcelec.com",
  "phone": "+1-555-0200",
  "website": "https://www.abcelec.com",
  "is_supplier": true,
  "active": true
}
```

### Add a Billing Address to a Company

```bash
POST /api/company/address/
Content-Type: application/json
Authorization: Token <token>

{
  "company": 1,
  "type": "billing",
  "title": "Main Office",
  "line1": "789 Oak Street",
  "city": "Springfield",
  "state": "IL",
  "postal_code": "62701",
  "country": "United States",
  "phone": "+1-555-0200",
  "is_default": true
}
```

### Update a Company (Partial)

```bash
PATCH /api/company/5/
Content-Type: application/json
Authorization: Token <token>

{
  "email": "newemail@example.com",
  "phone": "+1-555-9999"
}
```

## Error Responses

Standard HTTP error codes are used:

| Status Code | Meaning |
|-------------|---------|
| 200 | OK — Request succeeded |
| 201 | Created — Resource successfully created |
| 204 | No Content — Successful deletion |
| 400 | Bad Request — Invalid request parameters or data |
| 401 | Unauthorized — Missing or invalid authentication |
| 403 | Forbidden — Insufficient permissions |
| 404 | Not Found — Resource does not exist |
| 409 | Conflict — Resource conflict (e.g., duplicate entry) |
| 422 | Unprocessable Entity — Validation error on request body |
| 500 | Internal Server Error — Server-side error |

## Pagination

List endpoints return paginated results. The response includes:

- **`count`**: Total number of matching items
- **`next`**: URL for next page (or null if last page)
- **`previous`**: URL for previous page (or null if first page)
- **`results`**: Array of items for this page

Use `limit` and `offset` parameters to control pagination:

```
GET /api/company/?limit=20&offset=40
```

This retrieves 20 items starting at position 40 (third page with page size 20).

## Notes

- Company records should be deactivated (set `active=false`) rather than deleted when they should no longer be used, to maintain referential integrity with orders and historical records.
- Multiple addresses can be associated with a single company for different purposes (billing, shipping, etc.).
- The company relationship is hierarchical: addresses belong to companies, and companies are referenced by orders and other business objects.
