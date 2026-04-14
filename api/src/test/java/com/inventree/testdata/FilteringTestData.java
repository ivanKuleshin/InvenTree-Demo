package com.inventree.testdata;

public final class FilteringTestData {

    public static final int SINGLE_ITEM_LIMIT = 1;
    public static final int SMALL_PAGE_LIMIT = 5;
    public static final int LARGE_LIMIT = 10;

    public static final int PAGINATION_LIMIT = 3;
    public static final int PAGINATION_OFFSET = 3;
    public static final int BEYOND_TOTAL_OFFSET = 99999;
    public static final int LARGE_ENOUGH_LIMIT = 5000;
    public static final int LIMIT_UNPAGINATED = 0;
    public static final int LIMIT_NEGATIVE_ONE = -1;

    public static final String SEARCH_TERM_LOWER = "resistor";
    public static final String SEARCH_TERM_UPPER = "RESISTOR";
    public static final String SEARCH_TERM_NO_MATCH = "xyznonexistentterm12345";
    public static final String SEARCH_TERM_EMPTY = "";

    public static final int ELECTRONICS_CATEGORY_PK = 24;
    public static final int CATEGORY_WITH_CHILD_PK = 24;
    public static final int CHILD_CATEGORY_PK = 26;
    public static final String CATEGORY_NULL = "null";

    public static final String ORDERING_NAME_ASC = "name";
    public static final String ORDERING_NAME_DESC = "-name";
    public static final String ORDERING_STOCK_DESC = "-in_stock";
    public static final String ORDERING_STOCK_ASC = "in_stock";
    public static final String ORDERING_INVALID = "invalid_field";

    public static final String SEARCH_CATEGORY_NAME = "Passives";
    public static final int PARENT_CATEGORY_PK = 4;

    public static final String IPN_EXISTING = "RES-001";
    public static final int IPN_EXISTING_PK = 1692;
    public static final String IPN_NON_EXISTENT = "RES-999";
    public static final String IPN_REGEX_PREFIX_RES = "^RES";
    public static final String IPN_REGEX_NO_MATCH = "^XYZ_NOMATCH_99999";
    public static final String NAME_REGEX_PREFIX_R = "^R";

    public static final String DATE_RANGE_START = "2026-01-01";
    public static final String DATE_RANGE_END = "2026-04-14";
    public static final String DATE_FUTURE = "2030-01-01";

    public static final String FILTER_VALUE_TRUE = "true";
    public static final String FILTER_VALUE_FALSE = "false";
    public static final String FILTER_PARAM_CASCADE = "cascade";
    public static final String FILTER_PARAM_ASSEMBLY = "assembly";
    public static final String FILTER_PARAM_ACTIVE = "active";
    public static final String FILTER_PARAM_HAS_STOCK = "has_stock";
    public static final String FILTER_PARAM_VIRTUAL = "virtual";
    public static final String FILTER_PARAM_TRACKABLE = "trackable";
    public static final String FILTER_PARAM_PURCHASEABLE = "purchaseable";
    public static final String FILTER_PARAM_SALABLE = "salable";
    public static final String FILTER_PARAM_TOP_LEVEL = "top_level";

    public static final String JSON_KEY_COUNT = "\"count\":";
    public static final String JSON_KEY_NEXT = "\"next\":";
    public static final String JSON_KEY_PREVIOUS = "\"previous\":";

    private FilteringTestData() {}
}
