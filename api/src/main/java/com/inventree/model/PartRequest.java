package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartRequest {

    String name;
    String description;
    Integer category;
    Boolean active;
    Boolean assembly;
    Boolean component;

    @JsonProperty("is_template")
    Boolean isTemplate;

    Boolean virtual;
    Boolean trackable;
    Boolean testable;
    Boolean purchaseable;
    Boolean salable;
    Boolean locked;

    @JsonProperty("IPN")
    String ipn;

    String keywords;
    String revision;
    String units;

    @JsonProperty("default_location")
    Integer defaultLocation;

    @JsonProperty("minimum_stock")
    Double minimumStock;

    @JsonProperty("default_expiry")
    Integer defaultExpiry;
}
