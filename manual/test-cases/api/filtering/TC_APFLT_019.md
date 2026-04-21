# TC-APFLT-019: GET /api/part/?IPN_regex=^RES matches parts whose IPN starts with "RES"

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Multiple parts with IPN values starting with `"RES"` exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?IPN_regex=%5ERES&limit=10` with header `Authorization: Token <token>`
   (`%5E` is the URL-encoded caret `^`)
2. Verify response status code is `200`
3. Verify `count` is greater than `1`
4. Verify every part in `results` has `IPN` starting with `"RES"` (matches regex `^RES`)
5. Send `GET /api/part/?IPN_regex=%5EXYZ_NOMATCH_99999&limit=5`
6. Verify response status code is `200`
7. Verify `count` equals `0`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?IPN_regex=%5ERES&limit=10`
- Headers: `Authorization: Token <token>`

**Request (step 5):**

- Method: `GET`
- URL: `/api/part/?IPN_regex=%5EXYZ_NOMATCH_99999&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `IPN_regex` applies a regex match on the IPN field. `^RES` returns all parts whose IPN begins with "RES". A regex with no matches returns `count: 0`.

**Observed** (probed 2026-04-14):

- `IPN_regex=^RES` (`%5ERES`): status `200`, count `7`, sample results: `pk=1692 IPN="RES-001"`, `pk=2219 IPN="RES-10K-001"`, `pk=926 IPN="RES-1K-1776148821705"`, `pk=928 IPN="RES-1K-1776148858883"`, `pk=930 IPN="RES-1K-1776148929791"`
- All returned IPNs start with `"RES"` — regex anchor confirmed
- Matches spec: Yes

**Notes:** The `^` character must be URL-encoded as `%5E` in query strings. The regex engine used is database-level (PostgreSQL `~` operator by default on the demo). Complex regex patterns should be tested for compatibility.
