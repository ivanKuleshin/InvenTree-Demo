package com.inventree.tests;

import com.inventree.auth.AuthManager;
import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.client.SpecBuilder;
import com.inventree.config.ApiConfig;
import com.inventree.model.PartCategoryRequest;
import com.inventree.model.PartRequest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

@Test(groups = "smoke")
public class SmokeTest extends BaseTest {

    // Tests to verify TAF is initializing fine, sort of unit test for TAF

    @Test
    public void configLoadsSuccessfully() {
        String baseUrl = ApiConfig.getBaseUrl();
        assertNotNull(baseUrl, "Base URL must not be null");
        assertFalse(baseUrl.isBlank(), "Base URL must not be blank");

        assertNotNull(ApiConfig.getAuthStrategy(), "Auth strategy must not be null");
        assertTrue(ApiConfig.getRequestTimeoutMs() > 0, "Timeout must be positive");
    }

    @Test
    public void authManagerInstantiates() {
        AuthManager manager = AuthManager.getInstance();
        assertNotNull(manager, "AuthManager singleton must not be null");
        assertSame(AuthManager.getInstance(), manager,
            "AuthManager.getInstance() must always return the same instance");
    }

    @Test
    public void specBuilderProducesSpec() {
        var builder = SpecBuilder.requestSpec(Role.READER);
        assertNotNull(builder, "SpecBuilder.requestSpec() must return a non-null builder");
        assertNotNull(builder.build(), "Built RequestSpecification must not be null");
    }

    @Test
    public void serviceClassesAreInjected() {
        assertNotNull(partService,
            "PartService must be initialised by BaseTest.@BeforeSuite");
        assertNotNull(partCategoryService,
            "PartCategoryService must be initialised by BaseTest.@BeforeSuite");
    }

    @Test
    public void modelBuildersWork() {
        PartRequest part = PartRequest.builder()
            .name("Smoke Test Part")
            .description("Created by SmokeTest")
            .active(true)
            .build();

        assertEquals(part.getName(), "Smoke Test Part");
        assertEquals(part.getDescription(), "Created by SmokeTest");
        assertTrue(part.getActive());

        PartCategoryRequest category = PartCategoryRequest.builder()
            .name("Smoke Test Category")
            .structural(false)
            .build();

        assertEquals(category.getName(), "Smoke Test Category");
        assertFalse(category.getStructural());
    }
}
