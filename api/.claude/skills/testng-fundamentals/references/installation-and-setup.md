### Maven Configuration

Add TestNG to your Maven project:

```xml

<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.9.0</version>
    <scope>test</scope>
</dependency>
```

Configure the Surefire plugin for TestNG:

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```

### Gradle Configuration

Add TestNG to your Gradle project:

```groovy
dependencies {
    testImplementation 'org.testng:testng:7.9.0'
}

test {
    useTestNG()
}
```