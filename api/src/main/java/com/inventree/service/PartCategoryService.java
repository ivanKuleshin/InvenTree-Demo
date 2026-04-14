package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.PartCategory;
import com.inventree.model.PartCategoryRequest;
import com.inventree.model.PaginatedResponse;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.Map;

public class PartCategoryService extends BaseClient {

    public PartCategory createCategory(PartCategoryRequest request, Role role) {
        Response response = executePost(ApiConstants.CATEGORIES_ENDPOINT, role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_CREATED);
        return ResponseValidator.deserialize(response, PartCategory.class);
    }

    public Response createCategoryRaw(PartCategoryRequest request, Role role) {
        return executePost(ApiConstants.CATEGORIES_ENDPOINT, role, request);
    }

    public PartCategory getCategoryById(int id, Role role) {
        Response response = executeGet(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartCategory.class);
    }

    public Response getCategoryByIdRaw(int id, Role role) {
        return executeGet(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role);
    }

    public Response getCategoryByIdRaw(int id, Role role, Map<String, Object> queryParams) {
        return executeGet(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role, queryParams);
    }

    public PaginatedResponse<PartCategory> listCategories(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.CATEGORIES_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response,
                new TypeReference<PaginatedResponse<PartCategory>>() {});
    }

    public PaginatedResponse<PartCategory> listCategories(Role role) {
        return listCategories(null, role);
    }

    public Response listCategoriesRaw(Map<String, Object> queryParams, Role role) {
        return executeGet(ApiConstants.CATEGORIES_ENDPOINT, role, queryParams);
    }

    public PartCategory updateCategory(int id, PartCategoryRequest request, Role role) {
        Response response = executePut(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartCategory.class);
    }

    public PartCategory patchCategory(int id, PartCategoryRequest request, Role role) {
        Response response = executePatch(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role, request);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, PartCategory.class);
    }

    public void deleteCategory(int id, Role role) {
        Response response = executeDelete(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }

    public Response deleteCategoryRaw(int id, Role role) {
        return executeDelete(ApiConstants.CATEGORIES_ENDPOINT + id + "/", role);
    }
}
