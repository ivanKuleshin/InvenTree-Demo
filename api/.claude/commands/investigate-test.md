Investigate and fix the following failing test: $ARGUMENTS

Execute the pipeline below in strict order. Do not skip any step.
Do not attempt to fix anything directly in the main session — delegate every step.

---

## Step 1 — Run the failing test and capture evidence

Invoke @test-runner with this exact input:

```
Execute this specific test and return a structured failure report:

Test: [extract test name from $ARGUMENTS]
Full error provided by user:

$ARGUMENTS

Run the test in isolation. Parse the output into the structured failure report format.
Extract: HTTP method, full URL, query params, response status, response body,
expected value, assertion error, test file path, and inferred module name.
```

Wait for the full failure report before proceeding.
If the test unexpectedly passes, report it to the user and stop — it may be flaky.

---

## Step 2 — Retrieve documentation for the failing module

Invoke @requirement-researcher with this exact input:

```
MODE: INVESTIGATION

Retrieve requirements for the module identified in the test failure below.
Focus on: valid parameter values, constraints, error responses, and edge cases.

[paste full failure report from Step 1]

Key details to search for:
- Module: [inferred module from test-runner report]
- Endpoint: [API path from test-runner report]
- Parameters: [query params from test-runner report]
- Error received: [response body from test-runner report]
```

Wait for the requirements block.
If STATUS: NOT FOUND — continue to Step 3 anyway, noting that docs are unavailable.
The investigator must handle the no-docs case explicitly.

---

## Step 3 — Investigate root cause and resolve

Invoke @test-investigator with this exact input:

```
Investigate this test failure and determine root cause.

--- TEST FAILURE REPORT (from test-runner) ---
[paste full output from Step 1]

--- REQUIREMENTS / DOCUMENTATION (from requirement-researcher) ---
[paste full output from Step 2]

--- ORIGINAL FAILURE FROM USER ---
$ARGUMENTS

Instructions:
1. Analyse the conflict between actual API behaviour and documentation
2. Confirm your hypothesis with additional API probes if needed
3. If documentation and actual behaviour conflict → STOP and present the
   conflict to the human with options A / B / C / D before making any changes
4. Do NOT implement any test fix yourself — delegate ALL test file changes to
   the test-automation agent with a precise description of what to change and why
5. Verify the fix by re-running the test using test-runner agent
6. Produce the final investigation report
```

---

## Step 4 — Present outcome to user

After @test-investigator completes (or pauses for human input at the decision gate):

If a human decision was requested (conflict detected):
- Surface the conflict clearly with all options
- Wait for the human to reply (A, B, C, or D)
- Forward the decision back to @test-investigator to complete the fix

If resolved automatically:
- Present the Final Investigation Report
- List any follow-up actions the human needs to take (doc updates, bug tracking, etc.)
