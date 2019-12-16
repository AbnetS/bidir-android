package com.gebeya.mobile.bidir.data.costlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.costlist.CostList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/11/2018.
 */

public interface CostListParser {
    /**
     * Parse the given JsonObject and return a {@link com.gebeya.mobile.bidir.data.groupedlist.GroupedList} object.
     *
     * @param object JsonObject to parseResponse from.
     * @param sectionId the section Id to which the cost list belongs to
     * @return Parsed CostList object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    CostList parse(@NonNull JsonObject object, @NonNull String sectionId) throws Exception;

    /**
     * Get list of ACAT item IDs from the given Json Array (within linear cost list array)
     *
     * @param array Array of acat items wiithin a linear cost list.
     * @return Parsed linear List Item Ids.
     */
    List<String> getLinearListItemIds(@NonNull JsonArray array);

    /**
     * Get list of Group IDs from the given Json Array.
     *
     * @param array Array of groups within a cost list.
     * @return Parsed Grouped List Item Ids.
     */
    List<String> getGroupIds(@NonNull JsonArray array);

}
