package com.inventree.tests;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.PartInternalPrice;
import com.inventree.model.PartPricing;
import com.inventree.model.PartSalePrice;
import com.inventree.testdata.PricingTestData;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part Pricing")
public class PartPricingTest extends BaseTest {

    private final List<Integer> createdInternalPriceIds = new ArrayList<>();
    private final List<Integer> createdSalePriceIds = new ArrayList<>();
    private boolean aggregateOverrideSet = false;

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        for (Integer id : createdInternalPriceIds) {
            try {
                pricingService.deleteInternalPriceRaw(id, Role.ADMIN);
            } catch (Exception e) {
                log.warn("Failed to delete internal price id={}: {}", id, e.getMessage());
            }
        }
        createdInternalPriceIds.clear();

        for (Integer id : createdSalePriceIds) {
            try {
                pricingService.deleteSalePriceRaw(id, Role.ADMIN);
            } catch (Exception e) {
                log.warn("Failed to delete sale price id={}: {}", id, e.getMessage());
            }
        }
        createdSalePriceIds.clear();

        if (aggregateOverrideSet) {
            Map<String, Object> clearPayload = new HashMap<>();
            clearPayload.put(PricingTestData.FIELD_OVERRIDE_MIN, null);
            clearPayload.put(PricingTestData.FIELD_OVERRIDE_MAX, null);
            try {
                pricingService.patchAggregatePricing(PricingTestData.PRICING_PART_PK, clearPayload, Role.ADMIN);
            } catch (Exception e) {
                log.warn("Failed to clear aggregate pricing override: {}", e.getMessage());
            }
            aggregateOverrideSet = false;
        }
    }

    @Test(groups = {"regression", "pricing"})
    @Story("List Internal Prices")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APPRICE_001_getInternalPriceListReturnsResult() {
        PartInternalPrice created = createAndTrackInternalPrice(PricingTestData.standardInternalPricePayload());
        assertNotNull(created.getPk(), "created pk must not be null");

        PaginatedResponse<PartInternalPrice> response = pricingService.listInternalPrices(
                Map.of(PricingTestData.QUERY_PARAM_LIMIT, PricingTestData.LIST_LIMIT), Role.READER);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() > 0, "count must be > 0");
        assertNotNull(response.getResults(), "results must not be null");
        assertFalse(response.getResults().isEmpty(), "results must be non-empty");

        PartInternalPrice first = response.getResults().getFirst();
        assertNotNull(first.getPk(), "pk must not be null");
        assertNotNull(first.getPart(), "part must not be null");
        assertNotNull(first.getQuantity(), "quantity must not be null");
        assertNotNull(first.getPrice(), "price must not be null");
        assertNotNull(first.getPriceCurrency(), "price_currency must not be null");
        assertTrue(first.getPrice() > 0, "price must be a positive number");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Get Internal Price by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APPRICE_002_getInternalPriceByIdReturnsSingleRecord() {
        PartInternalPrice price = pricingService.getInternalPriceById(PricingTestData.KNOWN_INTERNAL_PRICE_PK, Role.READER);
        assertEquals(price.getPk(), Integer.valueOf(PricingTestData.KNOWN_INTERNAL_PRICE_PK));
        assertNotNull(price.getPart(), "part must not be null");
        assertNotNull(price.getQuantity(), "quantity must not be null");
        assertNotNull(price.getPrice(), "price must not be null");
        assertNotNull(price.getPriceCurrency(), "price_currency must not be null");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Create Internal Price")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APPRICE_003_createInternalPriceReturns201() {
        Response response = pricingService.createInternalPriceRaw(PricingTestData.standardInternalPricePayload(), Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);

        int newPk = response.jsonPath().getInt(PricingTestData.FIELD_PK);
        createdInternalPriceIds.add(newPk);

        assertTrue(newPk > 0, "pk must be a positive integer");
        assertEquals(response.jsonPath().getInt(PricingTestData.FIELD_PART),
                PricingTestData.PRICING_PART_PK, "part must match");
        assertEquals(response.jsonPath().getDouble(PricingTestData.FIELD_QUANTITY),
                (double) PricingTestData.CREATE_QUANTITY, "quantity must match");
        assertEquals(response.jsonPath().getDouble(PricingTestData.FIELD_PRICE),
                PricingTestData.EXPECTED_PRICE_NUMERIC,
                PricingTestData.PRICE_DELTA, "price must equal " + PricingTestData.EXPECTED_PRICE_NUMERIC);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Internal Price")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APPRICE_004_patchInternalPriceUpdatesPrice() {
        PartInternalPrice created = createAndTrackInternalPrice(PricingTestData.standardInternalPricePayload());
        int newPk = created.getPk();

        PartInternalPrice patched = pricingService.patchInternalPrice(
                newPk, Map.of(PricingTestData.FIELD_PRICE, PricingTestData.PATCH_PRICE), Role.ADMIN);

        assertEquals((double) patched.getPrice(), PricingTestData.EXPECTED_PATCH_PRICE,
                PricingTestData.PRICE_DELTA, "price must equal " + PricingTestData.EXPECTED_PATCH_PRICE);
        assertEquals(patched.getQuantity(), (double) PricingTestData.CREATE_QUANTITY, "quantity must be unchanged");
        assertEquals(patched.getPart(), Integer.valueOf(PricingTestData.PRICING_PART_PK), "part must be unchanged");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Delete Internal Price")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APPRICE_005_deleteInternalPriceReturns204() {
        PartInternalPrice created = pricingService.createInternalPrice(PricingTestData.standardInternalPricePayload(), Role.ADMIN);
        int newPk = created.getPk();
        createdInternalPriceIds.add(newPk);

        Response deleteResponse = pricingService.deleteInternalPriceRaw(newPk, Role.ADMIN);
        ResponseValidator.assertStatus(deleteResponse, HttpStatus.SC_NO_CONTENT);
        createdInternalPriceIds.remove(Integer.valueOf(newPk));

        Response getResponse = pricingService.getInternalPriceByIdRaw(newPk, Role.ADMIN);
        ResponseValidator.assertStatus(getResponse, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("List Sale Prices")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APSPRICE_001_getSalePriceListReturnsResult() {
        PartSalePrice created = createAndTrackSalePrice(PricingTestData.standardSalePricePayload());
        assertNotNull(created.getPk(), "created pk must not be null");

        PaginatedResponse<PartSalePrice> response = pricingService.listSalePrices(
                Map.of(PricingTestData.QUERY_PARAM_LIMIT, PricingTestData.LIST_LIMIT), Role.READER);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() > 0, "count must be > 0");
        assertNotNull(response.getResults(), "results must not be null");
        assertFalse(response.getResults().isEmpty(), "results must be non-empty");

        PartSalePrice first = response.getResults().getFirst();
        assertNotNull(first.getPk(), "pk must not be null");
        assertNotNull(first.getPart(), "part must not be null");
        assertNotNull(first.getQuantity(), "quantity must not be null");
        assertNotNull(first.getPrice(), "price must not be null");
        assertNotNull(first.getPriceCurrency(), "price_currency must not be null");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Get Sale Price by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APSPRICE_002_getSalePriceByIdReturnsSingleRecord() {
        PartSalePrice price = pricingService.getSalePriceById(PricingTestData.KNOWN_SALE_PRICE_PK, Role.READER);
        assertEquals(price.getPk(), Integer.valueOf(PricingTestData.KNOWN_SALE_PRICE_PK));
        assertNotNull(price.getPart(), "part must not be null");
        assertNotNull(price.getQuantity(), "quantity must not be null");
        assertNotNull(price.getPrice(), "price must not be null");
        assertNotNull(price.getPriceCurrency(), "price_currency must not be null");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Create Sale Price")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APSPRICE_003_createSalePriceReturns201() {
        Response response = pricingService.createSalePriceRaw(PricingTestData.standardSalePricePayload(), Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);

        int newPk = response.jsonPath().getInt(PricingTestData.FIELD_PK);
        createdSalePriceIds.add(newPk);

        assertTrue(newPk > 0, "pk must be a positive integer");
        assertEquals(response.jsonPath().getInt(PricingTestData.FIELD_PART),
                PricingTestData.SALABLE_PART_PK, "part must match");
        assertEquals(response.jsonPath().getDouble(PricingTestData.FIELD_QUANTITY),
                (double) PricingTestData.SALE_CREATE_QUANTITY, "quantity must match");
        assertEquals(response.jsonPath().getDouble(PricingTestData.FIELD_PRICE),
                PricingTestData.EXPECTED_SALE_PRICE_NUMERIC,
                PricingTestData.PRICE_DELTA, "price must equal " + PricingTestData.EXPECTED_SALE_PRICE_NUMERIC);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Delete Sale Price")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APSPRICE_004_deleteSalePriceReturns204() {
        PartSalePrice created = pricingService.createSalePrice(PricingTestData.standardSalePricePayload(), Role.ADMIN);
        int newPk = created.getPk();
        createdSalePriceIds.add(newPk);

        Response deleteResponse = pricingService.deleteSalePriceRaw(newPk, Role.ADMIN);
        ResponseValidator.assertStatus(deleteResponse, HttpStatus.SC_NO_CONTENT);
        createdSalePriceIds.remove(Integer.valueOf(newPk));

        Response getResponse = pricingService.getSalePriceByIdRaw(newPk, Role.ADMIN);
        ResponseValidator.assertStatus(getResponse, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Get Aggregate Pricing")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APAGPRICE_001_getAggregatePricingReturnsSummary() {
        PartPricing pricing = pricingService.getAggregatePricing(PricingTestData.PRICING_PART_PK, Role.READER);

        assertNotNull(pricing.getCurrency(), "currency must not be null");
        assertNotNull(pricing.getScheduledForUpdate(), "scheduled_for_update must not be null");
        assertNotNull(pricing.getInternalCostMin(), "internal_cost_min must not be null");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Aggregate Pricing Sets Override Min")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APAGPRICE_002_patchAggregatePricingSetsOverrideMin() {
        Map<String, Object> overridePayload = Map.of(
                PricingTestData.FIELD_OVERRIDE_MIN, PricingTestData.OVERRIDE_MIN_VALUE,
                PricingTestData.FIELD_OVERRIDE_MIN_CURRENCY, PricingTestData.CURRENCY_USD);

        PartPricing patched = pricingService.patchAggregatePricing(
                PricingTestData.PRICING_PART_PK, overridePayload, Role.ADMIN);
        aggregateOverrideSet = true;

        assertNotNull(patched.getOverrideMin(), "override_min must not be null after setting");
        assertEquals(patched.getOverrideMin(), PricingTestData.EXPECTED_OVERRIDE_MIN,
                "override_min must equal " + PricingTestData.EXPECTED_OVERRIDE_MIN);
        assertNotNull(patched.getOverallMin(), "overall_min must not be null after setting override");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Aggregate Pricing Clears Override")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APAGPRICE_003_patchAggregatePricingClearsOverride() {
        Map<String, Object> ensureClean = new HashMap<>();
        ensureClean.put(PricingTestData.FIELD_OVERRIDE_MIN, null);
        ensureClean.put(PricingTestData.FIELD_OVERRIDE_MAX, null);
        pricingService.patchAggregatePricing(PricingTestData.PRICING_PART_PK, ensureClean, Role.ADMIN);

        Map<String, Object> setPayload = Map.of(
                PricingTestData.FIELD_OVERRIDE_MIN, PricingTestData.OVERRIDE_MIN_VALUE,
                PricingTestData.FIELD_OVERRIDE_MIN_CURRENCY, PricingTestData.CURRENCY_USD);
        pricingService.patchAggregatePricing(PricingTestData.PRICING_PART_PK, setPayload, Role.ADMIN);
        aggregateOverrideSet = true;

        Map<String, Object> clearPayload = new HashMap<>();
        clearPayload.put(PricingTestData.FIELD_OVERRIDE_MIN, null);
        clearPayload.put(PricingTestData.FIELD_OVERRIDE_MAX, null);
        PartPricing cleared = pricingService.patchAggregatePricing(
                PricingTestData.PRICING_PART_PK, clearPayload, Role.ADMIN);

        assertNull(cleared.getOverrideMin(), "override_min must be null after clearing");

        PartPricing current = pricingService.getAggregatePricing(PricingTestData.PRICING_PART_PK, Role.READER);
        assertNotNull(current.getInternalCostMin(), "internal_cost_min must not be null");
        assertNotNull(current.getOverallMin(), "overall_min must not be null after clearing override");
        assertTrue(current.getOverallMin() >= current.getInternalCostMin(),
                "overall_min must revert to calculated value >= internal_cost_min");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Get Internal Price by Non-Existent ID Returns 404")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APPRICE_NEG_001_getInternalPriceByNonExistentIdReturns404() {
        Response response = pricingService.getInternalPriceByIdRaw(PricingTestData.NON_EXISTENT_PK, Role.READER);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Create Internal Price With Non-Existent Part FK Returns 400")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APPRICE_NEG_002_createInternalPriceWithInvalidPartReturns400() {
        Map<String, Object> payload = Map.of(
                PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK,
                PricingTestData.FIELD_QUANTITY, PricingTestData.CREATE_QUANTITY,
                PricingTestData.FIELD_PRICE, PricingTestData.CREATE_PRICE,
                PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD);
        Response response = pricingService.createInternalPriceRaw(payload, Role.ADMIN);
        ResponseValidator.assertStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Create Internal Price With Reader Role Returns 403")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APPRICE_NEG_003_createInternalPriceWithReaderRoleReturns403() {
        Response response = pricingService.createInternalPriceRaw(PricingTestData.standardInternalPricePayload(), Role.READER);
        ResponseValidator.assertStatus(response, HttpStatus.SC_FORBIDDEN);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Get Sale Price by Non-Existent ID Returns 404")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APSPRICE_NEG_001_getSalePriceByNonExistentIdReturns404() {
        Response response = pricingService.getSalePriceByIdRaw(PricingTestData.NON_EXISTENT_PK, Role.READER);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Create Sale Price With Non-Existent Part FK Returns 400")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APSPRICE_NEG_002_createSalePriceWithInvalidPartReturns400() {
        Map<String, Object> payload = Map.of(
                PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK,
                PricingTestData.FIELD_QUANTITY, PricingTestData.SALE_CREATE_QUANTITY,
                PricingTestData.FIELD_PRICE, PricingTestData.SALE_CREATE_PRICE,
                PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD);
        Response response = pricingService.createSalePriceRaw(payload, Role.ADMIN);
        ResponseValidator.assertStatus(response, HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Sale Price")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APSPRICE_005_patchSalePriceUpdatesPrice() {
        PartSalePrice created = createAndTrackSalePrice(PricingTestData.standardSalePricePayload());
        int newPk = created.getPk();

        PartSalePrice patched = pricingService.patchSalePrice(
                newPk, Map.of(PricingTestData.FIELD_PRICE, PricingTestData.PATCH_SALE_PRICE), Role.ADMIN);

        assertEquals((double) patched.getPrice(), PricingTestData.EXPECTED_PATCH_SALE_PRICE,
                PricingTestData.PRICE_DELTA, "price must equal " + PricingTestData.EXPECTED_PATCH_SALE_PRICE);
        assertEquals(patched.getQuantity(), (double) PricingTestData.SALE_CREATE_QUANTITY, "quantity must be unchanged");
        assertEquals(patched.getPart(), Integer.valueOf(PricingTestData.SALABLE_PART_PK), "part must be unchanged");
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Internal Price by Non-Existent ID Returns 404")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APPRICE_NEG_004_patchInternalPriceWithNonExistentIdReturns404() {
        Response response = pricingService.patchInternalPriceRaw(PricingTestData.NON_EXISTENT_PK,
                Map.of(PricingTestData.FIELD_PRICE, PricingTestData.PATCH_PRICE), Role.ADMIN);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "pricing"})
    @Story("Patch Aggregate Pricing With Reader Role Returns 403")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APAGPRICE_NEG_001_patchAggregatePricingWithReaderRoleReturns403() {
        Map<String, Object> payload = Map.of(
                PricingTestData.FIELD_OVERRIDE_MIN, PricingTestData.OVERRIDE_MIN_VALUE,
                PricingTestData.FIELD_OVERRIDE_MIN_CURRENCY, PricingTestData.CURRENCY_USD);
        Response response = pricingService.patchAggregatePricingRaw(
                PricingTestData.PRICING_PART_PK, payload, Role.READER);
        ResponseValidator.assertStatus(response, HttpStatus.SC_FORBIDDEN);
    }

    @Step("Create internal price break for part {partId} and track for cleanup")
    private PartInternalPrice createAndTrackInternalPrice(Map<String, Object> payload) {
        PartInternalPrice created = pricingService.createInternalPrice(payload, Role.ADMIN);
        createdInternalPriceIds.add(created.getPk());
        return created;
    }

    @Step("Create sale price break for part and track for cleanup")
    private PartSalePrice createAndTrackSalePrice(Map<String, Object> payload) {
        PartSalePrice created = pricingService.createSalePrice(payload, Role.ADMIN);
        createdSalePriceIds.add(created.getPk());
        return created;
    }
}
