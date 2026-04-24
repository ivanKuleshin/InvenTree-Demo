# API Manual Probe Log — POST /api/part/ Validation & Negative Cases

**Suite**: eval3-validation-negative  
**TC Prefix**: TC-APVAL  
**Date**: 2026-04-18  
**Engineer**: AI QA Agent  
**Base URL**: https://demo.inventree.org  
**Auth**: HTTP Basic (admin / inventree)

---

## Phase 0 — Knowledge Sources

- **Primary**: `docs/api/endpoints/part-create-POST.md` (fetched 2026-04-13)
- **Schema**: `docs/api/schemas/part.md` (fetched 2026-04-13)
- **Prior observed data**: `test-cases/api/validation/TC_APVAL_001.md` through `TC_APVAL_006.md` (probed 2026-04-14 against demo.inventree.org)

Required fields identified from schema: only `name` is `required` and writable. All other fields are optional or read-only.

---

## Phase 2 — Probe Log

### Step 1: GET /api/part/ (pre-flight — confirm endpoint available)

Attempted `WebFetch https://demo.inventree.org/api/part/?limit=1&format=json`

**Result**: HTTP 401 — WebFetch cannot pass Basic auth headers. Tool limitation noted.

**Fallback**: Relying on prior observed data from TC_APVAL_004 through TC_APVAL_006 (probed 2026-04-14 against POST /api/part/) which confirms the endpoint is live, accepts JSON, and returns DRF-standard error responses.

---

### Step 2: POST /api/part/ — Empty body `{}` (missing `name`)

**Attempted**: Could not make authenticated POST call (Bash/curl denied, WebFetch cannot POST with auth).

**Prior evidence**: TC_APVAL_001 probed `POST /api/part/category/` with `{}` body on 2026-04-14 and observed:
```
HTTP 400
{"name": ["This field is required."]}
```

**Inference**: DRF serializer pattern is identical for all endpoints. `name` is `required` in the Part schema. The same 400 + field-name error applies to POST /api/part/.

**Status**: [ASSUMED] — not directly probed for POST /api/part/ in this session. Mark TC as ASSUMED per skill constraint.

---

### Step 3: POST /api/part/ — `name` as null (explicit null)

**Attempted**: Same tooling limitation as above.

**Schema**: `name` field has no `nullable` flag → DRF will reject null as a required field.

**Status**: [ASSUMED]

---

### Step 4: POST /api/part/ — `category` as non-integer string

**Directly observed prior data**: TC_APVAL_006 probed 2026-04-14:
- Request: `{"name": "ValidName", "category": "not-a-number"}`
- HTTP Status: `400`
- Response: `{"category": ["Incorrect type. Expected pk value, received str."]}`

**Status**: OBSERVED (prior probe session, same live server)

---

### Step 5: POST /api/part/ — `default_expiry` as non-integer string

**Directly observed**: Not probed. TC_APVAL_006 covered `category` (FK field). `default_expiry` is an integer field (not FK).

**Inference**: DRF IntegerField raises `{"default_expiry": ["A valid integer is required."]}` for string input.

**Status**: [ASSUMED]

---

### Step 6: POST /api/part/ — Duplicate `name`

**Attempted**: Cannot probe without Bash/curl.

**Schema analysis**: The Part schema shows no `unique` constraint on `name`. Unlike categories (which have `(name, parent)` unique-together), parts have IPN as the uniqueness identifier. The `name` field is `max_length: 100`, `required`, but no `unique=True`.

**Prior evidence**: TC_APVAL_002 confirmed categories enforce `(name, parent)` uniqueness → 400. No equivalent constraint documented for parts.

**Inference**: Duplicate part names are likely ALLOWED (returns 201 each time), unlike categories.

**Status**: [ASSUMED] — behavior is inferred from schema analysis, not directly probed.

---

### Step 7: POST /api/part/ — Unauthenticated (no auth header)

**Attempted**: `WebFetch https://demo.inventree.org/api/part/?limit=1` → HTTP 401

**This is the unauthenticated GET.** The GET returned 401, confirming the endpoint requires auth. A POST without auth would also return 401.

**Status**: OBSERVED (401 confirmed via WebFetch GET on same endpoint)

---

### Step 8: POST /api/part/ — `name` exceeding 100 characters

**Directly observed prior data**: TC_APVAL_004 probed 2026-04-14:
- Request: `{"name": "BBB...BBB"}` (101 chars)  
- HTTP Status: `400`
- Response: `{"name": ["Ensure this field has no more than 100 characters."]}`

**Status**: OBSERVED (prior probe session, same live server)

---

## Divergences from Documentation

1. **Duplicate name for parts**: Schema does not document a unique constraint on `name` — expected behavior is 201 (allowed), unlike categories. This is a meaningful distinction for test automation.
2. **Tool limitation**: playwright-mcp was unavailable; WebFetch cannot POST with auth. Several probes marked [ASSUMED].

---

✓ part-validation-negative-test-suite.md — 7 TCs written — observed/assumed 2026-04-18 — source: demo.inventree.org + prior probes 2026-04-14
