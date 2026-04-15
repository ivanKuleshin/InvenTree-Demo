package com.inventree.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartListParams {

    private Integer limit;
    private Integer offset;
    private String search;
    private String ordering;
    private Object category;
    private Boolean cascade;
    private Boolean active;
    private Boolean assembly;
    private Boolean component;

    @JsonProperty("is_template")
    private Boolean isTemplate;

    private Boolean virtual;
    private Boolean trackable;
    private Boolean purchaseable;
    private Boolean salable;
    private Boolean locked;

    @JsonProperty("has_stock")
    private Boolean hasStock;

    @JsonProperty("has_ipn")
    private Boolean hasIpn;

    @JsonProperty("IPN")
    private String ipn;

    @JsonProperty("IPN_regex")
    private String ipnRegex;

    @JsonProperty("name_regex")
    private String nameRegex;

    @JsonProperty("created_after")
    private String createdAfter;

    @JsonProperty("created_before")
    private String createdBefore;

    private Boolean parameters;
    private Boolean tags;

    @JsonProperty("category_detail")
    private Boolean categoryDetail;

    @JsonProperty("path_detail")
    private Boolean pathDetail;

    @JsonProperty("location_detail")
    private Boolean locationDetail;
}
