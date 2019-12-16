package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/13/2018.
 */

public interface ACATRevenueSectionParser {

    /**
     * Parse the given JsonObject and return a {@link ACATCostSection} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Section object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATRevenueSection parse(@NonNull JsonObject object, @Nullable String parentSectionID) throws Exception;

    List<String> getSubSectionIds(@NonNull JsonArray array);
}
