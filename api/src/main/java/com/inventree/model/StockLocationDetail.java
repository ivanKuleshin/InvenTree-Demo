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
public class StockLocationDetail {

    private Integer pk;
    private String name;
    private String description;
    private Integer parent;
    private String pathstring;
    private Integer items;
    private Integer sublocations;
    private Boolean structural;
    private Boolean external;
    private Integer owner;
    private String icon;

    @JsonProperty("location_type")
    private Integer locationType;

    @JsonProperty("location_type_detail")
    private StockLocationTypeDetail locationTypeDetail;

    private Integer level;

    @JsonProperty("barcode_hash")
    private String barcodeHash;
}
