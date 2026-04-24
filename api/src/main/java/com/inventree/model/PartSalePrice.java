package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartSalePrice {

    private Integer pk;
    private Integer part;
    private Double quantity;
    private Double price;

    @JsonProperty("price_currency")
    private String priceCurrency;
}

