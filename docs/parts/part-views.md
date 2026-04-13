---
source: https://docs.inventree.org/en/stable/part/views/
component: parts
topic: Part Views - Part Detail Page
fetched: 2026-04-13
---

> **Source**: [https://docs.inventree.org/en/stable/part/views/](https://docs.inventree.org/en/stable/part/views/)

# Part Views

## Table of Contents

- [Part Views](#part-views)
- [Category Breadcrumb List](#category-breadcrumb-list)
- [Part Details](#part-details)
- [Part Tabs](#part-tabs)
  - [Variants](#variants)
  - [Stock](#stock)
  - [Allocations](#allocations)
  - [Bill of Materials](#bill-of-materials)
  - [Build Orders](#build-orders)
  - [Used In](#used-in)
  - [Suppliers](#suppliers)
  - [Purchase Orders](#purchase-orders)
  - [Sales Orders](#sales-orders)
  - [Stock History](#stock-history)
  - [Test Templates](#test-templates)
  - [Related Parts](#related-parts)
  - [Parameters](#parameters)
  - [Attachments](#attachments)
  - [Notes](#notes)

---

## Part Views

The Part detail view page provides a detailed view of a single part in the system. The page is divided into several sections, which are described in this document.

### Category Breadcrumb List

The categories of each part is displayed on the top navigation bar as shown in the above screenshot. [Click here](./part-overview.md#part-category) for more information about categories.

## Part Details

Details provides information about the particular part. Parts details can be displayed in the header panel clicking on "Show Part Details" toggle button.

> **[IMAGE DESCRIPTION]**: Screenshot of the Part Overview page in InvenTree (image: part/part_overview.png, alt: "Part Overview"). The header panel shows the part image on the left, and part metadata on the right including fields such as IPN (Internal Part Number), Name, Description, Revision, Keywords, External Link, Creation Date, and Units. A "Show Part Details" toggle button is visible. The category breadcrumb navigation is shown at the top of the page. Below the header, tabs are visible for navigating to different sections such as Stock, BOM, Build Orders, etc.

A Part is defined in the system by the following parameters:

**Internal Part Number (IPN)** - A special code which can be used to link a part to a numbering system. The IPN field is not required, but may be useful where a part numbering system has been defined.

**Name** - The Part name is a simple (unique) text label

**Description** - Longer form text field describing the Part

**Revision** - An optional revision code denoting the particular version for the part. Used when there are multiple revisions of the same master part object. Read [more about part revisions here](./revision.md).

**Keywords** - Optional few words to describe the part and make the part search more efficient.

**External Link** - An external URL field is provided to link to an external page. This could be useful if the part has extra documentation located on an external server.

**Creation Date** - Indicates when the part was created and by which user (label on right-hand side)

**Units** - Units of measure (UoM) for this Part. The default is 'pcs'

## Part Tabs

The Part view page organizes part data into sections, displayed as tabs. Each tab has its own function, which is described in this section.

### Variants

If a part is a _Template Part_ then the _Variants_ tab will be visible.

[Read about Part templates and variants](./part-template.md).

### Stock

The _Stock_ tab shows all the stock items for the selected _Part_. The user can quickly determine how many parts are in stock, where they are located, and the status of each _Stock Item_.

> **[IMAGE DESCRIPTION]**: Screenshot of the Part Stock tab in InvenTree (image: part/part_stock.png, alt: "Part Stock"). The tab displays a table listing all stock items for the selected part. Columns include quantity, location, batch number, serial number (if applicable), status, and other stock-related fields. Toolbar buttons are visible for exporting data, creating a new stock item, and performing stock actions on selected rows.

#### Functions

The following functions are available from the _Part Stock_ view.

##### Export

Exports the stocktake data for the selected Part. Launches a dialog to select export options, and then downloads a file containing data for all stock items for this Part.

##### New Stock Item

Launches a dialog to create a new _Stock Item_ for the selected _Part_.

##### Stock Actions

If stock items are selected in the table, stock actions are enabled via the drop-down menu.

### Allocations

The _Allocated_ tab displays how many units of this part have been allocated to pending build orders and/or sales orders. This tab is only visible if the Part is a _component_ (meaning it can be used to make assemblies), or it is _salable_ (meaning it can be sold to customers).

### Bill of Materials

The _BOM_ tab displays the [Bill of Materials](../manufacturing/bom.md) - a list of sub-components used to build an assembly. Each row in the BOM specifies a quantity of another Part which is required to build the assembly. This tab is only visible if the Part is an _assembly_ (meaning it can be built from other parts).

### Build Orders

The _Build Orders_ tab shows a list of the builds for this part. It provides a view for important build information like quantity, status, creation and completion dates.

### Used In

The _Used In_ tab displays a list of other parts that this part is used to make. This tab is only visible if the Part is a _component_.

### Suppliers

The _Suppliers_ tab displays all the _Part Suppliers_ and _Part Manufacturers_ for the selected _Part_.

This tab is only visible if the _Part_ is designated as _Purchaseable_.

> **[IMAGE DESCRIPTION]**: Screenshot of the Part Suppliers and Manufacturers tab in InvenTree (image: part/part_manufacturers_suppliers.png, alt: "Part Suppliers and Manufacturers"). The tab shows two sections or sub-tables: one listing all supplier parts (each row showing the supplier name, supplier SKU, packaging, price breaks, etc.) and another showing manufacturer parts (manufacturer name, MPN - manufacturer part number, etc.). Action buttons for adding new supplier/manufacturer entries are visible in the toolbar.

### Purchase Orders

The _Part Purchase Orders_ tab lists all the Purchase Orders against the selected part.

This tab is only displayed if the part is marked as _Purchaseable_.

### Sales Orders

The _Sales Orders_ tab shows a list of the sales orders for this part. It provides a view for important sales order information like customer, status, creation and shipment dates.

### Stock History

The _Stock History_ tab provides historical stock level information. Refer to the [stock history documentation](./stocktake.md) for further information.

### Test Templates

If a part is marked as _testable_, the user can define tests which must be performed on any stock items which are instances of this part. [Read more about testing](./part-test-templates.md).

### Related Parts

Related Part denotes a relationship between two parts, when users want to show their usage is "related" to another part or simply emphasize a link between two parts.

Related parts can be added and are shown under a table of the same name in the "Part" view:

> **[IMAGE DESCRIPTION]**: Screenshot of the Related Parts section within the Part view in InvenTree (image: part/part_related.png, alt: "Related Parts Example View"). A table is displayed showing parts that have been manually linked as "related" to the current part. Each row shows the related part's name, description, category, and stock level. Buttons are available to add a new related part relationship or remove existing ones.

This feature can be enabled or disabled in the global part settings.

### Parameters

Parts can have multiple defined parameters.

[Read about parameters](../concepts/parameters.md).

### Attachments

The _Part Attachments_ tab displays file attachments associated with the selected _Part_. Multiple file attachments (such as datasheets) can be uploaded for each _Part_.

[Read about attachments](../concepts/attachments.md).

### Notes

A part may have notes attached, which support markdown formatting.
