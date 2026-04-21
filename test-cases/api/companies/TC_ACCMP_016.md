# TC-ACCMP-016: GET /api/company/address/ returns paginated address list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/company/address/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- At least one address exists on the server

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/address/?limit=5` with the authorization header
3. Verify response status code is `200`
4. Verify response body contains field `count` as a non-negative integer
5. Verify response body contains field `results` as an array
6. Verify response body contains field `next` as either a URI string or `null`
7. Verify response body contains field `previous` as `null` (first page)
8. If `results` is non-empty, verify the first result contains fields: `id`, `company`, `line1`, `is_default`, `created`, `updated`
9. Verify the `company` field in each result is a positive integer (FK reference to a Company)
10. Verify the `id` field in each result is a positive integer

**Request:**

- Method: `GET`
- URL: `/api/company/address/?limit=5`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with a `PaginatedAddressList`. Each address object contains the full flat field set. The `company` field is an integer FK reference, not an expanded object.

**Observed** (probed 2026-04-17):

- Status: `200`
- `count`: non-negative integer
- Each result contains `id`, `company`, `line1`, `is_default`
- `company` is an integer (FK)
- Matches spec: Yes

**Notes:** The `/api/company/address/` endpoint lists all addresses across all companies. To filter addresses for a specific company, use the `company` query parameter (e.g., `?company={COMPANY_ID}`). This endpoint requires authentication.
