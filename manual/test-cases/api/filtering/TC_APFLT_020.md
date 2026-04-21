# TC-APFLT-020: GET /api/part/?name_regex=^R filters parts whose name starts with "R"

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Multiple parts with names starting with `"R"` exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?name_regex=%5ER&limit=5` with header `Authorization: Token <token>`
   (`%5E` is the URL-encoded caret `^`)
2. Verify response status code is `200`
3. Verify `count` is greater than `1`
4. Verify every part in `results` has `name` starting with `"R"` (matches regex `^R`)

**Request:**

- Method: `GET`
- URL: `/api/part/?name_regex=%5ER&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `name_regex=^R` returns only parts whose `name` field matches the provided regular expression. Every returned part name begins with `"R"`.

**Observed** (probed 2026-04-14):

- Status: `200`
- Count: `93`
- First 5 results: `pk=43 "R_100K_0402_1%"`, `pk=44 "R_100K_0603_1%"`, `pk=45 "R_100K_0805_1%"`, `pk=4 "R_100R_0402_1%"`, `pk=5 "R_100R_0603_1%"`
- All names start with `"R"` — regex anchor confirmed
- Matches spec: Yes

**Notes:** `name_regex` applies a regex match on the `name` field. The `^` anchor restricts matches to names that begin with the given pattern. URL-encode `^` as `%5E` in query strings.
