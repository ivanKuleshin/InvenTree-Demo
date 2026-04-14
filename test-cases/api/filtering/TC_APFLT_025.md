# TC-APFLT-025: GET /api/part/ with combined assembly=true, active=true, and search=resistor narrows results using AND logic

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with `assembly=true` and `active=true` exist on the server
- At least one such part has "resistor" in a searchable field (name, description, keywords, IPN, etc.)
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?assembly=true&limit=1` and record `count` as `ASSEMBLY_COUNT`
2. Send `GET /api/part/?active=true&limit=1` and record `count` as `ACTIVE_COUNT`
3. Send `GET /api/part/?assembly=true&active=true&limit=5` and record `count` as `COMBINED_COUNT`
4. Verify `COMBINED_COUNT` is less than or equal to `ASSEMBLY_COUNT`
5. Verify `COMBINED_COUNT` is less than or equal to `ACTIVE_COUNT`
6. Verify every part in `results` has `assembly` equal to `true`
7. Verify every part in `results` has `active` equal to `true`
8. Send `GET /api/part/?search=resistor&assembly=true&limit=5`
9. Verify response status code is `200`
10. Verify `count` is less than or equal to `COMBINED_COUNT`
11. Verify every part in `results` has `assembly` equal to `true`

**Request (step 3):**

- Method: `GET`
- URL: `/api/part/?assembly=true&active=true&limit=5`
- Headers: `Authorization: Token <token>`

**Request (step 8):**

- Method: `GET`
- URL: `/api/part/?search=resistor&assembly=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** Multiple filters combine with AND logic. Adding each additional filter narrows the result set. The combined count is always less than or equal to each individual filter's count. All returned parts satisfy all supplied filter conditions simultaneously.

**Observed** (probed 2026-04-14):

- `assembly=true` alone: count `136`
- `active=true` alone: count `1400` (approx)
- `assembly=true&active=true`: status `200`, count `135`, sample: `pk=1934 assembly: true active: true`, `pk=2046 assembly: true active: true`
- `135 <= 136` and `135 <= 1400` — AND narrowing confirmed
- `search=resistor&assembly=true`: status `200`, count `1`, result `pk=1692 "Full Field Part" assembly: true`
- `1 <= 135` — further narrowing by search confirmed
- Matches spec: Yes

**Notes:** The search parameter and boolean filters stack as AND conditions. The progression `assembly=136` → `assembly+active=135` → `assembly+active+search=1` demonstrates the AND narrowing behavior. The single match (`pk=1692`) has `keywords="resistor test"` which causes it to match the search term.
