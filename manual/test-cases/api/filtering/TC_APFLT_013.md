# TC-APFLT-013: GET /api/part/?assembly=true filters to assembly parts only

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with `assembly=true` and `assembly=false` both exist on the server
- Valid auth token available

**Steps:**

1. Send `GET /api/part/?assembly=true&limit=5` with header `Authorization: Token <token>`
2. Verify response status code is `200`
3. Verify `count` is greater than `0`
4. Verify every part in `results` has `assembly` equal to `true`
5. Record `count` as `ASSEMBLY_COUNT`
6. Send `GET /api/part/?assembly=false&limit=5`
7. Verify response status code is `200`
8. Verify every part in `results` has `assembly` equal to `false`
9. Record `count` as `NON_ASSEMBLY_COUNT`
10. Send `GET /api/part/?limit=1` and record `count` as `TOTAL_COUNT`
11. Verify `ASSEMBLY_COUNT + NON_ASSEMBLY_COUNT` equals `TOTAL_COUNT`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?assembly=true&limit=5`
- Headers: `Authorization: Token <token>`

**Expected Result:** `assembly=true` returns only parts where `assembly` is `true`. `assembly=false` returns the complement. The two counts sum to the total unfiltered count.

**Observed** (probed 2026-04-14):

- `assembly=true`: status `200`, count `136`, first 3 results: `pk=1934 "AllFlagsPart-TC004-..." assembly: true`, `pk=2046 "AllFlagsPart-TC004-..." assembly: true`, `pk=1921 "APIPart-01ad2779" assembly: true`
- All returned parts have `assembly: true`
- Matches spec: Yes

**Notes:** Assembly parts have a Bill of Materials (BOM). The `assembly` flag is independent of `active` status. The count will drift as new parts are created on the shared demo instance.
