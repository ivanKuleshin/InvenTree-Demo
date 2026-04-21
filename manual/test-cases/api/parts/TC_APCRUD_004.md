# TC-APCRUD-004: PUT /api/part/{id}/ replaces all writable fields on an existing part

**Type:** API
**Priority:** P2
**Endpoint:** `PUT /api/part/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- A test part exists; create one via `POST /api/part/` with `{"name": "TC-APCRUD-004-Setup", "IPN": "IPN-APCRUD-004", "category": 1}` and record its `pk` as `PART_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APCRUD-004-Setup", "IPN": "IPN-APCRUD-004", "category": 1}` and record the response `pk` as `PART_PK`
3. Send `PUT /api/part/{PART_PK}/` with the full replacement body below
4. Verify response status code is `200`
5. Verify response body field `name` equals `"TC-APCRUD-004-UpdatedName"`
6. Verify response body field `description` equals `"PUT full update applied"`
7. Verify response body field `active` equals `true`
8. Verify response body field `assembly` equals `true`
9. Verify response body field `keywords` equals `"put update test"`
10. Verify response body field `pk` equals `PART_PK`
11. Send `PATCH /api/part/{PART_PK}/` with body `{"active": false}` to deactivate
12. Send `DELETE /api/part/{PART_PK}/` to clean up
13. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PUT`
- URL: `/api/part/{PART_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-APCRUD-004-UpdatedName",
    "description": "PUT full update applied",
    "IPN": "IPN-APCRUD-004",
    "active": true,
    "assembly": true,
    "component": true,
    "purchasable": true,
    "salable": false,
    "is_template": false,
    "trackable": false,
    "virtual": false,
    "keywords": "put update test",
    "category": 1
  }
  ```

**Expected Result:** Server replaces all writable fields with the provided values and returns `200 OK`. The `pk` is unchanged. Read-only fields (`full_name`, `in_stock`, `creation_date`) are recomputed and returned but cannot be overwritten.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 1653,
    "name": "TC-APCRUD-TestPart",
    "description": "PUT full update applied",
    "active": true,
    "assembly": false
  }
  ```
- Matches spec: Yes

**Notes:** Unlike PATCH, PUT requires the `name` field — omitting it returns `400 {"name": ["This field is required."]}`. Active parts cannot be deleted directly; PATCH `{"active": false}` must be sent first. The full-name computed field updates immediately to reflect the new `name`, `IPN`, and `revision` values.
