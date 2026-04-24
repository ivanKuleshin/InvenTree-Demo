package com.inventree.tests.part;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.Part;
import com.inventree.model.PartCategory;
import com.inventree.model.PartCategoryRequest;
import com.inventree.model.PartListParams;
import com.inventree.model.PartRequest;
import com.inventree.model.PaginatedResponse;
import com.inventree.testdata.FilteringTestData;
import com.inventree.testdata.PartTestData;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Part Management")
@Feature("Part List — Filtering, Pagination, and Search")
public class PartFilteringPaginationSearchTest extends BaseTest {

    private final List<Integer> createdPartIds = new ArrayList<>();
    private int parentCategoryPk;
    private int childCategoryPk;
    private int ipnPartPk;
    private String ipnPartValue;
    private final List<Integer> setupCreatedPartIds = new ArrayList<>();
    private final List<Integer> setupCreatedCategoryIds = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setupFilteringTestData() {
        parentCategoryPk = findOrCreateParentCategory();
        childCategoryPk = findOrCreateChildCategory(parentCategoryPk);

        ipnPartValue = "IPN-FILTER-" + PartTestData.RUN_ID;
        PartRequest ipnPart = PartRequest.builder()
                .name("IPN-FILTER-PART-" + PartTestData.RUN_ID)
                .ipn(ipnPartValue)
                .build();
        Part created = partService.createPart(ipnPart, Role.ADMIN);
        ipnPartPk = created.getPk();
        setupCreatedPartIds.add(ipnPartPk);

        Part parentCatPart = partService.createPart(
                PartTestData.minimalPartWithCategory(
                        PartTestData.testPartName("APFLT-SETUP", "parent"), parentCategoryPk),
                Role.ADMIN);
        setupCreatedPartIds.add(parentCatPart.getPk());

        Part childCatPart = partService.createPart(
                PartTestData.minimalPartWithCategory(
                        PartTestData.testPartName("APFLT-SETUP", "child"), childCategoryPk),
                Role.ADMIN);
        setupCreatedPartIds.add(childCatPart.getPk());
    }

    @AfterClass(alwaysRun = true)
    public void teardownFilteringTestData() {
        setupCreatedPartIds.forEach(id -> {
            try {
                partService.patchPart(id, PartTestData.patchActiveOnly(false), Role.ADMIN);
                partService.deletePart(id, Role.ADMIN);
            } catch (Exception e) {
                log.error("Error while deleting setup part {}", id, e);
            }
        });
        setupCreatedPartIds.clear();
        setupCreatedCategoryIds.forEach(id -> {
            try {
                partCategoryService.deleteCategory(id, Role.ADMIN);
            } catch (Exception e) {
                log.error("Error while deleting setup category {}", id, e);
            }
        });
        setupCreatedCategoryIds.clear();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdPartIds.forEach(id -> {
            try {
                partService.deletePart(id, Role.ADMIN);
            } catch (Exception e) {
                log.error("Error while deleting part", e);
            }
        });
        createdPartIds.clear();
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_001_limitAndOffsetReturnCorrectNonOverlappingPages() {
        PaginatedResponse<Part> page1 = partService.listParts(
                PartListParams.builder().limit(FilteringTestData.PAGINATION_LIMIT).build(), Role.ADMIN);

        assertEquals(page1.getResults().size(), FilteringTestData.PAGINATION_LIMIT,
                "Page 1 must contain exactly limit items");
        assertNull(page1.getPrevious(), "previous must be null on first page");
        assertNotNull(page1.getNext(), "next must be non-null on first page");
        assertTrue(page1.getNext().contains("limit=" + FilteringTestData.PAGINATION_LIMIT),
                "next URL must carry limit param");
        assertTrue(page1.getNext().contains("offset=" + FilteringTestData.PAGINATION_OFFSET),
                "next URL must carry offset param");

        Set<Integer> page1Pks = new HashSet<>();
        page1.getResults().forEach(p -> page1Pks.add(p.getPk()));

        PaginatedResponse<Part> page2 = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .offset(FilteringTestData.PAGINATION_OFFSET)
                        .build(), Role.ADMIN);

        assertEquals(page2.getResults().size(), FilteringTestData.PAGINATION_LIMIT,
                "Page 2 must contain exactly limit items");
        assertNotNull(page2.getPrevious(), "previous must be non-null on page 2");

        page2.getResults().forEach(p ->
                assertFalse(page1Pks.contains(p.getPk()),
                        "pk " + p.getPk() + " appears on both pages — pages must not overlap"));
    }

