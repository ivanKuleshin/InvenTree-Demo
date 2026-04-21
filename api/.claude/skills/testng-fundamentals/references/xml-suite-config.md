### Basic testng.xml Structure

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="My Test Suite" verbose="1">

    <test name="Unit Tests">
        <classes>
            <class name="com.example.tests.UserServiceTest"/>
            <class name="com.example.tests.ProductServiceTest"/>
        </classes>
    </test>

    <test name="Integration Tests">
        <packages>
            <package name="com.example.integration.*"/>
        </packages>
    </test>

</suite>
```

### Group Configuration

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Group Suite">

    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <packages>
            <package name="com.example.tests.*"/>
        </packages>
    </test>

    <test name="Regression Tests">
        <groups>
            <run>
                <include name="regression"/>
                <exclude name="broken"/>
            </run>
        </groups>
        <packages>
            <package name="com.example.tests.*"/>
        </packages>
    </test>

</suite>
```

### Parameters in testng.xml

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Parameterized Suite">

    <parameter name="browser" value="chrome"/>
    <parameter name="environment" value="staging"/>

    <test name="Chrome Tests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="com.example.tests.BrowserTest"/>
        </classes>
    </test>

    <test name="Firefox Tests">
        <parameter name="browser" value="firefox"/>
        <classes>
            <class name="com.example.tests.BrowserTest"/>
        </classes>
    </test>

</suite>
```

Using parameters in tests:

```java
import org.testng.annotations.*;

public class BrowserTest {

    @Parameters({"browser", "environment"})
    @BeforeClass
    public void setUp(String browser, @Optional("production") String env) {
        System.out.println("Browser: " + browser);
        System.out.println("Environment: " + env);
    }

    @Test
    public void testBrowser() {
        // Test implementation
    }
}
```