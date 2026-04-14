# TC-APEDGE-003: GET /api/part/{id}/ with non-existent PK returns 404

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/{id}/`

**Preconditions:**

- Part PK `99999999` does not exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/99999999/`
2. Verify response status code is `404`
3. Verify response body contains key `"detail"`
4. Verify the value of `"detail"` equals `"No Part matches the given query."`
5. Send `GET /api/part/0/` (zero PK — invalid)
6. Verify response status code is `404`
7. Send `GET /api/part/-1/` (negative PK — invalid)
8. Verify response status code is `404`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/99999999/`
- Headers: _(none required)_

**Expected Result:** Any PK that does not correspond to an existing part returns `404`. The response body uses a model-specific message rather than the generic `"Not found."`.

**Observed** (probed 2026-04-14):

- `GET /api/part/99999999/` status: `404`
- Response body:
  ```json
  { "detail": "No Part matches the given query." }
  ```
- Matches spec: Partial divergence — documentation shows generic `{"detail": "Not found."}` but actual response uses the model-specific message `"No Part matches the given query."`

**Notes:** The exact 404 message is `"No Part matches the given query."` — not the generic `"Not found."`. Automation assertions that hard-code `"Not found."` will fail. Use the model-specific string or check only the status code `404` for portability across resource types.
