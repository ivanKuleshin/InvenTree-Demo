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
public class PartCategory {

    private Integer pk;
    private String name;
    private String description;
    private Integer parent;

    @JsonProperty("default_location")
    private Integer defaultLocation;

    @JsonProperty("default_keywords")
    private String defaultKeywords;

    private Integer level;

    @JsonProperty("part_count")
    private Integer partCount;

    private Integer subcategories;
    private String pathstring;
    private Boolean starred;
    private Boolean structural;
    private String icon;

    @JsonProperty("parent_default_location")
    private Integer parentDefaultLocation;
}
