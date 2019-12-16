package com.gebeya.mobile.bidir.data.loanapplication;

import android.support.annotation.NonNull;

/**
 * Interface contract for the loan application status helper.
 */
public interface LoanApplicationStatusHelper {

    String NEW = "new";
    String API_NEW = "new";

    String IN_PROGRESS = "in_progress";
    String API_IN_PROGRESS = "inprogress";

    String SUBMITTED = "submitted";
    String API_SUBMITTED = "submitted";

    String ACCEPTED = "accepted";
    String API_ACCEPTED = "accepted";

    String DECLINED_FINAL = "rejected";
    String API_DECLINED_FINAL = "rejected";

    String DECLINED_UNDER_REVIEW = "declined_under_review";
    String API_DECLINED_UNDER_REVIEW = "declined_under_review";

    String UNKNOWN_STATUS = "unknown_status";

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