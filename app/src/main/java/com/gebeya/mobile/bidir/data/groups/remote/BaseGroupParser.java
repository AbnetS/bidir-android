package com.gebeya.mobile.bidir.data.groups.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.SSLContextSpi;

/**
 * Concrete implementation of the {@link GroupParser}
 */
public class BaseGroupParser implements GroupParser {

    @Inject ClientParser clientParser;

    @Inject
    public BaseGroupParser() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public Group parse(@NonNull JsonObject object) throws Exception {
        try {
            final Group group = new Group();

            group._id = object.get("_id").getAsString();

            JsonObject createdByObject = object.get("created_by").getAsJsonObject();
            group.createdBy = createdByObject.get("_id").getAsString();

            if (!object.get("leader").isJsonNull()) {
                JsonObject leader = object.get("leader").getAsJsonObject();
                group.leaderId = leader.get("_id").getAsString();
            }

            if (!object.get("members").getAsJsonArray().isJsonNull()) {

                JsonArray memberList = object.get("members").getAsJsonArray();
                List<String> membersId = new ArrayList<>();
                for (int i = 0; i < memberList.size(); i++) {
                    JsonObject member = memberList.get(i).getAsJsonObject();
                    membersId.add(member.get("_id").getAsString());
                    group.clients.add(clientParser.parse(member));

                }
                group.membersId = membersId;
            }

            group.groupName = object.get("name").getAsString();
            group.membersCount = object.get("no_of_members").getAsInt();
            group.totalLoanAmount = object.get("total_amount").getAsDouble();
            group.totalGrantedAmount = object.get("total_granted_amount").getAsDouble();
            group.totalPaidAmount = object.get("total_paid_amount").getAsDouble();
            String apiStatus = object.get("status").getAsString();
            group.groupStatus = getLocalStatus(apiStatus);
            final String createdAt = object.get("date_created").getAsString();
            group.createdAt = new DateTime(createdAt);
            final String updatedAt = object.get("last_modified").getAsString();
            group.updatedAt = new DateTime(updatedAt);

            return group;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error Parsing Grouped Clients: " + e.getMessage());
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
            case NEW:
            case API_NEW:
                return localValue ? API_NEW : NEW;
            case SCREENING_IN_PROGRESS:
            case API_SCREENING_IN_PROGRESS:
                return localValue ? API_SCREENING_IN_PROGRESS : SCREENING_IN_PROGRESS;
            case SCREENING_SUBMITTED:
            case API_SCREENING_SUBMITTED:
                return localValue ? API_SCREENING_SUBMITTED : SCREENING_SUBMITTED;
            case ELIGIBLE:
            case API_ELIGIBLE:
                return localValue ? API_ELIGIBLE : ELIGIBLE;
            case INELIGIBLE:
            case API_INELIGIBLE:
                return localValue ? API_INELIGIBLE : INELIGIBLE;
            case LOAN_APPLICATION_NEW:
            case API_LOAN_APPLICATION_NEW:
                return localValue ? API_LOAN_APPLICATION_NEW : LOAN_APPLICATION_NEW;
            case LOAN_APPLICATION_IN_PROGRESS:
            case API_LOAN_APPLICATION_IN_PROGRESS:
                return localValue ? API_LOAN_APPLICATION_IN_PROGRESS : LOAN_APPLICATION_IN_PROGRESS;
            case LOAN_APPLICATION_SUBMITTED:
            case API_LOAN_APPLICATION_SUBMITTED:
                return localValue ? API_LOAN_APPLICATION_SUBMITTED : LOAN_APPLICATION_SUBMITTED;
            case LOAN_APPLICATION_ACCEPTED:
            case API_LOAN_APPLICATION_ACCEPTED:
                return localValue ? API_LOAN_APPLICATION_ACCEPTED : LOAN_APPLICATION_ACCEPTED;
            case LOAN_APPLICATION_DECLINED:
            case API_LOAN_APPLICATION_DECLINED:
                return localValue ? API_LOAN_APPLICATION_DECLINED : LOAN_APPLICATION_DECLINED;
            case ACAT_NEW:
            case API_ACAT_NEW:
                return localValue ? API_ACAT_NEW : ACAT_NEW;
            case ACAT_IN_PROGRESS:
            case API_ACAT_IN_PROGRESS:
                return localValue ? API_ACAT_IN_PROGRESS : ACAT_IN_PROGRESS;
            case ACAT_SUBMITTED:
            case API_ACAT_SUBMITTED:
                return localValue ? API_ACAT_SUBMITTED : ACAT_SUBMITTED;
            case ACAT_RESUBMITTED:
            case API_ACAT_RESUBMITTED:
                return localValue ? API_ACAT_RESUBMITTED : ACAT_RESUBMITTED;
            case ACAT_AUTHORIZED:
            case API_ACAT_AUTHORIZED:
                return localValue ? API_ACAT_AUTHORIZED : ACAT_AUTHORIZED;
            case ACAT_DECLINED_FOR_REVIEW:
            case API_ACAT_DECLINED_FOR_REVIEW:
                return localValue ? API_ACAT_DECLINED_FOR_REVIEW : ACAT_DECLINED_FOR_REVIEW;
            case LOAN_APPRAISAL_IN_PROGRESS:
            case API_LOAN_APPRAISAL_IN_PROGRESS:
                return localValue ? API_LOAN_APPRAISAL_IN_PROGRESS : LOAN_APPRAISAL_IN_PROGRESS;
            case LOAN_GRANTED:
            case API_LOAN_GRANTED:
                return localValue ? API_LOAN_GRANTED : LOAN_GRANTED;
            case LOAN_PAID:
            case API_LOAN_PAID:
                return localValue ? API_LOAN_PAID : LOAN_PAID;
            case LOAN_PAYMENT_IN_PROGRESS:
            case API_LOAN_PAYMENT_IN_PROGRESS:
                return localValue ? API_LOAN_PAYMENT_IN_PROGRESS : LOAN_PAYMENT_IN_PROGRESS;
            default:
                return UNKNOWN_STATUS;
        }
    }
}
