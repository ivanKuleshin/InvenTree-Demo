# TC-APFLT-006: GET /api/part/category/ supports search and parent filter

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/category/`

**Preconditions:**

- Category "Electronics" (pk=1) exists with at least one child subcategory
- No authentication required

**Steps:**

1. Send `GET /api/part/category/?search=Electronics&limit=10`
2. Verify response status code is `200`
3. Verify response body field `count` is greater than `0`
4. Verify the result with `name="Electronics"` is present in `results`
5. Verify additional results whose `pathstring` contains `"Electronics"` are also present (search matches pathstring)
6. Send `GET /api/part/category/?parent=1&limit=10`
7. Verify response status code is `200`
8. Verify response body field `count` is greater than `0`
9. Verify every result in `results` has `parent` equal to `1`
10. Verify no result has `parent` equal to `null` (top-level categories excluded)
11. Send `GET /api/part/category/?top_level=true&limit=10`
12. Verify response status code is `200`
13. Verify every result in `results` has `parent` equal to `null`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/category/?search=Electronics&limit=10`
- Headers: _(none required)_

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/category/?parent=1&limit=10`
- Headers: _(none required)_

**Expected Result:** `search` matches against category `name`, `description`, and `pathstring`. `parent=<pk>` returns only direct children of that category. `top_level=true` returns only root categories (no parent).

**Observed** (probed 2026-04-14):

- `search=Electronics`: status `200`, count `13`, names include `["Electronics", "Connectors", "Pin Headers", "IC", "Interface"]` — all have `pathstring` containing `"Electronics"`
- `parent=1`: status `200`, count `6`, all results have `parent: 1` — `["Connectors", "IC", "Passives", "PCB", "PCBA", "Wire"]`
- `top_level=true`: status `200`, all results have `parent: null`
- Matches spec: Yes

**Notes:** The `search` parameter on `/api/part/category/` searches `name`, `description`, and `pathstring` fields. This means searching for a parent category name will also return all its descendants (since their `pathstring` includes the parent name). The `parent` filter returns only direct children — grandchildren require `cascade=true` to be included.
