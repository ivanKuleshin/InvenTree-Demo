# No Hardcoding in Tests

All code examples in this skill use literal values for **illustration only**.  
In real tests, every hardcoded literal is a maintenance hazard and a security risk.

## What Not to Hardcode

| Category              | Bad ❌                         | Good ✅                                                  |
| --------------------- | ------------------------------ | -------------------------------------------------------- |
| Base URI              | `"https://demo.inventree.org"` | `Config.getBaseUri()`                                    |
| API path / endpoint   | `"/api/parts/"`                | `Endpoints.PARTS`                                        |
| Auth token / password | `"Token abc123"`               | `Config.getApiToken()`                                   |
| Expected status code  | `statusCode(201)`              | `statusCode(HttpStatus.SC_CREATED)`                      |
| Expected field value  | `equalTo("Resistor 10k")`      | `equalTo(part.getName())`                                |
| Timeout / threshold   | `lessThan(2000L)`              | `lessThan(Config.getResponseTimeoutMs())`                |
| Test user credentials | `"admin", "password"`          | `Config.getAdminUsername()`, `Config.getAdminPassword()` |

## Config — Externalized Environment Properties

Read all environment-specific values from system properties or environment variables:

```java
public final class Config {

    private Config() {}

    public static String getBaseUri() {
        return System.getProperty("api.base.uri",
            System.getenv().getOrDefault("API_BASE_URI", "https://demo.inventree.org"));
    }

    public static String getBasePath() {
        return System.getProperty("api.base.path", "/api");
    }

    public static String getApiToken() {
        String token = System.getProperty("api.token", System.getenv("API_TOKEN"));
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("API token not set. Use -Dapi.token=... or API_TOKEN env var.");
        }
        return token;
    }

    public static String getAdminUsername() {
        return System.getProperty("admin.username", System.getenv().getOrDefault("ADMIN_USERNAME", "admin"));
    }

    public static String getAdminPassword() {
        String pwd = System.getProperty("admin.password", System.getenv("ADMIN_PASSWORD"));
        if (pwd == null || pwd.isBlank()) {
            throw new IllegalStateException("Admin password not set. Use -Dadmin.password=... or ADMIN_PASSWORD env var.");
        }
        return pwd;
    }

    public static long getResponseTimeoutMs() {
        return Long.parseLong(System.getProperty("api.response.timeout.ms", "3000"));
    }
}
```

## Endpoints — Named API Path Constants

Never write path strings inline in test code:

```java
public final class Endpoints {

    private Endpoints() {}

    public static final String PARTS          = "/part/";
    public static final String PART_BY_ID     = "/part/{id}/";
    public static final String PART_CATEGORIES = "/part/category/";
    public static final String PART_PRICING   = "/part/{id}/pricing/";
    public static final String PART_BOM       = "/bom/{id}/";
}
```

## TestData — POJO Builders, Not Inline Literals

Never assert against string literals that you also wrote into the request body:

```java
public final class PartTestData {

    private PartTestData() {}

    /** Returns a minimal valid part payload for creation tests. */
    public static PartRequest newValidPart() {
        return PartRequest.builder()
            .name("Test Part " + System.currentTimeMillis())  // unique per run
            .category(1)
            .active(true)
            .build();
    }

    /** Returns a part payload missing the required 'name' field. */
    public static PartRequest missingNamePart() {
        return PartRequest.builder()
            .category(1)
            .build();
    }
}
```

Usage in tests:

```java
PartRequest payload = PartTestData.newValidPart();

given().
    spec(requestSpec).
    body(payload).
when().
    post(Endpoints.PARTS).
then().
    statusCode(HttpStatus.SC_CREATED).
    body("name", equalTo(payload.getName()));    // assertion derives from input, not a literal
```

## BaseTest with Externalized Config

```java
public abstract class BaseTest {

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = Config.getBaseUri();
        RestAssured.basePath = Config.getBasePath();

        requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", "Token " + Config.getApiToken())
            .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.config = RestAssured.config()
            .logConfig(logConfig().blacklistHeader("Authorization"));
    }

    @AfterSuite
    public void tearDown() {
        RestAssured.reset();
    }
}
```

## Running Tests with Config Overrides

```bash
# Via Maven system properties
mvn test -Dapi.base.uri=https://staging.inventree.org \
         -Dapi.token=$API_TOKEN \
         -Dapi.response.timeout.ms=5000

# Via environment variables (CI/CD)
API_BASE_URI=https://staging.inventree.org
API_TOKEN=<secret>
```
