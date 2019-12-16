package com.gebeya.mobile.bidir.data.task;

import android.support.annotation.NonNull;

/**
 * Concrete implementation for the {@link TaskHelper} interface
 */
public class BaseTaskHelper implements TaskHelper {

    @Override
    public String getApiType(@NonNull String local) {
        switch (local) {
            case TYPE_APPROVE:
                return API_TYPE_APPROVE;
            case TYPE_DECLINE:
                return API_TYPE_DECLINE;
            case TYPE_REVIEW:
                return API_TYPE_REVIEW;

        }
        return UNKNOWN_TYPE;
    }

    @Override
    public String getLocalType(@NonNull String api) {
        switch (api) {
            case API_TYPE_APPROVE:
                return TYPE_APPROVE;
            case API_TYPE_DECLINE:
                return TYPE_DECLINE;
            case API_TYPE_REVIEW:
                return TYPE_REVIEW;
        }
        return UNKNOWN_TYPE;
    }

    @Override
    public String getApiRef(@NonNull String local) {
        switch (local) {
            case REF_SCREENING:
                return API_REF_SCREENING;
            case REF_ACCOUNT:
                return API_REF_ACCOUNT;
            case REF_LOAN:
                return API_REF_LOAN;
            case REF_ACAT:
                return API_REF_ACAT;
            case REF_CLIENT_ACAT:
                return API_REF_CLIENT_ACAT;
        }
        return UNKNOWN_REF;
    }

    @Override
    public String getLocalRef(@NonNull String api) {
        switch (api) {
            case API_REF_SCREENING:
                return REF_SCREENING;
            case API_REF_ACCOUNT:
                return REF_ACCOUNT;
            case API_REF_LOAN:
                return REF_LOAN;
            case API_REF_ACAT:
                return REF_ACAT;
            case API_REF_CLIENT_ACAT:
                return REF_CLIENT_ACAT;
        }
        return UNKNOWN_REF;
    }

    @Override
    public String getApiStatus(@NonNull String local) {
        switch (local) {
            case STATUS_PENDING:
                return API_STATUS_PENDING;
            case STATUS_APPROVED:
                return API_STATUS_APPROVED;
            case STATUS_AUTHORIZED:
                return API_STATUS_AUTHORIZED;
            case STATUS_DONE:
                return API_STATUS_DONE;
            case STATUS_COMPLETED:
                return API_STATUS_COMPLETED;
        }
        return UNKNOWN_STATUS;
    }

    @Override
    public String getLocalStatus(@NonNull String api) {
        switch (api) {
            case API_STATUS_PENDING:
                return STATUS_PENDING;
            case API_STATUS_APPROVED:
                return STATUS_APPROVED;
            case API_STATUS_AUTHORIZED:
                return STATUS_AUTHORIZED;
            case API_STATUS_DONE:
                return STATUS_DONE;
            case API_STATUS_COMPLETED:
                return STATUS_COMPLETED;
        }
        return UNKNOWN_STATUS;
    }
}