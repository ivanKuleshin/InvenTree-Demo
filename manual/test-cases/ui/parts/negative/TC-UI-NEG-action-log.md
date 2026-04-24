# UI Manual Exploration Action Log — Negative / Boundary Scenarios

**Date:** 2026-04-14
**Base URL:** https://demo.inventree.org
**Auth:** admin / inventree (Adam Administrator — superuser)
**Suite:** TC-UI-NEG-*

---

## Session Summary

All error message strings below were confirmed by two complementary methods:

1. **Direct API probes** — POST /api/part/ with boundary/invalid payloads, capturing JSON response bodies.
2. **Edit Part dialog interaction** — Navigating to part 911 (Blue Furniture Set), opening the Edit Part dialog, entering invalid values, submitting, and reading the rendered error text from `.mantine-InputWrapper-error` elements and the `[role="alert"]` banner.

The `action-menu-add-parts` button was absent from the root category index view (`/web/part/category/index/parts`) and from specific sub-category pages during this session. All Add/Edit Part form behavior was observed via the **Edit Part** dialog, which renders identical fields and identical validation behavior to the Add Part dialog.

---

## Confirmed Error Messages

### Form-level banner (all validation failures)

| Trigger | Banner text |
|---|---|
| Any field fails validation on submit | `"Form Error"` (heading) + `"Errors exist for one or more form fields"` (body) |
| Uniqueness constraint violation | `"Form Error"` (heading) + `"The fields name, IPN, revision must make a unique set."` (body only — no inline field error) |

### Inline field errors (appear below the offending field)

| Field | Invalid input | Inline error text | Confirmed by |
|---|---|---|---|
| Name (`text-field-name`) | Empty string or whitespace | `"This field may not be blank."` | Edit Part dialog + API |
| Name (`text-field-name`) | 101 characters | `"Ensure this field has no more than 100 characters."` | Edit Part dialog + API |
| IPN (`text-field-IPN`) | 101 characters | `"Ensure this field has no more than 100 characters."` | API |
| Description (`text-field-description`) | 251 characters | `"Ensure this field has no more than 250 characters."` | API |
| Units (`text-field-units`) | `"boxes"` (unrecognized) | `"Invalid physical unit"` | Edit Part dialog + API |
| Units (`text-field-units`) | `"KG"` (uppercase) | `"Invalid physical unit"` | Edit Part dialog + API |
| Units (`text-field-units`) | 21-char string | `"Invalid physical unit"` + `"Ensure this field has no more than 20 characters."` | API (two errors) |
| Link (`text-field-link`) | `"not-a-url"` | `"Enter a valid URL."` | Edit Part dialog + API |
| Link (`text-field-link`) | `"example.com"` (no scheme) | `"Enter a valid URL."` | API |
| Link (`text-field-link`) | `"javascript:alert(1)"` | `"Enter a valid URL."` | API |
| Default Expiry (`number-field-default_expiry`) | `-1` | `"Ensure this value is greater than or equal to 0."` | Edit Part dialog + API |
| Default Expiry (`number-field-default_expiry`) | `2147483648` | `"Ensure this value is less than or equal to 2147483647."` | API |
| Minimum Stock (`number-field-minimum_stock`) | `-1` | `"Ensure this value is greater than or equal to 0."` | Edit Part dialog + API |
| Revision (`text-field-revision`) | when `revision_of` set but revision is blank | `"Revision code must be specified for a part marked as a revision"` | API |

---

## Boundary Values Confirmed

| Field | Boundary | Result | Status Code |
|---|---|---|---|
| Name | 100 chars exactly | Accepted | 201 |
| Name | 101 chars | Rejected | 400 |
| Name | whitespace-only `"   "` | Rejected — `"This field may not be blank."` | 400 |
| Default Expiry | `0` | Accepted | 201 |
| Default Expiry | `-1` | Rejected | 400 |
| Default Expiry | `2147483647` | Accepted | 201 |
| Default Expiry | `2147483648` | Rejected | 400 |
| Minimum Stock | `0` | Accepted | 201 |
| Minimum Stock | `-1` | Rejected — server enforces min=0 (not just schema) | 400 |
| Units | `"kg"` (valid lowercase) | Accepted | 201 |
| Units | `"KG"` (uppercase) | Rejected | 400 |
| Units | 20-char valid unit | Accepted (if Pint-valid) | 201 |
| Units | 21-char string | Rejected — two errors | 400 |
| Link | `"https://example.com/"` (valid) | Accepted | 201 |
| Link | `"not-a-url"` | Rejected | 400 |
| Link | 2001-char valid URL | **Accepted — maxLength NOT enforced server-side for link** | 201 |

### Key divergence from documentation

The `link` field maxLength of 2000 is **not enforced server-side** — a 2001-character valid URL was accepted with HTTP 201. The constraint appears in the OpenAPI schema but is not applied by the Django validator.

---

## Virtual Part UI Observation

- Part: "CRM license" (pk=914), `virtual: true`
- URL: `https://demo.inventree.org/web/part/914/details`
- Navigation tabs present: Part Details, Part Pricing, Suppliers, Purchase Orders, Related Parts, Parameters, Attachments, Notes
- Tabs **absent**: Stock, Allocations (Build Orders not present either)
- Confirmed: virtual parts have no Stock tab in the detail navigation

---

## Locked Assembly Part BOM Observation

- Part: "Blue Furniture Set" (pk=911), `locked: true`, `assembly: true`
- URL: `https://demo.inventree.org/web/part/911/bom`
- BOM tab shows: "Part is Locked" heading + "Bill of materials cannot be edited, as the part is locked"
- No add/edit/delete BOM row buttons present in toolbar

---

## Duplicate Submission Observation

- Opened Duplicate dialog on part 911 (Blue Furniture Set)
- Submitted without changing name, IPN, or revision
- Result: dialog remains open, alert banner shows `"Form Error — The fields name, IPN, revision must make a unique set."`
- No inline error below any specific field — error appears only in the top-level banner

---

## Add Part Dialog — Field Labels (from existing action log + Edit Part dialog confirmation)

| UI Label | aria-label | Type |
|---|---|---|
| Category | `related-field-category` | combobox |
| Name * | `text-field-name` | text input |
| IPN | `text-field-IPN` | text input |
| Description | `text-field-description` | text input |
| Revision | `text-field-revision` | text input |
| Revision Of | `related-field-revision_of` | combobox |
| Variant Of | `related-field-variant_of` | combobox |
| Keywords | `text-field-keywords` | text input |
| Units | `text-field-units` | text input |
| Link | `text-field-link` | text input |
| Default Location | `related-field-default_location` | combobox |
| Default Expiry | `number-field-default_expiry` | number input |
| Minimum Stock | `number-field-minimum_stock` | number input |
| Component | `boolean-field-component` | checkbox |
| Assembly | `boolean-field-assembly` | checkbox |
| Is Template | `boolean-field-is_template` | checkbox |
| Virtual | `boolean-field-virtual` | checkbox |
| Locked | `boolean-field-locked` | checkbox |
| Active | `boolean-field-active` | checkbox |
| Submit | — | button |
| Cancel | — | button |
