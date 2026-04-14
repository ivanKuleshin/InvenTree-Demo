import * as path from "path";
import { type UserRole } from "./users";

const AUTH_DIR = path.join(__dirname, "..", ".auth");

/**
 * Centralized storage-state file paths.
 * Use these in tests instead of hardcoding paths:
 *
 *   test.use({ storageState: STORAGE_STATE.ALLACCESS });
 */
export const STORAGE_STATE = {
  ALLACCESS: path.join(AUTH_DIR, "allaccess.json"),
  READER: path.join(AUTH_DIR, "reader.json"),
  ENGINEER: path.join(AUTH_DIR, "engineer.json"),
  ADMIN: path.join(AUTH_DIR, "admin.json"),
} as const;

/** Convenience map keyed by UserRole — used in setup tests */
export const STORAGE_STATE_BY_ROLE: Record<UserRole, string> = {
  allaccess: STORAGE_STATE.ALLACCESS,
  reader: STORAGE_STATE.READER,
  engineer: STORAGE_STATE.ENGINEER,
  admin: STORAGE_STATE.ADMIN,
};
