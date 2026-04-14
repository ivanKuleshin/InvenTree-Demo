---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: Part Categories CRUD API Requirements
fetched: 2026-04-14
---

# Part Categories API — CRUD Requirements

> **Source**: https://docs.inventree.org/en/stable/api/schema/part/
> API Version: 479

## Table of Contents

- [Authentication](#authentication)
- [Error Response Shapes](#error-response-shapes)
- [Pagination](#pagination)
- [Create Part Category](#create-part-category)
- [List Part Categories](#list-part-categories)
- [Retrieve Part Category](#retrieve-part-category)
- [Update Part Category (Full)](#update-part-category-full)
- [Partial Update Part Category](#partial-update-part-category)
- [Delete Part Category](#delete-part-category)
- [Category Schema — Writable Fields](#category-schema--writable-fields)
- [Category Schema — Read-Only Fields](#category-schema--read-only-fields)

---

## Authentication

All endpoints require authentication. Supported schemes:

| Scheme       | How to use                                       |
| ------------ | ------------------------------------------------ |
| `tokenAuth`  | Header: `Authorization: Token <token>`           |
| `basicAuth`  | HTTP Basic authentication                        |
| `cookieAuth` | Cookie: `sessionid`                              |
| `oauth2`     | Bearer token with appropriate scope (see per-op) |

**Demo credentials** (https://demo.inventree.org):

| Username  | Password  | Access Level                       |
| --------- | --------- | ---------------------------------- |
| allaccess | nolimits  | Full create/edit/view permissions  |
| reader    | readonly  | View-only access                   |
| engineer  | partsonly | Parts management and stock viewing |
| admin     | inventree | Superuser with admin functions     |

---

## Error Response Shapes

The InvenTree API does not define explicit 4xx/5xx schemas in the OpenAPI spec per endpoint. Standard Django REST Framework error shapes apply:

**400 Bad Request** — Validation failure. Body contains a JSON object mapping field names to arrays of error strings:

```json
{
  "name": ["This field is required."],
  "parent": ["Invalid pk \"999\" - object does not exist."]
}
```

Non-field errors appear under `"non_field_errors"`:

```json
{
  "non_field_errors": ["The fields name, parent must make a unique set."]
}
```

**401 Unauthorized** — Missing or invalid credentials:

```json
{ "detail": "Authentication credentials were not provided." }
```

**403 Forbidden** — Authenticated but insufficient permissions:

```json
{ "detail": "You do not have permission to perform this action." }
```

**404 Not Found** — Resource does not exist:

```json
{ "detail": "Not found." }
```

**405 Method Not Allowed**:

```json
{ "detail": "Method \"DELETE\" not allowed." }
```

**500 Internal Server Error** — Unhandled server exception; body varies.

---

## Pagination

List endpoints use limit/offset pagination.

**Request parameters:**

| Parameter | Type    | Required | Description                                             |
| --------- | ------- | -------- | ------------------------------------------------------- |
| `limit`   | integer | **yes**  | Number of results to return per page                    |
| `offset`  | integer | no       | Initial index from which to return results (default: 0) |

**Response shape** (`PaginatedCategoryList`):

```json
{
  "count": 42,
  "next": "https://demo.inventree.org/api/part/category/?limit=25&offset=25",
  "previous": null,
  "results": [
    /* array of Category objects */
  ]
}
```

| Field      | Type              | Required | Description                         |
| ---------- | ----------------- | -------- | ----------------------------------- |
| `count`    | integer           | yes      | Total number of matching results    |
| `next`     | string (uri)      | no       | URL of next page; null if last page |
| `previous` | string (uri)      | no       | URL of previous page; null if first |
| `results`  | array of Category | yes      | Page of Category objects            |

---

## Create Part Category

**`POST /api/part/category/`**

**Operation ID:** `part_category_create`

**Required OAuth2 scope:** `r:add:part_category` or `r:add:build`

### Request Body

Required. Content-Type: `application/json` (also accepts `application/x-www-form-urlencoded`, `multipart/form-data`).

See [Category Schema — Writable Fields](#category-schema--writable-fields) for the full field list.

**Minimum viable payload** (only truly required fields):

```json
{
  "name": "My Category"
}
```

### Responses

| Status | Description      | Body            |
| ------ | ---------------- | --------------- |
| 201    | Created          | Category object |
| 400    | Validation error | Error object    |
| 401    | Unauthorized     | Error object    |
| 403    | Forbidden        | Error object    |

---

## List Part Categories

**`GET /api/part/category/`**

**Operation ID:** `part_category_list`

**Required OAuth2 scope:** `r:view:part_category` or `r:view:build`

### Query Parameters

| Name           | Type    | Required | Description                                                                                                                                            |
| -------------- | ------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `limit`        | integer | **yes**  | Number of results to return per page                                                                                                                   |
| `offset`       | integer | no       | Initial index from which to return results                                                                                                             |
| `search`       | string  | no       | Full-text search term. Searched fields: `description`, `name`, `pathstring`                                                                            |
| `ordering`     | enum    | no       | Sort field. Values: `name`, `-name`, `pathstring`, `-pathstring`, `level`, `-level`, `tree_id`, `-tree_id`, `lft`, `-lft`, `part_count`, `-part_count` |
| `cascade`      | boolean | no       | Include sub-categories in filtered results                                                                                                             |
| `depth`        | number  | no       | Filter by category depth                                                                                                                               |
| `exclude_tree` | integer | no       | Exclude categories in this tree                                                                                                                        |
| `name`         | string  | no       | Filter by exact name                                                                                                                                   |
| `parent`       | integer | no       | Filter by parent category ID                                                                                                                           |
| `path_detail`  | boolean | no       | Include path detail in response (default: `false`)                                                                                                     |
| `starred`      | boolean | no       | Filter by starred categories                                                                                                                           |
| `structural`   | boolean | no       | Filter by structural flag                                                                                                                              |
| `top_level`    | boolean | no       | Filter by top-level categories (no parent)                                                                                                             |

### Responses

| Status | Description  | Body                  |
| ------ | ------------ | --------------------- |
| 200    | OK           | PaginatedCategoryList |
| 401    | Unauthorized | Error object          |
| 403    | Forbidden    | Error object          |

---

## Retrieve Part Category

**`GET /api/part/category/{id}/`**

**Operation ID:** `part_category_retrieve`

**Required OAuth2 scope:** `r:view:part_category` or `r:view:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Category PK |

### Query Parameters

| Name          | Type    | Required | Description                            |
| ------------- | ------- | -------- | -------------------------------------- |
| `path_detail` | boolean | no       | Include path detail (default: `false`) |

### Responses

| Status | Description  | Body            |
| ------ | ------------ | --------------- |
| 200    | OK           | Category object |
| 401    | Unauthorized | Error object    |
| 403    | Forbidden    | Error object    |
| 404    | Not Found    | Error object    |

---

## Update Part Category (Full)

**`PUT /api/part/category/{id}/`**

**Operation ID:** `part_category_update`

**Required OAuth2 scope:** `r:change:part_category` or `r:change:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Category PK |

### Request Body

Required. Same schema as Create (`Category`). All required fields must be present.

See [Category Schema — Writable Fields](#category-schema--writable-fields).

### Responses

| Status | Description      | Body            |
| ------ | ---------------- | --------------- |
| 200    | OK               | Category object |
| 400    | Validation error | Error object    |
| 401    | Unauthorized     | Error object    |
| 403    | Forbidden        | Error object    |
| 404    | Not Found        | Error object    |

---

## Partial Update Part Category

**`PATCH /api/part/category/{id}/`**

**Operation ID:** `part_category_partial_update`

**Required OAuth2 scope:** `r:change:part_category` or `r:change:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Category PK |

### Request Body

Optional. Uses `PatchedCategory` schema — identical fields to `Category` but all fields are optional. Send only the fields to be changed.

### Responses

| Status | Description      | Body            |
| ------ | ---------------- | --------------- |
| 200    | OK               | Category object |
| 400    | Validation error | Error object    |
| 401    | Unauthorized     | Error object    |
| 403    | Forbidden        | Error object    |
| 404    | Not Found        | Error object    |

---

## Delete Part Category

**`DELETE /api/part/category/{id}/`**

**Operation ID:** `part_category_destroy`

**Required OAuth2 scope:** `r:delete:part_category` or `r:delete:build`

### Path Parameters

| Name | Type    | Required | Description |
| ---- | ------- | -------- | ----------- |
| `id` | integer | **yes**  | Category PK |

### Responses

| Status | Description  | Body         |
| ------ | ------------ | ------------ |
| 204    | No Content   | (empty)      |
| 401    | Unauthorized | Error object |
| 403    | Forbidden    | Error object |
| 404    | Not Found    | Error object |

---

## Category Schema — Writable Fields

Fields that can be sent in POST (create) and PUT/PATCH (update) request bodies.

| Field              | Type    | Required | Nullable | Constraints     | Description                                                                             |
| ------------------ | ------- | -------- | -------- | --------------- | --------------------------------------------------------------------------------------- |
| `name`             | string  | **yes**  | no       | max length: 100 | Category name                                                                           |
| `description`      | string  | no       | no       | max length: 250 | Category description (optional)                                                         |
| `parent`           | integer | no       | yes      |                 | PK of the parent category; null means top-level                                         |
| `default_location` | integer | no       | yes      |                 | PK of the default stock location for parts in this category                             |
| `default_keywords` | string  | no       | yes      | max length: 250 | Default keywords applied to parts in this category                                      |
| `structural`       | boolean | no       | no       |                 | If true, parts may not be directly assigned to this category (only to child categories) |
| `icon`             | string  | no       | yes      | max length: 100 | Icon identifier (optional)                                                              |

---

## Category Schema — Read-Only Fields

These fields are returned in responses but ignored in request bodies.

| Field                     | Type              | Nullable | Description                                                                            |
| ------------------------- | ----------------- | -------- | -------------------------------------------------------------------------------------- |
| `pk`                      | integer           | no       | Primary key                                                                            |
| `level`                   | integer           | no       | Depth level in the category tree (0 = top-level)                                       |
| `pathstring`              | string            | no       | Full path string (e.g. `"Electronics/Passive/Resistors"`)                              |
| `starred`                 | boolean           | no       | True if this category is starred by the current user                                   |
| `part_count`              | integer           | yes      | Number of parts directly assigned to this category                                     |
| `subcategories`           | integer           | yes      | Number of direct child categories                                                      |
| `parent_default_location` | integer           | yes      | Default location inherited from parent category                                        |
| `path`                    | array of TreePath | yes      | Full path as array of `{pk, name}` objects (PATCH response only / `?path_detail=true`) |
