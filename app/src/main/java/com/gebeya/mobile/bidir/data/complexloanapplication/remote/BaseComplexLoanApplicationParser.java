package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

/**
 * Concrete implementation fo the {@link ComplexLoanApplicationParser} interface.
 */
public class BaseComplexLoanApplicationParser implements ComplexLoanApplicationParser {

    @Override
    public ComplexLoanApplication parse(@NonNull JsonObject object) throws Exception {
        try {
            final ComplexLoanApplication application = new ComplexLoanApplication();

            application._id = object.get("_id").getAsString();
            application.createdAt = new DateTime(object.get("date_created").getAsString());
            application.updatedAt = new DateTime(object.get("last_modified").getAsString());

            final JsonObject clientObject = object.get("client").getAsJsonObject();
            application.clientId = clientObject.get("_id").getAsString();

            application.createdBy = object.get("created_by").getAsString();
            application.branch = object.get("branch").getAsString();

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = getLocalStatus(apiStatus);
            if (localStatus.equals(STATUS_UNKNOWN)) throw new Exception("Unknown API LoanApplication status: " + apiStatus);
            application.status = localStatus;

            application.disclaimer = object.get("disclaimer").getAsString();
            application.hasSections = object.get("has_sections").getAsBoolean();

            application.forGroup = object.get("for_group").getAsBoolean();

            final String apiLayout = object.get("layout").getAsString();
            final String localLayout = getLocalLayout(apiLayout);
            if (localLayout.equals(LAYOUT_UNKNOWN)) throw new Exception("Unknown API LoanApplication layout: " + apiLayout);
            application.layout = localLayout;

            return application;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing ComplexLoanApplication: " + e.getMessage());
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
            case STATUS_LOCAL_ACCEPTED:
            case STATUS_API_ACCEPTED:
                return localValue ? STATUS_API_ACCEPTED : STATUS_LOCAL_ACCEPTED;
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

    @Override
    public String getLocalLayout(@NonNull String apiLayout) {
        switch (apiLayout) {
            case LAYOUT_API_TWO_COLUMNS:
                return LAYOUT_LOCAL_TWO_COLUMNS;
            case LAYOUT_API_THREE_COLUMNS:
                return LAYOUT_LOCAL_THREE_COLUMNS;
            default:
                return LAYOUT_UNKNOWN;
        }
    }

    @Override
    public String getApiLayout(@NonNull String localLayout) {
        switch (localLayout) {
            case LAYOUT_LOCAL_TWO_COLUMNS:
                return LAYOUT_API_TWO_COLUMNS;
            case LAYOUT_LOCAL_THREE_COLUMNS:
                return LAYOUT_API_THREE_COLUMNS;
            default:
                return LAYOUT_UNKNOWN;
        }
    }
}
