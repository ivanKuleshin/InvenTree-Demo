---
name: rest-assured-api-testing
user-invocable: false
description: Use when writing, reviewing, fixing or debugging REST API tests with REST Assured in Java. Covers Given-When-Then DSL, specifications, authentication, JSON validation, POJO serialization, logging, filters, JSON Schema, and Allure integration.
allowed-tools: [ Read, Write, Edit, Bash, Glob, Grep ]
---

# REST Assured API Testing — Best Practices

Comprehensive guide for writing, structuring, and maintaining REST API tests with REST Assured in Java.

## Activity-Based Reference Guide

Consult these references based on what you're doing:

### Writing API Tests

**When to use**: Creating new test files, writing test cases, implementing API test scenarios

| Activity                                                    | Reference Files                                                   |
|-------------------------------------------------------------|-------------------------------------------------------------------|
| **Given-When-Then DSL (GET, POST, PUT, PATCH, DELETE)**     | [given-when-then.md](core/given-when-then.md)                     |
| **Request / Response specifications and BaseTest**          | [specifications.md](core/specifications.md)                       |
| **Authentication (Basic, Token, OAuth2, Form, SSL)**        | [authentication.md](core/authentication.md)                       |
| **JSON body validation (Hamcrest, Schema, headers, time)**  | [json-validation.md](validation/json-validation.md)               |
| **Response extraction, JsonPath, POJO mapping**             | [response-extraction.md](validation/response-extraction.md)       |
| **Logging, filters, failure listeners**                     | [logging-filters.md](advanced/logging-filters.md)                 |
| **Configuration and multipart / file upload**               | [configuration-multipart.md](advanced/configuration-multipart.md) |
| **TestNG integration and data-driven tests**                | [testng-integration.md](integration/testng-integration.md)        |
| **Allure reporting integration**                            | [allure-integration.md](integration/allure-integration.md)        |
| **⚠️ No hardcoding — Config, Endpoints, TestData patterns** | [no-hardcoding.md](core/no-hardcoding.md)                         |

## Quick Decision Tree

```
What are you doing?
│
├─ Writing a new API test?
│  ├─ Simple GET/POST/PUT/DELETE → core/given-when-then.md
│  ├─ Reusable specs / BaseTest → core/specifications.md
│  ├─ Auth setup needed        → core/authentication.md
│  └─ Data-driven tests        → integration/testng-integration.md
│
├─ Validating responses?
│  ├─ JSON body with Hamcrest  → validation/json-validation.md
│  ├─ JSON Schema contract     → validation/json-validation.md
│  ├─ Extract values / POJO    → validation/response-extraction.md
│  ├─ Headers / status / time  → validation/json-validation.md
│  └─ XML (XPath, XSD, DTD)   → validation/response-extraction.md
│
├─ Debugging / maintaining?
│  ├─ Logging (request/response) → advanced/logging-filters.md
│  ├─ Conditional logging (CI)   → advanced/logging-filters.md
│  ├─ Custom filters             → advanced/logging-filters.md
│  └─ Failure listeners          → advanced/logging-filters.md
│
├─ Configuration?
│  ├─ Global config / defaults → advanced/configuration-multipart.md
│  ├─ SSL / HTTPS              → core/authentication.md
│  ├─ File upload / multipart  → advanced/configuration-multipart.md
│  └─ Session management       → core/authentication.md
│
└─ Reporting?
   ├─ Allure filter setup      → integration/allure-integration.md
   └─ Allure annotations       → integration/allure-integration.md
```

## Core Patterns (Quick Reference)

> **⚠️ No Hardcoding Rule — applies to every example in this skill**
>
> Code examples below use inline literals for **illustration only**.  
> In actual tests, **never hardcode** endpoints, status codes, credentials, field values, or thresholds.  
> Always use `Config`, `Endpoints`, and `TestData` classes — see [no-hardcoding.md](core/no-hardcoding.md).

```
given()
    .contentType(ContentType.JSON)
    .header("Authorization", "Token " + Config.getApiToken())
    .body(payload)
.when()
    .post(Endpoints.PARTS)
.then()
    .statusCode(HttpStatus.SC_CREATED)
    .body("name", equalTo(payload.getName()));
```

### BaseTest Setup

See the full canonical example in [specifications.md → BaseTest Pattern](core/specifications.md).

### Response Extraction