    @Test(groups = {"regression", "parts", "search"})
    @Story("Search")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_002_searchReturnsCaseInsensitiveMatchesAcrossFields() {
        PaginatedResponse<Part> lowerResult = partService.listParts(
                PartListParams.builder()
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(lowerResult.getCount() > 0, "count must be > 0 for 'resistor' search");

        lowerResult.getResults().forEach(p -> {
            String name = p.getName() != null ? p.getName().toLowerCase() : "";
            String description = p.getDescription() != null ? p.getDescription().toLowerCase() : "";
            String keywords = p.getKeywords() != null ? p.getKeywords().toLowerCase() : "";
            String ipn = p.getIpn() != null ? p.getIpn().toLowerCase() : "";
            assertTrue(
                    name.contains(FilteringTestData.SEARCH_TERM_LOWER)
                            || description.contains(FilteringTestData.SEARCH_TERM_LOWER)
                            || keywords.contains(FilteringTestData.SEARCH_TERM_LOWER)
                            || ipn.contains(FilteringTestData.SEARCH_TERM_LOWER),
                    "Part pk=" + p.getPk() + " has no searchable field containing '"
                            + FilteringTestData.SEARCH_TERM_LOWER + "'");
        });

        PaginatedResponse<Part> upperResult = partService.listParts(
                PartListParams.builder()
                        .search(FilteringTestData.SEARCH_TERM_UPPER)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(upperResult.getCount(), lowerResult.getCount(),
                "search must be case-insensitive — counts must match");

        PaginatedResponse<Part> noMatchResult = partService.listParts(
                PartListParams.builder()
                        .search(FilteringTestData.SEARCH_TERM_NO_MATCH)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(noMatchResult.getCount(), Integer.valueOf(0),
                "count must be 0 for non-existent search term");
        assertTrue(noMatchResult.getResults().isEmpty(),
                "results must be empty for non-existent search term");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Category Filter")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_003_categoryFilterReturnPartsInSpecifiedCategory() {
        PaginatedResponse<Part> directResult = partService.listParts(
                PartListParams.builder()
                        .category(parentCategoryPk)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(directResult.getCount() > 0,
                "count must be > 0 for category=" + parentCategoryPk);
        int directCount = directResult.getCount();

        PaginatedResponse<Part> cascadeResult = partService.listParts(
                PartListParams.builder()
                        .category(parentCategoryPk)
                        .cascade(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(cascadeResult.getCount() >= directCount,
                "cascade=true count must be >= direct count");

        PaginatedResponse<Part> nullCategoryResult = partService.listParts(
                PartListParams.builder()
                        .category(FilteringTestData.CATEGORY_NULL)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(nullCategoryResult.getCount() > 0, "category=null filter must return results");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_013_assemblyTrueFiltersToAssemblyPartsOnly() {
        PaginatedResponse<Part> totalPage = partService.listParts(
                PartListParams.builder().limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> assemblyTrue = partService.listParts(
                PartListParams.builder()
                        .assembly(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(assemblyTrue.getCount() > 0, "count must be > 0 for assembly=true");
        assemblyTrue.getResults().forEach(p ->
                assertTrue(p.getAssembly(), "Part pk=" + p.getPk() + " must have assembly=true"));
        int assemblyCount = assemblyTrue.getCount();

        PaginatedResponse<Part> assemblyFalse = partService.listParts(
                PartListParams.builder()
                        .assembly(false)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assemblyFalse.getResults().forEach(p ->
                assertFalse(p.getAssembly(), "Part pk=" + p.getPk() + " must have assembly=false"));
        int nonAssemblyCount = assemblyFalse.getCount();

        assertTrue(assemblyCount <= totalCount,
                "assembly=true count must be <= total count");
        assertTrue(nonAssemblyCount <= totalCount,
                "assembly=false count must be <= total count");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_016_hasStockTrueAndFalsePartitionAllPartsByStockPresence() {
        PaginatedResponse<Part> totalPage = partService.listParts(
                PartListParams.builder().limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> hasStockTrue = partService.listParts(
                PartListParams.builder()
                        .hasStock(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(hasStockTrue.getCount() > 0, "count must be > 0 for has_stock=true");
        hasStockTrue.getResults().forEach(p ->
                assertTrue(p.getInStock() != null && p.getInStock() > 0,
                        "Part pk=" + p.getPk() + " must have in_stock > 0"));
        int hasStockCount = hasStockTrue.getCount();

        PaginatedResponse<Part> hasStockFalse = partService.listParts(
                PartListParams.builder()
                        .hasStock(false)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        hasStockFalse.getResults().forEach(p ->
                assertEquals(p.getInStock(), Double.valueOf(0),
                        "Part pk=" + p.getPk() + " must have in_stock == 0"));
        int noStockCount = hasStockFalse.getCount();

        assertTrue(hasStockCount <= totalCount,
                "has_stock=true count must be <= total count");
        assertTrue(noStockCount <= totalCount,
                "has_stock=false count must be <= total count");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("IPN Filter")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_018_ipnExactMatchReturnsSingleMatchingPartNonExistentReturnsEmpty() {
        PaginatedResponse<Part> existingIpnResult = partService.listParts(
                PartListParams.builder()
                        .ipn(ipnPartValue)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(existingIpnResult.getCount(), Integer.valueOf(1),
                "count must be 1 for IPN=" + ipnPartValue);
        assertEquals(existingIpnResult.getResults().getFirst().getIpn(), ipnPartValue,
                "IPN of returned part must match filter value");
        assertEquals(existingIpnResult.getResults().getFirst().getPk(),
                Integer.valueOf(ipnPartPk),
                "pk of returned part must match expected pk");

        PaginatedResponse<Part> nonExistentIpnResult = partService.listParts(
                PartListParams.builder()
                        .ipn(FilteringTestData.IPN_NON_EXISTENT)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(nonExistentIpnResult.getCount(), Integer.valueOf(0),
                "count must be 0 for non-existent IPN");
        assertTrue(nonExistentIpnResult.getResults().isEmpty(),
                "results must be empty for non-existent IPN");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Combined Filters")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_025_combinedSearchAndBooleanFiltersApplyAndLogicToNarrowResults() {
        PaginatedResponse<Part> assemblyOnly = partService.listParts(
                PartListParams.builder().assembly(true).limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int assemblyCount = assemblyOnly.getCount();

        PaginatedResponse<Part> activeOnly = partService.listParts(
                PartListParams.builder().active(true).limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int activeCount = activeOnly.getCount();

        PaginatedResponse<Part> combined = partService.listParts(
                PartListParams.builder()
                        .assembly(true)
                        .active(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(combined.getCount() > 0, "combined count must be > 0");
        int combinedCount = combined.getCount();

        assertTrue(combinedCount <= assemblyCount,
                "combined count must be <= assembly=true count");
        assertTrue(combinedCount <= activeCount,
                "combined count must be <= active=true count");

        combined.getResults().forEach(p -> {
            assertTrue(p.getAssembly(), "Part pk=" + p.getPk() + " must have assembly=true");
            assertTrue(p.getActive(), "Part pk=" + p.getPk() + " must have active=true");
        });

        PaginatedResponse<Part> searchWithAssembly = partService.listParts(
                PartListParams.builder()
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .assembly(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(searchWithAssembly.getCount() <= assemblyCount,
                "search+assembly count must be <= assembly=true count");
        searchWithAssembly.getResults().forEach(p ->
                assertTrue(p.getAssembly(), "Part pk=" + p.getPk() + " must have assembly=true"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_004_activeFilterSeparatesActiveAndInactiveParts() {
        PaginatedResponse<Part> totalPage = partService.listParts(
                PartListParams.builder().limit(FilteringTestData.SMALL_PAGE_LIMIT).build(), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> activeResult = partService.listParts(
                PartListParams.builder()
                        .active(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        activeResult.getResults().forEach(p ->
                assertTrue(p.getActive(), "Part pk=" + p.getPk() + " must have active=true"));
        int activeCount = activeResult.getCount();

        PaginatedResponse<Part> inactiveResult = partService.listParts(
                PartListParams.builder()
                        .active(false)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        inactiveResult.getResults().forEach(p ->
                assertFalse(p.getActive(), "Part pk=" + p.getPk() + " must have active=false"));
        int inactiveCount = inactiveResult.getCount();

        assertTrue(activeCount <= totalCount,
                "active count must be <= total count");
        assertTrue(inactiveCount <= totalCount,
                "inactive count must be <= total count");
    }

    @Test(groups = {"regression", "parts", "ordering"})
    @Story("Ordering")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_005_orderingNameAndOrderingMinusNameSortAlphabetically() {
        PaginatedResponse<Part> ascResult = partService.listParts(
                PartListParams.builder()
                        .ordering(FilteringTestData.ORDERING_NAME_ASC)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        List<String> namesAsc = ascResult.getResults().stream()
                .map(Part::getName)
                .toList();

        for (int i = 0; i < namesAsc.size() - 1; i++) {
            assertTrue(namesAsc.get(i).compareToIgnoreCase(namesAsc.get(i + 1)) <= 0,
                    "Names must be in ascending order: '" + namesAsc.get(i)
                            + "' > '" + namesAsc.get(i + 1) + "'");
        }

        PaginatedResponse<Part> descResult = partService.listParts(
                PartListParams.builder()
                        .ordering(FilteringTestData.ORDERING_NAME_DESC)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        List<String> namesDesc = descResult.getResults().stream()
                .map(Part::getName)
                .toList();

        for (int i = 0; i < namesDesc.size() - 1; i++) {
            assertTrue(namesDesc.get(i).compareToIgnoreCase(namesDesc.get(i + 1)) >= 0,
                    "Names must be in descending order: '" + namesDesc.get(i)
                            + "' < '" + namesDesc.get(i + 1) + "'");
        }

        assertFalse(namesAsc.getFirst().equalsIgnoreCase(namesDesc.getFirst()),
                "First ascending and first descending name must differ");
    }

    @Test(groups = {"regression", "categories", "filtering"})
    @Story("Category Search and Parent Filter")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_006_categorySearchAndParentFilterOnCategoryEndpoint() {
        PaginatedResponse<PartCategory> searchResponse = partCategoryService.listCategories(
                Map.of("search", FilteringTestData.SEARCH_CATEGORY_NAME,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        int searchCount = searchResponse.getCount();
        assertTrue(searchCount > 0,
                "count must be > 0 for search=" + FilteringTestData.SEARCH_CATEGORY_NAME);

        List<String> names = searchResponse.getResults().stream()
                .map(PartCategory::getName)
                .toList();
        assertTrue(names.stream().anyMatch(
                        n -> n != null && n.toLowerCase().contains(FilteringTestData.SEARCH_CATEGORY_NAME.toLowerCase())),
                "results must contain a category whose name includes '" + FilteringTestData.SEARCH_CATEGORY_NAME + "'");

        List<String> pathstrings = searchResponse.getResults().stream()
                .map(PartCategory::getPathstring)
                .toList();
        assertTrue(
                pathstrings.stream().anyMatch(
                        ps -> ps != null && ps.contains(FilteringTestData.SEARCH_CATEGORY_NAME)),
                "At least one result must have pathstring containing '"
                        + FilteringTestData.SEARCH_CATEGORY_NAME + "'");

        PaginatedResponse<PartCategory> parentResponse = partCategoryService.listCategories(
                Map.of("parent", parentCategoryPk,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        List<Integer> parents = parentResponse.getResults().stream()
                .map(PartCategory::getParent)
                .toList();
        parents.forEach(p ->
                assertEquals(p, Integer.valueOf(parentCategoryPk),
                        "All results must have parent=" + parentCategoryPk));

        PaginatedResponse<PartCategory> topLevelResponse = partCategoryService.listCategories(
                Map.of(FilteringTestData.FILTER_PARAM_TOP_LEVEL, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        List<Integer> topLevelParents = topLevelResponse.getResults().stream()
                .map(PartCategory::getParent)
                .toList();
        topLevelParents.forEach(p ->
                assertNull(p, "All top-level categories must have parent=null"));
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_007_limitZeroReturnsUnpaginatedRawJsonArray() {
        Response response = partService.listPartsRaw(
                PartListParams.builder()
                        .limit(FilteringTestData.LIMIT_UNPAGINATED)
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .build(), Role.ADMIN);

        response.then().statusCode(HttpStatus.SC_OK);

        assertTrue(response.asString().trim().startsWith("["),
                "Response body must be a JSON array when limit=0");

        List<Object> array = response.jsonPath().getList("$");
        assertFalse(array.isEmpty(), "Array must not be empty");

        String body = response.asString();
        assertFalse(body.contains(FilteringTestData.JSON_KEY_COUNT), "count key must be absent in unpaginated array response");
        assertFalse(body.contains(FilteringTestData.JSON_KEY_NEXT), "next key must be absent in unpaginated array response");
        assertFalse(body.contains(FilteringTestData.JSON_KEY_PREVIOUS), "previous key must be absent in unpaginated array response");
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.MINOR)
    public void tc_APFLT_008_limitNegativeOneIsEquivalentToLimitZero() {
        Response negResponse = partService.listPartsRaw(
                PartListParams.builder()
                        .limit(FilteringTestData.LIMIT_NEGATIVE_ONE)
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .build(), Role.ADMIN);
        negResponse.then().statusCode(HttpStatus.SC_OK);
        assertTrue(negResponse.asString().trim().startsWith("["),
                "Response for limit=-1 must be a JSON array");
        int negLength = negResponse.jsonPath().getList("$").size();

        Response zeroResponse = partService.listPartsRaw(
                PartListParams.builder()
                        .limit(FilteringTestData.LIMIT_UNPAGINATED)
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .build(), Role.ADMIN);
        zeroResponse.then().statusCode(HttpStatus.SC_OK);
        int zeroLength = zeroResponse.jsonPath().getList("$").size();

        assertEquals(negLength, zeroLength,
                "Array length for limit=-1 must equal array length for limit=0 (search-filtered set)");
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.MINOR)
    public void tc_APFLT_009_offsetBeyondTotalCountReturnsEmptyResultsWithAccurateCount() {
        PaginatedResponse<Part> beyondResult = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .offset(FilteringTestData.BEYOND_TOTAL_OFFSET)
                        .build(), Role.ADMIN);

        assertTrue(beyondResult.getCount() > 0,
                "count must be > 0 even when offset exceeds total (reflects live dataset size)");
        assertTrue(beyondResult.getResults().isEmpty(), "results must be empty");
        assertNull(beyondResult.getNext(), "next must be null");
        assertNotNull(beyondResult.getPrevious(), "previous must be non-null");
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.MINOR)
    public void tc_APFLT_010_negativeOffsetIsNormalizedToZero() {
        PaginatedResponse<Part> page0 = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .offset(0)
                        .build(), Role.ADMIN);

        List<Integer> page0Pks = page0.getResults().stream().map(Part::getPk).toList();

        PaginatedResponse<Part> negOffsetPage = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .offset(-1)
                        .build(), Role.ADMIN);

        assertNull(negOffsetPage.getPrevious(), "previous must be null for offset=-1");
        List<Integer> negOffsetPks = negOffsetPage.getResults().stream().map(Part::getPk).toList();

        assertEquals(negOffsetPks, page0Pks,
                "offset=-1 must return the same pks as offset=0");
    }

    @Test(groups = {"regression", "parts", "pagination"})
    @Story("Pagination")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_011_limitExceedingTotalCountReturnsAllPartsInSinglePage() {
        PaginatedResponse<Part> singleItem = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.SINGLE_ITEM_LIMIT)
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .build(), Role.ADMIN);
        int filteredCount = singleItem.getCount();

        assertTrue(filteredCount > 0, "search='" + FilteringTestData.SEARCH_TERM_LOWER + "' must return at least one part");

        PaginatedResponse<Part> largeResult = partService.listParts(
                PartListParams.builder()
                        .limit(FilteringTestData.LARGE_ENOUGH_LIMIT)
                        .search(FilteringTestData.SEARCH_TERM_LOWER)
                        .build(), Role.ADMIN);

        assertEquals(largeResult.getCount(), Integer.valueOf(filteredCount),
                "count must be consistent between calls");
        assertEquals(largeResult.getResults().size(), filteredCount,
                "results length must equal count when limit exceeds total");
        assertNull(largeResult.getNext(), "next must be null when all results fit in one page");
    }

    @Test(groups = {"regression", "parts", "search"})
    @Story("Search")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_012_emptySearchStringEquivalentToNoSearchFilter() {
        PaginatedResponse<Part> unfiltered = partService.listParts(
                PartListParams.builder().limit(FilteringTestData.PAGINATION_LIMIT).build(), Role.ADMIN);
        int unfilteredCount = unfiltered.getCount();

        PaginatedResponse<Part> emptySearch = partService.listParts(
                PartListParams.builder()
                        .search(FilteringTestData.SEARCH_TERM_EMPTY)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(emptySearch.getCount(), Integer.valueOf(unfilteredCount),
                "empty search must return same count as unfiltered request");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_014_virtualTrueFiltersToVirtualPartsOnly() {
        PaginatedResponse<Part> virtualTrue = partService.listParts(
                PartListParams.builder()
                        .virtual(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(virtualTrue.getCount() > 0, "count must be > 0 for virtual=true");
        virtualTrue.getResults().forEach(p ->
                assertTrue(p.getVirtual(), "Part pk=" + p.getPk() + " must have virtual=true"));

        PaginatedResponse<Part> virtualFalse = partService.listParts(
                PartListParams.builder()
                        .virtual(false)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        virtualFalse.getResults().forEach(p ->
                assertFalse(p.getVirtual(), "Part pk=" + p.getPk() + " must have virtual=false"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_015_trackableTrueFiltersToSerialTrackedPartsOnly() {
        PaginatedResponse<Part> trackableTrue = partService.listParts(
                PartListParams.builder()
                        .trackable(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(trackableTrue.getCount() > 0, "count must be > 0 for trackable=true");
        trackableTrue.getResults().forEach(p ->
                assertTrue(p.getTrackable(), "Part pk=" + p.getPk() + " must have trackable=true"));

        PaginatedResponse<Part> trackableFalse = partService.listParts(
                PartListParams.builder()
                        .trackable(false)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        trackableFalse.getResults().forEach(p ->
                assertFalse(p.getTrackable(), "Part pk=" + p.getPk() + " must have trackable=false"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_017_purchaseableAndSalableCombinedFiltersApplyAndLogic() {
        PaginatedResponse<Part> purchaseableOnly = partService.listParts(
                PartListParams.builder().purchaseable(true).limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int purchaseableCount = purchaseableOnly.getCount();

        PaginatedResponse<Part> salableOnly = partService.listParts(
                PartListParams.builder().salable(true).limit(FilteringTestData.SINGLE_ITEM_LIMIT).build(), Role.ADMIN);
        int salableCount = salableOnly.getCount();

        PaginatedResponse<Part> combined = partService.listParts(
                PartListParams.builder()
                        .purchaseable(true)
                        .salable(true)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(combined.getCount() > 0, "combined count must be > 0");
        assertTrue(combined.getCount() <= purchaseableCount,
                "combined count must be <= purchaseable=true count");
        assertTrue(combined.getCount() <= salableCount,
                "combined count must be <= salable=true count");

        combined.getResults().forEach(p -> {
            assertTrue(p.getPurchaseable(), "Part pk=" + p.getPk() + " must have purchaseable=true");
            assertTrue(p.getSalable(), "Part pk=" + p.getPk() + " must have salable=true");
        });
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("IPN Regex Filter")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_019_ipnRegexMatchesAllPartsWhoseIpnStartsWithRES() {
        String ipnPrefix = "IPN-FILTER-";
        String ipnRegexPattern = "^" + ipnPrefix;

        PaginatedResponse<Part> matchingResult = partService.listParts(
                PartListParams.builder()
                        .ipnRegex(ipnRegexPattern)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(matchingResult.getCount() >= 1,
                "count must be >= 1 for IPN_regex=" + ipnRegexPattern);
        matchingResult.getResults().forEach(p ->
                assertTrue(p.getIpn() != null && p.getIpn().startsWith(ipnPrefix),
                        "Part pk=" + p.getPk() + " IPN '" + p.getIpn()
                                + "' must start with '" + ipnPrefix + "'"));

        PaginatedResponse<Part> noMatchResult = partService.listParts(
                PartListParams.builder()
                        .ipnRegex(FilteringTestData.IPN_REGEX_NO_MATCH)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(noMatchResult.getCount(), Integer.valueOf(0),
                "count must be 0 for non-matching IPN_regex");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Name Regex Filter")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_020_nameRegexFiltersPartsWhoseNameStartsWithR() {
        PaginatedResponse<Part> result = partService.listParts(
                PartListParams.builder()
                        .nameRegex(FilteringTestData.NAME_REGEX_PREFIX_R)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(result.getCount() > 1, "count must be > 1 for name_regex=^R");
        result.getResults().forEach(p ->
                assertTrue(p.getName() != null && p.getName().startsWith("R"),
                        "Part pk=" + p.getPk() + " name '" + p.getName()
                                + "' must start with 'R'"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Date Range Filter")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_021_createdAfterAndCreatedBeforeFilterByCreationDateRange() {
        PaginatedResponse<Part> rangeResult = partService.listParts(
                PartListParams.builder()
                        .createdAfter(FilteringTestData.DATE_RANGE_START)
                        .createdBefore(FilteringTestData.DATE_RANGE_END)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(rangeResult.getCount() > 0,
                "count must be > 0 for date range "
                        + FilteringTestData.DATE_RANGE_START + " to "
                        + FilteringTestData.DATE_RANGE_END);

        rangeResult.getResults().forEach(p -> {
            assertNotNull(p.getCreationDate(), "creation_date must not be null");
            assertTrue(p.getCreationDate().compareTo(FilteringTestData.DATE_RANGE_START) >= 0,
                    "Part pk=" + p.getPk() + " creation_date '" + p.getCreationDate()
                            + "' must be >= " + FilteringTestData.DATE_RANGE_START);
            assertTrue(p.getCreationDate().compareTo(FilteringTestData.DATE_RANGE_END) <= 0,
                    "Part pk=" + p.getPk() + " creation_date '" + p.getCreationDate()
                            + "' must be <= " + FilteringTestData.DATE_RANGE_END);
        });

        PaginatedResponse<Part> futureResult = partService.listParts(
                PartListParams.builder()
                        .createdAfter(FilteringTestData.DATE_FUTURE)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        assertEquals(futureResult.getCount(), Integer.valueOf(0),
                "count must be 0 for fully future date range");
        assertTrue(futureResult.getResults().isEmpty(),
                "results must be empty for fully future date range");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Category Filter")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_022_categoryWithCascadeTrueIncludesPartsInChildCategories() {
        PaginatedResponse<Part> cascadeResult = partService.listParts(
                PartListParams.builder()
                        .category(parentCategoryPk)
                        .cascade(true)
                        .limit(FilteringTestData.LARGE_LIMIT)
                        .build(), Role.ADMIN);

        assertTrue(cascadeResult.getCount() > 0, "count must be > 0 for cascade=true");

        List<Integer> categories = cascadeResult.getResults().stream()
                .map(Part::getCategory)
                .toList();

        assertTrue(categories.contains(parentCategoryPk),
                "Results must include parts from parent category pk=" + parentCategoryPk);
        assertTrue(categories.contains(childCategoryPk),
                "Results must include parts from child category pk=" + childCategoryPk);

        PaginatedResponse<Part> childOnlyResult = partService.listParts(
                PartListParams.builder()
                        .category(childCategoryPk)
                        .limit(FilteringTestData.SMALL_PAGE_LIMIT)
                        .build(), Role.ADMIN);

        childOnlyResult.getResults().forEach(p ->
                assertEquals(p.getCategory(), Integer.valueOf(childCategoryPk),
                        "All results must belong to child category pk=" + childCategoryPk));
    }

    @Test(groups = {"regression", "parts", "ordering"})
    @Story("Ordering")
    @Severity(SeverityLevel.MINOR)
    public void tc_APFLT_023_orderingInvalidFieldSilentlyReturnsHttp200WithDefaultOrder() {
        PaginatedResponse<Part> result = partService.listParts(
                PartListParams.builder()
                        .ordering(FilteringTestData.ORDERING_INVALID)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        assertNotNull(result.getCount(), "count must be present");
        assertNotNull(result.getResults(), "results must be present");
        assertEquals(result.getResults().size(), FilteringTestData.PAGINATION_LIMIT,
                "results length must equal limit");
    }

    @Test(groups = {"regression", "parts", "ordering"})
    @Story("Ordering")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_024_orderingMinusInStockSortsResultsByStockQuantityDescending() {
        PaginatedResponse<Part> descResult = partService.listParts(
                PartListParams.builder()
                        .ordering(FilteringTestData.ORDERING_STOCK_DESC)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        List<Double> stockDesc = descResult.getResults().stream()
                .map(Part::getInStock)
                .toList();

        for (int i = 0; i < stockDesc.size() - 1; i++) {
            assertTrue(stockDesc.get(i) >= stockDesc.get(i + 1),
                    "in_stock must be descending: " + stockDesc.get(i)
                            + " < " + stockDesc.get(i + 1));
        }

        PaginatedResponse<Part> ascResult = partService.listParts(
                PartListParams.builder()
                        .ordering(FilteringTestData.ORDERING_STOCK_ASC)
                        .limit(FilteringTestData.PAGINATION_LIMIT)
                        .build(), Role.ADMIN);

        List<Double> stockAsc = ascResult.getResults().stream()
                .map(Part::getInStock)
                .toList();

        for (int i = 0; i < stockAsc.size() - 1; i++) {
            assertTrue(stockAsc.get(i) <= stockAsc.get(i + 1),
                    "in_stock must be ascending: " + stockAsc.get(i)
                            + " > " + stockAsc.get(i + 1));
        }
    }

    private int findOrCreateParentCategory() {
        PaginatedResponse<PartCategory> listing = partCategoryService.listCategories(
                Map.of("top_level", "true", "limit", 1), Role.ADMIN);
        if (!listing.getResults().isEmpty()) {
            return listing.getResults().getFirst().getPk();
        }
        PartCategoryRequest request = PartCategoryRequest.builder()
                .name("FILTER-PARENT-CAT-" + PartTestData.RUN_ID)
                .build();
        PartCategory created = partCategoryService.createCategory(request, Role.ADMIN);
        setupCreatedCategoryIds.add(created.getPk());
        return created.getPk();
    }

    private int findOrCreateChildCategory(int parentPk) {
        PaginatedResponse<PartCategory> listing = partCategoryService.listCategories(
                Map.of("parent", parentPk, "limit", 1), Role.ADMIN);
        if (!listing.getResults().isEmpty()) {
            return listing.getResults().getFirst().getPk();
        }
        PartCategoryRequest request = PartCategoryRequest.builder()
                .name("FILTER-CHILD-CAT-" + PartTestData.RUN_ID)
                .parent(parentPk)
                .build();
        PartCategory created = partCategoryService.createCategory(request, Role.ADMIN);
        setupCreatedCategoryIds.add(created.getPk());
        return created.getPk();
    }
}
