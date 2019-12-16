package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/12/2018.
 */

public interface ACATCostSectionParser {
    /**
     * Parse the given JsonObject and return a {@link ACATCostSection} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Section object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATCostSection parse(@NonNull JsonObject object, @Nullable String parentSectionID) throws Exception;

    /**
     * Get list of Subsection IDs from the given Json Array.
     *
     * @param array Array of subsections.
     * @return Subsection Ids.
     */
    List<String> getSubSectionIds(@NonNull JsonArray array);


    List<String> toList(@NonNull JsonArray array);
}
