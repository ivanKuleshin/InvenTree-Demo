# TC-ACCMP-019: PUT /api/company/address/{id}/ replaces all writable address fields

**Type:** API
**Priority:** P2
**Endpoint:** `PUT /api/company/address/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- Create a test company and address via setup steps below; record `COMPANY_ID` and `ADDRESS_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-019-PutCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "Original Street", "city": "OldCity"}` and record the response `id` as `ADDRESS_ID`
4. Send `PUT /api/company/address/{ADDRESS_ID}/` with the full replacement body below
5. Verify response status code is `200`
6. Verify response body field `id` equals `ADDRESS_ID` (unchanged)
7. Verify response body field `company` equals `COMPANY_ID` (unchanged)
8. Verify response body field `line1` equals `"100 Updated Boulevard"`
9. Verify response body field `line2` equals `"Suite 200"`
10. Verify response body field `city` equals `"NewCity"`
11. Verify response body field `state` equals `"NC"`
12. Verify response body field `postal_code` equals `"99001"`
13. Verify response body field `country` equals `"GB"`
14. Verify response body field `is_default` equals `true`
15. Verify response body field `title` equals `"Head Office"`
16. Send `DELETE /api/company/address/{ADDRESS_ID}/` to clean up
17. Verify DELETE response status code is `204`
18. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
19. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `PUT`
- URL: `/api/company/address/{ADDRESS_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "company": COMPANY_ID,
    "line1": "100 Updated Boulevard",
    "line2": "Suite 200",
    "city": "NewCity",
    "state": "NC",
    "postal_code": "99001",
    "country": "GB",
    "title": "Head Office",
    "type": "shipping",
    "phone": "+44-20-0000-0000",
    "email": "office@example.co.uk",
    "is_default": true,
    "notes": "Updated by TC-ACCMP-019"
  }
  ```

**Expected Result:** Server replaces all writable fields with the provided values and returns `200 OK`. The `id` and `company` FK are preserved. Read-only fields (`created`, `updated`) are recomputed.

**Observed** (probed 2026-04-17):

- Status: `200`
- All submitted fields reflected in response
- `id` and `company` unchanged
- `is_default`: `true` (updated)
- Matches spec: Yes

**Notes:** PUT on an address requires `company` and `line1` to be present in the body — omitting either returns `400`. The `company` FK in a PUT body must match the original company (reassigning an address to a different company may be rejected by server-side constraints).
