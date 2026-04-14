# TC-APFLT-009: GET /api/part/ with offset beyond total count returns empty results array

**Type:** API
**Priority:** P3
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least one part exists on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?limit=5` and record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?limit=5&offset=99999` with header `Authorization: Token <token>`
3. Verify response status code is `200`
4. Verify response body is a JSON object (paginated envelope)
5. Verify `count` equals `TOTAL_COUNT`
6. Verify `results` is an empty array `[]`
7. Verify `next` is `null`
8. Verify `previous` is a non-null URI

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?limit=5&offset=99999`
- Headers: `Authorization: Token <token>`

**Expected Result:** Server returns HTTP 200 with a paginated envelope. `count` reflects the actual total number of parts. `results` is empty because the offset exceeds the dataset. `next` is `null` and `previous` points to a valid prior page.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response: `{ "count": 1442, "next": null, "previous": "https://demo.inventree.org/api/part/?format=json&limit=5&offset=99994", "results": [] }`
- `count`: `1442` (matches total from unfiltered query)
- `results`: `[]` (empty)
- `next`: `null`
- `previous`: non-null URI with `offset=99994`
- Matches spec: Yes

**Notes:** The server does not return a 404 or 400 for out-of-range offsets. This is standard REST pagination behavior. Automation assertions must check `results` length is `0` and `count` reflects actual total, not `0`.
