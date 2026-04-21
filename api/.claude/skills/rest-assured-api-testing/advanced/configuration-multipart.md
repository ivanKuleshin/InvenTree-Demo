# Configuration and Multipart

## Global Configuration (RestAssured.config)

```java
RestAssured.config = RestAssured.config()
    // Redirect behavior
    .redirect(redirectConfig().followRedirects(false).maxRedirects(0))
    // JSON number handling
    .jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
    // Parameter strategy
    .paramConfig(paramConfig().queryParamsUpdateStrategy(UpdateStrategy.REPLACE))
    // Encoder settings
    .encoderConfig(encoderConfig().defaultContentCharset("UTF-8"))
    // Logging
    .logConfig(logConfig()
        .enableLoggingOfRequestAndResponseIfValidationFails()
        .blacklistHeader("Authorization"));
```

## Global Defaults

```java
// Set once in @BeforeSuite or BaseTest
RestAssured.baseURI = "https://demo.inventree.org";
RestAssured.basePath = "/api";
RestAssured.port = 443;
RestAssured.authentication = preemptive().basic("admin", "password");

// Reset after test suite
@AfterSuite
public void tearDown() {
    RestAssured.reset();
}
```

## Multipart / File Upload

```java
// Upload a file
given().
    multiPart(new File("src/test/resources/data.csv")).
when().
    post("/api/upload");

// Upload with control name and MIME type
given().
    multiPart("attachment", new File("doc.pdf"), "application/pdf").
when().
    post("/api/parts/1/attachments/");

// Serialize POJO as multipart JSON
given().
    multiPart("metadata", myObject, "application/json").
    multiPart("file", new File("data.csv")).
when().
    post("/api/import");

// Specify ObjectMapper for multipart serialization
given().
    multiPart(new MultiPartSpecBuilder(greeting, ObjectMapperType.GSON)
        .fileName("data.csv")
        .controlName("text")
        .mimeType("application/vnd.ms-excel").build()).
when().
    post("/multipart/text");
```
