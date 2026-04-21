# TC-APRELATED-004: PATCH /api/part/related/{id}/ updates the note field

**Type:** API
**Priority:** P2
**Endpoint:** `PATCH /api/part/related/{id}/`

**Preconditions:**

- A related part entry created via POST (precondition setup step)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Create: `POST /api/part/related/` with `{"part_1": 73, "part_2": 75, "note": "initial note"}`; record `pk`
3. Send `PATCH /api/part/related/{pk}/` with body below
4. Verify response status code is `200`
5. Verify `note` equals `"TC-APRELATED updated note"`
6. Verify `part_1` and `part_2` are unchanged
7. Clean up: `DELETE /api/part/related/{pk}/`

**Request:**

- Method: `PATCH`
- URL: `/api/part/related/{pk}/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  { "note": "TC-APRELATED updated note" }
  ```

**Expected Result:** `200 OK`. Only `note` changes; `part_1` and `part_2` are immutable after creation.

**Observed** (probed 2026-04-14):

- Status: `200`
- `note` updated to `"TC-APRELATED updated note"`
- `part_1` and `part_2` unchanged
- Matches spec: Yes

**Notes:** None.
