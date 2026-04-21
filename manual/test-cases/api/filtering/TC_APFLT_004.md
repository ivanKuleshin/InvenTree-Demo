# TC-APFLT-004: GET /api/part/?active= filters parts by active status

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Both active and inactive parts exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/?limit=5` (no active filter) and record `count` as `TOTAL_COUNT`
2. Send `GET /api/part/?active=true&limit=5`
3. Verify response status code is `200`
4. Verify response body field `count` is less than `TOTAL_COUNT` (some parts are inactive)
5. Verify every part in `results` has `active` equal to `true`
6. Record count as `ACTIVE_COUNT`
7. Send `GET /api/part/?active=false&limit=5`
8. Verify response status code is `200`
9. Verify every part in `results` has `active` equal to `false`
10. Record count as `INACTIVE_COUNT`
11. Verify `ACTIVE_COUNT + INACTIVE_COUNT` equals `TOTAL_COUNT`

**Request (step 2):**

- Method: `GET`
- URL: `/api/part/?active=true&limit=5`
- Headers: _(none required)_

**Expected Result:** `active=true` returns only active parts; `active=false` returns only inactive parts. The two counts sum to the total unfiltered count.

**Observed** (probed 2026-04-14):

- Total count (no filter): `907`
- `active=true` count: `883`; all returned parts have `active: true`
- `active=false` count: `24`; all returned parts have `active: false`
- `883 + 24 = 907` — counts sum correctly
- Matches spec: Yes

**Notes:** The demo has `24` inactive parts at time of probing. These numbers will drift as other users create or deactivate parts. Assertions should verify the `active` field value on each result rather than asserting specific count values.
