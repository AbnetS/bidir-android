package com.gebeya.mobile.bidir.data.groupedlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedlist.GroupedListResponse;
import com.google.gson.JsonObject;

/**
 * Created by abuti on 5/13/2018.
 */

public interface GroupedListResponseParser {
    /**
     * Parse the given JsonObject and return a {@link GroupedListResponse} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return Parsed Grouped List Response object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    GroupedListResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID,
                              @NonNull String parentCostListID) throws Exception;
}
