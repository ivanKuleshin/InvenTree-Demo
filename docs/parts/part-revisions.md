---
source: https://docs.inventree.org/en/stable/part/revision/
component: parts
topic: Part Revisions
fetched: 2026-04-14
---

> **Source**: [https://docs.inventree.org/en/stable/part/revision/](https://docs.inventree.org/en/stable/part/revision/)

# Part Revisions

## Table of Contents

- [Part Revisions](#part-revisions)
  - [Revisions are Parts](#revisions-are-parts)
  - [Revision Fields](#revision-fields)
  - [Revision Restrictions](#revision-restrictions)
- [Revision Settings](#revision-settings)
- [Create a Revision](#create-a-revision)
- [Revision Navigation](#revision-navigation)
- [API Support for Revisions](#api-support-for-revisions)

---

## Part Revisions

When creating a complex part (such as an assembly comprised of other parts), it is often necessary to track changes to the part over time. For example, throughout the lifetime of an assembly, it may be necessary to adjust the bill of materials, or update the design of the part.

Rather than overwrite the existing part data, InvenTree allows you to create a new *revision* of the part. This allows you to track changes to the part over time, and maintain a history of the part design.

Crucially, creating a new *revision* ensures that any related data entries which refer to the original part (such as stock items, build orders, purchase orders, etc) are not affected by the change.

### Revisions are Parts

A *revision* of a part is itself a part. This means that each revision of a part has its own part number, stock items, parameters, bill of materials, etc. The only thing that differentiates a *revision* from any other part is that the *revision* is linked to the original part.

### Revision Fields

Each part has two fields which are used to track the revision of the part:

- **Revision**: The revision number of the part. This is a user-defined field, and can be any string value.
- **Revision Of**: A reference to the part of which *this* part is a revision. This field is used to keep track of the available revisions for any particular part.

The API schema exposes these as:

| API Field        | Type    | Constraints                          | Description                                   |
|------------------|---------|--------------------------------------|-----------------------------------------------|
| `revision`       | string  | nullable; max length: 100; default: `""` | The revision code (user-defined string)   |
| `revision_of`    | integer | nullable                             | FK to the part of which this is a revision    |
| `revision_count` | integer | read-only, nullable                  | Number of revisions linked to this part       |

### Revision Restrictions

When creating a new revision of a part, there are some restrictions which must be adhered to:

- **Circular References**: A part cannot be a revision of itself. This would create a circular reference which is not allowed.
- **Unique Revisions**: A part cannot have two revisions with the same revision number. Each revision (of a given part) must have a unique revision code.
- **Template Revisions**: A part which is a [template part](./part-template.md) cannot have revisions. This is because the template part is used to create variants, and allowing revisions of templates would create disallowed relationship states in the database. However, variant parts are allowed to have revisions.
- **Template References**: A part which is a revision of a variant part must point to the same template as the original part. This is to ensure that the revision is correctly linked to the original part.

---

## Revision Settings

The following global settings are available to control the behavior of part revisions:

| Setting Name                    | Display Name            | Description                             | Default | Type    |
|---------------------------------|-------------------------|-----------------------------------------|---------|---------|
| `PART_ENABLE_REVISION`          | Part Revisions          | Enable revision field for Part          | `True`  | boolean |
| `PART_REVISION_ASSEMBLY_ONLY`   | Assembly Revision Only  | Only allow revisions for assembly parts | `False` | boolean |

> **Note:** When `PART_REVISION_ASSEMBLY_ONLY` is `True`, revision fields are only shown and enforced for parts marked as assemblies.

---

## Create a Revision

To create a new revision for a given part, navigate to the part detail page, and click on the part actions menu (three vertical dots on the top right of the page).

Select the "Duplicate Part" action, to create a new copy of the selected part. This will open the "Duplicate Part" form.

> **[IMAGE DESCRIPTION]**: Screenshot of the "Duplicate Part" / "Create part revision" form in InvenTree (image: part/part_create_revision.png). The form dialog shows standard part creation fields pre-populated from the source part. The key fields to modify are: "Revision Of" (a part selector field pointing to the original part being duplicated) and "Revision" (a text field for entering the new unique revision code, e.g. "B" or "2"). Other fields such as Name, Description, IPN, Category are also present and editable. A "Submit" button completes the creation.

In this form, make the following updates:

1. Set the *Revision Of* field to the original part (the one that you are duplicating)
2. Set the *Revision* field to a unique revision number for the new part revision

Once these changes (and any other required changes) are made, press *Submit* to create the new part.

Once the form is submitted (without any errors), you will be redirected to the new part revision.

> **[IMAGE DESCRIPTION]**: Screenshot of a part detail page for revision "B" in InvenTree (image: part/part_revision_b.png). The part header panel shows the part name and metadata. A "Revision Of" field is visible, displaying a link back to the original (parent) part. The revision code "B" is shown in the Revision field. This confirms that the new part is correctly linked to the original part as a subsequent revision.

---

## Revision Navigation

When multiple revisions exist for a particular part, you can navigate between revisions using the *Select Part Revision* drop-down which renders at the top of the part page.

> **[IMAGE DESCRIPTION]**: Screenshot showing the "Select Part Revision" drop-down at the top of a part detail page in InvenTree (image: part/part_revision_select.png). The drop-down is positioned prominently near the top of the page, above the part detail header. It lists all available revisions for the part (e.g., the original part and revisions "A", "B", etc.) and allows the user to jump directly to any revision's detail page.

> **Note:** The revision selector is only visible when multiple revisions exist for the part.

---

## API Support for Revisions

The parts list API (`GET /api/part/`) supports revision-related query parameters:

| Parameter       | Type    | Description                                             |
|-----------------|---------|---------------------------------------------------------|
| `is_revision`   | boolean | Filter to parts that are a revision of another part     |
| `has_revisions` | boolean | Filter to parts that have one or more revisions         |
| `revision_of`   | integer | Filter to revisions of a specific parent part (by ID)   |

The `revision` and `revision_count` fields are also available as ordering fields when sorting part list results.

> **Note:** There is no dedicated "promote revision" API endpoint or workflow in InvenTree. Revisions are independent parts linked via the `revision_of` FK. Managing which revision is "current" or "active" is done by controlling the `active` field on each part independently.
