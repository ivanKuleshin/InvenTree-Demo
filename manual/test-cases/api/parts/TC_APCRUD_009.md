# TC-APCRUD-009: POST /api/part/ with initial_supplier write-only field

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- A valid supplier company PK exists (obtain via `GET /api/company/?is_supplier=true&limit=5`, record as `SUPPLIER_PK`)
- The name "TC-APCRUD-009-InitialSupplierPart" does not already exist

**Steps:**

1. Send `GET /api/company/?is_supplier=true&limit=5` to retrieve a valid supplier PK (record as `SUPPLIER_PK`)
2. Send POST to `/api/part/` with body containing `name`, `purchasable=true`, and `initial_supplier` object with `supplier=SUPPLIER_PK` and a SKU
3. Verify response status code is `201`; record new part PK as `NEW_PART_PK`
4. Verify response body does NOT contain the key `initial_supplier` (write-only)
5. Send `GET /api/company/part/?part=NEW_PART_PK` to retrieve supplier parts for the new part
6. Verify the list contains exactly one supplier part
7. Verify the supplier part has `supplier` equal to `SUPPLIER_PK`
8. Verify the supplier part has `SKU` equal to `"SKU-TC-APCRUD-009"`
9. DELETE the created part and supplier part to clean up test data

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-APCRUD-009-InitialSupplierPart",
    "purchasable": true,
    "initial_supplier": { "supplier": SUPPLIER_PK, "SKU": "SKU-TC-APCRUD-009" }
  }
  ```

**Expected Result:** Server creates the part and simultaneously creates one supplier part record linking the part to the specified supplier with the given SKU. `initial_supplier` is write-only and not returned in the part response.

**Observed** (probed 2026-04-14):

- Status: `201`; new part PK: `1502`
- Supplier used: PK `1` (DigiKey)
- Part response: key `initial_supplier` absent from body
- `GET /api/company/part/?part=1502` response snippet:
  ```json
  {
    "count": 1,
    "results": [
      {
        "pk": 302,
        "part": 1502,
        "supplier": 1,
        "SKU": "SKU-TC-APCRUD-009"
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The `purchasable` flag must be `true` for a supplier part to be creatable. If `purchasable=false`, the `initial_supplier` sub-object is likely ignored or returns a validation error.
