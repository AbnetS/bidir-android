package com.gebeya.mobile.bidir.data.groupedlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitem.remote.BaseACATItemResponseParser;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedListResponse;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/13/2018.
 */

public class BaseGroupedListResponseParser implements GroupedListResponseParser{

    @Inject
    BaseGroupedListParser groupedListParser;

    @Inject
    BaseACATItemResponseParser acatItemResponseParser;

    @Inject
    public BaseGroupedListResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public GroupedListResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID,
                                     @NonNull String parentCostListID) throws Exception{
        final GroupedListResponse response = new GroupedListResponse();
        final List<ACATItemResponse> acatItemResponses = new ArrayList<>();

        try {
            response.groupedList = groupedListParser.parse (object, parentCostListID);

            //This is to parseACATApplication the items
            final JsonArray itemsArray = object.get("items").getAsJsonArray();
            final String groupId = object.get("_id").getAsString();

            if (itemsArray.size() != 0) {
                for (int i = 0; i < itemsArray.size(); i++){
                    final JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                    acatItemResponses.add(acatItemResponseParser.parse(itemObject, parentSectionID, parentCostListID, groupId, "COST"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        response.items = acatItemResponses;

        return response;

    }
}
