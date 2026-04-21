# Logging and Filters

## Request Logging

```java
given().log().all().when().get("/api/parts/");     // Log everything
given().log().headers().when().get("/api/parts/");  // Headers only
given().log().body().when().get("/api/parts/");     // Body only
given().log().params().when().get("/api/parts/");   // Parameters only
given().log().uri().when().get("/api/parts/");      // URI only
```

## Response Logging

```java
when().get("/api/parts/").then().log().all();       // Log everything
when().get("/api/parts/").then().log().body();      // Body only
when().get("/api/parts/").then().log().status();    // Status line only
when().get("/api/parts/").then().log().headers();   // Headers only
```

## Conditional Logging (Recommended for CI)

Log only when validation fails — reduces noise in passing tests:

```java
// Per-request
given().log().ifValidationFails().when().get("/api/parts/").then().log().ifValidationFails();

// Globally (preferred — set once in BaseTest)
RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
```

## Blacklisting Sensitive Headers

Prevent tokens and credentials from appearing in logs:

```java
RestAssured.config = RestAssured.config()
    .logConfig(logConfig().blacklistHeader("Authorization"));
```

## Logging via RequestSpecBuilder

```java
RequestSpecification spec = new RequestSpecBuilder()
    .log(LogDetail.ALL)
    .build();
```

## Built-in Filters

```java
// Log errors (4xx/5xx responses)
given().filter(ErrorLogger.errorLogger()).when().get("/api/parts/");

// Log all responses
given().filter(ResponseLoggingFilter.loggingFilter()).when().get("/api/parts/");

// Log responses with specific status codes
given().filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500)).when().get("/api/parts/");

// Log request details
given().filter(RequestLoggingFilter.with(LogDetail.METHOD, LogDetail.HEADERS, LogDetail.BODY)).when().get("/api/parts/");
```

## Custom Filter

```java
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureRestAssuredFilter implements Filter {
    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext ctx) {
        // Pre-request logic (e.g., attach request to Allure)
        Response response = ctx.next(requestSpec, responseSpec);
        // Post-response logic (e.g., attach response to Allure)
        return response;
    }
}
```

## Global Filters

```java
RestAssured.filters(new AllureRestAssuredFilter(), new ErrorLogger());
```

## Custom Failure Listeners

```java
RestAssured.config = RestAssured.config()
    .failureConfig(failureConfig().with().failureListeners(
        (requestSpec, responseSpec, response) -> {
            log.error("API test failed! Status: {}", response.statusCode());
            log.error("Response body: {}", response.body().asString());
        }
    ));
```
