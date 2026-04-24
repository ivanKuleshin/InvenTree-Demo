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
public class StockItemDetail {

    private Integer pk;
    private Integer part;
    private Integer location;
    private Double quantity;
    private Integer status;

    @JsonProperty("status_text")
    private String statusText;

    private String serial;
    private String batch;

    @JsonProperty("in_stock")
    private Boolean inStock;

    @JsonProperty("is_building")
    private Boolean isBuilding;

    @JsonProperty("delete_on_deplete")
    private Boolean deleteOnDeplete;

    @JsonProperty("barcode_hash")
    private String barcodeHash;

    private String updated;

    @JsonProperty("tracking_items")
    private Integer trackingItems;

    private String notes;
    private String packaging;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("stocktake_date")
    private String stocktakeDate;

    private Integer customer;
}
