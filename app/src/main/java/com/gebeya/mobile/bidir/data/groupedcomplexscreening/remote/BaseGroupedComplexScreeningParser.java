package com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class BaseGroupedComplexScreeningParser implements GroupedComplexScreeningParser{

    @Inject ComplexScreeningParser screeningParser;

    @Inject
    public BaseGroupedComplexScreeningParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public GroupedComplexScreening parse(@NonNull JsonObject object) throws Exception {
        try {
            final GroupedComplexScreening screening = new GroupedComplexScreening();

            screening._id = object.get("_id").getAsString();

            JsonObject groupObject = object.get("group").getAsJsonObject();
            screening.groupId = groupObject.get("_id").getAsString();
            JsonArray memberIds = groupObject.get("members").getAsJsonArray();

            screening.membersId = new ArrayList<>();
            for (int i = 0; i < memberIds.size(); i++) {
                screening.membersId.add(memberIds.get(i).getAsString());
            }


            String apiStatus = object.get("status").getAsString();
            screening.status = getLocalStatus(apiStatus);

            JsonArray screeningIds = object.get("screenings").getAsJsonArray();
            screening.screeningIds = new ArrayList<>();
            for (int i = 0; i < screeningIds.size(); i++) {
                JsonObject screeningObject = screeningIds.get(i).getAsJsonObject();

                screening.complexScreenings.add(screeningParser.parse(screeningObject));
                screening.screeningIds.add(screeningObject.get("_id").getAsString());
            }

            final String createdAt = object.get("date_created").getAsString();
            screening.createdAt = new DateTime(createdAt);
            final String updatedAt = object.get("last_modified").getAsString();
            screening.updatedAt = new DateTime(updatedAt);

            return screening;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing grouped screening: " + e.getMessage());
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

    private String getStatus(@NonNull String value, boolean localValue) {
        switch (value) {
            case STATUS_LOCAL_NEW:
            case STATUS_API_NEW:
                return localValue ? STATUS_API_NEW : STATUS_LOCAL_NEW;
            case STATUS_LOCAL_IN_PROGRESS:
            case STATUS_API_IN_PROGRESS:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_IN_PROGRESS;
            case STATUS_LOCAL_SUBMITTED:
            case STATUS_API_SUBMITTED:
                return localValue ? STATUS_API_SUBMITTED : STATUS_LOCAL_SUBMITTED;
            case STATUS_LOCAL_APPROVED:
            case STATUS_API_APPROVED:
                return localValue ? STATUS_API_APPROVED : STATUS_LOCAL_APPROVED;
            case STATUS_LOCAL_DECLINED_FINAL:
            case STATUS_API_DECLINED_FINAL:
                return localValue ? STATUS_API_DECLINED_FINAL : STATUS_LOCAL_DECLINED_FINAL;
            case STATUS_LOCAL_DECLINED_UNDER_REVIEW:
            case STATUS_API_DECLINED_UNDER_REVIEW:
                return localValue ? STATUS_API_DECLINED_UNDER_REVIEW : STATUS_LOCAL_DECLINED_UNDER_REVIEW;
            default:
                return STATUS_UNKNOWN;
        }
    }

}
