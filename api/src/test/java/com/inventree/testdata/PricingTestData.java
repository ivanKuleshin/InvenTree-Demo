package com.inventree.testdata;

import java.util.Map;

public final class PricingTestData {

    public static final int PRICING_PART_PK = 73;
    public static final int SALABLE_PART_PK = 113;
    public static final int KNOWN_INTERNAL_PRICE_PK = 1;
    public static final int KNOWN_SALE_PRICE_PK = 1;
    public static final int NON_EXISTENT_PK = 999999;

    public static final String FIELD_PART = "part";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_PRICE_CURRENCY = "price_currency";

    public static final String QUERY_PARAM_LIMIT = "limit";

    public static final String FIELD_PK = "pk";
    public static final String FIELD_OVERRIDE_MIN = "override_min";
    public static final String FIELD_OVERRIDE_MAX = "override_max";
    public static final String FIELD_OVERRIDE_MIN_CURRENCY = "override_min_currency";
    public static final String FIELD_INTERNAL_COST_MIN = "internal_cost_min";
    public static final String FIELD_OVERALL_MIN = "overall_min";

    public static final String CURRENCY_USD = "USD";

    public static final int CREATE_QUANTITY = 10;
    public static final String CREATE_PRICE = "55.00";
    public static final int SALE_CREATE_QUANTITY = 100;
    public static final String SALE_CREATE_PRICE = "4500.00";
    public static final String PATCH_PRICE = "49.99";
    public static final String PATCH_SALE_PRICE = "4999.00";
    public static final String OVERRIDE_MIN_VALUE = "70.00";

    public static final double EXPECTED_PRICE_NUMERIC = 55.0;
    public static final double EXPECTED_SALE_PRICE_NUMERIC = 4500.0;
    public static final double EXPECTED_PATCH_PRICE = 49.99;
    public static final double EXPECTED_PATCH_SALE_PRICE = 4999.0;
    public static final double EXPECTED_OVERRIDE_MIN = 70.0;

    public static final int LIST_LIMIT = 5;
    public static final double PRICE_DELTA = 0.001;

    public static Map<String, Object> standardInternalPricePayload() {
        return Map.of(FIELD_PART, PRICING_PART_PK, FIELD_QUANTITY, CREATE_QUANTITY,
                FIELD_PRICE, CREATE_PRICE, FIELD_PRICE_CURRENCY, CURRENCY_USD);
    }

    public static Map<String, Object> standardSalePricePayload() {
        return Map.of(FIELD_PART, SALABLE_PART_PK, FIELD_QUANTITY, SALE_CREATE_QUANTITY,
                FIELD_PRICE, SALE_CREATE_PRICE, FIELD_PRICE_CURRENCY, CURRENCY_USD);
    }

    private PricingTestData() {}
}
