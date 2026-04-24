package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockItemRequest {

    private Integer part;
    private Double quantity;
    private Integer location;
    private Integer status;
    private String batch;
    private String notes;
    private String packaging;

    @JsonProperty("delete_on_deplete")
    private Boolean deleteOnDeplete;

    @JsonProperty("serial_numbers")
    private String serialNumbers;

    @JsonProperty("expiry_date")
    private String expiryDate;
}
