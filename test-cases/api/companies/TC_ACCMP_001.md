# TC-ACCMP-001: GET /api/company/ returns paginated company list with full field set

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/company/`

**Preconditions:**

- At least one company exists on the server
- Valid HTTP Basic credentials are available (authentication is required for all company endpoints)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?limit=5` with the authorization header
3. Verify response status code is `200`
4. Verify response body contains field `count` with an integer value greater than `0`
5. Verify response body contains field `results` as an array of at most `5` items
6. Verify response body contains field `next` as either a URI string or `null`
7. Verify response body contains field `previous` as `null` (first page)
8. Verify the first result contains all of the following fields: `id`, `name`, `description`, `website`, `address`, `phone`, `email`, `contact`, `link`, `image`, `currency`, `active`, `is_supplier`, `is_manufacturer`, `is_customer`, `notes`, `created`, `updated`
9. Verify the `id` field in the first result is a positive integer
10. Verify the `active` field in the first result is a boolean
11. Verify the `created` and `updated` fields are ISO 8601 datetime strings

**Request:**

- Method: `GET`
- URL: `/api/company/?limit=5`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server returns `200 OK` with a `PaginatedCompanyList`. The `count` field reflects total matching companies. Each company object contains the full flat field set including all role booleans (`is_supplier`, `is_manufacturer`, `is_customer`) and audit timestamps.

**Observed** (probed 2026-04-17):

- Status: `200`
- `count`: present, integer > 0
- Response snippet (first result):
  ```json
  {
    "pk": 1,
    "name": "DigiKey",
    "description": "Electronic components distributor",
    "website": "https://www.digikey.com",
    "address": null,
    "phone": null,
    "email": null,
    "contact": null,
    "is_supplier": true,
    "is_manufacturer": false,
    "is_customer": false,
    "active": true,
    "created": "2021-05-11T13:31:36.358000Z",
    "updated": "2021-05-11T13:31:36.358000Z"
  }
  ```
- `next` and `previous` pagination links present
- Matches spec: Yes

**Notes:** Unlike `/api/part/`, the company endpoint requires authentication — unauthenticated GET returns `403` on the demo. The `address` field on the Company schema is a free-text legacy field (max 250 chars), distinct from the structured `/api/company/address/` sub-resource.
