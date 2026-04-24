package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.PartInternalPrice;
import com.inventree.model.PartPricing;
import com.inventree.model.PartSalePrice;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.Map;

public class PricingService extends BaseClient {

    public PaginatedResponse<PartInternalPrice> listInternalPrices(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.INTERNAL_PRICE_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<PartInternalPrice>>() {});
    }

    public PartInternalPrice getInternalPriceById(int id, Role role) {
        Response response = executeGet(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartInternalPrice.class);
    }

    public Response getInternalPriceByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role);
    }

    public PartInternalPrice createInternalPrice(Map<String, Object> payload, Role role) {
        Response response = executePost(ApiConstants.INTERNAL_PRICE_ENDPOINT, role, payload);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, PartInternalPrice.class);
    }

    public Response createInternalPriceRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.INTERNAL_PRICE_ENDPOINT, role, payload);
    }

    public PartInternalPrice patchInternalPrice(int id, Map<String, Object> payload, Role role) {
        Response response = executePatch(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role, payload);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartInternalPrice.class);
    }

    public Response patchInternalPriceRaw(int id, Map<String, Object> payload, Role role) {
        return executePatch(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role, payload);
    }

    public void deleteInternalPrice(int id, Role role) {
        Response response = executeDelete(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteInternalPriceRaw(int id, Role role) {
        return executeDelete(ApiConstants.INTERNAL_PRICE_ENDPOINT + id + "/", role);
    }

    public PaginatedResponse<PartSalePrice> listSalePrices(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.SALE_PRICE_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<PartSalePrice>>() {});
    }

    public PartSalePrice getSalePriceById(int id, Role role) {
        Response response = executeGet(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartSalePrice.class);
    }

    public Response getSalePriceByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role);
    }

    public PartSalePrice createSalePrice(Map<String, Object> payload, Role role) {
        Response response = executePost(ApiConstants.SALE_PRICE_ENDPOINT, role, payload);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, PartSalePrice.class);
    }

    public Response createSalePriceRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.SALE_PRICE_ENDPOINT, role, payload);
    }

    public PartSalePrice patchSalePrice(int id, Map<String, Object> payload, Role role) {
        Response response = executePatch(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role, payload);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartSalePrice.class);
    }

    public Response patchSalePriceRaw(int id, Map<String, Object> payload, Role role) {
        return executePatch(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role, payload);
    }

    public void deleteSalePrice(int id, Role role) {
        Response response = executeDelete(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteSalePriceRaw(int id, Role role) {
        return executeDelete(ApiConstants.SALE_PRICE_ENDPOINT + id + "/", role);
    }

    public PartPricing getAggregatePricing(int partId, Role role) {
        Response response = executeGet(ApiConstants.PARTS_ENDPOINT + partId + "/pricing/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartPricing.class);
    }

    public PartPricing patchAggregatePricing(int partId, Map<String, Object> payload, Role role) {
        Response response = executePatch(ApiConstants.PARTS_ENDPOINT + partId + "/pricing/", role, payload);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartPricing.class);
    }

    public Response patchAggregatePricingRaw(int partId, Map<String, Object> payload, Role role) {
        return executePatch(ApiConstants.PARTS_ENDPOINT + partId + "/pricing/", role, payload);
    }
}

