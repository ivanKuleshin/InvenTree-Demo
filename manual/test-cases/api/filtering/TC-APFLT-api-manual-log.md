# API Manual Probe Log — TC-APFLT Filtering, Pagination, Search

**Source:** demo.inventree.org  
**Probed:** 2026-04-14  
**Auth:** Token auth (header `Authorization: Token inv-demo`)  
**Endpoint base:** `GET /api/part/`

---

## Probe Calls

### Pagination — limit=0

```
GET /api/part/?limit=0&format=json
→ 200 raw JSON array, length=1442
→ No count/next/previous fields
```

### Pagination — limit=-1

```
GET /api/part/?limit=-1&format=json
→ 200 raw JSON array, length=1442
→ Same as limit=0
```

### Pagination — offset=99999

```
GET /api/part/?limit=5&offset=99999&format=json
→ 200 { count:1442, next:null, previous:"...offset=99994", results:[] }
```

### Pagination — offset=-1

```
GET /api/part/?limit=5&offset=-1&format=json
→ 200 { count:1442, results_length:5, previous:null, first_pk:82 }
→ Treated as offset=0
```

### Pagination — limit=5000 (exceeds total)

```
GET /api/part/?limit=5000&format=json
→ 200 { count:1453, results_length:1453, next:null }
→ All parts in one page
```

### Search — empty string

```
GET /api/part/?search=&limit=3&format=json
→ 200 count:1453 — same as unfiltered
```

### Boolean filter — assembly=true

```
GET /api/part/?assembly=true&limit=3&format=json
→ 200 count:136, results: [pk=1934 assembly:true, pk=2046 assembly:true, pk=1921 assembly:true]
```

### Boolean filter — virtual=true

```
GET /api/part/?virtual=true&limit=3&format=json
→ 200 count:33, results: [pk=1934 virtual:true, pk=2046 virtual:true, pk=914 "CRM license" virtual:true]
```

### Boolean filter — trackable=true

```
GET /api/part/?trackable=true&limit=3&format=json
→ 200 count:58, results: [pk=1934 trackable:true, pk=2046 trackable:true, pk=1217 "AUTO_QA_TRACKABLE_PART" trackable:true]
```

### Boolean filter — purchasable=true&salable=true

```
GET /api/part/?purchasable=true&salable=true&limit=3&format=json
→ 200 count:38, results: [pk=1934 purchasable:true salable:true, pk=2046 purchasable:true salable:true, pk=1161 "Buy-Sell Part 1776151541544" purchasable:true salable:true]
```

### Boolean filter — has_stock=true

```
GET /api/part/?has_stock=true&limit=3&format=json
→ 200 count:404, results: [pk=82 "1551ABK" in_stock:1867, pk=84 "1551ACLR" in_stock:100, pk=83 "1551AGY" in_stock:123]
```

### Boolean filter — has_stock=false

```
GET /api/part/?has_stock=false&limit=3&format=json
→ 200 count:1038, results: [pk=86 "1553WDBK" in_stock:0, pk=2063 "5mm Red LED" in_stock:0, ...]
```

### IPN exact match

```
GET /api/part/?IPN=RES-001&limit=5&format=json
→ 200 count:1, result: pk=1692 name="Full Field Part" IPN="RES-001"

GET /api/part/?IPN=res-001&limit=5&format=json
→ 200 count:1, result: pk=1692 IPN="RES-001"  [case-insensitive observed — diverges from docs]

GET /api/part/?IPN=RES-999&limit=5&format=json
→ 200 count:0, results:[]
```

### IPN regex

```
GET /api/part/?IPN_regex=%5ERES&limit=5&format=json  (^RES)
→ 200 count:7, results: [pk=1692 IPN="RES-001", pk=2219 IPN="RES-10K-001", pk=926 IPN="RES-1K-1776148821705", ...]
```

### name_regex

```
GET /api/part/?name_regex=%5ER&limit=5&format=json  (^R)
→ 200 count:93, results: [pk=43 "R_100K_0402_1%", pk=44 "R_100K_0603_1%", pk=45 "R_100K_0805_1%", pk=4 "R_100R_0402_1%", pk=5 "R_100R_0603_1%"]
```

### Date range filter

```
GET /api/part/?created_after=2026-01-01&created_before=2026-04-14&limit=5&format=json
→ 200 count:7, results: [pk=914 "CRM license" creation_date="2026-03-30", pk=915 "Encabulator" creation_date="2026-04-03", ...]

GET /api/part/?created_after=2030-01-01&limit=5&format=json
→ 200 count:0, results:[]
```

### Category filter with cascade

```
GET /api/part/?category=24&limit=5&format=json
→ 200 count:2, results: [pk=1933 category=24, pk=2267 category=26]

GET /api/part/?category=24&cascade=true&limit=5&format=json
→ 200 count:2, same results
→ Category 24 = "Category 1" (parent=null), Category 26 = "Category 3" (parent=24)
→ Both queries include child category parts — cascade defaults true
```

### Combined filters — assembly+active

```
GET /api/part/?assembly=true&active=true&limit=5&format=json
→ 200 count:135, all results have assembly:true active:true
```

### Combined filters — search+assembly

```
GET /api/part/?search=resistor&assembly=true&limit=5&format=json
→ 200 count:1, result: pk=1692 "Full Field Part" assembly:true
```

### Ordering — invalid field

```
GET /api/part/?ordering=invalid_field&limit=3&format=json
→ 200 count:1452, results_length:3, first_name:"1551ABK"
→ Silent no-op, default order applied
```

### Ordering — -in_stock

```
GET /api/part/?ordering=-in_stock&limit=3&format=json
→ 200 count:1453, results: [pk=23 in_stock:19625, pk=46 in_stock:18090, pk=38 in_stock:15803]
→ Descending order confirmed
```

---

## Divergences from Documentation

| Area | Documented | Observed |
|------|-----------|----------|
| `IPN` filter case sensitivity | Case-sensitive | Demo instance returned match on lowercase `res-001` for `IPN="RES-001"` — likely case-insensitive DB collation |
| `category` without `cascade` | Direct members only | Demo returned child-category parts without `cascade=true` — cascade appears to default to `true` |

---

✓ TC_APFLT_007 through TC_APFLT_025 — 19 TCs written — observed 2026-04-14 — source: demo.inventree.org
