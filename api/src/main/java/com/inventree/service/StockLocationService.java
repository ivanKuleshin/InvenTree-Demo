package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.StockLocationDetail;
import com.inventree.model.StockLocationRequest;
import com.inventree.model.StockLocationType;
import com.inventree.model.StockLocationTypeRequest;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class StockLocationService extends BaseClient {

    public PaginatedResponse<StockLocationDetail> listStockLocations(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.STOCK_LOCATION_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response,
                new TypeReference<PaginatedResponse<StockLocationDetail>>() {});
    }

    public Response listStockLocationsRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.STOCK_LOCATION_ENDPOINT, role, queryParams);
    }

    public StockLocationDetail getStockLocationById(int id, Role role) {
        Response response = executeGet(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationDetail.class);
    }

    public Response getStockLocationByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role);
    }

    public Response getStockLocationTreeRaw(Role role) {
        return executeGet(ApiConstants.STOCK_LOCATION_TREE_ENDPOINT, role);
    }

    public StockLocationDetail createStockLocation(StockLocationRequest request, Role role) {
        Response response = executePost(ApiConstants.STOCK_LOCATION_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, StockLocationDetail.class);
    }

    public Response createStockLocationRaw(StockLocationRequest request, Role role) {
        return executePost(ApiConstants.STOCK_LOCATION_ENDPOINT, role, request);
    }

    public Response createStockLocationRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_LOCATION_ENDPOINT, role, payload);
    }

    public Response createStockLocationUnauthenticated(Map<String, Object> payload) {
        return executePostNoAuth(ApiConstants.STOCK_LOCATION_ENDPOINT, payload);
    }

    public StockLocationDetail updateStockLocation(int id, StockLocationRequest request, Role role) {
        Response response = executePut(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationDetail.class);
    }

    public StockLocationDetail patchStockLocation(int id, StockLocationRequest request, Role role) {
        Response response = executePatch(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationDetail.class);
    }

    public Response patchStockLocationRaw(int id, StockLocationRequest request, Role role) {
        return executePatch(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role, request);
    }

    public void deleteStockLocation(int id, Role role) {
        Response response = executeDelete(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteStockLocationRaw(int id, Role role) {
        return executeDelete(ApiConstants.STOCK_LOCATION_ENDPOINT + id + "/", role);
    }

    public List<StockLocationType> listStockLocationTypes(Role role) {
        Response response = executeGet(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT, role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<List<StockLocationType>>() {});
    }

    public Response getStockLocationTypeByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role);
    }

    public StockLocationType getStockLocationTypeById(int id, Role role) {
        Response response = executeGet(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationType.class);
    }

    public StockLocationType createStockLocationType(StockLocationTypeRequest request, Role role) {
        Response response = executePost(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, StockLocationType.class);
    }

    public Response createStockLocationTypeRaw(StockLocationTypeRequest request, Role role) {
        return executePost(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT, role, request);
    }

    public Response createStockLocationTypeRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT, role, payload);
    }

    public Response createStockLocationTypeUnauthenticated(Map<String, Object> payload) {
        return executePostNoAuth(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT, payload);
    }

    public StockLocationType updateStockLocationType(int id, StockLocationTypeRequest request, Role role) {
        Response response = executePut(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationType.class);
    }

    public StockLocationType patchStockLocationType(int id, StockLocationTypeRequest request, Role role) {
        Response response = executePatch(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockLocationType.class);
    }

    public void deleteStockLocationType(int id, Role role) {
        Response response = executeDelete(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteStockLocationTypeRaw(int id, Role role) {
        return executeDelete(ApiConstants.STOCK_LOCATION_TYPE_ENDPOINT + id + "/", role);
    }
}
