# Build Commands

All commands run from the `api/` directory.

```bash
mvn clean install              # Clean build and install
mvn test                       # Run all tests via TestNG
mvn package                    # Package without running tests
mvn clean compile              # Clean compile only
mvn clean verify               # Clean build + run tests + plugins
```

**Environment:**

- Java: JDK 21 (Amazon Corretto 21)
- Maven: 3.8+
- Checkstyle: Google/Sun profiles (v13.4.0)