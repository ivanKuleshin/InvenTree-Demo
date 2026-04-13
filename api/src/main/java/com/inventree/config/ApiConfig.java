package com.inventree.config;

import com.inventree.auth.AuthStrategy;
import com.inventree.auth.Role;
import com.inventree.util.ApiConstants;

public final class ApiConfig {

    private ApiConfig() {}

    public static String getBaseUrl() {
        return ConfigManager.getInstance().get(ApiConstants.PROP_BASE_URL);
    }

    public static AuthStrategy getAuthStrategy() {
        String value = ConfigManager.getInstance()
                .get(ApiConstants.PROP_AUTH_STRATEGY, AuthStrategy.TOKEN.name());
        return AuthStrategy.valueOf(value.toUpperCase());
    }

    public static int getRequestTimeoutMs() {
        return ConfigManager.getInstance().getInt(ApiConstants.PROP_TIMEOUT_MS, 10000);
    }

    public static boolean isLoggingEnabled() {
        return ConfigManager.getInstance().getBoolean(ApiConstants.PROP_LOGGING_ENABLED, true);
    }

    public static String getUsername(Role role) {
        String key = String.format(ApiConstants.PROP_CREDS_USERNAME, role.name().toLowerCase());
        return ConfigManager.getInstance().get(key);
    }

    public static String getPassword(Role role) {
        String key = String.format(ApiConstants.PROP_CREDS_PASSWORD, role.name().toLowerCase());
        return ConfigManager.getInstance().get(key);
    }
}
