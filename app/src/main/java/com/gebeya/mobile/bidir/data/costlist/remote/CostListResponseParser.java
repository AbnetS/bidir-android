package com.gebeya.mobile.bidir.data.costlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/12/2018.
 */

public interface CostListResponseParser {
    /**
     * Parse the given JsonObject and return a {@link CostListResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost List Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    CostListResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception;
}
