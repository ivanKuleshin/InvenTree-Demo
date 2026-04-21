# API Manual Exploration Log — Part Category Parameters

**Date:** 2026-04-14
**Tester:** AI exploration agent
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree

---

## Probe Sequence

### 1. GET /api/part/category/parameters/?limit=5

- Status: `200`
- count: `7`
- Response shape: `{ pk, category, category_detail, template, template_detail, default_value }`
- `category_detail` includes: pk, name, description, pathstring, level, parent, etc.
- `template_detail` includes: pk, name, units, description, checkbox, choices

### 2. GET /api/part/category/parameters/1/

- Status: `200`
- category: `5` (Resistors), template: `233` (Resistance), default_value: `""`

### 3. POST /api/part/category/parameters/ — create

- Request: `{"category": 6, "template": 233, "default_value": "100"}`
- Status: `201`; pk: `8`
- **Finding:** No validation that template type is appropriate for the category — Resistance template linked to Capacitors category without error

### 4. PATCH /api/part/category/parameters/8/

- Request: `{"default_value": "200"}`
- Status: `200`
- `default_value` updated to `"200"`

### 5. DELETE /api/part/category/parameters/8/

- Status: `204`
- Follow-up GET: `404`

---

## Divergences from Documentation

| Doc claim | Observed | Notes |
|-----------|----------|-------|
| Template-category type compatibility | No validation — any template can be linked to any category | Resistance template accepted on Capacitors category |
| `default_value` field | Always a string (even for numeric templates) | Empty string `""` when not set |

---

✓ category-parameters-test-suite.md — 5 TCs written — observed 2026-04-14 — source: demo.inventree.org
