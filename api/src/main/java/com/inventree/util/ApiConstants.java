package com.inventree.util;

public final class ApiConstants {

    // -------------------------------------------------
    // API endpoint paths
    // -------------------------------------------------
    public static final String TOKEN_ENDPOINT        = "/api/user/token/";
    public static final String PARTS_ENDPOINT        = "/api/part/";
    public static final String CATEGORIES_ENDPOINT   = "/api/part/category/";
    public static final String STOCK_ENDPOINT        = "/api/stock/";
    public static final String STOCK_LOCATION_ENDPOINT = "/api/stock/location/";
    public static final String COMPANY_ENDPOINT      = "/api/company/";
    public static final String SUPPLIER_PART_ENDPOINT = "/api/company/part/";

    // -------------------------------------------------
    // config.properties keys
    // -------------------------------------------------
    public static final String PROP_BASE_URL        = "api.base.url";
    public static final String PROP_AUTH_STRATEGY   = "api.auth.strategy";
    public static final String PROP_TIMEOUT_MS      = "api.request.timeout.ms";
    public static final String PROP_LOGGING_ENABLED = "api.logging.enabled";

    public static final String PROP_CREDS_USERNAME  = "api.credentials.%s.username";
    public static final String PROP_CREDS_PASSWORD  = "api.credentials.%s.password";

    private ApiConstants() {}
}
