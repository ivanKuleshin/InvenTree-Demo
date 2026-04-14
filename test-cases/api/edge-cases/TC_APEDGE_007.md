# TC-APEDGE-007: DELETE /api/part/{id}/ on an active part returns 400

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- An active part exists with known PK (create via POST or obtain from existing parts)
- The part has `active: true`

**Steps:**

1. Send POST to `/api/part/` to create a new part with `name: "TC-APEDGE-007-ActivePart"` and `active: true`
2. Verify response status code is `201`; record the PK as `ACTIVE_PART_PK`
3. Verify response body field `active` equals `true`
4. Send DELETE to `/api/part/{ACTIVE_PART_PK}/`
5. Verify response status code is `400`
6. Verify response body contains an error message indicating that active parts cannot be deleted
7. Verify the part still exists by sending `GET /api/part/{ACTIVE_PART_PK}/`
8. Verify GET response status code is `200`
9. Set the part to inactive via `PATCH /api/part/{ACTIVE_PART_PK}/` with body `{"active": false}`
10. Retry DELETE to `/api/part/{ACTIVE_PART_PK}/`
11. Verify DELETE now succeeds with status `204`

**Request (step 4):**

- Method: `DELETE`
- URL: `/api/part/{ACTIVE_PART_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** Server rejects DELETE request on active part with `400 Bad Request`. After deactivating the part, DELETE succeeds with `204 No Content`.

**Observed** (to be probed):

- DELETE active part status: TBD
- Error message: TBD
- DELETE inactive part status: TBD
- Matches spec: TBD

**Notes:** This test validates that InvenTree enforces business rules preventing deletion of active parts. Parts must be deactivated before deletion to prevent accidental data loss.
