package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/15/2018.
 */

public interface ACATRevenueSectionResponseParser {
    /**
     * Parse the given JsonObject and return a {@link ACATRevenueSectionResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Section Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATRevenueSectionResponse parse(@NonNull JsonObject object) throws Exception;

    List<ACATRevenueSection> parseAllSections(@NonNull JsonObject object) throws Exception;

    List<CashFlow> parseAllCashFlows(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception;

    CashFlow parseSectionCashFlow(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception;

    List<ACATItemResponse> parseAllYields(@NonNull JsonObject object) throws Exception;

    List<YieldConsumptionResponse> parseAllYieldConsumptions(@NonNull JsonObject object) throws Exception;
}
