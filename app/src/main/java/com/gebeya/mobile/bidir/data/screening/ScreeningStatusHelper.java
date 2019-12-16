package com.gebeya.mobile.bidir.data.screening;

import android.support.annotation.NonNull;

/**
 * Interface outlining both API and app screening statuses.
 */
public interface ScreeningStatusHelper {

    String NEW = "new";
    String API_NEW = "new";

    String IN_PROGRESS = "in_progress";
    String API_IN_PROGRESS = "screening_inprogress";

    String SUBMITTED = "submitted";
    String API_SUBMITTED = "submitted";

    String APPROVED = "approved";
    String API_APPROVED = "approved";

    String DECLINED_FINAL = "declined_final";
    String API_DECLINED_FINAL = "declined_final";

    String DECLINED_UNDER_REVIEW = "declined_under_review";
    String API_DECLINED_UNDER_REVIEW = "declined_under_review";

    String UNKNOWN_STATUS = "unknown_status";

    String LOAN_ACCEPTED = "accepted";
    String LOAN_API_ACCEPTED = "accepted";

    String LOAN_DECLINED_FINAL = "rejected";
    String LOAN_API_DECLINED_FINAL = "rejected";

    String LOAN_APP_DECLINED_UNDER_REVIEW = "declined_under_review";
    String LOAN_API_DECLINED_UNDER_REVIEW = "declined_under_review";

    /**
     * Return a local status implementation for the API.
     *
     * @param api status the API accepts
     * @return local implementation
     */
    String getLocalStatus(@NonNull String api);

    /**
     * Return an api status implementation for local usage.
     * @param local status the local implementation accepts/uses
     * @return api implementation
     */
    String getApiStatus(@NonNull String local);
}