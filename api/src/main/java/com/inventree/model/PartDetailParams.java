package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartDetailParams {

    private Boolean parameters;
    private Boolean tags;

    @JsonProperty("category_detail")
    private Boolean categoryDetail;

    @JsonProperty("path_detail")
    private Boolean pathDetail;

    @JsonProperty("location_detail")
    private Boolean locationDetail;

    @JsonProperty("price_breaks")
    private Boolean priceBreaks;
}
