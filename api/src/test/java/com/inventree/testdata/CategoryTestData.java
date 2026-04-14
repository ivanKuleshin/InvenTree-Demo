package com.inventree.testdata;

import com.inventree.model.PartCategoryRequest;

public final class CategoryTestData {

    public static final int ELECTRONICS_PK = 1;
    public static final String ELECTRONICS_NAME = "Electronics";

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
}
