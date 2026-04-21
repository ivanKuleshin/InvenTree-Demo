---
source: https://docs.inventree.org/en/stable/concepts/parameters/
component: parts
topic: Parameters and Parametric Tables
fetched: 2026-04-14
---

> **Source**: [https://docs.inventree.org/en/stable/concepts/parameters/](https://docs.inventree.org/en/stable/concepts/parameters/)

# Parameters and Parametric Tables

## Table of Contents

- [Parameters](#parameters)
  - [Parameter Tab](#parameter-tab)
- [Parameter Templates](#parameter-templates)
  - [Template Attributes](#template-attributes)
  - [Create Template](#create-template)
  - [Create Parameter](#create-parameter)
- [Parametric Tables](#parametric-tables)
  - [Parametric View](#parametric-view)
  - [Sorting by Parameter Value](#sorting-by-parameter-value)
  - [Filtering by Parameter Value](#filtering-by-parameter-value)
  - [Multiple Parameter Filters](#multiple-parameter-filters)
  - [Multiple Filters on the Same Parameter](#multiple-filters-on-the-same-parameter)
  - [Unit-Aware Filtering](#unit-aware-filtering)
  - [Removing Filters](#removing-filters)
  - [Available Filter Operators](#available-filter-operators)
- [Parameter Units](#parameter-units)
  - [Incompatible Units](#incompatible-units)
  - [Parameter Unit Sorting](#parameter-unit-sorting)
- [Selection Lists](#selection-lists)

---

## Parameters

A *parameter* describes a particular "attribute" or "property" of a specific object in InvenTree. Parameters allow for flexible and customizable data to be stored against various InvenTree models.

> **Note:** Parameters are not used for any core business logic within InvenTree. They are intended to provide additional metadata for objects, which can be useful for documentation, filtering, or reporting purposes.

Parameters can be associated with various InvenTree models.

### Parameter Tab

Any model which supports parameters will have a "Parameters" tab on its detail page. This tab displays all parameters associated with that object.

> **[IMAGE DESCRIPTION]**: Screenshot showing the Parameters tab on a Part detail page in InvenTree (image: concepts/parameter-tab.png, alt: "Parameters Example"). The Parameters tab is selected and shows a table of parameters for the current part. Each row shows the parameter name (from the template), the value for this specific part, and the units. An "Add Parameter" button is visible for adding new parameter values.

---

## Parameter Templates

Parameter templates are used to define the different types of parameters which are available for use.

### Template Attributes

| Attribute | Description |
|---|---|
| Name | The name of the parameter template (*must be unique*) |
| Description | Optional description for the template |
| Units | Optional units field (*must be a valid physical unit*) |
| Model Type | The InvenTree model to which this parameter template applies (e.g., Part, Company, etc.). If left blank, the template can be used for any model type. |
| Choices | A comma-separated list of valid choices for parameter values linked to this template. |
| Checkbox | If set, parameters linked to this template can only be assigned values *true* or *false*. |
| Selection List | If set, parameters linked to this template can only be assigned values from the linked [selection list](#selection-lists). |

> **[IMAGE DESCRIPTION]**: Screenshot showing the Parameter Templates management page in InvenTree (image: concepts/parameter-template.png, alt: "Parameters Template"). A list of existing parameter templates is displayed with columns for Name, Units, Description, and Choices. A "New Parameter" button is visible at the top. Each row has Edit and Delete action buttons.

### Create Template

Parameter templates are created and edited via the admin interface.

To create a template:

1. Navigate to the "Settings" page.
2. Click on the "Parameters" tab.
3. Click on the "New Parameter" button.
4. Fill out the `Create Parameter Template` form: `Name` (required) and `Units` (optional) fields.
5. Click on the "Submit" button.

An existing template can be edited by clicking on the "Edit" button associated with that template.

> **[IMAGE DESCRIPTION]**: Screenshot showing the Edit Parameter Template dialog (image: part/parameter_template_edit.png, alt: "Edit Parameter Template"). A modal dialog is open with fields for Name, Units, Description, Choices, and Checkbox. The Name field is pre-filled with the template's current name. A "Submit" button is at the bottom.

### Create Parameter

After creating a template or using existing templates, parameters can be added to any part.

To add a parameter:

1. Navigate to a specific part detail page.
2. Click on the "Parameters" tab.
3. Click on the "New Parameters" button.
4. The `Create Parameter` form is displayed.

> **[IMAGE DESCRIPTION]**: Screenshot showing the Create Parameter form for a part (image: part/create_part_parameter.png, alt: "Create Parameter Form"). A modal dialog with a dropdown to select the parameter `Template` and a `Data` field to enter the value. The `Template` dropdown lists all available parameter templates. A "Submit" button is at the bottom.

5. Select the parameter `Template` to use for this parameter.
6. Fill out the `Data` field (value of this specific parameter).
7. Click the "Submit" button.

---

## Parametric Tables

Parametric tables gather all parameters from all objects of a particular type to be sorted and filtered. For parts, the parametric table shows all parts in a category alongside their parameter values as additional columns.

> **[IMAGE DESCRIPTION]**: Screenshot showing the Parametric Parts Table in InvenTree (image: concepts/parametric-parts.png, alt: "Parametric Parts Table"). The table is in "Parametric View" mode showing parts as rows. Standard columns (Name, Description, Stock) appear on the left. Additional columns appear for each parameter defined for parts in this category (e.g., Resistance, Capacitance, Voltage Rating). Each cell shows the parameter value for that part, or is empty if the part does not have that parameter defined. A "Parametric View" toggle button is visible above the table.

### Parametric View

Table views that support parametric filtering and sorting have a **"Parametric View"** button above the table. Clicking this button switches the table to parametric mode, which adds parameter value columns to the standard table columns.

### Sorting by Parameter Value

The parametric parts table allows sorting by particular parameter values. Click on the header of a particular parameter column to sort results by that parameter.

> **[IMAGE DESCRIPTION]**: Screenshot showing the parametric table sorted by a parameter column (image: part/part_sort_by_param.png, alt: "Sort by Parameter"). A parameter column header (e.g., "Resistance") is highlighted with a sort indicator arrow. The parts in the table are reordered based on their resistance values, from lowest to highest (or highest to lowest depending on the sort direction).

Sorting is unit-aware: parameter values entered in different but compatible units (e.g., `10k` and `10000`) are sorted correctly after unit conversion.

### Filtering by Parameter Value

The parametric parts table allows filtering by particular parameter values. Click on the filter icon associated with a particular parameter column and enter the value to filter against.

> **[IMAGE DESCRIPTION]**: Screenshot showing the parameter filter dialog for the parametric table (image: part/filter_by_param.png, alt: "Filter by Parameter"). A filter input panel appears below or beside the parameter column header. It contains an operator selector dropdown (showing options like =, >, <, >=, <=, !=, ~) and a value input field. For parameters with predefined choices, the value field may be a dropdown showing valid options.

The available filter options depend on the type of parameter being filtered:
- A parameter with a limited set of choices allows filtering by those choices.
- A numeric parameter allows filtering against a specific value with an operator (e.g., greater than, less than).
- A text parameter supports the `~` (contains) operator.

### Multiple Parameter Filters

Multiple parameters can be used to filter the parametric table simultaneously. Add a new filter for each parameter to filter against. The results are filtered to include only parts which match **all** of the specified filters (AND logic).

Each parameter column indicates whether a filter is currently applied.

> **[IMAGE DESCRIPTION]**: Screenshot showing multiple parameter filters active at once on the parametric table (image: part/multiple_param_filters.png, alt: "Multiple Parameter Filters"). Multiple parameter column headers have a filter indicator icon (such as a funnel icon that turns blue or orange when active). The parts list is narrowed to only those matching all active filter conditions simultaneously.

### Multiple Filters on the Same Parameter

It is possible to apply multiple filters against the same parameter. For example, to find parts with a *Resistance* parameter greater than 10kΩ **and** less than 100kΩ, add two filters for the *Resistance* parameter:

1. Filter 1: Resistance `>` `10k`
2. Filter 2: Resistance `<` `100k`

> **[IMAGE DESCRIPTION]**: Screenshot showing two filters applied to the same parameter (image: part/multiple_filters_same_param.png, alt: "Multiple Filters on Same Parameter"). The Resistance parameter column shows two filter entries listed: one "> 10k" and one "< 100k". The table displays only parts with resistance values within the specified range.

### Unit-Aware Filtering

When filtering against a parameter that has a unit defined, the filter value can be specified in any compatible unit. The system automatically converts the value to the base unit defined for the parameter template.

For example, to show all parts with a *Resistance* parameter greater than 10kΩ, entering `10k` or `10000` in the filter field correctly interprets the value as 10,000 ohms.

> **[IMAGE DESCRIPTION]**: Screenshot showing unit-aware filter input (image: part/filter_with_unit.png, alt: "Unit Aware Filters"). A filter is being configured for the Resistance parameter. The user has entered "10k" as the filter value. A note or tooltip may indicate the interpreted value. The resulting table shows only parts with resistance values greater than 10,000 ohms regardless of what unit format was used to enter the parameter values.

### Removing Filters

To remove a filter against a given parameter, click on the remove button (circle-X icon) associated with that filter.

> **[IMAGE DESCRIPTION]**: Screenshot showing the remove filter button on the parametric table (image: part/remove_param_filter.png, alt: "Remove Parameter Filter"). Each active filter entry has a red circle with an X icon. Clicking this icon removes that specific filter and the table updates to show results without that filter applied.

### Available Filter Operators

| Operator | Meaning | Applicable to |
|---|---|---|
| `=` | Equal to | All parameter types |
| `>` | Greater than | Numeric parameters |
| `>=` | Greater than or equal to | Numeric parameters |
| `<` | Less than | Numeric parameters |
| `<=` | Less than or equal to | Numeric parameters |
| `!=` | Not equal to | All parameter types |
| `~` | Contains | Text parameters |

---

## Parameter Units

The *units* field defined against a parameter template defines the base unit of that template. Any parameters created against that template *must* be specified in compatible units.

The built-in conversion functionality means that parameter values can be input in different dimensions — as long as the dimension is compatible with the base template units.

> **Note:** Read more about how InvenTree supports [physical units of measure](https://docs.inventree.org/en/stable/concepts/units/).

### Incompatible Units

If a parameter is created with a value that is incompatible with the units specified for the template, it will be rejected.

> **[IMAGE DESCRIPTION]**: Screenshot showing an incompatible unit error when creating a parameter (image: part/part_invalid_units.png, alt: "Invalid Parameter Units"). An error message is displayed inline or in a dialog, indicating that the value entered uses a unit that is not compatible with the parameter template's defined base unit. The error prevents saving the parameter value.

This behavior can be disabled if required, so that any parameter value is accepted regardless of unit compatibility.

### Parameter Unit Sorting

Parameter sorting takes unit conversion into account, meaning that values provided in different (but compatible) units are sorted correctly.

> **[IMAGE DESCRIPTION]**: Screenshot showing the result of sorting a parameter column containing values in mixed compatible units (image: part/part_sorting_units.png, alt: "Sort by Parameter Units"). For example, a Resistance column may contain values like "1k", "100", "10k", "1M". After sorting, these are ordered correctly as 100Ω, 1kΩ, 10kΩ, 1MΩ, regardless of how they were entered.

---

## Selection Lists

Selection Lists can be used to add a large number of predefined values to a parameter template. This is useful for parameters that must be selected from a large predefined list of values (e.g., a list of standardized color codes).

Choices on templates are limited to 5000 characters. Selection lists can be used to overcome this limitation for large value sets.

It is possible that plugins lock selection lists to ensure a known state.

Administration of selection lists can be done through the `Parameter` section in the Admin Center or via the API.
