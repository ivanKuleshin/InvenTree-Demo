package com.inventree.util;

public final class ApiConstants {

    public static final String TOKEN_ENDPOINT                = "/api/user/token/";
    public static final String PARTS_ENDPOINT                = "/api/part/";
    public static final String CATEGORIES_ENDPOINT           = "/api/part/category/";
    public static final String STOCK_ENDPOINT                = "/api/stock/";
    public static final String STOCK_LOCATION_ENDPOINT       = "/api/stock/location/";
    public static final String STOCK_LOCATION_TREE_ENDPOINT  = "/api/stock/location/tree/";
    public static final String STOCK_LOCATION_TYPE_ENDPOINT  = "/api/stock/location-type/";
    public static final String STOCK_ADD_ENDPOINT            = "/api/stock/add/";
    public static final String STOCK_REMOVE_ENDPOINT         = "/api/stock/remove/";
    public static final String STOCK_COUNT_ENDPOINT          = "/api/stock/count/";
    public static final String STOCK_TRANSFER_ENDPOINT       = "/api/stock/transfer/";
    public static final String STOCK_CHANGE_STATUS_ENDPOINT  = "/api/stock/change_status/";
    public static final String STOCK_ASSIGN_ENDPOINT         = "/api/stock/assign/";
    public static final String STOCK_MERGE_ENDPOINT          = "/api/stock/merge/";
    public static final String STOCK_RETURN_ENDPOINT         = "/api/stock/return/";
    public static final String STOCK_TRACK_ENDPOINT          = "/api/stock/track/";
    public static final String COMPANY_ENDPOINT              = "/api/company/";
    public static final String SUPPLIER_PART_ENDPOINT        = "/api/company/part/";
    public static final String PART_TEST_TEMPLATE_ENDPOINT   = "/api/part/test-template/";
    public static final String INTERNAL_PRICE_ENDPOINT       = "/api/part/internal-price/";
    public static final String SALE_PRICE_ENDPOINT           = "/api/part/sale-price/";

    public static final String PROP_BASE_URL        = "api.base.url";
    public static final String PROP_AUTH_STRATEGY   = "api.auth.strategy";
    public static final String PROP_TIMEOUT_MS      = "api.request.timeout.ms";
    public static final String PROP_LOGGING_ENABLED = "api.logging.enabled";

    public static final String PROP_CREDS_USERNAME = "api.credentials.%s.username";
    public static final String PROP_CREDS_PASSWORD = "api.credentials.%s.password";

    private ApiConstants() {}
}
