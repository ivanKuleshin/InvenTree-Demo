# TC-APFLT-018: GET /api/part/?IPN=<value> performs exact match on Internal Part Number

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- A part with `IPN = "RES-001"` exists on the server (pk=1692 "Full Field Part")
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?IPN=RES-001&limit=5` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` equals `1`
4. Verify `results[0].IPN` equals `"RES-001"` (exact string match)
5. Verify `results[0].pk` equals `1692`
6. Send `GET /api/part/?IPN=RES-999&limit=5`
7. Verify response status code is `200`
8. Verify `count` equals `0`
9. Verify `results` is an empty array `[]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?IPN=RES-001&limit=5`
- Headers: `Authorization: Token <token>`

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?IPN=RES-999&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `IPN` filter performs an exact match. A matching IPN returns exactly that part. A non-existent IPN returns `count: 0` and empty `results`.

**Observed** (probed 2026-04-14):

- `IPN=RES-001`: status `200`, count `1`, result `pk=1692 name="Full Field Part" IPN="RES-001"`
- `IPN=RES-999`: status `200`, count `0`, results `[]`
- Matches spec: Yes

**Notes:** The docs state the IPN filter is case-sensitive. However, probing `IPN=res-001` (lowercase) also returned `pk=1692` with `IPN="RES-001"`, suggesting the demo database uses a case-insensitive collation for this field. Automation tests should use the canonical uppercase value `RES-001` and verify the exact `IPN` field in the response.
