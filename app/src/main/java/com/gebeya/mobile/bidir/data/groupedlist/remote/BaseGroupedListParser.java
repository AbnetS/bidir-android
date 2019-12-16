package com.gebeya.mobile.bidir.data.groupedlist.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedlist.GroupedList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/11/2018.
 */

public class BaseGroupedListParser implements GroupedListParser {

    @Inject
    public BaseGroupedListParser() {
    }

    @Override
    public GroupedList parse(@NonNull JsonObject object, @NonNull String parentCostListId) throws Exception{
        try{
            final GroupedList groupedList = new GroupedList();

            groupedList.parentCostListId = parentCostListId;

            groupedList._id = object.get("_id").getAsString();
            groupedList.title = object.get("title").getAsString();
            groupedList.itemIDs = getGroupItemIds (object.get("items").getAsJsonArray());

            groupedList.createdAt = new DateTime(object.get("date_created").getAsString());
            groupedList.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return groupedList;


        }catch(Exception e){
            e.printStackTrace();
            return null;
//            throw new Exception("Error parsing Cashflow: " + e.getMessage());
        }
    }


    @Override
    public List<String> toList(@NonNull JsonArray array) {
        final List<String> list = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonElement element = array.get(i);
            if (element != null && !element.isJsonNull()) {
                final String s = element.getAsString();
                list.add(s);
            }
        }
        return list;
    }
    @Override
    public List<String> getGroupItemIds(@NonNull JsonArray array){
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