```
int id = given().spec(requestSpec).when().get(Endpoints.PART_BY_ID, partId)
    .then().statusCode(HttpStatus.SC_OK).extract().path("pk");  // ✅ Possible, but not desired approach

Part part = get(Endpoints.PARTS + "1").as(Part.class);          // ✅ POJO, no literals, desired approach
List<Part> parts = from(json).getList("results", Part.class);
```

## Best Practices

1. **Use static imports** — `given()`, `when()`, `then()`, and Hamcrest matchers should always be statically imported
   for readability.
2. **Centralize configuration in BaseTest** — Set `baseURI`, `basePath`, default headers, auth, and logging once. Call
   `RestAssured.reset()` in `@AfterSuite`.
3. **Prefer RequestSpecBuilder over raw given()** — Build reusable specs for auth, content type, and common headers.
   Compose specs with `.spec()`.
4. **Log only on failure in CI** — Use `RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()` globally.
   Avoid `log().all()` in production test runs.
5. **Blacklist sensitive headers** — Use `logConfig().blacklistHeader("Authorization")` to prevent token leakage in
   logs.
6. **Validate response schema** — Use `matchesJsonSchemaInClasspath()` for contract testing. Store schemas in
   `src/test/resources/schemas/`.
7. **Assert status codes explicitly** — Always assert `statusCode()` before body assertions. A 500 with matching body
   text is still a failure.
8. **Use path parameters, not string concatenation** — Prefer `get("/parts/{id}", 42)` over `get("/parts/" + 42)` for
   readability and logging.
9. **Never hardcode test inputs or expectations** — endpoints, status codes, credentials, expected field values, and
   timeouts must come from `Config`, `Endpoints`, and `TestData` classes. See [no-hardcoding.md](core/no-hardcoding.md).
10. **Keep tests independent** — Each test should create its own test data. Use `@BeforeMethod` / `@AfterMethod` for
    setup/teardown when tests share state.
11. **Use POJOs for complex bodies** — Serialize Java objects instead of raw JSON strings. Keeps payloads type-safe and
    refactoring-friendly.
12. **Validate response time in performance-critical paths** — Use `.time(lessThan(2000L))` to catch regressions early.
13. **Use ResponseSpecification for repeated validations** — Reduce duplication for common checks (200 + JSON content
    type + time limit).
14. **Prefer `.then()` over `.expect()`** — The `.then()` API is the modern fluent approach; `.expect()` is legacy.
15. **Clean up test data** — Delete created resources in `@AfterMethod` or `@AfterClass` to prevent test pollution.

## Common Pitfalls

1. **Forgetting `contentType(JSON)` on POST/PUT** — REST Assured won't auto-serialize POJOs without it. The server may
   return 415 Unsupported Media Type.
2. **Not resetting global state** — `RestAssured.baseURI` persists across tests. Always call `RestAssured.reset()` in
   teardown.
3. **Using `expect()` to send requests** — Removed in REST Assured 4.x+. Use `when().get()` instead of `expect().get()`.
4. **Asserting on empty bodies with `equalTo(null)`** — Throws assertion error since REST Assured 3.x. Check
   `statusCode()` instead.
5. **Ignoring response status before body checks** — A body matcher may accidentally pass on an error response. Always
   assert status first.
6. **String comparison on numeric JSON values** — JSON numbers are parsed as `Integer` or `Float`. Use `equalTo(5)` not
   `equalTo("5")`.
7. **Multiple headers with the same name** — REST Assured validates against the LAST header value. Use `.headers()` to
   inspect all values.
8. **Not URL-encoding special characters** — REST Assured handles basic encoding, but special query parameters may need
   explicit encoding.
9. **Logging `all()` in CI pipelines** — Produces excessive output and may expose secrets. Use conditional logging or
   blacklist headers.
10. **Tight coupling via `dependsOnMethods`** — Avoid long dependency chains. Prefer independent tests with per-test
    setup.

## When to Use This Skill

- Writing new REST API test classes with REST Assured
- Setting up BaseTest and request/response specifications
- Configuring authentication (Basic, Token, OAuth2) for API tests
- Validating JSON/XML response bodies with Hamcrest matchers
- Extracting response data with JsonPath for chained API calls
- Serializing/deserializing POJOs with Jackson in request/response
- Adding JSON Schema validation for contract testing
- Debugging API test failures and flaky tests
- Configuring logging, filters, and Allure reporting
- Implementing data-driven API tests with TestNG DataProviders
- Testing file upload via multipart form data
- Configuring SSL, proxies, and session management
