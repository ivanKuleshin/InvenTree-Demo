# TC_AUTH_READER — Login Test Suite: Reader User

**Application:** InvenTree Demo  
**URL:** https://demo.inventree.org  
**User under test:** `reader` / `readonly` (Display name: Ronald Reader)  
**Approach:** Exploratory + Ad-hoc  
**Created:** 2026-04-13

---

## Template Legend

| Field                | Description                                  |
| -------------------- | -------------------------------------------- |
| **ID**               | Unique test case identifier                  |
| **Priority**         | High / Medium / Low                          |
| **Type**             | Functional / Exploratory / Ad-hoc / Security |
| **Preconditions**    | State required before execution              |
| **Steps**            | Action → Expected Result pairs               |
| **Automation Hints** | Selectors and assertions for implementation  |

---

## TC-AUTH-R-001: Login page renders all required elements

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Browser is open, user is not logged in
- Navigate to `https://demo.inventree.org/web/login`

**Steps:**

| #   | Action                       | Expected Result                                                                                                      |
| --- | ---------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| 1   | Load the login page URL      | Page loads with title "InvenTree Demo Server"                                                                        |
| 2   | Inspect the form             | Username field is visible with placeholder "Your username" and marked as required (\*)                               |
| 3   | Inspect the password field   | Password field is visible with placeholder "Your password", input is masked (type=password), marked as required (\*) |
| 4   | Inspect the show/hide toggle | A toggle button is visible adjacent to the password field                                                            |
| 5   | Inspect action buttons       | "Log In" button and "Send me an email" button are both visible                                                       |
| 6   | Inspect the footer text      | Text "InvenTree demo instance - Click here for login details" is visible with a clickable link                       |
| 7   | Inspect the version footer   | Version string (e.g., "1.4.0 dev") is visible at the bottom                                                          |

**Automation Hints:**

- Username field selector: `role=textbox[name="login-username"]`
- Password field selector: `role=textbox[name="login-password"]`
- Login button selector: `role=button[name="Log In"]`
- Assert page URL contains `/web/login`
- Assert username field has attribute `placeholder="Your username"`

---

## TC-AUTH-R-002: Successful login with valid reader credentials

**Priority:** High  
**Type:** Functional

**Preconditions:**

- User is not logged in
- Login page is open at `/web/login`

**Steps:**

| #   | Action                                          | Expected Result                                              |
| --- | ----------------------------------------------- | ------------------------------------------------------------ |
| 1   | Enter username `reader` in the username field   | Field value is "reader"                                      |
| 2   | Enter password `readonly` in the password field | Field value is masked                                        |
| 3   | Click the "Log In" button                       | Form is submitted                                            |
| 4   | Wait for redirect                               | URL changes to `https://demo.inventree.org/web/home`         |
| 5   | Verify page title                               | Browser tab shows "InvenTree Demo Server \| Dashboard"       |
| 6   | Verify user display name                        | Top-right button shows "Ronald Reader"                       |
| 7   | Verify page heading                             | Dashboard area shows "InvenTree Demo Server - Ronald Reader" |

**Automation Hints:**

- Fill username: `page.fill('[name="login-username"]', 'reader')`
- Fill password: `page.fill('[name="login-password"]', 'readonly')`
- Click login: `page.click('button:has-text("Log In")')`
- Assert URL: `expect(page).toHaveURL('/web/home')`
- Assert display name: `expect(page.locator('button:has-text("Ronald Reader")')).toBeVisible()`

---

## TC-AUTH-R-003: Post-login dashboard state for reader user

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `reader`

**Steps:**

| #   | Action                    | Expected Result                                                               |
| --- | ------------------------- | ----------------------------------------------------------------------------- |
| 1   | Observe top navigation    | All 6 tabs visible: Dashboard, Parts, Stock, Manufacturing, Purchasing, Sales |
| 2   | Observe the header area   | NO "Superuser Mode" alert banner is shown                                     |
| 3   | Observe header buttons    | NO "open-alerts" (bell/alert) button is visible                               |
| 4   | Observe dashboard widgets | "Requires Superuser" alert is shown for restricted widgets                    |
| 5   | Observe header buttons    | Notifications button (with badge) is visible                                  |

**Automation Hints:**

- Assert no superuser banner: `expect(page.locator('text=Superuser Mode')).not.toBeVisible()`
- Assert no alerts button: `expect(page.locator('[aria-label="open-alerts"]')).not.toBeVisible()`
- Assert superuser widget alert: `expect(page.locator('text=Requires Superuser')).toBeVisible()`
- Assert all nav tabs: loop through `['Dashboard', 'Parts', 'Stock', 'Manufacturing', 'Purchasing', 'Sales']` and check each tab is visible

---

## TC-AUTH-R-004: Password show/hide toggle on login page

**Priority:** Medium  
**Type:** Functional / Exploratory

**Preconditions:**

- Login page is open, user is not logged in

**Steps:**

