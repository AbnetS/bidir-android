package com.gebeya.mobile.bidir.data.yieldconsumption.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/15/2018.
 */

public interface YieldConsumptionParser {

    /**
     * Parse the given JsonObject and return a {@link YieldConsumption} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @return Parsed Yield Consumption object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    YieldConsumption parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception;
}
