package com.gebeya.mobile.bidir.data.costlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatitem.remote.BaseACATItemResponseParser;
import com.gebeya.mobile.bidir.data.costlist.CostListResponse;
import com.gebeya.mobile.bidir.data.groupedlist.remote.BaseGroupedListResponseParser;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by abuti on 5/12/2018.
 */
public class BaseCostListResponseParser implements CostListResponseParser {

    @Inject
    BaseCostListParser costListParser;

    @Inject
    BaseACATItemResponseParser acatItemResponseParser;

    @Inject
    BaseGroupedListResponseParser groupedListResponseParser;

    @Inject
    public BaseCostListResponseParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public CostListResponse parse(@NonNull JsonObject object, @NonNull String parentSectionID) throws Exception {

        final CostListResponse response = new CostListResponse();

        try {
            response.costList = costListParser.parse(object, parentSectionID);

            final String costListID = object.get("_id").toString();

            //This is to parseACATApplication the items
            final JsonArray linearArray = object.get("linear").getAsJsonArray();
            final JsonArray groupedArray = object.get("grouped").getAsJsonArray();


            if (linearArray.size() != 0) {
                response.linear = new ArrayList<>();
                for (int i = 0; i < linearArray.size(); i++) {
                    response.linear.add(acatItemResponseParser.parse(linearArray.get(i).getAsJsonObject(),
                            parentSectionID, costListID, null, "COST"));
                }
            } else if (groupedArray.size() != 0) {
                response.grouped = new ArrayList<>();
                for (int i = 0; i < groupedArray.size(); i++) {
                    response.grouped.add(groupedListResponseParser.parse(groupedArray.get(i).getAsJsonObject(),
                            parentSectionID, costListID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
