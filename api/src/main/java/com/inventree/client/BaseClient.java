package com.inventree.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventree.auth.Role;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseClient {

    protected final Logger log = LogManager.getLogger(getClass());

    private static final ObjectMapper MAPPER = new ObjectMapper();

    protected final Response executeGet(String path, Role role) {
        return executeGet(path, role, (Map<String, Object>) null);
    }

    protected final Response executeGet(String path, Role role, Object queryParams) {
        log.debug("GET {} [role={}]", path, role);
        var spec = given().spec(SpecBuilder.build(role));
        if (queryParams != null) {
            Map<String, Object> map = MAPPER.convertValue(queryParams, Map.class);
            if (!map.isEmpty()) {
                spec = spec.queryParams(map);
            }
        }
        return spec.when().get(path);
    }

    protected final Response executePost(String path, Role role, Object body) {
        log.debug("POST {} [role={}]", path, role);
        return given()
                .spec(SpecBuilder.build(role))
                .body(body)
                .when()
                .post(path);
    }

    protected final Response executePut(String path, Role role, Object body) {
        log.debug("PUT {} [role={}]", path, role);
        return given()
                .spec(SpecBuilder.build(role))
                .body(body)
                .when()
                .put(path);
    }

    protected final Response executePatch(String path, Role role, Object body) {
        log.debug("PATCH {} [role={}]", path, role);
        return given()
                .spec(SpecBuilder.build(role))
                .body(body)
                .when()
                .patch(path);
    }

    protected final Response executeDelete(String path, Role role) {
        log.debug("DELETE {} [role={}]", path, role);
        return given()
                .spec(SpecBuilder.build(role))
                .when()
                .delete(path);
    }
}
