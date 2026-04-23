package com.inventree.service;

import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.util.ApiConstants;
import io.restassured.response.Response;

import java.util.Map;

public class StockAdjustmentService extends BaseClient {

    public Response stockAdd(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_ADD_ENDPOINT, role, payload);
    }

    public Response stockRemove(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_REMOVE_ENDPOINT, role, payload);
    }

    public Response stockRemoveUnauthenticated(Map<String, Object> payload) {
        return executePostNoAuth(ApiConstants.STOCK_REMOVE_ENDPOINT, payload);
    }

    public Response stockCount(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_COUNT_ENDPOINT, role, payload);
    }

    public Response stockTransfer(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_TRANSFER_ENDPOINT, role, payload);
    }

    public Response stockChangeStatus(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_CHANGE_STATUS_ENDPOINT, role, payload);
    }

    public Response stockAssign(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_ASSIGN_ENDPOINT, role, payload);
    }

    public Response stockMerge(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_MERGE_ENDPOINT, role, payload);
    }

    public Response stockReturn(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.STOCK_RETURN_ENDPOINT, role, payload);
    }
}
