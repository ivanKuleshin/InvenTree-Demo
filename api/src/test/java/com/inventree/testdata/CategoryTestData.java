package com.inventree.testdata;

import com.inventree.model.PartCategoryRequest;

public final class CategoryTestData {

    public static final int ELECTRONICS_PK = 1;
    public static final String ELECTRONICS_NAME = "Electronics";

    public static final int DEFAULT_PAGE_LIMIT = 10;
    public static final String QUERY_PARAM_PATH_DETAIL = "path_detail";
    public static final String QUERY_VALUE_TRUE = "true";
    public static final String ERROR_MSG_NOT_FOUND = "No PartCategory matches the given query.";
    public static final String EMPTY_BODY = "";

    private CategoryTestData() {}

    public static PartCategoryRequest minimalTopLevel(String name) {
        return PartCategoryRequest.builder()
                .name(name)
                .build();
    }

    public static PartCategoryRequest withParentAndDescription(String name, int parentPk, String description) {
        return PartCategoryRequest.builder()
                .name(name)
                .parent(parentPk)
                .description(description)
                .build();
    }

    public static PartCategoryRequest fullUpdate(String name, String description,
                                                  boolean structural, String defaultKeywords,
                                                  Integer parent) {
        return PartCategoryRequest.builder()
                .name(name)
                .description(description)
                .structural(structural)
                .defaultKeywords(defaultKeywords)
                .parent(parent)
                .build();
    }

    public static PartCategoryRequest descriptionOnly(String description) {
        return PartCategoryRequest.builder()
                .description(description)
                .build();
    }

    public static String testCategoryName(String testCaseId, String description) {
        return testCaseId + "-" + description;
    }
}
