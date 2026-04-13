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
public class Part {

    private Integer pk;
    private String name;
    private String description;

    @JsonProperty("full_name")
    private String fullName;

    private Integer category;

    @JsonProperty("category_name")
    private String categoryName;

    private Boolean active;
    private Boolean assembly;
    private Boolean component;

    @JsonProperty("is_template")
    private Boolean isTemplate;

    private Boolean virtual;
    private Boolean trackable;
    private Boolean testable;
    private Boolean purchaseable;
    private Boolean salable;
    private Boolean locked;

    @JsonProperty("IPN")
    private String ipn;

    private String keywords;
    private String revision;
    private String units;

    @JsonProperty("default_location")
    private Integer defaultLocation;

    @JsonProperty("minimum_stock")
    private Double minimumStock;

    @JsonProperty("in_stock")
    private Double inStock;

    @JsonProperty("total_in_stock")
    private Double totalInStock;

    @JsonProperty("stock_item_count")
    private Integer stockItemCount;

    private String thumbnail;

    @JsonProperty("barcode_hash")
    private String barcodeHash;

    @JsonProperty("creation_date")
    private String creationDate;
}
