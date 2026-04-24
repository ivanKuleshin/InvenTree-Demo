---
name: POJO models with Jackson — required annotations
description: Rules for creating query param and request POJOs used with Jackson serialization in this project
type: feedback
originSessionId: 6898bc09-a81f-4ea2-b190-81e85816df25
---
All model POJOs (query params, request bodies) that are serialized by Jackson must follow this pattern:

- `@Getter` — required so Jackson can discover properties via public getters. Without it, `convertValue()` and serialization produce an empty object `{}`, silently dropping all fields.
- `@Builder` — for construction at call sites.
- `@JsonInclude(JsonInclude.Include.NON_NULL)` — exclude null fields so optional params are not sent.
- `@JsonProperty("snake_case_name")` — on any field whose Java name differs from the API param name (e.g., `isTemplate` → `"is_template"`, `ipn` → `"IPN"`).
- Do NOT use `@Data` or `@AllArgsConstructor` for query param models — `@Getter` + `@Builder` is sufficient and avoids unnecessary equals/hashCode/toString bloat.

**Why:** `PartListParams` was created with `@Builder` and `@JsonInclude` but without `@Getter`. Jackson's `MAPPER.convertValue(params, Map.class)` found no accessible properties and produced `{}`, so no query params (including the required `limit`) were sent to the API. Tests failed with deserialization errors because the API response format changed (no paginated wrapper when no `limit` param).

**How to apply:** Any time a new POJO is created for use with `BaseClient.executeGet` (or any Jackson serialization path), always add `@Getter`. Verify by checking: does the class have either `@Getter`, `@Data`, or manually written getters?
