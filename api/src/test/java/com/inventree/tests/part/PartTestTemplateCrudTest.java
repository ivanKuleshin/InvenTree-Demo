package com.inventree.tests.part;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartRequest;
import com.inventree.model.PartTestTemplate;
import com.inventree.model.PartTestTemplateRequest;
import com.inventree.testdata.PartTestData;
import com.inventree.testdata.PartTestTemplateTestData;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part Test Template CRUD")
public class PartTestTemplateCrudTest extends BaseTest {

    private final List<Integer> createdTemplateIds = new ArrayList<>();
    private final List<Integer> createdPartIds = new ArrayList<>();

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdTemplateIds.forEach(id -> {
            try {
                partTestTemplateService.deleteTemplate(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting part test template {}", id, t);
            }
        });
        createdTemplateIds.clear();

        createdPartIds.forEach(id -> {
            try {
                partService.patchPart(id, PartTestData.patchActiveOnly(false), Role.ADMIN);
                partService.deletePart(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting part {}", id, t);
            }
        });
        createdPartIds.clear();
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Create Part Test Template")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APTT_001_postPartTestTemplateCreatesTemplateWithRequiredFields() {
        int testablePartPk = createTestablePart("TC-APTT-001", "MinimalTemplate");

        String testName = PartTestTemplateTestData.testTemplateName("001", "Minimal");
        PartTestTemplateRequest request = PartTestTemplateTestData.minimalRequest(testablePartPk, testName);

        Response createResponse = partTestTemplateService.createTemplateRaw(request, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int templatePk = createResponse.jsonPath().getInt("pk");
        assertTrue(templatePk > 0, "pk must be a positive integer");
        createdTemplateIds.add(templatePk);

        assertEquals(createResponse.jsonPath().getString("test_name"), testName,
                "test_name must match the value supplied on creation");
        assertEquals(createResponse.jsonPath().getInt("part"), testablePartPk,
                "part must reference the testable part");
        assertNotNull(createResponse.jsonPath().getString("key"),
                "key must be auto-generated and non-null");
        assertFalse(createResponse.jsonPath().getString("key").isEmpty(),
                "key must be non-empty after auto-generation");
        assertTrue(createResponse.jsonPath().getBoolean("enabled"),
                "enabled must default to true when not supplied");
        assertEquals(createResponse.jsonPath().getString("choices"), "",
                "choices must return empty string when not configured");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Create Part Test Template")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_002_postPartTestTemplateCreatesTemplateWithAllOptionalFields() {
        int testablePartPk = createTestablePart("TC-APTT-002", "FullTemplate");

        String testName = PartTestTemplateTestData.testTemplateName("002", "Full");
        PartTestTemplateRequest request = PartTestTemplateTestData.fullRequest(
                testablePartPk, testName,
                PartTestTemplateTestData.DESCRIPTION_FULL,
                true, true, false,
                PartTestTemplateTestData.CHOICES_SAMPLE);

        Response createResponse = partTestTemplateService.createTemplateRaw(request, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int templatePk = createResponse.jsonPath().getInt("pk");
        assertTrue(templatePk > 0, "pk must be a positive integer");
        createdTemplateIds.add(templatePk);

        assertEquals(createResponse.jsonPath().getString("description"),
                PartTestTemplateTestData.DESCRIPTION_FULL,
                "description must match the value supplied on creation");
        assertTrue(createResponse.jsonPath().getBoolean("required"),
                "required must be true as supplied");
        assertTrue(createResponse.jsonPath().getBoolean("requires_value"),
                "requires_value must be true as supplied");
        assertFalse(createResponse.jsonPath().getBoolean("requires_attachment"),
                "requires_attachment must be false as supplied");
        assertEquals(createResponse.jsonPath().getString("choices"),
                PartTestTemplateTestData.CHOICES_SAMPLE,
                "choices must match the value supplied on creation");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Create Part Test Template")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_003_postPartTestTemplateKeyIsAutoGeneratedFromTestName() {
        int testablePartPk = createTestablePart("TC-APTT-003", "KeyDerivation");

        String testName = "TC-APTT-Create-Test-" + PartTestTemplateTestData.RUN_ID;
        String expectedKey = "tcapttcreatetest" + PartTestTemplateTestData.RUN_ID;
        PartTestTemplateRequest request = PartTestTemplateTestData.minimalRequest(testablePartPk, testName);

        Response createResponse = partTestTemplateService.createTemplateRaw(request, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int templatePk = createResponse.jsonPath().getInt("pk");
        createdTemplateIds.add(templatePk);

        assertEquals(createResponse.jsonPath().getString("key"), expectedKey,
                "key must strip hyphens and produce only lowercase alphanumeric characters");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Get Part Test Template by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APTT_004_getPartTestTemplateByIdReturnsSingleTemplate() {
        int testablePartPk = createTestablePart("TC-APTT-004", "GetById");

        String testName = PartTestTemplateTestData.testTemplateName("004", "GetById");
        PartTestTemplateRequest request = PartTestTemplateTestData.minimalRequest(testablePartPk, testName);
        PartTestTemplate created = partTestTemplateService.createTemplate(request, Role.ADMIN);
        createdTemplateIds.add(created.getPk());

        Response getResponse = partTestTemplateService.getTemplateByIdRaw(created.getPk(), Role.READER);
        ResponseValidator.assertStatusAndContentType(getResponse, HttpStatus.SC_OK);

        PartTestTemplate retrieved = ResponseValidator.deserialize(getResponse, PartTestTemplate.class);
        assertEquals(retrieved.getPk(), created.getPk(),
                "pk must match the created template");
        assertEquals(retrieved.getTestName(), testName,
                "test_name must match the created template");
        assertEquals(retrieved.getPart(), Integer.valueOf(testablePartPk),
                "part must match the testable part");
        assertNotNull(retrieved.getKey(), "key must be present");
        assertFalse(retrieved.getKey().isEmpty(), "key must be non-empty");
        assertNotNull(retrieved.getEnabled(), "enabled must be present");
        assertNotNull(retrieved.getRequired(), "required must be present");
        assertNotNull(retrieved.getRequiresValue(), "requires_value must be present");
        assertNotNull(retrieved.getRequiresAttachment(), "requires_attachment must be present");
        assertEquals(retrieved.getChoices(), "", "choices must return empty string when not configured");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("List Part Test Templates")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APTT_005_getPartTestTemplateListReturnsPaginatedResults() {
        int testablePartPk = createTestablePart("TC-APTT-005", "ListPagination");

        for (int i = 1; i <= 3; i++) {
            String testName = PartTestTemplateTestData.testTemplateName("005", "Item" + i);
            PartTestTemplate created = partTestTemplateService.createTemplate(
                    PartTestTemplateTestData.minimalRequest(testablePartPk, testName), Role.ADMIN);
            createdTemplateIds.add(created.getPk());
        }

        PaginatedResponse<PartTestTemplate> response = partTestTemplateService.listTemplates(
                Map.of(PartTestTemplateTestData.QUERY_PARAM_LIMIT, PartTestTemplateTestData.DEFAULT_PAGE_LIMIT,
                        PartTestTemplateTestData.QUERY_PARAM_PART, testablePartPk),
                Role.READER);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() >= 3, "count must be at least 3 after creating 3 templates");
        assertNotNull(response.getResults(), "results must not be null");
        assertFalse(response.getResults().isEmpty(), "results must be non-empty");

        PartTestTemplate first = response.getResults().getFirst();
        assertNotNull(first.getPk(), "pk must not be null");
        assertNotNull(first.getTestName(), "test_name must not be null");
        assertNotNull(first.getKey(), "key must not be null");
        assertNotNull(first.getEnabled(), "enabled must not be null");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("List Part Test Templates")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_006_getPartTestTemplateListFiltersByPart() {
        int testablePartPk = createTestablePart("TC-APTT-006", "FilterByPart");

        String testName = PartTestTemplateTestData.testTemplateName("006", "FilterTarget");
        PartTestTemplate created = partTestTemplateService.createTemplate(
                PartTestTemplateTestData.minimalRequest(testablePartPk, testName), Role.ADMIN);
        createdTemplateIds.add(created.getPk());

        PaginatedResponse<PartTestTemplate> filtered = partTestTemplateService.listTemplates(
                Map.of(PartTestTemplateTestData.QUERY_PARAM_LIMIT, PartTestTemplateTestData.DEFAULT_PAGE_LIMIT,
                        PartTestTemplateTestData.QUERY_PARAM_PART, testablePartPk),
                Role.READER);

        assertNotNull(filtered.getResults(), "results must not be null");
        assertFalse(filtered.getResults().isEmpty(), "results must be non-empty for existing part");
        filtered.getResults().forEach(t ->
                assertEquals(t.getPart(), Integer.valueOf(testablePartPk),
                        "every returned template must belong to the filtered part"));
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("List Part Test Templates")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_007_getPartTestTemplateListWithInvalidPartReturns400() {
        Response response = partTestTemplateService.listTemplatesRaw(
                Map.of(PartTestTemplateTestData.QUERY_PARAM_LIMIT, PartTestTemplateTestData.DEFAULT_PAGE_LIMIT,
                        PartTestTemplateTestData.QUERY_PARAM_PART, PartTestTemplateTestData.NON_EXISTENT_PART_PK),
                Role.READER);

        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_BAD_REQUEST);
        assertEquals(response.jsonPath().getList("part").getFirst(),
                PartTestTemplateTestData.ERROR_MSG_INVALID_PART_CHOICE,
                "part error must indicate the value is not a valid choice");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Update Part Test Template")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_008_putPartTestTemplateReplacesAllWritableFields() {
        int testablePartPk = createTestablePart("TC-APTT-008", "PutUpdate");

        String setupName = PartTestTemplateTestData.testTemplateName("008", "Setup");
        PartTestTemplate created = partTestTemplateService.createTemplate(
                PartTestTemplateTestData.minimalRequest(testablePartPk, setupName), Role.ADMIN);
        createdTemplateIds.add(created.getPk());

        String updatedName = PartTestTemplateTestData.testTemplateName("008", "Updated");
        PartTestTemplateRequest updateRequest = PartTestTemplateTestData.fullUpdateRequest(
                testablePartPk, updatedName,
                PartTestTemplateTestData.DESCRIPTION_FULL,
                false, true);

        PartTestTemplate updated = partTestTemplateService.updateTemplate(created.getPk(), updateRequest, Role.ADMIN);

        assertEquals(updated.getPk(), created.getPk(),
                "pk must be unchanged after PUT");
        assertEquals(updated.getTestName(), updatedName,
                "test_name must reflect the PUT value");
        assertEquals(updated.getDescription(), PartTestTemplateTestData.DESCRIPTION_FULL,
                "description must reflect the PUT value");
        assertEquals(updated.getEnabled(), Boolean.FALSE,
                "enabled must reflect the PUT value");
        assertEquals(updated.getRequired(), Boolean.TRUE,
                "required must reflect the PUT value");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Partial Update Part Test Template")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_009_patchPartTestTemplateUpdatesOnlySuppliedFields() {
        int testablePartPk = createTestablePart("TC-APTT-009", "PatchUpdate");

        String setupName = PartTestTemplateTestData.testTemplateName("009", "Setup");
        PartTestTemplateRequest setupRequest = PartTestTemplateTestData.fullRequest(
                testablePartPk, setupName,
                PartTestTemplateTestData.DESCRIPTION_MINIMAL,
                false, false, false, "");

        PartTestTemplate created = partTestTemplateService.createTemplate(setupRequest, Role.ADMIN);
        createdTemplateIds.add(created.getPk());

        PartTestTemplateRequest patchRequest = PartTestTemplateTestData.patchDescriptionOnly(
                PartTestTemplateTestData.DESCRIPTION_PATCHED);

        PartTestTemplate patched = partTestTemplateService.patchTemplate(created.getPk(), patchRequest, Role.ADMIN);

        assertEquals(patched.getPk(), created.getPk(),
                "pk must be unchanged after PATCH");
        assertEquals(patched.getDescription(), PartTestTemplateTestData.DESCRIPTION_PATCHED,
                "description must reflect the PATCH value");
        assertEquals(patched.getTestName(), setupName,
                "test_name must be unchanged after PATCH");
        assertEquals(patched.getPart(), Integer.valueOf(testablePartPk),
                "part must be unchanged after PATCH");
        assertEquals(patched.getEnabled(), Boolean.TRUE,
                "enabled must be unchanged after PATCH (defaulted to true on creation)");
    }

    @Test(groups = {"regression", "parts", "test-templates"})
    @Story("Delete Part Test Template")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APTT_010_deletePartTestTemplateRemovesTemplate() {
        int testablePartPk = createTestablePart("TC-APTT-010", "DeleteTemplate");

        String testName = PartTestTemplateTestData.testTemplateName("010", "ToDelete");
        PartTestTemplate created = partTestTemplateService.createTemplate(
                PartTestTemplateTestData.minimalRequest(testablePartPk, testName), Role.ADMIN);
        createdTemplateIds.add(created.getPk());

        Response deleteResponse = partTestTemplateService.deleteTemplateRaw(created.getPk(), Role.ADMIN);
        ResponseValidator.assertStatus(deleteResponse, HttpStatus.SC_NO_CONTENT);
        assertEquals(deleteResponse.body().asString(), PartTestTemplateTestData.EMPTY_BODY,
                "DELETE response body must be empty");

        Response getResponse = partTestTemplateService.getTemplateByIdRaw(created.getPk(), Role.READER);
        ResponseValidator.assertStatusAndContentType(getResponse, HttpStatus.SC_NOT_FOUND);
        assertEquals(getResponse.jsonPath().getString("detail"),
                PartTestTemplateTestData.ERROR_MSG_NOT_FOUND,
                "detail must indicate no matching template was found");
    }

    private int createTestablePart(String testCaseId, String suffix) {
        String partName = PartTestData.testPartName(testCaseId, suffix);
        PartRequest request = PartRequest.builder()
                .name(partName)
                .testable(true)
                .build();
        Part created = partService.createPart(request, Role.ADMIN);
        createdPartIds.add(created.getPk());
        return created.getPk();
    }
}
