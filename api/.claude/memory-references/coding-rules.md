# Coding Rules

- **No comments.** Code must be self-documenting via clear naming, proper OOP structure, and assertive variable/method
  names. Avoid inline comments, block comments, and Javadoc unless the WHY is non-obvious (e.g., workarounds for
  specific bugs, subtle invariants).
- **Dynamic test data.** Never hardcode IDs or field values copied from manual test cases. Always create, fetch, or
  discover data dynamically at runtime — InvenTree's environment changes frequently.
- **POJO models.** All models must have `@Getter`, `@Builder`, `@JsonInclude(NON_NULL)`, and field-level `@JsonProperty`
  mappings. Missing `@Getter` silently breaks Jackson serialization.
- **Service layer.** All API calls go through service classes, not directly in tests. Services return domain models, not
  raw REST Assured `Response` objects (except for edge-case assertions like status codes or headers).
- **No test interdependencies.** Tests must be independent — do not assume test execution order, do not depend on state
  from other tests. Use test data builders and `@AfterMethod` cleanup.