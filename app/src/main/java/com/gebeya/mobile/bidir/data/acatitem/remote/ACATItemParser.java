package com.gebeya.mobile.bidir.data.acatitem.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.google.gson.JsonObject;

/**
 * Contract class for  {@link ACATItem} parser.
 */

public interface ACATItemParser {
    /**
     * Parse the given JsonObject and return a {@link ACATItem} object.
     *
     * @param object           JsonObject to parseResponse from.
     * @return Parsed ACAT Item object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATItem parse(@NonNull JsonObject object, @NonNull String category, @NonNull String sectionId,
                   @NonNull String costListId, @NonNull String groupedListId) throws Exception;
}
