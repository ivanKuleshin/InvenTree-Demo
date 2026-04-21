package com.inventree.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inventree.auth.Role;
import com.inventree.client.BaseClient;
import com.inventree.model.Company;
import com.inventree.model.PaginatedResponse;
import com.inventree.model.SupplierPart;
import com.inventree.util.ApiConstants;
import com.inventree.util.HttpStatus;
import com.inventree.util.ResponseValidator;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class CompanyService extends BaseClient {

    public PaginatedResponse<Company> listCompanies(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.COMPANY_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<PaginatedResponse<Company>>() {});
    }

    public PaginatedResponse<Company> listCompanies(Role role) {
        return listCompanies(null, role);
    }

    public List<SupplierPart> listSupplierParts(Map<String, Object> queryParams, Role role) {
        Response response = executeGet(ApiConstants.SUPPLIER_PART_ENDPOINT, role, queryParams);
        ResponseValidator.assertStatusAndContentType(response, HttpStatus.SC_OK);
        return ResponseValidator.deserialize(response, new TypeReference<List<SupplierPart>>() {});
    }

    public void deleteSupplierPart(int id, Role role) {
        Response response = executeDelete(ApiConstants.SUPPLIER_PART_ENDPOINT + id + "/", role);
        ResponseValidator.assertStatus(response, HttpStatus.SC_NO_CONTENT);
    }
}