| #   | Action                                                       | Expected Result                             |
| --- | ------------------------------------------------------------ | ------------------------------------------- |
| 1   | Enter any text in the password field                         | Text is masked (shown as dots/asterisks)    |
| 2   | Click the show/hide toggle button next to the password field | Password text becomes visible in plain text |
| 3   | Click the toggle button again                                | Password text is masked again               |

**Automation Hints:**

- Toggle button selector: button adjacent to `[name="login-password"]`
- Assert input type changes: `expect(page.locator('[name="login-password"]')).toHaveAttribute('type', 'text')` after toggle

---

## TC-AUTH-R-005: Empty form submission is blocked

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Login page is open, both fields are empty

**Steps:**

| #   | Action                                   | Expected Result                                                                    |
| --- | ---------------------------------------- | ---------------------------------------------------------------------------------- |
| 1   | Leave username and password fields empty | Fields show placeholder text                                                       |
| 2   | Click "Log In" button                    | Form does not submit; browser-native validation triggers                           |
| 3   | Observe validation                       | "Please fill out this field" (or equivalent) message appears on the password field |
| 4   | Verify URL                               | URL remains `/web/login` — no redirect occurs                                      |

**Automation Hints:**

- Assert URL unchanged: `expect(page).toHaveURL('/web/login')`
- Check HTML5 validation via `page.evaluate(() => document.querySelector('[name="login-password"]').validity.valueMissing)`

---

## TC-AUTH-R-006: Login with valid username but empty password

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                               | Expected Result                                |
| --- | ------------------------------------ | ---------------------------------------------- |
| 1   | Enter `reader` in the username field | Field shows "reader"                           |
| 2   | Leave the password field empty       | Field is empty                                 |
| 3   | Click "Log In"                       | Form submission is blocked                     |
| 4   | Observe validation                   | Browser validation fires on the password field |
| 5   | Verify URL                           | Stays on `/web/login`                          |

**Automation Hints:**

- Same as TC-AUTH-R-005 but with username pre-filled

---

## TC-AUTH-R-007: Login with invalid credentials shows error toast

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                  | Expected Result                                                                                             |
| --- | --------------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| 1   | Enter `reader` in username field        | Field shows "reader"                                                                                        |
| 2   | Enter `wrongpassword` in password field | Field shows masked text                                                                                     |
| 3   | Click "Log In"                          | Request is submitted to the server                                                                          |
| 4   | Observe error notification              | Toast notification appears with message "Login failed (400)" and sub-text "Check your input and try again." |
| 5   | Observe form state                      | Username field retains the value "reader"                                                                   |
| 6   | Verify URL                              | URL remains `/web/login`                                                                                    |

**Automation Hints:**

- Assert toast visible: `expect(page.locator('text=Login failed (400)')).toBeVisible()`
- Assert sub-text: `expect(page.locator('text=Check your input and try again')).toBeVisible()`
- Assert URL: `expect(page).toHaveURL('/web/login')`

---

## TC-AUTH-R-008: Login with wrong user credentials (admin password for reader)

