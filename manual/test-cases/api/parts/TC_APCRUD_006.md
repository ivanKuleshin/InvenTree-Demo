# TC-APCRUD-006: DELETE /api/part/{id}/ removes an inactive part

**Type:** API
**Priority:** P2
**Endpoint:** `DELETE /api/part/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with delete permissions are available
- A test part exists in inactive state; create one via `POST /api/part/` with `{"name": "TC-APCRUD-006-ToDelete"}`, then PATCH `{"active": false}` and record its `pk` as `PART_PK`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APCRUD-006-ToDelete"}` and record the response `pk` as `PART_PK`
3. Send `PATCH /api/part/{PART_PK}/` with body `{"active": false}`
4. Verify PATCH response status code is `200` and `active` equals `false`
5. Send `DELETE /api/part/{PART_PK}/`
6. Verify response status code is `204`
7. Verify response body is empty
8. Send `GET /api/part/{PART_PK}/` to confirm deletion
9. Verify response status code is `404`
10. Verify response body field `detail` equals `"No Part matches the given query."`

**Request (step 5):**

- Method: `DELETE`
- URL: `/api/part/{PART_PK}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** After deactivating the part, DELETE returns `204 No Content` with an empty body. A subsequent GET on the same PK returns `404` with a model-specific message.

**Observed** (probed 2026-04-14):

- PATCH status: `200`; `active: false`
- DELETE status: `204`; body: `""`
- Subsequent GET status: `404`
- GET body: `{"detail": "No Part matches the given query."}`
- Matches spec: Yes

**Notes:** Active parts cannot be deleted — `DELETE` on an active part returns `400 {"non_field_errors": ["Cannot delete this part as it is still active"]}`. The two-step deactivate-then-delete pattern is mandatory. The 404 message is `"No Part matches the given query."` not the generic `"Not found."` — automation assertions must use the exact model-specific string.
