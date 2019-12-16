package com.gebeya.mobile.bidir.data.acatitem.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/13/2018.
 */

public interface ACATItemResponseParser {
    /**
     * Parse the given JsonObject and return a {@link ACATItemResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed ACAT Cost Item Response Object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    ACATItemResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID,
                           @Nullable String costListID, @Nullable String groupedListID, @NonNull String category) throws Exception;
}
