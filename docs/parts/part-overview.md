---
source: https://docs.inventree.org/en/stable/part/
component: parts
topic: Part Overview and Attributes
fetched: 2026-04-13
---

> **Source**: [https://docs.inventree.org/en/stable/part/](https://docs.inventree.org/en/stable/part/)

# Part Overview

## Table of Contents

- [Part](#part)
- [Part Category](#part-category)
- [Part Attributes](#part-attributes)
  - [Virtual](#virtual)
  - [Template](#template)
  - [Assembly](#assembly)
  - [Component](#component)
  - [Testable](#testable)
  - [Trackable](#trackable)
  - [Purchaseable](#purchaseable)
  - [Salable](#salable)
- [Locked Parts](#locked-parts)
- [Active Parts](#active-parts)
- [Units of Measure](#units-of-measure)
- [Part Images](#part-images)
- [Part Import](#part-import)

---

## Part

The *Part* is the core element of the InvenTree ecosystem. A Part object is the archetype of any stock item in your inventory. Parts are arranged in hierarchical categories which are used to organize and filter parts by function.

## Part Category

Part categories are very flexible and can be easily arranged to match a particular user requirement. Each part category displays a list of all parts *under* that given category. This means that any part belonging to a particular category, or belonging to a sub-category, will be displayed.

Each part category also shows a list of sub-categories which exist underneath it.

> **[IMAGE DESCRIPTION]**: Screenshot showing a Part Category view in InvenTree (image: part/part_category.png, alt: "Part category"). The view displays a list of parts that belong to a specific category, with columns for part name, description, image thumbnail, category, and stock level. A sub-category list is visible, showing the hierarchical nesting of categories. Navigation breadcrumbs are displayed at the top.

The category part list provides an overview of each part:

- Part name and description
- Part image thumbnail
- Part category
- Part stock level

The list of parts underneath a given category can be filtered by multiple user-configurable filters, which is especially useful when a large number of parts exist under a certain category.

Clicking on the part name links to the [*Part Detail*](./part-views.md) view.

## Part Attributes

Each *Part* defined in the database provides a number of different attributes which determine how that part can be used. Configuring these attributes for a given part will impact the available functions that can be performed on (or using) that part.

### Virtual

A *Virtual* part is one which does not physically exist but should still be tracked in the system. This could be a process step, machine time, software license, etc. Refer to the [virtual parts documentation](./part-virtual.md) for more information.

### Template

A *Template* part is one which can have *variants* which exist underneath it. [Read further information about template parts here](./part-template.md).

### Assembly

If a part is designated as an *Assembly* it can be created (or built) from other component parts. As an example, a circuit board assembly is made using multiple electronic components, which are tracked in the system. An *Assembly* Part has a Bill of Materials (BOM) which lists all the required sub-components. [Read further information about BOM management here](../manufacturing/bom.md).

### Component

If a part is designated as a *Component* it can be used as a sub-component of an *Assembly*. [Read further information about BOM management here](../manufacturing/bom.md)

### Testable

Testable parts can have test templates defined against the part, allowing test results to be recorded against any stock items for that part. For more information on testing, refer to the [testing documentation](./part-test-templates.md).

### Trackable

Trackable parts can be assigned batch numbers or serial numbers which uniquely identify a particular stock item. Trackable parts also provide other features (and restrictions) in the InvenTree ecosystem.

[Read further information about trackable parts here](./part-trackable.md).

### Purchaseable

If a part is designated as *Purchaseable* it can be purchased from external suppliers. Setting this flag allows parts be linked to supplier parts and procured via purchase orders.

#### Suppliers

A Supplier is an external vendor who provides goods or services.

#### Supplier Parts

Purchaseable parts can be linked to Supplier Parts. A supplier part represents an individual piece or unit that is procured from an external vendor.

#### Purchase Orders

A Purchase Order allows parts to be ordered from an external supplier.

### Salable

If a part is designated as *Salable* it can be sold to external customers. Setting this flag allows parts to be added to sales orders.

## Locked Parts

Parts can be locked to prevent them from being modified. This is useful for parts which are in production and should not be changed. The following restrictions apply to parts which are locked:

- Locked parts cannot be deleted
- BOM items cannot be created, edited, or deleted when they are part of a locked assembly
- Parameters linked to a locked part cannot be created, edited or deleted

## Active Parts

By default, all parts are *Active*. Marking a part as inactive means it is not available for many actions, but the part remains in the database. If a part becomes obsolete, it is recommended that it is marked as inactive, rather than deleting it from the database.

## Units of Measure

Each type of part can define a custom "unit of measure" which is a standardized unit which is used to track quantities for a particular part. By default, the "unit of measure" for each part is blank, which means that each part is tracked in dimensionless quantities of "pieces".

> **Note:** Read more on how InvenTree supports [physical units of measure](../concepts/units.md).

### Physical Units

It is possible to track parts using physical quantity values, such as *metres* or *litres*. For example, it would make sense to track a "wire" in units of "metres":

> **[IMAGE DESCRIPTION]**: Screenshot showing the Part units configuration in InvenTree (image: part/part_units.png, alt: "Part units"). A part is displayed with a unit of measure field set to "m" (metres). The interface shows how InvenTree supports physical unit tracking for a part, with the unit visible in the part detail header and stock quantity fields.

### Supplier Part Units

By default, units of measure for supplier parts are specified in the same unit as their base part. However, supplier part units can be changed to any unit *which is compatible with the base unit*.

> **Note:** Example: If the base part has a unit of `metres` then valid units for any supplier parts would include `feet`, `cm`, `inches` (etc)

If an incompatible unit type is specified, an error will be displayed:

> **[IMAGE DESCRIPTION]**: Screenshot showing an error message in InvenTree when an invalid/incompatible unit type is entered for a supplier part (image: part/part_units_invalid.png, alt: "Invalid supplier part units"). The error message appears inline or as a dialog, indicating that the selected unit is not compatible with the base part unit.

## Part Images

Each part can have an associated image, which is used for display purposes throughout the InvenTree interface. A prominent example is on the part detail page itself:

> **[IMAGE DESCRIPTION]**: Screenshot of the Part detail page in InvenTree showing a part image prominently displayed (image: part/part_image_example.png, alt: "Part image example"). The image appears alongside the part name, IPN, description, and other key fields. The image is displayed at a medium size, serving as a visual identifier for the part.

### Image Thumbnails

Thumbnail images are also used throughout the interface, particularly in table views, to reduce data load on the server. These thumbnail images are generated automatically when a new part image is uploaded.

### Uploading Part Image

#### Web Interface

In the web interface, part images can be uploaded directly from the [part view](./part-views.md). Hover the mouse cursor over the Part image to reveal multiple options:

> **[IMAGE DESCRIPTION]**: Screenshot showing the part image upload hover interaction in InvenTree (image: part/part_image_upload.png, alt: "Part image upload"). When hovering over the part image in the part detail view, a set of overlay icons or buttons appears, providing options to upload a new image, select from existing images, or delete the current image.

| Action | Description |
| --- | --- |
| Upload new image | Select an image file from your local computer to associate with the selected part |
| Select from existing images | Select an image from a list of part images which already exist in the database |
| Delete image | Remove the associated image from the selected part |

#### API

Image upload is supported via the [InvenTree API](../api/index.md), allowing images to be associated with parts programmatically. Further, this means that the [Python interface](../api/python/index.md) also supports image upload.

#### Mobile App

The [InvenTree mobile app](../app/part.md#part-image-view) allows part images to be directly uploaded, either from stored files or integrated camera.

## Part Import

*Parts* can be imported by staff-members on the part-list-view (this feature must be enabled in the part-settings), in the part-settings or on the [admin-page for parts](../settings/import.md) (only accessible if you are also an admin). The first two options provide a multi-stage wizard that enables mapping fields from various spreadsheet or table-data formats while the latter requires a well-formatted file but is much more performant.
