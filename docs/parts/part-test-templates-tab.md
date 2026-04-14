---
source: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/test.md
component: parts
topic: Part Detail - Test Templates Tab
fetched: 2026-04-14
---

# Part Detail — Test Templates Tab

## Table of Contents

- [Overview](#overview)
- [Visibility Condition](#visibility-condition)
- [Test Template Concept](#test-template-concept)
- [Test Template Cascading](#test-template-cascading)
- [Test Template Parameters](#test-template-parameters)
- [Test Key Examples](#test-key-examples)
- [Test Results](#test-results)
- [Best Practices](#best-practices)

## Overview

If a part is marked as *testable*, the user can define tests which must be performed on any stock items which are instances of this part. The *Test Templates* tab is where these test definitions are managed.

> **[IMAGE DESCRIPTION]**: A "Part Test Templates" view showing a table of test templates defined for a part. Columns include test name, test key, description, required (boolean), requires value (boolean), requires attachment (boolean), and enabled (boolean). Buttons for adding new test templates and editing/deleting existing ones are visible.

## Visibility Condition

The *Test Templates* tab is only visible if the Part is designated as **testable**.

## Test Template Concept

Parts which are designated as testable can define templates for tests which are to be performed against individual stock items corresponding to the part.

A test template defines the parameters of the test; individual stock items can then have associated test results which correspond to a test template.

## Test Template Cascading

Test templates "cascade" down to variant parts: if a master part has multiple variants, any test template defined for the master part will be assigned to the variants. Any stock items of the variant parts will have the same test templates associated with them.

## Test Template Parameters

### Test Name

The name of the test is a simple string value which defines the name of the test. This name must be unique for a given part (or across a set of part variants).

### Test Key

The test name is used to generate a test "key" which is then used to match against test results associated with individual stock items.

The *key* is a simplified string representation of the test name, which consists only of lowercase alphanumeric characters. This key value is automatically generated (based on the test name) whenever the test template is saved.

The generated test key is intended to be a valid Python variable name, and can be used to reference the test in the report generation system.

### Test Description

A simple description field for providing information back to the user. The description is not used by the InvenTree testing framework.

### Required

If the *required* flag is set, this indicates that the test is crucial for the acceptance of a particular stock item.

### Requires Value

If this flag is set, then a corresponding test result against a stock item must set the *value* parameter.

### Requires Attachment

If this flag is set, then a corresponding test result against a stock item must provide a file attachment.

### Enabled

Tests can be *disabled* by setting the *enabled* flag to `False`. This can be useful if a test is no longer required, but the test template should be retained for historical purposes.

> **Note:** Deleting a test template will also delete any associated test results. If a test template is no longer required, it is better to disable it rather than delete it.

## Test Key Examples

| Test Name | Test Key |
| --- | --- |
| "Firmware Version" | "firmwareversion" |
| " My NEW T E sT " | "mynewtest" |
| "100 Percent Test" | "_100percenttest" *(leading underscore added to ensure the key is a valid Python variable name)* |
| "Test 123" | "test123" |

## Test Results

Individual stock item objects can have test results associated with them which correspond to test templates.

### Test Result Fields

| Field | Required | Description |
| --- | --- | --- |
| Test Template | Yes | Links to the Part Test Template object |
| Result | Yes | Boolean pass/fail status of the test |
| Value | No | Optional value uploaded as part of the test data (e.g., firmware version number) |
| Notes | No | Optional field for extra notes |
| Attachment | No | An attached file containing extra test information |

### Multiple Test Results

Multiple results can be uploaded against the same test name. In cases where multiple test results exist, the most recent value is used to determine the pass/fail status of the test.

This is useful when a test is required to run multiple times — for example, if it fails the first time and something must be fixed before running the test again.

### Automated Test Integration

The stock item testing framework is especially useful when integrating with an automated acceptance testing framework. Test results can be uploaded using the InvenTree API or the InvenTree Python Interface.

> **[IMAGE DESCRIPTION]**: A "Stock Item Test Results" view showing a table of test results for a specific stock item. Columns include test name/key, result (pass/fail indicator), value, notes, attachment link, and date. Results are grouped by test template. The most recent result for each test template is highlighted or shown as the current status.

## Best Practices

- Disable obsolete test templates rather than deleting them, to preserve historical test result data.
- Deletion of a test template removes all associated test results permanently.
- Use descriptive test names; the system will auto-generate a unique key from the name.
- For automated testing integration, use the test key as the identifier when uploading results via the API.

> **Source**: https://github.com/inventree/InvenTree/blob/master/docs/docs/part/test.md
> https://github.com/inventree/InvenTree/blob/master/docs/docs/stock/test.md
