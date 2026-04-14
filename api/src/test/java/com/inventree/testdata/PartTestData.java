package com.inventree.testdata;

import com.inventree.model.PartRequest;

public final class PartTestData {

    public static final String RUN_ID = String.valueOf(System.currentTimeMillis() % 100000);

    public static final int KNOWN_PART_PK = 82;
    public static final String KNOWN_PART_NAME = "1551ABK";
    public static final boolean KNOWN_PART_ACTIVE = true;
    public static final boolean KNOWN_PART_COMPONENT = true;
    public static final boolean KNOWN_PART_PURCHASEABLE = true;
    public static final boolean KNOWN_PART_ASSEMBLY = false;

    public static final int DEFAULT_PAGE_LIMIT = 5;
    public static final int DEFAULT_CATEGORY_PK = 17;

    public static final String QUERY_VALUE_TRUE = "true";
    public static final String QUERY_PARAM_PARAMETERS = "parameters";
    public static final String QUERY_PARAM_TAGS = "tags";
    public static final String QUERY_PARAM_CATEGORY_DETAIL = "category_detail";
    public static final String QUERY_PARAM_PATH_DETAIL = "path_detail";
    public static final String QUERY_PARAM_PART = "part";
    public static final String QUERY_PARAM_IS_SUPPLIER = "is_supplier";
    public static final String QUERY_PARAM_LIMIT = "limit";

    public static final String ERROR_MSG_NOT_FOUND = "No Part matches the given query.";
    public static final String EMPTY_BODY = "";

    public static final String URI_PREFIX = "http";
    public static final String PUT_UPDATE_IPN = "IPN-APCRUD-004";
    public static final String FIELD_PK = "pk";

    public static final String PUT_UPDATE_DESCRIPTION = "PUT full update applied";
    public static final String PUT_UPDATE_KEYWORDS = "put update test";
    public static final String PATCH_UPDATE_KEYWORDS = "patch-test qa";
    public static final String ORIGINAL_KEYWORDS = "original";

    public static final String DUPLICATE_PART_NAME_SUFFIX = "DuplicatePart";
    public static final String INITIAL_STOCK_PART_NAME_SUFFIX = "InitialStockPart";
    public static final String INITIAL_SUPPLIER_PART_NAME_SUFFIX = "InitialSupplierPart";

    public static final int INITIAL_STOCK_QUANTITY = 50;
    public static final int STOCK_STATUS_OK = 10;
    public static final String SUPPLIER_SKU = "SKU-TC-APCRUD-009-" + RUN_ID;

    public static final String FIELD_PARAMETERS = "parameters";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_CATEGORY_DETAIL = "category_detail";
    public static final String FIELD_CATEGORY_PATH = "category_path";
    public static final String FIELD_DUPLICATE = "duplicate";
    public static final String FIELD_INITIAL_STOCK = "initial_stock";
    public static final String FIELD_INITIAL_SUPPLIER = "initial_supplier";
    public static final String FIELD_FULL_NAME = "full_name";
    public static final String FIELD_NEXT = "next";
    public static final String FIELD_PREVIOUS = "previous";
    public static final String FIELD_COUNT = "count";
    public static final String FIELD_RESULTS = "results";
    public static final String FIELD_DETAIL = "detail";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PART = "part";
    public static final String FIELD_COPY_IMAGE = "copy_image";
    public static final String FIELD_COPY_NOTES = "copy_notes";
    public static final String FIELD_COPY_PARAMETERS = "copy_parameters";
    public static final String FIELD_COPY_BOM = "copy_bom";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_SUPPLIER = "supplier";
    public static final String FIELD_SKU = "SKU";
    public static final String FIELD_SKU_INPUT = "sku";
    public static final String FIELD_PURCHASEABLE = "purchaseable";

    private PartTestData() {}

    public static String testPartName(String testCaseId, String suffix) {
        return testCaseId + "-" + suffix + "-" + RUN_ID;
    }

    public static PartRequest minimalPart(String name) {
        return PartRequest.builder()
                .name(name)
                .build();
    }

    public static PartRequest minimalPartWithCategory(String name, int categoryPk) {
        return PartRequest.builder()
                .name(name)
                .category(categoryPk)
                .build();
    }

    public static PartRequest fullUpdateRequest(String name, String description, String ipn,
                                                 boolean assembly, boolean component,
                                                 boolean purchaseable, boolean salable,
                                                 boolean isTemplate, boolean trackable,
                                                 boolean virtual, boolean active,
                                                 String keywords, int categoryPk) {
        return PartRequest.builder()
                .name(name)
                .description(description)
                .ipn(ipn)
                .active(active)
                .assembly(assembly)
                .component(component)
                .purchaseable(purchaseable)
                .salable(salable)
                .isTemplate(isTemplate)
                .trackable(trackable)
                .virtual(virtual)
                .keywords(keywords)
                .category(categoryPk)
                .build();
    }

    public static PartRequest patchActiveAndKeywords(boolean active, String keywords) {
        return PartRequest.builder()
                .active(active)
                .keywords(keywords)
                .build();
    }

    public static PartRequest patchActiveOnly(boolean active) {
        return PartRequest.builder()
                .active(active)
                .build();
    }

    public static PartRequest purchaseablePart(String name) {
        return PartRequest.builder()
                .name(name)
                .purchaseable(true)
                .build();
    }

    public static PartRequest partWithKeywords(String name, String keywords) {
        return PartRequest.builder()
                .name(name)
                .active(true)
                .keywords(keywords)
                .build();
    }
}
