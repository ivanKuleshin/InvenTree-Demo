# InvenTree Demo - Login Access Steps for All Users

**Date:** April 13, 2026  
**Test Environment:** https://demo.inventree.org  
**Documentation:** https://inventree.org/demo.html

---

## Overview

InvenTree demo provides 4 default user accounts with different permission levels. This document describes the login process and access level for each user.

---

## User Credentials Summary

| Username    | Password    | Role             | Display Name       | Access Level     |
| ----------- | ----------- | ---------------- | ------------------ | ---------------- |
| `allaccess` | `nolimits`  | Full Access User | Ally Access        | ✅ All Features  |
| `reader`    | `readonly`  | Read-Only User   | Ronald Reader      | 👁️ View Only     |
| `engineer`  | `partsonly` | Parts Engineer   | Robert Shuruncle   | ⚙️ Parts & Stock |
| `admin`     | `inventree` | Administrator    | Adam Administrator | 👑 Superuser     |

---

## Login Steps for Each User

### 1. **allaccess** - Full Access User

**Credentials:**

- Username: `allaccess`
- Password: `nolimits`

**Login Steps:**

```
1. Navigate to https://demo.inventree.org
2. Enter username: allaccess
3. Enter password: nolimits
4. Click "Log In"
5. Wait for dashboard to load (~2 seconds)
```

**Dashboard Display Name:** Ally Access

**Accessible Modules:**

- ✅ Dashboard
- ✅ Parts
- ✅ Stock
- ✅ Manufacturing
- ✅ Purchasing
- ✅ Sales

**Key Features:**

- Can view all pages
- Can create new items
- Can edit existing items
- Can delete records
- Full read/write permissions
- Note: Does NOT have superuser badge warning

**Access Level:** Full access to all features (except superuser administrative functions)

---

### 2. **reader** - Read-Only User

**Credentials:**

- Username: `reader`
- Password: `readonly`

**Login Steps:**

```
1. Navigate to https://demo.inventree.org
2. Enter username: reader
3. Enter password: readonly
4. Click "Log In"
5. Wait for dashboard to load (~2 seconds)
```

**Dashboard Display Name:** Ronald Reader

**Accessible Modules:**

- ✅ Dashboard
- ✅ Parts
- ✅ Stock
- ✅ Manufacturing
- ✅ Purchasing
- ✅ Sales

**Key Features:**

- Can view all pages and information
- CANNOT create new records
- CANNOT edit existing records
- CANNOT delete records
- Read-only permissions across all modules
- Cannot access administrative functions
- Dashboard shows "Requires Superuser" alert for widgets requiring elevated privileges

**Access Level:** View-only access to all modules

---

### 3. **engineer** - Parts Engineer

**Credentials:**

- Username: `engineer`
- Password: `partsonly`

**Login Steps:**

```
1. Navigate to https://demo.inventree.org
2. Enter username: engineer
3. Enter password: partsonly
4. Click "Log In"
5. Wait for dashboard to load (~2 seconds)
```

**Dashboard Display Name:** Robert Shuruncle

**Accessible Modules:**

- ✅ Dashboard
- ✅ Parts
- ✅ Stock
- ✅ Manufacturing
- ❌ Purchasing (NOT VISIBLE)
- ❌ Sales (NOT VISIBLE)

**Key Features:**

- Can manage parts and their attributes
- Can view and manage stock levels
- Can access manufacturing information
- CANNOT access purchasing orders
- CANNOT access sales orders
- Can create/edit parts and stock items (within scope)
- Limited to parts-related workflows
- Dashboard shows "Requires Superuser" alert for restricted widgets

**Access Level:** Restricted to Parts, Stock, and Manufacturing modules only

---

### 4. **admin** - System Administrator (Superuser)

**Credentials:**

- Username: `admin`
- Password: `inventree`

**Login Steps:**

```
1. Navigate to https://demo.inventree.org
2. Enter username: admin
3. Enter password: inventree
4. Click "Log In"
5. Wait for dashboard to load (~2 seconds)
```

**Dashboard Display Name:** Adam Administrator

