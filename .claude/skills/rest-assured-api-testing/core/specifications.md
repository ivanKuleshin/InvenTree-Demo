# Request and Response Specifications

## RequestSpecification — DRY Request Setup

Build reusable request specifications to eliminate duplication:

```java
RequestSpecification requestSpec = new RequestSpecBuilder()
    .setBaseUri("https://demo.inventree.org")
    .setBasePath("/api")
    .setContentType(ContentType.JSON)
    .addHeader("Authorization", "Token " + apiToken)
    .log(LogDetail.ALL)
    .build();

// Usage
given().
    spec(requestSpec).
    queryParam("active", true).
when().
    get("/part/").
then().
    statusCode(200);
```

## ResponseSpecification — DRY Response Validation

```java
ResponseSpecification successSpec = new ResponseSpecBuilder()
    .expectStatusCode(200)
    .expectContentType(ContentType.JSON)
    .expectResponseTime(lessThan(3000L))
    .build();

// Usage
given().
    spec(requestSpec).
when().
    get("/part/").
then().
    spec(successSpec).
    body("results.size()", greaterThan(0));
```

## BaseTest Pattern

Centralize common configuration in a base class. All environment-specific values must come
from `Config` — never hardcoded. See [no-hardcoding.md](no-hardcoding.md) for the full `Config` class.

```java
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.restassured.AllureRestAssured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI  = Config.getBaseUri();   // from system property / env var
        RestAssured.basePath = Config.getBasePath();  // from system property / env var

        requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", "Token " + Config.getApiToken())
            .log(LogDetail.URI)
            .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config()
            .logConfig(logConfig().blacklistHeader("Authorization"));
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterSuite
    public void tearDown() {
        RestAssured.reset();
    }
}
```

## Configuration Merging with Specifications

Configurations from a specification merge (not overwrite) with existing configurations:

```java
RequestSpecification spec = new RequestSpecBuilder()
    .setConfig(config().headerConfig(headerConfig().overwriteHeadersWithName("header1")))
    .build();

given().
    config(config().sessionConfig(sessionConfig().sessionIdName("phpsessionid"))).
    spec(spec).    // Both configs are applied
when().
    get("/endpoint");
```
