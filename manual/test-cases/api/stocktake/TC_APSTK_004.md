# TC-APSTK-004: PATCH /api/part/stocktake/{id}/ updates a stocktake record

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/stocktake/{id}/`

**Preconditions:**

- Stocktake record with pk `849` exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `PATCH /api/part/stocktake/849/` with `{"quantity": 0}` (value matches existing, so it is a no-op write that still exercises the PATCH verb)
3. Verify response status code is `200`
4. Verify `pk` equals `849` in the response
5. Verify response includes a nested `part_detail` object with full part fields

**Request:**

- Method: `PATCH`
- URL: `/api/part/stocktake/849/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "quantity": 0 }
  ```

**Expected Result:** `200 OK` with updated stocktake. Response includes `part_detail` nested object not present in the GET response.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response snippet:
  ```json
  {
    "pk": 849,
    "part": 907,
    "quantity": 0,
    "date": "2024-07-27",
    "part_detail": {
      "pk": 907,
      "IPN": "002.01-PCBA",
      "name": "Widget Board (assembled)",
      "active": true,
      "trackable": true,
      "assembly": true
    }
  }
  ```
- `part_detail` present in PATCH response (absent in GET list/detail)
- Matches spec: Partial — extra `part_detail` field undocumented in schema

**Notes:** [DIVERGENCE] PATCH response includes a `part_detail` nested object with full part info not present in GET responses. This is an inconsistency in the API response shape between verbs.
