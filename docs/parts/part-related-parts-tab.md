---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
component: parts
topic: Part Detail - Related Parts Tab
fetched: 2026-04-14
---

# Part Detail — Related Parts Tab

## Table of Contents

- [Overview](#overview)
- [Related Part Concept](#related-part-concept)
- [User Interface](#user-interface)
- [Feature Availability](#feature-availability)
- [Managing Related Parts](#managing-related-parts)

## Overview

The *Related Parts* tab on the Part detail page displays parts that have been designated as related to the current part.

## Related Part Concept

Related Part denotes a relationship between two parts, when users want to show their usage is "related" to another part or simply emphasize a link between two parts.

Related parts is a bidirectional association — it does not imply a hierarchical or dependency relationship (unlike BOM items or variants). It is a user-defined informational link between parts.

Common use cases:
- Indicating that two parts are interchangeable or functionally similar
- Highlighting that parts are commonly used together
- Drawing attention to alternative or substitute parts
- Linking replacement parts to obsolete parts

## User Interface

Related parts can be added and are shown under a table of the same name in the "Part" view.

> **[IMAGE DESCRIPTION]**: A "Related Parts Example View" showing a table labeled "Related Parts" within the Part detail page. The table lists related parts with columns for part name, IPN (Internal Part Number), description, and category. An "Add Related Part" button is visible above the table. Each row has a remove/delete action icon.

## Feature Availability

The Related Parts feature can be enabled or disabled in the global part settings. If disabled, the Related Parts tab will not be visible on part detail pages.

## Managing Related Parts

### Adding a Related Part

1. Navigate to the Part detail page
2. Click on the "Related Parts" tab
3. Click the "Add Related Part" button
4. Select the part to relate in the part selector
5. Confirm to create the relationship

### Removing a Related Part

1. Navigate to the Related Parts tab
2. Find the related part entry in the table
3. Click the delete/remove action for that row

> **Note:** Removing a related part relationship does not delete either part — it only removes the association between them.

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/views.md
