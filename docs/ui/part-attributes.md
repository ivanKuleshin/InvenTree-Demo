---
source: https://docs.inventree.org/en/stable/part/part/
component: parts
topic: Part Boolean Toggle Attributes
fetched: 2026-04-14
---

# Part Boolean Toggle Attributes

> **Source**: https://docs.inventree.org/en/stable/part/part/
> Secondary sources: https://docs.inventree.org/en/stable/part/#part-attributes,
> https://docs.inventree.org/en/stable/part/virtual/,
> https://docs.inventree.org/en/stable/part/trackable/,
> https://docs.inventree.org/en/stable/part/template/

## Table of Contents

1. [Overview](#overview)
2. [Attribute Summary Table](#attribute-summary-table)
3. [Active / Inactive](#active--inactive)
4. [Virtual](#virtual)
5. [Template (is_template)](#template-is_template)
6. [Assembly](#assembly)
7. [Component](#component)
8. [Trackable](#trackable)
9. [Testable](#testable)
10. [Purchaseable](#purchaseable)
11. [Salable](#salable)
12. [Locked](#locked)
13. [Attribute Dependencies and Interactions](#attribute-dependencies-and-interactions)
14. [API Field Reference](#api-field-reference)

---

## Overview

Each Part in InvenTree carries a set of boolean toggle attributes that control which system features are available for
that part. These flags are set on the Part detail page under the **Details** tab and are visible in the right-hand
attributes panel. Every flag defaults to `false` unless noted otherwise. Multiple flags can be enabled simultaneously
on the same part; some combinations introduce constraints described in the
[Attribute Dependencies](#attribute-dependencies-and-interactions) section.

---

## Attribute Summary Table

| UI Label      | API Field      | Default | Short Description                                          |
|---------------|----------------|---------|------------------------------------------------------------|
| Active        | `active`       | `true`  | Part is available for use; inactive parts are restricted   |
| Virtual       | `virtual`      | `false` | Non-physical item; cannot have stock                       |
| Template      | `is_template`  | `false` | Part can have variant parts nested under it                |
| Assembly      | `assembly`     | `false` | Part can be built from other component parts via a BOM     |
| Component     | `component`    | `false` | Part can be used as a sub-component in an assembly BOM     |
| Trackable     | `trackable`    | `false` | Stock items must carry a batch or serial number            |
| Testable      | `testable`     | `false` | Test templates and results can be recorded against the part|
| Purchaseable  | `purchaseable` | `false` | Part can be linked to suppliers and placed on purchase orders|
| Salable       | `salable`      | `false` | Part can be added to sales orders                          |
| Locked        | `locked`       | `false` | Part cannot be modified; BOM and parameters are read-only  |

---

## Active / Inactive

**API field:** `active` (boolean, default: `true`)

**Description from docs:**
> By default, all parts are *Active*. Marking a part as inactive means it is not available for many actions, but the
> part remains in the database.

**Behavior:**
- All newly created parts are active by default. This is the only attribute that defaults to `true`.
- Marking a part inactive is the recommended approach for obsolete parts that should no longer be used but must not be
  deleted (e.g., for historical traceability on existing orders or build records).
- Inactive parts are excluded from most selection lists (e.g., BOM part selection, purchase order line creation, sales
  order line creation).
- The part record, its historical stock movements, BOM history, and order history are all preserved when a part is made
  inactive.

**UI label:** Active (toggle)

> **Note:** Inactive parts cannot simply be re-activated without review; the recommended workflow is to mark a part
> inactive when it becomes obsolete rather than deleting it.

---

## Virtual

**API field:** `virtual` (boolean, default: `false`)

**Description from docs:**
> A *Virtual* part can be used to represent non-physical items in the InvenTree system. Virtual parts cannot have stock
> items associated with them, as they not physically exist.

**Common use cases:**
- Software licenses
- Labor costs
- Process steps

**Behavior by feature area:**

### Stock Items
Virtual parts cannot have stock items associated with them. User interface elements related to stock items are hidden
when viewing a virtual part.

### Bills of Material
Virtual parts can be added as a subcomponent to the Bills of Material for an assembled part. This can be useful to
represent labor costs, or other non-physical components which are required to build an assembly. Even though the virtual
parts are not allocated during the build process, they are still listed in the BOM and can be included in cost
calculations.

### Build Orders
When creating a Build Order for an assembly which includes virtual parts in its BOM, the virtual parts will be hidden
from the list of required parts. This is because virtual parts do not need to be allocated during the build process.
The parts are still available in the BOM, and the cost of the virtual parts will be included in the total cost of the
build.

### Sales Orders
Virtual parts can be added to Sales Orders in the same way as regular parts. This can be useful to represent services,
or other non-physical items which are being sold. When a sales order is fulfilled, virtual parts will not be allocated
from stock, but they will be included in the order and the total cost. Virtual parts do not need to be allocated during
the fulfillment process, as they do not physically exist.

**Summary:** "Apart from the fact that virtual parts cannot have stock items, they behave in the same way as regular
parts."

**UI label:** Virtual (toggle)

---

## Template (is_template)

**API field:** `is_template` (boolean, default: `false`)

**Description from docs:**
> Is this part a template part?

A Template part is one that can have *variant* parts nested underneath it. Variants represent manufacturing
configurations that customers can order — for example, a product available in different colors, harnesses, enclosures,
or specifications.

**Behavior:**

- Once a part is set as a template, a **Variants** tab appears on the part detail page.
- New variants are created from the Variants tab using "New Variant," which opens a **Duplicate Part** form.
- A variant explicitly references its template part via the `variant_of` API field.

**Serial Numbers:**
> Parts that are linked in a template / variant relationship must have unique serial numbers across all related parts.

This means serial number uniqueness is enforced not just within a single part but across the entire template-variant
family.

**Stock Reporting:**
> The 'stock' for a template part includes stock for all variants under that part.

The template part's stock totals aggregate the stock of all its variants, providing a consolidated view.

**Logical Grouping note:**
> The template / variant relationship is subtly different to the category / part relationship.

Categories provide a hierarchical organizational tree; template/variant is a product configuration relationship where
variants share base specifications with their template.

**UI label:** Template (toggle, located in the Details tab right-hand panel)

> **Note:** The API field name is `is_template`, not `template`. The UI displays it as "Template."

---

## Assembly

**API field:** `assembly` (boolean, default: `false`)

**Description from docs:**
> Can this part be built from other parts?

> If a part is designated as an *Assembly* it can be created (or built) from other component parts. An *Assembly* Part
> has a Bill of Materials (BOM) which lists all the required sub-components.

**Behavior:**
- Enabling Assembly unlocks the **BOM** tab on the part detail page, where BOM line items (sub-components) can be
  added, edited, and deleted.
- Build Orders can be created for assembly parts, consuming BOM components to produce finished goods.
- When the part is also **Locked**, BOM items cannot be created, edited, or deleted.

**UI label:** Assembly (toggle)

---

## Component

**API field:** `component` (boolean, default: `false`)

**Description from docs:**
> Can this part be used to build other parts?

> If a part is designated as a *Component* it can be used as a sub-component of an *Assembly*.

**Behavior:**
- A part flagged as a Component can appear as a BOM line item in any Assembly part's Bill of Materials.
- A part that is not flagged as a Component cannot be added to any BOM.
- There is no restriction preventing the same part from being both an Assembly and a Component simultaneously (i.e.,
  a sub-assembly that is itself built from other parts and is also used inside a higher-level assembly).

**UI label:** Component (toggle)

---

## Trackable

**API field:** `trackable` (boolean, default: `false`)

**Description from docs:**
> Does this part have tracking for unique items?

> Marking a part as *Trackable* modifies how stock items are managed in the database, and also introduces additional
> restrictions and features for that stock item.

**Key constraint:**
> Any stock item associated with a trackable part *must* have either a batch number or a serial number.

This means stock items for trackable parts cannot be created without identifying them via a batch number or serial
number.

**Serial Number Shorthand Notation:**

InvenTree supports a shorthand syntax for entering multiple serial numbers at once:

| Notation    | Meaning                                                               | Example result              |
|-------------|-----------------------------------------------------------------------|-----------------------------|
| `1`         | Single serial number                                                  | `[1]`                       |
| `1,3,5`     | Comma-separated list                                                  | `[1, 3, 5]`                 |
| `1-5`       | Inclusive range                                                       | `[1, 2, 3, 4, 5]`           |
| `~`         | Next available serial number                                          | (next in sequence)          |
| `4+`        | Next 4 consecutive numbers starting from the current next serial     | (4 consecutive from next)   |
| `2+2`       | 2 consecutive numbers starting at 2                                  | `[2, 3]`                    |

Notations can be mixed using whitespace or commas as separators. Example: `1 3-5 9+2` produces `[1, 3, 4, 5, 9, 10, 11]`.
Example: `~+2` produces 3 consecutive numbers starting from the next available serial number.

**Build Orders:** Build orders have extra requirements when either building a trackable part, or when the BOM of the
assembly being built contains trackable component parts.

**UI label:** Trackable (toggle)

---

## Testable

**API field:** `testable` (boolean, default: `false`)

**Description from docs:**
> Can this part have test results recorded against it?

> Testable parts can have test templates defined against the part, allowing test results to be recorded against any
> stock items for that part.

**Behavior:**
- Enabling Testable unlocks the **Test Templates** tab on the part detail page.
- Test templates define the expected tests (e.g., electrical test, visual inspection) with pass/fail criteria.
- Actual test results are recorded against individual stock items (not the part itself), linking test outcomes to
  specific serialized or batched inventory.

**UI label:** Testable (toggle)

---

## Purchaseable

**API field:** `purchaseable` (boolean, default: `false`)

**Description from docs:**
> Can this part be purchased from external suppliers?

> If a part is designated as *Purchaseable* it can be purchased from external suppliers. Setting this flag allows
> parts to be linked to supplier parts and procured via purchase orders.

**Behavior:**
- Enabling Purchaseable allows supplier part records to be created and linked to this part.
- Purchase order line items can reference this part.
- Without this flag, the part cannot appear on a purchase order.

**UI label:** Purchaseable (toggle)

> **Note:** The API field name is `purchaseable` (with an 'e' after 'purchas'). The UI may display it as "Purchasable"
> (without the extra 'e'). Both spellings refer to the same attribute.

---

## Salable

**API field:** `salable` (boolean, default: `false`)

**Description from docs:**
> Can this part be sold to customers?

> If a part is designated as *Salable* it can be sold to external customers. Setting this flag allows parts to be
> added to sales orders.

**Behavior:**
- Enabling Salable allows this part to be added as a line item on sales orders.
- Without this flag, the part cannot appear on a sales order.

**UI label:** Salable (toggle)

---

## Locked

**API field:** `locked` (boolean, default: `false`)

**Description from docs:**
> Locked parts cannot be edited.

> Parts can be locked to prevent them from being modified. This is useful for parts which are in production and should
> not be changed.

**Restrictions applied when a part is locked:**

- Locked parts cannot be deleted.
- BOM items cannot be created, edited, or deleted when they are part of a locked assembly.
- Parameters linked to a locked part cannot be created, edited, or deleted.

**UI label:** Locked (toggle)

> **Note:** Locked is not strictly a classification attribute like the others above — it is a protective flag that
> restricts modification. It interacts most significantly with Assembly parts (BOM lockout).

---

## Attribute Dependencies and Interactions

The following describes known dependencies, constraints, and notable interactions between attributes.

### Assembly + Component (Compatible, Recommended Pattern)

A single part can be both `assembly: true` and `component: true` simultaneously. This is the correct configuration for
a sub-assembly: a part that is built from lower-level components (assembly) and is itself used inside a higher-level
assembly (component).

### Assembly + Locked (Restrictive Interaction)

When a part is both `assembly: true` and `locked: true`:
- The BOM for that assembly becomes read-only.
- BOM items cannot be created, edited, or deleted.
- This protects production-ready assemblies from inadvertent BOM changes.

### Template + Variant Serial Numbers (Uniqueness Constraint)

When `is_template: true`, all variant parts linked via `variant_of` share a serial number namespace. Serial numbers
must be unique across the entire template-variant family, not just within a single variant.

### Template + Stock Aggregation

The stock quantity displayed for a template part includes the aggregate stock of all its variants. There is no
independent stock on the template part itself when variants exist.

### Virtual + Stock (Exclusive)

`virtual: true` parts cannot have any stock items. The stock tab UI elements are hidden for virtual parts. This is an
enforced system constraint, not just a UI hint.

### Trackable + Build Orders (Extra Requirements)

When `trackable: true`, build orders that produce this part (or use it as a BOM component) have additional
requirements — specifically, output stock items must be assigned serial or batch numbers before the build can be
completed.

### Purchaseable / Salable (Independent, Additive)

`purchaseable` and `salable` are fully independent flags. A part can be:
- Both purchaseable and salable (bought and re-sold, or sourced externally and sold as-is).
- Only purchaseable (bought for internal use or builds).
- Only salable (manufactured in-house and sold).
- Neither (purely internal/virtual classification).

### Active + All Other Flags (Overriding Restriction)

Setting `active: false` overrides the functional availability of all other flags. An inactive part cannot be added to
BOMs, purchase orders, sales orders, or build orders regardless of whether its other flags are enabled.

### No Documented Conflict Between Template and Assembly/Component

The official documentation does not specify any restriction preventing a part from being simultaneously a template and
an assembly or component. These combinations are not explicitly prohibited.

---

## API Field Reference

The following boolean fields appear in the Part serializer schema. All are read-write (no `read-only` constraint)
unless noted.

| API Field      | Type    | Default | API Description                                             |
|----------------|---------|---------|-------------------------------------------------------------|
| `active`       | boolean | `true`  | Is this part active?                                        |
| `assembly`     | boolean | `false` | Can this part be built from other parts?                    |
| `component`    | boolean | `false` | Can this part be used to build other parts?                 |
| `is_template`  | boolean | `false` | Is this part a template part?                               |
| `locked`       | boolean | `false` | Locked parts cannot be edited                               |
| `purchaseable` | boolean | `false` | Can this part be purchased from external suppliers?         |
| `salable`      | boolean | `false` | Can this part be sold to customers?                         |
| `testable`     | boolean | `false` | Can this part have test results recorded against it?        |
| `trackable`    | boolean | `false` | Does this part have tracking for unique items?              |
| `virtual`      | boolean | `false` | Is this a virtual part, such as a software product or license? |

Related read-only fields that reflect the effect of these flags at runtime:

| API Field                   | Type            | Notes                                                      |
|-----------------------------|-----------------|------------------------------------------------------------|
| `variant_of`                | integer, nullable | Foreign key to the template part (set on variant parts)  |
| `revision_of`               | integer, nullable | Foreign key for revision relationships                    |
| `in_stock`                  | number (double) | 0 for virtual parts (no stock items allowed)              |
| `total_in_stock`            | number (double) | For template parts, aggregates all variant stock          |
| `variant_stock`             | number (double) | Stock quantity held across variant parts                  |
| `building`                  | number (double) | Non-zero only when assembly=true and a build is active    |
| `allocated_to_build_orders` | number (double) | Non-zero only when component=true and part is in a BOM    |
| `allocated_to_sales_orders` | number (double) | Non-zero only when salable=true and part is on an order   |
