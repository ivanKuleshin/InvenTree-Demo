# API Manual Exploration Log — Part Related Parts

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/related/?limit=5

- Status: `200`
- count: `5`
- Response shape: `{ pk, part_1, part_1_detail, part_2, part_2_detail, note }`
- `part_1_detail` and `part_2_detail` are full Part objects (~40 fields each), much more verbose than schema description

### 2. GET /api/part/related/1/

- Status: `200`
- `part_1: 89` (Blue Paint), `part_2: 92` (Green Paint), `note: ""`

### 3. POST /api/part/related/ — duplicate pair

- Request: `{"part_1": 82, "part_2": 84, "note": "TC-APRELATED probe"}`
- Status: `400` — `{"non_field_errors": ["Duplicate relationship already exists"]}`
- Parts 82 and 84 already have relationship pk=5

### 4. POST /api/part/related/ — new unique pair

- Request: `{"part_1": 73, "part_2": 75, "note": "TC-APRELATED probe"}`
- Status: `201`; pk: `6`
- Full `part_1_detail` and `part_2_detail` returned in response

### 5. PATCH /api/part/related/6/

- Request: `{"note": "TC-APRELATED updated note"}`
- Status: `200`
- Note updated; part fields unchanged

### 6. DELETE /api/part/related/6/

- Status: `204`
- Follow-up GET: `404`

---

## Divergences from Documentation

| Doc claim | Observed | Notes |
|-----------|----------|-------|
| `part_1_detail` / `part_2_detail` are `$ref: Part` | Contains ALL ~40 part fields | Much more verbose than expected; includes null fields like `in_stock`, `ordering` |
| Duplicate relationship | 400 `"Duplicate relationship already exists"` | Error in `non_field_errors` array |

---

✓ related-parts-test-suite.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
