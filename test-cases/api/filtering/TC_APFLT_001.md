# TC-APFLT-001: GET /api/part/ with limit and offset returns correct pagination window

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least 6 parts exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/?limit=3` and record the `pk` values of all 3 results as `PAGE_1_PKS`
2. Verify response status code is `200`
3. Verify response body field `results` contains exactly `3` items
4. Verify response body field `previous` equals `null`
5. Verify response body field `next` is a non-null URI containing `limit=3` and `offset=3`
6. Send `GET /api/part/?limit=3&offset=3` and record the `pk` values as `PAGE_2_PKS`
7. Verify response status code is `200`
8. Verify response body field `results` contains exactly `3` items
9. Verify response body field `previous` is a non-null URI
10. Verify none of `PAGE_2_PKS` appear in `PAGE_1_PKS` (no overlap between pages)
11. Verify the `count` field is identical in both responses

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?limit=3`
- Headers: _(none required)_

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?limit=3&offset=3`
- Headers: _(none required)_

**Expected Result:** Each page returns exactly `limit` items. Pages do not overlap. `previous` is null on the first page and non-null on subsequent pages. `count` is the same across all pages of the same query.

**Observed** (probed 2026-04-14):

- Page 1 (`limit=3`): status `200`, pks `[82, 84, 83]`, `previous: null`, `next` contains `offset=3`
- Page 2 (`limit=3&offset=3`): status `200`, pks `[86, 85, 69]`, `previous` non-null
- No pk overlap between pages
- `count` identical in both responses: `907`
- Matches spec: Yes

**Notes:** The default sort order on the demo is not guaranteed to be stable across runs (other users may add/delete parts). Pagination correctness assertions should focus on structural properties (no overlap, correct page size) rather than asserting specific `pk` values.
