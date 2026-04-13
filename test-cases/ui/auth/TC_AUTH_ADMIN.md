# TC_AUTH_ADMIN — Login Test Suite: Admin User

**Application:** InvenTree Demo  
**URL:** https://demo.inventree.org  
**User under test:** `admin` / `inventree` (Display name: Adam Administrator)  
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

## TC-AUTH-A-001: Successful login with valid admin credentials

**Priority:** High  
**Type:** Functional

**Preconditions:**

- User is not logged in
- Login page is open at `/web/login`

**Steps:**

| #   | Action                                  | Expected Result                                                   |
| --- | --------------------------------------- | ----------------------------------------------------------------- |
| 1   | Enter `admin` in the username field     | Field value is "admin"                                            |
| 2   | Enter `inventree` in the password field | Field value is masked                                             |
| 3   | Click the "Log In" button               | Form is submitted                                                 |
| 4   | Wait for redirect                       | URL changes to `https://demo.inventree.org/web/home`              |
| 5   | Verify page title                       | Browser tab shows "InvenTree Demo Server \| Dashboard"            |
| 6   | Verify user display name                | Top-right button shows "Adam Administrator"                       |
| 7   | Verify page heading                     | Dashboard area shows "InvenTree Demo Server - Adam Administrator" |

**Automation Hints:**

- Fill username: `page.fill('[name="login-username"]', 'admin')`
- Fill password: `page.fill('[name="login-password"]', 'inventree')`
- Click login: `page.click('button:has-text("Log In")')`
- Assert URL: `expect(page).toHaveURL('/web/home')`
- Assert display name: `expect(page.locator('button:has-text("Adam Administrator")')).toBeVisible()`

---

## TC-AUTH-A-002: Superuser warning banner appears immediately after admin login

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`, on `/web/home`

**Steps:**

| #   | Action                                               | Expected Result                                                                                    |
| --- | ---------------------------------------------------- | -------------------------------------------------------------------------------------------------- |
| 1   | Observe the page header area immediately after login | A prominent alert banner is visible below the navigation bar                                       |
| 2   | Verify banner heading                                | Text "Superuser Mode" is displayed in the banner                                                   |
| 3   | Verify banner body text                              | Text contains "The current user has elevated privileges and should not be used for regular usage." |
| 4   | Verify dismiss button                                | A close/dismiss button (X) is visible on the banner                                                |
| 5   | Click the dismiss button                             | Banner disappears                                                                                  |
| 6   | Navigate to another page and return                  | Verify whether banner reappears (exploratory — document actual behavior)                           |

**Automation Hints:**

- Assert banner visible: `expect(page.locator('[role=alert]:has-text("Superuser Mode")')).toBeVisible()`
- Assert body text: `expect(page.locator('text=elevated privileges')).toBeVisible()`
- Dismiss: `page.locator('[role=alert]:has-text("Superuser Mode") button').click()`

---

## TC-AUTH-A-003: Admin-specific header elements are present

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`, on `/web/home`

**Steps:**

| #   | Action                            | Expected Result                                                                          |
| --- | --------------------------------- | ---------------------------------------------------------------------------------------- |
| 1   | Observe the top navigation header | All 6 module tabs are visible: Dashboard, Parts, Stock, Manufacturing, Purchasing, Sales |
| 2   | Observe right-side header buttons | "open-search" button is present                                                          |
| 3   | Observe right-side header buttons | "open-spotlight" button is present                                                       |
| 4   | Observe right-side header buttons | "barcode-scan-button-any" button is present                                              |
| 5   | Observe right-side header buttons | Notifications (bell) button is present                                                   |
| 6   | Observe right-side header buttons | **"open-alerts" button is present** (not visible for non-admin users)                    |
| 7   | Observe the user button           | Displays "Adam Administrator"                                                            |

**Automation Hints:**

- Assert alerts button: `expect(page.locator('[aria-label="open-alerts"], button[name="open-alerts"]')).toBeVisible()`
- Assert all nav tabs present and visible

---

## TC-AUTH-A-004: Admin user menu contains administrative options

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`, on any page

**Steps:**

| #   | Action                                                 | Expected Result                |
| --- | ------------------------------------------------------ | ------------------------------ |
| 1   | Click the "Adam Administrator" button in the top-right | Dropdown menu opens            |
| 2   | Verify "User Settings" option is present               | Menu item visible              |
| 3   | Verify "System Settings" option is present             | Menu item visible (admin-only) |
| 4   | Verify "Admin Center" option is present                | Menu item visible (admin-only) |
| 5   | Verify "Change Color Mode" option is present           | Menu item visible              |
| 6   | Verify "About InvenTree" option is present             | Menu item visible              |
| 7   | Verify "Logout" option is present                      | Menu item visible              |

**Automation Hints:**

- Open menu: `page.click('button:has-text("Adam Administrator")')`
- Assert System Settings: `expect(page.locator('[role=menuitem]:has-text("System Settings")')).toBeVisible()`
- Assert Admin Center: `expect(page.locator('[role=menuitem]:has-text("Admin Center")')).toBeVisible()`

---

## TC-AUTH-A-005: System Settings accessible to admin

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`

