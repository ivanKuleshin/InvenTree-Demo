package com.inventree.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ResponseValidator {

    private static final Logger log = LogManager.getLogger(ResponseValidator.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ResponseValidator() {}

    public static void assertStatus(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    public static void assertStatusAndContentType(Response response, int expectedStatus) {
        response.then()
                .statusCode(expectedStatus)
                .contentType(ContentType.JSON);
    }

    public static <T> T deserialize(Response response, Class<T> type) {
        try {
            return response.as(type);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize response body to " + type.getSimpleName()
                            + ". Body: " + response.asString(), e);
        }
    }

    public static <T> T deserialize(Response response, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(response.asString(), typeRef);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize response body to " + typeRef.getType().getTypeName()
                            + ". Body: " + response.asString(), e);
        }
    }
}
