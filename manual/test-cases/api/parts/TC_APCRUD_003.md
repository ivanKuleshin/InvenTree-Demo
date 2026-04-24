# TC-APCRUD-003: GET /api/part/{id}/ with query flags returns expanded fields

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/{id}/`

**Preconditions:**

- Part with `pk=82` exists and has at least one parameter assigned
- No authentication required (demo allows public read)

**Steps:**

1. Send `GET /api/part/82/?parameters=true&tags=true&category_detail=true` with no authorization header
2. Verify response status code is `200`
3. Verify response body field `pk` equals `82`
4. Verify response body contains field `parameters` as an array
5. Verify each element in `parameters` contains fields: `pk`, `template`, `data`, `template_detail`
6. Verify each `template_detail` object contains fields: `pk`, `name`, `units`, `description`
7. Verify response body contains field `tags` as an array (may be empty)
8. Verify response body contains field `category_detail` — it is `null` because part `pk=82` has no category assigned
9. Send `GET /api/part/82/?path_detail=true` and verify response body contains field `category_path` (may be empty array for uncategorized part)

**Request:**

- Method: `GET`
- URL: `/api/part/82/?parameters=true&tags=true&category_detail=true`
- Headers: _(none required)_

**Expected Result:** Each query flag independently adds its expanded field to the response. `parameters` contains inline `template_detail` objects. `category_detail` is `null` when the part has no category. `tags` is present as an array (empty if none assigned).

**Observed** (probed 2026-04-14):

- Status: `200`
- `parameters` array: 15 elements; each has `pk`, `template`, `data`, `data_numeric`, `template_detail`
- `template_detail` example: `{"pk": 235, "name": "Length", "units": "mm", "description": "Part length", "checkbox": false}`
- `tags`: `[]` (no tags assigned to this part)
- `category_detail`: `null` (part has no category)
- Matches spec: Yes

**Notes:** Multiple query flags can be combined in a single request. `parameters=true` significantly increases response payload size for parts with many parameters — automation should avoid requesting this flag on list endpoints unless needed. `category_path` (from `path_detail=true`) returns an empty array `[]` for uncategorized parts rather than `null`.
