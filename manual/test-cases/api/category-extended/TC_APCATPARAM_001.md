# TC-APCATPARAM-001: GET /api/part/category/parameters/ returns paginated category parameter list

**Type:** API
**Priority:** P1
**Endpoint:** `GET /api/part/category/parameters/`

**Preconditions:**

- At least one category parameter template assignment exists
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `GET /api/part/category/parameters/?limit=5`
3. Verify response status code is `200`
4. Verify `count` > `0`
5. Verify each item contains: `pk`, `category`, `category_detail`, `template`, `template_detail`, `default_value`
6. Verify `category_detail` includes `pathstring` (full hierarchical path)
7. Verify `template_detail` includes `name`, `units`, `description`

**Request:**

- Method: `GET`
- URL: `/api/part/category/parameters/?limit=5`
- Headers: `Authorization: Basic <base64>`

**Expected Result:** `200 OK` with paginated list. Each entry embeds full category and template detail objects.

**Observed** (probed 2026-04-14):

- Status: `200`
- count: `7`
- Response snippet:
  ```json
  {
    "pk": 1,
    "category": 5,
    "category_detail": {
      "pk": 5,
      "name": "Resistors",
      "pathstring": "Electronics/Passives/Resistors",
      "level": 2,
      "parent": 4
    },
    "template": 233,
    "template_detail": {
      "pk": 233,
      "name": "Resistance",
      "units": "ohms",
      "description": "Resistance value"
    },
    "default_value": ""
  }
  ```
- `default_value` is `""` (empty string) when not set — not `null`
- Matches spec: Yes

**Notes:** `default_value` is always a string (never `null`). `pathstring` in `category_detail` gives the full hierarchy path.
