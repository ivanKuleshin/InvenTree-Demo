# Running Tests

### All Tests

```bash
mvn test
```

### Single Test Class

```bash
mvn test -Dtest=PartCrudTest
```

### Single Test Method

```bash
mvn test -Dtest=PartCrudTest#testCreatePart
```

### Tests by Pattern

```bash
mvn test -Dtest=Part*Test
```

### With Allure Report

```bash
mvn clean test allure:serve
```

This compiles, runs tests, and opens the Allure HTML report in your browser.