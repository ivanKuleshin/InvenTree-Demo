# TC-APRELATED-002: GET /api/part/related/{id}/ retrieves a single related part entry

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/related/{id}/`

**Preconditions:**

- Related part entry with pk `1` exists (Blue Paint ↔ Green Paint)
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/related/1/`
3. Verify response status code is `200`
4. Verify `pk` equals `1`
5. Verify `part_1` equals `89`, `part_2` equals `92`
6. Verify `note` is a string (empty or populated)

**Request:**

- Method: `GET`
- URL: `/api/part/related/1/`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with single `PartRelation` including full part detail for both parts.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response: `{"pk": 1, "part_1": 89, "part_2": 92, "note": ""}` (plus full `*_detail` objects)
- Matches spec: Yes

**Notes:** None.
