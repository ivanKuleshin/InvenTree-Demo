# TC-APCRUD-001: GET /api/part/ returns paginated part list with full field set

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least one part exists on the server
- No authentication required (demo allows public read)

**Steps:**

1. Send `GET /api/part/?limit=5` with no authorization header
2. Verify response status code is `200`
3. Verify response body contains field `count` with an integer value greater than `0`
4. Verify response body contains field `results` as an array of exactly `5` items
5. Verify response body contains field `next` as a non-null URI string (more pages available)
6. Verify response body contains field `previous` as `null` (first page)
7. Verify the first result contains all of the following fields: `pk`, `name`, `IPN`, `revision`, `description`, `category`, `active`, `assembly`, `component`, `is_template`, `trackable`, `virtual`, `purchasable`, `salable`, `locked`, `full_name`, `thumbnail`, `creation_date`, `in_stock`, `total_in_stock`, `unallocated_stock`, `stock_item_count`, `pricing_min`, `pricing_max`
8. Verify that the `full_name` field for a part with no IPN and no revision equals just the `name` value

**Request:**

- Method: `GET`
- URL: `/api/part/?limit=5`
- Headers: _(none required)_

**Expected Result:** Server returns `200 OK` with a `PaginatedPartList`. Each part object contains the complete flat field set including all computed read-only fields. The `full_name` is auto-formatted based on IPN and revision presence.

**Observed** (probed 2026-04-14):

- Status: `200` (no auth)
- count: `904`–`917` (fluctuates as demo data is modified by other users)
- Response snippet (first result):
  ```json
  {
    "pk": 82,
    "name": "1551ABK",
    "IPN": "",
    "revision": "",
    "description": "Small plastic enclosure, black",
    "category": null,
    "active": true,
    "assembly": false,
    "component": true,
    "is_template": false,
    "full_name": "1551ABK",
    "in_stock": 765,
    "total_in_stock": 765,
    "unallocated_stock": 765,
    "stock_item_count": 6,
    "pricing_min": null,
    "pricing_max": null
  }
  ```
- `full_name` = `"1551ABK"` when IPN and revision are both empty
- Matches spec: Yes

**Notes:** The demo instance allows unauthenticated GET on `/api/part/` — `200` is returned without any `Authorization` header. This is a demo-environment configuration, not the default InvenTree behavior for production deployments.
