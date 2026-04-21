# TC-ACCRUD-101: GET /api/part/category/ returns flat category list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/`

**Preconditions:**

- At least one part category exists on the server
- Valid HTTP Basic credentials are available

**Steps:**

1. Send `GET /api/part/category/` with `Authorization: Basic <base64(admin:inventree)>`
2. Verify response status code is `200`
3. Verify response body is a JSON array (not paginated — flat list mode)
4. Verify each element contains fields: `pk`, `name`, `description`, `default_location`, `default_keywords`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `parent_default_location`
5. Verify at least one item has `parent: null` and `level: 0` (top-level category)
6. Verify at least one item has `parent` set to a valid `pk` and `level` greater than `0` (child category)

**Request:**

- Method: `GET`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with a JSON array of all part category objects. Each object contains hierarchical metadata (`level`, `parent`, `pathstring`).

**Observed** (probed 2026-04-18):

- Status: `200`
- Response is a flat JSON array (no `count`/`results` envelope)
- Array contains 27 categories
- Response snippet:
  ```json
  [
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
    }
  ]
  ```
- Matches spec: Yes

**Notes:** Unlike the paginated list endpoint behavior described in some docs, `GET /api/part/category/` returns a flat array (not an envelope with `count`/`results`) by default. A child category (e.g., `pk=24`) has `parent: 23` and `level: 1`.
