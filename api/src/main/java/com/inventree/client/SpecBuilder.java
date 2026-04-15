package com.inventree.client;

import com.inventree.auth.AuthManager;
import com.inventree.auth.Role;
import com.inventree.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class SpecBuilder {

    private SpecBuilder() {}

    public static RequestSpecBuilder requestSpec(Role role) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", ContentType.JSON.toString())
                .setRelaxedHTTPSValidation()
                .setConfig(io.restassured.config.RestAssuredConfig.config()
                        .httpClient(io.restassured.config.HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", ApiConfig.getRequestTimeoutMs())
                                .setParam("http.socket.timeout", ApiConfig.getRequestTimeoutMs())));

        AuthManager.getInstance().applyAuth(builder, role);

        builder.addFilter(new ResponseLoggingFilter());

        if (ApiConfig.isLoggingEnabled()) {
            builder.addFilter(new AllureRestAssured());
        }

        return builder;
    }

    public static RequestSpecification build(Role role) {
        return requestSpec(role).build();
    }

    public static ResponseSpecBuilder responseSpec(int expectedStatus) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatus)
                .expectContentType(ContentType.JSON);
    }

    public static ResponseSpecification buildResponse(int expectedStatus) {
        return responseSpec(expectedStatus).build();
    }
}
