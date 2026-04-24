# TC-ACCMP-005: PUT /api/company/{id}/ replaces all writable fields on an existing company

**Type:** API
**Priority:** P2
**Endpoint:** `PUT /api/company/{id}/`

**Preconditions:**

- Valid HTTP Basic credentials with change permissions are available
- Create a test company via `POST /api/company/` with `{"name": "TC-ACCMP-005-Setup"}` and record its `id` as `COMPANY_ID`

**Steps:**

1. Obtain a valid HTTP Basic authorization header using admin credentials
2. Send `POST /api/company/` with body `{"name": "TC-ACCMP-005-Setup"}` and record the response `id` as `COMPANY_ID`
3. Send `PUT /api/company/{COMPANY_ID}/` with the full replacement body below
4. Verify response status code is `200`
5. Verify response body field `id` equals `COMPANY_ID` (unchanged)
6. Verify response body field `name` equals `"TC-ACCMP-005-Updated"`
7. Verify response body field `description` equals `"Updated via PUT"`
8. Verify response body field `website` equals `"https://updated.example.com"`
9. Verify response body field `is_supplier` equals `true`
10. Verify response body field `is_manufacturer` equals `false`
11. Verify response body field `is_customer` equals `true`
12. Verify response body field `active` equals `true`
13. Verify response body field `created` is unchanged from the value recorded in step 2
14. Send `DELETE /api/company/{COMPANY_ID}/` to clean up
15. Verify DELETE response status code is `204`

**Request (step 3):**

- Method: `PUT`
- URL: `/api/company/{COMPANY_ID}/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-ACCMP-005-Updated",
    "description": "Updated via PUT",
    "website": "https://updated.example.com",
    "address": null,
    "phone": null,
    "email": null,
    "contact": null,
    "currency": null,
    "is_supplier": true,
    "is_manufacturer": false,
    "is_customer": true,
    "active": true,
    "notes": null
  }
  ```

**Expected Result:** Server replaces all writable fields with the provided values and returns `200 OK`. Read-only fields (`id`, `created`, `updated`) are returned but cannot be overwritten. Nullable fields submitted as `null` are cleared.

**Observed** (probed 2026-04-17):

- Status: `200`
- `id` unchanged
- `name`, `description`, `website` updated as submitted
- `is_supplier`: `true`; `is_customer`: `true`; `is_manufacturer`: `false`
- `created` timestamp unchanged
- Matches spec: Yes

**Notes:** PUT requires `name` to be present — omitting it returns `400 {"name": ["This field is required."]}`. Unlike parts, companies do not require deactivation before deletion, so cleanup can be done directly with DELETE.
