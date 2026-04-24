# JSON Body Validation

## Hamcrest Matchers on JSON Paths

```java
when().
    get("/api/parts/{id}", 1).
then().
    statusCode(200).
    body("name", equalTo("Resistor 10k")).
    body("category", notNullValue()).
    body("active", is(true)).
    body("pk", greaterThan(0));
```

## Nested JSON and Collections

```java
when().
    get("/api/parts/").
then().
    body("results.size()", greaterThan(0)).
    body("results[0].name", not(emptyString())).
    body("results.name", hasItem("Resistor 10k")).
    body("results.findAll { it.active == true }.size()", greaterThan(0));
```

## Multiple Assertions in a Single body() Call

```java
when().
    get("/api/parts/{id}", 1).
then().
    body(
        "name", equalTo("Resistor 10k"),
        "category", notNullValue(),
        "active", is(true)
    );
```

## JSON Schema Validation

Place JSON schema files in `src/test/resources/schemas/`:

```java
// Requires json-schema-validator dependency
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

when().
    get("/api/parts/{id}", 1).
then().
    assertThat().
    body(matchesJsonSchemaInClasspath("schemas/part-schema.json"));
```

## Header Validation

```java
when().
    get("/api/parts/").
then().
    header("Content-Type", containsString("application/json")).
    header("X-Request-Id", notNullValue());

// Validate header with a mapping function
when().
    get("/api/parts/").
then().
    header("Content-Length", Integer::parseInt, lessThan(50000));

// Response-aware header validation
given().
    redirects().follow(false).
when().
    get("/redirect").
then().
    statusCode(301).
    header("Location", response -> endsWith("/redirect/" + response.path("id")));
```

## Response Time Validation

```java
// Assert response time in milliseconds
when().
    get("/api/parts/").
then().
    time(lessThan(2000L));

// Assert response time in specific time unit
when().
    get("/api/parts/").
then().
    time(lessThan(3L), java.util.concurrent.TimeUnit.SECONDS);

// Extract response time
long timeInMs = get("/api/parts/").time();
long timeInSeconds = get("/api/parts/").timeIn(java.util.concurrent.TimeUnit.SECONDS);
```

## Error Response Testing

```java
// Validate error status codes
when().
    get("/api/parts/99999").
then().
    statusCode(404);

// Validate error response body
given().
    contentType(ContentType.JSON).
    body("{\"name\": \"\"}").
when().
    post("/api/parts/").
then().
    statusCode(400).
    body("name", hasItem("This field may not be blank."));

// Capture error responses for assertions
Response response = given().
    contentType(ContentType.JSON).
    body("invalid json").
when().
    post("/api/parts/").
then().
    extract().response();

assertThat(response.statusCode(), anyOf(is(400), is(422)));
```
