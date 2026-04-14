---
source: https://docs.inventree.org/en/stable/part/units/
component: parts
topic: Units of Measure
fetched: 2026-04-14
---

# Units of Measure

> **Source**: https://docs.inventree.org/en/stable/part/
> Secondary source: https://docs.inventree.org/en/stable/concepts/units/

## Table of Contents

1. [Feature Overview](#feature-overview)
2. [How Units Are Assigned to Parts](#how-units-are-assigned-to-parts)
3. [Physical Unit Support](#physical-unit-support)
4. [Unit Notation Formats](#unit-notation-formats)
5. [Case Sensitivity](#case-sensitivity)
6. [Built-in Dimensionless Units](#built-in-dimensionless-units)
7. [Custom Units](#custom-units)
8. [Supplier Part Units and Conversion](#supplier-part-units-and-conversion)
9. [Parameters and Units](#parameters-and-units)
10. [UI Flows](#ui-flows)
11. [Validation Rules and Constraints](#validation-rules-and-constraints)
12. [API Field Reference](#api-field-reference)

---

## Feature Overview

Each type of part can define a custom "unit of measure" which is a standardized unit used to track quantities for a
particular part. By default, the "unit of measure" for each part is blank, which means that each part is tracked in
dimensionless quantities of "pieces".

InvenTree uses the **Pint** Python library to manage real-world physical units of measure. This provides:

- Consistent use of real units for inventory management
- Conversion between compatible units (e.g., grams to kilograms)
- Enforcement of compatible units when creating parameters and supplier parts
- Support for custom unit definitions

The unit system applies across three subsystems:

- **Parts** — unit of measure field on the part record
- **Supplier Parts** — supplier-side quantity can use any unit compatible with the base part unit
- **Parameters** — parameter templates can specify units; parameter values must use compatible units

---

## How Units Are Assigned to Parts

The `units` field is set on the Part record at creation or edit time.

- **UI location:** Part detail page → **Details** tab → **Units** field (labeled "Units of measure (UoM) for this Part.
  The default is 'pcs'")
- **API field:** `units` (string, nullable, max length: 20)
- **Default behavior:** When the `units` field is left blank or not provided, the part is tracked as dimensionless
  "pieces" (`pcs`).
- **Valid values:** Any unit string recognized by the Pint library (e.g., `metres`, `kg`, `litres`, `feet`, `cm`,
  `inches`), or any built-in/custom dimensionless unit (e.g., `piece`, `each`, `dozen`).

> **[IMAGE DESCRIPTION]**: A Part detail view screenshot showing the Part Units field. The part is named "Wire" and the
> Units field is set to "metres". The UI renders the field as a text input within the part details editing panel. The
> label "Units" is shown to the left of the field, with the value "metres" entered.

---

## Physical Unit Support

It is possible to track parts using physical quantity values, such as *metres* or *litres*. For example, it makes sense
to track a "wire" part in units of "metres".

Physical units cover the full range of SI and imperial measurements supported by Pint, including but not limited to:

- **Length:** `metres`, `feet`, `cm`, `inches`, `km`, `miles`
- **Mass:** `kg`, `g`, `lb`, `oz`
- **Volume:** `litres`, `ml`, `gallons`
- **Time:** `s`, `min`, `hours`
- **Electrical:** standard Pint-supported electrical units

---

## Unit Notation Formats

InvenTree (via Pint) supports several notation formats for entering unit quantities:

### Engineering Notation

Values can be specified using engineering notation:

| Input    | Interpreted Value |
|----------|-------------------|
| `10k3`   | 10,300            |
| `10M3`   | 10,000,000        |
| `3n02`   | 0.00000000302     |

### Scientific Notation

Scientific notation is supported:

| Input      | Interpreted Value |
|------------|-------------------|
| `1E-3`     | 0.001             |
| `1E3`      | 1000              |
| `-123.45E-3` | -0.12345        |

> **Note:** Support for scientific notation is case sensitive. `1E3` is valid; `1e3` is not.

### Feet and Inches Shorthand

| Input | Meaning  |
|-------|----------|
| `3'`  | 3 feet   |
| `6"`  | 6 inches |

> **Note:** Compound measurements (e.g., `3'6"`) are not supported.

---

## Case Sensitivity

The Pint library is case sensitive, and units must be specified in the correct case. Incorrect casing will be rejected
or misinterpreted.

Examples:

- `kg` is valid; `KG` is **not** valid
- `k` (kilo), `M` (mega), `n` (nano) — SI prefixes are case sensitive

This applies everywhere a unit string is entered: part `units` field, supplier part units, and parameter template units.

---

## Built-in Dimensionless Units

InvenTree ships with the following custom dimensionless units that extend Pint's defaults:

| Unit       | Description                           |
|------------|---------------------------------------|
| `piece`    | A single item, dimensionless          |
| `each`     | A single item, dimensionless          |
| `dozen`    | Twelve items, dimensionless           |
| `hundred`  | One hundred items, dimensionless      |
| `thousand` | One thousand items, dimensionless     |

These units are compatible with each other (all dimensionless), so a supplier part can supply in `dozen` when the base
part uses `piece`, and InvenTree will convert accordingly.

---

## Custom Units

Users can define additional custom units through the InvenTree settings interface.

- **UI location:** System Settings → **Physical Units** tab → Create new unit
- Custom units can represent new physical quantities, link to existing units, or serve as aliases for existing units.
- To create a dimensionless custom unit (e.g., a "box" or "roll"), enter the literal value `1` as the unit definition.

> **Note:** Custom unit creation requires staff or admin permissions. Refer to the Pint documentation for valid unit
> definition syntax when creating units that represent non-dimensionless physical quantities.

---

## Supplier Part Units and Conversion

By default, units of measure for supplier parts are specified in the same unit as their base part. However, supplier
part units can be changed to any unit *which is compatible with the base unit*.

**Example:** If the base part has a unit of `metres`, then valid units for any supplier parts would include `feet`,
`cm`, `inches`, etc.

InvenTree performs automatic quantity conversion when the supplier unit differs from the base part unit. This means
that a purchase order line for a supplier part specified in `feet` will be correctly converted to `metres` when the
stock is received against the base part.

**Incompatible units:** If an incompatible unit type is specified for a supplier part (e.g., `kg` when the base part
is in `metres`), an error is displayed and the supplier part cannot be saved.

> **[IMAGE DESCRIPTION]**: A screenshot showing the supplier part edit form with an incompatible unit entered. The
> Units field shows a unit from a different physical dimension than the base part. An error message is displayed below
> the Units field indicating that the specified unit is incompatible with the base part unit (e.g., "Units are not
> compatible"). The form cannot be submitted while this error is present.

---

## Parameters and Units

Parameter templates can specify a unit. When a parameter is created against a template that has a unit defined, the
parameter value must be specified in a unit compatible with the template unit.

- The system rejects parameter values with incompatible units (e.g., entering a length value against a mass parameter
  template).
- Sorting and filtering on parametric tables is unit-aware: filtering by `10k` and `10000` on a resistance parameter
  template produces equivalent results. Unit-aware sorting ensures correct ordering when values are stored with
  different but compatible units (e.g., `1 km` sorts correctly alongside `1000 m`).

---

## UI Flows

### Setting Units on a New Part (Part Creation)

1. Navigate to **Parts** in the sidebar.
2. Click **New Part** (or the equivalent create action).
3. In the Create Part modal/form, locate the **Units** field.
4. Enter a unit string (e.g., `metres`, `kg`) or leave blank for dimensionless "pieces".
5. Submit the form. The unit is saved to the `units` API field.

### Editing Units on an Existing Part

1. Navigate to the Part detail page.
2. Open the **Details** tab (or "Show Part Details" panel).
3. Click the edit (pencil) icon to open the edit form.
4. Modify the **Units** field.
5. Save. The `units` field is updated via `PATCH /api/part/{pk}/`.

> **Note:** Changing the unit on a part that already has associated supplier parts, parameters, or stock may require
> reviewing the compatibility of those related records. The system does not automatically migrate existing associated
> values when the base part unit changes.

### Setting Units on a Supplier Part

1. Navigate to the Part detail page → **Suppliers** tab.
2. Create or edit a supplier part.
3. In the supplier part form, locate the **Units** field.
4. Enter any unit compatible with the base part unit.
5. If an incompatible unit is entered, an error is shown and the form cannot be submitted.

### Viewing Unit Information in Stock

- Stock quantities for a part are always displayed in the base part unit.
- The **Stock** tab on a part detail page shows quantities in the part's unit of measure.
- When a supplier part uses a different-but-compatible unit, received quantities are converted to the base unit when
  stock is created.

---

## Validation Rules and Constraints

| Rule | Detail |
|------|--------|
| Unit string max length | 20 characters (API field constraint) |
| Blank unit | Allowed; the part is treated as dimensionless "pieces" |
| Invalid/unrecognized unit string | Rejected by the API with a validation error |
| Supplier part unit must be compatible | Incompatible units trigger a validation error; the record cannot be saved |
| Parameter value unit must be compatible with template unit | Incompatible units are rejected |
| Case sensitivity | Unit strings are case sensitive per Pint rules (e.g., `kg` valid, `KG` invalid) |
| Compound feet/inches notation | `3'6"` compound notation is not supported; use single-unit notation only |
| Scientific notation case | `1E3` valid; `1e3` invalid |

---

## API Field Reference

### `units` field on the Part schema

| Property    | Value                                    |
|-------------|------------------------------------------|
| Field name  | `units`                                  |
| Type        | string                                   |
| Nullable    | yes                                      |
| Max length  | 20                                       |
| Default     | `null` (displayed as "pcs" in the UI)    |
| Read-only   | no                                       |
| Description | Units of measure for this part           |

**Relevant endpoints:**

- `POST /api/part/` — include `units` in the request body to set units at creation time
- `GET /api/part/{pk}/` — `units` field is returned in the response
- `PATCH /api/part/{pk}/` — send `units` field to update an existing part's unit of measure
- `PUT /api/part/{pk}/` — full update, `units` must be included
