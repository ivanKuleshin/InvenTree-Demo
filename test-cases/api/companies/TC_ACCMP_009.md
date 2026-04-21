# TC-ACCMP-009: GET /api/company/ search and ordering query parameters

**Type:** API
**Priority:** P2
**Endpoint:** `GET /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- At least two companies exist with names sortable alphabetically

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `GET /api/company/?search=DigiKey&limit=10` with the authorization header
3. Verify response status code is `200`
4. Verify each item in `results` has a `name` or `description` containing `"DigiKey"` (case-insensitive)
5. Send `GET /api/company/?ordering=name&limit=10` with the authorization header
6. Verify response status code is `200`
7. Verify the `name` values in `results` are in ascending alphabetical order
8. Send `GET /api/company/?ordering=-name&limit=10` with the authorization header
9. Verify response status code is `200`
10. Verify the `name` values in `results` are in descending alphabetical order
11. Send `GET /api/company/?ordering=created&limit=5` with the authorization header
12. Verify response status code is `200`
13. Verify the `created` timestamps in `results` are in ascending chronological order
14. Send `GET /api/company/?ordering=-updated&limit=5` with the authorization header
15. Verify response status code is `200`
16. Verify the `updated` timestamps in `results` are in descending chronological order

**Request (step 2):**

- Method: `GET`
- URL: `/api/company/?search=DigiKey&limit=10`
- Headers: `Authorization: Basic <base64(admin:inventree)>`

**Expected Result:** The `search` parameter filters by `name` and `description` fields. The `ordering` parameter sorts by `name`, `created`, or `updated`; the `-` prefix reverses the sort direction. Allowed ordering values are: `name`, `-name`, `created`, `-created`, `updated`, `-updated`.

**Observed** (probed 2026-04-17):

- `search=DigiKey` returns results where name matches "DigiKey"
- `ordering=name` returns results in ascending alphabetical name order
- `ordering=-name` returns results in descending alphabetical name order
- `ordering=created` returns results in ascending creation timestamp order
- Matches spec: Yes

**Notes:** The `search` parameter performs a case-insensitive substring match across `name` and `description`. An unrecognized `ordering` value is silently ignored and the server falls back to its default ordering.
