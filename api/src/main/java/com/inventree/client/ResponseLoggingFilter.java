package com.inventree.client;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ResponseLoggingFilter implements Filter {

    private static final Logger log = LogManager.getLogger(ResponseLoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        log.debug("Response for: [{}] {} -> {} {}",
                requestSpec.getMethod(),
                requestSpec.getURI(),
                response.statusCode(),
                response.body().asString());
        return response;
    }
}