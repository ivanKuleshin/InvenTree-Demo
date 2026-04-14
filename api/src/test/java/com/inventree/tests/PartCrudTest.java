package com.inventree.tests;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.client.SpecBuilder;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartRequest;
import com.inventree.testdata.PartTestData;
import com.inventree.util.ApiConstants;
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

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part CRUD")
public class PartCrudTest extends BaseTest {

    private final List<Integer> createdPartIds = new ArrayList<>();

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdPartIds.forEach(id -> {
            try {
                List<Map<String, Object>> stockItems = given()
                    .spec(SpecBuilder.build(Role.ADMIN))
                    .queryParam(PartTestData.QUERY_PARAM_PART, id)
                    .get(ApiConstants.STOCK_ENDPOINT)
                    .jsonPath().getList("$");
                if (stockItems != null) {
                    stockItems.forEach(item ->
                        given().spec(SpecBuilder.build(Role.ADMIN))
                            .delete(ApiConstants.STOCK_ENDPOINT + item.get("pk") + "/"));
                }
            } catch (Throwable t) {
                log.error("Error while deleting stock items for part {}", id, t);
            }
            try {
                List<Map<String, Object>> supplierParts = given()
                    .spec(SpecBuilder.build(Role.ADMIN))
                    .queryParam(PartTestData.QUERY_PARAM_PART, id)
                    .get(ApiConstants.SUPPLIER_PART_ENDPOINT)
                    .jsonPath().getList("$");
                if (supplierParts != null) {
                    supplierParts.forEach(sp ->
                        given().spec(SpecBuilder.build(Role.ADMIN))
                            .delete(ApiConstants.SUPPLIER_PART_ENDPOINT + sp.get("pk") + "/"));
                }
            } catch (Throwable t) {
                log.error("Error while deleting supplier parts for part {}", id, t);
            }
            try {
                partService.patchPart(id, PartTestData.patchActiveOnly(false), Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deactivating part for cleanup", t);
            }
            try {
                partService.deletePart(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting part", t);
            }
        });
        createdPartIds.clear();
    }

    @Test(groups = {"regression", "parts"})
    @Story("List Parts")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APCRUD_001_getPartListReturnsPaginatedResultWithFullFieldSet() {
        PaginatedResponse<Part> response = partService.listParts(
            Map.of(PartTestData.QUERY_PARAM_LIMIT, PartTestData.DEFAULT_PAGE_LIMIT), Role.READER);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() > 0, "count must be greater than 0");
        assertNotNull(response.getResults(), "results must not be null");
        assertEquals(response.getResults().size(), PartTestData.DEFAULT_PAGE_LIMIT,
            "results must contain exactly " + PartTestData.DEFAULT_PAGE_LIMIT + " items");
        assertNull(response.getPrevious(), "previous must be null on first page");
        assertNotNull(response.getNext(), "next must be non-null when more pages exist");
        assertTrue(response.getNext().startsWith("http"), "next must be a URI string");

        Part first = response.getResults().getFirst();
        assertNotNull(first.getPk(), "pk must not be null");
        assertNotNull(first.getName(), "name must not be null");
        assertNotNull(first.getActive(), "active must not be null");
        assertNotNull(first.getAssembly(), "assembly must not be null");
        assertNotNull(first.getComponent(), "component must not be null");
        assertNotNull(first.getIsTemplate(), "is_template must not be null");
        assertNotNull(first.getTrackable(), "trackable must not be null");
        assertNotNull(first.getVirtual(), "virtual must not be null");
        assertNotNull(first.getPurchaseable(), "purchaseable must not be null");
        assertNotNull(first.getSalable(), "salable must not be null");
        assertNotNull(first.getLocked(), "locked must not be null");
        assertNotNull(first.getFullName(), "full_name must not be null");

        Part noIpnNoRevision = response.getResults().stream()
            .filter(p -> (p.getIpn() == null || p.getIpn().isEmpty())
                && (p.getRevision() == null || p.getRevision().isEmpty()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("No part with empty IPN and revision found in results"));
        assertEquals(noIpnNoRevision.getFullName(), noIpnNoRevision.getName(),
            "full_name must equal name when IPN and revision are both empty");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Get Part by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APCRUD_002_getPartByIdReturnsSinglePartWithCorrectFields() {
        Response rawResponse = partService.getPartByIdRaw(PartTestData.KNOWN_PART_PK, Role.READER);
        ResponseValidator.assertStatusAndContentType(rawResponse, HttpStatus.SC_OK);

        Part part = ResponseValidator.deserialize(rawResponse, Part.class);
        assertEquals(part.getPk(), Integer.valueOf(PartTestData.KNOWN_PART_PK));
        assertEquals(part.getName(), PartTestData.KNOWN_PART_NAME);
        assertEquals(part.getActive(), PartTestData.KNOWN_PART_ACTIVE);
        assertEquals(part.getComponent(), PartTestData.KNOWN_PART_COMPONENT);
        assertEquals(part.getPurchaseable(), PartTestData.KNOWN_PART_PURCHASEABLE);
        assertEquals(part.getAssembly(), PartTestData.KNOWN_PART_ASSEMBLY);
        assertNull(part.getCategory(), "category must be null for this part");
        assertNotNull(part.getInStock(), "in_stock must not be null");
        assertTrue(part.getInStock() >= 0, "in_stock must be >= 0");

        assertNull(rawResponse.jsonPath().get(PartTestData.QUERY_PARAM_PARAMETERS),
            "parameters must be absent without ?parameters=true");
        assertNull(rawResponse.jsonPath().get(PartTestData.QUERY_PARAM_TAGS),
            "tags must be absent without ?tags=true");
        assertNull(rawResponse.jsonPath().get(PartTestData.QUERY_PARAM_CATEGORY_DETAIL),
            "category_detail must be absent without ?category_detail=true");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Get Part by ID with Query Flags")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_003_getPartByIdWithQueryFlagsReturnsExpandedFields() {
        Response response = partService.getPartByIdRaw(PartTestData.KNOWN_PART_PK, Role.READER,
            Map.of(PartTestData.QUERY_PARAM_PARAMETERS, PartTestData.QUERY_VALUE_TRUE,
                PartTestData.QUERY_PARAM_TAGS, PartTestData.QUERY_VALUE_TRUE,
                PartTestData.QUERY_PARAM_CATEGORY_DETAIL, PartTestData.QUERY_VALUE_TRUE));

        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);

        assertEquals(response.jsonPath().getInt("pk"), PartTestData.KNOWN_PART_PK);

        List<Map<String, Object>> parameters = response.jsonPath().getList(PartTestData.FIELD_PARAMETERS);
        assertNotNull(parameters, "parameters must be present with ?parameters=true");
        assertFalse(parameters.isEmpty(), "parameters must be non-empty");

        Map<String, Object> templateDetail = response.jsonPath().getMap(
            PartTestData.FIELD_PARAMETERS + "[0].template_detail");
        assertNotNull(response.jsonPath().get(PartTestData.FIELD_PARAMETERS + "[0].pk"),
            "parameter pk must not be null");
        assertNotNull(response.jsonPath().get(PartTestData.FIELD_PARAMETERS + "[0].template"),
            "parameter template must not be null");
        assertNotNull(response.jsonPath().getString(PartTestData.FIELD_PARAMETERS + "[0].data"),
            "parameter data must not be null");
        assertNotNull(templateDetail, "parameter template_detail must not be null");
        assertNotNull(templateDetail.get("pk"), "template_detail pk must not be null");
        assertNotNull(templateDetail.get(PartTestData.FIELD_NAME), "template_detail name must not be null");
        assertNotNull(templateDetail.get("units"), "template_detail units must not be null");
        assertNotNull(templateDetail.get("description"), "template_detail description must not be null");

        assertNotNull(response.jsonPath().getList(PartTestData.FIELD_TAGS),
            "tags must be present with ?tags=true");
        assertNull(response.jsonPath().get(PartTestData.FIELD_CATEGORY_DETAIL),
            "category_detail must be null when part has no category");

        PaginatedResponse<Part> listing = partService.listParts(
            Map.of(PartTestData.QUERY_PARAM_LIMIT, PartTestData.DEFAULT_PAGE_LIMIT), Role.READER);
        int categorizedPartPk = listing.getResults().stream()
            .filter(p -> p.getCategory() != null)
            .findFirst()
            .orElseThrow(() -> new AssertionError("No categorized part found in first-page results"))
            .getPk();

        Response pathDetailResponse = partService.getPartByIdRaw(categorizedPartPk, Role.READER,
            Map.of(PartTestData.QUERY_PARAM_PATH_DETAIL, PartTestData.QUERY_VALUE_TRUE));

        ResponseValidator.assertStatusAndContentType(pathDetailResponse, HttpStatus.SC_OK);
        List<Map<String, Object>> categoryPath = pathDetailResponse.jsonPath().getList(PartTestData.FIELD_CATEGORY_PATH);
        assertNotNull(categoryPath, "category_path must be present with ?path_detail=true for a categorized part");
        assertFalse(categoryPath.isEmpty(), "category_path must be non-empty for a categorized part");
        assertNotNull(categoryPath.getFirst().get("pk"), "category_path entry must have pk");
        assertNotNull(categoryPath.getFirst().get("name"), "category_path entry must have name");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Update Part")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_004_putPartReplacesAllWritableFields() {
        String setupName = PartTestData.testPartName("TC-APCRUD-004", "Setup");
        PartRequest setupRequest = PartTestData.minimalPartWithCategory(
            setupName, PartTestData.DEFAULT_CATEGORY_PK);
        Part created = partService.createPart(setupRequest, Role.ADMIN);
        int partPk = created.getPk();
        createdPartIds.add(partPk);

        String updatedName = PartTestData.testPartName("TC-APCRUD-004", "UpdatedName");
        PartRequest updateRequest = PartTestData.fullUpdateRequest(
            updatedName,
            PartTestData.PUT_UPDATE_DESCRIPTION,
            "IPN-APCRUD-004",
            true,
            true,
            true,
            false,
            false,
            false,
            false,
            true,
            PartTestData.PUT_UPDATE_KEYWORDS,
            PartTestData.DEFAULT_CATEGORY_PK);

        Part updated = partService.updatePart(partPk, updateRequest, Role.ADMIN);

        assertEquals(updated.getPk(), Integer.valueOf(partPk));
        assertEquals(updated.getName(), updatedName);
        assertEquals(updated.getDescription(), PartTestData.PUT_UPDATE_DESCRIPTION);
        assertEquals(updated.getActive(), Boolean.TRUE);
        assertEquals(updated.getAssembly(), Boolean.TRUE);
        assertEquals(updated.getKeywords(), PartTestData.PUT_UPDATE_KEYWORDS);
    }

    @Test(groups = {"regression", "parts"})
    @Story("Partial Update Part")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_005_patchPartPartiallyUpdatesSelectedFields() {
        String setupName = PartTestData.testPartName("TC-APCRUD-005", "Setup");
        PartRequest setupRequest = PartRequest.builder()
            .name(setupName)
            .active(true)
            .keywords(PartTestData.ORIGINAL_KEYWORDS)
            .build();
        Part created = partService.createPart(setupRequest, Role.ADMIN);
        int partPk = created.getPk();
        createdPartIds.add(partPk);

        PartRequest patchRequest = PartTestData.patchActiveAndKeywords(false, PartTestData.PATCH_UPDATE_KEYWORDS);
        Part patched = partService.patchPart(partPk, patchRequest, Role.ADMIN);

        assertEquals(patched.getPk(), Integer.valueOf(partPk));
        assertEquals(patched.getActive(), Boolean.FALSE);
        assertEquals(patched.getKeywords(), PartTestData.PATCH_UPDATE_KEYWORDS);
        assertEquals(patched.getName(), setupName, "name must be unchanged after PATCH");
        assertEquals(patched.getComponent(), Boolean.TRUE, "component must be unchanged after PATCH");
        assertEquals(patched.getPurchaseable(), Boolean.TRUE, "purchaseable must be unchanged after PATCH");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Delete Part")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_006_deletePartRemovesInactivePart() {
        String setupName = PartTestData.testPartName("TC-APCRUD-006", "ToDelete");
        Part created = partService.createPart(PartTestData.minimalPart(setupName), Role.ADMIN);
        int partPk = created.getPk();
        createdPartIds.add(partPk);

        partService.patchPart(partPk, PartTestData.patchActiveOnly(false), Role.ADMIN);

        Response deleteResponse = partService.deletePartRaw(partPk, Role.ADMIN);
        ResponseValidator.assertStatus(deleteResponse, HttpStatus.SC_NO_CONTENT);
        assertEquals(deleteResponse.body().asString(), PartTestData.EMPTY_BODY,
            "DELETE response body must be empty");

        Response getResponse = partService.getPartByIdRaw(partPk, Role.READER);
        ResponseValidator.assertStatusAndContentType(getResponse, HttpStatus.SC_NOT_FOUND);
        assertEquals(getResponse.jsonPath().getString(PartTestData.FIELD_DETAIL),
            PartTestData.ERROR_MSG_NOT_FOUND);
    }

    @Test(groups = {"regression", "parts"})
    @Story("Create Part via Duplicate")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_007_postPartWithDuplicateFieldCreatesIndependentPart() {
        PaginatedResponse<Part> listing = partService.listParts(
            Map.of(PartTestData.QUERY_PARAM_LIMIT, PartTestData.DEFAULT_PAGE_LIMIT), Role.READER);
        int sourcePk = listing.getResults().stream()
            .filter(p -> p.getCategory() != null)
            .findFirst()
            .orElseThrow(() -> new AssertionError("No categorized part found in first-page results"))
            .getPk();

        String newName = PartTestData.testPartName("TC-APCRUD-007", PartTestData.DUPLICATE_PART_NAME_SUFFIX);
        Map<String, Object> duplicatePayload = Map.of(
            PartTestData.FIELD_NAME, newName,
            PartTestData.FIELD_DUPLICATE, Map.of(
                PartTestData.FIELD_PART, sourcePk,
                PartTestData.FIELD_COPY_IMAGE, false,
                PartTestData.FIELD_COPY_NOTES, false,
                PartTestData.FIELD_COPY_PARAMETERS, false,
                PartTestData.FIELD_COPY_BOM, false));

        Response createResponse = partService.createPartRaw(duplicatePayload, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int newPartPk = createResponse.jsonPath().getInt("pk");
        createdPartIds.add(newPartPk);

        assertEquals(createResponse.jsonPath().getString("name"), newName);
        assertNull(createResponse.jsonPath().get(PartTestData.FIELD_DUPLICATE),
            "duplicate must be absent from response (write-only)");
        assertNull(createResponse.jsonPath().get("category"),
            "category must be null — not inherited from source part");

        Response getResponse = partService.getPartByIdRaw(newPartPk, Role.READER);
        ResponseValidator.assertStatusAndContentType(getResponse, HttpStatus.SC_OK);
        assertEquals(getResponse.jsonPath().getInt("pk"), newPartPk);
    }

    @Test(groups = {"regression", "parts"})
    @Story("Create Part with Initial Stock")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_008_postPartWithInitialStockCreatesStockItem() {
        Response locationResponse = given()
            .spec(SpecBuilder.build(Role.ADMIN))
            .queryParam(PartTestData.QUERY_PARAM_LIMIT, PartTestData.DEFAULT_PAGE_LIMIT)
            .when()
            .get(ApiConstants.STOCK_LOCATION_ENDPOINT);
        ResponseValidator.assertStatusAndContentType(locationResponse, HttpStatus.SC_OK);
        int locationPk = locationResponse.jsonPath().getInt("results[0].pk");

        String newName = PartTestData.testPartName("TC-APCRUD-008", PartTestData.INITIAL_STOCK_PART_NAME_SUFFIX);
        Map<String, Object> payload = Map.of(
            PartTestData.FIELD_NAME, newName,
            PartTestData.FIELD_INITIAL_STOCK, Map.of(
                PartTestData.FIELD_QUANTITY, PartTestData.INITIAL_STOCK_QUANTITY,
                PartTestData.FIELD_LOCATION, locationPk));

        Response createResponse = partService.createPartRaw(payload, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int newPartPk = createResponse.jsonPath().getInt("pk");
        createdPartIds.add(newPartPk);

        assertNull(createResponse.jsonPath().get(PartTestData.FIELD_INITIAL_STOCK),
            "initial_stock must be absent from response (write-only)");

        Response stockResponse = given()
            .spec(SpecBuilder.build(Role.ADMIN))
            .queryParam(PartTestData.QUERY_PARAM_PART, newPartPk)
            .when()
            .get(ApiConstants.STOCK_ENDPOINT);
        ResponseValidator.assertStatusAndContentType(stockResponse, HttpStatus.SC_OK);

        log.info("Stock response body: {}", stockResponse.getBody().asString());
        List<?> stockItems = stockResponse.jsonPath().getList("$");
        assertEquals(stockItems.size(), 1, "exactly one stock item must exist");
        assertEquals(stockResponse.jsonPath().getDouble("[0].quantity"),
            PartTestData.INITIAL_STOCK_QUANTITY,
            "stock quantity must equal " + PartTestData.INITIAL_STOCK_QUANTITY);
        assertEquals(stockResponse.jsonPath().getInt("[0].location"), locationPk,
            "stock location must match the supplied location pk");
        assertEquals(stockResponse.jsonPath().getInt("[0].status"), PartTestData.STOCK_STATUS_OK,
            "stock status must be OK (" + PartTestData.STOCK_STATUS_OK + ")");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Create Part with Initial Supplier")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_009_postPartWithInitialSupplierCreatesSupplierPartRecord() {
        Response supplierResponse = given()
            .spec(SpecBuilder.build(Role.ADMIN))
            .queryParam(PartTestData.QUERY_PARAM_IS_SUPPLIER, PartTestData.QUERY_VALUE_TRUE)
            .queryParam(PartTestData.QUERY_PARAM_LIMIT, PartTestData.DEFAULT_PAGE_LIMIT)
            .when()
            .get(ApiConstants.COMPANY_ENDPOINT);
        ResponseValidator.assertStatusAndContentType(supplierResponse, HttpStatus.SC_OK);
        int supplierPk = supplierResponse.jsonPath().getInt("results[0].pk");

        String newName = PartTestData.testPartName("TC-APCRUD-009", PartTestData.INITIAL_SUPPLIER_PART_NAME_SUFFIX);
        Map<String, Object> payload = Map.of(
            PartTestData.FIELD_NAME, newName,
            PartTestData.FIELD_PURCHASEABLE, true,
            PartTestData.FIELD_INITIAL_SUPPLIER, Map.of(
                PartTestData.FIELD_SUPPLIER, supplierPk,
                PartTestData.FIELD_SKU_INPUT, PartTestData.SUPPLIER_SKU));

        Response createResponse = partService.createPartRaw(payload, Role.ADMIN);
        log.info("Create part response body: {}", createResponse.getBody().asString());
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int newPartPk = createResponse.jsonPath().getInt("pk");
        createdPartIds.add(newPartPk);

        assertNull(createResponse.jsonPath().get(PartTestData.FIELD_INITIAL_SUPPLIER),
            "initial_supplier must be absent from response (write-only)");

        Response supplierPartResponse = given()
            .spec(SpecBuilder.build(Role.ADMIN))
            .queryParam(PartTestData.QUERY_PARAM_PART, newPartPk)
            .when()
            .get(ApiConstants.SUPPLIER_PART_ENDPOINT);
        ResponseValidator.assertStatusAndContentType(supplierPartResponse, HttpStatus.SC_OK);
        log.info("Supplier part response body: {}", supplierPartResponse.getBody().asString());

        List<?> supplierParts = supplierPartResponse.jsonPath().getList("$");
        assertEquals(supplierParts.size(), 1, "exactly one supplier part must exist");
        assertEquals(supplierPartResponse.jsonPath().getInt("[0].supplier"), supplierPk,
            "supplier pk must match");
        assertEquals(supplierPartResponse.jsonPath().getString("[0].SKU"), PartTestData.SUPPLIER_SKU,
            "SKU must match");
    }
}
