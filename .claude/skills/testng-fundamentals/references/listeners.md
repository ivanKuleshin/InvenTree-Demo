### ITestListener

```java
import org.testng.*;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Failed: " + result.getName());
        System.out.println("Reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test suite starting: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test suite finished: " + context.getName());
    }
}
```

### Using Listeners

```java
// Using annotation
@Listeners(TestListener.class)
public class MyTest {
    @Test
    public void testMethod() {
        // Test implementation
    }
}
```

Or in testng.xml:

```xml

<suite name="Suite">
    <listeners>
        <listener class-name="com.example.listeners.TestListener"/>
    </listeners>
    <test name="Test">
        <classes>
            <class name="com.example.tests.MyTest"/>
        </classes>
    </test>
</suite>
```