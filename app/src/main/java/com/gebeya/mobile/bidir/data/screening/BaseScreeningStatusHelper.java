package com.gebeya.mobile.bidir.data.screening;

import android.support.annotation.NonNull;

/**
 * Implementation for the {@link ScreeningStatusHelper} helper class
 */
public class BaseScreeningStatusHelper implements ScreeningStatusHelper {

    @Override
    public String getLocalStatus(@NonNull String api) {
        switch (api) {
            case API_NEW:
                return NEW;
            case API_IN_PROGRESS:
                return IN_PROGRESS;
            case API_SUBMITTED:
                return SUBMITTED;
            case API_APPROVED:
                return APPROVED;
            case API_DECLINED_FINAL:
                return DECLINED_FINAL;
            case API_DECLINED_UNDER_REVIEW:
                return DECLINED_UNDER_REVIEW;
        }
        return UNKNOWN_STATUS;
    }

    @Override
    public String getApiStatus(@NonNull String local) {
        switch (local) {
            case NEW:
                return API_NEW;
            case IN_PROGRESS:
                return API_IN_PROGRESS;
            case SUBMITTED:
                return API_SUBMITTED;
            case APPROVED:
                return API_APPROVED;
            case DECLINED_FINAL:
                return API_DECLINED_FINAL;
            case DECLINED_UNDER_REVIEW:
                return API_DECLINED_UNDER_REVIEW;
            case LOAN_ACCEPTED:
                return LOAN_API_ACCEPTED;
            case LOAN_DECLINED_FINAL:
                return LOAN_API_DECLINED_FINAL;

        }
        return UNKNOWN_STATUS;
    }
}
