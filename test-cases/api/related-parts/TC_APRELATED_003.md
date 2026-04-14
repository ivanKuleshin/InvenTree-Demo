# TC-APRELATED-003: POST /api/part/related/ creates a new related part link

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/related/`

**Preconditions:**

- Two parts that do not already have a relationship (e.g., pk `73` and `75`)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/related/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer
5. Verify `part_1` equals `73`, `part_2` equals `75`
6. Verify `note` equals `"TC-APRELATED probe"`
7. Verify `part_1_detail` and `part_2_detail` nested objects are present
8. Clean up: `DELETE /api/part/related/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/related/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "part_1": 73,
    "part_2": 75,
    "note": "TC-APRELATED probe"
  }
  ```

**Expected Result:** `201 Created` with new `PartRelation`. Full `*_detail` objects embedded in response.

**Observed** (probed 2026-04-14):

- Status: `201`
- pk: `6`
- Response snippet:
  ```json
  {
    "pk": 6,
    "part_1": 73,
    "part_1_detail": { "pk": 73, "name": "Green Widget", "IPN": "widget.green", "active": true },
    "part_2": 75,
    "part_2_detail": { "pk": 75, "name": "Pink Widget", "IPN": "widget.pink", "active": true },
    "note": "TC-APRELATED probe"
  }
  ```
- Matches spec: Yes

**Notes:** None.
