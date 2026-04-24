package com.inventree.tests.stock;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.Company;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartListParams;
import com.inventree.model.PartRequest;
import com.inventree.model.StockItemDetail;
import com.inventree.model.StockItemRequest;
import com.inventree.model.StockLocationDetail;
import com.inventree.testdata.PartTestData;
import com.inventree.testdata.StockTestData;
import com.inventree.util.HttpStatus;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Stock Management")
@Feature("Stock Adjustments")
public class StockAdjustmentsTest extends BaseTest {

    private final List<Integer> createdStockItemIds = new ArrayList<>();
    private final List<Integer> createdLocationIds = new ArrayList<>();
    private final List<Integer> createdPartIds = new ArrayList<>();

    private static final double DELTA = 0.0001;

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdStockItemIds.forEach(id -> {
            try {
                stockItemService.deleteStockItem(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock item {}", id, t);
            }
        });
        createdStockItemIds.clear();

        createdLocationIds.forEach(id -> {
            try {
                stockLocationService.deleteStockLocationRaw(id, Role.ADMIN);
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

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Add Quantity")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_001_postStockAddIncreasesQuantity() {
        int itemPk = createStockItem("ASADJ-001", 20.0);
        double original = fetchQuantity(itemPk);

        Response response = stockAdjustmentService.stockAdd(
            StockTestData.addPayload(itemPk, StockTestData.ADD_QUANTITY,
                "TC-ASADJ-001: add quantity test"),
            Role.ADMIN);
        assertSuccess(response);

        double updated = fetchQuantity(itemPk);
        assertEquals(updated, original + StockTestData.ADD_QUANTITY, DELTA,
            "quantity must equal original + 10");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Remove Quantity")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_002_postStockRemoveDecreasesQuantity() {
        int itemPk = createStockItem("ASADJ-002", 20.0);
        double original = fetchQuantity(itemPk);

        Response response = stockAdjustmentService.stockRemove(
            StockTestData.removePayload(itemPk, StockTestData.REMOVE_QUANTITY,
                "TC-ASADJ-002: remove quantity test"),
            Role.ADMIN);
        assertSuccess(response);

        double updated = fetchQuantity(itemPk);
        assertEquals(updated, original - StockTestData.REMOVE_QUANTITY, DELTA,
            "quantity must equal original - 5");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Count Quantity (Stocktake)")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_003_postStockCountSetsAbsoluteQuantity() {
        int itemPk = createStockItem("ASADJ-003", 20.0);

        Response response = stockAdjustmentService.stockCount(
            StockTestData.countPayload(itemPk, StockTestData.COUNT_QUANTITY,
                "TC-ASADJ-003: Q2 stocktake"),
            Role.ADMIN);
        assertSuccess(response);

        StockItemDetail updated = stockItemService.getStockItemById(itemPk, Role.READER);
        assertEquals(updated.getQuantity(), StockTestData.COUNT_QUANTITY, DELTA,
            "quantity must equal the absolute count value");
        assertNotNull(updated.getStocktakeDate(), "stocktake_date must be set");
        assertTrue(updated.getStocktakeDate().matches("\\d{4}-\\d{2}-\\d{2}"),
            "stocktake_date must be YYYY-MM-DD");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Transfer Between Locations")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_004_postStockTransferMovesItemToNewLocation() {
        int sourceLoc = createLocation("ASADJ-004", "Source");
        int destLoc = createLocation("ASADJ-004", "Dest");
        int partPk = createTestPart("ASADJ-004");

        int itemPk = createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partPk, 10.0, sourceLoc));

        Response response = stockAdjustmentService.stockTransfer(
            StockTestData.transferPayload(itemPk, 10.0, destLoc,
                "TC-ASADJ-004: transfer to new shelf"),
            Role.ADMIN);
        assertSuccess(response);

        StockItemDetail moved = stockItemService.getStockItemById(itemPk, Role.READER);
        assertEquals(moved.getLocation(), Integer.valueOf(destLoc),
            "location must match destination");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Change Status")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_005_postStockChangeStatusUpdatesMultipleItems() {
        int item1 = createStockItem("ASADJ-005a", 5.0);
        int item2 = createStockItem("ASADJ-005b", 5.0);

        Response response = stockAdjustmentService.stockChangeStatus(
            StockTestData.changeStatusPayload(List.of(item1, item2),
                StockTestData.STOCK_STATUS_DAMAGED,
                "TC-ASADJ-005: marked damaged after drop"),
            Role.ADMIN);
        assertSuccess(response);

        for (int pk : List.of(item1, item2)) {
            StockItemDetail state = stockItemService.getStockItemById(pk, Role.READER);
            assertEquals(state.getStatus(), Integer.valueOf(StockTestData.STOCK_STATUS_DAMAGED),
                "status must be 55 (Damaged)");
            assertEquals(state.getStatusText(), "Damaged",
                "status_text must be 'Damaged'");
            assertTrackingEntryExists(pk);
        }
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Assign to Customer")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_006_postStockAssignSetsCustomerAndTakesOutOfStock() {
        int customerPk = findCustomerPk();
        int itemPk = createSalableStockItem("ASADJ-006", 10.0);

        StockItemDetail before = stockItemService.getStockItemById(itemPk, Role.READER);
        assertTrue(before.getInStock(),
            "item must be in_stock=true before assign");

        Response response = stockAdjustmentService.stockAssign(
            StockTestData.assignPayload(itemPk, customerPk,
                "TC-ASADJ-006: assigned for direct shipment"),
            Role.ADMIN);
        assertSuccess(response);

        StockItemDetail after = stockItemService.getStockItemById(itemPk, Role.READER);
        assertEquals(after.getCustomer(), Integer.valueOf(customerPk),
            "customer must be the supplied customer PK");
        assertNotEquals(after.getInStock(), Boolean.TRUE, "in_stock must be false after assign");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Merge Same-Part Items")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_007_postStockMergeSamePartItems() {
        int partPk = createTestPart("ASADJ-007");
        int destLoc = createLocation("ASADJ-007", "Dest");

        int item1Pk = createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partPk, 10.0, destLoc));
        int item2Pk = createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partPk, 20.0, destLoc));

        Response response = stockAdjustmentService.stockMerge(
            StockTestData.mergePayload(item1Pk, item2Pk, destLoc,
                false, false, "TC-ASADJ-007: consolidate after reorg"),
            Role.ADMIN);
        assertSuccess(response);

        PaginatedResponse<StockItemDetail> listing = stockItemService.listStockItemsPaginated(
            Map.of(StockTestData.QUERY_PARAM_PART, partPk,
                StockTestData.QUERY_PARAM_LOCATION, destLoc,
                StockTestData.QUERY_PARAM_LIMIT, 10),
            Role.READER);

        double totalQuantity = listing.getResults().stream()
            .mapToDouble(si -> si.getQuantity() == null ? 0.0 : si.getQuantity())
            .sum();
        assertEquals(totalQuantity, 30.0, DELTA,
            "combined quantity of surviving items at destination must equal 30");

        listing.getResults().forEach(si -> {
            if (!createdStockItemIds.contains(si.getPk())) {
                createdStockItemIds.add(si.getPk());
            }
        });
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Return Customer-Assigned Item")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_008_postStockReturnRestoresCustomerItemToStock() {
        int customerPk = findCustomerPk();
        int returnLoc = createLocation("ASADJ-008", "Return");
        int itemPk = createSalableStockItem("ASADJ-008", 10.0);

        Response assignResponse = stockAdjustmentService.stockAssign(
            StockTestData.assignPayload(itemPk, customerPk, "setup for return"),
            Role.ADMIN);
        assertSuccess(assignResponse);

        StockItemDetail assigned = stockItemService.getStockItemById(itemPk, Role.READER);
        assertEquals(assigned.getCustomer(), Integer.valueOf(customerPk),
            "precondition: item must be assigned to customer");

        Response response = stockAdjustmentService.stockReturn(
            StockTestData.returnPayload(itemPk, 10.0, returnLoc, false,
                "TC-ASADJ-008: customer return RMA-001"),
            Role.ADMIN);
        assertSuccess(response);

        StockItemDetail returned = stockItemService.getStockItemById(itemPk, Role.READER);
        assertTrue(returned.getInStock(), "in_stock must be true after return");
        assertTrue(returned.getCustomer() == null || returned.getCustomer() != customerPk,
            "customer must be cleared or differ after return");

        assertTrackingEntryExists(itemPk);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Merge Different-Part Items - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASADJ_009_postStockMergeDifferentPartsReturns400() {
        int partA = createFreshPart("ASADJ-009a");
        int partB = createFreshPart("ASADJ-009b");
        int destLoc = createLocation("ASADJ-009", "Dest");

        int itemAPk = createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partA, 5.0, destLoc));
        int itemBPk = createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partB, 5.0, destLoc));

        Response response = stockAdjustmentService.stockMerge(
            StockTestData.mergePayload(itemAPk, itemBPk, destLoc,
                false, false, "TC-ASADJ-009: incompatible parts merge"),
            Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Transfer to Structural Location - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASADJ_010_postStockTransferToStructuralLocationReturns400() {
        int itemPk = createStockItem("ASADJ-010", 5.0);

        StockLocationDetail structural = stockLocationService.createStockLocation(
            StockTestData.structuralLocation(
                StockTestData.testLocationName("TC-ASADJ-010", "Structural")),
            Role.ADMIN);
        createdLocationIds.add(structural.getPk());

        Response response = stockAdjustmentService.stockTransfer(
            StockTestData.transferPayload(itemPk, 1.0, structural.getPk(),
                "TC-ASADJ-010: transfer to structural location"),
            Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Change Status with Invalid Code - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASADJ_011_postStockChangeStatusWithInvalidCodeReturns400() {
        int itemPk = createStockItem("ASADJ-011", 5.0);

        Response response = stockAdjustmentService.stockChangeStatus(
            StockTestData.changeStatusPayload(List.of(itemPk),
                StockTestData.STOCK_STATUS_INVALID,
                "TC-ASADJ-011: invalid status"),
            Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Add with Empty Items Array - Negative")
    @Severity(SeverityLevel.MINOR)
    public void tc_ASADJ_012_postStockAddWithEmptyItemsReturns400() {
        Response response = stockAdjustmentService.stockAdd(
            Map.of("items", List.of(), "notes", "TC-ASADJ-012: empty items"),
            Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Assign to Non-Customer Company - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ASADJ_013_postStockAssignToNonCustomerReturns400() {
        int nonCustomerPk = findNonCustomerPk();
        int itemPk = createSalableStockItem("ASADJ-013", 5.0);

        Response response = stockAdjustmentService.stockAssign(
            StockTestData.assignPayload(itemPk, nonCustomerPk,
                "TC-ASADJ-013: assign to non-customer"),
            Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-adjustments"})
    @Story("Remove without Authentication - Security")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ASADJ_014_postStockRemoveWithoutAuthenticationReturns401Or403() {
        Response response = stockAdjustmentService.stockRemoveUnauthenticated(
            Map.of("items", List.of(Map.of("pk", 1, "quantity", "1.000"))));
        int status = response.statusCode();
        assertTrue(status == HttpStatus.SC_UNAUTHORIZED || status == HttpStatus.SC_FORBIDDEN,
            "status must be 401 or 403, got: " + status);
    }

    private int createStockItem(String tcTag, double quantity) {
        int partPk = createTestPart(tcTag);
        int locationPk = createLocation(tcTag, "Bin");
        return createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partPk, quantity, locationPk));
    }

    private int createSalableStockItem(String tcTag, double quantity) {
        int partPk = createSalablePart(tcTag);
        int locationPk = createLocation(tcTag, "Bin");
        return createStockItemAndTrack(
            StockTestData.stockItemWithLocation(partPk, quantity, locationPk));
    }

    private int createSalablePart(String tcTag) {
        PaginatedResponse<Part> listing = partService.listParts(
            PartListParams.builder().limit(StockTestData.DEFAULT_PAGE_LIMIT).build(), Role.ADMIN);
        Part reusable = listing.getResults().stream()
            .filter(p -> Boolean.TRUE.equals(p.getActive())
                && Boolean.TRUE.equals(p.getSalable())
                && !Boolean.TRUE.equals(p.getVirtual())
                && !Boolean.TRUE.equals(p.getTrackable()))
            .findFirst()
            .orElse(null);
        if (reusable != null) {
            return reusable.getPk();
        }
        Part created = partService.createPart(
            PartRequest.builder()
                .name(StockTestData.testStockItemName(tcTag, "Part"))
                .active(true)
                .salable(true)
                .build(),
            Role.ADMIN);
        createdPartIds.add(created.getPk());
        return created.getPk();
    }

    private int createStockItemAndTrack(StockItemRequest request) {
        Response response = stockItemService.createStockItemRaw(request, Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_CREATED);
        int pk = extractPk(response);
        createdStockItemIds.add(pk);
        return pk;
    }

    private int extractPk(Response response) {
        Object root = response.jsonPath().get("$");
        if (root instanceof List) {
            return response.jsonPath().getInt("[0].pk");
        }
        return response.jsonPath().getInt("pk");
    }

    private int createLocation(String tcTag, String suffix) {
        StockLocationDetail loc = stockLocationService.createStockLocation(
            StockTestData.minimalLocation(
                StockTestData.testLocationName("TC-" + tcTag, suffix)),
            Role.ADMIN);
        createdLocationIds.add(loc.getPk());
        return loc.getPk();
    }

    private int createTestPart(String tcTag) {
        PaginatedResponse<Part> listing = partService.listParts(
            PartListParams.builder().limit(StockTestData.DEFAULT_PAGE_LIMIT).build(), Role.ADMIN);
        Part reusable = listing.getResults().stream()
            .filter(p -> Boolean.TRUE.equals(p.getActive())
                && !Boolean.TRUE.equals(p.getVirtual())
                && !Boolean.TRUE.equals(p.getTrackable()))
            .findFirst()
            .orElse(null);
        if (reusable != null) {
            return reusable.getPk();
        }
        Part created = partService.createPart(
            PartRequest.builder()
                .name(StockTestData.testStockItemName(tcTag, "Part"))
                .active(true)
                .build(),
            Role.ADMIN);
        createdPartIds.add(created.getPk());
        return created.getPk();
    }

    private int createFreshPart(String tcTag) {
        Part created = partService.createPart(
            PartRequest.builder()
                .name(StockTestData.testStockItemName(tcTag, "Part"))
                .active(true)
                .build(),
            Role.ADMIN);
        createdPartIds.add(created.getPk());
        return created.getPk();
    }

    private double fetchQuantity(int itemPk) {
        StockItemDetail state = stockItemService.getStockItemById(itemPk, Role.READER);
        assertNotNull(state.getQuantity(), "quantity must be present");
        return state.getQuantity();
    }

    private void assertSuccess(Response response) {
        int status = response.statusCode();
        assertTrue(status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED,
            "adjustment must succeed with 200 or 201, got: " + status
                + " body=" + response.getBody().asString());
    }

    private void assertTrackingEntryExists(int itemPk) {
        Response tracking = stockItemService.getStockTrackingRaw(
            Map.of("item", itemPk,
                StockTestData.QUERY_PARAM_LIMIT, 1,
                StockTestData.QUERY_PARAM_ORDERING,
                StockTestData.QUERY_VALUE_ORDERING_DATE_DESC),
            Role.READER);
        tracking.then().statusCode(HttpStatus.SC_OK);
        int count = tracking.jsonPath().getInt("count");
        assertTrue(count > 0,
            "at least one StockTracking entry must exist for item " + itemPk);
    }

    private int findCustomerPk() {
        PaginatedResponse<Company> customers = companyService.listCompanies(
            Map.of(StockTestData.QUERY_PARAM_IS_CUSTOMER, StockTestData.QUERY_VALUE_TRUE,
                StockTestData.QUERY_PARAM_LIMIT, StockTestData.DEFAULT_PAGE_LIMIT),
            Role.ADMIN);
        return customers.getResults().stream()
            .filter(c -> Boolean.TRUE.equals(c.getIsCustomer()))
            .findFirst()
            .orElseThrow(() -> new SkipException(
                "No customer company found in demo environment; skipping test"))
            .getPk();
    }

    private int findNonCustomerPk() {
        PaginatedResponse<Company> companies = companyService.listCompanies(
            Map.of(StockTestData.QUERY_PARAM_IS_SUPPLIER, StockTestData.QUERY_VALUE_TRUE,
                StockTestData.QUERY_PARAM_LIMIT, StockTestData.DEFAULT_PAGE_LIMIT),
            Role.ADMIN);
        return companies.getResults().stream()
            .filter(c -> !Boolean.TRUE.equals(c.getIsCustomer()))
            .findFirst()
            .orElseThrow(() -> new SkipException(
                "No non-customer company found in demo environment; skipping test"))
            .getPk();
    }
}
