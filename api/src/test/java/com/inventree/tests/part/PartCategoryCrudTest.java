package com.inventree.tests.part;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.PartCategory;
import com.inventree.model.PartCategoryRequest;
import com.inventree.model.PaginatedResponse;
import com.inventree.testdata.CategoryTestData;
import com.inventree.util.HttpStatus;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part Category CRUD")
public class PartCategoryCrudTest extends BaseTest {

    private final List<Integer> createdCategoryIds = new ArrayList<>();
    private int passivesCategoryPk;
    private boolean passivesCreatedBySetup;

    @BeforeClass(alwaysRun = true)
    public void setupPassivesCategory() {
        PaginatedResponse<PartCategory> result = partCategoryService.listCategories(
                Map.of("search", CategoryTestData.PASSIVES_NAME), Role.ADMIN);
        if (!result.getResults().isEmpty()) {
            passivesCategoryPk = result.getResults().get(0).getPk();
            passivesCreatedBySetup = false;
        } else {
            PartCategory created = partCategoryService.createCategory(
                    CategoryTestData.minimalTopLevel(CategoryTestData.PASSIVES_NAME), Role.ADMIN);
            passivesCategoryPk = created.getPk();
            passivesCreatedBySetup = true;
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardownPassivesCategory() {
        if (passivesCreatedBySetup) {
            try {
                partCategoryService.deleteCategory(passivesCategoryPk, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting setup passives category", t);
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdCategoryIds.forEach(id -> {
            try {
                partCategoryService.deleteCategory(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error while deleting part category", t);
            }
        });
        createdCategoryIds.clear();
    }

    @Test(groups = {"regression", "categories"})
    @Story("List Categories")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ACCRUD_001_getPartCategoryListReturnsPaginatedResult() {
        PaginatedResponse<PartCategory> response = partCategoryService.listCategories(
                Map.of("limit", CategoryTestData.DEFAULT_PAGE_LIMIT), Role.ADMIN);

        assertNotNull(response.getCount(), "count must not be null");
        assertTrue(response.getCount() > 0, "count must be greater than 0");
        assertNotNull(response.getResults(), "results must not be null");
        assertFalse(response.getResults().isEmpty(), "results must be non-empty");
        assertTrue(response.getNext() == null || response.getNext().startsWith("http"),
                "next must be null or a URI string");
        assertNull(response.getPrevious(), "previous must be null on first page");

        PartCategory topLevel = response.getResults().stream()
                .filter(c -> c.getParent() == null)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No top-level category (parent=null) found in first-page results"));

        assertNotNull(topLevel.getPk(), "pk must not be null");
        assertNotNull(topLevel.getName(), "name must not be null");
        assertNotNull(topLevel.getDescription(), "description must not be null");
        assertNotNull(topLevel.getLevel(), "level must not be null");
        assertNotNull(topLevel.getPartCount(), "part_count must not be null");
        assertNotNull(topLevel.getSubcategories(), "subcategories must not be null");
        assertNotNull(topLevel.getPathstring(), "pathstring must not be null");
        assertNotNull(topLevel.getStarred(), "starred must not be null");
        assertNotNull(topLevel.getStructural(), "structural must not be null");
        assertNull(topLevel.getParent(), "Top-level category parent must be null");
        assertEquals(topLevel.getLevel(), Integer.valueOf(0), "Top-level category level must be 0");

        PartCategory childCategory = response.getResults().stream()
                .filter(c -> c.getParent() != null)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No child category found in first-page results"));
        assertNotNull(childCategory.getParent(), "Child category parent must not be null");
        assertTrue(childCategory.getLevel() > 0, "Child category level must be greater than 0");
    }

    @Test(groups = {"regression", "categories"})
    @Story("Get Category by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ACCRUD_002_getPartCategoryByIdReturnsCorrectData() {
        Response rawResponse = partCategoryService.getCategoryByIdRaw(
                passivesCategoryPk, Role.ADMIN);
        rawResponse.then().statusCode(HttpStatus.SC_OK);

        PartCategory category = rawResponse.as(PartCategory.class);
        assertEquals(category.getPk(), Integer.valueOf(passivesCategoryPk));
        assertEquals(category.getName(), CategoryTestData.PASSIVES_NAME);
        assertEquals(category.getLevel(), Integer.valueOf(0));
        assertNull(category.getParent(), "Top-level category parent must be null");

        assertNull(rawResponse.jsonPath().get("path"),
                "path field must be absent when path_detail is not requested");

        Response pathDetailResponse = partCategoryService.getCategoryByIdRaw(
                passivesCategoryPk, Role.ADMIN,
                Map.of(CategoryTestData.QUERY_PARAM_PATH_DETAIL, CategoryTestData.QUERY_VALUE_TRUE));
        pathDetailResponse.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> path = pathDetailResponse.jsonPath().getList("path");
        assertNotNull(path, "path array must be present with path_detail=true");
        assertFalse(path.isEmpty(), "path array must not be empty");

        Map<String, Object> firstElement = path.getFirst();
        assertEquals(firstElement.get("pk"), passivesCategoryPk,
                "First path element pk must match category pk");
        assertEquals(firstElement.get("name"), CategoryTestData.PASSIVES_NAME,
                "First path element name must match category name");
    }

    @Test(groups = {"regression", "categories"})
    @Story("Create Category")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ACCRUD_003_postPartCategoryCreatesMinimalTopLevelCategory() {
        String categoryName = CategoryTestData.testCategoryName("TC-ACCRUD-003", "MinimalCat");
        PartCategoryRequest request = CategoryTestData.minimalTopLevel(categoryName);

        Response createResponse = partCategoryService.createCategoryRaw(request, Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newCatPk = createResponse.jsonPath().getInt("pk");
        assertTrue(newCatPk > 0, "pk must be a positive integer");

        assertEquals(createResponse.jsonPath().getString("name"), categoryName);
        assertNull(createResponse.jsonPath().get("parent"), "parent must be null for top-level");
        assertEquals(createResponse.jsonPath().getInt("level"), 0);
        assertEquals(createResponse.jsonPath().getString("pathstring"), categoryName);
        assertEquals(createResponse.jsonPath().getString("description"), "");
        assertFalse(createResponse.jsonPath().getBoolean("structural"));
        assertNull(createResponse.jsonPath().get("default_location"),
                "default_location must be null");

        List<Map<String, Object>> path = createResponse.jsonPath().getList("path");
        assertNotNull(path, "path array must be present in POST response");
        assertEquals(path.size(), 1, "path array must contain exactly one element");
        assertEquals(path.getFirst().get("pk"), newCatPk);
        assertEquals(path.getFirst().get("name"), categoryName);

        createdCategoryIds.add(newCatPk);
    }

    @Test(groups = {"regression", "categories"})
    @Story("Create Category")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ACCRUD_004_postPartCategoryCreatesChildCategoryWithParentAssigned() {
        int parentPk = passivesCategoryPk;
        String childName = CategoryTestData.testCategoryName("TC-ACCRUD-004", "ChildCat");
        String description = "Child category for CRUD test";
        String expectedPathstring = CategoryTestData.PASSIVES_NAME + "/" + childName;

        PartCategoryRequest request = CategoryTestData.withParentAndDescription(
                childName, parentPk, description);

        Response createResponse = partCategoryService.createCategoryRaw(request, Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int childPk = createResponse.jsonPath().getInt("pk");
        assertTrue(childPk > 0, "pk must be a positive integer");

        assertEquals(createResponse.jsonPath().getString("name"), childName);
        assertEquals(createResponse.jsonPath().getInt("parent"), parentPk);
        assertEquals(createResponse.jsonPath().getInt("level"), 1);
        assertEquals(createResponse.jsonPath().getString("pathstring"), expectedPathstring);
        assertEquals(createResponse.jsonPath().getString("description"), description);

        List<Map<String, Object>> path = createResponse.jsonPath().getList("path");
        assertNotNull(path, "path array must be present in POST response");
        assertEquals(path.size(), 2, "path array must contain two elements for level-1 category");
        assertEquals(path.get(0).get("pk"), parentPk);
        assertEquals(path.get(0).get("name"), CategoryTestData.PASSIVES_NAME);
        assertEquals(path.get(1).get("pk"), childPk);
        assertEquals(path.get(1).get("name"), childName);

        createdCategoryIds.add(childPk);
    }

    @Test(groups = {"regression", "categories"})
    @Story("Update Category")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ACCRUD_005_putPartCategoryReplacesAllWritableFields() {
        String setupName = CategoryTestData.testCategoryName("TC-ACCRUD-005", "Setup");
        PartCategory created = partCategoryService.createCategory(
                CategoryTestData.minimalTopLevel(setupName), Role.ADMIN);
        int catPk = created.getPk();
        createdCategoryIds.add(catPk);

        String updatedName = "TC-ACCRUD-005-UpdatedName";
        String updatedDescription = "PUT full update applied";
        String updatedKeywords = "updated keywords";

        PartCategoryRequest updateRequest = CategoryTestData.fullUpdate(
                updatedName, updatedDescription, true, updatedKeywords, null);

        PartCategory updated = partCategoryService.updateCategory(catPk, updateRequest, Role.ADMIN);

        assertEquals(updated.getPk(), Integer.valueOf(catPk));
        assertEquals(updated.getName(), updatedName);
        assertEquals(updated.getDescription(), updatedDescription);
        assertEquals(updated.getStructural(), Boolean.TRUE);
        assertEquals(updated.getDefaultKeywords(), updatedKeywords);
        assertNull(updated.getParent(), "parent must be null");
        assertEquals(updated.getPathstring(), updatedName);
        assertEquals(updated.getPartCount(), Integer.valueOf(0));
    }

    @Test(groups = {"regression", "categories"})
    @Story("Partial Update Category")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ACCRUD_006_patchPartCategoryPartiallyUpdatesDescription() {
        String setupName = CategoryTestData.testCategoryName("TC-ACCRUD-006", "Setup");
        String originalDescription = "original description";
        PartCategoryRequest setupRequest = PartCategoryRequest.builder()
                .name(setupName)
                .description(originalDescription)
                .build();
        PartCategory created = partCategoryService.createCategory(setupRequest, Role.ADMIN);
        int catPk = created.getPk();
        createdCategoryIds.add(catPk);

        String patchedDescription = "PATCH partial update applied";
        PartCategoryRequest patchRequest = CategoryTestData.descriptionOnly(patchedDescription);

        PartCategory patched = partCategoryService.patchCategory(catPk, patchRequest, Role.ADMIN);

        assertEquals(patched.getPk(), Integer.valueOf(catPk));
        assertEquals(patched.getDescription(), patchedDescription);
        assertEquals(patched.getName(), setupName, "name must be unchanged after PATCH");
        assertNull(patched.getParent(), "parent must be unchanged after PATCH");
        assertEquals(patched.getStructural(), Boolean.FALSE, "structural must be unchanged after PATCH");
    }

    @Test(groups = {"regression", "categories"})
    @Story("Delete Category")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ACCRUD_007_deletePartCategoryRemovesCategory() {
        String setupName = CategoryTestData.testCategoryName("TC-ACCRUD-007", "ToDelete");
        PartCategory created = partCategoryService.createCategory(
                CategoryTestData.minimalTopLevel(setupName), Role.ADMIN);
        int catPk = created.getPk();

        Response deleteResponse = partCategoryService.deleteCategoryRaw(catPk, Role.ADMIN);
        deleteResponse.then().statusCode(HttpStatus.SC_NO_CONTENT);
        assertEquals(deleteResponse.body().asString(), CategoryTestData.EMPTY_BODY,
                "DELETE response body must be empty");

        Response getResponse = partCategoryService.getCategoryByIdRaw(catPk, Role.ADMIN);
        getResponse.then().statusCode(HttpStatus.SC_NOT_FOUND);
        assertEquals(getResponse.jsonPath().getString("detail"),
                CategoryTestData.ERROR_MSG_NOT_FOUND);
    }
}
