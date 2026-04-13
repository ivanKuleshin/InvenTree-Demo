# Authentication

## Basic Authentication

```java
// Challenged (sends credentials after 401 challenge)
given().auth().basic("username", "password").when().get("/secured");

// Preemptive (sends credentials with the initial request — preferred)
given().auth().preemptive().basic("username", "password").when().get("/secured");

// Set as default for all requests
RestAssured.authentication = preemptive().basic("username", "password");
```

## Token / API Key Authentication

```java
// Via header (most common for REST APIs)
given().
    header("Authorization", "Token " + apiToken).
when().
    get("/api/parts/");

// Via RequestSpecBuilder for reuse
RequestSpecification authSpec = new RequestSpecBuilder()
    .addHeader("Authorization", "Token " + apiToken)
    .build();
```

## OAuth 2.0

```java
// Bearer token (preemptive by default in 5.x)
given().auth().oauth2(accessToken).when().get("/resource");

// Set globally
RestAssured.authentication = oauth2(accessToken);
```

## Form Authentication

```java
given().
    auth().form("username", "password", new FormAuthConfig("/login", "user", "pass")).
when().
    get("/protected-resource");

// With Spring Security
given().
    auth().form("John", "Doe", FormAuthConfig.springSecurity()).
when().
    get("/formAuth");
```

## SSL / HTTPS

```java
// Relaxed HTTPS validation (for self-signed certs in test environments)
given().relaxedHTTPSValidation().when().get("https://self-signed.example.com/api");

// Set globally
RestAssured.useRelaxedHTTPSValidation();

// Custom keystore
given().keystore("/path/to/keystore.jks", "password").when().get("https://secure.example.com");
```

## Session Management

```java
SessionFilter sessionFilter = new SessionFilter();

// First request — captures session
given().
    auth().form("admin", "password").
    filter(sessionFilter).
when().
    get("/login");

// Subsequent requests — reuses session automatically
given().
    filter(sessionFilter).
when().
    get("/api/parts/").
then().
    statusCode(200);
```
