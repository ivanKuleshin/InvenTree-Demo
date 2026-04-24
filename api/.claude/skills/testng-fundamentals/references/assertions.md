### Basic Assertions

TestNG provides comprehensive assertion methods:

```java
import org.testng.Assert;
import org.testng.annotations.Test;

public class AssertionExamples {

    @Test
    public void testBasicAssertions() {
        // Equality
        Assert.assertEquals(5, 5);
        Assert.assertEquals("hello", "hello");
        Assert.assertEquals(new int[] {1, 2, 3}, new int[] {1, 2, 3});

        // Boolean
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        // Null checks
        Assert.assertNull(null);
        Assert.assertNotNull("value");

        // Same reference
        String s1 = "test";
        String s2 = s1;
        Assert.assertSame(s1, s2);
        Assert.assertNotSame(new String("test"), new String("test"));
    }

    @Test
    public void testAssertionsWithMessages() {
        // Assertions with custom failure messages
        Assert.assertEquals(5, 5, "Values should be equal");
        Assert.assertTrue(true, "Condition should be true");
        Assert.assertNotNull("value", "Value should not be null");
    }

    @Test
    public void testCollectionAssertions() {
        // Array assertions
        String[] expected = {"a", "b", "c"};
        String[] actual = {"a", "b", "c"};
        Assert.assertEquals(actual, expected);

        // Unordered comparison
        String[] array1 = {"a", "b", "c"};
        String[] array2 = {"c", "a", "b"};
        Assert.assertEqualsNoOrder(array1, array2);
    }
}
```

### Soft Assertions

Soft assertions allow multiple assertions to be collected before failing:

```java
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SoftAssertExample {

    @Test
    public void testWithSoftAssert() {
        SoftAssert softAssert = new SoftAssert();

        // All assertions are executed
        softAssert.assertEquals(1, 1, "First check");
        softAssert.assertEquals(2, 3, "Second check - will fail");
        softAssert.assertTrue(false, "Third check - will fail");
        softAssert.assertNotNull(null, "Fourth check - will fail");

        // Report all failures at the end
        softAssert.assertAll();
    }
}
```