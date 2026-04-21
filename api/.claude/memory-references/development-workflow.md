# Development Workflow

1. **Read InvenTree API docs** — Understand endpoint behavior, request/response schemas
2. **Create POJO models** — Annotate with `@Getter`, `@Builder`, `@JsonInclude`, `@JsonProperty`
3. **Extend services** — Add methods calling REST Assured, returning domain models
4. **Build test data** — Use builders in `testdata/` for clean, reusable setup
5. **Write test** — Extend `BaseTest`, follow Given/When/Then, assert with `ResponseValidator`
6. **Cleanup** — Use `@AfterMethod` to delete created resources
7. **Run and report** — `mvn test`, view report with `mvn allure:serve`