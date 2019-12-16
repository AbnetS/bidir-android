package com.gebeya.mobile.bidir.data.acatitem.remote;

import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * Concrete implementation for the {@link ACATItemParser} interface.
 */

public class BaseACATItemParser implements ACATItemParser {

    @Inject
    public BaseACATItemParser() {
    }

    @Override
    public ACATItem parse (@NonNull JsonObject object, @NonNull String category, @NonNull String sectionId,
                           @NonNull String costListId, @NonNull String groupedListId) throws Exception {
        try{
            final ACATItem acatItem = new ACATItem();

            acatItem._id = object.get("_id").getAsString();
            acatItem.item = object.get("item").getAsString();
            acatItem.unit = object.get("unit").getAsString();
            acatItem.remark = object.get("remark").getAsString();

            acatItem.sectionId = sectionId;
            acatItem.category = category;
            acatItem.costListId = costListId;
            acatItem.groupedListId = groupedListId;

            acatItem.createdAt = new DateTime(object.get("date_created").getAsString());
            acatItem.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return acatItem;

        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Item: " + e.getMessage());
        }
    }
}
