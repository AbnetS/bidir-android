package com.gebeya.mobile.bidir.data.acatsection.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatsection.ACATSection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for the {@link ACATSectionParser} parser.
 */

public class BaseACATSectionParser implements ACATSectionParser {

    @Override
    public ACATSection parse(@NonNull JsonObject object, @NonNull String referenceId, @NonNull String referenceType) throws Exception {
        try {
            final ACATSection acatSection = new ACATSection();

            acatSection._id = object.get("_id").getAsString();
            acatSection.referenceId = referenceId;
            acatSection.referenceType = referenceType;
            acatSection.number = object.get("number").getAsInt();
            acatSection.subSectionIDs = getSubSectionIds(object.get("sub_sections").getAsJsonArray());
            acatSection.estimatedSubTotal = object.get("estimated_sub_total").getAsDouble();
            acatSection.acheivedSubTotal = object.get("achieved_sub_total").getAsDouble();
            acatSection.estimatedCashFlowId = getEstimatedCashFlowId(object.get("estimated_cash_flow").getAsJsonObject());
            acatSection.achievedCashFlowId = getAchievedCashFlowId(object.get("achieved_cash_flow").getAsJsonObject());
            acatSection.title = object.get("title").getAsString();
            acatSection.createdAt = new DateTime(object.get("date_created").getAsString());
            acatSection.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return acatSection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ACAT Section: " + e.getMessage());
        }
    }

    @Override
    public List<String> getSubSectionIds(@NonNull JsonArray array) {
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
    public String getEstimatedCashFlowId(@NonNull JsonObject object) {
        final String id = object.get("_id").getAsString();
        return id;
    }

    @Override
    public String getAchievedCashFlowId(@NonNull JsonObject object) {
        final String id = object.get("_id").getAsString();
        return id;
    }
}
