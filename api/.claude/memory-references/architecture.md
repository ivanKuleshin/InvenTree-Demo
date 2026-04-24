# Architecture

## High-Level Design

```
src/
├── main/java/com/inventree/
│   ├── service/          # Service layer wrapping API calls
│   ├── model/            # POJO models (parts, categories, etc.)
│   ├── client/           # HTTP client wrappers
│   ├── config/           # Configuration management
│   ├── auth/             # Role-based authentication
│   └── util/             # Constants, validators, HTTP status codes
│
└── test/java/com/inventree/
    ├── tests/            # Test classes (extends BaseTest)
    ├── testdata/         # Test data builders and providers
    ├── listeners/        # TestNG listeners for hooks
    └── base/             # BaseTest (setup/teardown, services)
```

## Service Layer

Services are static singletons initialized in `BaseTest.initFramework()`. They handle all API calls via REST Assured and
return domain models.

**Key services:**

- `PartService` — Parts CRUD, filtering, validation
- `PartCategoryService` — Part categories CRUD
- `StockService` — Stock locations and items
- `CompanyService` — Company/supplier management
- `PricingService` — Part pricing data

Services use REST Assured's fluent DSL (`given()`, `when()`, `then()`) and return strongly typed responses via Jackson
POJOs.

## Test Structure

```
BaseTest
  ├── @BeforeSuite → initFramework() → initialize services, cache auth tokens
  ├── @BeforeMethod → log test name
  ├── @AfterMethod → log result (PASSED/FAILED/SKIPPED)
  └── @AfterSuite → teardown auth cache

TestClass extends BaseTest
  ├── Uses protected final services
  ├── Calls service methods and captures Response
  ├── Asserts response status, headers, body structure
  └── Cleans up test data in @AfterMethod
```

Tests follow **Given/When/Then** structure:

- **Given** — Create test data (via service calls)
- **When** — Call API endpoint being tested
- **Then** — Assert status code, JSON body structure, and business logic

## Authentication

Role-based auth managed by `AuthManager` singleton. Roles: `ADMIN`, `ENGINEER`, `READER`, `ALLACCESS`.

Token flow:

1. `BaseTest.initFramework()` pre-fetches tokens for all roles
2. Service methods call `AuthManager.getInstance().getToken(role)` to get cached token
3. Token added to `Authorization: Token <token>` header in REST Assured `given()`

Credentials stored in `config.properties`:

```properties
api.credentials.engineer.username=engineer
api.credentials.engineer.password=partsonly
```