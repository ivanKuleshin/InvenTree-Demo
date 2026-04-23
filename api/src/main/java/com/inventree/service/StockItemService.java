package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.StockItem;
import com.inventree.model.StockItemDetail;
import com.inventree.model.StockItemRequest;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class StockItemService extends BaseClient {

    public List<StockItem> listStockItems(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.STOCK_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<List<StockItem>>() {});
    }

    public PaginatedResponse<StockItemDetail> listStockItemsPaginated(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.STOCK_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<StockItemDetail>>() {});
    }

    public Response listStockItemsRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.STOCK_ENDPOINT, role, queryParams);
    }

    public StockItemDetail getStockItemById(int id, Role role) {
        Response response = executeGet(ApiConstants.STOCK_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockItemDetail.class);
    }

    public Response getStockItemByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.STOCK_ENDPOINT + id + "/", role);
    }

    public StockItemDetail createStockItem(StockItemRequest request, Role role) {
        Response response = executePost(ApiConstants.STOCK_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        List<StockItemDetail> items = ResponseValidator.deserialize(response, new TypeReference<List<StockItemDetail>>() {});
        return items.getFirst();
    }

    public Response createStockItemRaw(StockItemRequest request, Role role) {
        return executePost(ApiConstants.STOCK_ENDPOINT, role, request);
    }

    public Response createStockItemRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_ENDPOINT, role, payload);
    }

    public StockItemDetail updateStockItem(int id, StockItemRequest request, Role role) {
        Response response = executePut(ApiConstants.STOCK_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockItemDetail.class);
    }

    public Response updateStockItemRaw(int id, StockItemRequest request, Role role) {
        return executePut(ApiConstants.STOCK_ENDPOINT + id + "/", role, request);
    }

    public StockItemDetail patchStockItem(int id, StockItemRequest request, Role role) {
        Response response = executePatch(ApiConstants.STOCK_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, StockItemDetail.class);
    }

    public Response patchStockItemRaw(int id, StockItemRequest request, Role role) {
        return executePatch(ApiConstants.STOCK_ENDPOINT + id + "/", role, request);
    }

    public void deleteStockItem(int id, Role role) {
        Response response = executeDelete(ApiConstants.STOCK_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteStockItemRaw(int id, Role role) {
        return executeDelete(ApiConstants.STOCK_ENDPOINT + id + "/", role);
    }

    public Response getStockTrackingRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.STOCK_TRACK_ENDPOINT, role, queryParams);
    }
}
