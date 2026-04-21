# TC-ACCMP-018: GET /api/company/address/{id}/ retrieves a single address by primary key

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/company/address/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- An address with a known `id` exists; create one via the setup steps below if needed

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-018-AddrCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "88 Query Road", "city": "Fetchburg"}` and record the response `id` as `ADDRESS_ID`
4. Send `GET /api/company/address/{ADDRESS_ID}/` with the authorization header
5. Verify response status code is `200`
6. Verify response body field `id` equals `ADDRESS_ID`
7. Verify response body field `company` equals `COMPANY_ID`
8. Verify response body field `line1` equals `"88 Query Road"`
9. Verify response body field `city` equals `"Fetchburg"`
10. Verify response body does NOT contain a `results` wrapper (it is a flat object, not paginated)
11. Send `DELETE /api/company/address/{ADDRESS_ID}/` to clean up
12. Verify DELETE response status code is `204`
13. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
14. Verify DELETE response status code is `204`

**Request (step 4):**

- Method: `GET`
- URL: `/api/company/address/{ADDRESS_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with the full Address object for the given `id`. The response is a single flat object. The `company` field is the integer FK to the owning company. All schema fields are present; nullable fields may be `null`.

**Observed** (probed 2026-04-17):

- Status: `200`
- Flat Address object returned (no `results` wrapper)
- `id`, `company`, `line1`, `city` all match submitted values
- All nullable fields present as `null` or their values
- Matches spec: Yes

**Notes:** The detail endpoint returns all address fields including `is_default`, `type`, `title`, `line2`, `state`, `postal_code`, `country`, `phone`, `email`, `notes`, `created`, and `updated`. Using a dynamically created address ensures the test is not dependent on pre-existing seed data.
