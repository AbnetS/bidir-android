package com.gebeya.mobile.bidir.data.acatcostsection.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abuti on 5/12/2018.
 */
public class BaseACATCostSectionParser implements ACATCostSectionParser {

    @Inject
    public BaseACATCostSectionParser() {
    }

    @Override
    public ACATCostSection parse(@NonNull JsonObject object, @Nullable String parentSectionID) throws Exception {

        try {
            final ACATCostSection acatCostSection = new ACATCostSection();

            acatCostSection._id = object.get("_id").getAsString();

            acatCostSection.title = object.get("title").getAsString();
            acatCostSection.number = object.get("number").getAsInt();

            acatCostSection.estimatedSubTotal = object.get("estimated_sub_total").getAsDouble();
            acatCostSection.actualSubTotal = object.get("achieved_sub_total").getAsDouble();

            if (object.has("variety"))
                acatCostSection.variety = object.get("variety").getAsString();

            if (object.has("seed_source")) {
                acatCostSection.seedSources = toList(object.get("seed_source").getAsJsonArray());
            } else {
                acatCostSection.seedSources = new ArrayList<>();
            }

            if (object.has("cost_list")) {
                JsonObject costList = object.get("cost_list").getAsJsonObject();
                acatCostSection.costListID = costList.get("_id").getAsString();
            }

            acatCostSection.subSectionIDs = getSubSectionIds(object.get("sub_sections").getAsJsonArray());

            //parentSectionID will have a value when a section is subsection
            acatCostSection.parentSectionID = parentSectionID;

            acatCostSection.createdAt = new DateTime(object.get("date_created").getAsString());
            acatCostSection.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return acatCostSection;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Cost Section: " + e.getMessage());
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
    public List<String> getSubSectionIds(@NonNull JsonArray array) {
        final List<String> ids = new ArrayList<>();
        final int length = array.size();
        for (int i = 0; i < length; i++) {
            final JsonObject object = array.get(i).getAsJsonObject();
            final String id = object.get("_id").getAsString();
            ids.add(id);
        }
        return ids;
    }
}
