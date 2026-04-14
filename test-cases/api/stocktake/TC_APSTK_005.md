# TC-APSTK-005: DELETE /api/part/stocktake/{id}/ returns 404 for non-existent record

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/stocktake/{id}/`

**Preconditions:**

- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `DELETE /api/part/stocktake/99999999/` using a pk that does not exist
3. Verify response status code is `404`
4. Verify response body contains `detail` field

**Request:**

- Method: `DELETE`
- URL: `/api/part/stocktake/99999999/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `404 Not Found` when attempting to delete a non-existent stocktake.

**Observed** (probed 2026-04-14):

- Status: `404`
- Response:
  ```json
  { "detail": "No PartStocktake matches the given query." }
  ```
- Matches spec: Yes

**Notes:** Error message format is `"No <ModelName> matches the given query."` — consistent with the rest of the InvenTree API.
