# TC-ACCRUD-001: GET /api/part/category/ returns paginated category list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/`

**Preconditions:**

- At least one part category exists on the server
- Valid HTTP Basic credentials are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/part/category/?limit=10` with the authorization header
3. Verify response status code is `200`
4. Verify response body contains field `count` with an integer value greater than `0`
5. Verify response body contains field `results` as a non-empty array
6. Verify response body contains field `next` (string URI or null)
7. Verify response body contains field `previous` (null on first page)
8. Verify each object in `results` contains fields: `pk`, `name`, `description`, `default_location`, `default_keywords`, `level`, `parent`, `part_count`, `subcategories`, `pathstring`, `starred`, `structural`, `icon`, `parent_default_location`
9. Verify that a top-level category (e.g., `pk=1` "Electronics") has `parent: null` and `level: 0`
10. Verify that a child category has `parent` equal to its parent's `pk` and `level` greater than `0`

**Request:**

- Method: `GET`
- URL: `/api/part/category/?limit=10`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with a `PaginatedCategoryList` object. The `results` array contains category objects with correct hierarchical metadata (`level`, `parent`, `pathstring`).

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `31`
- Response snippet:
  ```json
  {
    "count": 31,
    "next": "https://demo.inventree.org/api/part/category/?format=json&limit=10&offset=10",
    "previous": null,
    "results": [
      {
        "pk": 1,
        "name": "Electronics",
        "description": "Electronic components and systems",
        "default_location": null,
        "default_keywords": null,
        "level": 0,
        "parent": null,
        "part_count": 144,
        "subcategories": 12,
        "pathstring": "Electronics",
        "starred": false,
        "structural": false,
        "icon": "",
        "parent_default_location": null
      },
      {
        "pk": 25,
        "name": "Category 2",
        "level": 1,
        "parent": 24,
        "pathstring": "Category 1/Category 2"
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The demo allows unauthenticated GET access — the list returns `200` even without credentials. Auth is still required for write operations.
