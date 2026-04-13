/**
 * Static test data constants for the Parts domain.
 * All test-specific data should be kept here to avoid
 * magic strings scattered across test files.
 */

export const PARTS = {
  /** A well-known part that exists in the demo database after daily reset */
  EXISTING: {
    name: "R_10K_0402",
    description: "10K Resistor (0402)",
    category: "Resistors",
  },

  /** Data used for creating a new part in write-access tests */
  NEW: {
    name: `Test Part ${Date.now()}`,
    description: "Automated test part — safe to delete",
    ipn: "TEST-001",
  },

  /** A part known to be in a specific category */
  BY_CATEGORY: {
    category: "Electronics",
  },
} as const;

export const PART_CATEGORIES = {
  ROOT: "Parts",
  ELECTRONICS: "Electronics",
  RESISTORS: "Resistors",
  CAPACITORS: "Capacitors",
} as const;

/** Column indices for the parts list table (zero-based) */
export const PARTS_TABLE_COLUMNS = {
  THUMBNAIL: 0,
  NAME: 1,
  DESCRIPTION: 2,
  CATEGORY: 3,
  STOCK: 4,
  ACTIONS: 5,
} as const;
