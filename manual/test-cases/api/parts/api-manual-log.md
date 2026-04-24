# API Manual Exploration Log — Part Creation

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/?limit=5

- Status: `200`
- Response shape: `{ count: integer, results: [{ pk, name, IPN, revision, description, category, active, ... }] }`
- Confirmed auth works; total part count observed in the hundreds

### 2. GET /api/part/category/?limit=5

- Status: `200`
- Collected valid category PKs for use in subsequent payloads; PK `1` used in TC-AP-PC-002

### 3. POST /api/part/ — minimal payload (name only)

- Request: `{"name": "TC-AP-PC-001-MinimalPart"}`
- Status: `201`; pk: `1454`
- Defaults confirmed: `active=true`, `component=true`, `purchasable=true`, other booleans `false`, relational fields `null`
- Second identical POST → `400` `{"non_field_errors": ["The fields name, IPN, revision must make a unique set."]}`

### 4. POST /api/part/ — full optional fields payload

- Request: all optional fields including IPN, revision, description, category, keywords, assembly
- Status: `201`; pk: `1490`
- `full_name` computed as `"IPN-TC-AP-002 | TC-AP-PC-002-FullPart-v2 | B"`

### 5. POST /api/part/ — duplicate write-only field

- Source part PK retrieved from GET; `duplicate.part` set to that PK
- Status: `201`; pk: `1494`
- `duplicate` key absent from response (write-only confirmed)
- `category` was `null` — category NOT inherited from source part

### 6. POST /api/part/ — initial_stock write-only field

- Location PK retrieved from `GET /api/stock/location/?limit=5`
- Request: `{"name": "...", "initial_stock": {"quantity": 50, "location": 1}}`
- Part status: `201`; pk: `1498`; `initial_stock` absent from response
- `GET /api/stock/?part=1498` → `{"count": 1, "results": [{"pk": 2459, "quantity": 50.0, "location": 1, "status": 10}]}`

### 7. POST /api/part/ — initial_supplier write-only field

- Supplier PK retrieved from `GET /api/company/?is_supplier=true&limit=5`; PK `1` (DigiKey)
- Request: `{"name": "...", "purchasable": true, "initial_supplier": {"supplier": 1, "SKU": "SKU-TC-AP-005"}}`
- Part status: `201`; pk: `1502`; `initial_supplier` absent from response
- `GET /api/company/part/?part=1502` → `{"count": 1, "results": [{"pk": 302, "supplier": 1, "SKU": "SKU-TC-AP-005"}]}`

### 8. POST /api/part/ — missing required field

- Request: `{}`
- Status: `400`
- Body: `{"name": ["This field is required."]}`

### 9. POST /api/part/ — invalid category FK

- Request: `{"name": "...", "category": 99999999}`
- Status: `400`
- Body: `{"category": ["Invalid pk \"99999999\" - object does not exist."]}`

### 10. POST /api/part/ — no auth

- Request: no Authorization header
- Status: `401`
- Body: `{"detail": "Authentication credentials were not provided."}`

### 11. POST /api/part/ — bad credentials

- Request: `Authorization: Basic aW52YWxpZDppbnZhbGlk` (invalid:invalid)
- Status: `401`
- Body: `{"detail": "Invalid username/password."}`

### 12. GET /api/importer/session/

- Status: `200`; response: `{"count": N, "results": [...]}`
- `/api/importer/` root → `404` (only `/api/importer/session/` accessible)

### 13. POST /api/importer/session/ — CSV upload

- Content-Type: `multipart/form-data`; fields: `model_type=part`, `data_file=<CSV>`
- CSV content: `name,description,IPN\nImportTestPart001,Imported via API test,IPN-IMPORT-001`
- Status: `201`; pk: `1`
- Auto-mapped: `name→name`, `description→description`, `IPN→IPN`
- `available_fields` array contains all writable Part fields

---

## Divergences from Documentation

| Doc claim                            | Observed                                     | Notes                                                      |
| ------------------------------------ | -------------------------------------------- | ---------------------------------------------------------- |
| `component` default: `false`         | API returns `true`                           | Differs from schema default; server seed data may override |
| `duplicate` write-only field         | Confirmed — not returned in response         | Matches                                                    |
| `initial_stock` write-only field     | Confirmed — stock item created atomically    | Matches                                                    |
| `initial_supplier` write-only field  | Confirmed — supplier part created atomically | Matches                                                    |
| Category inheritance via `duplicate` | NOT inherited — `category: null`             | Behavioral finding; not documented explicitly              |
| `/api/importer/` root                | Returns `404`                                | Only `/api/importer/session/` is accessible                |

---

✓ TC_AP_PART_CREATE.md — 9 TCs written — observed 2026-04-14 — source: demo.inventree.org
