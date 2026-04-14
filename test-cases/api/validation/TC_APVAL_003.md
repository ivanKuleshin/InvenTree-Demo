# TC-APVAL-003: POST /api/part/category/ with name exceeding 100 characters returns 400

**Type:** API
**Priority:** P3
**Endpoint:** `POST /api/part/category/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No specific data state required (request will be rejected before persistence)

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Construct a string of exactly 101 characters (e.g., the letter `"A"` repeated 101 times)
3. Send `POST /api/part/category/` with body `{"name": "<101-char string>"}`
4. Verify response status code is `400`
5. Verify response body contains key `"name"`
6. Verify the value of `"name"` is an array containing the string `"Ensure this field has no more than 100 characters."`
7. Verify response body does NOT contain a `pk` field
8. Send `POST /api/part/category/` with body `{"name": "<100-char string>"}` (exactly at the boundary)
9. Verify response status code is `201`
10. Record the `pk` from the 201 response as `BOUNDARY_PK`
11. Send `DELETE /api/part/category/{BOUNDARY_PK}/` to clean up
12. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `POST`
- URL: `/api/part/category/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
  }
  ```
  _(101 "A" characters)_

**Expected Result:** A name of 101 characters is rejected with `400`. A name of exactly 100 characters is accepted with `201`. The boundary value (100 chars) is valid; 101 chars is not.

**Observed** (probed 2026-04-14):

- 101-char name: status `400`
- Response body:
  ```json
  { "name": ["Ensure this field has no more than 100 characters."] }
  ```
- Matches spec: Yes

**Notes:** The `description` field on categories has the same 250-character max. The boundary test (exactly 100 chars) was not separately probed at the time of writing — mark the 100-char step as `[ASSUMED]` until an automation run confirms it. The 101-char rejection was directly observed.
