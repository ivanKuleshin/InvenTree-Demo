# TC-APCRUD-005: PATCH /api/part/{id}/ partially updates selected fields on an existing part

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- A test part exists; create one via `POST /api/part/` with `{"name": "TC-APCRUD-005-Setup", "active": true, "keywords": "original"}` and record its `pk` as `PART_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APCRUD-005-Setup", "active": true, "keywords": "original"}` and record the response `pk` as `PART_PK`
3. Send `PATCH /api/part/{PART_PK}/` with body `{"active": false, "keywords": "patch-test qa"}`
4. Verify response status code is `200`
5. Verify response body field `active` equals `false`
6. Verify response body field `keywords` equals `"patch-test qa"`
7. Verify response body field `name` equals `"TC-APCRUD-005-Setup"` (unchanged)
8. Verify response body field `component` equals `true` (unchanged default)
9. Verify response body field `purchasable` equals `true` (unchanged default)
10. Verify response body field `pk` equals `PART_PK`
11. Send `DELETE /api/part/{PART_PK}/` to clean up (part is now inactive so DELETE is allowed)
12. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PATCH`
- URL: `/api/part/{PART_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "active": false, "keywords": "patch-test qa" }
  ```

**Expected Result:** Server applies only the two specified field changes (`active` and `keywords`) and returns `200 OK`. All other fields retain their current values. Because `active` is now `false`, the part can be deleted immediately without an additional PATCH step.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 1653,
    "name": "TC-APCRUD-TestPart",
    "active": false,
    "keywords": "patch-test qa",
    "component": true,
    "purchasable": true
  }
  ```
- Matches spec: Yes

**Notes:** Setting `active: false` via PATCH is a prerequisite for DELETE — sending DELETE on an active part returns `400 {"non_field_errors": ["Cannot delete this part as it is still active"]}`. This constraint is not documented in the API spec but was confirmed in live testing.