**Priority:** Medium  
**Type:** Ad-hoc / Exploratory

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                                 | Expected Result                                            |
| --- | ------------------------------------------------------ | ---------------------------------------------------------- |
| 1   | Enter `reader` in username field                       |                                                            |
| 2   | Enter `inventree` (admin's password) in password field |                                                            |
| 3   | Click "Log In"                                         |                                                            |
| 4   | Observe result                                         | Login fails; error toast appears with "Login failed (400)" |
| 5   | Verify URL stays on login page                         | Not redirected to dashboard                                |

**Automation Hints:**

- Same as TC-AUTH-R-007 with different password value

---

## TC-AUTH-R-009: Username field is case-sensitive

**Priority:** Medium  
**Type:** Ad-hoc / Exploratory

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                           | Expected Result                                                                |
| --- | ------------------------------------------------ | ------------------------------------------------------------------------------ |
| 1   | Enter `READER` (uppercase) in the username field |                                                                                |
| 2   | Enter `readonly` in password field               |                                                                                |
| 3   | Click "Log In"                                   |                                                                                |
| 4   | Observe result                                   | Verify whether login succeeds or fails (document actual behavior)              |
| 5   | Repeat with `Reader` (mixed case)                |                                                                                |
| 6   | Document actual behavior                         | Note: InvenTree uses Django auth; usernames may be case-insensitive by default |

**Automation Hints:**

- Test variations: `READER`, `Reader`, `rEaDer`
- Assert consistent behavior across all variations

---

## TC-AUTH-R-010: Logout flow for reader user

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `reader`, on any page

**Steps:**

| #   | Action                                               | Expected Result                                                          |
| --- | ---------------------------------------------------- | ------------------------------------------------------------------------ |
| 1   | Click the "Ronald Reader" button in top-right corner | Dropdown menu opens                                                      |
| 2   | Verify menu items                                    | Menu contains: User Settings, Change Color Mode, About InvenTree, Logout |
| 3   | Verify absent menu items                             | System Settings and Admin Center are NOT in the menu                     |
| 4   | Click "Logout"                                       | Menu closes; request to logout is sent                                   |
| 5   | Verify redirect                                      | URL changes to `/web/login`                                              |
| 6   | Verify session cleared                               | Login page is shown with empty fields                                    |

**Automation Hints:**

- Click user menu: `page.click('button:has-text("Ronald Reader")')`
- Assert no System Settings: `expect(page.locator('text=System Settings')).not.toBeVisible()`
- Assert no Admin Center: `expect(page.locator('text=Admin Center')).not.toBeVisible()`
- Click logout: `page.click('[role=menuitem]:has-text("Logout")')`
- Assert URL: `expect(page).toHaveURL('/web/login')`

---

## TC-AUTH-R-011: Accessing protected URL after logout redirects to login

**Priority:** High  
**Type:** Functional / Security

**Preconditions:**

- Reader user has just logged out (on `/web/login`)

**Steps:**

| #   | Action                                                     | Expected Result                |
| --- | ---------------------------------------------------------- | ------------------------------ |
| 1   | Directly navigate to `https://demo.inventree.org/web/home` |                                |
| 2   | Observe redirect                                           | Page redirects to `/web/login` |
| 3   | Directly navigate to `https://demo.inventree.org/web/part` |                                |
| 4   | Observe redirect                                           | Page redirects to `/web/login` |

**Automation Hints:**

- After logout, navigate to protected URL: `page.goto('/web/home')`
- Assert redirect to login: `expect(page).toHaveURL(/.*\/web\/login/)`

---

## TC-AUTH-R-012: SQL injection attempt in login fields

**Priority:** Medium  
**Type:** Security / Ad-hoc

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                               | Expected Result                                                                                         |
| --- | ---------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| 1   | Enter `' OR '1'='1` in the username field            |                                                                                                         |
| 2   | Enter `' OR '1'='1` in the password field            |                                                                                                         |
| 3   | Click "Log In"                                       |                                                                                                         |
| 4   | Observe result                                       | Login fails with error toast; application does NOT authenticate; no server crash or unexpected behavior |
| 5   | Enter `admin'--` in username field with any password |                                                                                                         |
| 6   | Click "Log In"                                       | Login fails; no bypass                                                                                  |

**Automation Hints:**

- Assert no authentication: `expect(page).toHaveURL('/web/login')` after each attempt
- Assert error toast appears

---

## TC-AUTH-R-013: XSS attempt in username field

**Priority:** Medium  
**Type:** Security / Ad-hoc

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                                      | Expected Result                                                     |
| --- | ----------------------------------------------------------- | ------------------------------------------------------------------- |
| 1   | Enter `<script>alert('xss')</script>` in the username field |                                                                     |
| 2   | Enter any text in the password field                        |                                                                     |
| 3   | Click "Log In"                                              |                                                                     |
| 4   | Observe result                                              | No JavaScript alert dialog appears; login fails with standard error |

**Automation Hints:**

- `page.on('dialog', () => { throw new Error('XSS alert triggered!') })`
- Assert no dialog triggered
- Assert page URL remains `/web/login`

---

## TC-AUTH-R-014: Very long credentials do not crash the form

**Priority:** Low  
**Type:** Ad-hoc / Boundary

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                             | Expected Result                                                           |
| --- | -------------------------------------------------- | ------------------------------------------------------------------------- |
| 1   | Enter a 500-character string in the username field | Field accepts input                                                       |
| 2   | Enter a 500-character string in the password field | Field accepts input                                                       |
| 3   | Click "Log In"                                     |                                                                           |
| 4   | Observe result                                     | Login fails gracefully (error toast or validation); no crash or 500 error |

**Automation Hints:**

- Generate long string: `'a'.repeat(500)`
- Assert no 5xx error; page remains functional

---

## TC-AUTH-R-015: "Send me an email" button is present and interactive

**Priority:** Low  
**Type:** Exploratory

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                                  | Expected Result                                                            |
| --- | ------------------------------------------------------- | -------------------------------------------------------------------------- |
| 1   | Observe the "Send me an email" button                   | Button is visible on the login form                                        |
| 2   | Click the button with an empty username field           | Observe behavior (may prompt for email, show message, or require username) |
| 3   | Enter `reader` in username and click "Send me an email" | Observe behavior — document actual result                                  |

**Automation Hints:**

- Assert button visible: `expect(page.locator('button:has-text("Send me an email")')).toBeVisible()`
- Document actual behavior for regression baseline

---

## TC-AUTH-R-016: Language toggle is visible on login page

**Priority:** Low  
**Type:** Exploratory

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                             | Expected Result                   |
| --- | ---------------------------------- | --------------------------------- |
| 1   | Observe the login page footer area | Language toggle button is visible |
| 2   | Click the language toggle          | Language options are displayed    |

**Automation Hints:**

- Assert toggle: `expect(page.locator('[aria-label*="language"]')).toBeVisible()` (verify actual aria-label)
