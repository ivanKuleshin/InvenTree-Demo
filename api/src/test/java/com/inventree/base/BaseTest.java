package com.inventree.base;

import com.inventree.auth.AuthManager;
import com.inventree.auth.Role;
import com.inventree.config.ApiConfig;
import com.inventree.service.CompanyService;
import com.inventree.service.PartCategoryService;
import com.inventree.service.PartService;
import com.inventree.service.StockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    protected static PartService partService;
    protected static PartCategoryService partCategoryService;
    protected static StockService stockService;
    protected static CompanyService companyService;

    @BeforeSuite(alwaysRun = true)
    public void initFramework() {
        log.info("=== Initialising InvenTree API Test Suite ===");
        log.info("Base URL: {}", ApiConfig.getBaseUrl());
        log.info("Auth strategy: {}", ApiConfig.getAuthStrategy());

        partService = new PartService();
        partCategoryService = new PartCategoryService();
        stockService = new StockService();
        companyService = new CompanyService();

        for (Role role : Role.values()) {
            try {
                AuthManager.getInstance().getToken(role);
                log.info("Auth token cached for role: {}", role);
            } catch (Exception e) {
                log.warn("Could not pre-fetch token for role {}: {}", role, e.getMessage());
            }
        }

        log.info("=== Framework initialisation complete ===");
    }

    @AfterSuite(alwaysRun = true)
    public void teardownFramework() {
        AuthManager.getInstance().clearCache();
        log.info("=== InvenTree API Test Suite teardown complete ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void logTestStart(Method method) {
        log.info(">> Starting test: {}", method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void logTestEnd(Method method, ITestResult result) {
        String outcome = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP    -> "SKIPPED";
            default                  -> "UNKNOWN";
        };
        log.info("<< Finished test: {} — {}", method.getName(), outcome);
    }
}
