# Memory Index

- [Dynamic test data — no hardcoded IDs or field values](feedback_dynamic_test_data.md) — live env data changes; always create or find data dynamically, never copy PKs/values from manual TCs
- [POJO models with Jackson — required annotations](feedback_pojo_jackson.md) — @Getter + @Builder + @JsonInclude(NON_NULL) + @JsonProperty; missing @Getter silently breaks Jackson convertValue/serialization
