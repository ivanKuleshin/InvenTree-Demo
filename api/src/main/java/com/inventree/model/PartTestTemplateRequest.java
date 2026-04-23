package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartTestTemplateRequest {

    private Integer part;

    @JsonProperty("test_name")
    private String testName;

    private String description;
    private Boolean enabled;
    private Boolean required;

    @JsonProperty("requires_value")
    private Boolean requiresValue;

    @JsonProperty("requires_attachment")
    private Boolean requiresAttachment;

    private String choices;
}
