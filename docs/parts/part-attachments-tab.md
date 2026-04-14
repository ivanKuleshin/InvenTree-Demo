---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/concepts/attachments.md
component: parts
topic: Part Detail - Attachments Tab
fetched: 2026-04-14
---

# Part Detail — Attachments Tab

## Table of Contents

- [Overview](#overview)
- [Attachment Concept](#attachment-concept)
- [Purpose and Scope](#purpose-and-scope)
- [User Interface](#user-interface)
- [Supported Object Types](#supported-object-types)

## Overview

The *Part Attachments* tab displays file attachments associated with the selected Part. Multiple file attachments (such as datasheets) can be uploaded for each Part.

## Attachment Concept

An *attachment* is a file which has been uploaded and linked to a specific object within InvenTree. Attachments serve as supplementary documentation for parts and other objects.

> "Attachments are not used for any core business logic within InvenTree. They are intended to provide additional metadata for objects, which can be useful for documentation, reference, or reporting purposes."

## Purpose and Scope

Attachments enable users to store additional documentation, images, and relevant files associated with various InvenTree models.

Common use cases for part attachments:
- Datasheets
- CAD drawings or design files
- Test reports
- Specifications or standards documents
- Photos of the physical component
- Manufacturer documentation

## User Interface

Objects that support attachments feature an "Attachments" tab on their detail pages, which displays all linked files for that specific object.

> **[IMAGE DESCRIPTION]**: An "Attachments" tab view on a Part detail page showing a table of uploaded files. Columns include file name, comment/description, upload date, and uploaded by (user). An "Add Attachment" button is visible at the top of the table. Each row has a download link and a delete action.

From the Attachments tab, users can:
- Upload new file attachments
- View existing attachments with metadata (filename, description, date, uploader)
- Download attached files
- Delete attachments

## Supported Object Types

Attachments can be associated with multiple InvenTree model types, including:
- Parts
- Stock Items
- Build Orders
- Purchase Orders
- Sales Orders

This makes the attachment feature versatile across the entire system.

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/concepts/attachments.md
