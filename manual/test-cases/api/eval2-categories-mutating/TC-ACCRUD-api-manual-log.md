# API Manual Probe Log — Part Categories CRUD

**Probe date:** 2026-04-18  
**Base URL:** `https://demo.inventree.org`  
**Auth:** HTTP Basic (admin / inventree)  
**Suite:** TC-ACCRUD-NNN (eval2-categories-mutating)

---

## Step 1 — GET /api/part/category/?limit=5&offset=0

**Request:**
```
GET https://demo.inventree.org/api/part/category/?limit=5&offset=0
Authorization: Basic (admin:inventree)
```

**HTTP Status:** 200 OK

**Response body (full):**
```json
{
  "count": 27,
  "next": "https://demo.inventree.org/api/part/category/?limit=5&offset=5",
  "previous": null,
  "results": [
    {
      "pk": 23,
      "name": "Category 0",
      "description": "Part category, level 1",
      "default_location": null,
      "default_keywords": null,
      "level": 0,
      "parent": null,
      "part_count": 0,
      "subcategories": 5,
      "pathstring": "Category 0",
      "starred": false,
      "structural": false,
      "icon": "",
      "parent_default_location": null
    },
    {
      "pk": 24,
      "name": "Category 1",
      "description": "Part category, level 2",
      "default_location": null,
      "default_keywords": null,
      "level": 1,
      "parent": 23,
      "part_count": 0,
      "subcategories": 4,
      "pathstring": "Category 0/Category 1",
      "starred": false,
      "structural": true,
      "icon": "",
      "parent_default_location": null
    },
    {
      "pk": 25,
      "name": "Category 2",
      "description": "Part category, level 3",
      "default_location": null,
      "default_keywords": null,
      "level": 2,
      "parent": 24,
      "part_count": 0,
      "subcategories": 3,
      "pathstring": "Category 0/Category 1/Category 2",
      "starred": false,
      "structural": false,
      "icon": "",
      "parent_default_location": null
    }
  ]
}
```

**Notes:**
- Response is paginated; `count: 27` categories total in demo.
- `limit` is a required query param per schema, but default pagination worked with explicit `limit=5`.
- `icon` field returns empty string `""` rather than `null` when not set.
- `part_count` and `subcategories` are integers (not null) on existing categories.
- `structural: true` means parts cannot be directly assigned to that category.

---

## Step 2 — GET /api/part/category/23/

**Request:**
```
GET https://demo.inventree.org/api/part/category/23/
Authorization: Basic (admin:inventree)
```

**HTTP Status:** 200 OK

**Response body (full):**
```json
{
  "pk": 23,
  "name": "Category 0",
  "description": "Part category, level 1",
  "default_location": null,
  "default_keywords": null,
  "level": 0,
  "parent": null,
  "part_count": 0,
  "subcategories": 5,
  "pathstring": "Category 0",
  "starred": false,
  "structural": false,
  "icon": "",
  "parent_default_location": null
}
```

**Notes:**
- Returns a single Category object (not paginated).
- All schema fields present.
- `level: 0` confirms top-level category.

---

## Step 3 — POST /api/part/category/ (CREATE)

**Request:**
```
POST https://demo.inventree.org/api/part/category/
Authorization: Basic (admin:inventree)
Content-Type: application/json

{
  "name": "TC-ACCRUD-Eval-Category",
  "description": "Test category for eval CRUD",
  "structural": false
}
```

**HTTP Status:** 201 Created

**Response body (full):**
```json
{
  "pk": 30,
  "name": "TC-ACCRUD-Eval-Category",
  "description": "Test category for eval CRUD",
  "default_location": null,
  "default_keywords": null,
  "level": 0,
  "parent": null,
  "part_count": null,
  "subcategories": null,
  "pathstring": "TC-ACCRUD-Eval-Category",
  "starred": false,
  "structural": false,
  "icon": "",
  "parent_default_location": null,
  "path": [
    {"pk": 30, "name": "TC-ACCRUD-Eval-Category"}
  ]
}
```

**Notes:**
- `pk: 30` assigned by server.
- `part_count` and `subcategories` are `null` on newly created category (vs `0` on retrieved ones — likely computed lazily).
- Response includes an extra `path` array field not in the schema snapshot — actual API adds breadcrumb path data on create/update.
- `level: 0`, `parent: null` — top-level category as expected (no parent supplied).

---

## Step 4 — PATCH /api/part/category/30/ (RENAME)

**Request:**
```
PATCH https://demo.inventree.org/api/part/category/30/
Authorization: Basic (admin:inventree)
Content-Type: application/json

{
  "name": "TC-ACCRUD-Eval-Category-Renamed"
}
```

**HTTP Status:** 200 OK

**Response body (full):**
```json
{
  "pk": 30,
  "name": "TC-ACCRUD-Eval-Category-Renamed",
  "description": "Test category for eval CRUD",
  "default_location": null,
  "default_keywords": null,
  "level": 0,
  "parent": null,
  "part_count": 0,
  "subcategories": 0,
  "pathstring": "TC-ACCRUD-Eval-Category-Renamed",
  "starred": false,
  "structural": false,
  "icon": "",
  "parent_default_location": null,
  "path": [
    {"pk": 30, "name": "TC-ACCRUD-Eval-Category-Renamed"}
  ]
}
```

**Notes:**
- `name` and `pathstring` both updated to `"TC-ACCRUD-Eval-Category-Renamed"`.
- `description` preserved from create step (PATCH is partial update — untouched fields remain).
- `part_count` and `subcategories` now `0` (integer) rather than `null` as on create.
- `path` array also updates to reflect new name.

---

## Step 5 — DELETE /api/part/category/30/

**Request:**
```
DELETE https://demo.inventree.org/api/part/category/30/
Authorization: Basic (admin:inventree)
```

**HTTP Status:** 204 No Content

**Response body:** (empty)

**Notes:**
- 204 with empty body matches schema spec.
- Category pk=30 no longer exists after this call.

---

✓ TC-ACCRUD-api-test-suite.md — 5 TCs written — observed 2026-04-18 — source: demo.inventree.org
