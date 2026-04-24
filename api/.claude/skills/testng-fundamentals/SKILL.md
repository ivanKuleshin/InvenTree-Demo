---
name: testng-fundamentals
user-invocable: false
description: Use when working with TestNG annotations, assertions, test lifecycle, and configuration for Java testing.
allowed-tools: [ Read, Write, Edit, Bash, Glob, Grep ]
---

# TestNG Fundamentals

Master TestNG fundamentals including annotations, assertions, test lifecycle, and XML configuration for Java testing.
This skill provides comprehensive coverage of essential concepts, patterns, and best practices for professional TestNG
development.

## Overview

TestNG is a powerful testing framework for Java inspired by JUnit and NUnit, designed to cover a wider range of test
categories: unit, functional, end-to-end, and integration testing. It supports annotations, data-driven testing,
parameterization, and parallel execution.

## Installation and Setup

- Refer to this document if you need to set up or configure
  TestNG - [installation-and-setup.md](references/installation-and-setup.md)

## Core Annotations

### Test Lifecycle Annotations

TestNG provides comprehensive lifecycle annotations:

```java
import org.testng.annotations.*;

public class LifecycleTest {

    @BeforeSuite
    public void beforeSuite() {
        // Runs once before the entire test suite
        System.out.println("Before Suite");
    }

    @AfterSuite
    public void afterSuite() {
        // Runs once after the entire test suite
        System.out.println("After Suite");
    }

    @BeforeTest
    public void beforeTest() {
        // Runs before each <test> tag in testng.xml
        System.out.println("Before Test");
    }

    @AfterTest
    public void afterTest() {
        // Runs after each <test> tag in testng.xml
        System.out.println("After Test");
    }

    @BeforeClass
    public void beforeClass() {
        // Runs once before the first test method in the class
        System.out.println("Before Class");
    }

    @AfterClass
    public void afterClass() {
        // Runs once after the last test method in the class
        System.out.println("After Class");
    }

    @BeforeMethod
    public void beforeMethod() {
        // Runs before each test method
        System.out.println("Before Method");
    }

    @AfterMethod
    public void afterMethod() {
        // Runs after each test method
        System.out.println("After Method");
    }

    @Test
    public void testMethod() {
        System.out.println("Test Method");
    }
}
```

### Test Annotation Attributes

- Refer to this document for all `@Test` annotation attributes with
  examples - [test-annotation-attributes.md](references/test-annotation-attributes.md)

## Assertions

- Refer to this document for assertion methods and soft
  assertions - [assertions.md](references/assertions.md)

## TestNG XML Configuration

- Refer to this document for detailed XML Test Suite
  configuration - [xml-suite-config.md](references/xml-suite-config.md)

## Test Groups

- Refer to this document for test group definitions and group
  dependencies - [test-groups.md](references/test-groups.md)

## Listeners

- Refer to this document for detailed TestNG Listeners guide - [listeners.md](references/listeners.md)

## Best Practices

1. **Use descriptive test names** - Name tests clearly to indicate what they verify
2. **Group related tests** - Use groups to organize tests by feature or type
3. **Avoid test dependencies** - Tests should be independent when possible
4. **Use soft assertions wisely** - For multiple related checks in one test
5. **Configure timeouts** - Prevent tests from hanging indefinitely
6. **Use BeforeClass/AfterClass** - For expensive setup/teardown operations
7. **Leverage testng.xml** - For suite-level configuration and organization
8. **Implement listeners** - For custom reporting and test lifecycle hooks
9. **Use priority sparingly** - Prefer dependency declarations over priority
10. **Document test purpose** - Use the description attribute

## Common Pitfalls

1. **Test order dependency** - Relying on implicit test execution order
2. **Shared mutable state** - Tests modifying shared resources
3. **Missing assertions** - Tests without verification
4. **Overly broad groups** - Groups that are too generic to be useful
5. **Circular dependencies** - Tests that depend on each other in a cycle
6. **Long-running tests** - Tests without appropriate timeouts
7. **Poor failure messages** - Assertions without descriptive messages
8. **Ignoring test failures** - Using enabled=false to hide failing tests
9. **Hard-coded test data** - Not using parameters or data providers
10. **Missing cleanup** - Not properly releasing resources in @After methods

## When to Use This Skill

- Setting up TestNG in new Java projects
- Writing unit and integration tests with TestNG
- Configuring test suites with testng.xml
- Organizing tests with groups and dependencies
- Implementing custom test listeners
- Troubleshooting TestNG test failures
- Migrating from JUnit to TestNG
- Training team members on TestNG fundamentals
