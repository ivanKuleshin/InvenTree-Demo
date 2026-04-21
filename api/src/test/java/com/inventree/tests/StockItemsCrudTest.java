package com.inventree.tests;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartListParams;
import com.inventree.model.PartRequest;
import com.inventree.model.StockItemDetail;
import com.inventree.model.StockItemRequest;
import com.inventree.model.StockLocationDetail;
import com.inventree.model.StockLocationRequest;
import com.inventree.testdata.PartTestData;
import com.inventree.testdata.StockTestData;
import com.inventree.util.HttpStatus;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Stock Management")
@Feature("Stock Items CRUD")
public class StockItemsCrudTest extends BaseTest {

    private final List<Integer> createdStockItemIds = new ArrayList<>();
    private final List<Integer> createdPartIds = new ArrayList<>();
    private final List<Integer> createdLocationIds = new ArrayList<>();

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdStockItemIds.forEach(id -> {
            try {
                stockService.deleteStockItem(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock item {}", id, t);
            }
        });
        createdStockItemIds.clear();

        createdLocationIds.forEach(id -> {
            try {
                stockService.deleteStockLocationRaw(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock location {}", id, t);
            }
        });
        createdLocationIds.clear();

        createdPartIds.forEach(id -> {
            try {
                partService.patchPart(id, PartTestData.patchActiveOnly(false), Role.ADMIN);
                partService.deletePart(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting part {}", id, t);
            }
        });
        createdPartIds.clear();
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("List Stock Items")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_001_getStockListReturnsPaginatedResultWithFullFieldSet() {
        Response response = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_LIMIT, StockTestData.DEFAULT_PAGE_LIMIT), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        assertTrue(response.jsonPath().getInt("count") > 0, "count must be > 0");
        List<Map<String, Object>> results = response.jsonPath().getList("results");
        assertNotNull(results, "results must not be null");
        assertFalse(results.isEmpty(), "results must not be empty");
        assertTrue(results.size() <= StockTestData.DEFAULT_PAGE_LIMIT, "results must not exceed limit");
        assertNull(response.jsonPath().get("previous"), "previous must be null on first page");

        Map<String, Object> first = results.getFirst();
        assertNotNull(first.get("pk"), "pk must be present");
        assertNotNull(first.get("part"), "part must be present");
        assertNotNull(first.get("quantity"), "quantity must be present");
        assertNotNull(first.get("status"), "status must be present");
        assertNotNull(first.get("status_text"), "status_text must be present");
        assertNotNull(first.get("in_stock"), "in_stock must be present");
        assertNotNull(first.get("is_building"), "is_building must be present");
        assertNotNull(first.get("delete_on_deplete"), "delete_on_deplete must be present");
        assertNotNull(first.get("barcode_hash"), "barcode_hash must be present");
        assertNotNull(first.get("updated"), "updated must be present");
        assertNotNull(first.get("tracking_items"), "tracking_items must be present");

        String statusText = (String) first.get("status_text");
        Set<String> validStatusTexts = Set.of("OK", "Attention needed", "Damaged", "Destroyed",
                "Rejected", "Lost", "Quarantined", "Returned");
        assertTrue(validStatusTexts.contains(statusText),
                "status_text must be one of the documented labels, got: " + statusText);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Get Stock Item by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_002_getStockItemByIdReturnsSingleItemWithCorrectFields() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 10.0), Role.ADMIN);
        int itemPk = created.getPk();
        createdStockItemIds.add(itemPk);

        Response response = stockService.getStockItemByIdRaw(itemPk, Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        StockItemDetail item = response.as(StockItemDetail.class);
        assertEquals(item.getPk(), Integer.valueOf(itemPk));
        assertNotNull(item.getPart(), "part must not be null");
        assertNotNull(item.getQuantity(), "quantity must not be null");
        assertNotNull(item.getStatus(), "status must not be null");
        assertNotNull(item.getInStock(), "in_stock must not be null");
        assertNotNull(item.getBarcodeHash(), "barcode_hash must not be null");
        assertTrue(item.getQuantity() >= 0, "quantity must be >= 0");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_003_postStockItemCreatesItemWithRequiredFieldsOnly() {
        int partPk = findActivePartPk();
        StockItemRequest request = StockTestData.minimalStockItem(partPk, 10.0);

        Response createResponse = stockService.createStockItemRaw(request, Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        assertTrue(newPk > 0, "pk must be a positive integer");
        createdStockItemIds.add(newPk);

        assertEquals(createResponse.jsonPath().getInt("part"), partPk);
        assertEquals(createResponse.jsonPath().getDouble("quantity"), 10.0);
        assertEquals(createResponse.jsonPath().getInt("status"), StockTestData.STOCK_STATUS_OK);
        assertTrue(createResponse.jsonPath().getBoolean("in_stock"), "in_stock must be true");
        assertNull(createResponse.jsonPath().get("location"), "location must be null");

        StockItemDetail fetched = stockService.getStockItemById(newPk, Role.READER);
        assertEquals(fetched.getPk(), Integer.valueOf(newPk));
        assertEquals(fetched.getQuantity(), 10.0);
        assertEquals(fetched.getStatus(), Integer.valueOf(StockTestData.STOCK_STATUS_OK));
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Serialized Stock Item")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_004_postStockItemCreatesSerializedItemsUsingSerialNumbersField() {
        int trackablePartPk = findTrackablePartPk();
        StockLocationDetail location = createTemporaryLocation("TC-ASCRUD-004");
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        StockItemRequest request = StockTestData.stockItemWithSerialNumbers(
                trackablePartPk, locationPk, 3.0, "1-3");

        Response createResponse = stockService.createStockItemRaw(request, Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        PaginatedResponse<StockItemDetail> serializedItems = stockService.listStockItemsPaginated(
                Map.of(StockTestData.QUERY_PARAM_PART, trackablePartPk,
                        StockTestData.QUERY_PARAM_SERIALIZED, StockTestData.QUERY_VALUE_TRUE,
                        StockTestData.QUERY_PARAM_LOCATION, locationPk),
                Role.ADMIN);

        assertNotNull(serializedItems.getResults(), "serialized items results must not be null");
        assertEquals(serializedItems.getResults().size(), 3, "exactly 3 serialized items must exist");

        List<String> serials = serializedItems.getResults().stream()
                .map(StockItemDetail::getSerial)
                .toList();
        assertTrue(serials.contains("1"), "serial 1 must exist");
        assertTrue(serials.contains("2"), "serial 2 must exist");
        assertTrue(serials.contains("3"), "serial 3 must exist");

        for (StockItemDetail si : serializedItems.getResults()) {
            assertEquals(si.getQuantity(), 1.0, "serialized item quantity must be 1");
            assertEquals(si.getLocation(), Integer.valueOf(locationPk));
            createdStockItemIds.add(si.getPk());
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item with Batch")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_005_postStockItemCreatesItemWithBatchCode() {
        int partPk = findActivePartPk();
        String batch = "BATCH-2026-001";
        String notes = "Initial receipt";
        StockItemRequest request = StockTestData.stockItemWithBatch(
                partPk, 25.0, batch, notes, false);

        Response createResponse = stockService.createStockItemRaw(request, Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        createdStockItemIds.add(newPk);

        assertEquals(createResponse.jsonPath().getString("batch"), batch);
        assertEquals(createResponse.jsonPath().getString("notes"), notes);
        assertFalse(createResponse.jsonPath().getBoolean("delete_on_deplete"));
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Filter Stock Items by Part")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_006_getStockListFiltersByPartPk() {
        int partPk = findActivePartPk();
        StockItemDetail item = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 5.0), Role.ADMIN);
        createdStockItemIds.add(item.getPk());

        Response response = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_PART, partPk), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        assertTrue(response.jsonPath().getInt("count") > 0, "count must reflect items for the part");
        List<Map<String, Object>> results = response.jsonPath().getList("results");
        assertFalse(results.isEmpty(), "results must not be empty");
        for (Map<String, Object> result : results) {
            assertEquals(result.get("part"), partPk, "every result must have part=" + partPk);
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Filter Stock Items by Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_007_getStockListFiltersByLocationPk() {
        StockLocationDetail location = createTemporaryLocation("TC-ASCRUD-007");
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        int partPk = findActivePartPk();
        StockItemDetail item = stockService.createStockItem(
                StockTestData.stockItemWithLocation(partPk, 5.0, locationPk), Role.ADMIN);
        createdStockItemIds.add(item.getPk());

        Response response = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_LOCATION, locationPk), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> results = response.jsonPath().getList("results");
        assertFalse(results.isEmpty(), "results must not be empty");
        for (Map<String, Object> result : results) {
            assertEquals(result.get("location"), locationPk, "every result must have location=" + locationPk);
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Filter Stock Items by Status")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_008_getStockListFiltersByStatusCode() {
        Response responseOk = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_STATUS, StockTestData.STOCK_STATUS_OK), Role.READER);
        responseOk.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> resultsOk = responseOk.jsonPath().getList("results");
        for (Map<String, Object> result : resultsOk) {
            assertEquals(result.get("status"), StockTestData.STOCK_STATUS_OK,
                    "every result must have status=" + StockTestData.STOCK_STATUS_OK);
        }

        Response responseDamaged = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_STATUS, StockTestData.STOCK_STATUS_DAMAGED), Role.READER);
        responseDamaged.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> resultsDamaged = responseDamaged.jsonPath().getList("results");
        for (Map<String, Object> result : resultsDamaged) {
            assertEquals(result.get("status"), StockTestData.STOCK_STATUS_DAMAGED,
                    "every result must have status=" + StockTestData.STOCK_STATUS_DAMAGED);
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Filter Unlocated Stock Items")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_009_getStockListFiltersUnlocatedItems() {
        int partPk = findActivePartPk();
        StockItemDetail item = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 3.0), Role.ADMIN);
        createdStockItemIds.add(item.getPk());

        Response response = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_LOCATION, StockTestData.QUERY_VALUE_NULL), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> results = response.jsonPath().getList("results");
        assertFalse(results.isEmpty(), "results must not be empty");
        for (Map<String, Object> result : results) {
            assertNull(result.get("location"), "every result must have location=null");
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Filter Serialized Stock Items")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_010_getStockListFiltersSerializedItems() {
        Response responseSerialized = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_SERIALIZED, StockTestData.QUERY_VALUE_TRUE), Role.READER);
        responseSerialized.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> resultsTrue = responseSerialized.jsonPath().getList("results");
        for (Map<String, Object> result : resultsTrue) {
            Object serial = result.get("serial");
            assertTrue(serial != null && !serial.toString().isEmpty(),
                    "serialized=true results must have a non-null non-empty serial");
        }

        Response responseNonSerialized = stockService.listStockItemsRaw(
                Map.of(StockTestData.QUERY_PARAM_SERIALIZED, StockTestData.QUERY_VALUE_FALSE), Role.READER);
        responseNonSerialized.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> resultsFalse = responseNonSerialized.jsonPath().getList("results");
        for (Map<String, Object> result : resultsFalse) {
            Object serial = result.get("serial");
            assertTrue(serial == null || serial.toString().isEmpty(),
                    "serialized=false results must have null or empty serial");
        }
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Full Update Stock Item")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_011_putStockItemReplacesAllWritableFields() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 5.0), Role.ADMIN);
        int itemPk = created.getPk();
        createdStockItemIds.add(itemPk);

        StockLocationDetail location = createTemporaryLocation("TC-ASCRUD-011");
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        StockItemRequest updateRequest = StockTestData.fullStockItemUpdate(
                partPk, locationPk, 50.0, StockTestData.STOCK_STATUS_ATTENTION,
                "BATCH-PUT-TEST", "Updated via PUT", true, "Box");

        Response updateResponse = stockService.updateStockItemRaw(itemPk, updateRequest, Role.ADMIN);
        updateResponse.then().statusCode(HttpStatus.SC_OK);

        assertEquals(updateResponse.jsonPath().getDouble("quantity"), 50.0);
        assertEquals(updateResponse.jsonPath().getInt("location"), locationPk);
        assertEquals(updateResponse.jsonPath().getInt("status"), StockTestData.STOCK_STATUS_ATTENTION);
        assertEquals(updateResponse.jsonPath().getString("batch"), "BATCH-PUT-TEST");
        assertEquals(updateResponse.jsonPath().getString("notes"), "Updated via PUT");
        assertTrue(updateResponse.jsonPath().getBoolean("delete_on_deplete"));
        assertEquals(updateResponse.jsonPath().getString("packaging"), "Box");

        StockItemDetail fetched = stockService.getStockItemById(itemPk, Role.READER);
        assertEquals(fetched.getQuantity(), 50.0);
        assertEquals(fetched.getLocation(), Integer.valueOf(locationPk));
        assertEquals(fetched.getStatus(), Integer.valueOf(StockTestData.STOCK_STATUS_ATTENTION));
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Partial Update Stock Item Quantity")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_012_patchStockItemPartiallyUpdatesQuantity() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 20.0), Role.ADMIN);
        int itemPk = created.getPk();
        createdStockItemIds.add(itemPk);

        StockItemDetail original = stockService.getStockItemById(itemPk, Role.READER);
        double originalQty = original.getQuantity();

        StockItemRequest patchRequest = StockItemRequest.builder().quantity(99.0).build();
        Response patchResponse = stockService.patchStockItemRaw(itemPk, patchRequest, Role.ADMIN);
        patchResponse.then().statusCode(HttpStatus.SC_OK);

        assertEquals(patchResponse.jsonPath().getDouble("quantity"), 99.0);
        assertEquals(patchResponse.jsonPath().getInt("status"), original.getStatus(),
                "status must be unchanged");
        assertEquals(patchResponse.jsonPath().getInt("part"), partPk,
                "part must be unchanged");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Partial Update Stock Item Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_013_patchStockItemPartiallyUpdatesLocation() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 10.0), Role.ADMIN);
        int itemPk = created.getPk();
        createdStockItemIds.add(itemPk);

        StockLocationDetail locationB = createTemporaryLocation("TC-ASCRUD-013");
        int locationBPk = locationB.getPk();
        createdLocationIds.add(locationBPk);

        StockItemRequest patchRequest = StockItemRequest.builder().location(locationBPk).build();
        Response patchResponse = stockService.patchStockItemRaw(itemPk, patchRequest, Role.ADMIN);
        patchResponse.then().statusCode(HttpStatus.SC_OK);

        assertEquals(patchResponse.jsonPath().getInt("location"), locationBPk);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Partial Update Stock Item Multiple Fields")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_014_patchStockItemUpdatesStatusBatchNotesPackagingExpiry() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 10.0), Role.ADMIN);
        int itemPk = created.getPk();
        createdStockItemIds.add(itemPk);

        StockItemRequest patchRequest = StockItemRequest.builder()
                .status(StockTestData.STOCK_STATUS_DAMAGED)
                .batch("BATCH-PATCH")
                .notes("Patched notes field")
                .packaging("Bag")
                .expiryDate("2027-12-31")
                .build();

        Response patchResponse = stockService.patchStockItemRaw(itemPk, patchRequest, Role.ADMIN);
        patchResponse.then().statusCode(HttpStatus.SC_OK);

        assertEquals(patchResponse.jsonPath().getInt("status"), StockTestData.STOCK_STATUS_DAMAGED);
        assertEquals(patchResponse.jsonPath().getString("batch"), "BATCH-PATCH");
        assertEquals(patchResponse.jsonPath().getString("notes"), "Patched notes field");
        assertEquals(patchResponse.jsonPath().getString("packaging"), "Bag");
        assertEquals(patchResponse.jsonPath().getString("expiry_date"), "2027-12-31");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Delete Stock Item")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_015_deleteStockItemRemovesItem() {
        int partPk = findActivePartPk();
        StockItemDetail created = stockService.createStockItem(
                StockTestData.minimalStockItem(partPk, 5.0), Role.ADMIN);
        int itemPk = created.getPk();

        stockService.getStockItemByIdRaw(itemPk, Role.READER).then().statusCode(HttpStatus.SC_OK);

        Response deleteResponse = stockService.deleteStockItemRaw(itemPk, Role.ADMIN);
        deleteResponse.then().statusCode(HttpStatus.SC_NO_CONTENT);
        assertEquals(deleteResponse.body().asString(), StockTestData.EMPTY_BODY);

        stockService.getStockItemByIdRaw(itemPk, Role.READER).then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Negative: Non-existent Part")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_016_postStockItemWithNonExistentPartFkReturns400() {
        Response response = stockService.createStockItemRaw(
                Map.of("part", StockTestData.NONEXISTENT_PK, "quantity", 1), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("part"), "error must reference 'part' field");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Negative: Non-existent Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_017_postStockItemWithNonExistentLocationFkReturns400() {
        int partPk = findActivePartPk();
        Response response = stockService.createStockItemRaw(
                Map.of("part", partPk, "quantity", 1, "location", StockTestData.NONEXISTENT_PK), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("location"), "error must reference 'location' field");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Negative: Structural Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_018_postStockItemWithStructuralLocationReturns400() {
        int partPk = findActivePartPk();
        StockLocationDetail structuralLoc = stockService.createStockLocation(
                StockTestData.structuralLocation(
                        StockTestData.testLocationName("TC-ASCRUD-018", "Structural")),
                Role.ADMIN);
        int structuralPk = structuralLoc.getPk();
        createdLocationIds.add(structuralPk);

        stockService.getStockLocationByIdRaw(structuralPk, Role.READER).then().statusCode(HttpStatus.SC_OK);

        Response response = stockService.createStockItemRaw(
                Map.of("part", partPk, "quantity", 5, "location", structuralPk), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Negative: Missing Required Fields")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_019_postStockItemWithMissingRequiredFieldsReturns400() {
        Response response = stockService.createStockItemRaw(Map.of(), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("part"), "error must reference 'part' field");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Security: No Auth")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASCRUD_020_postStockItemWithoutAuthenticationReturns401Or403() {
        Response response = stockService.stockRemoveUnauthenticated(
                Map.of("items", List.of(Map.of("pk", 1, "quantity", "1.000"))));

        int status = response.statusCode();
        assertTrue(status == HttpStatus.SC_UNAUTHORIZED || status == HttpStatus.SC_FORBIDDEN,
                "status must be 401 or 403, got: " + status);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Delete Stock Item - Negative: Non-existent PK")
    @Severity(SeverityLevel.MINOR)
    public void tc_ASCRUD_021_deleteStockItemOnNonExistentPkReturns404() {
        Response response = stockService.deleteStockItemRaw(StockTestData.NONEXISTENT_PK, Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Stock Item - Negative: Negative Quantity")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_022_postStockItemWithNegativeQuantityReturns400() {
        int partPk = findActivePartPk();
        Response response = stockService.createStockItemRaw(
                Map.of("part", partPk, "quantity", -5), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("quantity"), "error must reference 'quantity' field");
    }

    @Test(groups = {"regression", "stock-items"})
    @Story("Create Serialized Stock Item - Negative: Duplicate Serial")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASCRUD_023_postStockItemWithDuplicateSerialNumberReturns400() {
        int trackablePartPk = findTrackablePartPk();
        StockLocationDetail location = createTemporaryLocation("TC-ASCRUD-023");
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        String uniqueSerial = "SN-ASCRUD023-" + StockTestData.RUN_ID;
        StockItemRequest firstRequest = StockTestData.stockItemWithSerialNumbers(
                trackablePartPk, locationPk, 1.0, uniqueSerial);
        Response firstResponse = stockService.createStockItemRaw(firstRequest, Role.ADMIN);
        firstResponse.then().statusCode(HttpStatus.SC_CREATED);

        PaginatedResponse<StockItemDetail> created = stockService.listStockItemsPaginated(
                Map.of(StockTestData.QUERY_PARAM_PART, trackablePartPk,
                        StockTestData.QUERY_PARAM_LOCATION, locationPk,
                        StockTestData.QUERY_PARAM_SERIALIZED, StockTestData.QUERY_VALUE_TRUE),
                Role.ADMIN);
        created.getResults().forEach(si -> createdStockItemIds.add(si.getPk()));

        StockItemRequest duplicateRequest = StockTestData.stockItemWithSerialNumbers(
                trackablePartPk, locationPk, 1.0, uniqueSerial);
        Response duplicateResponse = stockService.createStockItemRaw(duplicateRequest, Role.ADMIN);
        duplicateResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private int findActivePartPk() {
        PaginatedResponse<Part> listing = partService.listParts(
                PartListParams.builder().limit(StockTestData.DEFAULT_PAGE_LIMIT).build(), Role.ADMIN);
        return listing.getResults().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActive()) && !Boolean.TRUE.equals(p.getVirtual()))
                .findFirst()
                .orElseGet(() -> {
                    Part created = partService.createPart(
                            PartRequest.builder()
                                    .name(StockTestData.testStockItemName("ASCRUD", "SetupPart"))
                                    .active(true)
                                    .build(),
                            Role.ADMIN);
                    createdPartIds.add(created.getPk());
                    return created;
                }).getPk();
    }

    private int findTrackablePartPk() {
        PaginatedResponse<Part> listing = partService.listParts(
                PartListParams.builder().limit(PartTestData.DEFAULT_PAGE_LIMIT).build(), Role.ADMIN);
        return listing.getResults().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActive()) && Boolean.TRUE.equals(p.getTrackable()))
                .findFirst()
                .orElseGet(() -> {
                    Part created = partService.createPart(
                            PartRequest.builder()
                                    .name(StockTestData.testStockItemName("ASCRUD", "TrackablePart"))
                                    .active(true)
                                    .trackable(true)
                                    .build(),
                            Role.ADMIN);
                    createdPartIds.add(created.getPk());
                    return created;
                }).getPk();
    }

    private StockLocationDetail createTemporaryLocation(String testCaseId) {
        String locationName = StockTestData.testLocationName(testCaseId, "Temp");
        return stockService.createStockLocation(
                StockTestData.minimalLocation(locationName), Role.ADMIN);
    }
}
