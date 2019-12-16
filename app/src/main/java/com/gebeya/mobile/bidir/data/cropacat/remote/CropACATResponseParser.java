package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.gebeya.mobile.bidir.data.cropacat.CropACATResponse;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/17/2018.
 */

public interface CropACATResponseParser {
    /**
     * Parse the given JsonObject and return a {@link CostListResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost List Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    CropACATResponse parse(@NonNull JsonObject object, @NonNull String acatApplciationID) throws Exception;
}
