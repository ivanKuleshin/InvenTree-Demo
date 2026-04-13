package org.example.base;

import io.restassured.RestAssured;
import org.example.config.ApiConfig;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all API tests. Configures REST Assured defaults.
 */
public abstract class BaseTest {

    @BeforeSuite
    public void setUp() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

