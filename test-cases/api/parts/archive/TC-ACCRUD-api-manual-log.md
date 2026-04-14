# API Manual Exploration Log — Categories & Parts Full CRUD

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree (Basic)

---

## Probe Sequence

### 1. GET /api/part/category/?limit=10

- Status: `200`
- count: `31`
- Shape: `{ pk, name, description, default_location, default_keywords, level, parent, part_count, subcategories, pathstring, starred, structural, icon, parent_default_location }`
- PK `1` = "Electronics" (top-level, parent=null, level=0, 144 parts, 12 subcategories)
- PK `25` = "Category 2" (level=1, parent=24, pathstring="Category 1/Category 2")

### 2. GET /api/part/category/1/?format=json

- Status: `200`
- All fields present; no `path` field without `?path_detail=true`

### 3. GET /api/part/category/1/?format=json&path_detail=true

- Status: `200`
- `path` field added: `[{"pk":1,"name":"Electronics"}]`

### 4. GET /api/part/?limit=5

- Status: `200` (NO AUTH — public read access confirmed)
- count: `904–917` (fluctuates as demo data changes)
- Full part object returned including all read-only fields (in_stock, pricing_min, etc.)
- First result: pk=82, name="1551ABK", category=null, active=true

### 5. GET /api/part/82/?category_detail=true&parameters=true&tags=true

- Status: `200`
- `category_detail`: null (part has no category)
- `parameters`: array of 15 parameter objects (template_detail nested inline)
- `tags`: empty array `[]`

### 6. GET /api/part/?limit=3&offset=3

- Status: `200`
- `previous` URL present (not null); `next` URL present
- pks returned: `[86, 85, 69]` (different from first page pks `[82, 84, 83, 86, 85]`)

### 7. GET /api/part/?search=resistor&limit=5

- Status: `200`
- count: `55`
- First result: pk=43, name="R_100K_0402_1%", description="100K resistor in 0402 SMD package"
- Search matches against name, description, keywords, IPN, category name, tags

### 8. GET /api/part/?ordering=name&limit=3

- Status: `200`
- names_asc: `["1551ABK", "1551ACLR", "1551AGY"]` — alphabetical ascending confirmed

### 9. GET /api/part/?ordering=-name&limit=3

- Status: `200`
- names_desc: `["Zero Qty Comp", "Zero Qty Comp", "Zero Qty Assembly"]` — descending confirmed

### 10. GET /api/part/?active=true&limit=3

- Status: `200`
- count: `883` (vs total ~907 — inactive parts excluded)
- All returned parts have `active: true`

### 11. GET /api/part/category/?search=Electronics&limit=5

- Status: `200`
- count: `13` (Electronics top-level + all sub-categories matching "Electronics" path)
- Returned names: `["Electronics", "Connectors", "Pin Headers", "IC", "Interface"]`
- Search matches against `name`, `description`, `pathstring`

### 12. GET /api/part/category/?parent=1&limit=10

- Status: `200`
- count: `6` direct children of Electronics (pk=1)
- All returned items have `parent: 1`

### 13. POST /api/part/ — no auth header

- Status: `403`
- Body: `{"detail": "CSRF Failed: Referer checking failed - no Referer."}`
- **Divergence from docs**: doc says `401`; actual response is `403` (CSRF enforcement on unauthenticated session-based requests)
- GET /api/part/ without auth returns `200` (public read is allowed)

### 14. POST /api/part/ — reader:readonly credentials

- Status: `403`
- Body: `{"detail": "You do not have permission to perform this action."}`

### 15. GET /api/part/99999999/

- Status: `404`
- Body: `{"detail": "No Part matches the given query."}`
- **Note**: message is "No Part matches the given query." not "Not found." (DRF custom message)

### 16. GET /api/part/category/99999999/

- Status: `404`
- Body: `{"detail": "No PartCategory matches the given query."}`

### 17. POST /api/part/category/ — missing name → 400

- Status: `400`
- Body: `{"name": ["This field is required."]}`

### 18. POST /api/part/category/ — name 101 chars → 400

- Status: `400`
- Body: `{"name": ["Ensure this field has no more than 100 characters."]}`

### 19. POST /api/part/ — name 101 chars → 400

- Status: `400`
- Body: `{"name": ["Ensure this field has no more than 100 characters."]}`

### 20. POST /api/part/ — IPN 101 chars → 400

- Status: `400`
- Body: `{"IPN": ["Ensure this field has no more than 100 characters."]}`

### 21. POST /api/part/ — category as string "not-a-number" → 400

- Status: `400`
- Body: `{"category": ["Incorrect type. Expected pk value, received str."]}`

### 22. POST /api/part/category/ — invalid default_location 99999999 → 400

- Status: `400`
- Body: `{"default_location": ["Invalid pk \"99999999\" - object does not exist."]}`

### 23. POST /api/part/ — invalid default_location 99999999 → 400

- Status: `400`
- Body: `{"default_location": ["Invalid pk \"99999999\" - object does not exist."]}`

### 24. GET /api/stock/location/?limit=5

