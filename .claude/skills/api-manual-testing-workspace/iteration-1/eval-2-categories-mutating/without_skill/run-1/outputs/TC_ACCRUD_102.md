# TC-ACCRUD-102: GET /api/part/category/{pk}/ returns a single category by PK

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/{pk}/`

**Preconditions:**

- A part category with `pk=1` ("Electronics") exists on the server
- Valid HTTP Basic credentials are available

**Steps:**

1. Send `GET /api/part/category/1/` with `Authorization: Basic <base64(admin:inventree)>`
2. Verify response status code is `200`
3. Verify response body `pk` equals `1`
4. Verify response body `name` equals `"Electronics"`
5. Verify response body `description` equals `"Electronic components and systems"`
6. Verify response body `parent` equals `null`
7. Verify response body `level` equals `0`
8. Verify response body `pathstring` equals `"Electronics"`
9. Verify response body `part_count` is an integer greater than `0`
10. Verify response body `subcategories` is an integer greater than `0`
11. Verify response body `starred` is a boolean
12. Verify response body `structural` is a boolean
13. Verify response body contains fields `default_location`, `default_keywords`, `icon`, `parent_default_location`

**Request:**

- Method: `GET`
- URL: `/api/part/category/1/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with a single category object matching `pk=1` with all expected fields populated.

**Observed** (probed 2026-04-18):

- Status: `200`
- Response:
  ```json
  {
    "pk": 1,
    "name": "Electronics",
    "description": "Electronic components and systems",
    "default_location": null,
    "default_keywords": null,
    "level": 0,
    "parent": null,
    "part_count": 138,
    "subcategories": 12,
    "pathstring": "Electronics",
    "starred": false,
    "structural": false,
    "icon": "",
    "parent_default_location": null
  }
  ```
- Matches spec: Yes

**Notes:** The single-item GET response does NOT include a `path` array (unlike POST/PATCH responses). `part_count` and `subcategories` reflect current live data and will vary over time.
