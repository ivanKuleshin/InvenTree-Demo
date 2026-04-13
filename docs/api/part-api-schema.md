---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: Parts and Part Categories API Schema — Index
fetched: 2026-04-13
---

# Parts and Part Categories — API Schema Index

> **Source**: https://docs.inventree.org/en/stable/api/schema/part/

## Table of Contents

- [Overview](#overview)
- [Authentication](#authentication)
- [OAuth2 Scopes](#oauth2-scopes)
- [Endpoints Summary](#endpoints-summary)
    - [Parts (Core)](#parts-core)
    - [Part Categories](#part-categories)
    - [Part Internal Pricing](#part-internal-pricing)
    - [Part Sale Pricing](#part-sale-pricing)
    - [Part Related Parts](#part-related-parts)
    - [Part Stocktake](#part-stocktake)
    - [Part Test Templates](#part-test-templates)
    - [Part Thumbnails](#part-thumbnails)
- [Component Schemas](#component-schemas)
    - [BulkRequest](schemas/bulk-request.md)
    - [Category](schemas/category.md)
    - [CategoryParameterTemplate](schemas/category-parameter-template.md)
    - [PaginatedCategoryList](schemas/paginated-category-list.md)
    - [PaginatedCategoryParameterTemplateList](schemas/paginated-category-parameter-template-list.md)
    - [PaginatedCategoryTreeList](schemas/paginated-category-tree-list.md)
    - [PaginatedPartInternalPriceList](schemas/paginated-part-internal-price-list.md)
    - [PaginatedPartList](schemas/paginated-part-list.md)
    - [PaginatedPartRelationList](schemas/paginated-part-relation-list.md)
    - [PaginatedPartSalePriceList](schemas/paginated-part-sale-price-list.md)
    - [PaginatedPartStocktakeList](schemas/paginated-part-stocktake-list.md)
    - [PaginatedPartTestTemplateList](schemas/paginated-part-test-template-list.md)
    - [PaginatedPartThumbList](schemas/paginated-part-thumb-list.md)
    - [Part](schemas/part.md)
    - [PartBomValidate](schemas/part-bom-validate.md)
    - [PartCopyBOM](schemas/part-copy-bom.md)
    - [PartInternalPrice](schemas/part-internal-price.md)
    - [PartPricing](schemas/part-pricing.md)
    - [PartRelation](schemas/part-relation.md)
    - [PartRequirements](schemas/part-requirements.md)
    - [PartSalePrice](schemas/part-sale-price.md)
    - [PartSerialNumber](schemas/part-serial-number.md)
    - [PartStocktake](schemas/part-stocktake.md)
    - [PartStocktakeGenerate](schemas/part-stocktake-generate.md)
    - [PartTestTemplate](schemas/part-test-template.md)
    - [PartThumbSerializerUpdate](schemas/part-thumb-serializer-update.md)
    - [PatchedCategory](schemas/patched-category.md)
    - [PatchedCategoryParameterTemplate](schemas/patched-category-parameter-template.md)
    - [PatchedPart](schemas/patched-part.md)
    - [PatchedPartBomValidate](schemas/patched-part-bom-validate.md)
    - [PatchedPartInternalPrice](schemas/patched-part-internal-price.md)
    - [PatchedPartPricing](schemas/patched-part-pricing.md)
    - [PatchedPartRelation](schemas/patched-part-relation.md)
    - [PatchedPartSalePrice](schemas/patched-part-sale-price.md)
    - [PatchedPartStocktake](schemas/patched-part-stocktake.md)
    - [PatchedPartTestTemplate](schemas/patched-part-test-template.md)
    - [PatchedPartThumbSerializerUpdate](schemas/patched-part-thumb-serializer-update.md)

## Overview

**API Version:** 479

**Description:** API for InvenTree - the intuitive open source inventory management system

**License:** MIT — https://github.com/inventree/InvenTree/blob/master/LICENSE

The Parts and Part Categories section of the InvenTree API provides endpoints for managing parts (components,
assemblies, and other items tracked in the inventory system) and the hierarchical category system used to organise them.
This includes creating, retrieving, updating, and deleting parts and categories; managing part pricing (internal and
sale price breaks); part relationships; stocktake records; test templates; and part thumbnails.

## Authentication

All endpoints require authentication unless otherwise noted. The following authentication schemes are supported:

### tokenAuth

- **Type:** API key
- **In:** Header (`Authorization`)
- **Format:** Token-based authentication with required prefix `"Token"`
- **Example:** `Authorization: Token abc123def456`

### basicAuth

- **Type:** HTTP Basic authentication
- **Scheme:** `basic`

### cookieAuth

- **Type:** API key
- **In:** Cookie (`sessionid`)

### oauth2

- **Type:** OAuth2
- **Authorization URL:** `/o/authorize/`
- **Token URL:** `/o/token/`
- **Refresh/Revoke URL:** `/o/revoke_token/`
- **Flows:** Authorization Code, Client Credentials

## OAuth2 Scopes

| Scope                     | Description                          |
|---------------------------|--------------------------------------|
| `a:staff`                 | User Role Staff                      |
| `a:superuser`             | User Role Superuser                  |
| `g:read`                  | General Read scope                   |
| `openid`                  | OpenID Connect scope                 |
| `r:add:admin`             | POST for Role Admin                  |
| `r:add:build`             | POST for Role Build Orders           |
| `r:add:part`              | POST for Role Parts                  |
| `r:add:part_category`     | POST for Role Part Categories        |
| `r:add:purchase_order`    | POST for Role Purchase Orders        |
| `r:add:return_order`      | POST for Role Return Orders          |
| `r:add:sales_order`       | POST for Role Sales Orders           |
| `r:add:stock`             | POST for Role Stock Items            |
| `r:add:stock_location`    | POST for Role Stock Locations        |
| `r:change:admin`          | PUT / PATCH for Role Admin           |
| `r:change:build`          | PUT / PATCH for Role Build Orders    |
| `r:change:part`           | PUT / PATCH for Role Parts           |
| `r:change:part_category`  | PUT / PATCH for Role Part Categories |
| `r:change:purchase_order` | PUT / PATCH for Role Purchase Orders |
| `r:change:return_order`   | PUT / PATCH for Role Return Orders   |
| `r:change:sales_order`    | PUT / PATCH for Role Sales Orders    |
| `r:change:stock`          | PUT / PATCH for Role Stock Items     |
| `r:change:stock_location` | PUT / PATCH for Role Stock Locations |
| `r:delete:admin`          | DELETE for Role Admin                |
| `r:delete:build`          | DELETE for Role Build Orders         |
| `r:delete:part`           | DELETE for Role Parts                |
| `r:delete:part_category`  | DELETE for Role Part Categories      |
| `r:delete:purchase_order` | DELETE for Role Purchase Orders      |
| `r:delete:return_order`   | DELETE for Role Return Orders        |
| `r:delete:sales_order`    | DELETE for Role Sales Orders         |
| `r:delete:stock`          | DELETE for Role Stock Items          |
| `r:delete:stock_location` | DELETE for Role Stock Locations      |
| `r:view:admin`            | GET for Role Admin                   |
| `r:view:build`            | GET for Role Build Orders            |
| `r:view:part`             | GET for Role Parts                   |
| `r:view:part_category`    | GET for Role Part Categories         |
| `r:view:purchase_order`   | GET for Role Purchase Orders         |
| `r:view:return_order`     | GET for Role Return Orders           |
| `r:view:sales_order`      | GET for Role Sales Orders            |
| `r:view:stock`            | GET for Role Stock Items             |
| `r:view:stock_location`   | GET for Role Stock Locations         |

## Endpoints Summary

Each endpoint has its own detail file under `docs/api/endpoints/`. Click the Operation ID link to view full parameter
and response details.

### Parts (Core)

| Method | Path                             | Operation ID                       | Detail File                                                                      |
|--------|----------------------------------|------------------------------------|----------------------------------------------------------------------------------|
| GET    | `/api/part/`                     | `part_list`                        | [part-list-GET.md](endpoints/part-list-GET.md)                                   |
| POST   | `/api/part/`                     | `part_create`                      | [part-create-POST.md](endpoints/part-create-POST.md)                             |
| PUT    | `/api/part/`                     | `part_bulk_update`                 | [part-bulk-update-PUT.md](endpoints/part-bulk-update-PUT.md)                     |
| PATCH  | `/api/part/`                     | `part_bulk_partial_update`         | [part-bulk-partial-update-PATCH.md](endpoints/part-bulk-partial-update-PATCH.md) |
| GET    | `/api/part/{id}/`                | `part_retrieve`                    | [part-detail-GET.md](endpoints/part-detail-GET.md)                               |
| PUT    | `/api/part/{id}/`                | `part_update`                      | [part-detail-PUT.md](endpoints/part-detail-PUT.md)                               |
| PATCH  | `/api/part/{id}/`                | `part_partial_update`              | [part-detail-PATCH.md](endpoints/part-detail-PATCH.md)                           |
| DELETE | `/api/part/{id}/`                | `part_destroy`                     | [part-detail-DELETE.md](endpoints/part-detail-DELETE.md)                         |
| POST   | `/api/part/{id}/bom-copy/`       | `part_bom_copy_create`             | [part-bom-copy-POST.md](endpoints/part-bom-copy-POST.md)                         |
| GET    | `/api/part/{id}/bom-validate/`   | `part_bom_validate_retrieve`       | [part-bom-validate-GET.md](endpoints/part-bom-validate-GET.md)                   |
| PUT    | `/api/part/{id}/bom-validate/`   | `part_bom_validate_update`         | [part-bom-validate-PUT.md](endpoints/part-bom-validate-PUT.md)                   |
| PATCH  | `/api/part/{id}/bom-validate/`   | `part_bom_validate_partial_update` | [part-bom-validate-PATCH.md](endpoints/part-bom-validate-PATCH.md)               |
| GET    | `/api/part/{id}/pricing/`        | `part_pricing_retrieve`            | [part-pricing-GET.md](endpoints/part-pricing-GET.md)                             |
| PUT    | `/api/part/{id}/pricing/`        | `part_pricing_update`              | [part-pricing-PUT.md](endpoints/part-pricing-PUT.md)                             |
| PATCH  | `/api/part/{id}/pricing/`        | `part_pricing_partial_update`      | [part-pricing-PATCH.md](endpoints/part-pricing-PATCH.md)                         |
| GET    | `/api/part/{id}/requirements/`   | `part_requirements_retrieve`       | [part-requirements-GET.md](endpoints/part-requirements-GET.md)                   |
| GET    | `/api/part/{id}/serial-numbers/` | `part_serial_numbers_retrieve`     | [part-serial-numbers-GET.md](endpoints/part-serial-numbers-GET.md)               |

### Part Categories

| Method | Path                                  | Operation ID                              | Detail File                                                                                        |
|--------|---------------------------------------|-------------------------------------------|----------------------------------------------------------------------------------------------------|
| GET    | `/api/part/category/`                 | `part_category_list`                      | [part-category-list-GET.md](endpoints/part-category-list-GET.md)                                   |
| POST   | `/api/part/category/`                 | `part_category_create`                    | [part-category-create-POST.md](endpoints/part-category-create-POST.md)                             |
| PUT    | `/api/part/category/`                 | `part_category_bulk_update`               | [part-category-bulk-update-PUT.md](endpoints/part-category-bulk-update-PUT.md)                     |
| PATCH  | `/api/part/category/`                 | `part_category_bulk_partial_update`       | [part-category-bulk-partial-update-PATCH.md](endpoints/part-category-bulk-partial-update-PATCH.md) |
| GET    | `/api/part/category/{id}/`            | `part_category_retrieve`                  | [part-category-detail-GET.md](endpoints/part-category-detail-GET.md)                               |
| PUT    | `/api/part/category/{id}/`            | `part_category_update`                    | [part-category-detail-PUT.md](endpoints/part-category-detail-PUT.md)                               |
| PATCH  | `/api/part/category/{id}/`            | `part_category_partial_update`            | [part-category-detail-PATCH.md](endpoints/part-category-detail-PATCH.md)                           |
| DELETE | `/api/part/category/{id}/`            | `part_category_destroy`                   | [part-category-detail-DELETE.md](endpoints/part-category-detail-DELETE.md)                         |
| GET    | `/api/part/category/tree/`            | `part_category_tree_list`                 | [part-category-tree-GET.md](endpoints/part-category-tree-GET.md)                                   |
| GET    | `/api/part/category/parameters/`      | `part_category_parameters_list`           | [part-category-parameters-list-GET.md](endpoints/part-category-parameters-list-GET.md)             |
| POST   | `/api/part/category/parameters/`      | `part_category_parameters_create`         | [part-category-parameters-create-POST.md](endpoints/part-category-parameters-create-POST.md)       |
| GET    | `/api/part/category/parameters/{id}/` | `part_category_parameters_retrieve`       | [part-category-parameters-detail-GET.md](endpoints/part-category-parameters-detail-GET.md)         |
| PUT    | `/api/part/category/parameters/{id}/` | `part_category_parameters_update`         | [part-category-parameters-detail-PUT.md](endpoints/part-category-parameters-detail-PUT.md)         |
| PATCH  | `/api/part/category/parameters/{id}/` | `part_category_parameters_partial_update` | [part-category-parameters-detail-PATCH.md](endpoints/part-category-parameters-detail-PATCH.md)     |
| DELETE | `/api/part/category/parameters/{id}/` | `part_category_parameters_destroy`        | [part-category-parameters-detail-DELETE.md](endpoints/part-category-parameters-detail-DELETE.md)   |

### Part Internal Pricing

| Method | Path                             | Operation ID                         | Detail File                                                                            |
|--------|----------------------------------|--------------------------------------|----------------------------------------------------------------------------------------|
| GET    | `/api/part/internal-price/`      | `part_internal_price_list`           | [part-internal-price-list-GET.md](endpoints/part-internal-price-list-GET.md)           |
| POST   | `/api/part/internal-price/`      | `part_internal_price_create`         | [part-internal-price-create-POST.md](endpoints/part-internal-price-create-POST.md)     |
| GET    | `/api/part/internal-price/{id}/` | `part_internal_price_retrieve`       | [part-internal-price-detail-GET.md](endpoints/part-internal-price-detail-GET.md)       |
| PUT    | `/api/part/internal-price/{id}/` | `part_internal_price_update`         | [part-internal-price-detail-PUT.md](endpoints/part-internal-price-detail-PUT.md)       |
| PATCH  | `/api/part/internal-price/{id}/` | `part_internal_price_partial_update` | [part-internal-price-detail-PATCH.md](endpoints/part-internal-price-detail-PATCH.md)   |
| DELETE | `/api/part/internal-price/{id}/` | `part_internal_price_destroy`        | [part-internal-price-detail-DELETE.md](endpoints/part-internal-price-detail-DELETE.md) |

### Part Sale Pricing

| Method | Path                         | Operation ID                     | Detail File                                                                    |
|--------|------------------------------|----------------------------------|--------------------------------------------------------------------------------|
| GET    | `/api/part/sale-price/`      | `part_sale_price_list`           | [part-sale-price-list-GET.md](endpoints/part-sale-price-list-GET.md)           |
| POST   | `/api/part/sale-price/`      | `part_sale_price_create`         | [part-sale-price-create-POST.md](endpoints/part-sale-price-create-POST.md)     |
| GET    | `/api/part/sale-price/{id}/` | `part_sale_price_retrieve`       | [part-sale-price-detail-GET.md](endpoints/part-sale-price-detail-GET.md)       |
| PUT    | `/api/part/sale-price/{id}/` | `part_sale_price_update`         | [part-sale-price-detail-PUT.md](endpoints/part-sale-price-detail-PUT.md)       |
| PATCH  | `/api/part/sale-price/{id}/` | `part_sale_price_partial_update` | [part-sale-price-detail-PATCH.md](endpoints/part-sale-price-detail-PATCH.md)   |
| DELETE | `/api/part/sale-price/{id}/` | `part_sale_price_destroy`        | [part-sale-price-detail-DELETE.md](endpoints/part-sale-price-detail-DELETE.md) |

### Part Related Parts

| Method | Path                      | Operation ID                  | Detail File                                                              |
|--------|---------------------------|-------------------------------|--------------------------------------------------------------------------|
| GET    | `/api/part/related/`      | `part_related_list`           | [part-related-list-GET.md](endpoints/part-related-list-GET.md)           |
| POST   | `/api/part/related/`      | `part_related_create`         | [part-related-create-POST.md](endpoints/part-related-create-POST.md)     |
| GET    | `/api/part/related/{id}/` | `part_related_retrieve`       | [part-related-detail-GET.md](endpoints/part-related-detail-GET.md)       |
| PUT    | `/api/part/related/{id}/` | `part_related_update`         | [part-related-detail-PUT.md](endpoints/part-related-detail-PUT.md)       |
| PATCH  | `/api/part/related/{id}/` | `part_related_partial_update` | [part-related-detail-PATCH.md](endpoints/part-related-detail-PATCH.md)   |
| DELETE | `/api/part/related/{id}/` | `part_related_destroy`        | [part-related-detail-DELETE.md](endpoints/part-related-detail-DELETE.md) |

### Part Stocktake

| Method | Path                            | Operation ID                     | Detail File                                                                              |
|--------|---------------------------------|----------------------------------|------------------------------------------------------------------------------------------|
| GET    | `/api/part/stocktake/`          | `part_stocktake_list`            | [part-stocktake-list-GET.md](endpoints/part-stocktake-list-GET.md)                       |
| POST   | `/api/part/stocktake/`          | `part_stocktake_create`          | [part-stocktake-create-POST.md](endpoints/part-stocktake-create-POST.md)                 |
| DELETE | `/api/part/stocktake/`          | `part_stocktake_bulk_destroy`    | [part-stocktake-bulk-destroy-DELETE.md](endpoints/part-stocktake-bulk-destroy-DELETE.md) |
| GET    | `/api/part/stocktake/{id}/`     | `part_stocktake_retrieve`        | [part-stocktake-detail-GET.md](endpoints/part-stocktake-detail-GET.md)                   |
| PUT    | `/api/part/stocktake/{id}/`     | `part_stocktake_update`          | [part-stocktake-detail-PUT.md](endpoints/part-stocktake-detail-PUT.md)                   |
| PATCH  | `/api/part/stocktake/{id}/`     | `part_stocktake_partial_update`  | [part-stocktake-detail-PATCH.md](endpoints/part-stocktake-detail-PATCH.md)               |
| DELETE | `/api/part/stocktake/{id}/`     | `part_stocktake_destroy`         | [part-stocktake-detail-DELETE.md](endpoints/part-stocktake-detail-DELETE.md)             |
| POST   | `/api/part/stocktake/generate/` | `part_stocktake_generate_create` | [part-stocktake-generate-POST.md](endpoints/part-stocktake-generate-POST.md)             |

### Part Test Templates

| Method | Path                            | Operation ID                        | Detail File                                                                          |
|--------|---------------------------------|-------------------------------------|--------------------------------------------------------------------------------------|
| GET    | `/api/part/test-template/`      | `part_test_template_list`           | [part-test-template-list-GET.md](endpoints/part-test-template-list-GET.md)           |
| POST   | `/api/part/test-template/`      | `part_test_template_create`         | [part-test-template-create-POST.md](endpoints/part-test-template-create-POST.md)     |
| GET    | `/api/part/test-template/{id}/` | `part_test_template_retrieve`       | [part-test-template-detail-GET.md](endpoints/part-test-template-detail-GET.md)       |
| PUT    | `/api/part/test-template/{id}/` | `part_test_template_update`         | [part-test-template-detail-PUT.md](endpoints/part-test-template-detail-PUT.md)       |
| PATCH  | `/api/part/test-template/{id}/` | `part_test_template_partial_update` | [part-test-template-detail-PATCH.md](endpoints/part-test-template-detail-PATCH.md)   |
| DELETE | `/api/part/test-template/{id}/` | `part_test_template_destroy`        | [part-test-template-detail-DELETE.md](endpoints/part-test-template-detail-DELETE.md) |

### Part Thumbnails

| Method | Path                     | Operation ID                 | Detail File                                                          |
|--------|--------------------------|------------------------------|----------------------------------------------------------------------|
| GET    | `/api/part/thumbs/`      | `part_thumbs_list`           | [part-thumbs-list-GET.md](endpoints/part-thumbs-list-GET.md)         |
| GET    | `/api/part/thumbs/{id}/` | `part_thumbs_retrieve`       | [part-thumbs-detail-GET.md](endpoints/part-thumbs-detail-GET.md)     |
| PUT    | `/api/part/thumbs/{id}/` | `part_thumbs_update`         | [part-thumbs-detail-PUT.md](endpoints/part-thumbs-detail-PUT.md)     |
| PATCH  | `/api/part/thumbs/{id}/` | `part_thumbs_partial_update` | [part-thumbs-detail-PATCH.md](endpoints/part-thumbs-detail-PATCH.md) |

## Component Schemas

The following schemas are referenced by the Parts API endpoints. Each schema has its own detail file under
`docs/api/schemas/`.

| Schema                                                                                          | Detail File                                             | Description                                                                            |
|-------------------------------------------------------------------------------------------------|---------------------------------------------------------|----------------------------------------------------------------------------------------|
| [BulkRequest](schemas/bulk-request.md)                                                          | `schemas/bulk-request.md`                               | Parameters for selecting items for bulk operations                                     |
| [Category](schemas/category.md)                                                                 | `schemas/category.md`                                   | Serializer for PartCategory                                                            |
| [CategoryParameterTemplate](schemas/category-parameter-template.md)                             | `schemas/category-parameter-template.md`                | Serializer for the PartCategoryParameterTemplate model                                 |
| [PaginatedCategoryList](schemas/paginated-category-list.md)                                     | `schemas/paginated-category-list.md`                    | Paginated list response wrapping an array of Category objects                          |
| [PaginatedCategoryParameterTemplateList](schemas/paginated-category-parameter-template-list.md) | `schemas/paginated-category-parameter-template-list.md` | Paginated list response wrapping an array of CategoryParameterTemplate objects         |
| [PaginatedCategoryTreeList](schemas/paginated-category-tree-list.md)                            | `schemas/paginated-category-tree-list.md`               | Paginated list response wrapping an array of CategoryTree objects                      |
| [PaginatedPartInternalPriceList](schemas/paginated-part-internal-price-list.md)                 | `schemas/paginated-part-internal-price-list.md`         | Paginated list response wrapping an array of PartInternalPrice objects                 |
| [PaginatedPartList](schemas/paginated-part-list.md)                                             | `schemas/paginated-part-list.md`                        | Paginated list response wrapping an array of Part objects                              |
| [PaginatedPartRelationList](schemas/paginated-part-relation-list.md)                            | `schemas/paginated-part-relation-list.md`               | Paginated list response wrapping an array of PartRelation objects                      |
| [PaginatedPartSalePriceList](schemas/paginated-part-sale-price-list.md)                         | `schemas/paginated-part-sale-price-list.md`             | Paginated list response wrapping an array of PartSalePrice objects                     |
| [PaginatedPartStocktakeList](schemas/paginated-part-stocktake-list.md)                          | `schemas/paginated-part-stocktake-list.md`              | Paginated list response wrapping an array of PartStocktake objects                     |
| [PaginatedPartTestTemplateList](schemas/paginated-part-test-template-list.md)                   | `schemas/paginated-part-test-template-list.md`          | Paginated list response wrapping an array of PartTestTemplate objects                  |
| [PaginatedPartThumbList](schemas/paginated-part-thumb-list.md)                                  | `schemas/paginated-part-thumb-list.md`                  | Paginated list response wrapping an array of PartThumb objects                         |
| [Part](schemas/part.md)                                                                         | `schemas/part.md`                                       | Serializer for complete detail information of a part                                   |
| [PartBomValidate](schemas/part-bom-validate.md)                                                 | `schemas/part-bom-validate.md`                          | Serializer for Part BOM information                                                    |
| [PartCopyBOM](schemas/part-copy-bom.md)                                                         | `schemas/part-copy-bom.md`                              | Serializer for copying a BOM from another part                                         |
| [PartInternalPrice](schemas/part-internal-price.md)                                             | `schemas/part-internal-price.md`                        | Serializer for internal prices for Part model                                          |
| [PartPricing](schemas/part-pricing.md)                                                          | `schemas/part-pricing.md`                               | Serializer for Part pricing information                                                |
| [PartRelation](schemas/part-relation.md)                                                        | `schemas/part-relation.md`                              | Serializer for a PartRelated model                                                     |
| [PartRequirements](schemas/part-requirements.md)                                                | `schemas/part-requirements.md`                          | Serializer for Part requirements                                                       |
| [PartSalePrice](schemas/part-sale-price.md)                                                     | `schemas/part-sale-price.md`                            | Serializer for sale prices for Part model                                              |
| [PartSerialNumber](schemas/part-serial-number.md)                                               | `schemas/part-serial-number.md`                         | Serializer for Part serial number information                                          |
| [PartStocktake](schemas/part-stocktake.md)                                                      | `schemas/part-stocktake.md`                             | Serializer for the PartStocktake model                                                 |
| [PartStocktakeGenerate](schemas/part-stocktake-generate.md)                                     | `schemas/part-stocktake-generate.md`                    | Serializer for generating PartStocktake entries                                        |
| [PartTestTemplate](schemas/part-test-template.md)                                               | `schemas/part-test-template.md`                         | Serializer for the PartTestTemplate class                                              |
| [PartThumbSerializerUpdate](schemas/part-thumb-serializer-update.md)                            | `schemas/part-thumb-serializer-update.md`               | Serializer for updating Part thumbnail                                                 |
| [PatchedCategory](schemas/patched-category.md)                                                  | `schemas/patched-category.md`                           | PATCH variant of Category serializer (all fields optional)                             |
| [PatchedCategoryParameterTemplate](schemas/patched-category-parameter-template.md)              | `schemas/patched-category-parameter-template.md`        | PATCH variant of CategoryParameterTemplate serializer (all fields optional)            |
| [PatchedPart](schemas/patched-part.md)                                                          | `schemas/patched-part.md`                               | PATCH variant of Part serializer with additional detail fields (all fields optional)   |
| [PatchedPartBomValidate](schemas/patched-part-bom-validate.md)                                  | `schemas/patched-part-bom-validate.md`                  | PATCH variant of PartBomValidate serializer (all fields optional)                      |
| [PatchedPartInternalPrice](schemas/patched-part-internal-price.md)                              | `schemas/patched-part-internal-price.md`                | PATCH variant of PartInternalPrice serializer (all fields optional)                    |
| [PatchedPartPricing](schemas/patched-part-pricing.md)                                           | `schemas/patched-part-pricing.md`                       | PATCH variant of PartPricing serializer (all fields optional)                          |
| [PatchedPartRelation](schemas/patched-part-relation.md)                                         | `schemas/patched-part-relation.md`                      | PATCH variant of PartRelation serializer (all fields optional)                         |
| [PatchedPartSalePrice](schemas/patched-part-sale-price.md)                                      | `schemas/patched-part-sale-price.md`                    | PATCH variant of PartSalePrice serializer (all fields optional)                        |
| [PatchedPartStocktake](schemas/patched-part-stocktake.md)                                       | `schemas/patched-part-stocktake.md`                     | PATCH variant of PartStocktake serializer with part_detail field (all fields optional) |
| [PatchedPartTestTemplate](schemas/patched-part-test-template.md)                                | `schemas/patched-part-test-template.md`                 | PATCH variant of PartTestTemplate serializer (all fields optional)                     |
| [PatchedPartThumbSerializerUpdate](schemas/patched-part-thumb-serializer-update.md)             | `schemas/patched-part-thumb-serializer-update.md`       | PATCH variant of PartThumbSerializerUpdate serializer (all fields optional)            |
