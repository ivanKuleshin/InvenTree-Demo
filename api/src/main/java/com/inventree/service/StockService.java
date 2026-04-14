package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.StockItem;
import com.inventree.model.StockLocation;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class StockService extends BaseClient {

    public List<StockItem> listStockItems(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.STOCK_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<List<StockItem>>() {});
    }

    public void deleteStockItem(int id, Role role) {
        Response response = executeDelete(ApiConstants.STOCK_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public PaginatedResponse<StockLocation> listStockLocations(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.STOCK_LOCATION_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<StockLocation>>() {});
    }
}
