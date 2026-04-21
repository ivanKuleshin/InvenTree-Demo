---
name: Dynamic test data — no hardcoded IDs or field values
description: All test data must be sourced dynamically; hardcoded part PKs, names, or field values from manual TCs are forbidden in automation
type: feedback
originSessionId: 6898bc09-a81f-4ea2-b190-81e85816df25
---
Never hardcode part PKs, names, or other field values copied from manual test cases into automated tests. The target environment (demo.inventree.org) is a shared live env where data changes at any time — a part that had no category yesterday may have one today, a name may be edited, a PK may be recycled.

**Why:** TC-APCRUD-002 and TC-APCRUD-003 both failed in production because they hardcoded PK 82 and its observed field values from a manual probe. The live part's name and category changed, breaking assertions.

**How to apply:**
- If you need controlled field values → create a new part via Admin in the test body, add its pk to `createdPartIds` for cleanup, then assert against the values you set.
- If you need a part with specific characteristics (has parameters, has a category, etc.) → find one dynamically at test runtime using a helper (e.g. `findCategorizedPartPk()`, `findPartWithParametersPk()`).
- Manual TC docs may contain observed values (IDs, names) from a specific probe date — treat these as illustrative only, never copy them as constants into automation.
