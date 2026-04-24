package com.inventree.testdata;

import com.inventree.model.PartTestTemplateRequest;

public final class PartTestTemplateTestData {

    public static final String RUN_ID = String.valueOf(System.currentTimeMillis() % 100000);

    public static final int DEFAULT_PAGE_LIMIT = 5;
    public static final int NON_EXISTENT_PART_PK = Integer.MAX_VALUE;

    public static final String QUERY_PARAM_LIMIT = "limit";
    public static final String QUERY_PARAM_PART = "part";

    public static final String ERROR_MSG_NOT_FOUND = "No PartTestTemplate matches the given query.";
    public static final String ERROR_MSG_INVALID_PART_CHOICE = "Select a valid choice. That choice is not one of the available choices.";
    public static final String EMPTY_BODY = "";

    public static final String DESCRIPTION_MINIMAL = "Minimal template description";
    public static final String DESCRIPTION_FULL = "Full field template description";
    public static final String DESCRIPTION_PATCHED = "PATCH partial update applied";
    public static final String CHOICES_SAMPLE = "pass,fail,skip";

    private PartTestTemplateTestData() {}

    public static String testTemplateName(String testCaseId, String suffix) {
        return "TC-APTT-" + testCaseId + "-" + suffix + "-" + RUN_ID;
    }

    public static PartTestTemplateRequest minimalRequest(int partPk, String testName) {
        return PartTestTemplateRequest.builder()
                .part(partPk)
                .testName(testName)
                .description(DESCRIPTION_MINIMAL)
                .build();
    }

    public static PartTestTemplateRequest fullRequest(int partPk, String testName, String description,
                                                       boolean required, boolean requiresValue,
                                                       boolean requiresAttachment, String choices) {
        return PartTestTemplateRequest.builder()
                .part(partPk)
                .testName(testName)
                .description(description)
                .required(required)
                .requiresValue(requiresValue)
                .requiresAttachment(requiresAttachment)
                .choices(choices)
                .build();
    }

    public static PartTestTemplateRequest fullUpdateRequest(int partPk, String testName, String description,
                                                             boolean enabled, boolean required) {
        return PartTestTemplateRequest.builder()
                .part(partPk)
                .testName(testName)
                .description(description)
                .enabled(enabled)
                .required(required)
                .build();
    }

    public static PartTestTemplateRequest patchDescriptionOnly(String description) {
        return PartTestTemplateRequest.builder()
                .description(description)
                .build();
    }
}
