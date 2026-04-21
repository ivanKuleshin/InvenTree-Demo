# TC-ACCRUD-105: DELETE /api/part/category/{pk}/ removes a category

**Type:** API
**Priority:** P1
**Endpoint:** `DELETE /api/part/category/{pk}/`

**Preconditions:**

- Valid HTTP Basic credentials with delete permissions are available
- A test category can be created via `POST /api/part/category/` before this test
- The category to delete has no child categories and no associated parts

**Steps:**

1. Send `POST /api/part/category/` with body `{"name": "TC-ACCRUD-105-ToDelete", "description": "Will be deleted"}` and record `pk` as `CAT_PK`
2. Verify POST response status is `201`
3. Send `DELETE /api/part/category/{CAT_PK}/` with `Authorization: Basic <base64(admin:inventree)>`
4. Verify response status code is `204`
5. Verify response body is empty (no JSON content)
6. Send `GET /api/part/category/{CAT_PK}/` to confirm deletion
7. Verify GET response status code is `404`
8. Verify GET response body contains field `detail` with a message indicating no match (e.g., `"No PartCategory matches the given query."`)

**Request:**

- Method: `DELETE`
- URL: `/api/part/category/{CAT_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server deletes the category and returns `204 No Content` with an empty body. A subsequent GET for the same `pk` returns `404 Not Found`.

**Observed** (probed 2026-04-18):

- Created category with `pk=29`
- Sent `DELETE /api/part/category/29/`
- Status: `204`; body: empty
- Follow-up `GET /api/part/category/29/`:
  - Status: `404`
  - Body: `{"detail": "No PartCategory matches the given query."}`
- Matches spec: Yes

**Notes:** The `204 No Content` response has no body. The follow-up GET confirming deletion returns a standard DRF 404 detail message. The same endpoint will return `404` for any subsequent attempts to GET, PATCH, or DELETE the removed category.
