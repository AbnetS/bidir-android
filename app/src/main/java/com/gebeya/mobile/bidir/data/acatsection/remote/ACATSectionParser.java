package com.gebeya.mobile.bidir.data.acatsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatsection.ACATSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Contract for a{@link com.gebeya.mobile.bidir.data.acatsection.ACATSection} parser.
 */

public interface ACATSectionParser {

    /**
     * Parse the fetched JSONObject and return an ACAT Section object.
     *
     * @param object JSONObject to parseACATApplication from.
     * @param referenceId ReferenceID to which this ACAT Section belongs to.
     * @param referenceType Reference type to which this section belongs to. Obviously it's ACAT
     * @return ACAT section that has been parsed.
     * @throws Exception thrown if error occurs during parsing the pageResponse.
     */
    ACATSection parse(@NonNull JsonObject object,
                      @NonNull String referenceId,
                      @NonNull String referenceType) throws Exception;


    /**
     * Get list of ACAT subsection IDs from the given Json Array.
     *
     * @param array JsonArray to parseACATApplication pageResponse list of ACAT subsection IDs from.
     * @return List of parsed String ACAT section IDs.
     */
    List<String> getSubSectionIds(@NonNull JsonArray array);

    /**
     * Get Estimated cash flow id from the give JSON object.
     * @param object JSON object to parseACATApplication pageResponse of estimated cash flow id.
     * @return ID of estimated cash flow.
     */
    String getEstimatedCashFlowId(@NonNull JsonObject object);

    /**
     * Get Achieved cash flow id from the give JSON object.
     * @param object JSON object to parseACATApplication pageResponse of achieved cash flow id.
     * @return ID of achieved cash flow.
     */
    String getAchievedCashFlowId(@NonNull JsonObject object);
}
