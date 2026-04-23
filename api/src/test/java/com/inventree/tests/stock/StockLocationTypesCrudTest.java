package com.inventree.tests.stock;

import com.inventree.auth.Role;
import com.inventree.base.BaseTest;
import com.inventree.model.StockLocationDetail;
import com.inventree.model.StockLocationType;
import com.inventree.model.StockLocationTypeRequest;
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
import static org.testng.Assert.assertTrue;

@Epic("Stock Management")
@Feature("Stock Location Types CRUD")
public class StockLocationTypesCrudTest extends BaseTest {

    private final List<Integer> createdLocationTypeIds = new ArrayList<>();
    private final List<Integer> createdLocationIds = new ArrayList<>();

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        createdLocationIds.forEach(id -> {
            try {
                stockLocationService.deleteStockLocationRaw(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock location {}", id, t);
            }
        });
        createdLocationIds.clear();

        createdLocationTypeIds.forEach(id -> {
            try {
                stockLocationService.deleteStockLocationTypeRaw(id, Role.ADMIN);
            } catch (Throwable t) {
                log.error("Error deleting stock location type {}", id, t);
            }
        });
        createdLocationTypeIds.clear();
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("List Location Types")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALTCRUD_001_getStockLocationTypeListReturnsArray() {
        StockLocationType seed = stockLocationService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-001", "Seed")),
                Role.ADMIN);
        createdLocationTypeIds.add(seed.getPk());

        List<StockLocationType> types = stockLocationService.listStockLocationTypes(Role.READER);
        assertNotNull(types, "list response must not be null");
        assertFalse(types.isEmpty(), "list must contain at least the seeded type");

        StockLocationType first = types.getFirst();
        assertNotNull(first.getPk(), "pk must be present");
        assertNotNull(first.getName(), "name must be present");
        assertNotNull(first.getLocationCount(), "location_count must be present");
        assertTrue(first.getLocationCount() >= 0, "location_count must be >= 0");
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Get Location Type by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALTCRUD_002_getStockLocationTypeByIdReturnsSingleType() {
        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-002", "GetById")),
                Role.ADMIN);
        int typePk = created.getPk();
        createdLocationTypeIds.add(typePk);

        StockLocationType fetched = stockLocationService.getStockLocationTypeById(typePk, Role.READER);
        assertEquals(fetched.getPk(), Integer.valueOf(typePk));
        assertNotNull(fetched.getName(), "name must not be null");
        assertFalse(fetched.getName().isEmpty(), "name must not be empty");
        assertTrue(fetched.getLocationCount() >= 0, "location_count must be >= 0");
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Create Location Type with Required Fields Only")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALTCRUD_003_postStockLocationTypeCreatesWithRequiredFieldsOnly() {
        String name = StockTestData.testLocationTypeName("TC-ALTCRUD-003", "Minimal");
        Response response = stockLocationService.createStockLocationTypeRaw(
                StockTestData.minimalLocationType(name), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_CREATED);

        int newPk = response.jsonPath().getInt("pk");
        assertTrue(newPk > 0, "pk must be positive");
        createdLocationTypeIds.add(newPk);

        assertEquals(response.jsonPath().getString("name"), name);
        assertEquals(response.jsonPath().getInt("location_count"), 0);
        assertTrue(response.jsonPath().getMap("$").containsKey("description"),
                "description must be present");
        assertTrue(response.jsonPath().getMap("$").containsKey("icon"),
                "icon must be present");
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Create Location Type with All Fields")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_004_postStockLocationTypeCreatesWithAllFields() {
        String name = StockTestData.testLocationTypeName("TC-ALTCRUD-004", "Full");
        String description = "Large storage bin for bulk components";

        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.fullLocationType(name, description, StockTestData.ICON_BOX_OUTLINE),
                Role.ADMIN);
        createdLocationTypeIds.add(created.getPk());

