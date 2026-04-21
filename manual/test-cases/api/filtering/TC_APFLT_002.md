# TC-APFLT-002: GET /api/part/?search= returns parts matching the search term

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/`

**Preconditions:**

- Parts with the word "resistor" in their name or description exist on the server
- No authentication required

**Steps:**

1. Send `GET /api/part/?search=resistor&limit=10`
2. Verify response status code is `200`
3. Verify response body field `count` is greater than `0`
4. Verify each result in the `results` array has at least one of the following fields containing the substring `"resistor"` (case-insensitive): `name`, `description`, `keywords`, `IPN`
5. Send `GET /api/part/?search=RESISTOR&limit=10` (uppercase)
6. Verify response status code is `200`
7. Verify response body field `count` equals the same value as step 3 (search is case-insensitive)
8. Send `GET /api/part/?search=xyznonexistentterm12345&limit=10`
9. Verify response status code is `200`
10. Verify response body field `count` equals `0`
11. Verify response body field `results` is an empty array `[]`

**Request (step 1):**

- Method: `GET`
- URL: `/api/part/?search=resistor&limit=10`
- Headers: _(none required)_

**Expected Result:** Search returns parts where any of the searched fields contain the term. Search is case-insensitive. A search with no matches returns `200` with `count: 0` and an empty `results` array.

**Observed** (probed 2026-04-14):

- Status: `200`
- `search=resistor`: count `55`, first result pk `43` name `"R_100K_0402_1%"` description `"100K resistor in 0402 SMD package"`
- `search=RESISTOR`: same count `55` — case-insensitive confirmed
- `search=xyznonexistentterm12345`: count `0`, results `[]`
- Matches spec: Yes

**Notes:** The `search` parameter performs a full-text search across `name`, `description`, `keywords`, `IPN`, `category__name`, `revision`, `supplier_parts__SKU`, `tags__name`, and `tags__slug`. A single `search` term is ANDed across all fields — results must contain the term in at least one field.
