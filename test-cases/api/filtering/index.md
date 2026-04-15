# Test Suite Index: API Filtering, Pagination, and Search

**Endpoint:** `GET /api/part/`  
**Probed:** 2026-04-14  
**Source doc:** `docs/api-parts-list-filtering-pagination-search.md`

| TC ID | Title | Type | Priority |
|-------|-------|------|----------|
| TC-APFLT-001 | limit and offset return correct non-overlapping pages | API | P1 |
| TC-APFLT-002 | search returns parts matching the search term across multiple fields | API | P1 |
| TC-APFLT-003 | category filter returns parts in the specified category | API | P1 |
| TC-APFLT-004 | active filter separates active and inactive parts | API | P2 |
| TC-APFLT-005 | ordering=name and ordering=-name sort results alphabetically | API | P2 |
| TC-APFLT-006 | category search and parent filter on /api/part/category/ | API | P2 |
| TC-APFLT-007 | limit=0 returns unpaginated raw JSON array | API | P2 |
| TC-APFLT-008 | limit=-1 is treated as limit=0 — returns unpaginated raw array | API | P3 |
| TC-APFLT-009 | offset beyond total count returns empty results with accurate count | API | P3 |
| TC-APFLT-010 | offset=-1 is treated as offset=0 — returns first page | API | P3 |
| TC-APFLT-011 | limit exceeding total count returns all parts in a single page | API | P2 |
| TC-APFLT-012 | search= empty string is equivalent to no search filter | API | P2 |
| TC-APFLT-013 | assembly=true filters to assembly parts only | API | P1 |
| TC-APFLT-014 | virtual=true filters to virtual parts only | API | P2 |
| TC-APFLT-015 | trackable=true filters to serial-tracked parts only | API | P2 |
| TC-APFLT-016 | has_stock=true and has_stock=false partition all parts by stock presence | API | P1 |
| TC-APFLT-017 | purchasable=true&salable=true combines boolean filters with AND logic | API | P2 |
| TC-APFLT-018 | IPN exact match returns the single matching part; non-existent IPN returns empty | API | P1 |
| TC-APFLT-019 | IPN_regex=^RES matches all parts whose IPN starts with "RES" | API | P2 |
| TC-APFLT-020 | name_regex=^R filters parts whose name starts with "R" | API | P2 |
| TC-APFLT-021 | created_after and created_before filter by creation date range | API | P2 |
| TC-APFLT-022 | category with cascade=true includes parts in child categories | API | P1 |
| TC-APFLT-023 | ordering=invalid_field silently returns HTTP 200 with default order | API | P3 |
| TC-APFLT-024 | ordering=-in_stock sorts results by stock quantity descending | API | P2 |
| TC-APFLT-025 | combined search + boolean filters apply AND logic to narrow results | API | P1 |
