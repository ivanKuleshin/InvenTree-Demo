---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/concepts/parameters.md
component: parts
topic: Part Detail - Parameters Tab
fetched: 2026-04-14
---

# Part Detail — Parameters Tab

## Table of Contents

- [Overview](#overview)
- [Core Concept](#core-concept)
- [Parameter Templates](#parameter-templates)
- [Parameter Template Fields](#parameter-template-fields)
- [Parameters (Instances)](#parameters-instances)
- [Parametric Tables](#parametric-tables)
- [Unit-Aware Filtering](#unit-aware-filtering)
- [Filter Operators](#filter-operators)
- [Selection Lists](#selection-lists)
- [Administration](#administration)

## Overview

The *Parameters* tab on the Part detail page allows users to view and manage multiple defined parameters associated with a part.

## Core Concept

Parameters are flexible metadata attributes for InvenTree objects. As stated in the documentation:

> "Parameters are not used for any core business logic within InvenTree. They are intended to provide additional metadata for objects, which can be useful for documentation, filtering, or reporting purposes."

Parameters consist of two components:
1. **Parameter Templates** — Define what parameter types are available
2. **Parameters** — Actual data instances created from templates and attached to specific objects

## Parameter Templates

Parameter Templates define the available parameter types with customizable attributes. Templates are the schema; Parameters are the values.

## Parameter Template Fields

| Field | Required | Description |
| --- | --- | --- |
| Name | Yes | Unique name for the parameter template |
| Description | No | Optional descriptive text |
| Units | No | Units of measurement for the parameter value |
| Model Type | No | Associated model type |
| Value Choices | No | Constrains valid values; can be a checkbox, selection list, or comma-separated options |

## Parameters (Instances)

Parameters are the actual data instances created from templates and attached to specific part objects.

Each parameter instance records:
- A reference to the Parameter Template
- The value for this specific part

Parameters are added and managed via the "Parameters" tab on the Part detail page.

## Parametric Tables

The parametric table feature displays parameters across multiple parts with sorting and filtering capabilities. This allows:
- Comparing parameter values across multiple parts in the same category
- Sorting parts by parameter value
- Filtering parts by parameter constraints

## Unit-Aware Filtering

The system converts compatible units automatically when filtering. For example:
- "10k" equals "10000" ohms when filtering resistance values
- Unit conversion is performed transparently during search/filter operations

## Filter Operators

The following filter operators are supported when filtering by parameter values:

| Operator | Description |
| --- | --- |
| `=` | Equality |
| `>` | Greater than |
| `>=` | Greater than or equal to |
| `<` | Less than |
| `<=` | Less than or equal to |
| `!=` | Not equal to |
| `~` | Text matching (contains) |

Multiple filters can be applied simultaneously to narrow results.

## Selection Lists

Selection Lists allow large predefined value sets for parameters. They overcome the 5000-character limit on template value choices. A Selection List can store an extensive set of allowed values for a parameter template.

## Administration

- Parameter Templates are managed through the admin interface
- Parameters (instances) are added and managed via the "Parameters" tab on the relevant object detail page
- The parametric table is accessible from the Part Category detail page

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/concepts/parameters.md
