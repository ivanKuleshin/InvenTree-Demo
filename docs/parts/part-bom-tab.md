---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/manufacturing/bom.md
component: parts
topic: Part Detail - BOM Tab
fetched: 2026-04-14
---

# Part Detail — BOM Tab

## Table of Contents

- [Overview](#overview)
- [Visibility Condition](#visibility-condition)
- [BOM Line Items](#bom-line-items)
- [BOM Line Item Properties](#bom-line-item-properties)
- [Consumable BOM Line Items](#consumable-bom-line-items)
- [Optional BOM Line Items](#optional-bom-line-items)
- [Substitute BOM Line Items](#substitute-bom-line-items)
- [Inherited BOM Line Items](#inherited-bom-line-items)
- [BOM Creation](#bom-creation)
- [Multi Level BOMs](#multi-level-boms)
- [BOM Validation](#bom-validation)
- [Required Quantity Calculation](#required-quantity-calculation)

## Overview

A Bill of Materials (BOM) defines the list of component parts required to make an assembly, create builds, and allocate inventory.

A part which can be built from other sub components is called an *Assembly*.

> **[IMAGE DESCRIPTION]**: A flat BOM table view labeled "Flat BOM Table" showing a list of component parts required to build an assembly. Columns include component part name/IPN, reference, quantity, attrition percentage, and notes. Each row is a BOM line item. Action icons allow editing or deleting individual line items.

## Visibility Condition

The *BOM* tab is only visible if the Part is designated as an **assembly** (meaning it can be built from other parts).

## BOM Line Items

A BOM for a particular assembly is comprised of a number (zero or more) of BOM "Line Items".

## BOM Line Item Properties

| Property | Description |
| --- | --- |
| Part | A reference to another Part object which is required to build this assembly |
| Reference | Optional reference field to describe the BOM Line Item, e.g. part designator |
| Quantity | The quantity of Part required for the assembly |
| Attrition | Estimated attrition losses for a production run. Expressed as a percentage of the base quantity (e.g. 2%) |
| Setup Quantity | An additional quantity of the part required to account for fixed setup losses during the production process. Added to the base quantity of the BOM line item |
| Rounding Multiple | A value which indicates that the required quantity should be rounded up to the nearest multiple of this value |
| Consumable | A boolean field indicating whether this BOM Line Item is *consumable* |
| Inherited | A boolean field indicating whether this BOM Line Item will be "inherited" by BOMs for parts which are a variant (or sub-variant) of the part for which this BOM is defined |
| Optional | A boolean field indicating if this BOM Line Item is "optional" |
| Note | Optional note field for additional information |

## Consumable BOM Line Items

If a BOM line item is marked as *consumable*, this means that while the part and quantity information is tracked in the BOM, this line item does not get allocated to a Build Order. This may be useful for certain items that the user does not wish to track through the build process, as they may be low value, in abundant stock, or otherwise complicated to track.

> **[IMAGE DESCRIPTION]**: A BOM table showing a "Consumable BOM Item" example. The "Wood Screw" line item is visually marked as consumable (e.g., with an icon or label). The assembly is a "Table" requiring 12 screws per unit. The consumable marking indicates the screws will not be allocated or tracked through the build process.

In the Build Order stock allocation table, consumable line items cannot be allocated, as they are *consumable*.

## Optional BOM Line Items

If a BOM line item is marked as *optional*, this means that the part and quantity information is tracked in the BOM, but this line item is not required to be allocated to a Build Order. This may be useful for certain items which are not strictly required for the build process to be completed.

When completing a Build Order, the user can choose whether to include optional items in the build process or not:
- If optional items are **included**, they will be allocated to the Build Order as normal.
- If optional items are **excluded**, they will not be allocated to the Build Order, and the build process can be completed without them.

## Substitute BOM Line Items

Where alternative parts can be used when building an assembly, these parts are assigned as *Substitute* parts in the Bill of Materials. A particular line item may have multiple substitute parts assigned to it. When allocating stock to a Build Order, stock items associated with any of the substitute parts may be allocated against the particular line item.

> **Note:** When calculating the *available quantity* of a particular line item in a BOM, stock quantities associated with substitute parts are included in the calculation.

> **[IMAGE DESCRIPTION]**: An "Edit BOM Item Substitutes" dialog form showing a list of substitute parts for a selected BOM line item. A part selector dropdown and an "Add Substitute" button are visible. Existing substitutes are listed with a remove action.

### Adding a Substitute for a BOM Item

To manually add a substitute for a BOM item, click on the transfer icon in the *Actions* column. The `Edit BOM Item Substitutes` form will be displayed. Select a part in the list and click on "Add Substitute" button to confirm.

## Inherited BOM Line Items

When using the InvenTree template / variant feature, it may be useful to make use of the *inheritance* capability of BOM Line Items.

If a BOM Line Item is designated as *Inherited*, it will be automatically included in the BOM of any part which is a variant (or sub-variant) of the part for which the BOM Line Item is defined.

This is particularly useful if a template part is defined with the "common" BOM items which exist for all variants of that template.

> **[IMAGE DESCRIPTION]**: A diagram labeled "Inherited BOM Line Items" showing a tree structure. Template Part A has BOM items A1 (inherited) and A2 (not inherited). Variant B inherits A1 and defines B1 (also inherited). Variant C inherits A1 and defines C1. Variant D inherits A1 from A, B1 from B, and defines D1. Variant E follows the same pattern.

### Inheritance Rules

- *A1* is inherited by all variant parts underneath *Template Part A*
- *A2* is not inherited, and is only included in the BOM for *Template Part A*
- Inherited BOM Line Items only flow "downwards" in the variant inheritance chain
- Parts higher up the variant chain cannot inherit BOM items from child parts

> **Note:** When editing an inherited BOM Line Item for a template part, the changes are automatically reflected in the BOM of any variant parts.

## BOM Creation

BOMs can be created manually by adjusting individual line items, or by uploading (importing) an existing BOM file.

### Importing a BOM

BOM data can be imported from an existing file (such as CSV or Excel) from the *BOM* panel for a particular part/assembly. This process is a special case of the more general data import process.

At the top of the *BOM* panel, click on the import icon to open the import dialog.

### Add BOM Item

To manually add a BOM item:

1. Navigate to the part/assembly detail page.
2. Click on the *BOM* panel tab.
3. Click on the edit icon at the top of the *BOM* view.
4. After the page reloads, click on the plus-circle icon.
5. The `Create BOM Item` form will be displayed.

> **[IMAGE DESCRIPTION]**: A "Create BOM Item Form" dialog with fields for Part (part selector), Reference (text), Quantity (number), Attrition (percentage), Optional (checkbox), Inherited (checkbox), Consumable (checkbox), and Note (text). A Submit button is at the bottom.

6. Fill out the required fields then click **Submit** to add the BOM item to this part's BOM.

## Multi Level BOMs

Multi-level (hierarchical) BOMs are natively supported by InvenTree. A Bill of Materials can contain sub-assemblies which themselves have a defined BOM. This can continue for an unlimited number of levels.

## BOM Validation

InvenTree maintains a "validated" flag for each assembled part. When set, this flag indicates that the production requirements for this part have been validated, and that the BOM has not been changed since the last validation.

A BOM "checksum" is stored against each part, which is a hash of the BOM line items associated with that part. This checksum is used to determine whether the BOM has changed since the last validation. Whenever a BOM line item is created, adjusted, or deleted, any assemblies which are associated with that BOM must be validated.

### BOM Checksum Fields

The following BOM item fields are used when calculating the BOM checksum:

- *Assembly ID* — The unique identifier of the assembly associated with the BOM line item
- *Component ID* — The unique identifier of the component part associated with the BOM line item
- *Reference* — The reference field of the BOM line item
- *Quantity* — The quantity of the component part required for the assembly
- *Attrition* — The attrition percentage of the BOM line item
- *Setup Quantity* — The setup quantity of the BOM line item
- *Rounding Multiple* — The rounding multiple of the BOM line item
- *Consumable* — Whether the BOM line item is consumable
- *Inherited* — Whether the BOM line item is inherited
- *Optional* — Whether the BOM line item is optional
- *Allow Variants* — Whether the BOM line item allows variants

If any of these fields are changed, the BOM checksum is recalculated, and any assemblies associated with the BOM are marked as "not validated". The user must then manually revalidate the BOM.

### BOM Validation Status

To view the "validation" status of an assembled part, navigate to the "Bill of Materials" tab of the part detail page. The validation status is displayed at the top of the BOM table.

> **[IMAGE DESCRIPTION]**: A "BOM Validation Status" indicator at the top of the BOM table showing a green checkmark or badge labeled "Validated" when the BOM is valid.

> **[IMAGE DESCRIPTION]**: A "BOM Not Validated" state showing an orange/red status indicator labeled "Not Validated" along with a "Validate BOM" button that allows the user to revalidate.

## Required Quantity Calculation

When a new Build Order is created, the required production quantity of each component part is calculated based on the BOM line items.

### Base Quantity

```
Required Quantity = Base Quantity * Number of Assemblies
```

### Attrition

If a non-zero attrition percentage is specified, it is applied to the calculated Required Quantity:

```
Required Quantity = Required Quantity * (1 + Attrition Percentage)
```

> **Note:** The attrition percentage is optional. If not specified, it defaults to 0%.

### Setup Quantity

```
Required Quantity = Required Quantity + Setup Quantity
```

> **Note:** The setup quantity is optional. If not specified, it defaults to 0.

### Rounding Multiple

```
Required Quantity = ceil(Required Quantity / Rounding Multiple) * Rounding Multiple
```

> **Note:** The rounding multiple is optional. If not specified, no rounding is applied.

### Example Calculation

Given a BOM line item with:
- Base Quantity: 3
- Attrition: 2% (0.02)
- Setup Quantity: 10
- Rounding Multiple: 25

For 100 assemblies:

```
Required Quantity = 3 * 100 = 300

Attrition Value = 300 * 0.02 = 6

Required Quantity = 300 + 6 = 306

Required Quantity = 306 + 10 = 316

Required Quantity = ceil(316 / 25) * 25 = 13 * 25 = 325
```

Final required production quantity: **325**

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/manufacturing/bom.md
