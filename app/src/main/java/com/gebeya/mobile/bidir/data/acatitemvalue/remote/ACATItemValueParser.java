package com.gebeya.mobile.bidir.data.acatitemvalue.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.google.gson.JsonObject;

/**
 * Contract class for  {@link ACATItemValue } parser.
 */

public interface ACATItemValueParser {
    /**
     * Parse the given JsonObject and return a {@link ACATItemValue} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @return Parsed ACAT Item Value object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATItemValue parse(@NonNull JsonObject object, @NonNull String acatItemId, @NonNull String type) throws Exception;
}