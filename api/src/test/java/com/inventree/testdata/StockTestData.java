package com.inventree.testdata;

import com.inventree.model.StockItemRequest;
import com.inventree.model.StockLocationRequest;
import com.inventree.model.StockLocationTypeRequest;

import java.util.List;
import java.util.Map;

public final class StockTestData {

    public static final String RUN_ID = String.valueOf(System.currentTimeMillis() % 100000);

    public static final int DEFAULT_PAGE_LIMIT = 5;
    public static final int NONEXISTENT_PK = 999999999;

    public static final int STOCK_STATUS_OK = 10;
    public static final int STOCK_STATUS_ATTENTION = 50;
    public static final int STOCK_STATUS_DAMAGED = 55;
    public static final int STOCK_STATUS_INVALID = 999;

    public static final double ADD_QUANTITY = 10.0;
    public static final double REMOVE_QUANTITY = 5.0;
    public static final double COUNT_QUANTITY = 47.0;

    public static final String EMPTY_BODY = "";
    public static final String ERROR_MSG_NOT_FOUND_STOCK_ITEM = "No StockItem matches the given query.";
    public static final String ERROR_MSG_NOT_FOUND_LOCATION = "No StockLocation matches the given query.";
    public static final String ERROR_MSG_NOT_FOUND_LOCATION_TYPE = "No StockLocationType matches the given query.";

    public static final String QUERY_PARAM_PART = "part";
    public static final String QUERY_PARAM_LOCATION = "location";
    public static final String QUERY_PARAM_STATUS = "status";
    public static final String QUERY_PARAM_SERIALIZED = "serialized";
    public static final String QUERY_PARAM_TOP_LEVEL = "top_level";
    public static final String QUERY_PARAM_STRUCTURAL = "structural";
    public static final String QUERY_PARAM_EXTERNAL = "external";
    public static final String QUERY_PARAM_SEARCH = "search";
    public static final String QUERY_PARAM_LIMIT = "limit";
    public static final String QUERY_PARAM_ORDERING = "ordering";
    public static final String QUERY_PARAM_IS_CUSTOMER = "is_customer";
    public static final String QUERY_PARAM_IS_SUPPLIER = "is_supplier";

    public static final String QUERY_VALUE_TRUE = "true";
    public static final String QUERY_VALUE_FALSE = "false";
    public static final String QUERY_VALUE_NULL = "null";
    public static final String QUERY_VALUE_ORDERING_DATE_DESC = "-date";

    public static final String LOCATION_SEARCH_TERM = "Shelf";
    public static final String ICON_BOX_OUTLINE = "ti:box:outline";
    public static final String ICON_BOOKMARK_FILLED = "ti:bookmark:filled";
    public static final String ICON_ARCHIVE_OUTLINE = "ti:archive:outline";

    public static final String LOC_TYPE_NAME_101_CHARS =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB";

    private StockTestData() {}

    public static String testStockItemName(String testCaseId, String suffix) {
        return testCaseId + "-" + suffix + "-" + RUN_ID;
    }

    public static String testLocationName(String testCaseId, String suffix) {
        return testCaseId + "-Loc-" + suffix + "-" + RUN_ID;
    }

    public static String testLocationTypeName(String testCaseId, String suffix) {
        return testCaseId + "-Type-" + suffix + "-" + RUN_ID;
    }

    public static StockItemRequest minimalStockItem(int partPk, double quantity) {
        return StockItemRequest.builder()
                .part(partPk)
                .quantity(quantity)
                .build();
    }

    public static StockItemRequest stockItemWithLocation(int partPk, double quantity, int locationPk) {
        return StockItemRequest.builder()
                .part(partPk)
                .quantity(quantity)
                .location(locationPk)
                .build();
    }

    public static StockItemRequest stockItemWithBatch(int partPk, double quantity, String batch,
                                                       String notes, boolean deleteOnDeplete) {
        return StockItemRequest.builder()
                .part(partPk)
                .quantity(quantity)
                .batch(batch)
                .notes(notes)
                .deleteOnDeplete(deleteOnDeplete)
                .build();
    }