**Steps:**

| #   | Action                                          | Expected Result                                                       |
| --- | ----------------------------------------------- | --------------------------------------------------------------------- |
| 1   | Click the "Adam Administrator" user menu button | Menu opens                                                            |
| 2   | Click "System Settings"                         | Navigates to system settings page                                     |
| 3   | Verify page loads                               | System settings content is displayed (not a 403 or redirect to login) |

**Automation Hints:**

- `page.click('[role=menuitem]:has-text("System Settings")')`
- Assert URL contains `/system` or `/settings`
- Assert settings content is visible (not error page)

---

## TC-AUTH-A-006: Admin Center accessible to admin

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`

**Steps:**

| #   | Action                                          | Expected Result                   |
| --- | ----------------------------------------------- | --------------------------------- |
| 1   | Click the "Adam Administrator" user menu button | Menu opens                        |
| 2   | Click "Admin Center"                            | Navigates to admin center         |
| 3   | Verify page loads                               | Admin center content is displayed |

**Automation Hints:**

- `page.click('[role=menuitem]:has-text("Admin Center")')`
- Assert admin content visible; no permission error

---

## TC-AUTH-A-007: Admin login with wrong password shows error toast

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Login page is open, user is not logged in

**Steps:**

| #   | Action                                      | Expected Result                                                               |
| --- | ------------------------------------------- | ----------------------------------------------------------------------------- |
| 1   | Enter `admin` in the username field         |                                                                               |
| 2   | Enter `wrongpassword` in the password field |                                                                               |
| 3   | Click "Log In"                              | Request sent                                                                  |
| 4   | Observe error notification                  | Toast appears with "Login failed (400)" and "Check your input and try again." |
| 5   | Verify URL remains `/web/login`             | No redirect to dashboard                                                      |
| 6   | Verify username field retains value         | "admin" is still in the username field                                        |

**Automation Hints:**

- Assert toast: `expect(page.locator('text=Login failed (400)')).toBeVisible()`
- Assert URL: `expect(page).toHaveURL('/web/login')`

---

## TC-AUTH-A-008: Admin login using another user's password

**Priority:** Medium  
**Type:** Ad-hoc

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                                     | Expected Result                                 |
| --- | ---------------------------------------------------------- | ----------------------------------------------- |
| 1   | Enter `admin` in the username field                        |                                                 |
| 2   | Enter `readonly` (reader's password) in the password field |                                                 |
| 3   | Click "Log In"                                             |                                                 |
| 4   | Observe result                                             | Login fails; "Login failed (400)" toast appears |

**Automation Hints:**

- Same assertion pattern as TC-AUTH-A-007

---

## TC-AUTH-A-009: Logout flow for admin user

**Priority:** High  
**Type:** Functional

**Preconditions:**

- Logged in as `admin`, on any page

**Steps:**

| #   | Action                                             | Expected Result                                  |
| --- | -------------------------------------------------- | ------------------------------------------------ |
| 1   | Click the "Adam Administrator" button in top-right | Dropdown menu opens                              |
| 2   | Click "Logout"                                     | Session termination request sent                 |
| 3   | Verify redirect                                    | URL changes to `/web/login`                      |
| 4   | Verify session cleared                             | Login page is shown with empty credential fields |
| 5   | Observe login page                                 | No user-specific content remains; page is clean  |

**Automation Hints:**

- `page.click('[role=menuitem]:has-text("Logout")')`
- Assert URL: `expect(page).toHaveURL('/web/login')`
- Assert username field empty: `expect(page.locator('[name="login-username"]')).toHaveValue('')`

---

## TC-AUTH-A-010: Post-logout protected URL redirects to login

**Priority:** High  
**Type:** Functional / Security

**Preconditions:**

- Admin user has just logged out (URL is `/web/login`)

**Steps:**

| #   | Action                                                              | Expected Result            |
| --- | ------------------------------------------------------------------- | -------------------------- |
| 1   | Navigate directly to `https://demo.inventree.org/web/home`          | Redirected to `/web/login` |
| 2   | Navigate directly to `https://demo.inventree.org/web/part`          | Redirected to `/web/login` |
| 3   | Navigate directly to `https://demo.inventree.org/web/manufacturing` | Redirected to `/web/login` |

**Automation Hints:**

- After logout: `await page.goto('/web/home')`
- `expect(page).toHaveURL(/.*\/web\/login/)`

---

## TC-AUTH-A-011: Admin login via direct URL with credentials

**Priority:** Medium  
**Type:** Exploratory

**Preconditions:**

- User is not logged in

**Steps:**

| #   | Action                                                                            | Expected Result                                       |
| --- | --------------------------------------------------------------------------------- | ----------------------------------------------------- |
| 1   | Navigate to `https://demo.inventree.org/web/login?login=admin&password=inventree` |                                                       |
| 2   | Observe result                                                                    | User is authenticated and redirected to the dashboard |
| 3   | Verify user                                                                       | "Adam Administrator" is displayed in top-right        |
| 4   | Verify superuser banner                                                           | "Superuser Mode" banner appears                       |

**Automation Hints:**

