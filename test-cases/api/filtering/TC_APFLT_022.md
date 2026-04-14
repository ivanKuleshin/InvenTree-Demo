# TC-APFLT-022: GET /api/part/?category=<pk> with cascade=true includes parts in child categories

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Category `pk=24` ("Category 1") exists with at least one direct part and at least one child category (`pk=26` "Category 3") that also has parts
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?category=24&limit=10` and record `count` as `DIRECT_COUNT` and record all distinct `category` values from `results` as `DIRECT_CATEGORIES`
2. Send `GET /api/part/?category=24&cascade=true&limit=10` with header `Authorization: Token <token>`
3. Verify response status code is `200`
4. Verify `count` is greater than or equal to `DIRECT_COUNT`
5. Verify at least one result in `results` has `category` equal to `26` (child of 24)

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?category=24&limit=10`
- Headers: `Authorization: Token <token>`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?category=24&cascade=true&limit=10`
- Headers: `Authorization: Token <token>`

**Expected Result:** `cascade=true` broadens the category filter to include parts in all descendant categories, not just the directly assigned category. The result set with `cascade=true` is a superset of the result without cascade (or equal if no children exist).

**Observed** (probed 2026-04-14):

- `category=24` (no cascade): status `200`, count `2`, results: `pk=1933 category=24`, `pk=2267 category=26`
- `category=24&cascade=true`: status `200`, count `2`, same results
- Both return a part in child category `26` (Category 3, which has `parent=24`)
- Note: without explicit `cascade=false`, the demo instance includes child category parts by default
- Matches spec: Partial — `cascade` appears to default to `true` on this demo instance (child category parts are included even without specifying `cascade=true`)

**Notes:** On the demo instance, `category=24` without `cascade` already returned a part with `category=26` (a child of 24), indicating cascade behavior is the default. Automation tests should use `category=24&cascade=true` to explicitly test cascade inclusion and verify both parent and child category parts appear in results.
