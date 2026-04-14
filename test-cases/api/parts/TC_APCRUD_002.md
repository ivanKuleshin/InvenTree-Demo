# TC-APCRUD-002: GET /api/part/{id}/ retrieves a single part by primary key

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/{id}/`

**Preconditions:**

- Part with `pk=82` ("1551ABK") exists on the demo server
- No authentication required (demo allows public read)

**Steps:**

1. Send `GET /api/part/82/` with no authorization header
2. Verify response status code is `200`
3. Verify response body field `pk` equals `82`
4. Verify response body field `name` equals `"1551ABK"`
5. Verify response body field `active` equals `true`
6. Verify response body field `component` equals `true`
7. Verify response body field `purchaseable` equals `true`
8. Verify response body field `assembly` equals `false`
9. Verify response body field `category` equals `null`
10. Verify response body field `in_stock` is a number greater than or equal to `0`
11. Verify response body does NOT contain field `parameters` (absent without `?parameters=true`)
12. Verify response body does NOT contain field `tags` (absent without `?tags=true`)
13. Verify response body does NOT contain field `category_detail` object (absent without `?category_detail=true`)

**Request:**

- Method: `GET`
- URL: `/api/part/82/`
- Headers: _(none required)_

**Expected Result:** Server returns `200 OK` with the full part object for pk=82. Optional expanded fields (`parameters`, `tags`, `category_detail`) are absent from the response unless their corresponding query parameters are supplied.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "description": "Small plastic enclosure, black",
    "active": true,
    "component": true,
    "purchaseable": true,
    "assembly": false,
    "category": null,
    "in_stock": 765
  }
  ```
- `parameters`, `tags`, `category_detail` absent without query flags
- Matches spec: Yes

**Notes:** Part `pk=82` has no category assigned (`category: null`). This is valid — parts without a category are permitted. Stock counts (`in_stock`, `total_in_stock`) may differ between runs due to concurrent demo activity.
