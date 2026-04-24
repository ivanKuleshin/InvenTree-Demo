# Response Extraction and POJO Mapping

## Extract Single Values

```java
int partId = given().
    spec(requestSpec).
when().
    get("/api/parts/1").
then().
    statusCode(200).
    extract().path("pk");

String name = get("/api/parts/1").path("name");
```

## Extract Entire Response

```java
Response response = given().
    spec(requestSpec).
when().
    get("/api/parts/").
then().
    statusCode(200).
    extract().response();

int count = response.path("count");
String firstPartName = response.path("results[0].name");
```

## JsonPath Standalone Usage

```java
String json = get("/api/parts/1").asString();
JsonPath jsonPath = new JsonPath(json);

String name = jsonPath.getString("name");
int category = jsonPath.getInt("category");
List<String> tags = jsonPath.getList("tags", String.class);
```

## Map JSON to POJO

```java
// Entire response body
Part part = get("/api/parts/1").as(Part.class);

// Nested path to POJO
Part part = from(json).getObject("results[0]", Part.class);

// List of POJOs
List<Part> parts = from(json).getList("results", Part.class);
```

## Sending POJOs as Request Bodies

REST Assured auto-serializes POJOs to JSON when `contentType` is set:

```java
Part newPart = new Part();
newPart.setName("Capacitor 100uF");
newPart.setCategory(3);
newPart.setActive(true);

given().
    contentType(ContentType.JSON).
    body(newPart).                      // Auto-serialized to JSON via Jackson
when().
    post("/api/parts/").
then().
    statusCode(201).
    body("name", equalTo("Capacitor 100uF"));
```

## Specifying an Object Mapper

```java
// Explicit mapper type
given().body(myObject, ObjectMapperType.JACKSON_2).when().post("/endpoint");

// Global configuration
RestAssured.config = RestAssured.config()
    .objectMapperConfig(
        ObjectMapperConfig.objectMapperConfig()
            .defaultObjectMapperType(ObjectMapperType.JACKSON_2)
    );
```

## XML Validation and Extraction

```java
// XPath assertions
given().
    params("firstName", "John", "lastName", "Doe").
when().
    post("/greetMe").
then().
    body(hasXPath("/greeting/firstName[text()='John']"));

// XSD schema validation
when().get("/api/data.xml").then().body(matchesXsd(xsdContent));

// DTD validation
when().get("/api/data.xml").then().body(matchesDtd(dtdContent));

// XmlPath extraction
String xml = get("/api/data.xml").asString();
Node node = from(xml).get("root.child[0]");
```
