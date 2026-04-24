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
public class PartPricing {

    private String currency;
    private String updated;

    @JsonProperty("scheduled_for_update")
    private Boolean scheduledForUpdate;

    @JsonProperty("bom_cost_min")
    private Double bomCostMin;

    @JsonProperty("bom_cost_max")
    private Double bomCostMax;

    @JsonProperty("purchase_cost_min")
    private Double purchaseCostMin;

    @JsonProperty("purchase_cost_max")
    private Double purchaseCostMax;

    @JsonProperty("internal_cost_min")
    private Double internalCostMin;

    @JsonProperty("internal_cost_max")
    private Double internalCostMax;

    @JsonProperty("supplier_price_min")
    private Double supplierPriceMin;

    @JsonProperty("supplier_price_max")
    private Double supplierPriceMax;

    @JsonProperty("overall_min")
    private Double overallMin;

    @JsonProperty("overall_max")
    private Double overallMax;

    @JsonProperty("sale_price_min")
    private Double salePriceMin;

    @JsonProperty("sale_price_max")
    private Double salePriceMax;

    @JsonProperty("sale_history_min")
    private Double saleHistoryMin;

    @JsonProperty("sale_history_max")
    private Double saleHistoryMax;

    @JsonProperty("override_min")
    private Double overrideMin;

    @JsonProperty("override_max")
    private Double overrideMax;

    @JsonProperty("override_min_currency")
    private String overrideMinCurrency;

    @JsonProperty("override_max_currency")
    private String overrideMaxCurrency;
}

