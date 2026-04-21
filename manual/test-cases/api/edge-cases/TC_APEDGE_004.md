# TC-APEDGE-004: GET /api/part/category/{id}/ with non-existent PK returns 404

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/category/{id}/`

**Preconditions:**

- Category PK `99999999` does not exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/category/99999999/`
2. Verify response status code is `404`
3. Verify response body contains key `"detail"`
4. Verify the value of `"detail"` equals `"No PartCategory matches the given query."`
5. Send `DELETE /api/part/category/99999999/` with valid admin credentials
6. Verify response status code is `404`
7. Verify response body contains key `"detail"` with value `"No PartCategory matches the given query."`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/category/99999999/`
- Headers: _(none required)_

**Request (step 5):**

- Method: `DELETE`
- URL: `/api/part/category/99999999/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Both GET and DELETE on a non-existent category PK return `404`. The model-specific message is `"No PartCategory matches the given query."`.

**Observed** (probed 2026-04-14):

- GET status: `404`
- GET body: `{"detail": "No PartCategory matches the given query."}`
- Matches spec: Partial divergence — documentation shows generic `{"detail": "Not found."}` but model-specific message is used

**Notes:** The 404 message differs between resource types: parts use `"No Part matches the given query."` and categories use `"No PartCategory matches the given query."`. This pattern extends to all InvenTree model types. Automation should use resource-type-specific assertions or assert only on status code `404`.
