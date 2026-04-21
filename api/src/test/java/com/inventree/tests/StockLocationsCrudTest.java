package com.inventree.tests;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.Part;
import com.inventree.model.PartListParams;
import com.inventree.model.PartRequest;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.StockItemDetail;
import com.inventree.model.StockItemRequest;
import com.inventree.model.StockLocationType;
import com.inventree.model.StockLocationDetail;
import com.inventree.model.StockLocationRequest;
import com.inventree.testdata.StockTestData;
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
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@Epic("Stock Management")
@Feature("Stock Locations CRUD")
public class StockLocationsCrudTest extends BaseTest {

    private final List<Integer> createdLocationIds = new ArrayList<>();
    private final List<Integer> createdLocationTypeIds = new ArrayList<>();
    private final List<Integer> createdStockItemIds = new ArrayList<>();
    private final List<Integer> createdPartIds = new ArrayList<>();

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdStockItemIds.forEach(id -> {
            try {
                stockService.deleteStockItem(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock item {}", id, t);
            }
        });
        createdStockItemIds.clear();

        createdLocationIds.forEach(id -> {
            try {
                stockService.deleteStockLocationRaw(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock location {}", id, t);
            }
        });
        createdLocationIds.clear();

        createdLocationTypeIds.forEach(id -> {
            try {
                stockService.deleteStockLocationTypeRaw(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock location type {}", id, t);
            }
        });
        createdLocationTypeIds.clear();

        createdPartIds.forEach(id -> {
            try {
                partService.patchPart(id, com.inventree.testdata.PartTestData.patchActiveOnly(false), Role.ADMIN);
                partService.deletePart(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting part {}", id, t);
            }
        });
        createdPartIds.clear();
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("List Locations")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_001_getStockLocationListReturnsPaginatedResultWithFullFieldSet() {
        Response response = stockService.listStockLocationsRaw(
                Map.of(StockTestData.QUERY_PARAM_LIMIT, StockTestData.DEFAULT_PAGE_LIMIT), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        assertTrue(response.jsonPath().getInt("count") > 0, "count must be > 0");
        List<Map<String, Object>> results = response.jsonPath().getList("results");
        assertNotNull(results, "results must not be null");
        assertFalse(results.isEmpty(), "results must not be empty");

        Map<String, Object> first = results.getFirst();
        assertNotNull(first.get("pk"), "pk must be present");
        assertNotNull(first.get("name"), "name must be present");
        assertNotNull(first.get("pathstring"), "pathstring must be present");
        assertTrue(first.containsKey("description"), "description must be present");
        assertTrue(first.containsKey("parent"), "parent must be present");
        assertTrue(first.containsKey("items"), "items must be present");
        assertTrue(first.containsKey("sublocations"), "sublocations must be present");
        assertTrue(first.containsKey("structural"), "structural must be present");
        assertTrue(first.containsKey("external"), "external must be present");
        assertTrue(first.containsKey("level"), "level must be present");
        assertTrue(first.containsKey("barcode_hash"), "barcode_hash must be present");

        assertFalse(((String) first.get("pathstring")).isEmpty(), "pathstring must be non-empty");
        assertNotNull(first.get("structural"), "structural must not be null");
        assertNotNull(first.get("external"), "external must not be null");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Get Location by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_002_getStockLocationByIdReturnsSingleLocationWithCorrectFields() {
        StockLocationDetail created = stockService.createStockLocation(
                StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-002", "GetById")),
                Role.ADMIN);
        int locationPk = created.getPk();
        createdLocationIds.add(locationPk);

        Response response = stockService.getStockLocationByIdRaw(locationPk, Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        StockLocationDetail location = response.as(StockLocationDetail.class);
        assertEquals(location.getPk(), Integer.valueOf(locationPk));
        assertNotNull(location.getName(), "name must not be null");
        assertNotNull(location.getPathstring(), "pathstring must not be null");
        assertNotNull(location.getLevel(), "level must not be null");
        assertNotNull(location.getItems(), "items must not be null");
        assertNotNull(location.getSublocations(), "sublocations must not be null");
        assertTrue(location.getLevel() >= 0, "level must be >= 0");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Location Tree")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_003_getStockLocationTreeReturnsLightweightTreeNodes() {
        Response response = stockService.getStockLocationTreeRaw(Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> tree = response.jsonPath().getList("$");
        assertNotNull(tree, "response must be an array");

        if (!tree.isEmpty()) {
            Map<String, Object> first = tree.getFirst();
            assertNotNull(first.get("pk"), "pk must be present in tree node");
            assertNotNull(first.get("name"), "name must be present in tree node");
            assertTrue(first.containsKey("parent"), "parent must be present in tree node");
            assertTrue(first.containsKey("structural"), "structural must be present in tree node");
            assertTrue(first.containsKey("sublocations"), "sublocations must be present in tree node");
        }
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Filter Top-Level Locations")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_004_getStockLocationListFiltersTopLevelLocations() {
        stockService.createStockLocation(
                StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-004", "Root")),
                Role.ADMIN).getPk();

        Response response = stockService.listStockLocationsRaw(
                Map.of(StockTestData.QUERY_PARAM_TOP_LEVEL, StockTestData.QUERY_VALUE_TRUE), Role.READER);
        response.then().statusCode(HttpStatus.SC_OK);

        PaginatedResponse<StockLocationDetail> paged = stockService.listStockLocations(
                Map.of(StockTestData.QUERY_PARAM_TOP_LEVEL, StockTestData.QUERY_VALUE_TRUE), Role.READER);
        for (StockLocationDetail loc : paged.getResults()) {
            assertNull(loc.getParent(), "top_level=true must return only locations with parent=null");
        }

        PaginatedResponse<StockLocationDetail> topLevel = stockService.listStockLocations(
                Map.of(StockTestData.QUERY_PARAM_TOP_LEVEL, StockTestData.QUERY_VALUE_TRUE), Role.ADMIN);
        topLevel.getResults().forEach(loc -> createdLocationIds.add(loc.getPk()));
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Filter Structural Locations")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_005_getStockLocationListFiltersStructuralLocations() {
        StockLocationDetail structural = stockService.createStockLocation(
                StockTestData.structuralLocation(
                        StockTestData.testLocationName("TC-ALCRUD-005", "Structural")),
                Role.ADMIN);
        createdLocationIds.add(structural.getPk());

        PaginatedResponse<StockLocationDetail> paged = stockService.listStockLocations(
                Map.of(StockTestData.QUERY_PARAM_STRUCTURAL, StockTestData.QUERY_VALUE_TRUE), Role.READER);

        assertFalse(paged.getResults().isEmpty(), "at least one structural location must exist");
        for (StockLocationDetail loc : paged.getResults()) {
            assertTrue(Boolean.TRUE.equals(loc.getStructural()),
                    "structural=true filter must return only structural locations");
        }
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Filter External Locations")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_006_getStockLocationListFiltersExternalLocations() {
        StockLocationDetail external = stockService.createStockLocation(
                StockTestData.externalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-006", "External")),
                Role.ADMIN);
        createdLocationIds.add(external.getPk());

        PaginatedResponse<StockLocationDetail> paged = stockService.listStockLocations(
                Map.of(StockTestData.QUERY_PARAM_EXTERNAL, StockTestData.QUERY_VALUE_TRUE), Role.READER);

        assertFalse(paged.getResults().isEmpty(), "at least one external location must exist");
        for (StockLocationDetail loc : paged.getResults()) {
            assertTrue(Boolean.TRUE.equals(loc.getExternal()),
                    "external=true filter must return only external locations");
        }
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Root Location")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_007_postStockLocationCreatesRootLocationWithRequiredFieldsOnly() {
        String name = StockTestData.testLocationName("TC-ALCRUD-007", "Root");
        Response createResponse = stockService.createStockLocationRaw(
                StockTestData.minimalLocation(name), Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        assertTrue(newPk > 0, "pk must be a positive integer");
        createdLocationIds.add(newPk);

        assertEquals(createResponse.jsonPath().getString("name"), name);
        assertNull(createResponse.jsonPath().get("parent"), "parent must be null for root location");
        assertEquals(createResponse.jsonPath().getInt("level"), 0);
        assertEquals(createResponse.jsonPath().getString("pathstring"), name);
        assertFalse(createResponse.jsonPath().getBoolean("structural"));
        assertFalse(createResponse.jsonPath().getBoolean("external"));
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Child Location")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_008_postStockLocationCreatesNestedChildLocation() {
        StockLocationDetail parent = stockService.createStockLocation(
                StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-008", "Parent")),
                Role.ADMIN);
        int parentPk = parent.getPk();
        createdLocationIds.add(parentPk);
        String parentPathstring = parent.getPathstring();
        int parentLevel = parent.getLevel();

        String childName = "Child Shelf";
        Response createResponse = stockService.createStockLocationRaw(
                StockTestData.locationWithParent(childName, parentPk, "Second-level shelf under parent"),
                Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int childPk = createResponse.jsonPath().getInt("pk");
        createdLocationIds.add(childPk);

        assertEquals(createResponse.jsonPath().getInt("parent"), parentPk);
        assertEquals(createResponse.jsonPath().getString("pathstring"), parentPathstring + "/" + childName);
        assertEquals(createResponse.jsonPath().getInt("level"), parentLevel + 1);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Structural Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_009_postStockLocationCreatesStructuralLocation() {
        String name = StockTestData.testLocationName("TC-ALCRUD-009", "Structural");
        Response createResponse = stockService.createStockLocationRaw(
                StockTestData.structuralLocation(name), Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        createdLocationIds.add(newPk);

        assertTrue(createResponse.jsonPath().getBoolean("structural"),
                "structural must be true");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create External Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_010_postStockLocationCreatesExternalLocation() {
        String name = StockTestData.testLocationName("TC-ALCRUD-010", "External");
        Response createResponse = stockService.createStockLocationRaw(
                StockTestData.externalLocation(name), Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        createdLocationIds.add(newPk);

        assertTrue(createResponse.jsonPath().getBoolean("external"),
                "external must be true");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Location with Type")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_011_postStockLocationCreatesLocationWithLocationType() {
        StockLocationType locType = stockService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALCRUD-011", "Type")),
                Role.ADMIN);
        int typePk = locType.getPk();
        createdLocationTypeIds.add(typePk);

        String name = StockTestData.testLocationName("TC-ALCRUD-011", "Typed");
        Response createResponse = stockService.createStockLocationRaw(
                StockTestData.locationWithType(name, typePk), Role.ADMIN);
        createResponse.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = createResponse.jsonPath().getInt("pk");
        createdLocationIds.add(newPk);

        assertEquals(createResponse.jsonPath().getInt("location_type"), typePk);
        assertNotNull(createResponse.jsonPath().get("location_type_detail"),
                "location_type_detail must be present");
        assertEquals(createResponse.jsonPath().getInt("location_type_detail.pk"), typePk);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Full Update Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_012_putStockLocationRenamesLocationAndUpdatesPathstring() {
        String originalName = StockTestData.testLocationName("TC-ALCRUD-012", "OldName");
        StockLocationDetail created = stockService.createStockLocation(
                StockTestData.minimalLocation(originalName), Role.ADMIN);
        int locationPk = created.getPk();
        createdLocationIds.add(locationPk);

        String newName = StockTestData.testLocationName("TC-ALCRUD-012", "NewName");
        Response updateResponse = stockService.listStockLocationsRaw(
                Map.of(StockTestData.QUERY_PARAM_LIMIT, 1), Role.READER);
        updateResponse.then().statusCode(HttpStatus.SC_OK);

        StockLocationDetail updated = stockService.updateStockLocation(locationPk,
                StockTestData.minimalLocation(newName), Role.ADMIN);

        assertEquals(updated.getName(), newName);
        assertTrue(updated.getPathstring().contains(newName),
                "pathstring must contain the new name");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Toggle Structural Flag")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_013_patchStockLocationTogglesStructuralFlag() {
        String name = StockTestData.testLocationName("TC-ALCRUD-013", "ToggleStructural");
        StockLocationDetail created = stockService.createStockLocation(
                StockTestData.minimalLocation(name), Role.ADMIN);
        int locationPk = created.getPk();
        createdLocationIds.add(locationPk);

        assertFalse(Boolean.TRUE.equals(created.getStructural()), "initial structural must be false");

        StockLocationDetail patched = stockService.patchStockLocation(locationPk,
                StockLocationRequest.builder().structural(true).build(), Role.ADMIN);
        assertTrue(Boolean.TRUE.equals(patched.getStructural()), "structural must be true after patch");

        int partPk = findActivePartPk();
        Response stockCreateResponse = stockService.createStockItemRaw(
                Map.of("part", partPk, "quantity", 1, "location", locationPk), Role.ADMIN);
        stockCreateResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Toggle External Flag")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_014_patchStockLocationTogglesExternalFlag() {
        String name = StockTestData.testLocationName("TC-ALCRUD-014", "ToggleExternal");
        StockLocationDetail created = stockService.createStockLocation(
                StockTestData.minimalLocation(name), Role.ADMIN);
        int locationPk = created.getPk();
        createdLocationIds.add(locationPk);

        StockLocationDetail patchedTrue = stockService.patchStockLocation(locationPk,
                StockLocationRequest.builder().external(true).build(), Role.ADMIN);
        assertTrue(Boolean.TRUE.equals(patchedTrue.getExternal()), "external must be true after patch");

        StockLocationDetail patchedFalse = stockService.patchStockLocation(locationPk,
                StockLocationRequest.builder().external(false).build(), Role.ADMIN);
        assertFalse(Boolean.TRUE.equals(patchedFalse.getExternal()), "external must be false after second patch");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Reparent Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_015_patchStockLocationReparentsToNewParent() {
        StockLocationDetail locA = stockService.createStockLocation(
                StockTestData.minimalLocation(StockTestData.testLocationName("TC-ALCRUD-015", "A")),
                Role.ADMIN);
        int locAPk = locA.getPk();
        createdLocationIds.add(locAPk);

        StockLocationDetail locB = stockService.createStockLocation(
                StockTestData.minimalLocation(StockTestData.testLocationName("TC-ALCRUD-015", "B")),
                Role.ADMIN);
        int locBPk = locB.getPk();
        createdLocationIds.add(locBPk);
        String locBName = locB.getName();

        StockLocationDetail locC = stockService.createStockLocation(
                StockTestData.locationWithParent(
                        StockTestData.testLocationName("TC-ALCRUD-015", "C"), locAPk, "child of A"),
                Role.ADMIN);
        int locCPk = locC.getPk();
        createdLocationIds.add(locCPk);

        StockLocationDetail reparented = stockService.patchStockLocation(locCPk,
                StockLocationRequest.builder().parent(locBPk).build(), Role.ADMIN);

        assertEquals(reparented.getParent(), Integer.valueOf(locBPk));
        assertTrue(reparented.getPathstring().startsWith(locBName),
                "pathstring must start with parent B name after reparenting");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Change Location Type")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_016_patchStockLocationChangesLocationType() {
        StockLocationType typeA = stockService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALCRUD-016", "TypeA")),
                Role.ADMIN);
        createdLocationTypeIds.add(typeA.getPk());

        StockLocationType typeB = stockService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALCRUD-016", "TypeB")),
                Role.ADMIN);
        int typeBPk = typeB.getPk();
        createdLocationTypeIds.add(typeBPk);

        StockLocationDetail location = stockService.createStockLocation(
                StockTestData.locationWithType(
                        StockTestData.testLocationName("TC-ALCRUD-016", "Loc"), typeA.getPk()),
                Role.ADMIN);
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        StockLocationDetail patched = stockService.patchStockLocation(locationPk,
                StockLocationRequest.builder().locationType(typeBPk).build(), Role.ADMIN);

        assertEquals(patched.getLocationType(), Integer.valueOf(typeBPk));
        assertNotNull(patched.getLocationTypeDetail(), "location_type_detail must not be null");
        assertEquals(patched.getLocationTypeDetail().getPk(), Integer.valueOf(typeBPk));
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Delete Empty Location")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_017_deleteStockLocationRemovesEmptyLocation() {
        String name = StockTestData.testLocationName("TC-ALCRUD-017", "ToDelete");
        StockLocationDetail created = stockService.createStockLocation(
                StockTestData.minimalLocation(name), Role.ADMIN);
        int locationPk = created.getPk();

        StockLocationDetail state = stockService.getStockLocationById(locationPk, Role.READER);
        assertEquals(state.getItems(), Integer.valueOf(0), "items must be 0");
        assertEquals(state.getSublocations(), Integer.valueOf(0), "sublocations must be 0");

        Response deleteResponse = stockService.deleteStockLocationRaw(locationPk, Role.ADMIN);
        deleteResponse.then().statusCode(HttpStatus.SC_NO_CONTENT);

        stockService.getStockLocationByIdRaw(locationPk, Role.READER).then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Delete Location with Stock Items - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_018_deleteStockLocationWithStockItemsReturns400() {
        String locationName = StockTestData.testLocationName("TC-ALCRUD-018", "WithItems");
        StockLocationDetail location = stockService.createStockLocation(
                StockTestData.minimalLocation(locationName), Role.ADMIN);
        int locationPk = location.getPk();
        createdLocationIds.add(locationPk);

        int partPk = findActivePartPk();
        StockItemDetail item = stockService.createStockItem(
                StockTestData.stockItemWithLocation(partPk, 5.0, locationPk), Role.ADMIN);
        createdStockItemIds.add(item.getPk());

        StockLocationDetail state = stockService.getStockLocationById(locationPk, Role.READER);
        assertTrue(state.getItems() > 0, "items must be > 0");

        Response deleteResponse = stockService.deleteStockLocationRaw(locationPk, Role.ADMIN);
        int statusCode = deleteResponse.statusCode();
        assertTrue(statusCode == HttpStatus.SC_BAD_REQUEST || statusCode == HttpStatus.SC_CONFLICT,
                "status must be 400 or 409, got: " + statusCode);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Delete Location with Children - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_019_deleteStockLocationWithChildrenReturns400() {
        StockLocationDetail parent = stockService.createStockLocation(
                StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-019", "Parent")),
                Role.ADMIN);
        int parentPk = parent.getPk();
        createdLocationIds.add(parentPk);

        StockLocationDetail child = stockService.createStockLocation(
                StockTestData.locationWithParent(
                        StockTestData.testLocationName("TC-ALCRUD-019", "Child"),
                        parentPk, "child location"),
                Role.ADMIN);
        createdLocationIds.add(child.getPk());

        StockLocationDetail state = stockService.getStockLocationById(parentPk, Role.READER);
        assertTrue(state.getSublocations() > 0, "sublocations must be > 0");

        Response deleteResponse = stockService.deleteStockLocationRaw(parentPk, Role.ADMIN);
        int statusCode = deleteResponse.statusCode();
        assertTrue(statusCode == HttpStatus.SC_BAD_REQUEST || statusCode == HttpStatus.SC_CONFLICT,
                "status must be 400 or 409, got: " + statusCode);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Location - Negative: Missing Name")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_020_postStockLocationWithMissingNameReturns400() {
        Response response = stockService.createStockLocationRaw(
                Map.of("description", "No name supplied"), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("name"), "error must reference 'name' field");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Location - Negative: Non-existent Parent")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_021_postStockLocationWithNonExistentParentReturns400() {
        Response response = stockService.createStockLocationRaw(
                Map.of("name", "Orphan Location", "parent", StockTestData.NONEXISTENT_PK), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        assertNotNull(response.jsonPath().get("parent"), "error must reference 'parent' field");
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Patch Location - Negative: Cyclic Parent")
    @Severity(SeverityLevel.MINOR)
    public void tc_ALCRUD_022_patchStockLocationWithCyclicParentReturns400() {
        StockLocationDetail locA = stockService.createStockLocation(
                StockTestData.minimalLocation(StockTestData.testLocationName("TC-ALCRUD-022", "A")),
                Role.ADMIN);
        int locAPk = locA.getPk();
        createdLocationIds.add(locAPk);

        StockLocationDetail locB = stockService.createStockLocation(
                StockTestData.locationWithParent(
                        StockTestData.testLocationName("TC-ALCRUD-022", "B"),
                        locAPk, "child of A"),
                Role.ADMIN);
        int locBPk = locB.getPk();
        createdLocationIds.add(locBPk);

        Response patchResponse = stockService.patchStockLocationRaw(locAPk,
                StockLocationRequest.builder().parent(locBPk).build(), Role.ADMIN);
        patchResponse.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Create Location - Security: No Auth")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALCRUD_023_postStockLocationWithoutAuthenticationReturns401Or403() {
        Response response = stockService.createStockLocationUnauthenticated(
                Map.of("name", "Unauthorized Location"));

        int status = response.statusCode();
        assertTrue(status == HttpStatus.SC_UNAUTHORIZED || status == HttpStatus.SC_FORBIDDEN,
                "status must be 401 or 403, got: " + status);
    }

    @Test(groups = {"regression", "stock-locations"})
    @Story("Search Locations by Name")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALCRUD_024_getStockLocationListSearchFilterMatchesNameAndPathstring() {
        StockLocationDetail shelfLoc = stockService.createStockLocation(
                StockTestData.minimalLocation(
                        StockTestData.testLocationName("TC-ALCRUD-024", "Shelf")),
                Role.ADMIN);
        createdLocationIds.add(shelfLoc.getPk());

        PaginatedResponse<StockLocationDetail> results = stockService.listStockLocations(
                Map.of(StockTestData.QUERY_PARAM_SEARCH, StockTestData.LOCATION_SEARCH_TERM), Role.READER);

        assertFalse(results.getResults().isEmpty(), "search must return at least one result");
        for (StockLocationDetail loc : results.getResults()) {
            String name = loc.getName() != null ? loc.getName().toLowerCase() : "";
            String pathstring = loc.getPathstring() != null ? loc.getPathstring().toLowerCase() : "";
            assertTrue(name.contains(StockTestData.LOCATION_SEARCH_TERM.toLowerCase())
                            || pathstring.contains(StockTestData.LOCATION_SEARCH_TERM.toLowerCase()),
                    "result name or pathstring must contain search term: " + StockTestData.LOCATION_SEARCH_TERM);
        }
    }

    private int findActivePartPk() {
        PaginatedResponse<Part> listing = partService.listParts(
                PartListParams.builder().limit(StockTestData.DEFAULT_PAGE_LIMIT).build(), Role.ADMIN);
        return listing.getResults().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActive()) && !Boolean.TRUE.equals(p.getVirtual()))
                .findFirst()
                .orElseGet(() -> {
                    Part created = partService.createPart(
                            PartRequest.builder()
                                    .name(StockTestData.testStockItemName("ALCRUD", "SetupPart"))
                                    .active(true)
                                    .build(),
                            Role.ADMIN);
                    createdPartIds.add(created.getPk());
                    return created;
                }).getPk();
    }
}
