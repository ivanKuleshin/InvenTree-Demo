---
name: Company Module Documentation Created
description: Complete API documentation for InvenTree Companies module with endpoints and schemas; created 2026-04-17
type: project
---

# Company Module API Documentation

Complete documentation created for the InvenTree Companies module.

## Created Files

### API Index/Overview
- `docs/api/company-api-schema.md` — Master index with endpoint table, authentication, and common workflows

### Company Endpoints (6 files)
- `docs/api/endpoints/company-list-GET.md` — List all companies with pagination/search
- `docs/api/endpoints/company-create-POST.md` — Create new company
- `docs/api/endpoints/company-detail-GET.md` — Retrieve single company by ID
- `docs/api/endpoints/company-detail-PUT.md` — Full update (all fields required)
- `docs/api/endpoints/company-detail-PATCH.md` — Partial update (only supplied fields)
- `docs/api/endpoints/company-detail-DELETE.md` — Delete company

### Company Address Endpoints (7 files)
- `docs/api/endpoints/company-address-list-GET.md` — List addresses with pagination/search
- `docs/api/endpoints/company-address-create-POST.md` — Create new address
- `docs/api/endpoints/company-address-detail-GET.md` — Retrieve single address by ID
- `docs/api/endpoints/company-address-detail-PUT.md` — Full update
- `docs/api/endpoints/company-address-detail-PATCH.md` — Partial update
- `docs/api/endpoints/company-address-detail-DELETE.md` — Delete address
- `docs/api/endpoints/company-address-bulk-destroy-DELETE.md` — Bulk delete multiple addresses

### Schema Documentation (6 files)
- `docs/api/schemas/company.md` — Company object schema (all fields with constraints)
- `docs/api/schemas/patched-company.md` — Company object for PATCH requests
- `docs/api/schemas/address.md` — Address object schema
- `docs/api/schemas/patched-address.md` — Address object for PATCH requests
- `docs/api/schemas/paginated-company-list.md` — Paginated company response with example
- `docs/api/schemas/paginated-address-list.md` — Paginated address response with example

## Total: 20 files created

## Key Features Documented

- **Company Fields**: id, name, description, website, address, phone, email, contact, currency, active, is_supplier, is_manufacturer, is_customer, notes, timestamps
- **Address Fields**: id, company (FK), type, title, line1-2, city, state, postal_code, country, phone, email, notes, is_default, timestamps
- **Authentication**: Token, Basic, Cookie, OAuth2 with `g:read` scope
- **Pagination**: limit/offset pattern with next/previous URL pointers
- **Search**: Full-text search across name/description fields
- **Ordering**: Configurable sorting by multiple fields

## Endpoint Operations Summary

| Operation | Count |
|-----------|-------|
| Company CRUD | 6 |
| Address CRUD | 7 |
| Schema definitions | 6 |
| **Total** | **13 endpoints + 6 schemas** |

## Notes

- All endpoints follow standard REST conventions with proper HTTP status codes
- Authentication required on all endpoints
- Schemas include field descriptions, types, constraints, and nullability info
- Paginated responses use standard envelope with count/next/previous/results
- Bulk operations included for mass address deletion
- Company records should be soft-deleted (set active=false) rather than hard-deleted
