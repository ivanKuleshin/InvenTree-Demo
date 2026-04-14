# TC-APCATPARAM-003: POST /api/part/category/parameters/ creates a category parameter assignment

**Type:** API
**Priority:** P1
**Endpoint:** `POST /api/part/category/parameters/`

**Preconditions:**

- Category pk `6` (Capacitors) and parameter template pk `233` (Resistance) both exist
- No existing assignment of template `233` to category `6`
- Valid admin credentials available

**Steps:**

1. Obtain authorization credentials
2. Send `POST /api/part/category/parameters/` with body below
3. Verify response status code is `201`
4. Verify `pk` is a new integer
5. Verify `category` equals `6`, `template` equals `233`, `default_value` equals `"100"`
6. Verify `category_detail` and `template_detail` embedded in response
7. Clean up: `DELETE /api/part/category/parameters/{new_pk}/`

**Request:**

- Method: `POST`
- URL: `/api/part/category/parameters/`
- Headers: `Authorization: Basic <base64>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "category": 6,
    "template": 233,
    "default_value": "100"
  }
  ```

**Expected Result:** `201 Created` with new assignment. No validation that the template type is compatible with the category.

**Observed** (probed 2026-04-14):

- Status: `201`
- pk: `8`
- Response snippet:
  ```json
  {
    "pk": 8,
    "category": 6,
    "category_detail": { "pk": 6, "name": "Capacitors", "pathstring": "Electronics/Passives/Capacitors" },
    "template": 233,
    "template_detail": { "pk": 233, "name": "Resistance", "units": "ohms" },
    "default_value": "100"
  }
  ```
- Matches spec: Yes

**Notes:** The API does not enforce template-category type compatibility — a Resistance template can be assigned to a Capacitors category without error. `default_value` is always stored as a string.
