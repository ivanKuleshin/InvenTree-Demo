# Allure Reporting Integration

## Allure REST Assured Filter

```java
import io.qameta.allure.restassured.AllureRestAssured;

// Per-request
given().
    filter(new AllureRestAssured()).
    spec(requestSpec).
when().
    get("/part/1/").
then().
    statusCode(200);

// Global (preferred — set once in BaseTest)
RestAssured.filters(new AllureRestAssured());
```

## Allure Annotations on API Tests

```java
import io.qameta.allure.*;

@Epic("Part Management")
@Feature("Part CRUD")
public class PartApiTest extends BaseTest {

    @Test
    @Story("Create Part")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a valid part can be created via POST /api/part/")
    public void testCreatePart() {
        // ...
    }
}
```

## Maven Dependency

```xml
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```
