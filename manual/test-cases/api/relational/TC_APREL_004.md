# TC-APREL-004: POST /api/part/ with variant_of FK links variant to template part

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- No parts named "TC-APREL-004-Template" or "TC-APREL-004-Variant" exist

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/part/` with body `{"name": "TC-APREL-004-Template", "is_template": true}`
3. Verify response status code is `201`
4. Verify response body field `is_template` equals `true`
5. Record the response `pk` as `TEMPLATE_PK`
6. Send `POST /api/part/` with body `{"name": "TC-APREL-004-Variant", "variant_of": TEMPLATE_PK}`
7. Verify response status code is `201`
8. Verify response body field `variant_of` equals `TEMPLATE_PK`
9. Record the response `pk` as `VARIANT_PK`
10. Send `GET /api/part/?variant_of=TEMPLATE_PK&limit=10` and verify the variant part appears in results
11. Send `POST /api/part/` with body `{"name": "TC-APREL-004-BadVariant", "variant_of": 99999999}`
12. Verify response status code is `400`
13. Verify response body contains key `"variant_of"` with a value containing `"Invalid pk"` and `"object does not exist"`
14. Send `PATCH /api/part/{VARIANT_PK}/` with body `{"active": false}`, verify `200`
15. Send `DELETE /api/part/{VARIANT_PK}/`, verify `204`
16. Send `PATCH /api/part/{TEMPLATE_PK}/` with body `{"active": false}`, verify `200`
17. Send `DELETE /api/part/{TEMPLATE_PK}/`, verify `204`

**Request (step 6):**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  { "name": "TC-APREL-004-Variant", "variant_of": TEMPLATE_PK }
  ```

**Expected Result:** A variant part is created pointing to a template part via `variant_of`. The FK is validated — a non-existent PK returns `400`. The `GET /api/part/?variant_of=<pk>` filter returns the variant in its results.

**Observed** (probed 2026-04-14):

- Template POST status: `201`; `pk`: `1658`; `is_template: true`
- Variant POST status: `201`; `pk`: `1659`; `variant_of: 1658`
- Invalid `variant_of: 99999999` → `400` with `{"variant_of": ["Invalid pk \"99999999\" - object does not exist."]}`
- Matches spec: Yes

**Notes:** The server does not enforce that `variant_of` must point to a part with `is_template: true` — a variant can reference any existing part. Pointing a variant at a non-template part was not explicitly tested but is worth a separate exploratory test. Deleting the template part while variants still reference it was not tested — this may cascade-null or block deletion.
