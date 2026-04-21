# TC-ACCMP-002: GET /api/company/{id}/ retrieves a single company by primary key

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/company/{id}/`

**Preconditions:**

- A company with a known `id` exists on the server
- Valid HTTP Basic credentials are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?limit=1` and record the `id` of the first result as `COMPANY_ID`
3. Send `GET /api/company/{COMPANY_ID}/` with the authorization header
4. Verify response status code is `200`
5. Verify response body field `id` equals `COMPANY_ID`
6. Verify response body contains field `name` as a non-empty string
7. Verify response body contains field `active` as a boolean
8. Verify response body contains field `is_supplier` as a boolean
9. Verify response body contains field `is_manufacturer` as a boolean
10. Verify response body contains field `is_customer` as a boolean
11. Verify response body contains field `created` as a non-null ISO 8601 datetime string
12. Verify response body contains field `updated` as a non-null ISO 8601 datetime string
13. Verify response body does NOT contain a `results` wrapper (detail response is a flat object, not paginated)

**Request:**

- Method: `GET`
- URL: `/api/company/{COMPANY_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with the full Company object for the given `id`. The response is a single flat object (not a paginated list). All schema fields are present, nullable fields may be `null`.

**Observed** (probed 2026-04-17):

- Status: `200`
- Response is a flat Company object, not paginated
- All role boolean fields (`is_supplier`, `is_manufacturer`, `is_customer`) present
- `created` and `updated` present as ISO 8601 timestamps
- Matches spec: Yes

**Notes:** The `id` field is the primary key used in all company sub-resource endpoints (addresses, contacts). Always use a dynamically resolved `COMPANY_ID` from a prior GET rather than hardcoding a PK, since demo data changes between test runs.
