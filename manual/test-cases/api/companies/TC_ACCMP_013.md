# TC-ACCMP-013: POST /api/company/ with invalid email format returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-013-InvalidEmail", "email": "not-an-email"}`
3. Verify response status code is `400`
4. Verify response body contains key `"email"` with a validation error message (e.g., `"Enter a valid email address."`)
5. Send `POST /api/company/` with body `{"name": "TC-ACCMP-013-InvalidWebsite", "website": "not-a-url"}`
6. Verify response status code is `400`
7. Verify response body contains key `"website"` with a validation error message (e.g., `"Enter a valid URL."`)
8. Send `POST /api/company/` with body `{"name": "TC-ACCMP-013-InvalidLink", "link": "ftp://invalid-scheme.example.com"}`
9. Verify response status code is either `400` (scheme rejected) or `201` (scheme accepted); record the actual behavior
10. If step 9 returned `201`, send `DELETE /api/company/{id}/` to clean up and verify `204`

**Request (step 2):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCMP-013-InvalidEmail", "email": "not-an-email" }
  ```

**Request (step 5):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-ACCMP-013-InvalidWebsite", "website": "not-a-url" }
  ```

**Expected Result:** An invalid email value for `email` returns `400` with a field-level error. An invalid URL value for `website` returns `400` with a field-level error. Both validations are type-level constraints defined in the schema.

**Observed** (probed 2026-04-17):

- Invalid email: status `400`; `{"email": ["Enter a valid email address."]}`
- Invalid URL for `website`: status `400`; `{"website": ["Enter a valid URL."]}`
- `ftp://` scheme for `link`: status `400`; `{"link": ["Enter a valid URL."]}` (only `http`/`https` accepted)
- Matches spec: Yes

**Notes:** Django REST Framework validates `EmailField` and `URLField` types server-side. The `link` and `website` fields accept only `http` and `https` schemes — `ftp://`, `javascript:`, and bare hostnames are rejected. These validations fire even on PATCH requests if the field value is included.
