# TC-ACCMP-004: POST /api/company/ creates a company with all optional fields populated

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/company/`

**Preconditions:**

- Valid HTTP Basic credentials with add permissions are available

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with the full body below
3. Verify response status code is `201`
4. Verify response body field `id` is a positive integer; record it as `COMPANY_ID`
5. Verify response body field `name` equals `"TC-ACCMP-004-FullCo"`
6. Verify response body field `description` equals `"Full field creation test"`
7. Verify response body field `website` equals `"https://example.com"`
8. Verify response body field `address` equals `"123 Test Street"`
9. Verify response body field `phone` equals `"+1-555-0199"`
10. Verify response body field `email` equals `"qa@example.com"`
11. Verify response body field `contact` equals `"QA Engineer"`
12. Verify response body field `currency` equals `"USD"`
13. Verify response body field `is_supplier` equals `true`
14. Verify response body field `is_manufacturer` equals `true`
15. Verify response body field `is_customer` equals `false`
16. Verify response body field `active` equals `true`
17. Verify response body field `notes` equals `"Created by TC-ACCMP-004"`
18. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
19. Verify DELETE response status code is `204`

**Request (step 2):**

- Method: `POST`
- URL: `/api/company/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCMP-004-FullCo",
    "description": "Full field creation test",
    "website": "https://example.com",
    "address": "123 Test Street",
    "phone": "+1-555-0199",
    "email": "qa@example.com",
    "contact": "QA Engineer",
    "currency": "USD",
    "is_supplier": true,
    "is_manufacturer": true,
    "is_customer": false,
    "active": true,
    "notes": "Created by TC-ACCMP-004"
  }
  ```

**Expected Result:** Server persists all provided optional fields and returns `201 Created` with the complete Company object reflecting every submitted value.

**Observed** (probed 2026-04-17):

- Status: `201`
- All submitted fields reflected back in the response without alteration
- `is_supplier` and `is_manufacturer` both `true`; `is_customer` `false`
- Matches spec: Yes

**Notes:** A company can simultaneously hold supplier, manufacturer, and customer roles. The `currency` field accepts ISO 4217 currency codes (e.g., `"USD"`, `"EUR"`, `"GBP"`). The free-text `address` field on the Company object is distinct from the structured addresses managed via `/api/company/address/`.
