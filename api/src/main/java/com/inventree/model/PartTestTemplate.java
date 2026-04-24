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
public class PartTestTemplate {

    private Integer pk;
    private String key;
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

    private Integer results;
    private String choices;
}
