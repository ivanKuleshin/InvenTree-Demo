# Given-When-Then DSL

## Core Pattern

Every REST Assured test follows the BDD pattern:

```java
given().           // Request specification (headers, params, body, auth)
    header("Accept", "application/json").
    queryParam("active", true).
when().             // HTTP method + endpoint
    get("/api/parts").
then().             // Response validation
    statusCode(200).
    body("results.size()", greaterThan(0));
```

## Simple GET Without Preconditions

When no request setup is needed, start directly with `when()`:

```java
when().
    get("/api/health").
then().
    statusCode(200).
    body("status", equalTo("ok"));
```

## POST with JSON Body

```java
given().
    contentType("application/json").
    body("""
        {
            "name": "Resistor 10k",
            "category": 5,
            "active": true
        }
        """).
when().
    post("/api/parts/").
then().
    statusCode(201).
    body("name", equalTo("Resistor 10k")).
    body("pk", notNullValue());
```

## PUT / PATCH / DELETE

```java
// Full update
given().
    contentType("application/json").
    body(updatedPartJson).
when().
    put("/api/parts/{id}", partId).
then().
    statusCode(200);

// Partial update
given().
    contentType("application/json").
    body("{\"active\": false}").
when().
    patch("/api/parts/{id}", partId).
then().
    statusCode(200).
    body("active", equalTo(false));

// Delete
when().
    delete("/api/parts/{id}", partId).
then().
    statusCode(204);
```
