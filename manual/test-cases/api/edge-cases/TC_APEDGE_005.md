# TC-APEDGE-005: POST /api/part/ with duplicate name+IPN+revision combination returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No part with `name="TC-APEDGE-005-DupePart"`, `IPN="IPN-DUPE-005"`, and `revision="A"` exists

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APEDGE-005-DupePart", "IPN": "IPN-DUPE-005", "revision": "A"}`
3. Verify response status code is `201` and record `pk` as `FIRST_PK`
4. Send a second `POST /api/part/` with the identical body `{"name": "TC-APEDGE-005-DupePart", "IPN": "IPN-DUPE-005", "revision": "A"}`
5. Verify response status code is `400`
6. Verify response body contains key `"non_field_errors"`
7. Verify the value of `"non_field_errors"` is an array containing the string `"The fields name, IPN, revision must make a unique set."`
8. Verify response body does NOT contain a `pk` field
9. Send `POST /api/part/` with body `{"name": "TC-APEDGE-005-DupePart", "IPN": "IPN-DUPE-005", "revision": "B"}` (different revision)
10. Verify response status code is `201` (different revision — no conflict)
11. Record the new `pk` as `SECOND_PK`
12. Send `PATCH /api/part/{FIRST_PK}/` with body `{"active": false}`, verify `200`
13. Send `DELETE /api/part/{FIRST_PK}/`, verify `204`
14. Send `PATCH /api/part/{SECOND_PK}/` with body `{"active": false}`, verify `200`
15. Send `DELETE /api/part/{SECOND_PK}/`, verify `204`

**Request (step 4):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APEDGE-005-DupePart", "IPN": "IPN-DUPE-005", "revision": "A" }
  ```

**Expected Result:** The second POST with an identical `(name, IPN, revision)` triple is rejected with `400`. Changing any one of the three fields (e.g., revision `"B"`) produces a unique combination and is accepted with `201`.

**Observed** (probed 2026-04-14):

- First POST status: `201`; `pk`: `1663`
- Second POST (identical) status: `400`
- Response body:
  ```json
  {
    "non_field_errors": [
      "The fields name, IPN, revision must make a unique set."
    ]
  }
  ```
- Third POST (`revision: "B"`) status: `201` — different revision allowed
- Matches spec: Yes

**Notes:** The uniqueness constraint covers the triple `(name, IPN, revision)` — not just `name`. Two parts can share the same name as long as their IPN or revision differs. Empty string `""` counts as a value in the uniqueness check — two parts with `name="X"`, `IPN=""`, `revision=""` conflict with each other.
