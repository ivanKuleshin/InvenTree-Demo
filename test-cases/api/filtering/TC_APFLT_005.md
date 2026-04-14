# TC-APFLT-005: GET /api/part/?ordering= sorts results ascending and descending by name

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- At least 3 parts exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/?ordering=name&limit=3`
2. Verify response status code is `200`
3. Record the `name` values of all 3 results as `NAMES_ASC`
4. Verify `NAMES_ASC[0]` is lexicographically less than or equal to `NAMES_ASC[1]`
5. Verify `NAMES_ASC[1]` is lexicographically less than or equal to `NAMES_ASC[2]`
6. Send `GET /api/part/?ordering=-name&limit=3`
7. Verify response status code is `200`
8. Record the `name` values as `NAMES_DESC`
9. Verify `NAMES_DESC[0]` is lexicographically greater than or equal to `NAMES_DESC[1]`
10. Verify `NAMES_DESC[1]` is lexicographically greater than or equal to `NAMES_DESC[2]`
11. Verify `NAMES_ASC[0]` does not equal `NAMES_DESC[0]` (ascending and descending start at opposite ends)

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?ordering=name&limit=3`
- Headers: _(none required)_

**Request (step 6):**

- Method: `GET`
- URL: `/api/part/?ordering=-name&limit=3`
- Headers: _(none required)_

**Expected Result:** `ordering=name` returns results sorted A→Z by name. `ordering=-name` returns results sorted Z→A. The prefix `-` reverses the sort direction for any supported ordering field.

**Observed** (probed 2026-04-14):

- `ordering=name`: status `200`, names `["1551ABK", "1551ACLR", "1551AGY"]` — ascending confirmed
- `ordering=-name`: status `200`, names `["Zero Qty Comp", "Zero Qty Comp", "Zero Qty Assembly"]` — descending confirmed
- First name ascending (`"1551ABK"`) ≠ first name descending (`"Zero Qty Comp"`)
- Matches spec: Yes

**Notes:** String ordering is case-sensitive and locale-dependent. On this instance, numeric-prefixed names (`"1551..."`) sort before alphabetic names, and uppercase letters sort before lowercase in the default PostgreSQL collation. Assertions should use relative ordering comparisons (a ≤ b ≤ c), not hardcoded name values.
