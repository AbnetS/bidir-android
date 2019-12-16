package com.gebeya.mobile.bidir.data.acatapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public class BaseACATApplicationParser implements ACATApplicationParser {

    @Override
    public ACATApplication  parse(@NonNull JsonObject object) throws Exception {
        try {
            final ACATApplication acatApplication = new ACATApplication();
            acatApplication._id = object.get("_id").getAsString();

            JsonObject createdByObject = object.get("created_by").getAsJsonObject();
            acatApplication.createdByID = createdByObject.get("_id").getAsString();

            JsonObject branchObject = object.get("branch").getAsJsonObject();
            acatApplication.branchID = branchObject.get("_id").getAsString();

            JsonObject clientObject = object.get("client").getAsJsonObject();
            acatApplication.clientID = clientObject.get("_id").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = getLocalStatus(apiStatus);
            if (localStatus.equals(ACATApplicationParser.STATUS_UNKNOWN)) {
                throw new Exception("Unknown API ACAT Application status: " + apiStatus);
            }
            acatApplication.status = localStatus;

            final JsonObject estimated = object.get("estimated").getAsJsonObject();
            acatApplication.estimatedTotalRevenue = estimated.get("total_revenue").getAsDouble();
            acatApplication.estimatedTotalCost = estimated.get("total_cost").getAsDouble();
            acatApplication.estimatedNetIncome = estimated.get("net_income").getAsDouble();

            final JsonObject actual = object.get("achieved").getAsJsonObject();
            acatApplication.actualTotalRevenue = actual.get("total_revenue").getAsDouble();
            acatApplication.actualTotalCost = actual.get("total_cost").getAsDouble();
            acatApplication.actualNetIncome = actual.get("net_income").getAsDouble();

            if (!object.get("loan_product").isJsonNull()){
                final JsonObject loanProduct = object.get("loan_product").getAsJsonObject();
                acatApplication.loanProductID = loanProduct.get("_id").getAsString();
            }


            acatApplication.createdAt = new DateTime(object.get("date_created").getAsString());
            acatApplication.updatedAt = new DateTime(object.get("last_modified").getAsString());

            return acatApplication;

        } catch (Exception e) {
//            return null;
            throw new Exception("Error parsing ACAT: " + e);
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
                return localValue ? STATUS_API_NEW : STATUS_LOCAL_NEW;
            case STATUS_LOCAL_IN_PROGRESS:
            case STATUS_API_IN_PROGRESS:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_IN_PROGRESS;
            case STATUS_LOCAL_ACAT_IN_PROGRESS:
            case STATUS_API_ACAT_IN_PROGRESS:
                return localValue ? STATUS_API_ACAT_IN_PROGRESS : STATUS_LOCAL_ACAT_IN_PROGRESS;
            case STATUS_LOCAL_SUBMITTED:
            case STATUS_API_SUBMITTED:
                return localValue ? STATUS_API_SUBMITTED : STATUS_LOCAL_SUBMITTED;
            case STATUS_LOCAL_RESUBMITTED:
            case STATUS_API_RESUBMITTED:
                return localValue ? STATUS_API_RESUBMITTED : STATUS_LOCAL_RESUBMITTED;
            case STATUS_LOCAL_AUTHORIZED:
            case STATUS_API_AUTHORIZED:
                return localValue ? STATUS_API_AUTHORIZED : STATUS_LOCAL_AUTHORIZED;
            case STATUS_LOCAL_LOAN_GRANTED:
            case STATUS_API_LOAN_GRANTED:
                return localValue ? STATUS_API_LOAN_GRANTED : STATUS_LOCAL_LOAN_GRANTED;
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
