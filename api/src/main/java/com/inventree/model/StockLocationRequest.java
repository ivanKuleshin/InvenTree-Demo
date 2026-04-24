package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockLocationRequest {

    private String name;
    private String description;
    private Integer parent;
    private Boolean structural;
    private Boolean external;
    private String icon;

    @JsonProperty("location_type")
    private Integer locationType;
}
