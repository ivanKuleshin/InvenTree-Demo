package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.PartTestTemplate;
import com.inventree.model.PartTestTemplateRequest;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.Map;

public class PartTestTemplateService extends BaseClient {

    public PartTestTemplate createTemplate(PartTestTemplateRequest request, Role role) {
        Response response = executePost(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, PartTestTemplate.class);
    }

    public Response createTemplateRaw(PartTestTemplateRequest request, Role role) {
        return executePost(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT, role, request);
    }

    public PartTestTemplate getTemplateById(int id, Role role) {
        Response response = executeGet(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartTestTemplate.class);
    }

    public Response getTemplateByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role);
    }

    public PaginatedResponse<PartTestTemplate> listTemplates(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<PartTestTemplate>>() {});
    }

    public Response listTemplatesRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT, role, queryParams);
    }

    public PartTestTemplate updateTemplate(int id, PartTestTemplateRequest request, Role role) {
        Response response = executePut(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartTestTemplate.class);
    }

    public PartTestTemplate patchTemplate(int id, PartTestTemplateRequest request, Role role) {
        Response response = executePatch(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartTestTemplate.class);
    }

    public void deleteTemplate(int id, Role role) {
        Response response = executeDelete(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteTemplateRaw(int id, Role role) {
        return executeDelete(ApiConstants.PART_TEST_TEMPLATE_ENDPOINT + id + "/", role);
    }
}
