package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/12/2018.
 */

public interface ACATCostSectionResponseParser {

    /**
     * Parse the given JsonObject and return a {@link ACATCostSectionResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Section object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATCostSectionResponse parse(@NonNull JsonObject object) throws Exception;

    /**
     * Parse the given JsonObject and return a {@link List< ACATCostSection >} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Section object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ACATCostSection> parseAllSections(@NonNull JsonObject object) throws Exception;

    /**
     * Parse the given JsonObject and return a {@link ACATCostSection} object.
     *
     * @param array JsonArray to parseResponse from.
     * @return Parsed ACAT Cost Subsection object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    List<ACATCostSection> parseSubSections(@NonNull JsonArray array, @NonNull String parentSectionID) throws Exception;


    List<CashFlow> parseAllCashFlows(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception;


    CashFlow parseSectionCashFlow(@NonNull JsonObject object, @NonNull String whatToParse) throws Exception;

    List<CostListResponse> parseAllCostLists(@NonNull JsonObject object) throws Exception;

    ACATCostSectionResponse parseSingleSection (@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception;
}
