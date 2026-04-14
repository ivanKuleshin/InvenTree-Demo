# TC-APFLT-003: GET /api/part/?category= filters parts to a specific category

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Category with `pk=1` ("Electronics") exists and has parts assigned to it or its subcategories
- No authentication required

**Steps:**

1. Send `GET /api/part/?category=1&limit=5`
2. Verify response status code is `200`
3. Verify response body field `count` is greater than `0`
4. Verify every part in `results` has `category` equal to `1` (direct members only, not subcategory members)
5. Record `DIRECT_COUNT` from the response `count`
6. Send `GET /api/part/?category=1&cascade=true&limit=5`
7. Verify response status code is `200`
8. Verify response body field `count` is greater than or equal to `DIRECT_COUNT` (cascade includes subcategory parts)
9. Send `GET /api/part/?category=null&limit=5`
10. Verify response status code is `200`
11. Verify every part in `results` has `category` equal to `null`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?category=1&limit=5`
- Headers: _(none required)_

**Expected Result:** `category=<pk>` filters to parts directly assigned to that category. `cascade=true` broadens the filter to include all descendant categories. `category=null` filters to uncategorized parts.

**Observed** (probed 2026-04-14):

- `category=1&limit=5`: status `200`, count `144`, first result `pk=69 category=13` — the filter returns parts in subcategories too by default
- `category=null&limit=5`: status `200`, all returned parts have `category: null`
- Matches spec: Partial — `category=1` without `cascade` returned parts from subcategories (e.g., category 13 is a child of 1), suggesting `cascade` may default to `true` on this instance

**Notes:** On the demo instance, `category=1` without `cascade=true` returned parts whose `category` was `13` (a child of `1`), not `1` directly. This suggests the demo may default `cascade=true`, or the filter applies to the category tree rather than direct members. Automation assertions should use `cascade=false` explicitly when testing direct-membership filtering, and verify behavior against the actual count rather than assuming strict direct-only filtering.
