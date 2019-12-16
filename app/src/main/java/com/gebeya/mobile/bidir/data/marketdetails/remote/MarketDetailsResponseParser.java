package com.gebeya.mobile.bidir.data.marketdetails.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.marketdetails.MarketDetailsResponse;
import com.google.gson.JsonArray;

/**
 * Created by abuti on 5/15/2018.
 */

public interface MarketDetailsResponseParser {
    /**
     * Parse the given JsonObject and return a {@link MarketDetailsResponse} object.
     *
     * @param array JsonArray to parseResponse from.
     * @param type Estimated/Actual
     * @param yieldConsumptionID the ID of the yield consumption
     * @return Parsed Market details pageResponse object.
     * @throws Exception thrown if there was an error during the parsing.
     */

    MarketDetailsResponse parse(@NonNull JsonArray array, @NonNull String yieldConsumptionID, @NonNull String type)
            throws Exception;
}