- `page.goto('https://demo.inventree.org/web/login?login=admin&password=inventree')`
- Assert URL redirects to `/web/home`
- Assert display name visible

---

## TC-AUTH-A-012: Admin and reader sessions are independent

**Priority:** Medium  
**Type:** Exploratory / Ad-hoc

**Preconditions:**

- Two browser contexts or incognito sessions available

**Steps:**

| #   | Action                                         | Expected Result                                      |
| --- | ---------------------------------------------- | ---------------------------------------------------- |
| 1   | In Context A: login as `admin`                 | Admin dashboard loads                                |
| 2   | In Context B: login as `reader`                | Reader dashboard loads                               |
| 3   | In Context A: verify still logged in as admin  | "Adam Administrator" shown; Superuser banner visible |
| 4   | In Context B: verify still logged in as reader | "Ronald Reader" shown; no Superuser banner           |
| 5   | In Context A: logout admin                     | Admin session cleared                                |
| 6   | In Context B: verify reader is still logged in | Session is unaffected by admin logout                |

**Automation Hints:**

- Use `browser.newContext()` for isolated session contexts in Playwright
- Each context manages its own cookies/session independently

---

## TC-AUTH-A-013: Admin-only alerts button opens notifications panel

**Priority:** Medium  
**Type:** Exploratory

**Preconditions:**

- Logged in as `admin`

**Steps:**

| #   | Action                                        | Expected Result                                                                     |
| --- | --------------------------------------------- | ----------------------------------------------------------------------------------- |
| 1   | Locate the "open-alerts" button in the header | Button is visible (this button does NOT appear for reader/engineer/allaccess users) |
| 2   | Click the "open-alerts" button                | A notifications or alerts panel opens                                               |
| 3   | Observe panel content                         | System alerts or notifications are displayed                                        |

**Automation Hints:**

- `page.click('button[name="open-alerts"]')` or by aria-label
- Assert panel/drawer opens: check for a visible panel container

---

## TC-AUTH-A-014: Admin cannot see superuser banner after dismissing — re-login check

**Priority:** Low  
**Type:** Exploratory / Ad-hoc

**Preconditions:**

- Logged in as `admin`

**Steps:**

| #   | Action                                      | Expected Result                                    |
| --- | ------------------------------------------- | -------------------------------------------------- |
| 1   | Dismiss the "Superuser Mode" warning banner | Banner disappears                                  |
| 2   | Navigate away (e.g., to Parts tab)          | Observe whether banner returns                     |
| 3   | Logout and log back in as admin             | Observe whether banner reappears after fresh login |
| 4   | Document actual behavior                    | Note: this is a regression baseline test           |

**Automation Hints:**

- Dismiss banner, then navigate: `page.click('[role=tab]:has-text("Parts")')`
- Assert banner visibility after navigation

---

## TC-AUTH-A-015: SQL injection attempt in admin login fields

**Priority:** Medium  
**Type:** Security / Ad-hoc

**Preconditions:**

- Login page is open

**Steps:**

| #   | Action                                       | Expected Result                                                                  |
| --- | -------------------------------------------- | -------------------------------------------------------------------------------- |
| 1   | Enter `admin'--` in the username field       |                                                                                  |
| 2   | Enter any string in the password field       |                                                                                  |
| 3   | Click "Log In"                               |                                                                                  |
| 4   | Observe result                               | Login fails with error toast; no admin session is created; application is stable |
| 5   | Enter `' OR 1=1--` in username, any password |                                                                                  |
| 6   | Click "Log In"                               | Login fails; no session bypass                                                   |

**Automation Hints:**

- Assert no redirect to dashboard: `expect(page).toHaveURL('/web/login')`
- Assert error toast visible

---

## TC-AUTH-A-016: Reader user cannot access admin-only System Settings URL

**Priority:** High  
**Type:** Security / Ad-hoc

**Preconditions:**

- Logged in as `reader`

**Steps:**

| #   | Action                                                                 | Expected Result                                                               |
| --- | ---------------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| 1   | While logged in as reader, note the System Settings URL from knowledge |                                                                               |
| 2   | Directly navigate to the system settings URL                           |                                                                               |
| 3   | Observe result                                                         | Access is denied, redirected, or an error is shown — NOT the settings content |

**Automation Hints:**

- Navigate to settings URL directly
- Assert no settings content visible; assert access denied or redirect

---

## TC-AUTH-A-017: Color mode toggle works for admin user

**Priority:** Low  
**Type:** Exploratory

**Preconditions:**

- Logged in as `admin`

**Steps:**

| #   | Action                                          | Expected Result                                                |
| --- | ----------------------------------------------- | -------------------------------------------------------------- |
| 1   | Click the "Adam Administrator" user menu button | Menu opens                                                     |
| 2   | Click "Change Color Mode"                       | Color theme of the UI changes (light to dark or dark to light) |
| 3   | Verify color mode applied                       | Page background and elements reflect the new theme             |
| 4   | Click "Change Color Mode" again                 | Reverts to previous color mode                                 |

**Automation Hints:**

- Check CSS class on `<html>` or `<body>` for dark/light mode class
- Assert class changes after toggle
