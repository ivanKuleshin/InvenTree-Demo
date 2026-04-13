package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartCategoryRequest {

    String name;
    String description;
    Integer parent;

    @JsonProperty("default_location")
    Integer defaultLocation;

    @JsonProperty("default_keywords")
    String defaultKeywords;

    Boolean structural;
    String icon;
}
