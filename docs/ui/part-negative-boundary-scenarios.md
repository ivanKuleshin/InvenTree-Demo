---
source: https://docs.inventree.org/en/stable/part/part/
component: parts
topic: UI Negative and Boundary Scenarios — Parts Domain
fetched: 2026-04-14
---

# UI Negative and Boundary Scenarios — Parts Domain

> **Source**: https://docs.inventree.org/en/stable/part/part/
> Secondary sources:
> - https://docs.inventree.org/en/stable/part/ (part attributes, overview)
> - https://docs.inventree.org/en/stable/part/create/ (part creation)
> - https://docs.inventree.org/en/stable/concepts/units/ (unit validation)
> - https://docs.inventree.org/en/stable/part/revision/ (revision constraints)
> - Live API schema: https://demo.inventree.org/api/schema/ (field types, maxLength, constraints)
> - Live UI exploration: https://demo.inventree.org (confirmed error messages, selectors)

## Table of Contents

1. [Field-Level Constraints Reference](#field-level-constraints-reference)
2. [Required Fields](#required-fields)
3. [Text Field Constraints (maxLength, format)](#text-field-constraints)
4. [Numeric Field Constraints (range, minimum, type)](#numeric-field-constraints)
5. [Foreign Key / Relational Fields](#foreign-key--relational-fields)
6. [Boolean Fields — Default States](#boolean-fields--default-states)
7. [Units Field — Validation Rules](#units-field--validation-rules)
8. [Link Field — URL Validation](#link-field--url-validation)
9. [Revision Field — Uniqueness Constraint](#revision-field--uniqueness-constraint)
10. [File Import — Validation Rules](#file-import--validation-rules)
11. [Error Messages Observed in the UI](#error-messages-observed-in-the-ui)
12. [Boundary Value Summary Table](#boundary-value-summary-table)
13. [Invalid Input Behaviors](#invalid-input-behaviors)
14. [Attribute Interaction Constraints](#attribute-interaction-constraints)

---

## Field-Level Constraints Reference

The following table is the complete field-level constraint reference for the `Part` model as defined in the API schema
(OpenAPI 3.0.3, API version 479). These constraints are enforced server-side and reflected back through the UI as
inline validation errors.

| Field              | UI Label              | Type            | Required | Nullable | maxLength | minValue | maxValue         | Default  | Format     |
|--------------------|-----------------------|-----------------|----------|----------|-----------|----------|------------------|----------|------------|
| `name`             | Name                  | string          | YES      | NO       | 100       | —        | —                | —        | plain text |
| `IPN`              | IPN                   | string          | NO       | NO       | 100       | —        | —                | `""`     | plain text |
| `description`      | Description           | string          | NO       | NO       | 250       | —        | —                | —        | plain text |
| `revision`         | Revision              | string          | NO       | YES      | 100       | —        | —                | `""`     | plain text |
| `keywords`         | Keywords              | string          | NO       | YES      | 250       | —        | —                | —        | plain text |
| `link`             | Link                  | string (uri)    | NO       | YES      | 2000      | —        | —                | —        | URI        |
| `units`            | Units                 | string          | NO       | YES      | 20        | —        | —                | —        | Pint unit  |
| `default_expiry`   | Default Expiry        | integer         | NO       | NO       | —         | 0        | 2147483647       | 0        | integer    |
| `minimum_stock`    | Minimum Stock         | number (double) | NO       | NO       | —         | —        | —                | 0.0      | decimal    |
| `category`         | Category              | integer (FK)    | NO       | YES      | —         | —        | —                | null     | PK ref     |
| `default_location` | Default Location      | integer (FK)    | NO       | YES      | —         | —        | —                | null     | PK ref     |
| `variant_of`       | Variant Of            | integer (FK)    | NO       | YES      | —         | —        | —                | null     | PK ref     |
| `revision_of`      | Revision Of           | integer (FK)    | NO       | YES      | —         | —        | —                | null     | PK ref     |
| `responsible`      | Responsible           | integer (FK)    | NO       | YES      | —         | —        | —                | null     | PK ref     |

**Notes on constraint source:**
- `maxLength` values are from the OpenAPI schema `maxLength` property (enforced at Django model level).
- `minValue`/`maxValue` for `default_expiry` come from OpenAPI schema `minimum`/`maximum` properties.
- `minimum_stock` has no schema-level minimum or maximum — it accepts any non-negative decimal number.
- `link` and `image`/`remote_image` fields have `format: uri` — the server validates the string is a valid URI.

---

## Required Fields

Only one field is required when creating a part through the UI:

| Field  | UI Label | aria-label          | Enforcement                                                |
|--------|----------|---------------------|------------------------------------------------------------|
| `name` | Name *   | `text-field-name`   | Red asterisk on label; client-side + server-side validated |

**Observed behavior (confirmed on live UI):**
- Submitting the "Add Part" dialog with the Name field empty triggers immediate client-side validation.
- A red banner appears at the top of the form: **"Form Error — Errors exist for one or more form fields"**
- The Name field border turns red.
- An inline error message appears directly below the Name field: **"This field is required."**
- No network request is made (validation is pre-flight, not server-round-trip).

**Empty / whitespace-only Name:**
- A Name consisting entirely of whitespace characters may pass client-side validation (the field is technically
  non-empty) but is subject to server-side strip/blank validation. Actual behavior must be confirmed by testing.
- The API schema marks `name` as `required: true` without `allowBlank: false` specified separately, meaning the
  server will reject a blank string but the behavior of whitespace-only strings depends on Django's
  `validate_not_blank` validator (if applied).

---

## Text Field Constraints

### Name (`text-field-name`)

- **maxLength:** 100 characters
- **Required:** YES
- **Uniqueness:** No uniqueness constraint on name alone. A combination of `(name, IPN, revision)` must be unique
  per the API edge case documented in TC-APEDGE-005.
- **Boundary values:**
  - 1 character: valid minimum (non-blank)
  - 100 characters: valid at boundary
  - 101 characters: server should return a 400 error with message on the `name` field
- **Special characters:** No documented restriction — alphanumeric, punctuation, and Unicode characters are
  accepted unless the server rejects them. Behavior with `<`, `>`, `"`, `'`, `&`, SQL injection strings, and
  null bytes is untested and should be explored during negative testing.
- **Observed error for empty:** "This field is required."

### IPN (`text-field-IPN`)

- **maxLength:** 100 characters
- **Required:** NO
- **Default:** empty string `""`
- **Uniqueness:** No uniqueness constraint on IPN alone per schema. Combination of `(name, IPN, revision)` must
  be unique.
- **Boundary values:**
  - Empty string: valid (default)
  - 100 characters: valid at boundary
  - 101 characters: server should return 400

### Description (`text-field-description`)

- **maxLength:** 250 characters
- **Required:** NO
- **Boundary values:**
  - Empty: valid
  - 250 characters: valid at boundary
  - 251 characters: server should return 400

### Revision (`text-field-revision`)

- **maxLength:** 100 characters
- **Required:** NO (but subject to uniqueness constraint when `revision_of` is set — see below)
- **Default:** empty string `""`
- **Boundary values:**
  - Empty: valid (default)
  - 100 characters: valid at boundary
  - 101 characters: server should return 400

### Keywords (`text-field-keywords`)

- **maxLength:** 250 characters
- **Required:** NO
- **Boundary values:**
  - Empty: valid
  - 250 characters: valid at boundary
  - 251 characters: server should return 400

### Link (`text-field-link`)

- **maxLength:** 2000 characters
- **Required:** NO
- **format:** URI — server validates the string is a valid URL (scheme + host required)
- **Boundary values:**
  - Empty: valid (field is nullable)
  - Valid URL (e.g., `https://example.com`): accepted
  - 2000 characters: valid at boundary
  - 2001 characters: server should return 400
  - Non-URL string (e.g., `not-a-url`): server returns validation error
  - Protocol-relative URL (e.g., `//example.com`): behavior untested
  - Local file URL (e.g., `file:///etc/passwd`): behavior untested
- **Observed error for invalid URL in CSV import row:** row-level Error status with URL validation message

### Units (`text-field-units`)

- **maxLength:** 20 characters
- **Required:** NO
- **format:** Must be a unit string recognized by the Pint library, OR one of the built-in dimensionless unit
  strings. See [Units Field — Validation Rules](#units-field--validation-rules) for full details.
- **Boundary values:**
  - Empty: valid (defaults to dimensionless "pieces")
  - 20 characters: valid at boundary (if the string is a valid Pint unit)
  - 21 characters: server should return 400 (maxLength exceeded regardless of Pint validity)

---

## Numeric Field Constraints

### Default Expiry (`number-field-default_expiry`)

- **Type:** integer
- **Required:** NO
- **Default:** `0`
- **Minimum (schema):** `0`
- **Maximum (schema):** `2147483647` (Java/Django `IntegerField` max — `2^31 - 1`)
- **Boundary values:**
  - `0`: valid minimum (default)
  - `-1`: server should return 400 (below minimum)
  - `2147483647`: valid maximum
  - `2147483648`: server should return 400 (exceeds maximum)
  - Non-integer (e.g., `3.5`): behavior depends on form type coercion — the UI renders this as
    `number-field-default_expiry` which may or may not truncate decimal input
  - Non-numeric string: client-side or server-side should reject

### Minimum Stock (`number-field-minimum_stock`)

- **Type:** number (double / decimal)
- **Required:** NO
- **Default:** `0.0`
- **Minimum (schema):** None defined — the schema has no `minimum` constraint on this field
- **Maximum (schema):** None defined
- **Boundary values:**
  - `0`: valid (default)
  - `0.0`: valid
  - Negative values (e.g., `-1`, `-0.01`): no schema-level constraint — actual validation behavior
    must be confirmed on the live UI (may be accepted or rejected by model-level validator)
  - Very large decimal (e.g., `9999999999.99`): behavior untested — no documented maximum
  - Non-numeric string: client-side or server-side should reject

### Initial Stock Quantity (`number-field-initial_stock.quantity`)

- **Type:** decimal (write-only field, only present in the Create Part form)
- **Required:** YES (marked with `*` in the dialog — but creation proceeds with quantity `0`)
- **Default:** `0`
- **Note from TC-UI-PC-009:** "When Initial Stock Quantity is `0`, no stock item is created." The quantity
  field is technically required in the form schema but a value of `0` is valid and results in no stock
  being created.
- **Boundary values:**
  - `0`: valid; no stock item created
  - Positive decimal: valid; stock item created at given quantity
  - Negative value: behavior untested — no schema-level minimum constraint documented for this write-only field

---

## Foreign Key / Relational Fields

### Category (`related-field-category`)

- **Type:** integer FK → PartCategory
- **Required:** NO
- **UI:** Combobox (React-Select) with placeholder "Search..." — searches by category name
- **Constraint:** The referenced category ID must exist in the database. A non-existent ID returns 400.
- **Behavior when parent category is structural:** Parts cannot be directly assigned to structural categories
  (structural categories are container-only); attempting to assign a structural category as a part's category
  may be rejected.

### Default Location (`related-field-default_location`)

- **Type:** integer FK → StockLocation
- **Required:** NO
- **Constraint:** Referenced stock location ID must exist.

### Revision Of (`related-field-revision_of`)

- **Type:** integer FK → Part
- **Required:** NO
- **Constraint:**
  - Cannot be set to a self-reference (circular revision reference is blocked).
  - Cannot reference a Template part — template parts cannot have revisions.
  - The revision field on the new part must be unique among all revisions of the same base part.

### Variant Of (`related-field-variant_of`)

- **Type:** integer FK → Part (must be a Template part)
- **Required:** NO
- **Constraint:** Referenced part must have `is_template: true`.

---

## Boolean Fields — Default States

These are the confirmed default states for all boolean fields in the "Add Part" dialog, as observed on the live
demo UI (TC-UI-PC-001, TC-UI-PC-004):

| UI Label                 | aria-label                                  | Default State |
|--------------------------|---------------------------------------------|---------------|
| Component                | `boolean-field-component`                   | CHECKED       |
| Purchaseable             | `boolean-field-purchaseable`                | CHECKED       |
| Active                   | `boolean-field-active`                      | CHECKED       |
| Copy Category Parameters | `boolean-field-copy_category_parameters`    | CHECKED       |
| Assembly                 | `boolean-field-assembly`                    | unchecked     |
| Is Template              | `boolean-field-is_template`                 | unchecked     |
| Testable                 | `boolean-field-testable`                    | unchecked     |
| Trackable                | `boolean-field-trackable`                   | unchecked     |
| Salable                  | `boolean-field-salable`                     | unchecked     |
| Virtual                  | `boolean-field-virtual`                     | unchecked     |
| Locked                   | `boolean-field-locked`                      | unchecked     |

**Note:** The API schema shows `active` defaults to `true` and `component`/`purchaseable` default to `false`.
The UI dialog pre-checks Component, Purchaseable, and Active as a UX convenience; these are the most commonly
needed flags for a new physical part. The `copy_category_parameters` field is write-only (only present on
Create, not on Edit).

---

## Units Field — Validation Rules

The `units` field accepts only strings recognized by the Pint Python library or the built-in dimensionless units.

### Valid values (examples)

| Category        | Valid strings                                              |
|-----------------|------------------------------------------------------------|
| Dimensionless   | `piece`, `each`, `dozen`, `pcs`, `` (blank/empty)         |
| Length          | `metres`, `feet`, `cm`, `inches`, `km`, `miles`           |
| Mass            | `kg`, `g`, `lb`, `oz`                                     |
| Volume          | `litres`, `ml`, `gallons`                                  |
| Time            | `s`, `min`, `hours`                                       |

### Case sensitivity (critical constraint)

- Units are **case-sensitive** in Pint.
- `kg` (lowercase) is valid.
- `KG` (uppercase) is **invalid** — the server will reject it with a validation error.
- `Metres` (capital M) is **invalid** — must be `metres`.
- This applies to all physical units; only the exact Pint-registered spelling is accepted.

### Invalid values (produce server-side validation error)

- Any string that is not a Pint-recognized unit (e.g., `boxes`, `widget`, `PALLET`).
- Any string exceeding 20 characters.
- Compound unit expressions that Pint does not support in this context.

> **Note (from TC-UI-UNIT-004 and TC-UI-UNIT-005):** Entering an invalid unit string is **rejected with an
> error** when the part form is submitted. The field is validated server-side. The exact error message text
> must be confirmed during test execution (documentation does not specify the exact error string).

---

## Link Field — URL Validation

The `link` field has `format: uri` in the API schema.

### Valid values

- Any string that is a well-formed URI with a scheme and host, e.g.:
  - `https://example.com`
  - `https://example.com/path/to/datasheet.pdf`
  - `http://example.com`
  - `ftp://files.example.com/file`

### Invalid values (produce server-side validation error)

- Strings without a scheme: `example.com`, `//example.com`
- Strings without a host: `https://`
- Plain text: `not-a-url`, `datasheet`, `N/A`
- Empty scheme strings: `:///path`

**Observed in CSV import (TC-UI-PC-013):** Rows with a `link` value of `not-a-url` produce an **"Error"**
row status in the import wizard's Process Data step. Valid URL rows import successfully.

**Note on UI behavior for the Link field in the "Add Part" dialog:** The field is a text input
(`aria-label: text-field-link`). Client-side validation may not check URL format before submission;
the error will only appear after the form is submitted to the server.

---

## Revision Field — Uniqueness Constraint

**Source:** `docs/parts/part-revisions.md` / `https://docs.inventree.org/en/stable/part/revision/`

When creating a revision of an existing part (using the "Duplicate Part" flow with `revision_of` set):

- The `revision` string must be **unique** among all revisions of the same base part.
- Attempting to create a second part with `revision_of = X` and the same `revision` value as an existing revision
  of X produces a server-side error.
- The error is returned on the `revision` field.
- **Circular references are blocked:** Setting `revision_of` to the same part being edited (or to a part
  that already forms a cycle) is rejected.
- **Template parts cannot have revisions:** Attempting to set `revision_of` to a Template part (`is_template: true`)
  is rejected.
- **The global setting `PART_ENABLE_REVISION` must be `True`** (default) for the revision fields to appear in
  the UI. When this setting is `False`, the Revision and Revision Of fields are hidden; attempting to set them
  via the API when the feature is disabled may return a validation error.

---

## File Import — Validation Rules

### Supported file formats

The "Import Parts" wizard (Step 1 — Upload File) accepts only:

| Extension | Format                  |
|-----------|-------------------------|
| `.csv`    | Comma-Separated Values  |
| `.xlsx`   | Excel Open XML          |
| `.tsv`    | Tab-Separated Values    |

### Error messages for unsupported formats (confirmed on live UI — TC-UI-PC-011)

When a `.txt` file (or any unsupported extension) is submitted:
1. Banner: **"Form Error — Errors exist for one or more form fields"**
2. Inline error 1 (below Data File field): `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.`
3. Inline error 2 (below Data File field): `Unsupported data file format`

### Error message when no file is selected (confirmed on live UI — TC-UI-PC-011)

- Banner: **"Form Error — Errors exist for one or more form fields"**
- Inline error (below Data File field): **"No file was submitted."**

> **Divergence from docs:** The documentation states the error for a missing file should be "This field is
> required." but the actual live UI shows **"No file was submitted."** instead.

### Row-level validation errors during import

During Step 4 (Process Data), individual rows can have errors. Each row is assigned one of these statuses:

| Status    | Visual indicator | Meaning                                                         |
|-----------|------------------|-----------------------------------------------------------------|
| Pending   | Neutral          | Row is ready; no validation errors detected                     |
| Error     | Red              | Row has one or more validation errors; not imported             |
| Imported  | Green            | Row was successfully written to the database                    |

**Known causes of row-level Error status:**

- Missing required field (`name` is blank or missing)
- Invalid FK reference (e.g., `category` column contains an ID that does not exist)
- Invalid URL format in `link` column (e.g., `not-a-url`)
- Duplicate `(name, IPN, revision)` combination matching an existing part
- Character limit exceeded (e.g., name > 100 characters)

---

## Error Messages Observed in the UI

This section catalogs exact error message strings that have been confirmed by live UI exploration. These are the
strings automation tests should assert.

### "Add Part" / "Edit Part" dialog

| Trigger                                | Location               | Message Text                                          |
|----------------------------------------|------------------------|-------------------------------------------------------|
| Submit with empty Name field           | Banner (top of form)   | **"Form Error"** (heading) + **"Errors exist for one or more form fields"** (body) |
| Submit with empty Name field           | Inline below Name      | **"This field is required."**                         |
| Submit with Name > 100 chars           | Inline below Name      | Error text TBD — must confirm on live UI              |
| Submit with invalid URL in Link field  | Inline below Link      | Error text TBD — must confirm on live UI              |
| Submit with invalid unit in Units field| Inline below Units     | Error text TBD — must confirm on live UI              |
| Submit with IPN > 100 chars            | Inline below IPN       | Error text TBD — must confirm on live UI              |

**Notes on confirmed error selector (Mantine UI framework):**
- Banner: `page.getByRole('alert', { name: 'Form Error' })` (or by text content)
- Inline error: `page.getByText('This field is required.')`
- Name field error state: the Mantine text input has `data-error="true"` attribute when in error state

### "Import Parts" dialog

| Trigger                                | Location               | Message Text                                                              |
|----------------------------------------|------------------------|---------------------------------------------------------------------------|
| Submit with no file selected           | Banner                 | **"Form Error — Errors exist for one or more form fields"**               |
| Submit with no file selected           | Inline below Data File | **"No file was submitted."**                                              |
| Submit with `.txt` file                | Inline below Data File | `File extension "txt" is not allowed. Allowed extensions are: csv, xlsx, tsv.` |
| Submit with `.txt` file (second msg)   | Inline below Data File | `Unsupported data file format`                                            |

---

## Boundary Value Summary Table

This table consolidates all boundary values across fields for use as test inputs.

| Field              | At-Min Boundary       | Just-Below Min      | At-Max Boundary         | Just-Above Max          | Empty/Null    |
|--------------------|-----------------------|---------------------|-------------------------|-------------------------|---------------|
| `name`             | 1 char (`"A"`)        | 0 chars (`""`)      | 100 chars exactly       | 101 chars               | Error: required |
| `IPN`              | 0 chars (empty)       | N/A                 | 100 chars exactly       | 101 chars               | Valid (empty) |
| `description`      | 0 chars (empty)       | N/A                 | 250 chars exactly       | 251 chars               | Valid (empty) |
| `revision`         | 0 chars (empty)       | N/A                 | 100 chars exactly       | 101 chars               | Valid (empty) |
| `keywords`         | 0 chars (empty)       | N/A                 | 250 chars exactly       | 251 chars               | Valid (empty) |
| `link`             | Empty / null          | N/A                 | 2000 chars valid URL    | 2001 chars              | Valid (empty) |
| `units`            | Empty (blank)         | N/A                 | 20 chars valid unit     | 21 chars                | Valid (dimensionless) |
| `default_expiry`   | `0` (integer)         | `-1`                | `2147483647`            | `2147483648`            | Valid (`0`)   |
| `minimum_stock`    | `0.0`                 | N/A (no min)        | No documented max       | N/A                     | Valid (`0.0`) |
| `initial_stock.quantity` | `0` (no stock) | N/A (no min documented) | N/A                 | N/A                     | Valid (`0`)   |

---

## Invalid Input Behaviors

These input categories should be explored during negative testing. Expected behavior is noted where confirmed;
otherwise behavior is "TBD — confirm on live UI."

### Special Characters in Text Fields (Name, IPN, Description, etc.)

| Input Type                        | Example                            | Expected Behavior                                     |
|-----------------------------------|------------------------------------|-------------------------------------------------------|
| HTML entities                     | `<script>alert(1)</script>`        | Stored as literal text; rendered escaped in UI. TBD.  |
| SQL injection string              | `'; DROP TABLE part; --`           | Stored as literal text (ORM handles parameterization) |
| Angle brackets                    | `<Test>`                           | TBD — may or may not be rejected by server            |
| Single/double quotes              | `O'Reilly`, `name "quoted"`        | Likely valid text; TBD                                |
| Null byte                         | `\x00`                             | Likely rejected by server; TBD                        |
| Unicode (non-ASCII)               | `Ωмega`, `部品名`                   | Likely accepted; TBD — Pint does not apply here       |
| Newline / carriage return         | `Line1\nLine2`                     | TBD — single-line inputs may strip or reject          |
| Control characters                | `\t`, `\r`                         | TBD                                                   |
| Very long string (stress test)    | 1000-character string              | Server should return 400 with maxLength error          |

### URL Field — Invalid Format Inputs (`link`)

| Input                             | Expected Result                                           |
|-----------------------------------|-----------------------------------------------------------|
| `not-a-url`                       | Server 400 — URL validation error                         |
| `example.com` (no scheme)         | Server 400 — URL validation error (scheme required)       |
| `javascript:alert(1)` (XSS)       | TBD — Django URL validator may reject non-http/https      |
| `ftp://files.example.com`         | TBD — may be valid URI but ftp scheme acceptance untested |
| 2001-character URL                | Server 400 — maxLength exceeded                           |
| Whitespace-only                   | Treated as blank/null (field is nullable); likely valid   |

### Numeric Fields — Invalid Type Inputs

| Field            | Input           | Expected Result                                              |
|------------------|-----------------|--------------------------------------------------------------|
| `default_expiry` | `-1`            | Server 400 — below minimum (0)                               |
| `default_expiry` | `3.5` (decimal) | TBD — integer field may truncate or reject fractional input  |
| `default_expiry` | `abc` (text)    | Client-side or server-side rejection — not a valid integer   |
| `default_expiry` | `2147483648`    | Server 400 — exceeds maximum                                 |
| `minimum_stock`  | `-1`            | TBD — no schema minimum; actual server behavior unknown      |
| `minimum_stock`  | `abc` (text)    | Client-side or server-side rejection — not a valid number    |

### Units Field — Invalid Unit Strings

| Input      | Expected Result                                                          |
|------------|--------------------------------------------------------------------------|
| `KG`       | Server 400 — case-sensitive; `KG` is not a valid Pint unit (use `kg`)   |
| `Metres`   | Server 400 — case-sensitive; use `metres`                                |
| `boxes`    | Server 400 — not a recognized Pint unit or built-in dimensionless unit  |
| `PALLET`   | Server 400 — not a recognized unit                                       |
| 21-char string | Server 400 — maxLength exceeded                                     |
| `kg/m`     | TBD — compound unit; Pint may or may not accept                         |

### Boolean Fields — Non-Boolean Inputs (API only, not applicable to UI checkboxes)

In the UI, boolean fields are rendered as checkboxes and can only be checked/unchecked. Non-boolean input is only
testable via direct API manipulation:

| Input      | Field         | Expected Result          |
|------------|---------------|--------------------------|
| `"yes"`    | `active`      | API 400 — not boolean    |
| `1` (int)  | `active`      | TBD — may be coerced     |
| `null`     | `active`      | API 400 — not nullable   |

---

## Attribute Interaction Constraints

These are functional restrictions enforced by the system when specific boolean attribute combinations are set.
They are relevant for negative testing because they may produce unexpected validation errors or blocked UI actions.

| Scenario                           | Constraint                                                                      | Expected UI Behavior                                           |
|------------------------------------|---------------------------------------------------------------------------------|----------------------------------------------------------------|
| `virtual: true` + Add Stock        | Virtual parts cannot have stock items                                           | Stock-related UI elements are hidden; attempts blocked         |
| `locked: true` + Edit BOM          | BOM cannot be modified when part is locked                                      | BOM tab is read-only; add/edit/delete BOM items blocked        |
| `locked: true` + Edit Parameters   | Parameters cannot be modified when part is locked                               | Parameters tab is read-only                                    |
| `locked: true` + Delete Part       | Locked parts cannot be deleted                                                  | Delete action returns an error or is disabled in UI            |
| `is_template: true` + Set Revision | Template parts cannot be set as a revision of another part                      | Server 400 when attempting to set `revision_of` on a template  |
| `active: false` + Add to BOM       | Inactive parts are excluded from selection lists                                | Part does not appear in BOM part search combobox               |
| `active: false` + Add to PO/SO     | Inactive parts cannot be placed on purchase or sales orders                     | Part does not appear in order line item selection              |
| `purchaseable: false` + Suppliers tab | Suppliers tab is absent / hidden for non-purchaseable parts                  | Suppliers tab does not appear in part detail tab bar           |
| `salable: false` + Sales Orders tab | Sales Orders tab is absent for non-salable parts                              | Sales Orders tab does not appear in part detail tab bar        |
| `is_template: false` + Variants tab | Variants tab is absent for non-template parts                                 | Variants tab does not appear in part detail tab bar            |
| `component: false` + Add to BOM    | Non-component parts cannot be added to any assembly BOM                        | Part does not appear in BOM sub-part search combobox           |
| Revision duplicate code            | Two revisions of the same base part cannot share the same `revision` value      | Server 400 on the `revision` field                             |
| Circular revision reference        | `revision_of` cannot form a cycle                                               | Server 400 — circular reference blocked                        |
| `virtual: true` + `trackable: true`| No explicit documented conflict — but virtual parts cannot have stock (and thus no serial/batch numbers) | Exploratory; may present contradictory UX |
