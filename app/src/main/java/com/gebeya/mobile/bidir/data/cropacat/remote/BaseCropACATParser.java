package com.gebeya.mobile.bidir.data.cropacat.remote;

import android.support.annotation.NonNull;


import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abuti on 5/17/2018.
 */

public class BaseCropACATParser implements CropACATParser {

    @Override
    public CropACAT parse(@NonNull JsonObject object, @NonNull String acatApplicationID) throws Exception {
        try {
            final CropACAT cropACAT = new CropACAT();

            cropACAT._id = object.get("_id").getAsString();
            cropACAT.acatApplicationID = acatApplicationID;

            // TODO: Revert this back when API pageResponse is fixed.
            if (object.get("client").isJsonObject()) {
                JsonObject clientObject = object.get("client").getAsJsonObject();
                cropACAT.clientID = clientObject.get("_id").getAsString();
            } else {
                cropACAT.clientID = object.get("client").getAsString();
            }

            cropACAT.createdBy = object.get("created_by").getAsString();

            JsonObject cropObject = object.get("crop").getAsJsonObject();
            cropACAT.cropID = cropObject.get("_id").getAsString();
            cropACAT.cropName = cropObject.get("name").getAsString();

            final JsonObject estimated = object.get("estimated").getAsJsonObject();
            cropACAT.estimatedTotalRevenue = estimated.get("total_revenue").getAsDouble();
            cropACAT.estimatedTotalCost = estimated.get("total_cost").getAsDouble();
            cropACAT.estimatedNetIncome = estimated.get("net_income").getAsDouble();

            final JsonObject actual = object.get("achieved").getAsJsonObject();
            cropACAT.actualTotalRevenue = actual.get("total_revenue").getAsDouble();
            cropACAT.actualTotalCost = actual.get("total_cost").getAsDouble();
            cropACAT.actualNetIncome = actual.get("net_income").getAsDouble();

            cropACAT.firstExpenseMonth = object.get("first_expense_month").getAsString();
            cropACAT.accessToNonFinServices = object.get("access_to_non_financial_resources").getAsBoolean();

            cropACAT.NonFinServices = toList(object.get("non_financial_resources").getAsJsonArray());
            cropACAT.croppingArea = object.get("cropping_area_size").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = getLocalStatus(apiStatus);
            if (localStatus.equals(CropACATParser.STATUS_UNKNOWN)) {
                throw new Exception("Unknown Crop ACAT status: " + apiStatus);
            }

            cropACAT.status = localStatus;

            final JsonArray sections = object.get("sections").getAsJsonArray();
            cropACAT.costSectionId = (sections.get(0).getAsJsonObject()).get("_id").getAsString(); //the first section is always the cost section
            cropACAT.revenueSectionId = (sections.get(1).getAsJsonObject()).get("_id").getAsString(); //The second section is always the revenue section

            final JsonObject gps = object.get("gps_location").getAsJsonObject();
            final JsonArray polygon = gps.getAsJsonArray("polygon");

            if (polygon.size() == 0) cropACAT.isGPSPolygon = false;
            else cropACAT.isGPSPolygon = true;

            cropACAT.createdAt = new DateTime(object.get("date_created").getAsString());
            cropACAT.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return cropACAT;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing CropACAT: " + e.getMessage());
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
    public String getLocalStatus(@NonNull String apiStatus) {
        return getStatus(apiStatus, false);
    }

    @Override
    public String getApiStatus(@NonNull String localStatus) {
        return getStatus(localStatus, true);
    }

    private String getStatus(@NonNull String value, boolean localValue) {
        switch (value) {
            case STATUS_LOCAL_NEW:
            case STATUS_API_NEW:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_NEW; // change in the status is to account for the api restrictions. todo wait for the fix.
            case STATUS_LOCAL_IN_PROGRESS:
            case STATUS_API_IN_PROGRESS:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_IN_PROGRESS;
            case STATUS_LOCAL_SUBMITTED:
            case STATUS_API_SUBMITTED:
                return localValue ? STATUS_API_SUBMITTED : STATUS_LOCAL_SUBMITTED;
            case STATUS_LOCAL_COMPLETED:
            case STATUS_API_COMPLETED:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_COMPLETED; // change in the status is to account for the api restrictions. todo wait for the fix.
            case STATUS_LOCAL_AUTHORIZED:
            case STATUS_API_AUTHORIZED:
                return localValue ? STATUS_API_AUTHORIZED : STATUS_LOCAL_AUTHORIZED;
            case STATUS_LOCAL_DECLINED_FOR_REVIEW:
            case STATUS_API_DECLINED_FOR_REVIEW:
                return localValue ? STATUS_API_DECLINED_FOR_REVIEW : STATUS_LOCAL_DECLINED_FOR_REVIEW;
            case STATUS_LOCAL_LOAN_PAID:
            case STATUS_API_LOAN_PAID:
                return localValue ? STATUS_API_LOAN_PAID : STATUS_LOCAL_LOAN_PAID;
            default:
                return STATUS_UNKNOWN;
        }
    }
}
