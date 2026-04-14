package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.Part;
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

    public Part getPartById(int id, Role role) {
        Response response = executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, Part.class);
    }

    public Response getPartByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.PARTS_ENDPOINT + id + "/", role);
    }

    public PaginatedResponse<Part> listParts(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.PARTS_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<Part>>() {});
    }

    public PaginatedResponse<Part> listParts(Role role) {
        return listParts(null, role);
    }

    public Response listPartsRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.PARTS_ENDPOINT, role, queryParams);
    }

    public Response listPartsRaw(Role role) {
        return listPartsRaw(null, role);
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
