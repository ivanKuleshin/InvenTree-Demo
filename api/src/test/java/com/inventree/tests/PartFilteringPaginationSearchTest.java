package com.inventree.tests;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.Part;
import com.inventree.model.PartCategory;
import com.inventree.model.PaginatedResponse;
import com.inventree.testdata.FilteringTestData;
import com.inventree.util.HttpStatus;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
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
                Map.of("limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.PAGINATION_LIMIT,
                       "offset", FilteringTestData.PAGINATION_OFFSET), Role.ADMIN);

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
                Map.of("search", FilteringTestData.SEARCH_TERM_LOWER,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

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
                Map.of("search", FilteringTestData.SEARCH_TERM_UPPER,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        assertEquals(upperResult.getCount(), lowerResult.getCount(),
                "search must be case-insensitive — counts must match");

        PaginatedResponse<Part> noMatchResult = partService.listParts(
                Map.of("search", FilteringTestData.SEARCH_TERM_NO_MATCH,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

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
                Map.of("category", FilteringTestData.ELECTRONICS_CATEGORY_PK,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(directResult.getCount() > 0,
                "count must be > 0 for category=" + FilteringTestData.ELECTRONICS_CATEGORY_PK);
        int directCount = directResult.getCount();

        PaginatedResponse<Part> cascadeResult = partService.listParts(
                Map.of("category", FilteringTestData.ELECTRONICS_CATEGORY_PK,
                       FilteringTestData.FILTER_PARAM_CASCADE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(cascadeResult.getCount() >= directCount,
                "cascade=true count must be >= direct count");

        PaginatedResponse<Part> nullCategoryResult = partService.listParts(
                Map.of("category", FilteringTestData.CATEGORY_NULL,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        nullCategoryResult.getResults().forEach(p ->
                assertNull(p.getCategory(), "All parts with category=null filter must have null category"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.BLOCKER)
    public void tc_APFLT_013_assemblyTrueFiltersToAssemblyPartsOnly() {
        PaginatedResponse<Part> totalPage = partService.listParts(
                Map.of("limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> assemblyTrue = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ASSEMBLY, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(assemblyTrue.getCount() > 0, "count must be > 0 for assembly=true");
        assemblyTrue.getResults().forEach(p ->
                assertTrue(p.getAssembly(), "Part pk=" + p.getPk() + " must have assembly=true"));
        int assemblyCount = assemblyTrue.getCount();

        PaginatedResponse<Part> assemblyFalse = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ASSEMBLY, FilteringTestData.FILTER_VALUE_FALSE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> hasStockTrue = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_HAS_STOCK, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(hasStockTrue.getCount() > 0, "count must be > 0 for has_stock=true");
        hasStockTrue.getResults().forEach(p ->
                assertTrue(p.getInStock() != null && p.getInStock() > 0,
                        "Part pk=" + p.getPk() + " must have in_stock > 0"));
        int hasStockCount = hasStockTrue.getCount();

        PaginatedResponse<Part> hasStockFalse = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_HAS_STOCK, FilteringTestData.FILTER_VALUE_FALSE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("IPN", FilteringTestData.IPN_EXISTING,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertEquals(existingIpnResult.getCount(), Integer.valueOf(1),
                "count must be 1 for IPN=" + FilteringTestData.IPN_EXISTING);
        assertEquals(existingIpnResult.getResults().getFirst().getIpn(), FilteringTestData.IPN_EXISTING,
                "IPN of returned part must match filter value");
        assertEquals(existingIpnResult.getResults().getFirst().getPk(),
                Integer.valueOf(FilteringTestData.IPN_EXISTING_PK),
                "pk of returned part must match expected pk");

        PaginatedResponse<Part> nonExistentIpnResult = partService.listParts(
                Map.of("IPN", FilteringTestData.IPN_NON_EXISTENT,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of(FilteringTestData.FILTER_PARAM_ASSEMBLY, FilteringTestData.FILTER_VALUE_TRUE, "limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int assemblyCount = assemblyOnly.getCount();

        PaginatedResponse<Part> activeOnly = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ACTIVE, FilteringTestData.FILTER_VALUE_TRUE, "limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int activeCount = activeOnly.getCount();

        PaginatedResponse<Part> combined = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ASSEMBLY, FilteringTestData.FILTER_VALUE_TRUE,
                       FilteringTestData.FILTER_PARAM_ACTIVE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("search", FilteringTestData.SEARCH_TERM_LOWER,
                       FilteringTestData.FILTER_PARAM_ASSEMBLY, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);
        int totalCount = totalPage.getCount();

        PaginatedResponse<Part> activeResult = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ACTIVE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        activeResult.getResults().forEach(p ->
                assertTrue(p.getActive(), "Part pk=" + p.getPk() + " must have active=true"));
        int activeCount = activeResult.getCount();

        PaginatedResponse<Part> inactiveResult = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_ACTIVE, FilteringTestData.FILTER_VALUE_FALSE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("ordering", FilteringTestData.ORDERING_NAME_ASC,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

        List<String> namesAsc = ascResult.getResults().stream()
                .map(Part::getName)
                .toList();

        for (int i = 0; i < namesAsc.size() - 1; i++) {
            assertTrue(namesAsc.get(i).compareToIgnoreCase(namesAsc.get(i + 1)) <= 0,
                    "Names must be in ascending order: '" + namesAsc.get(i)
                            + "' > '" + namesAsc.get(i + 1) + "'");
        }

        PaginatedResponse<Part> descResult = partService.listParts(
                Map.of("ordering", FilteringTestData.ORDERING_NAME_DESC,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

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
                Map.of("parent", FilteringTestData.PARENT_CATEGORY_PK,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        List<Integer> parents = parentResponse.getResults().stream()
                .map(PartCategory::getParent)
                .toList();
        parents.forEach(p ->
                assertEquals(p, Integer.valueOf(FilteringTestData.PARENT_CATEGORY_PK),
                        "All results must have parent=" + FilteringTestData.PARENT_CATEGORY_PK));

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
                Map.of("limit", FilteringTestData.LIMIT_UNPAGINATED, "search", FilteringTestData.SEARCH_TERM_LOWER), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.LIMIT_NEGATIVE_ONE, "search", FilteringTestData.SEARCH_TERM_LOWER), Role.ADMIN);
        negResponse.then().statusCode(HttpStatus.SC_OK);
        assertTrue(negResponse.asString().trim().startsWith("["),
                "Response for limit=-1 must be a JSON array");
        int negLength = negResponse.jsonPath().getList("$").size();

        Response zeroResponse = partService.listPartsRaw(
                Map.of("limit", FilteringTestData.LIMIT_UNPAGINATED, "search", FilteringTestData.SEARCH_TERM_LOWER), Role.ADMIN);
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
                Map.of("limit", FilteringTestData.SMALL_PAGE_LIMIT,
                       "offset", FilteringTestData.BEYOND_TOTAL_OFFSET), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.SMALL_PAGE_LIMIT,
                       "offset", 0), Role.ADMIN);

        List<Integer> page0Pks = page0.getResults().stream().map(Part::getPk).toList();

        PaginatedResponse<Part> negOffsetPage = partService.listParts(
                Map.of("limit", FilteringTestData.SMALL_PAGE_LIMIT,
                       "offset", -1), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.SINGLE_ITEM_LIMIT,
                       "search", FilteringTestData.SEARCH_TERM_LOWER), Role.ADMIN);
        int filteredCount = singleItem.getCount();

        assertTrue(filteredCount > 0, "search='" + FilteringTestData.SEARCH_TERM_LOWER + "' must return at least one part");

        PaginatedResponse<Part> largeResult = partService.listParts(
                Map.of("limit", FilteringTestData.LARGE_ENOUGH_LIMIT,
                       "search", FilteringTestData.SEARCH_TERM_LOWER), Role.ADMIN);

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
                Map.of("limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);
        int unfilteredCount = unfiltered.getCount();

        PaginatedResponse<Part> emptySearch = partService.listParts(
                Map.of("search", FilteringTestData.SEARCH_TERM_EMPTY,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

        assertEquals(emptySearch.getCount(), Integer.valueOf(unfilteredCount),
                "empty search must return same count as unfiltered request");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_014_virtualTrueFiltersToVirtualPartsOnly() {
        PaginatedResponse<Part> virtualTrue = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_VIRTUAL, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(virtualTrue.getCount() > 0, "count must be > 0 for virtual=true");
        virtualTrue.getResults().forEach(p ->
                assertTrue(p.getVirtual(), "Part pk=" + p.getPk() + " must have virtual=true"));

        PaginatedResponse<Part> virtualFalse = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_VIRTUAL, FilteringTestData.FILTER_VALUE_FALSE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        virtualFalse.getResults().forEach(p ->
                assertFalse(p.getVirtual(), "Part pk=" + p.getPk() + " must have virtual=false"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_015_trackableTrueFiltersToSerialTrackedPartsOnly() {
        PaginatedResponse<Part> trackableTrue = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_TRACKABLE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertTrue(trackableTrue.getCount() > 0, "count must be > 0 for trackable=true");
        trackableTrue.getResults().forEach(p ->
                assertTrue(p.getTrackable(), "Part pk=" + p.getPk() + " must have trackable=true"));

        PaginatedResponse<Part> trackableFalse = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_TRACKABLE, FilteringTestData.FILTER_VALUE_FALSE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        trackableFalse.getResults().forEach(p ->
                assertFalse(p.getTrackable(), "Part pk=" + p.getPk() + " must have trackable=false"));
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Boolean Filters")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_017_purchaseableAndSalableCombinedFiltersApplyAndLogic() {
        PaginatedResponse<Part> purchaseableOnly = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_PURCHASEABLE, FilteringTestData.FILTER_VALUE_TRUE, "limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int purchaseableCount = purchaseableOnly.getCount();

        PaginatedResponse<Part> salableOnly = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_SALABLE, FilteringTestData.FILTER_VALUE_TRUE, "limit", FilteringTestData.SINGLE_ITEM_LIMIT), Role.ADMIN);
        int salableCount = salableOnly.getCount();

        PaginatedResponse<Part> combined = partService.listParts(
                Map.of(FilteringTestData.FILTER_PARAM_PURCHASEABLE, FilteringTestData.FILTER_VALUE_TRUE,
                       FilteringTestData.FILTER_PARAM_SALABLE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
        PaginatedResponse<Part> matchingResult = partService.listParts(
                Map.of("IPN_regex", FilteringTestData.IPN_REGEX_PREFIX_RES,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        assertTrue(matchingResult.getCount() > 1,
                "count must be > 1 for IPN_regex=^RES");
        matchingResult.getResults().forEach(p ->
                assertTrue(p.getIpn() != null && p.getIpn().startsWith("RES"),
                        "Part pk=" + p.getPk() + " IPN '" + p.getIpn()
                                + "' must start with 'RES'"));

        PaginatedResponse<Part> noMatchResult = partService.listParts(
                Map.of("IPN_regex", FilteringTestData.IPN_REGEX_NO_MATCH,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        assertEquals(noMatchResult.getCount(), Integer.valueOf(0),
                "count must be 0 for non-matching IPN_regex");
    }

    @Test(groups = {"regression", "parts", "filtering"})
    @Story("Name Regex Filter")
    @Severity(SeverityLevel.NORMAL)
    public void tc_APFLT_020_nameRegexFiltersPartsWhoseNameStartsWithR() {
        PaginatedResponse<Part> result = partService.listParts(
                Map.of("name_regex", FilteringTestData.NAME_REGEX_PREFIX_R,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("created_after", FilteringTestData.DATE_RANGE_START,
                       "created_before", FilteringTestData.DATE_RANGE_END,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

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
                Map.of("created_after", FilteringTestData.DATE_FUTURE,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

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
                Map.of("category", FilteringTestData.CATEGORY_WITH_CHILD_PK,
                       FilteringTestData.FILTER_PARAM_CASCADE, FilteringTestData.FILTER_VALUE_TRUE,
                       "limit", FilteringTestData.LARGE_LIMIT), Role.ADMIN);

        assertTrue(cascadeResult.getCount() > 0, "count must be > 0 for cascade=true");

        List<Integer> categories = cascadeResult.getResults().stream()
                .map(Part::getCategory)
                .toList();

        assertTrue(categories.contains(FilteringTestData.CATEGORY_WITH_CHILD_PK),
                "Results must include parts from parent category pk="
                        + FilteringTestData.CATEGORY_WITH_CHILD_PK);
        assertTrue(categories.contains(FilteringTestData.CHILD_CATEGORY_PK),
                "Results must include parts from child category pk="
                        + FilteringTestData.CHILD_CATEGORY_PK);

        PaginatedResponse<Part> childOnlyResult = partService.listParts(
                Map.of("category", FilteringTestData.CHILD_CATEGORY_PK,
                       "limit", FilteringTestData.SMALL_PAGE_LIMIT), Role.ADMIN);

        childOnlyResult.getResults().forEach(p ->
                assertEquals(p.getCategory(), Integer.valueOf(FilteringTestData.CHILD_CATEGORY_PK),
                        "All results must belong to child category pk="
                                + FilteringTestData.CHILD_CATEGORY_PK));
    }

    @Test(groups = {"regression", "parts", "ordering"})
    @Story("Ordering")
    @Severity(SeverityLevel.MINOR)
    public void tc_APFLT_023_orderingInvalidFieldSilentlyReturnsHttp200WithDefaultOrder() {
        PaginatedResponse<Part> result = partService.listParts(
                Map.of("ordering", FilteringTestData.ORDERING_INVALID,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

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
                Map.of("ordering", FilteringTestData.ORDERING_STOCK_DESC,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

        List<Double> stockDesc = descResult.getResults().stream()
                .map(Part::getInStock)
                .toList();

        for (int i = 0; i < stockDesc.size() - 1; i++) {
            assertTrue(stockDesc.get(i) >= stockDesc.get(i + 1),
                    "in_stock must be descending: " + stockDesc.get(i)
                            + " < " + stockDesc.get(i + 1));
        }

        PaginatedResponse<Part> ascResult = partService.listParts(
                Map.of("ordering", FilteringTestData.ORDERING_STOCK_ASC,
                       "limit", FilteringTestData.PAGINATION_LIMIT), Role.ADMIN);

        List<Double> stockAsc = ascResult.getResults().stream()
                .map(Part::getInStock)
                .toList();

        for (int i = 0; i < stockAsc.size() - 1; i++) {
            assertTrue(stockAsc.get(i) <= stockAsc.get(i + 1),
                    "in_stock must be ascending: " + stockAsc.get(i)
                            + " > " + stockAsc.get(i + 1));
        }
    }
}
