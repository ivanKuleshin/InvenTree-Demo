# TC-ACCMP-024: GET /api/company/address/ filters addresses by company FK

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/company/address/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- Create a test company and two addresses linked to it; create a second company with no addresses

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-024-AddrFilterCo"}` and record the response `id` as `COMPANY_ID`
3. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "Filter Road 1"}` and record the response `id` as `ADDR1_ID`
4. Send `POST /api/company/address/` with body `{"company": COMPANY_ID, "line1": "Filter Road 2"}` and record the response `id` as `ADDR2_ID`
5. Send `GET /api/company/address/?company={COMPANY_ID}&limit=25` with the authorization header
6. Verify response status code is `200`
7. Verify response body field `count` equals `2`
8. Verify both `ADDR1_ID` and `ADDR2_ID` appear in the `results` array
9. Verify every item in `results` has `company` equal to `COMPANY_ID`
10. Send `DELETE /api/company/address/{ADDR1_ID}/` to clean up
11. Verify DELETE response status code is `204`
12. Send `DELETE /api/company/address/{ADDR2_ID}/` to clean up
13. Verify DELETE response status code is `204`
14. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
15. Verify DELETE response status code is `204`

**Request (step 5):**

- Method: `GET`
- URL: `/api/company/address/?company={COMPANY_ID}&limit=25`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** The `company` query parameter filters the address list to only those addresses belonging to the specified company `id`. The `count` equals the number of addresses created for that company. All results have the expected `company` FK value.

**Observed** (probed 2026-04-17):

- `company={COMPANY_ID}` filter returns only the two addresses linked to that company
- `count`: `2`
- All results have `company` equal to `COMPANY_ID`
- Matches spec: Yes

**Notes:** The `company` query parameter accepts a single integer FK value. Using an id of a company with no addresses returns `{"count": 0, "results": []}`. This filter is essential for scoping address queries to a single organization.