        assertEquals(created.getName(), name);
        assertEquals(created.getDescription(), description);
        assertEquals(created.getIcon(), StockTestData.ICON_BOX_OUTLINE);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Full Update Location Type")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_005_putStockLocationTypeReplacesAllWritableFields() {
        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-005", "Original")),
                Role.ADMIN);
        int typePk = created.getPk();
        createdLocationTypeIds.add(typePk);

        String newName = StockTestData.testLocationTypeName("TC-ALTCRUD-005", "Updated");
        StockLocationTypeRequest update = StockLocationTypeRequest.builder()
                .name(newName)
                .description("Updated description")
                .icon(StockTestData.ICON_BOOKMARK_FILLED)
                .build();

        StockLocationType updated = stockLocationService.updateStockLocationType(typePk, update, Role.ADMIN);

        assertEquals(updated.getName(), newName);
        assertEquals(updated.getDescription(), "Updated description");
        assertEquals(updated.getIcon(), StockTestData.ICON_BOOKMARK_FILLED);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Partial Update Description")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_006_patchStockLocationTypeUpdatesDescriptionOnly() {
        String originalName = StockTestData.testLocationTypeName("TC-ALTCRUD-006", "KeepName");
        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.fullLocationType(originalName, "Original description",
                        StockTestData.ICON_BOX_OUTLINE),
                Role.ADMIN);
        int typePk = created.getPk();
        createdLocationTypeIds.add(typePk);

        StockLocationType patched = stockLocationService.patchStockLocationType(typePk,
                StockLocationTypeRequest.builder().description("Patched description only").build(),
                Role.ADMIN);

        assertEquals(patched.getDescription(), "Patched description only");
        assertEquals(patched.getName(), originalName);
        assertEquals(patched.getIcon(), StockTestData.ICON_BOX_OUTLINE);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Partial Update Icon")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_007_patchStockLocationTypeUpdatesIcon() {
        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.fullLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-007", "IconSwap"),
                        "icon patch target", StockTestData.ICON_BOX_OUTLINE),
                Role.ADMIN);
        int typePk = created.getPk();
        createdLocationTypeIds.add(typePk);

        StockLocationType patched = stockLocationService.patchStockLocationType(typePk,
                StockLocationTypeRequest.builder().icon(StockTestData.ICON_ARCHIVE_OUTLINE).build(),
                Role.ADMIN);

        assertEquals(patched.getIcon(), StockTestData.ICON_ARCHIVE_OUTLINE);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Delete Unreferenced Location Type")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_008_deleteStockLocationTypeRemovesUnreferencedType() {
        StockLocationType created = stockLocationService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-008", "ToDelete")),
                Role.ADMIN);
        int typePk = created.getPk();

        StockLocationType state = stockLocationService.getStockLocationTypeById(typePk, Role.READER);
        assertEquals(state.getLocationCount(), Integer.valueOf(0),
                "location_count must be 0 before delete");

        Response deleteResponse = stockLocationService.deleteStockLocationTypeRaw(typePk, Role.ADMIN);
        deleteResponse.then().statusCode(HttpStatus.SC_NO_CONTENT);

        stockLocationService.getStockLocationTypeByIdRaw(typePk, Role.READER).then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Delete Location Type Referenced by Locations")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_009_deleteStockLocationTypeReferencedByLocations() {
        StockLocationType type = stockLocationService.createStockLocationType(
                StockTestData.minimalLocationType(
                        StockTestData.testLocationTypeName("TC-ALTCRUD-009", "InUse")),
                Role.ADMIN);
        int typePk = type.getPk();
        createdLocationTypeIds.add(typePk);

        StockLocationDetail location = stockLocationService.createStockLocation(
                StockTestData.locationWithType(
                        StockTestData.testLocationName("TC-ALTCRUD-009", "WithType"), typePk),
                Role.ADMIN);
        createdLocationIds.add(location.getPk());

        StockLocationType state = stockLocationService.getStockLocationTypeById(typePk, Role.READER);
        assertTrue(state.getLocationCount() > 0, "location_count must be > 0");

        Response deleteResponse = stockLocationService.deleteStockLocationTypeRaw(typePk, Role.ADMIN);
        int status = deleteResponse.statusCode();
        assertTrue(status == HttpStatus.SC_BAD_REQUEST
                        || status == HttpStatus.SC_CONFLICT
                        || status == HttpStatus.SC_NO_CONTENT,
                "status must be 400, 409 or 204 (ASSUMED), got: " + status);

        if (status == HttpStatus.SC_NO_CONTENT) {
            createdLocationTypeIds.remove(Integer.valueOf(typePk));
            StockLocationDetail afterDelete = stockLocationService.getStockLocationById(
                    location.getPk(), Role.READER);
            assertTrue(afterDelete.getLocationType() == null
                            || !afterDelete.getLocationType().equals(typePk),
                    "location.location_type must be cleared when type delete succeeds");
        }
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Create Location Type - Negative: Missing Name")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALTCRUD_010_postStockLocationTypeWithMissingNameReturns400() {
        Response response = stockLocationService.createStockLocationTypeRaw(
                Map.of("description", "No name provided"), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().get("name"),
                "error must reference 'name' field");
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Create Location Type - Negative: Name Over 100 Chars")
    @Severity(SeverityLevel.MINOR)
    public void tc_ALTCRUD_011_postStockLocationTypeWithOverlongNameReturns400() {
        Response response = stockLocationService.createStockLocationTypeRaw(
                Map.of("name", StockTestData.LOC_TYPE_NAME_101_CHARS), Role.ADMIN);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        assertNotNull(response.jsonPath().get("name"),
                "error must reference 'name' field");
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Get Location Type - Negative: Non-existent PK")
    @Severity(SeverityLevel.NORMAL)
    public void tc_ALTCRUD_012_getStockLocationTypeWithNonExistentPkReturns404() {
        Response response = stockLocationService.getStockLocationTypeByIdRaw(
                StockTestData.NONEXISTENT_PK, Role.READER);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {"regression", "stock-location-types"})
    @Story("Create Location Type - Security: No Auth")
    @Severity(SeverityLevel.CRITICAL)
    public void tc_ALTCRUD_013_postStockLocationTypeWithoutAuthenticationReturns401Or403() {
        Response response = stockLocationService.createStockLocationTypeUnauthenticated(
                Map.of("name", "Unauthorized Type"));
        int status = response.statusCode();
        assertTrue(status == HttpStatus.SC_UNAUTHORIZED || status == HttpStatus.SC_FORBIDDEN,
                "status must be 401 or 403, got: " + status);
    }

}
