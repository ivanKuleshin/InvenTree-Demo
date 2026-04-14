import "dotenv/config";

export type UserRole = "allaccess" | "reader" | "engineer" | "admin";

export interface UserCredentials {
  username: string;
  password: string;
  /** Human-readable description of what this role can do */
  description: string;
}

function requireEnv(name: string): string {
  const value = process.env[name];
  if (!value) {
    throw new Error(`Missing required environment variable: ${name}`);
  }
  return value;
}

export function getUser(role: UserRole): UserCredentials {
  const roleKey = role.toUpperCase();
  return {
    username: requireEnv(`USER_${roleKey}_USERNAME`),
    password: requireEnv(`USER_${roleKey}_PASSWORD`),
    description: ROLE_DESCRIPTIONS[role],
  };
}

export const ROLE_DESCRIPTIONS: Record<UserRole, string> = {
  allaccess: "View / create / edit all pages and items",
  reader:
    "Can view all pages but cannot create, edit or delete database records",
  engineer:
    "Can manage parts, view stock, but no access to purchase or sales orders",
  admin: "Superuser account, access all areas plus administrator actions",
};

export const ALL_ROLES: UserRole[] = [
  "allaccess",
  "reader",
  "engineer",
  "admin",
];

/** Roles that are allowed to create/edit/delete records */
export const WRITE_ROLES: UserRole[] = ["allaccess", "admin", "engineer"];

/** Roles that are read-only */
export const READ_ONLY_ROLES: UserRole[] = ["reader"];
