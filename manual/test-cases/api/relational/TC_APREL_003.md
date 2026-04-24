# TC-APREL-003: POST /api/part/ with valid default_location FK persists the relationship

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- Stock location `pk=1` ("Factory") exists on the server

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APREL-003-PartWithLoc", "default_location": 1}`
3. Verify response status code is `201`
4. Verify response body field `default_location` equals `1`
5. Record the response `pk` as `PART_PK`
6. Send `GET /api/part/{PART_PK}/` and verify `default_location` still equals `1` (persisted across GET)
7. Send `POST /api/part/` with body `{"name": "TC-APREL-003-BadLoc", "default_location": 99999999}`
8. Verify response status code is `400`
9. Verify response body contains key `"default_location"`
10. Verify the value contains a string including `"Invalid pk"` and `"object does not exist"`
11. Send `PATCH /api/part/{PART_PK}/` with body `{"active": false}`
12. Send `DELETE /api/part/{PART_PK}/` to clean up
13. Verify DELETE response status code is `204`

**Request (step 2):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APREL-003-PartWithLoc", "default_location": 1 }
  ```

**Expected Result:** A valid `default_location` PK is accepted and stored on the part. The value is returned in both the create response and subsequent GET responses. An invalid PK returns `400` with a `default_location` field error.

**Observed** (probed 2026-04-14):

- Valid location POST status: `201`; `pk`: `1656`; `default_location: 1`
- Invalid location POST status: `400`
- Response body:
  ```json
  { "default_location": ["Invalid pk \"99999999\" - object does not exist."] }
  ```
- Matches spec: Yes

**Notes:** The `default_location` on a part takes precedence over the `category_default_location` (inherited from category) when both are set. The read-only `category_default_location` field reflects what would be inherited if `default_location` were null — both fields are present in the part response for transparency.
