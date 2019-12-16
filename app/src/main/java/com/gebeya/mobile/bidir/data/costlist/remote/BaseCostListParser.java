package com.gebeya.mobile.bidir.data.costlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.costlist.CostList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/11/2018.
 */

public class BaseCostListParser implements CostListParser {

    @Inject
    public BaseCostListParser() {
    }

    @Override
    public CostList parse (@NonNull JsonObject object, @NonNull String sectionId) throws Exception{
        try{
            final CostList costList = new CostList();

            costList.parentSectionID = sectionId;

            costList._id = object.get("_id").getAsString();

            costList.linearListIDs = getLinearListItemIds (object.get("linear").getAsJsonArray());
            costList.groupedListIDs = getGroupIds (object.get("grouped").getAsJsonArray());

            costList.createdAt = new DateTime(object.get("date_created").getAsString());
            costList.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return costList;


        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing Cashflow: " + e.getMessage());
        }
    }

    @Override
    public List<String> getGroupIds(@NonNull JsonArray array){
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i <length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }

    @Override
    public List<String> getLinearListItemIds(@NonNull JsonArray array){
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i <length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }
}
