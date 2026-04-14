---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/template.md
component: parts
topic: Part Detail - Variants Tab
fetched: 2026-04-14
---

# Part Detail — Variants Tab

## Table of Contents

- [Overview](#overview)
- [Visibility Condition](#visibility-condition)
- [Template Parts](#template-parts)
- [Purposes of Template Parts](#purposes-of-template-parts)
- [Template / Variant Capabilities](#template--variant-capabilities)
- [Enable Template Part](#enable-template-part)
- [Create a Variant](#create-a-variant)
- [Variant Behaviors](#variant-behaviors)

## Overview

The *Variants* tab is visible on a Part detail page when the part is designated as a *Template Part*. It shows all variant parts derived from that template.

## Visibility Condition

The *Variants* tab is only visible if the part is a *Template Part*.

## Template Parts

Any part can be set as a Template part. Template parts can hold information that can be reused across "Variants".

### Purposes of Template Parts

- A template part could be used to create a base variant of an assembly which can be derived from, with BOM changes for instance.
- Variants can be used as "manufacturing variants" where the variant dictates a particular configuration which a customer can order: a variant might determine the particular options that come with a part, like harnesses, enclosure, color, specs, etc.

"Variants" parts reference the "Template" part, thereby explicitly creating and showing a direct relationship.

## Template / Variant Capabilities

### Serial Numbers

Parts that are linked in a template / variant relationship must have unique serial numbers. For example, if you have a template part Widget, and two variants Widget-01 and Widget-02, then any assigned serial numbers must be unique across all these variants.

### Stock Reporting

The "stock" for a template part includes stock for all variants under that part. This means viewing stock on the template part shows the combined stock across all its variants.

### Logical Grouping

The template / variant relationship is subtly different to the category / part relationship. It creates an explicit hierarchical design relationship between parts.

## Enable Template Part

Any part can be set as "Template" part. To do so:

1. Navigate to a specific part detail page
2. Click on the "Details" tab
3. Locate the part options on the right-hand side
4. Toggle the `Template` option so it shows green (slider to the right)

## Create a Variant

When a part's Template option is turned on, "Variants" of this part can be created.

To create a variant:
1. Navigate to a specific part detail page
2. Click on the "Variants" tab
3. Click on the "New Variant" button
4. The `Duplicate Part` form will be displayed
5. Fill out the form
6. Click **Submit** to create the variant

## Variant Behaviors

### BOM Inheritance

BOM line items can be marked as *Inherited* on a template part. Inherited BOM items automatically propagate to all variant BOMs. See the BOM tab documentation for full details.

### Test Template Cascading

Test templates defined on a template part cascade to all variant parts. Any stock items of the variant parts will have the same test templates associated with them.

### Revision Compatibility

Revisions of variant parts are allowed. A revision of a variant part must reference the same template as the original variant.

> **Note:** Template parts themselves cannot have revisions. Only variant (and non-template) parts can have revisions.

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/template.md
