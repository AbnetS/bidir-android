package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/15/2018.
 */

public interface YieldConsumptionResponseParser {
    /**
     * Parse the given JsonObject and return a {@link YieldConsumptionResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @param parentSectionID the ID of the yield consumption
     * @return Parsed YieldConsumption Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    YieldConsumptionResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception;
}
