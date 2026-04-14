# TC-APFLT-023: GET /api/part/?ordering=invalid_field silently ignores unrecognized ordering value

**Type:** API
**Priority:** P3
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least 3 parts exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?ordering=invalid_field&limit=3` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify response body is a JSON object with `count`, `next`, `previous`, and `results` fields
4. Verify `results` contains exactly `3` items
5. Verify no error message or validation field appears in the response body

**Request:**

- Method: `GET`
- URL: `/api/part/?ordering=invalid_field&limit=3`
- Headers: `Authorization: Token <token>`

**Expected Result:** An unrecognized `ordering` value is silently ignored. The API returns HTTP 200 with results in default database order. No validation error, no 400 status.

**Observed** (probed 2026-04-14):

- Status: `200`
- Response: paginated envelope, count `1452`, results_length `3`, first result name `"1551ABK"` (default order)
- No error field in response body
- Matches spec: Yes

**Notes:** The silent ignore behavior contrasts with strict validation frameworks. Automation tests should assert HTTP 200 and the presence of a valid `results` array, confirming no error body is returned when an invalid ordering field is supplied.
