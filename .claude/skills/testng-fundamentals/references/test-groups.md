### Defining and Using Groups

```java
public class GroupExample {

    @BeforeGroups("database")
    public void setUpDatabase() {
        System.out.println("Setting up database");
    }

    @AfterGroups("database")
    public void tearDownDatabase() {
        System.out.println("Tearing down database");
    }

    @Test(groups = {"smoke", "frontend"})
    public void testHomePage() {
        System.out.println("Testing home page");
    }

    @Test(groups = {"smoke", "api"})
    public void testHealthEndpoint() {
        System.out.println("Testing health endpoint");
    }

    @Test(groups = {"regression", "database"})
    public void testDataPersistence() {
        System.out.println("Testing data persistence");
    }

    @Test(groups = {"slow", "integration"})
    public void testEndToEnd() {
        System.out.println("Testing end-to-end flow");
    }
}
```

### Group Dependencies (not recommended)

```java
public class GroupDependencyExample {

    @Test(groups = {"init"})
    public void initializeSystem() {
        System.out.println("Initializing");
    }

    @Test(groups = {"init"})
    public void configureSystem() {
        System.out.println("Configuring");
    }

    @Test(dependsOnGroups = {"init"}, groups = {"core"})
    public void coreTest1() {
        System.out.println("Core test 1");
    }

    @Test(dependsOnGroups = {"init"}, groups = {"core"})
    public void coreTest2() {
        System.out.println("Core test 2");
    }

    @Test(dependsOnGroups = {"core"}, groups = {"final"})
    public void finalTest() {
        System.out.println("Final test");
    }
}
```
