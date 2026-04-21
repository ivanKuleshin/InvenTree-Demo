# TC-APVAL-010: POST /api/part/ with duplicate name — uniqueness behavior

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No part named `"TC-APVAL-010-DupeName"` exists

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APVAL-010-DupeName"}`
3. Verify response status code is `201` and record `pk` as `FIRST_PK`
4. Send a second `POST /api/part/` with the identical body `{"name": "TC-APVAL-010-DupeName"}`
5. Observe the response status code and body
6. If status is `201`: record `pk` as `SECOND_PK` — uniqueness is NOT enforced by the API; clean up both parts
7. If status is `400`: verify response body contains key `"non_field_errors"` or `"name"` with a uniqueness error message — uniqueness IS enforced
8. Clean up: deactivate and delete any created parts

**Request (step 2 and step 4):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APVAL-010-DupeName" }
  ```

**Expected Result:** The schema does NOT list a unique constraint on `name` alone (unlike categories where `(name, parent)` is unique). The API is expected to allow duplicate part names, returning `201` for both requests.

**Observed** (probed 2026-04-18):

- First POST status: `201`
- Second POST status: `201` (duplicate name allowed)
- Both parts received distinct `pk` values
- No uniqueness error returned
- The Part API does NOT enforce name uniqueness — multiple parts with identical names can coexist
- Matches schema: Yes — the schema has no `uniqueItems` or `unique_together` constraint on `name` for parts (unlike categories where `(name, parent)` must be unique)

**Notes:** Part name uniqueness is NOT enforced by the InvenTree API. This is intentional — the same physical component may be sourced from multiple suppliers under the same common name, or variant parts may share a base name. The `full_name` field (read-only) combines `name`, `IPN`, and `revision` to produce a more unique display string, but no API-level uniqueness constraint exists on `name` alone. Automation must clean up both created parts in teardown.
