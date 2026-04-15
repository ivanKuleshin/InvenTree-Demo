The `@Test` annotation supports various attributes:

```java
public class TestAttributesExample {

    @Test(description = "Verifies user login functionality")
    public void testLogin() {
        // Test with description
    }

    @Test(enabled = false)
    public void disabledTest() {
        // This test will not run
    }

    @Test(priority = 1)
    public void firstTest() {
        // Runs first (lower priority = earlier execution)
    }

    @Test(priority = 2)
    public void secondTest() {
        // Runs second
    }

    @Test(groups = {"smoke", "regression"})
    public void groupedTest() {
        // Test belongs to multiple groups
    }

    @Test(dependsOnMethods = {"testLogin"})
    public void testDashboard() {
        // Runs only if testLogin passes
    }

    @Test(dependsOnGroups = {"setup"})
    public void dependentTest() {
        // Runs only if all tests in "setup" group pass
    }

    @Test(timeOut = 5000)
    public void timedTest() {
        // Fails if takes more than 5 seconds
    }

    @Test(invocationCount = 3)
    public void repeatedTest() {
        // Runs 3 times
    }

    @Test(invocationCount = 100, threadPoolSize = 10)
    public void parallelRepeatedTest() {
        // Runs 100 times across 10 threads
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionTest() {
        throw new IllegalArgumentException("Expected");
    }

    @Test(expectedExceptions = RuntimeException.class,
        expectedExceptionsMessageRegExp = ".*invalid.*")
    public void exceptionWithMessageTest() {
        throw new RuntimeException("This is invalid input");
    }
}
```