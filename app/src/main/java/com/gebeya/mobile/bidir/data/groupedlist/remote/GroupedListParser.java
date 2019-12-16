package com.gebeya.mobile.bidir.data.groupedlist.remote;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedlist.GroupedList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Contract class for  {@link GroupedList} parser.
 */

public interface GroupedListParser {

    /**
     * Parse the given JsonObject and return a {@link GroupedList} object.
     *
     * @param object JsonObject to parseResponse from.
     * @param parentCostListId the parent cost list Id to which the grouped list belongs to
     * @return Parsed GroupedList Item object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    GroupedList parse(@NonNull JsonObject object, @NonNull String parentCostListId) throws Exception;


    /**
     * Get list of ACAT item IDs from the given Json Array.
     *
     * @param array Array of acat items wiithin a grouped cost list.
     * @return Parsed Grouped List Item Ids.
     */
    List<String> toList(@NonNull JsonArray array);

    List<String> getGroupItemIds(@NonNull JsonArray array);

}
