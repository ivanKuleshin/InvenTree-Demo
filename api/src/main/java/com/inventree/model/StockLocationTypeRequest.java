package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockLocationTypeRequest {

    private String name;
    private String description;
    private String icon;
}
