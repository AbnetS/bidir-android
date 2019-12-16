package com.gebeya.mobile.bidir.data.acatrevenuesection.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abuti on 5/13/2018.
 */

public class BaseACATRevenueSectionParser implements ACATRevenueSectionParser {

    @Override
    public ACATRevenueSection parse(@NonNull JsonObject object, @Nullable String parentSectionID) throws Exception{
        try{
            final ACATRevenueSection acatRevenueSection = new ACATRevenueSection();

            acatRevenueSection._id = object.get("_id").getAsString();
            acatRevenueSection.title = object.get("title").getAsString();
            acatRevenueSection.number = object.get("number").getAsInt();

            if (object.has("estimated_probable_revenue"))
                acatRevenueSection.estimatedMaxRevenue = object.get("estimated_probable_revenue").getAsDouble();

            if (object.has("estimated_min_revenue"))
                acatRevenueSection.estimatedMinRevenue = object.get("estimated_min_revenue").getAsDouble();

            if (object.has("estimated_max_revenue"))
                acatRevenueSection.estimatedMaxRevenue = object.get("estimated_max_revenue").getAsDouble();

            if (object.has("achieved_revenue"))
                acatRevenueSection.actualRevenue = object.get("achieved_revenue").getAsDouble();

            if (object.has("estimated_sub_total"))
                acatRevenueSection.estimatedSubTotal = object.get("estimated_sub_total").getAsDouble();

            if (object.has("actual_sub_total"))
                acatRevenueSection.actualSubTotal = object.get("actual_sub_total").getAsDouble();

            if (object.has("yield")) {
                JsonObject yieldObject = object.get("yield").getAsJsonObject();
                acatRevenueSection.yieldID = yieldObject.get("_id").getAsString();
            }

            if (object.has("yield_consumption")) {
                JsonObject yieldConsObject = object.get("yield_consumption").getAsJsonObject();
                acatRevenueSection.yieldConsumptionID = yieldConsObject.get("_id").getAsString();
            }

            acatRevenueSection.subSectionIDs = getSubSectionIds(object.get("sub_sections").getAsJsonArray());

            //parentSectionID will have a value when a section is subsection
            acatRevenueSection.parentSectionID = parentSectionID;

            acatRevenueSection.createdAt = new DateTime(object.get("date_created").getAsString());
            acatRevenueSection.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return acatRevenueSection;

        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Revenue Section: " + e.getMessage());
        }
    }

    @Override
    public List<String> getSubSectionIds(@NonNull JsonArray array){
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
