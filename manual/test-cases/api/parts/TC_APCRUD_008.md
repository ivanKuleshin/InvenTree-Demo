# TC-APCRUD-008: POST /api/part/ with initial_stock write-only field

**Type:** API
**Priority:** P2
**Endpoint:** `POST /api/part/`

**Preconditions:**

- Valid HTTP Basic credentials are available
- A valid stock location PK exists (obtain via `GET /api/stock/location/?limit=5`, record as `LOCATION_PK`)
- The name "TC-APCRUD-008-InitialStockPart" does not already exist

**Steps:**

1. Send `GET /api/stock/location/?limit=5` to retrieve a valid location PK (record as `LOCATION_PK`)
2. Send POST to `/api/part/` with body containing `name` and `initial_stock` object with `quantity=50` and `location=LOCATION_PK`
3. Verify response status code is `201`; record new part PK as `NEW_PART_PK`
4. Verify response body does NOT contain the key `initial_stock` (write-only)
5. Send `GET /api/stock/?part=NEW_PART_PK` to retrieve stock items for the new part
6. Verify the stock item list contains exactly one item
7. Verify the stock item has `quantity` equal to `50.0`
8. Verify the stock item has `location` equal to `LOCATION_PK`
9. Verify the stock item has `status` equal to `10` (OK status code)
10. DELETE the created part and stock item to clean up test data

**Request:**

- Method: `POST`
- URL: `/api/part/`
- Headers: `Authorization: Basic <base64(admin:inventree)>`, `Content-Type: application/json`
- Body:
  ```json
  {
    "name": "TC-APCRUD-008-InitialStockPart",
    "initial_stock": { "quantity": 50, "location": LOCATION_PK }
  }
  ```

**Expected Result:** Server creates the part and simultaneously creates one stock item with the specified quantity at the specified location. `initial_stock` is write-only and not returned in the part response.

**Observed** (probed 2026-04-14):

- Status: `201`; new part PK: `1498`
- Part response: key `initial_stock` absent from body
- `GET /api/stock/?part=1498` response snippet:
  ```json
  {
    "count": 1,
    "results": [
      {
        "pk": 2459,
        "quantity": 50.0,
        "location": 1,
        "status": 10
      }
    ]
  }
  ```
- Matches spec: Yes

**Notes:** The stock item is created atomically with the part in a single POST. Status code `10` corresponds to the "OK" stock status in InvenTree's internal enum. Verify `GET /api/stock/?part=<pk>` immediately after creation.
