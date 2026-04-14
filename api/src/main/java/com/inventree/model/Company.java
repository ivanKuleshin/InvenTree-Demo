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
public class Company {

    private Integer pk;
    private String name;

    @JsonProperty("is_supplier")
    private Boolean isSupplier;

    @JsonProperty("is_manufacturer")
    private Boolean isManufacturer;
}
