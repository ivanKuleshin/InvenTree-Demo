package com.inventree.testdata;

import com.inventree.auth.Role;
import com.inventree.util.HttpStatus;
import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

public final class PricingDataProviders {

    private PricingDataProviders() {}

    @DataProvider(name = "forbiddenRolesForInternalPrice")
    public static Object[][] forbiddenRolesForInternalPrice() {
        return new Object[][] {
            {Role.READER}
        };
    }

    @DataProvider(name = "invalidInternalPricePayloads")
    public static Object[][] invalidInternalPricePayloads() {
        Map<String, Object> missingPrice = new HashMap<>();
        missingPrice.put(PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK);
        missingPrice.put(PricingTestData.FIELD_QUANTITY, PricingTestData.CREATE_QUANTITY);
        missingPrice.put(PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD);

        Map<String, Object> missingQuantity = new HashMap<>();
        missingQuantity.put(PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK);
        missingQuantity.put(PricingTestData.FIELD_PRICE, PricingTestData.CREATE_PRICE);
        missingQuantity.put(PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD);

        return new Object[][] {
            {
                "TC-APPRICE-NEG-002-NonExistentPartFK",
                Map.of(
                    PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK,
                    PricingTestData.FIELD_QUANTITY, PricingTestData.CREATE_QUANTITY,
                    PricingTestData.FIELD_PRICE, PricingTestData.CREATE_PRICE,
                    PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD
                ),
                HttpStatus.SC_BAD_REQUEST
            },
            {
                "TC-APPRICE-NEG-002-MissingPrice",
                missingPrice,
                HttpStatus.SC_BAD_REQUEST
            },
            {
                "TC-APPRICE-NEG-002-MissingQuantity",
                missingQuantity,
                HttpStatus.SC_BAD_REQUEST
            }
        };
    }

    @DataProvider(name = "invalidSalePricePayloads")
    public static Object[][] invalidSalePricePayloads() {
        Map<String, Object> missingPrice = new HashMap<>();
        missingPrice.put(PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK);
        missingPrice.put(PricingTestData.FIELD_QUANTITY, PricingTestData.SALE_CREATE_QUANTITY);
        missingPrice.put(PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD);

        return new Object[][] {
            {
                "TC-APSPRICE-NEG-002-NonExistentPartFK",
                Map.of(
                    PricingTestData.FIELD_PART, PricingTestData.NON_EXISTENT_PK,
                    PricingTestData.FIELD_QUANTITY, PricingTestData.SALE_CREATE_QUANTITY,
                    PricingTestData.FIELD_PRICE, PricingTestData.SALE_CREATE_PRICE,
                    PricingTestData.FIELD_PRICE_CURRENCY, PricingTestData.CURRENCY_USD
                ),
                HttpStatus.SC_BAD_REQUEST
            },
            {
                "TC-APSPRICE-NEG-002-MissingPrice",
                missingPrice,
                HttpStatus.SC_BAD_REQUEST
            }
        };
    }

    @DataProvider(name = "overrideMinValues")
    public static Object[][] overrideMinValues() {
        return new Object[][] {
            {PricingTestData.OVERRIDE_MIN_VALUE, PricingTestData.EXPECTED_OVERRIDE_MIN},
            {PricingTestData.OVERRIDE_MIN_BOUNDARY_VALUE, PricingTestData.EXPECTED_OVERRIDE_MIN_BOUNDARY}
        };
    }
}