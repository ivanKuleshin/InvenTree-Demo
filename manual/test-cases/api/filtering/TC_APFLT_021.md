# TC-APFLT-021: GET /api/part/?created_after=&created_before= filters parts by creation date range

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts created between 2026-01-01 and 2026-04-14 exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?created_after=2026-01-01&created_before=2026-04-14&limit=10` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` is greater than `0`
4. Verify every part in `results` has `creation_date` that is on or after `"2026-01-01"`
5. Verify every part in `results` has `creation_date` that is on or before `"2026-04-14"`
6. Send `GET /api/part/?created_after=2030-01-01&limit=5`
7. Verify response status code is `200`
8. Verify `count` equals `0`
9. Verify `results` is an empty array `[]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?created_after=2026-01-01&created_before=2026-04-14&limit=10`
- Headers: `Authorization: Token <token>`

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?created_after=2030-01-01&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `created_after` and `created_before` filter parts by their `creation_date` field. Parts outside the date range are excluded. A future `created_after` date returns no results.

**Observed** (probed 2026-04-14):

- `created_after=2026-01-01&created_before=2026-04-14`: status `200`, count `7`, sample: `pk=914 "CRM license" creation_date="2026-03-30"`, `pk=915 "Encabulator" creation_date="2026-04-03"`, `pk=916 "Encabulator" creation_date="2026-04-03"`
- All returned `creation_date` values are within `[2026-01-01, 2026-04-14]`
- `created_after=2030-01-01`: status `200`, count `0`, results `[]`
- Matches spec: Yes

**Notes:** Both `created_after` and `created_before` are inclusive (they filter on the `creation_date` field which is a date-only value, no time component). The parameter names in the OpenAPI spec description say "Updated after/before" but actually filter by `creation_date`. Use ISO 8601 date format `YYYY-MM-DD`.
