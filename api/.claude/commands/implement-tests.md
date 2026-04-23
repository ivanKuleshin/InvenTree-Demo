Run the full test implementation pipeline for: $ARGUMENTS

Execute the following steps in strict order. Do not skip any step.
Do not implement tests directly — delegate every step as described below.

---

## Step 1 — Requirements Research

Invoke @requirement-researcher with this exact input:

```
Find all requirements in /docs related to: $ARGUMENTS
Return the structured requirements block and Coverage Summary.
```

Wait for the response. If it returns STATUS: NOT FOUND, stop here and report to the user.

---

## Step 2 — Test Implementation

Invoke @test-automation with this exact input:

```
MODE: New implementation

Requirements block:
[paste full output from Step 1]

Implement test cases for all operations listed in the Coverage Summary.
Follow existing project test conventions. Report all files created.
```

Wait for the completion report.

---

## Step 3 — Code Review Loop 1

Invoke @code-reviewer with this exact input:

```
LOOP: 1 of 2

Review the following test files produced by test-automation:
[paste file list from Step 2]

Evaluate against the full checklist. Produce a structured feedback report.
Then PAUSE and wait for human approval before any fixes are applied.
```

Wait. The reviewer will pause for human input.

---

## Step 4 — Apply Fixes (only if human approves)

If the user replies "approve fixes" or "skip [N]":

Invoke @test-automation with this exact input:

```
MODE: Fix iteration — Loop 1

[paste approved feedback block from code-reviewer]

Apply only the approved fixes. Re-run lint. Report what was changed.
```

---

## Step 5 — Code Review Loop 2

Invoke @code-reviewer with this exact input:

```
LOOP: 2 of 2

Re-review the updated test files:
[paste updated file list]

Check that Loop 1 fixes were correctly applied. Check for regressions.
Then PAUSE and wait for human approval on any remaining issues.
```

---

## Step 6 — Final Verdict

Present the Final Verdict from @code-reviewer to the user.
Include: status (APPROVED / APPROVED WITH NOTES / REJECTED), files produced, issues resolved.
