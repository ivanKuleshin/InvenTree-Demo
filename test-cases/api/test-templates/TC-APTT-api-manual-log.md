# API Manual Exploration Log — Part Test Templates

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/test-template/?limit=5

- Status: `200`
- count: `15`
- Response shape: `{ pk, key, part, test_name, description, enabled, required, requires_value, requires_attachment, results, choices }`
- `key` is auto-generated from `test_name` (lowercased, spaces stripped)
- `choices` present as empty string `""` (not `null`) when no choices defined

### 2. GET /api/part/test-template/8/

- Status: `200`
- Same field set as list response
- `results: 0` — read-only count of test results recorded against this template

### 3. POST /api/part/test-template/ — create with boolean flags

- Request: `{"part": 74, "test_name": "TC-APTT-Create-Test", "description": "...", "required": true, "requires_value": true, "requires_attachment": false}`
- Status: `201`; pk: `16`
- **DIVERGENCE:** `results` field is absent from the POST response (only present in GET responses)
- `key` auto-generated as `"tcapttcreatetest"` (lowercased, hyphens stripped)
- `enabled` defaults to `true`

### 4. PATCH /api/part/test-template/16/

- Request: `{"description": "Updated probe description", "requires_attachment": true}`
- Status: `200`
- Both fields updated; `results` field re-appears in PATCH response (unlike POST response)

### 5. DELETE /api/part/test-template/16/

- Status: `204` (no body)

### 6. GET /api/part/test-template/16/ (post-delete)

- Status: `404`
- Confirmed deletion

### 7. GET /api/part/test-template/?part=74&limit=5

- Status: `200`
- Returns only templates linked to part 74

---

## Divergences from Documentation

| Doc claim | Observed | Notes |
|-----------|----------|-------|
| `results` field present on PartTestTemplate | Absent from POST response | Only appears in GET and PATCH responses |
| `key` is read-only | Confirmed — auto-generated from test_name (lowercased, stripped) | |
| `choices` nullable string | Returns `""` not `null` when not set | |

---

✓ test-templates-test-suite.md — 6 TCs written — observed 2026-04-14 — source: demo.inventree.org