**Accessible Modules:**

- ✅ Dashboard
- ✅ Parts
- ✅ Stock
- ✅ Manufacturing
- ✅ Purchasing
- ✅ Sales

**Additional Admin Features:**

- 🔔 Alerts button (visible in top right)
- 📊 Full access to system widgets
- 👑 Superuser mode indicator alert
- ⚙️ Administrative functions
- ✉️ Email/notification management
- 📈 System monitoring capabilities

**Key Features:**

- Full access to all modules and features
- Can perform all CRUD operations
- Can access all administrative functions
- Superuser warning displayed: "The current user has elevated privileges and should not be used for regular usage"
- Can view notifications and alerts
- Highest privilege level in the system

**Access Level:** Complete system access with administrative privileges

---

## Logout Process (All Users)

**Steps:**

```
1. Click on username button in top-right corner
2. Click "Logout" from the dropdown menu
3. Redirected to login page
4. Session cleared successfully
```

---

## Quick Access URLs

You can also use direct login URLs with pre-filled credentials:

### Direct Login Links:

- **allaccess:** https://demo.inventree.org/web/login?login=allaccess&password=nolimits
- **reader:** https://demo.inventree.org/web/login?login=reader&password=readonly
- **engineer:** https://demo.inventree.org/web/login?login=engineer&password=partsonly
- **admin:** https://demo.inventree.org/web/login?login=admin&password=inventree

---

## Permission Matrix

### Module Access by Role

| Feature       | allaccess | reader | engineer | admin |
| ------------- | --------- | ------ | -------- | ----- |
| Dashboard     | ✅        | ✅     | ✅       | ✅    |
| Parts         | ✅        | ✅     | ✅       | ✅    |
| Stock         | ✅        | ✅     | ✅       | ✅    |
| Manufacturing | ✅        | ✅     | ✅       | ✅    |
| Purchasing    | ✅        | ✅     | ❌       | ✅    |
| Sales         | ✅        | ✅     | ❌       | ✅    |

### Action Permissions by Role

| Action          | allaccess | reader | engineer | admin |
| --------------- | --------- | ------ | -------- | ----- |
| View Records    | ✅        | ✅     | ✅       | ✅    |
| Create Records  | ✅        | ❌     | ⚠️       | ✅    |
| Edit Records    | ✅        | ❌     | ⚠️       | ✅    |
| Delete Records  | ✅        | ❌     | ⚠️       | ✅    |
| Admin Functions | ❌        | ❌     | ❌       | ✅    |
| View Alerts     | ❌        | ❌     | ❌       | ✅    |
| System Settings | ❌        | ❌     | ❌       | ✅    |

_Note: ⚠️ = Limited to specific modules_

---

## Testing Notes

### Session Management

- Session cookies are preserved during navigation
- Sessions can be terminated via logout
- Direct login URLs work for quick testing
- Database resets daily (demo state is refreshed)

### UI Indicators

- User display name appears in top-right button
- Role-based features are dynamically hidden/shown
- "Requires Superuser" alerts appear for restricted widgets
- Superuser mode shows warning banner for admin account

### Data Persistence

- InvenTree demo database resets once per day
- Test data is restored from demo dataset
- User accounts and permissions remain consistent

---

## Browser Requirements

- Modern web browser (Chrome, Firefox, Safari, Edge)
- JavaScript enabled
- Cookies enabled for session management
- Resolution: 1024x768 minimum recommended

---

## Related Documentation

- InvenTree Official Docs: https://docs.inventree.org/
- Demo Page: https://inventree.org/demo.html
- GitHub Repository: https://github.com/inventree/inventree
- API Documentation: https://docs.inventree.org/en/stable/api/

---

## Summary

This document provides a complete reference for accessing the InvenTree demo environment with different user roles:

1. **allaccess** - Use for full feature testing
2. **reader** - Use for UI/permission testing (read-only scenarios)
3. **engineer** - Use for parts-focused workflows and access control testing
4. **admin** - Use for administrative functions and superuser-restricted features

All users can access the demo at https://demo.inventree.org
