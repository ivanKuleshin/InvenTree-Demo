# TC-APCRUD-007: POST /api/part/ using the duplicate write-only field

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- A source part exists with a known PK (obtain via `GET /api/part/?limit=5`, record as `SOURCE_PK`)
- The source part has a category assigned (to verify whether category is inherited)
- The name "TC-APCRUD-007-DuplicatePart" does not already exist

**Steps:**

1. Send `GET /api/part/?limit=5` to retrieve a source part PK with a non-null category (record as `SOURCE_PK` and `SOURCE_CATEGORY`)
2. Send POST to `/api/part/` with body containing `name` and `duplicate.part = SOURCE_PK`
3. Verify response status code is `201`
4. Verify response body field `name` equals `"TC-APCRUD-007-DuplicatePart"`
5. Verify response body does NOT contain the key `duplicate` (write-only — not returned)
6. Verify response body field `category` equals `null` (category is NOT inherited from the source part)
7. Retrieve the new part via `GET /api/part/<new_pk>/` and verify it exists and is independent of the source
8. DELETE the created part to clean up test data

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-APCRUD-007-DuplicatePart",
    "duplicate": { "part": SOURCE_PK, "copy_image": false, "copy_notes": false, "copy_parameters": false, "copy_bom": false }
  }
  ```

**Expected Result:** Server creates a new part using the source part as a template. The `duplicate` field is write-only and is not included in the response. The new part is independent (category is not automatically inherited from source).

**Observed** (probed 2026-04-14):

- Status: `201`
- Response snippet:
  ```json
  {
    "pk": 1494,
    "name": "TC-APCRUD-007-DuplicatePart",
    "category": null
  }
  ```
- The key `duplicate` is absent from the response body
- Matches spec: Yes

**Notes:** Category is not inherited from the source part when using the `duplicate` write-only field — the new part has `category: null` unless explicitly set in the same POST payload.
