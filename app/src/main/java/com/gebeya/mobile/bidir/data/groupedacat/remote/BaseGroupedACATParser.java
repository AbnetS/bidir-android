package com.gebeya.mobile.bidir.data.groupedacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;

import javax.inject.Inject;

public class    BaseGroupedACATParser implements GroupedACATParser{

    @Inject ACATApplicationParser acatParser;

    @Inject
    public BaseGroupedACATParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public GroupedACAT parse(@NonNull JsonObject object) throws Exception {
        try {
            final GroupedACAT acat = new GroupedACAT();

            acat._id = object.get("_id").getAsString();

            JsonObject groupObject = object.get("group").getAsJsonObject();
            acat.groupId = groupObject.get("_id").getAsString();
            JsonArray memberIds = groupObject.get("members").getAsJsonArray();
            acat.membersId = new ArrayList<>();
            for (int i = 0; i < memberIds.size(); i++) {
                acat.membersId.add(memberIds.get(i).getAsString());
            }

            String apiStatus = object.get("status").getAsString();
            acat.status = getLocalStatus(apiStatus);


            JsonArray acatIds = object.get("acats").getAsJsonArray();

            for (int i = 0; i < acatIds.size(); i++) {
                JsonObject acatObject = acatIds.get(i).getAsJsonObject();
                if (!acatObject.isJsonNull())
                    acat.acatApplications.add(acatParser.parse(acatObject));
            }

            final String createdAt = object.get("date_created").getAsString();
            acat.createdAt = new DateTime(createdAt);
            final String updatedAt = object.get("last_modified").getAsString();
            acat.updatedAt = new DateTime(updatedAt);

            return acat;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing grouped ACAT: " + e.getMessage());
        }
    }

    @Override
    public String getLocalStatus(@NonNull String apiStatus) {
        return getStatus(apiStatus, false);
    }

    @Override
    public String getApiStatus(@NonNull String localStatus) {
        return getStatus(localStatus, true);
    }

    private String getStatus(@NonNull String value , boolean localValue) {
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
