# TC-APCRUD-008 — GET /api/part/ Pagination With limit and offset

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that `limit` and `offset` query parameters correctly control pagination, and that `next`/`previous` cursor links are consistent with the requested pagination state.

## Preconditions
- Authenticated as `reader` role
- Total part count is 435 (may vary; test must use observed `count` from first request)

## Requests

**Request 1** — First page:
```
GET /api/part/?limit=5&offset=0&format=json
```

**Request 2** — Middle page:
```
GET /api/part/?limit=5&offset=10&format=json
```

**Request 3** — Last page edge (use limit exceeding remaining items):
```
GET /api/part/?limit=5&offset=432&format=json
```

## Assertions

**All requests**:
1. Status code is 200.
2. `count` is the same across all pages (total catalog size, observed: 435).
3. `results` length equals `limit`, except on the last page where it may be less.

**Request 1 (offset=0)**:
4. `previous` is null.
5. `next` is a non-null URL containing `offset=5`.

**Request 2 (offset=10)**:
6. `previous` is a non-null URL.
7. `next` is a non-null URL.
8. `results` contains exactly 5 items.

**Request 3 (near last page)**:
9. `next` may be null when no more items remain.
10. `results` length is less than or equal to `limit`.

## Observed (offset=10, limit=5)
- `count`: 435
- `results` returned: 5
- `next`: `https://demo.inventree.org/api/part/?format=json&limit=5&offset=15`
- `previous`: `https://demo.inventree.org/api/part/?format=json&limit=5&offset=5`
