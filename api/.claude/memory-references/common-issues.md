# Common Issues

**Q: Tests fail with "Missing required config key: api.base.url"**

- A: Ensure `config.properties` is on the classpath in `src/main/resources/`.

**Q: Authentication fails even with correct credentials**

- A: Check `ApiConfig.getAuthStrategy()` — must match the InvenTree instance auth method (TOKEN vs BASIC). Verify token
  is cached in `BaseTest.initFramework()` logs.

**Q: Jackson serialization fails silently**

- A: Verify models have `@Getter` annotation. Without it, Jackson cannot read fields.

**Q: Tests pass locally but fail in CI**

- A: Check environment variables and `config.properties` overrides. InvenTree demo may change; use dynamic test data
  discovery, not hardcoded IDs.

**Q: Allure report is empty**

- A: Run `mvn clean verify allure:serve` (include `clean` to reset Allure results directory).