    public static StockItemRequest stockItemWithSerialNumbers(int partPk, int locationPk,
                                                               double quantity, String serialNumbers) {
        return StockItemRequest.builder()
                .part(partPk)
                .quantity(quantity)
                .location(locationPk)
                .serialNumbers(serialNumbers)
                .build();
    }

    public static StockItemRequest fullStockItemUpdate(int partPk, int locationPk, double quantity,
                                                        int status, String batch, String notes,
                                                        boolean deleteOnDeplete, String packaging) {
        return StockItemRequest.builder()
                .part(partPk)
                .quantity(quantity)
                .location(locationPk)
                .status(status)
                .batch(batch)
                .notes(notes)
                .deleteOnDeplete(deleteOnDeplete)
                .packaging(packaging)
                .build();
    }

    public static StockLocationRequest minimalLocation(String name) {
        return StockLocationRequest.builder()
                .name(name)
                .build();
    }

    public static StockLocationRequest locationWithParent(String name, int parentPk, String description) {
        return StockLocationRequest.builder()
                .name(name)
                .parent(parentPk)
                .description(description)
                .build();
    }

    public static StockLocationRequest structuralLocation(String name) {
        return StockLocationRequest.builder()
                .name(name)
                .structural(true)
                .description("Organizational node only")
                .build();
    }

    public static StockLocationRequest externalLocation(String name) {
        return StockLocationRequest.builder()
                .name(name)
                .external(true)
                .build();
    }

    public static StockLocationRequest locationWithType(String name, int typeId) {
        return StockLocationRequest.builder()
                .name(name)
                .locationType(typeId)
                .build();
    }

    public static StockLocationTypeRequest minimalLocationType(String name) {
        return StockLocationTypeRequest.builder()
                .name(name)
                .build();
    }

    public static StockLocationTypeRequest fullLocationType(String name, String description, String icon) {
        return StockLocationTypeRequest.builder()
                .name(name)
                .description(description)
                .icon(icon)
                .build();
    }

    public static Map<String, Object> addPayload(int itemPk, double quantity, String notes) {
        return Map.of(
                "items", List.of(Map.of("pk", itemPk, "quantity", String.format("%.3f", quantity))),
                "notes", notes);
    }

    public static Map<String, Object> removePayload(int itemPk, double quantity, String notes) {
        return Map.of(
                "items", List.of(Map.of("pk", itemPk, "quantity", String.format("%.3f", quantity))),
                "notes", notes);
    }

    public static Map<String, Object> countPayload(int itemPk, double quantity, String notes) {
        return Map.of(
                "items", List.of(Map.of("pk", itemPk, "quantity", String.format("%.3f", quantity))),
                "notes", notes);
    }

    public static Map<String, Object> transferPayload(int itemPk, double quantity,
                                                       int destLocationPk, String notes) {
        return Map.of(
                "items", List.of(Map.of("pk", itemPk, "quantity", String.format("%.3f", quantity))),
                "location", destLocationPk,
                "notes", notes);
    }

    public static Map<String, Object> changeStatusPayload(List<Integer> itemPks, int status, String note) {
        return Map.of(
                "items", itemPks,
                "status", status,
                "note", note);
    }

    public static Map<String, Object> assignPayload(int itemPk, int customerPk, String notes) {
        return Map.of(
                "items", List.of(Map.of("item", itemPk)),
                "customer", customerPk,
                "notes", notes);
    }

    public static Map<String, Object> mergePayload(int itemPk1, int itemPk2, int locationPk,
                                                    boolean allowMismatchedSuppliers,
                                                    boolean allowMismatchedStatus, String notes) {
        return Map.of(
                "items", List.of(Map.of("item", itemPk1), Map.of("item", itemPk2)),
                "location", locationPk,
                "allow_mismatched_suppliers", allowMismatchedSuppliers,
                "allow_mismatched_status", allowMismatchedStatus,
                "notes", notes);
    }

    public static Map<String, Object> returnPayload(int itemPk, double quantity,
                                                     int locationPk, boolean merge, String notes) {
        return Map.of(
                "items", List.of(Map.of("pk", itemPk, "quantity", String.format("%.3f", quantity))),
                "location", locationPk,
                "merge", merge,
                "notes", notes);
    }
}
