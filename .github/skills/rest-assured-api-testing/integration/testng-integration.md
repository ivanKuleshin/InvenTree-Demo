# TestNG Integration

## Test Structure

```java
import io.restassured.response.Response;
import org.testng.annotations.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PartApiTest extends BaseTest {

    private int createdPartId;

    @Test(description = "Create a new part via POST", groups = {"smoke", "api"})
    public void testCreatePart() {
        createdPartId = given().
            spec(requestSpec).
            body("""
                {"name": "Test Part", "category": 1, "active": true}
                """).
        when().
            post("/part/").
        then().
            statusCode(201).
            body("name", equalTo("Test Part")).
            extract().path("pk");
    }

    @Test(description = "Retrieve created part by ID",
          dependsOnMethods = "testCreatePart",
          groups = {"smoke", "api"})
    public void testGetPart() {
        given().
            spec(requestSpec).
        when().
            get("/part/{id}/", createdPartId).
        then().
            statusCode(200).
            body("pk", equalTo(createdPartId)).
            body("name", equalTo("Test Part"));
    }

    @Test(description = "Delete the test part",
          dependsOnMethods = "testGetPart",
          groups = {"api"})
    public void testDeletePart() {
        given().
            spec(requestSpec).
        when().
            delete("/part/{id}/", createdPartId).
        then().
            statusCode(204);
    }
}
```

## Data-Driven API Tests with DataProvider

```java
@DataProvider(name = "invalidPayloads")
public Object[][] invalidPayloads() {
    return new Object[][] {
        {"Missing name",    "{\"category\": 1}",                400},
        {"Empty name",      "{\"name\": \"\", \"category\": 1}", 400},
        {"Invalid category","{\"name\": \"X\", \"category\": -1}", 400},
    };
}

@Test(dataProvider = "invalidPayloads",
      description = "Validate error responses for invalid part payloads")
public void testCreatePartWithInvalidData(String scenario, String body, int expectedStatus) {
    given().
        spec(requestSpec).
        body(body).
    when().
        post("/part/").
    then().
        statusCode(expectedStatus);
}
```
