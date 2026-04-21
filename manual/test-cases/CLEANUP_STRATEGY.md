# Test Cleanup Strategy

**Document Version:** 1.0
**Last Updated:** 2026-04-14

## Overview

This document defines the cleanup strategy for manual and automated test cases in the InvenTree Parts domain test suite.

## Cleanup Approaches

### 1. In-Test Cleanup (Recommended for Manual Testing)

Each test case includes explicit cleanup steps at the end:

**Example:**
```markdown
**Steps:**
1. Create part via POST /api/part/
2. Verify part exists
3. Perform test validations
4. DELETE /api/part/{id}/ to clean up test data
```

**Pros:**
- Test independence guaranteed
- No residual test data
- Clear in manual execution

**Cons:**
- Verbose test cases
- Cleanup may fail if test fails mid-execution

**Application:** API test cases TC_APCRUD_001 through TC_APCRUD_009, TC_ACCRUD_001 through TC_ACCRUD_007 include DELETE steps where appropriate.

### 2. CI Pipeline Cleanup (Recommended for Automation)

Test automation framework handles cleanup via:
- TestNG `@AfterMethod` hooks for individual test cleanup
- TestNG `@AfterClass` hooks for test suite cleanup
- Database snapshots/rollbacks between test runs (if using dedicated test environment)

**Example (Java/TestNG):**
```java
@AfterMethod
public void cleanup() {
    if (createdPartId != null) {
        given().spec(requestSpec)
            .delete("/api/part/" + createdPartId + "/")
            .then().statusCode(204);
    }
}
```

**Pros:**
- Clean test case structure
- Guaranteed cleanup even on test failure
- Centralized cleanup logic

**Cons:**
- Requires automation framework setup
- Manual testers must handle cleanup separately

**Application:** API automation agent should implement cleanup in `@AfterMethod` for all CRUD test methods.

### 3. Hybrid Approach (Current Standard)

**Manual Test Cases:** Include cleanup steps in test case documentation
**Automated Tests:** Implement cleanup in test framework hooks

## Current State (as of 2026-04-14)

**Status:** Partially standardized

**Test Cases with Cleanup:**
- TC_ACCRUD_003: Includes DELETE step for created category
- TC_APEDGE_005: Includes DELETE step for duplicate part
- TC_APCRUD_007, 008, 009: Newly created with cleanup steps

**Test Cases without Cleanup:**
- TC_APCRUD_001, 002: GET operations (read-only, no cleanup needed)
- TC_APCRUD_004, 005: PUT/PATCH operations (modify existing, cleanup optional)
- TC_APCRUD_006: DELETE operation (cleanup is the operation itself)

## Recommendations

1. **For API Test Cases (P1 Priority):**
   - Add cleanup DELETE steps to all POST test cases (TC_APCRUD_007, 008, 009 already include this)
   - Document in test case template that cleanup is required for create operations

2. **For UI Test Cases:**
   - Document cleanup via UI delete actions OR note that cleanup is manual/CI-driven
   - Consider dedicated test user account for UI testing to isolate test data

3. **For Automation Implementation:**
   - API automation agent: Implement `@AfterMethod` cleanup for all create operations
   - UI automation agent: Implement cleanup in test teardown (e.g., Playwright `test.afterEach`)
   - Store created entity IDs in test context for cleanup reference

## Template Updates

Update test case templates to include:

```markdown
## Cleanup Strategy

**Manual Testing:** Execute DELETE step at end of test case
**Automated Testing:** Framework @AfterMethod hook handles cleanup
**CI Environment:** Database snapshot restored between test runs
```

## Next Steps

1. Update TC_APCRUD_001 through TC_APCRUD_006 to add explicit cleanup steps where applicable
2. Update test case template in `.claude/skills/api-manual-testing/references/tc-template.md`
3. Document cleanup expectations in automation agent prompts
4. Validate cleanup strategy with first automation implementation

---

**References:**
- TestNG Annotations: https://testng.org/doc/documentation-main.html#annotations
- REST Assured Best Practices: See `.claude/skills/rest-assured-api-testing/`
