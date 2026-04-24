---
name: testng-data-driven
user-invocable: false
description: Use when implementing data-driven tests with TestNG DataProviders, factory methods, and parameterization patterns.
allowed-tools: [Read, Write, Edit, Bash, Glob, Grep]
---

# TestNG Data-Driven Testing

Master TestNG data-driven testing including DataProviders, Factory patterns, and parameterization for comprehensive test coverage. This skill covers techniques for testing with multiple data sets efficiently.

## Overview

Data-driven testing separates test logic from test data, enabling a single test method to run against multiple inputs. TestNG provides powerful features for data-driven testing through DataProviders, Factory methods, and XML parameterization.

## Check the references for more info about data-driven testing with TestNG
- Check the [code-patterns.md](references/code-patterns.md)

## Best Practices

1. **Keep DataProviders focused** - One DataProvider per data type or scenario
2. **Use external files for large datasets** - CSV, JSON, or Excel for maintainability
3. **Make DataProviders static when shared** - Required for external DataProvider classes
4. **Include test case identifiers** - First column can be test case ID for tracing
5. **Use meaningful DataProvider names** - Clear names improve test reports
6. **Consider parallel execution** - Enable parallel flag for independent tests
7. **Handle null values explicitly** - Define behavior for null/empty data
8. **Document expected formats** - Comment data structure for complex providers
9. **Use Iterator for large datasets** - Avoid memory issues with lazy loading
10. **Separate test data from test logic** - Keep tests clean and data manageable

## Common Pitfalls

1. **Type mismatches** - DataProvider types must match test method parameters
2. **Missing static modifier** - External DataProvider methods must be static
3. **Array dimension errors** - Must return Object[][] not Object[]
4. **Null pointer exceptions** - Not handling null values in data
5. **Resource leaks** - Not closing file streams in DataProviders
6. **Tight coupling** - Hard-coding environment-specific data
7. **Over-parameterization** - Too many parameters make tests hard to read
8. **Missing test case IDs** - Hard to trace failures without identifiers
9. **Ignoring execution order** - DataProvider rows may run in any order
10. **Not considering parallelism** - Shared state issues with parallel DataProviders

## When to Use This Skill

- Testing with multiple input combinations
- Implementing boundary value testing
- Creating cross-browser or cross-environment tests
- Loading test data from external sources
- Building parameterized test suites
- Creating data-driven API tests
- Implementing database-driven tests
- Building reusable test data frameworks
