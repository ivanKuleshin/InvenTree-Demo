---
source: https://docs.inventree.org/en/stable/part/
component: parts
topic: Part Categories — Hierarchy, Filtering, and Parametric Tables
fetched: 2026-04-14
---

> **Source**: [https://docs.inventree.org/en/stable/part/](https://docs.inventree.org/en/stable/part/)

# Part Categories

## Table of Contents

- [Overview](#overview)
- [Category Hierarchy](#category-hierarchy)
  - [Parent and Child Categories](#parent-and-child-categories)
  - [Structural Categories](#structural-categories)
  - [Category Fields](#category-fields)
  - [Category Tree](#category-tree)
  - [Pathstring](#pathstring)
- [Category Display and Navigation](#category-display-and-navigation)
  - [Category Breadcrumb](#category-breadcrumb)
  - [Sub-category List](#sub-category-list)
  - [Parts List Within a Category](#parts-list-within-a-category)
- [Filtering Parts by Category](#filtering-parts-by-category)
  - [Cascade / Subcategory Inclusion](#cascade--subcategory-inclusion)
  - [User-Configurable Filters](#user-configurable-filters)
- [Category Parameter Templates](#category-parameter-templates)
- [Parametric Tables](#parametric-tables)
  - [Parametric View](#parametric-view)
  - [Sorting by Parameter Value](#sorting-by-parameter-value)
  - [Filtering by Parameter Value](#filtering-by-parameter-value)
  - [Multiple Parameter Filters](#multiple-parameter-filters)
  - [Multiple Filters on the Same Parameter](#multiple-filters-on-the-same-parameter)
  - [Unit-Aware Filtering](#unit-aware-filtering)
  - [Removing Filters](#removing-filters)
  - [Available Filter Operators](#available-filter-operators)
- [API Reference — Category Endpoints](#api-reference--category-endpoints)
  - [Category List — GET /api/part/category/](#category-list--get-apipartcategory)
  - [Category Create — POST /api/part/category/](#category-create--post-apipartcategory)
  - [Category Detail — GET /api/part/category/{id}/](#category-detail--get-apipartcategoryid)
  - [Category Update — PUT /api/part/category/{id}/](#category-update--put-apipartcategoryid)
  - [Category Partial Update — PATCH /api/part/category/{id}/](#category-partial-update--patch-apipartcategoryid)
  - [Category Delete — DELETE /api/part/category/{id}/](#category-delete--delete-apipartcategoryid)
  - [Category Tree — GET /api/part/category/tree/](#category-tree--get-apipartcategorytree)
  - [Category Parameter Templates — GET /api/part/category/parameters/](#category-parameter-templates--get-apipartcategoryparameters)
  - [Category Parameter Template Detail](#category-parameter-template-detail)
- [API Schemas](#api-schemas)
  - [Category Schema](#category-schema)
  - [CategoryTree Schema](#categorytree-schema)
  - [CategoryParameterTemplate Schema](#categoryparametertemplate-schema)
  - [PaginatedCategoryList Schema](#paginatedcategorylist-schema)

---

## Overview

Part categories are flexible hierarchical containers used to organize parts by function or type. Each part in InvenTree belongs to a category (or to no category, appearing under the "top level"). Categories can be nested arbitrarily deep, forming a tree structure.

> **[IMAGE DESCRIPTION]**: Screenshot showing a Part Category view in InvenTree (image: part/part_category.png, alt: "Part category"). The view displays a list of parts that belong to a specific category, with columns for part name, description, image thumbnail, category, and stock level. A sub-category list is visible above the parts list, showing the hierarchical nesting of categories. Navigation breadcrumbs are displayed at the top showing the full path from the root to the current category.

The category part list provides an overview of each part within that category and all sub-categories:

- Part name and description
- Part image thumbnail
- Part category
- Part stock level

Clicking on the part name links to the Part Detail view.

---

## Category Hierarchy

### Parent and Child Categories

Categories are arranged in a tree (hierarchical) structure:

- A category may have a **parent** category (making it a child/sub-category).
- A category may have zero or more **child categories** (sub-categories).
- Top-level categories have no parent (parent is null).
- There is no fixed limit to the depth of nesting.

Each part category displays:
1. A list of all **sub-categories** which exist immediately underneath it.
2. A list of all **parts** belonging to that category or any of its descendant categories.

### Structural Categories

A **structural** category is one to which parts cannot be directly assigned. Parts may only be assigned to child (non-structural) categories within a structural category. This allows creating organizational "folder" nodes in the tree without directly holding parts.

- `structural: true` — parts cannot be directly assigned to this category; they must be assigned to a child category.
- `structural: false` (default) — parts can be assigned directly to this category.

### Category Fields

| Field | Type | Required | Read-Only | Description |
|---|---|---|---|---|
| `pk` | integer | yes (auto) | yes | Unique numeric ID |
| `name` | string (max 100) | yes | no | Category name |
| `description` | string (max 250) | no | no | Optional description |
| `parent` | integer (nullable) | no | no | ID of the parent category; null for top-level |
| `level` | integer | yes (auto) | yes | Depth level in the tree (0 = top-level) |
| `pathstring` | string | yes (auto) | yes | Full path string (e.g., `Electronics/Passive/Resistors`) |
| `structural` | boolean | no | no | If true, parts cannot be assigned directly to this category |
| `default_location` | integer (nullable) | no | no | Default stock location for parts in this category |
| `default_keywords` | string (max 250, nullable) | no | no | Default keywords applied to parts in this category |
| `icon` | string (max 100, nullable) | no | no | Optional icon identifier |
| `part_count` | integer (nullable) | no (auto) | yes | Total number of parts under this category (including sub-categories) |
| `subcategories` | integer (nullable) | no (auto) | yes | Number of immediate sub-categories |
| `starred` | boolean | no (auto) | yes | Whether the current user has starred this category |
| `parent_default_location` | integer (nullable) | no (auto) | yes | Inherited default location from parent category |

### Category Tree

The tree endpoint (`/api/part/category/tree/`) returns a lightweight representation of the full category hierarchy suitable for rendering a navigation tree. Each node in the tree includes:

- `pk` — unique ID
- `name` — category name
- `parent` — parent category ID (nullable)
- `icon` — optional icon
- `structural` — whether this is a structural (container-only) category
- `subcategories` — count of immediate child categories

The tree endpoint supports ordering by `level`, `name`, or `subcategories`.

### Pathstring

The `pathstring` field is a read-only computed field that represents the full path from the root to the current category. Path segments are separated by `/`. For example, a category named "Resistors" under "Passive" under "Electronics" would have a pathstring of `Electronics/Passive/Resistors`.

The parts list API (`/api/part/`) supports searching and ordering by `pathstring`.

---

## Category Display and Navigation

### Category Breadcrumb

When viewing a part's detail page, the full category path is displayed as a breadcrumb navigation at the top of the page. This shows each ancestor category as a clickable link, allowing users to navigate up the hierarchy.

### Sub-category List

When viewing a category, the UI displays a list of direct sub-categories under that category. Each entry in the sub-category list is a link to navigate into that child category.

### Parts List Within a Category

The parts list shown in a category view includes **all parts belonging to that category and all of its descendant categories** (i.e., the cascade behavior is on by default in the UI). The list provides:

- Part name (link to part detail)
- Description
- Image thumbnail
- Category (may differ from the current one if the part belongs to a sub-category)
- Stock level

The list can be filtered using the user-configurable filter controls.

---

## Filtering Parts by Category

### Cascade / Subcategory Inclusion

The `cascade` parameter controls whether parts in child/descendant categories are included when filtering by category:

- `cascade=true` — include parts in the specified category **and all its sub-categories** (recursive).
- `cascade=false` — include parts in the specified category **only** (no sub-categories).

This parameter is available on the `/api/part/` list endpoint:

| Parameter | Type | Description |
|---|---|---|
| `category` | integer | Filter parts by numeric category ID, or the literal `'null'` to filter for uncategorized parts |
| `cascade` | boolean | If `true`, include parts in all child categories of the specified category |

### User-Configurable Filters

The category part list in the UI provides multiple user-configurable filters. These allow narrowing the displayed parts when a large number exist under a category. The specific available filters match the standard part list filters (name, IPN, description, active status, etc.).

---

## Category Parameter Templates

A **Category Parameter Template** links a parameter template to a category. This establishes that all parts belonging to the category (and optionally sub-categories) are expected to have a value for that parameter template.

Category parameter templates allow a category to define a "schema" of expected parameters for its parts, which is the foundation of parametric filtering.

### CategoryParameterTemplate Fields

| Field | Type | Required | Read-Only | Description |
|---|---|---|---|---|
| `pk` | integer | yes (auto) | yes | Unique ID |
| `category` | integer | yes | no | ID of the part category |
| `category_detail` | Category object (nullable) | no (auto) | yes | Full nested category object |
| `template` | integer | yes | no | ID of the parameter template |
| `template_detail` | ParameterTemplate object | no (auto) | yes | Full nested parameter template object |
| `default_value` | string (max 500) | no | no | Default value for this parameter in this category |

### ParameterTemplate Fields (nested reference)

| Field | Type | Description |
|---|---|---|
| `pk` | integer | Unique ID |
| `name` | string (max 100) | Parameter name (must be unique) |
| `units` | string (max 25) | Physical units for this parameter |
| `description` | string (max 250) | Parameter description |
| `model_type` | string (nullable) | The InvenTree model type this applies to |
| `checkbox` | boolean | If true, parameter only accepts true/false values |
| `choices` | string (max 5000) | Comma-separated list of valid choices |
| `selectionlist` | integer (nullable) | ID of the selection list for this parameter |
| `enabled` | boolean | Whether this parameter template is enabled |

---

## Parametric Tables

Parametric tables gather all parameters from all parts within a category to allow sorting and filtering by parameter values. This is a powerful feature for finding parts by their electrical, physical, or other characteristics.

> **[IMAGE DESCRIPTION]**: Screenshot showing the Parametric Parts Table in InvenTree (image: concepts/parametric-parts.png, alt: "Parametric Parts Table"). The table displays parts as rows, with columns for standard part fields (name, description, stock) plus additional columns for each defined parameter (e.g., Resistance, Capacitance, Voltage). The table has a "Parametric View" toggle button above it. Each parameter column shows the parameter value for each part where it is defined.

### Parametric View

Table views that support parametric filtering and sorting have a **"Parametric View"** button above the table. Clicking this button switches the table to parametric mode, showing parameter values as additional columns alongside the standard part fields.

### Sorting by Parameter Value

The parametric parts table allows sorting by particular parameter values. Clicking on the column header of a parameter column sorts the results by that parameter value.

> **[IMAGE DESCRIPTION]**: Screenshot showing sorting by a parameter column in the parametric parts table (image: part/part_sort_by_param.png, alt: "Sort by Parameter"). The column header is highlighted or has a sort indicator arrow showing the current sort direction. Parts are re-ordered based on the numeric or string value of the selected parameter.

Sorting is unit-aware: values provided in different but compatible units (e.g., `10k` and `10000` ohms) are sorted correctly after unit conversion.

### Filtering by Parameter Value

The parametric parts table allows filtering by particular parameter values. Clicking the filter icon associated with a parameter column opens a filter input:

> **[IMAGE DESCRIPTION]**: Screenshot showing the filter dialog for a parameter column (image: part/filter_by_param.png, alt: "Filter by Parameter"). A filter input appears with an operator dropdown (=, >, <, etc.) and a value field. For parameters with predefined choices, a dropdown list appears instead of a free-text field.

The available filter options depend on the parameter type:
- Parameters with a limited set of choices show a dropdown of those choices.
- Numeric parameters allow filtering against a value with an operator (e.g., greater than, less than).
- Text parameters support the `~` (contains) operator.

### Multiple Parameter Filters

Multiple parameters can be used simultaneously to filter the parametric table. Adding a new filter for each parameter narrows the results to include only parts matching **all** specified filters (AND logic).

Each parameter column indicates visually whether a filter is currently applied.

> **[IMAGE DESCRIPTION]**: Screenshot showing multiple parameter filters active at once (image: part/multiple_param_filters.png, alt: "Multiple Parameter Filters"). Multiple parameter columns have filter indicators showing that more than one filter is active. The parts list is narrowed to show only parts matching all active filters simultaneously.

### Multiple Filters on the Same Parameter

It is possible to apply multiple filters against the same parameter. For example, filtering for parts with a *Resistance* parameter greater than 10kΩ **and** less than 100kΩ requires adding two filters for the *Resistance* parameter.

> **[IMAGE DESCRIPTION]**: Screenshot showing two filters applied to the same Resistance parameter (image: part/multiple_filters_same_param.png, alt: "Multiple Filters on Same Parameter"). Both filters are shown in the filter list for the Resistance column, one with operator ">" and value "10k", and another with operator "<" and value "100k". The table shows only parts with resistance values in that range.

### Unit-Aware Filtering

When filtering against a parameter that has a physical unit defined, the filter value can be specified in any compatible unit. The system automatically converts to the base unit defined for that parameter template.

For example, to show all parts with a *Resistance* parameter greater than 10kΩ, entering `10k` or `10000` in the filter field will both correctly be interpreted as 10,000 ohms.

> **[IMAGE DESCRIPTION]**: Screenshot showing unit-aware filter input (image: part/filter_with_unit.png, alt: "Unit Aware Filters"). A filter dialog for the Resistance parameter shows the user has typed "10k" as the filter value. The system interprets this as 10,000 ohms and displays results accordingly.

### Removing Filters

To remove a filter against a given parameter, click the remove (X) button associated with that filter in the filter list.

> **[IMAGE DESCRIPTION]**: Screenshot showing the remove filter button (image: part/remove_param_filter.png, alt: "Remove Parameter Filter"). Each active filter in the filter list has a red circle-X icon next to it. Clicking the icon removes that specific filter and refreshes the table results.

### Available Filter Operators

The following filter operators are available for parameter filtering:

| Operator | Meaning |
|---|---|
| `=` | Equal to |
| `>` | Greater than |
| `>=` | Greater than or equal to |
| `<` | Less than |
| `<=` | Less than or equal to |
| `!=` | Not equal to |
| `~` | Contains (text parameters only) |

---

## API Reference — Category Endpoints

All category endpoints are under `/api/part/category/`. Authentication is required via token, basic auth, cookie, or OAuth2.

### Category List — GET /api/part/category/

**Operation ID**: `part_category_list`

Returns a paginated list of PartCategory objects.

**Authentication scopes (OAuth2)**: `r:view:part_category`, `r:view:build`

**Query Parameters**:

| Parameter | Type | Required | Description |
|---|---|---|---|
| `cascade` | boolean | no | Include sub-categories in filtered results |
| `depth` | number | no | Filter by category depth |
| `exclude_tree` | integer | no | Exclude a specific category tree |
| `limit` | integer | yes | Number of results to return per page |
| `name` | string | no | Filter by category name |
| `offset` | integer | no | The initial index from which to return results |
| `ordering` | string (enum) | no | Field to use for ordering results |
| `parent` | integer | no | Filter by parent category ID |
| `path_detail` | boolean | no (default: false) | Include full path detail in results |
| `search` | string | no | Search term — searched fields: description, name, pathstring |
| `starred` | boolean | no | Filter by starred categories |
| `structural` | boolean | no | Filter by structural flag |
| `top_level` | boolean | no | Filter to return only top-level categories |

**Ordering options** (`ordering` parameter values):
- `name`, `-name`
- `pathstring`, `-pathstring`
- `level`, `-level`
- `tree_id`, `-tree_id`
- `lft`, `-lft`
- `part_count`, `-part_count`

**Response** `200 OK`:
```json
{
  "count": 123,
  "next": "http://api.example.org/accounts/?offset=400&limit=100",
  "previous": "http://api.example.org/accounts/?offset=200&limit=100",
  "results": [
    {
      "pk": 1,
      "name": "Electronics",
      "description": "Electronic components",
      "default_location": null,
      "default_keywords": null,
      "level": 0,
      "parent": null,
      "part_count": 42,
      "subcategories": 3,
      "pathstring": "Electronics",
      "starred": false,
      "structural": false,
      "icon": null,
      "parent_default_location": null
    }
  ]
}
```

---

### Category Create — POST /api/part/category/

**Operation ID**: `part_category_create`

Creates a new PartCategory object.

**Authentication scopes (OAuth2)**: `r:add:part_category`, `r:add:build`

**Request Body** (required): `Category` schema (see [Category Schema](#category-schema))

**Content types accepted**: `application/json`, `application/x-www-form-urlencoded`, `multipart/form-data`

**Response** `201 Created`: Returns the created `Category` object.

---

### Category Detail — GET /api/part/category/{id}/

**Operation ID**: `part_category_retrieve`

Returns detail for a single PartCategory.

**Authentication scopes (OAuth2)**: `r:view:part_category`, `r:view:build`

**Path Parameters**:

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | integer | yes | Category primary key |

**Query Parameters**:

| Parameter | Type | Default | Description |
|---|---|---|---|
| `path_detail` | boolean | false | Include full path detail |

**Response** `200 OK`: Returns a `Category` object.

---

### Category Update — PUT /api/part/category/{id}/

**Operation ID**: `part_category_update`

Full replacement update of a PartCategory.

**Authentication scopes (OAuth2)**: `r:change:part_category`, `r:change:build`

**Path Parameters**: `id` (integer, required)

**Request Body** (required): `Category` schema

**Response** `200 OK`: Returns the updated `Category` object.

---

### Category Partial Update — PATCH /api/part/category/{id}/

**Operation ID**: `part_category_partial_update`

Partial update of a PartCategory (only specified fields are updated).

**Authentication scopes (OAuth2)**: `r:change:part_category`, `r:change:build`

**Path Parameters**: `id` (integer, required)

**Request Body**: `PatchedCategory` schema (all fields optional)

**Response** `200 OK`: Returns the updated `Category` object.

---

### Category Delete — DELETE /api/part/category/{id}/

**Operation ID**: `part_category_destroy`

Deletes a PartCategory.

**Authentication scopes (OAuth2)**: `r:delete:part_category`, `r:delete:build`

**Path Parameters**: `id` (integer, required)

**Response** `204 No Content`

---

### Category Tree — GET /api/part/category/tree/

**Operation ID**: `part_category_tree_list`

Returns a paginated list of PartCategory objects formatted for rendering a navigation tree. Returns lightweight `CategoryTree` objects (not full `Category` objects).

**Authentication scopes (OAuth2)**: `r:view:part_category`, `r:view:build`

**Query Parameters**:

| Parameter | Type | Required | Description |
|---|---|---|---|
| `limit` | integer | yes | Number of results per page |
| `offset` | integer | no | Initial index |
| `ordering` | string (enum) | no | Field to use for ordering |

**Ordering options**: `level`, `-level`, `name`, `-name`, `subcategories`, `-subcategories`

**Response** `200 OK`: Returns a `PaginatedCategoryTreeList` object.

---

### Category Parameter Templates — GET /api/part/category/parameters/

**Operation ID**: `part_category_parameters_list`

Returns a paginated list of PartCategoryParameterTemplate objects, which define the parameter templates associated with categories.

**Authentication scopes (OAuth2)**: `r:view:part_category`

**Query Parameters**:

| Parameter | Type | Required | Description |
|---|---|---|---|
| `limit` | integer | yes | Number of results per page |
| `offset` | integer | no | Initial index |

**Response** `200 OK`: Returns a `PaginatedCategoryParameterTemplateList` object.

**POST** — Create a new CategoryParameterTemplate:
- **Authentication scopes (OAuth2)**: `r:add:part_category`
- **Request Body** (required): `CategoryParameterTemplate` schema
- **Response** `201 Created`: Returns the created `CategoryParameterTemplate` object.

---

### Category Parameter Template Detail

**Path**: `/api/part/category/parameters/{id}/`

Supports: GET, PUT, PATCH, DELETE on individual `CategoryParameterTemplate` objects.

| Method | Operation ID | OAuth2 Scope | Description |
|---|---|---|---|
| GET | `part_category_parameters_retrieve` | `r:view:part_category` | Retrieve a single template link |
| PUT | `part_category_parameters_update` | `r:change:part_category` | Full update |
| PATCH | `part_category_parameters_partial_update` | `r:change:part_category` | Partial update |
| DELETE | `part_category_parameters_destroy` | `r:delete:part_category` | Delete |

**Path Parameters**: `id` (integer, required)

---

## API Schemas

### Category Schema

Used for full category CRUD operations. The `Category` and `PatchedCategory` schemas differ only in that all fields in `PatchedCategory` are optional (for PATCH).

| Field | Type | Nullable | Read-Only | Required | Constraints | Description |
|---|---|---|---|---|---|---|
| `pk` | integer | no | yes | yes | auto | Unique ID |
| `name` | string | no | no | yes | maxLength: 100 | Category name |
| `description` | string | no | no | no | maxLength: 250 | Optional description |
| `default_location` | integer | yes | no | no | | Default stock location for parts in this category |
| `default_keywords` | string | yes | no | no | maxLength: 250 | Default keywords for parts in this category |
| `level` | integer | no | yes | yes | auto | Depth level in hierarchy |
| `parent` | integer | yes | no | no | | Parent category ID; null for top-level |
| `part_count` | integer | yes | yes | no | auto | Total parts count (including sub-categories) |
| `subcategories` | integer | yes | yes | no | auto | Number of immediate sub-categories |
| `pathstring` | string | no | yes | yes | auto | Full path string (e.g., `Electronics/Passive`) |
| `starred` | boolean | no | yes | yes | auto | Whether the current user has starred this category |
| `structural` | boolean | no | no | no | | If true, parts cannot be directly assigned here |
| `icon` | string | yes | no | no | maxLength: 100 | Optional icon identifier |
| `parent_default_location` | integer | yes | yes | no | auto | Inherited default location from parent |
| `path` | array of TreePath | yes | yes | no | auto | Full path array (PatchedCategory only) |

**Required fields for create (POST)**: `name`

### CategoryTree Schema

Lightweight schema for tree navigation rendering.

| Field | Type | Nullable | Read-Only | Required | Description |
|---|---|---|---|---|---|
| `pk` | integer | no | yes | yes | Unique ID |
| `name` | string | no | no | yes | Category name (maxLength: 100) |
| `parent` | integer | yes | no | no | Parent category ID |
| `icon` | string | no | no | no | Optional icon (maxLength: 100) |
| `structural` | boolean | no | no | no | Whether this is a structural category |
| `subcategories` | integer | no | yes | yes | Count of immediate child categories |

### CategoryParameterTemplate Schema

Represents the association between a category and a parameter template.

| Field | Type | Nullable | Read-Only | Required | Description |
|---|---|---|---|---|---|
| `pk` | integer | no | yes | yes | Unique ID |
| `category` | integer | no | no | yes | Part category ID |
| `category_detail` | Category object | yes | yes | no | Full nested category object |
| `template` | integer | no | no | yes | Parameter template ID |
| `template_detail` | ParameterTemplate object | no | yes | no | Full nested parameter template object |
| `default_value` | string | no | no | no | Default value for this parameter in this category (maxLength: 500) |

### PaginatedCategoryList Schema

Standard paginated wrapper for Category list responses.

| Field | Type | Required | Description |
|---|---|---|---|
| `count` | integer | yes | Total number of results |
| `next` | string (uri, nullable) | no | URL to the next page |
| `previous` | string (uri, nullable) | no | URL to the previous page |
| `results` | array of Category | yes | Array of Category objects for the current page |
