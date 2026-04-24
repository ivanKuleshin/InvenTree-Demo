package com.inventree.tests.part;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.CategoryDetail;
import com.inventree.model.CategoryPathEntry;
import com.inventree.model.PartCategory;
import com.inventree.model.PartCategoryRequest;
import com.inventree.model.Company;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartDetailParams;
import com.inventree.model.PartListParams;
import com.inventree.model.PartParameter;
import com.inventree.model.PartParameterTemplate;
import com.inventree.model.PartRequest;
import com.inventree.model.StockItem;
import com.inventree.model.StockLocationDetail;
import com.inventree.model.SupplierPart;
import com.inventree.testdata.PartTestData;
import com.inventree.testdata.StockTestData;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.inventree.testdata.PartTestData.DEFAULT_PAGE_LIMIT;
import static com.inventree.testdata.PartTestData.PAGE_LIMIT_FOR_SEARCH;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part CRUD")
public class PartCrudTest extends BaseTest {

    private final List<Integer> createdPartIds = new ArrayList<>();
    private final List<Integer> createdStockLocationIds = new ArrayList<>();
    private int defaultCategoryPk;

    @BeforeClass(alwaysRun = true)
    public void setupTestData() {
        defaultCategoryPk = findOrCreateDefaultCategory();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdPartIds.forEach(id -> {
            try {
                List<StockItem> stockItems = stockItemService.listStockItems(
                    Map.of(PartTestData.QUERY_PARAM_PART, id), Role.ADMIN);
                stockItems.forEach(item -> stockItemService.deleteStockItem(item.getPk(), Role.ADMIN));
            } catch (Throwable t) {
                log.error("Error while deleting stock items for part {}", id, t);
            }
            try {
                List<SupplierPart> supplierParts = companyService.listSupplierParts(
                    Map.of(PartTestData.QUERY_PARAM_PART, id), Role.ADMIN);
                supplierParts.forEach(sp -> companyService.deleteSupplierPart(sp.getPk(), Role.ADMIN));
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
        createdStockLocationIds.forEach(id -> {
            try {
                stockLocationService.deleteStockLocation(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting stock location {}", id, t);
            }
        });
        createdStockLocationIds.clear();
    }

    @Test(groups = {"regression", "parts"})
    @Story("List Parts")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_APCRUD_001_getPartListReturnsPaginatedResultWithFullFieldSet() {
        PaginatedResponse<Part> response = partService.listParts(
            PartListParams.builder().limit(DEFAULT_PAGE_LIMIT).build(), Role.READER);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() > 0, "count must be greater than 0");
        assertNotNull(response.getResults(), "results must not be null");
        assertEquals(response.getResults().size(), DEFAULT_PAGE_LIMIT,
            "results must contain exactly " + DEFAULT_PAGE_LIMIT + " items");
        assertNull(response.getPrevious(), "previous must be null on first page");
        assertNotNull(response.getNext(), "next must be non-null when more pages exist");
        assertTrue(response.getNext().startsWith(PartTestData.URI_PREFIX), "next must be a URI string");

        assertFalse(response.getResults().isEmpty(), "results must be non-empty");
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
        String name = PartTestData.testPartName("TC-APCRUD-002", "GetById");
        boolean active = true;
        boolean component = true;
        boolean purchasable = true;
        boolean assembly = false;
        PartRequest createRequest = PartTestData.fullPartRequest(
            name,
            PartTestData.GET_BY_ID_DESCRIPTION,
            PartTestData.GET_BY_ID_IPN,
            PartTestData.GET_BY_ID_KEYWORDS,
            active, component, purchasable, assembly);
        Part created = partService.createPart(createRequest, Role.ADMIN);
        int partPk = created.getPk();
        createdPartIds.add(partPk);

        Response rawResponse = partService.getPartByIdRaw(partPk, Role.READER);
        ResponseValidator.assertStatusAndContentType(rawResponse, HttpStatus.SC_OK);

        Part part = ResponseValidator.deserialize(rawResponse, Part.class);
        assertEquals(part.getPk(), Integer.valueOf(partPk));
        assertEquals(part.getName(), name);
        assertEquals(part.getActive(), active);
        assertEquals(part.getComponent(), component);
        assertEquals(part.getPurchaseable(), purchasable);
        assertEquals(part.getAssembly(), assembly);
        assertNull(part.getCategory(), "category must be null when part created without category");
        assertNotNull(part.getInStock(), "in_stock must not be null");
        assertTrue(part.getInStock() >= 0, "in_stock must be >= 0");

        rawResponse.then()
            .body(PartTestData.QUERY_PARAM_PARAMETERS, nullValue())
            .body(PartTestData.QUERY_PARAM_TAGS, nullValue())
            .body(PartTestData.QUERY_PARAM_CATEGORY_DETAIL, nullValue());
    }

    @Test(groups = {"regression", "parts"})
    @Story("Get Part by ID with Query Flags")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_003_getPartByIdWithQueryFlagsReturnsExpandedFields() {
        int partWithParamsPk = findPartWithParametersPk();

        Response parametersResponse = partService.getPartByIdRaw(partWithParamsPk, Role.READER,
            PartDetailParams.builder().parameters(true).tags(true).build());

        ResponseValidator.assertStatusAndContentType(parametersResponse, HttpStatus.SC_OK);

        Part paramsPart = ResponseValidator.deserialize(parametersResponse, Part.class);
        List<PartParameter> parameters = paramsPart.getParameters();
        assertNotNull(parameters, "parameters must be present with ?parameters=true");
        assertFalse(parameters.isEmpty(), "parameters must be non-empty");

        PartParameter firstParam = parameters.getFirst();
        assertNotNull(firstParam.getPk(), "parameter pk must not be null");
        assertNotNull(firstParam.getTemplate(), "parameter template must not be null");
        assertNotNull(firstParam.getData(), "parameter data must not be null");

        PartParameterTemplate templateDetail = firstParam.getTemplateDetail();
        assertNotNull(templateDetail, "parameter template_detail must not be null");
        assertNotNull(templateDetail.getPk(), "template_detail pk must not be null");
        assertNotNull(templateDetail.getName(), "template_detail name must not be null");
        assertNotNull(templateDetail.getUnits(), "template_detail units must not be null");
        assertNotNull(templateDetail.getDescription(), "template_detail description must not be null");

        assertNotNull(paramsPart.getTags(), "tags must be present with ?tags=true");

        String noCatName = PartTestData.testPartName("TC-APCRUD-003", "NoCat");
        Part noCatPart = partService.createPart(PartTestData.minimalPart(noCatName), Role.ADMIN);
        int noCatPk = noCatPart.getPk();
        createdPartIds.add(noCatPk);

        Response noCatResponse = partService.getPartByIdRaw(noCatPk, Role.READER,
            PartDetailParams.builder().categoryDetail(true).build());
        ResponseValidator.assertStatusAndContentType(noCatResponse, HttpStatus.SC_OK);
        Part noCatResult = ResponseValidator.deserialize(noCatResponse, Part.class);
        assertNull(noCatResult.getCategoryDetail(), "category_detail must be null when part has no category");

        int categorizedPartPk = findCategorizedPartPk();

        Response catDetailResponse = partService.getPartByIdRaw(categorizedPartPk, Role.READER,
            PartDetailParams.builder().categoryDetail(true).build());
        ResponseValidator.assertStatusAndContentType(catDetailResponse, HttpStatus.SC_OK);
        Part catDetailPart = ResponseValidator.deserialize(catDetailResponse, Part.class);
        CategoryDetail categoryDetail = catDetailPart.getCategoryDetail();
        assertNotNull(categoryDetail, "category_detail must be non-null when part has a category");
        assertNotNull(categoryDetail.getPk(), "category_detail pk must not be null");
        assertNotNull(categoryDetail.getName(), "category_detail name must not be null");

        Response pathDetailResponse = partService.getPartByIdRaw(categorizedPartPk, Role.READER,
            PartDetailParams.builder().pathDetail(true).build());
        ResponseValidator.assertStatusAndContentType(pathDetailResponse, HttpStatus.SC_OK);
        Part pathDetailPart = ResponseValidator.deserialize(pathDetailResponse, Part.class);
        List<CategoryPathEntry> categoryPath = pathDetailPart.getCategoryPath();
        assertNotNull(categoryPath, "category_path must be present with ?path_detail=true for a categorized part");
        assertFalse(categoryPath.isEmpty(), "category_path must be non-empty for a categorized part");
        assertNotNull(categoryPath.getFirst().getPk(), "category_path entry must have pk");
        assertNotNull(categoryPath.getFirst().getName(), "category_path entry must have name");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Update Part")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_004_putPartReplacesAllWritableFields() {
        String setupName = PartTestData.testPartName("TC-APCRUD-004", "Setup");
        PartRequest setupRequest = PartTestData.minimalPartWithCategory(
            setupName, defaultCategoryPk);
        Part created = partService.createPart(setupRequest, Role.ADMIN);
        int partPk = created.getPk();
        createdPartIds.add(partPk);

        String updatedName = PartTestData.testPartName("TC-APCRUD-004", "UpdatedName");
        PartRequest updateRequest = PartTestData.fullUpdateRequest(
            updatedName,
            PartTestData.PUT_UPDATE_DESCRIPTION,
            PartTestData.PUT_UPDATE_IPN,
            true,
            true,
            true,
            false,
            false,
            false,
            false,
            true,
            PartTestData.PUT_UPDATE_KEYWORDS,
            defaultCategoryPk);

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
        PartRequest setupRequest = PartTestData.partWithKeywords(setupName, PartTestData.ORIGINAL_KEYWORDS);
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
        int sourcePk = findCategorizedPartPk();

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

        int newPartPk = createResponse.jsonPath().getInt(PartTestData.FIELD_PK);
        createdPartIds.add(newPartPk);

        assertEquals(createResponse.jsonPath().getString(PartTestData.FIELD_NAME), newName);
        createResponse.then()
            .body(PartTestData.FIELD_DUPLICATE, nullValue())
            .body(PartTestData.FIELD_CATEGORY, nullValue());

        Response getResponse = partService.getPartByIdRaw(newPartPk, Role.READER);
        ResponseValidator.assertStatusAndContentType(getResponse, HttpStatus.SC_OK);
        assertEquals(getResponse.jsonPath().getInt(PartTestData.FIELD_PK), newPartPk);
    }

    @Test(groups = {"regression", "parts"})
    @Story("Create Part with Initial Stock")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_008_postPartWithInitialStockCreatesStockItem() {
        PaginatedResponse<StockLocationDetail> locations = stockLocationService.listStockLocations(
            Map.of(PartTestData.QUERY_PARAM_LIMIT, PAGE_LIMIT_FOR_SEARCH,
                PartTestData.QUERY_PARAM_STRUCTURAL, PartTestData.QUERY_VALUE_FALSE), Role.ADMIN);
        int locationPk = locations.getResults().stream()
            .filter(l -> !Boolean.TRUE.equals(l.getStructural()))
            .findFirst()
            .map(StockLocationDetail::getPk)
            .orElseGet(() -> {
                StockLocationDetail created = stockLocationService.createStockLocation(
                    StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-APCRUD-008", "precondition")),
                    Role.ADMIN);
                createdStockLocationIds.add(created.getPk());
                return created.getPk();
            });

        String newName = PartTestData.testPartName("TC-APCRUD-008", PartTestData.INITIAL_STOCK_PART_NAME_SUFFIX);
        Map<String, Object> payload = Map.of(
            PartTestData.FIELD_NAME, newName,
            PartTestData.FIELD_INITIAL_STOCK, Map.of(
                PartTestData.FIELD_QUANTITY, PartTestData.INITIAL_STOCK_QUANTITY,
                PartTestData.FIELD_LOCATION, locationPk));

        Response createResponse = partService.createPartRaw(payload, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int newPartPk = createResponse.jsonPath().getInt(PartTestData.FIELD_PK);
        createdPartIds.add(newPartPk);

        assertNull(createResponse.jsonPath().get(PartTestData.FIELD_INITIAL_STOCK),
            "initial_stock must be absent from response (write-only)");

        List<StockItem> stockItems = stockItemService.listStockItems(
            Map.of(PartTestData.QUERY_PARAM_PART, newPartPk), Role.ADMIN);
        assertEquals(stockItems.size(), 1, "exactly one stock item must exist");
        StockItem stockItem = stockItems.getFirst();
        assertEquals(stockItem.getQuantity(), PartTestData.INITIAL_STOCK_QUANTITY,
            "stock quantity must equal " + PartTestData.INITIAL_STOCK_QUANTITY);
        assertEquals(stockItem.getLocation(), Integer.valueOf(locationPk),
            "stock location must match the supplied location pk");
        assertEquals(stockItem.getStatus(), Integer.valueOf(PartTestData.STOCK_STATUS_OK),
            "stock status must be OK (" + PartTestData.STOCK_STATUS_OK + ")");
    }

    @Test(groups = {"regression", "parts"})
    @Story("Create Part with Initial Supplier")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APCRUD_009_postPartWithInitialSupplierCreatesSupplierPartRecord() {
        PaginatedResponse<Company> companies = companyService.listCompanies(
            Map.of(PartTestData.QUERY_PARAM_IS_SUPPLIER, PartTestData.QUERY_VALUE_TRUE,
                PartTestData.QUERY_PARAM_LIMIT, DEFAULT_PAGE_LIMIT), Role.ADMIN);
        assertFalse(companies.getResults().isEmpty(), "supplier companies must be non-empty");
        int supplierPk = companies.getResults().getFirst().getPk();

        String newName = PartTestData.testPartName("TC-APCRUD-009", PartTestData.INITIAL_SUPPLIER_PART_NAME_SUFFIX);
        Map<String, Object> payload = Map.of(
            PartTestData.FIELD_NAME, newName,
            PartTestData.FIELD_PURCHASEABLE, true,
            PartTestData.FIELD_INITIAL_SUPPLIER, Map.of(
                PartTestData.FIELD_SUPPLIER, supplierPk,
                PartTestData.FIELD_SKU_INPUT, PartTestData.SUPPLIER_SKU));

        Response createResponse = partService.createPartRaw(payload, Role.ADMIN);
        ResponseValidator.assertStatusAndContentType(createResponse, HttpStatus.SC_CREATED);

        int newPartPk = createResponse.jsonPath().getInt(PartTestData.FIELD_PK);
        createdPartIds.add(newPartPk);

        assertNull(createResponse.jsonPath().get(PartTestData.FIELD_INITIAL_SUPPLIER),
            "initial_supplier must be absent from response (write-only)");

        List<SupplierPart> supplierParts = companyService.listSupplierParts(
            Map.of(PartTestData.QUERY_PARAM_PART, newPartPk), Role.ADMIN);
        assertEquals(supplierParts.size(), 1, "exactly one supplier part must exist");
        SupplierPart supplierPart = supplierParts.getFirst();
        assertEquals(supplierPart.getSupplier(), Integer.valueOf(supplierPk), "supplier pk must match");
        assertEquals(supplierPart.getSku(), PartTestData.SUPPLIER_SKU, "SKU must match");
    }

    private int findOrCreateDefaultCategory() {
        PaginatedResponse<PartCategory> listing = partCategoryService.listCategories(
                Map.of("limit", 1), Role.ADMIN);
        if (!listing.getResults().isEmpty()) {
            return listing.getResults().getFirst().getPk();
        }
        PartCategoryRequest newCategory = PartCategoryRequest.builder()
                .name("TC-APCRUD-DEFAULT-CAT-" + PartTestData.RUN_ID)
                .build();
        return partCategoryService.createCategory(newCategory, Role.ADMIN).getPk();
    }

    private int findPartWithParametersPk() {
        return partService.listParts(
                PartListParams.builder().limit(PAGE_LIMIT_FOR_SEARCH).parameters(true).build(), Role.READER)
            .getResults().stream()
            .filter(p -> p.getParameters() != null && !p.getParameters().isEmpty())
            .findFirst()
            .orElseThrow(() -> new AssertionError("No part with parameters found in first 100 results"))
            .getPk();
    }

    private int findCategorizedPartPk() {
        PaginatedResponse<Part> listing = partService.listParts(
            PartListParams.builder().limit(DEFAULT_PAGE_LIMIT).build(), Role.READER);
        return listing.getResults().stream()
            .filter(p -> p.getCategory() != null)
            .findFirst()
            .orElseThrow(() -> new AssertionError("No categorized part found in first-page results"))
            .getPk();
    }
}
