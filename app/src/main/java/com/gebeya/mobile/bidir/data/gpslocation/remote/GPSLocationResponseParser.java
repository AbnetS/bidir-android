package com.gebeya.mobile.bidir.data.gpslocation.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.gpslocation.GPSLocationResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/17/2018.
 */

public interface GPSLocationResponseParser {
    /**
     * Parse the given JsonObject and return a {@link GPSLocationResponseParser} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed GPS Location Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    GPSLocationResponse parse(@NonNull JsonObject object, @NonNull String acatApplicationID,
                              @NonNull String cropACATID) throws Exception;
}
