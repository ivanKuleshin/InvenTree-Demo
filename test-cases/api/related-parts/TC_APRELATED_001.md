# TC-APRELATED-001: GET /api/part/related/ returns paginated related part list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/related/`

**Preconditions:**

- At least one related part pair exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/related/?limit=5`
3. Verify response status code is `200`
4. Verify `count` > `0`
5. Verify each item contains: `pk`, `part_1`, `part_1_detail`, `part_2`, `part_2_detail`, `note`
6. Verify `part_1_detail` and `part_2_detail` are nested objects with at least `pk`, `name`, `active`, `category` fields

**Request:**

- Method: `GET`
- URL: `/api/part/related/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated related parts. Each entry embeds full part detail objects for both parts.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `5`
- Response snippet:
  ```json
  {
    "pk": 1,
    "part_1": 89,
    "part_1_detail": { "pk": 89, "name": "Blue Paint", "active": true, "category": 20, "category_name": "Paint" },
    "part_2": 92,
    "part_2_detail": { "pk": 92, "name": "Green Paint", "active": true },
    "note": ""
  }
  ```
- `part_1_detail` and `part_2_detail` contain ~40 fields each including many nullable aggregates (`in_stock`, `ordering`, etc.)
- Matches spec: Yes (field names correct, detail objects more verbose than schema suggests)

**Notes:** The `*_detail` objects include all Part fields, many of which are `null` in the list context (e.g., `in_stock`, `ordering`, `allocated_to_build_orders`). Automation should assert key identity fields and not assert `null` on aggregate fields.
