package com.inventree.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventree.config.ApiConfig;
import com.inventree.util.ApiConstants;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.RestAssured.given;

public final class AuthManager {

    private static final Logger log = LogManager.getLogger(AuthManager.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Map<Role, String> tokenCache = new ConcurrentHashMap<>();

    private AuthManager() {}

    public static AuthManager getInstance() {
        return Holder.INSTANCE;
    }

    public String getToken(Role role) {
        return tokenCache.computeIfAbsent(role, this::fetchToken);
    }

    public void applyAuth(RequestSpecBuilder specBuilder, Role role) {
        Credentials creds = credentialsFor(role);
        if (ApiConfig.getAuthStrategy() == AuthStrategy.TOKEN) {
            specBuilder.addHeader("Authorization", "Token " + getToken(role));
        } else {
            specBuilder.setAuth(
                    RestAssured.preemptive().basic(creds.getUsername(), creds.getPassword()));
        }
    }

    public void clearCache() {
        tokenCache.clear();
        log.debug("Auth token cache cleared");
    }

    private String fetchToken(Role role) {
        Credentials creds = credentialsFor(role);
        log.info("Fetching auth token for role: {}", role);
        String responseBody = given()
                .baseUri(ApiConfig.getBaseUrl())
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(creds.getUsername(), creds.getPassword())
                .when()
                .get(ApiConstants.TOKEN_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        try {
            JsonNode node = MAPPER.readTree(responseBody);
            if (!node.has("token")) {
                throw new IllegalStateException(
                        "Token endpoint response did not contain 'token' field: " + responseBody);
            }
            return node.get("token").asText();
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse token response for role " + role, e);
        }
    }

    private Credentials credentialsFor(Role role) {
        return new Credentials(ApiConfig.getUsername(role), ApiConfig.getPassword(role));
    }

    private static final class Holder {
        private static final AuthManager INSTANCE = new AuthManager();
    }
}
