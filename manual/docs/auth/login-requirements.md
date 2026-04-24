---
source: https://inventree.org/demo.html
component: auth
topic: Login and Authentication Requirements
fetched: 2026-04-13
---

> **Source**: [https://inventree.org/demo.html](https://inventree.org/demo.html)
> **Secondary sources**: [https://docs.inventree.org/en/stable/settings/permissions/](https://docs.inventree.org/en/stable/settings/permissions/), [https://docs.inventree.org/en/stable/settings/global/](https://docs.inventree.org/en/stable/settings/global/), [https://docs.inventree.org/en/stable/settings/MFA/](https://docs.inventree.org/en/stable/settings/MFA/)

# InvenTree Login and Authentication Requirements

## Table of Contents

1. [Login Page and Fields](#login-page-and-fields)
2. [Authentication Behavior](#authentication-behavior)
3. [User Roles and Permissions](#user-roles-and-permissions)
4. [Demo User Accounts](#demo-user-accounts)
5. [Role-Based Access: reader and admin](#role-based-access-reader-and-admin)
6. [Session Management](#session-management)
7. [Logout Process](#logout-process)
8. [Multi-Factor Authentication](#multi-factor-authentication)
9. [SSO Authentication](#sso-authentication)
10. [Global Login Settings](#global-login-settings)
11. [UI Indicators and Feedback](#ui-indicators-and-feedback)
12. [Browser and Environment Requirements](#browser-and-environment-requirements)
13. [Data Persistence and Environment Notes](#data-persistence-and-environment-notes)

---

## Login Page and Fields

**Login URL:** `https://demo.inventree.org` (root redirects to login when unauthenticated)

**Direct login URL pattern** (pre-fills credentials via query parameters):

```
https://demo.inventree.org/web/login?login=<username>&password=<password>
```

**Login form fields:**

- **Username** — text input, required
- **Password** — password input (masked), required
- **Log In** — submit button

**Registration-related fields** (configurable via global settings, may appear on login/signup page):

- Email address (required if `LOGIN_MAIL_REQUIRED` is enabled)
- Email confirmation (if `LOGIN_SIGNUP_MAIL_TWICE` is enabled)
- Password confirmation (if `LOGIN_SIGNUP_PWD_TWICE` is enabled)

---

## Authentication Behavior

### Success Scenario

1. User navigates to `https://demo.inventree.org`
2. Login form is presented when no valid session exists
3. User enters valid username and password
4. Clicks "Log In"
5. Server validates credentials
6. Session is established; user is redirected to the dashboard
7. Expected load time: approximately 2 seconds
8. Dashboard displays the user's display name in the top-right navigation button

### Failure Scenarios

| Scenario                           | Expected Behavior                                                                           |
| ---------------------------------- | ------------------------------------------------------------------------------------------- |
| Invalid username                   | Authentication fails; error message displayed on login form                                 |
| Invalid password                   | Authentication fails; error message displayed on login form                                 |
| Empty username or password         | Form validation prevents submission or server returns error                                 |
| Account locked / MFA required      | If `LOGIN_ENFORCE_MFA` is enabled, users without MFA configured are blocked from logging in |
| Correct credentials via direct URL | Session established the same as form-based login                                            |

> **Note:** The InvenTree documentation does not specify the exact text of error messages for failed login attempts. Error message copy must be verified by direct observation against the live demo environment.

### Password Reset

- Configurable via `LOGIN_ENABLE_PWD_FORGOT` setting: "users can reset their password via email"
- Not enabled by default in all configurations
- Admin-side fallback: `python ./manage.py changepassword <username>`

---

## User Roles and Permissions

InvenTree implements role-based access control (RBAC) through a three-tier hierarchy: **Users** are assigned to **Groups**, which receive **Permissions** across multiple functional roles.

### Functional Role Areas (9 total)

| Role Area      | Scope                              |
| -------------- | ---------------------------------- |
| Admin          | Assigning user permissions         |
| Build          | Build Orders and Bill of Materials |
| Part           | Part data                          |
| Part Category  | Part Category data                 |
| Purchase Order | Purchase Order data                |
| Return Order   | Return Order data                  |
| Sales Order    | Sales Order data                   |
| Stock Item     | Stock Item data                    |
| Stock Location | Stock Location data                |

### Permission Levels per Role Area

Each role area supports four granular permission levels:

| Permission | Description                              |
| ---------- | ---------------------------------------- |
| View       | Read-only access to role-related content |
| Change     | Ability to modify existing data          |
| Add        | Authorization to create new records      |
| Delete     | Permission to remove records             |

### Special User Flags

Two elevated privilege flags exist beyond standard RBAC:

**Staff Flag:**

- Grants access to the Django admin interface
- Enables "dangerous actions that might have a security impact"
- May require additional explicit Admin role assignment to be fully effective

**Superuser Flag:**

- Grants unrestricted access to "all data and functions of InvenTree"
- Includes server OS shell access
- Strongly recommended to use with multi-factor authentication
- Not recommended for daily use; dedicated for administrative tasks only

### Permission Enforcement

Permissions are enforced consistently across:

- Admin interface
- Web interface (UI)
- API endpoints

Unavailable functions are selectively hidden or disabled based on the user's permissions — they do not simply return errors when accessed; the UI adapts dynamically.

---

## Demo User Accounts

The InvenTree demo environment provides four pre-configured accounts:

| Username    | Password    | Display Name       | Role Description                                                    |
| ----------- | ----------- | ------------------ | ------------------------------------------------------------------- |
| `allaccess` | `nolimits`  | Ally Access        | View, create, edit all pages and items                              |
| `reader`    | `readonly`  | Ronald Reader      | Can view all pages but cannot create, edit, or delete records       |
| `engineer`  | `partsonly` | Robert Shuruncle   | Can manage parts, view stock; no access to purchase or sales orders |
| `admin`     | `inventree` | Adam Administrator | Superuser account; access all areas plus administrator actions      |

**Direct login URLs:**

```
https://demo.inventree.org/web/login?login=allaccess&password=nolimits
https://demo.inventree.org/web/login?login=reader&password=readonly
https://demo.inventree.org/web/login?login=engineer&password=partsonly
https://demo.inventree.org/web/login?login=admin&password=inventree
```

### Module Access Matrix

| Module        | allaccess | reader | engineer | admin |
| ------------- | --------- | ------ | -------- | ----- |
| Dashboard     | Yes       | Yes    | Yes      | Yes   |
| Parts         | Yes       | Yes    | Yes      | Yes   |
| Stock         | Yes       | Yes    | Yes      | Yes   |
| Manufacturing | Yes       | Yes    | Yes      | Yes   |
| Purchasing    | Yes       | Yes    | No       | Yes   |
| Sales         | Yes       | Yes    | No       | Yes   |

### Action Permission Matrix

| Action                      | allaccess | reader | engineer                       | admin |
| --------------------------- | --------- | ------ | ------------------------------ | ----- |
| View Records                | Yes       | Yes    | Yes                            | Yes   |
| Create Records              | Yes       | No     | Limited (Parts/Stock/Mfg only) | Yes   |
| Edit Records                | Yes       | No     | Limited (Parts/Stock/Mfg only) | Yes   |
| Delete Records              | Yes       | No     | Limited (Parts/Stock/Mfg only) | Yes   |
| Admin Functions             | No        | No     | No                             | Yes   |
| View Alerts / Notifications | No        | No     | No                             | Yes   |
| System Settings             | No        | No     | No                             | Yes   |

---

## Role-Based Access: reader and admin

### reader (Ronald Reader)

**Credentials:** username `reader`, password `readonly`

**Authentication:** Standard form-based login. Dashboard loads with display name "Ronald Reader" in the top-right user button.

**Permissions:**

- Full read access across all modules (Parts, Stock, Manufacturing, Purchasing, Sales)
- Cannot create any new records
- Cannot edit any existing records
- Cannot delete any records
- Cannot access administrative functions

**UI Behavior after Login:**

- All module navigation links are visible
- Create / Edit / Delete controls are hidden or disabled
- Dashboard widgets requiring elevated access display a "Requires Superuser" alert
- No admin interface access

**Testable Assertions:**

- All read/list/detail pages load successfully
- Attempting to access create/edit forms either hides the route or shows an appropriate access-denied response
- "Requires Superuser" message appears for restricted dashboard widgets
- Superuser warning banner is NOT displayed

---

### admin (Adam Administrator)

**Credentials:** username `admin`, password `inventree`

**Authentication:** Standard form-based login. Dashboard loads with display name "Adam Administrator" in the top-right user button.

**Permissions:**

- Full access to all modules and all CRUD operations
- Superuser flag is active — unrestricted system access
- Access to administrative functions not available to any other demo account

**UI Behavior after Login:**

- All module navigation links are visible
- Create / Edit / Delete controls are fully enabled
- Alerts button is visible in the top-right navigation area
- Full access to system widgets on the dashboard
- A superuser warning banner is displayed: _"The current user has elevated privileges and should not be used for regular usage"_
- Access to notification and email management features
- Access to system monitoring capabilities

**Testable Assertions:**

- All pages and forms are accessible and functional
- Superuser warning banner is visible immediately after login
- Alerts/notifications button is present in the navigation bar
- Admin-only features (system settings, user management, etc.) are accessible
- All CRUD operations succeed across all modules

---

## Session Management

### Session Establishment

- A session is created upon successful login
- Session is maintained via cookies
- Cookies must be enabled in the browser for sessions to function
- Session persists across page navigations during the same browser session

### Session Termination

- Sessions are terminated via the explicit logout flow (see below)
- Session cookies are cleared on logout
- Direct login URLs can be used to establish new sessions without a prior logout step

### Session Persistence Notes

- No explicit session timeout is documented for the demo environment
- The demo database resets once per day; this does not invalidate active sessions but restores data to its baseline state
- During daily database reset, the demo server may be briefly inaccessible (a few minutes)

---

## Logout Process

Applies to all user accounts:

1. Click the username button in the top-right corner of the navigation bar
2. Click "Logout" from the dropdown menu
3. User is redirected to the login page
4. Session is cleared

**Testable Assertions:**

- After logout, navigating to any protected URL redirects to the login page
- The user's display name no longer appears in the navigation bar
- A new login is required to regain access

---

## Multi-Factor Authentication

MFA is supported but not enabled by default in the demo environment.

**Supported Methods:**

- TOTP (Time-based One-Time Password) — open standard, compatible with Google Authenticator, Authy, etc.
- Static backup tokens — pre-generated emergency access codes

**Enforcement:**

- When `LOGIN_ENFORCE_MFA` is enabled globally, users without MFA configured cannot log in
- Administrators can manage TOTP devices and static tokens from the admin interface

**Lockout Recovery:**

- If a user loses access to both their TOTP device and backup tokens, they are locked out
- An admin can delete the user's tokens from the admin pages to restore access

> **Note:** MFA is highly recommended when the InvenTree instance is exposed to the public internet.

---

## SSO Authentication

SSO is supported via the `django-allauth` library and is not enabled by default.

**Supported Providers:** Any OpenID or OAuth provider supported by django-allauth (examples include Google, GitHub, Microsoft)

**Login Flow:**

1. Provider is configured in the backend config file
2. An application is registered with the external provider to obtain client credentials
3. Credentials and site assignment are added in the InvenTree admin interface
4. Callback URL format: `<hostname>/accounts/<provider>/login/callback/`
5. SSO is enabled globally via Login Settings
6. Email must be configured and operational before enabling SSO

**Group Synchronization:**

- SSO can automatically assign users to InvenTree groups via OIDC group claims
- Configurable via `LOGIN_ENABLE_SSO_GROUP_SYNC`, `SSO_GROUP_MAP`, `SSO_GROUP_KEY`, and `SSO_REMOVE_GROUPS`

---

## Global Login Settings

These are system-level settings (accessible to staff users only) that affect login and authentication behavior:

| Setting Key                     | Description                                                                                |
| ------------------------------- | ------------------------------------------------------------------------------------------ |
| `LOGIN_ENABLE_PWD_FORGOT`       | Allow users to reset password via email                                                    |
| `LOGIN_MAIL_REQUIRED`           | Require email address on signup                                                            |
| `LOGIN_ENFORCE_MFA`             | Require MFA for all users to log in                                                        |
| `LOGIN_ENABLE_REG`              | Allow new user self-registration                                                           |
| `LOGIN_SIGNUP_MAIL_TWICE`       | Require email address to be entered twice on signup                                        |
| `LOGIN_SIGNUP_PWD_TWICE`        | Require password to be entered twice on signup                                             |
| `SIGNUP_GROUP`                  | Default group assigned to newly registered users                                           |
| `LOGIN_SIGNUP_MAIL_RESTRICTION` | Restrict signups to specific email domains                                                 |
| `LOGIN_ENABLE_SSO`              | Enable Single Sign-On                                                                      |
| `LOGIN_ENABLE_SSO_REG`          | Allow account creation via SSO                                                             |
| `LOGIN_SIGNUP_SSO_AUTO`         | Auto-fill SSO profile data; only ask for username/name if not provided by the SSO provider |
| `LOGIN_ENABLE_SSO_GROUP_SYNC`   | Synchronize user groups from SSO provider                                                  |
| `SSO_GROUP_MAP`                 | Group mapping between SSO provider groups and InvenTree groups                             |
| `SSO_GROUP_KEY`                 | SSO group attribute/claim name                                                             |
| `SSO_REMOVE_GROUPS`             | Remove InvenTree groups not present in SSO sync                                            |

> **Note:** Only users with staff status can view and edit global settings.

---

## UI Indicators and Feedback

| Condition                      | UI Indicator                                                                                           |
| ------------------------------ | ------------------------------------------------------------------------------------------------------ |
| Logged in                      | User display name shown in top-right navigation button                                                 |
| Superuser account (admin)      | Warning banner: "The current user has elevated privileges and should not be used for regular usage"    |
| Restricted dashboard widget    | "Requires Superuser" alert displayed on that widget                                                    |
| Admin account only             | Alerts/notifications button visible in top-right navigation                                            |
| engineer and reader accounts   | Purchasing and Sales navigation hidden for engineer; all create/edit/delete controls hidden for reader |
| Role-based feature restriction | Unavailable functions are hidden or disabled in the UI (not shown as accessible then rejected)         |

---

## Browser and Environment Requirements

- Modern web browser (Chrome, Firefox, Safari, Edge)
- JavaScript must be enabled
- Cookies must be enabled (required for session management)
- Minimum recommended resolution: 1024x768

---

## Data Persistence and Environment Notes

- The demo database resets once per day to a known baseline state
- User accounts, credentials, and permissions are consistent across resets
- Test data (parts, stock, orders) is restored from the demo dataset on each reset
- During the daily reset, the demo server may be inaccessible for a few minutes
- The demo server runs the latest InvenTree master branch version (auto-updated via Docker)
