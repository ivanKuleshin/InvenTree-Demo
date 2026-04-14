---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/revision.md
component: parts
topic: Part Detail - Revisions Tab
fetched: 2026-04-14
---

# Part Detail — Revisions Tab

## Table of Contents

- [Overview](#overview)
- [Purpose of Revisions](#purpose-of-revisions)
- [Revisions Are Parts](#revisions-are-parts)
- [Revision Fields](#revision-fields)
- [Revision Restrictions](#revision-restrictions)
- [Global Settings](#global-settings)
- [Creating a Revision](#creating-a-revision)
- [Revision Navigation](#revision-navigation)

## Overview

Part Revisions allow tracking changes to a part over time, particularly useful for complex assemblies that undergo design changes throughout their lifecycle.

## Purpose of Revisions

When creating a complex part (such as an assembly comprised of other parts), it is often necessary to track changes to the part over time. For example, throughout the lifetime of an assembly, it may be necessary to adjust the bill of materials, or update the design of the part.

Rather than overwrite the existing part data, InvenTree allows you to create a new *revision* of the part. This allows you to:
- Track changes to the part over time
- Maintain a history of the part design
- Ensure that any related data entries which refer to the original part (such as stock items, build orders, purchase orders, etc.) are not affected by the change

## Revisions Are Parts

A *revision* of a part is itself a part. This means that each revision of a part has its own:
- Part number
- Stock items
- Parameters
- Bill of materials

The only thing that differentiates a *revision* from any other part is that the *revision* is linked to the original part via the "Revision Of" field.

## Revision Fields

Each part has two fields used to track revisions:

| Field | Description |
| --- | --- |
| Revision | The revision code/number of this part. A user-defined field; can be any string value. |
| Revision Of | A reference to the part of which *this* part is a revision. Used to keep track of the available revisions for a particular part. |

## Revision Restrictions

When creating a new revision of a part, the following restrictions must be adhered to:

| Restriction | Description |
| --- | --- |
| Circular References | A part cannot be a revision of itself |
| Unique Revisions | Each revision of a given part must have a unique revision code |
| Template Revisions | A part which is a template part cannot have revisions (template parts are used to create variants; allowing revisions would create disallowed relationship states) |
| Template References | A part which is a revision of a variant part must point to the same template as the original part |

> **Note:** Variant parts *are* allowed to have revisions, even though template parts are not.

## Global Settings

The following global settings control the behavior of part revisions:

| Name | Description |
| --- | --- |
| PART_ENABLE_REVISION | Enable or disable the part revision feature globally |
| PART_REVISION_ASSEMBLY_ONLY | Restrict revisions to assembly parts only |

## Creating a Revision

To create a new revision for a given part:

1. Navigate to the part detail page
2. Click on the part actions menu (three vertical dots on the top right of the page)
3. Select the "Duplicate Part" action to create a new copy of the selected part
4. The "Duplicate Part" form will open

> **[IMAGE DESCRIPTION]**: A "Duplicate Part" form dialog showing fields for part creation. The "Revision Of" field has a part selector populated with the original part's name and IPN. The "Revision" field contains a text input where the user enters a unique revision code (e.g., "B" or "2"). Other standard part fields (Name, Description, IPN, etc.) are also visible and pre-filled from the original.

5. In the form, make the following updates:
   - Set the *Revision Of* field to the original part (the one being duplicated)
   - Set the *Revision* field to a unique revision code for the new part revision

6. Once changes are made, press **Submit** to create the new part revision

> **[IMAGE DESCRIPTION]**: A Part detail page for a newly created revision (e.g., "Part Revision B"). A "Revision Of" field in the part details section shows a link to the original part. This confirms the revision is correctly linked to its parent.

## Revision Navigation

When multiple revisions exist for a particular part, a *Select Part Revision* drop-down renders at the top of the part page for easy navigation between versions.

> **[IMAGE DESCRIPTION]**: A dropdown selector labeled "Select Part Revision" at the top of a Part detail page. The dropdown lists all available revisions of the part (e.g., "Original", "Rev B", "Rev C") allowing the user to switch between revisions.

> **Note:** The revision selector is only visible when multiple revisions exist for the part.

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/revision.md
