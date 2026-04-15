package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
import com.inventree.model.PartDetailParams;
import com.inventree.model.PartListParams;
import com.inventree.model.PartRequest;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.Map;

public class PartService extends BaseClient {

    public Part createPart(PartRequest request, Role role) {
        Response response = executePost(ApiConstants.PARTS_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, Part.class);
    }

    public Response createPartRaw(PartRequest request, Role role) {
        return executePost(ApiConstants.PARTS_ENDPOINT, role, request);
    }

    public Response createPartRaw(Map<String, Object> payload, Role role) {
        return executePost(ApiConstants.PARTS_ENDPOINT, role, payload);
    }

    public Part getPartById(int id, Role role) {
        Response response = executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, Part.class);
    }

    public Response getPartByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role);
    }

    public Response getPartByIdRaw(int id, Role role, Map<String, Object> queryParams) {
        return executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role, queryParams);
    }

    public Response getPartByIdRaw(int id, Role role, PartDetailParams params) {
        return executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role, params);
    }

    public PaginatedResponse<Part> listParts(PartListParams params, Role role) {
        Response response = executeGet(ApiConstants.PARTS_ENDPOINT, role, params);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<Part>>() {});
    }

    public PaginatedResponse<Part> listParts(Role role) {
        return listParts(PartListParams.builder().build(), role);
    }

    public Response listPartsRaw(PartListParams params, Role role) {
        return executeGet(ApiConstants.PARTS_ENDPOINT, role, params);
    }

    public Response listPartsRaw(Role role) {
        return listPartsRaw(PartListParams.builder().build(), role);
    }

    public Part updatePart(int id, PartRequest request, Role role) {
        Response response = executePut(ApiConstants.PARTS_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, Part.class);
    }

    public Part patchPart(int id, PartRequest request, Role role) {
        Response response = executePatch(ApiConstants.PARTS_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, Part.class);
    }

    public void deletePart(int id, Role role) {
        Response response = executeDelete(ApiConstants.PARTS_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deletePartRaw(int id, Role role) {
        return executeDelete(ApiConstants.PARTS_ENDPOINT + id + "/", role);
    }
}