- Status: `200`; count: `24`
- Valid location PKs: `pk=1` ("Factory"), `pk=8` ("Reel Storage"), `pk=10` ("Parts Bins")

### 25. POST /api/part/category/ — with valid default_location=1 → 201

- Status: `201`; pk: `284`
- `default_location: 1` confirmed in response

### 26. POST /api/part/ — with valid default_location=1 → 201

- Status: `201`; pk: `1656`
- `default_location: 1` confirmed in response

### 27. POST /api/part/ — is_template=true (template part) → 201

- Status: `201`; pk: `1658`; `is_template: true`

### 28. POST /api/part/ — variant_of=1658 → 201

- Status: `201`; pk: `1659`; `variant_of: 1658` confirmed in response

### 29. POST /api/part/category/ — duplicate name under same parent → 400

- First POST status: `201`; pk: `285`
- Second POST status: `400`
- Body: `{"non_field_errors": ["Duplicate names cannot exist under the same parent"]}`
- **Divergence from docs**: doc shows error `"The fields name, parent must make a unique set."` but actual message is `"Duplicate names cannot exist under the same parent"`

### 30. POST /api/part/ — duplicate name+IPN+revision → 400

- First POST status: `201`; pk: `1663`
- Second POST status: `400`
- Body: `{"non_field_errors": ["The fields name, IPN, revision must make a unique set."]}`
- Matches existing observation from TC-AP-PC-001

### 31. POST /api/part/category/ — minimal payload → 201

- Status: `201`; pk: `282`
- `path` field present in POST response (always returned from create/update)
- `part_count: null`, `subcategories: null` on fresh create (not yet computed)

### 32. POST /api/part/category/ — with parent=282 → 201

- Status: `201`; pk: `283`
- `level: 1`, `pathstring: "TC-ACCRUD-003-MinimalCat/TC-ACCRUD-004-ChildCat"` auto-computed
- `path: [{"pk":282,...},{"pk":283,...}]`

### 33. PUT /api/part/category/283/ → 200

- Status: `200`
- Name updated to "TC-ACCRUD-005-UpdatedCat"
- `pathstring` recomputed: "TC-ACCRUD-003-MinimalCat/TC-ACCRUD-005-UpdatedCat"
- `part_count: 0` (populated in PUT response, unlike fresh create)

### 34. PATCH /api/part/category/283/ — description only → 200

- Status: `200`
- `name` unchanged; `description` updated; all other fields unchanged

### 35. DELETE /api/part/category/283/ → 204

- Status: `204`; empty body

### 36. POST /api/part/ — full part for CRUD testing → 201

- Status: `201`; pk: `1653`
- Full part object returned including `category_detail`, `path`, `parameters`, `tags`, `price_breaks`, `default_location_detail`
- POST response includes more expanded fields than GET response by default

### 37. PUT /api/part/1653/ → 200

- Status: `200`; `description: "PUT full update applied"`, `active: true`

### 38. PATCH /api/part/1653/ → 200

- Status: `200`; `active: false`, `keywords: "patch-test qa"`, `name` unchanged

### 39. DELETE /api/part/1653/ → 204

- Status: `204`; empty body

### 40. DELETE /api/part/99999999/ → 404

- Status: `404`
- Body: `{"detail": "No Part matches the given query."}`

### 41. DELETE active part (cleanup attempt) → 400

- Status: `400`
- Body: `{"non_field_errors": ["Cannot delete this part as it is still active"]}`
- Parts must be PATCHED to `active: false` before DELETE

---

## Divergences from Documentation

| Doc claim                                                                | Observed                                               | Notes                                                                                                    |
| ------------------------------------------------------------------------ | ------------------------------------------------------ | -------------------------------------------------------------------------------------------------------- |
| Unauthenticated POST → `401`                                             | Returns `403` with CSRF error                          | Demo uses session-based CSRF; Basic auth headers bypass CSRF; unauthenticated POST hits CSRF check first |
| GET /api/part/ requires auth                                             | Returns `200` without auth                             | Public read access enabled on demo instance                                                              |
| 404 body: `{"detail": "Not found."}`                                     | `{"detail": "No Part matches the given query."}`       | DRF uses model-specific messages                                                                         |
| Category dupe error: `"The fields name, parent must make a unique set."` | `"Duplicate names cannot exist under the same parent"` | Custom validator message differs from generic DRF unique-together message                                |
| `part_count` nullable                                                    | Returns `null` on fresh create; `0` on PUT response    | Counts are computed/cached and may lag                                                                   |
| Deleting a part → 204                                                    | Active parts → 400 before 204                          | Must PATCH `active: false` first; this precondition is not documented                                    |

---

✓ TC_ACCRUD_CATEGORY_CRUD.md — 7 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ TC_APCRUD_PARTS_CRUD.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ TC_APFLT_FILTERING.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ TC_APVAL_VALIDATION.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ TC_APREL_RELATIONAL.md — 4 TCs written — observed 2026-04-14 — source: demo.inventree.org
✓ TC_APEDGE_EDGE_CASES.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
