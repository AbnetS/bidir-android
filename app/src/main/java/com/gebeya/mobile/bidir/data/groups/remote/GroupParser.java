package com.gebeya.mobile.bidir.data.groups.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groups.Group;
import com.google.gson.JsonObject;

/**
 * Interface definition for a class responsible for parsing a {@link com.gebeya.mobile.bidir.data.groups.Group}
 */
public interface GroupParser {

    String NEW = "New";
    String API_NEW = "new";

    String SCREENING_IN_PROGRESS = "Screening_in_progress";
    String API_SCREENING_IN_PROGRESS = "screening_in_progress";

    String SCREENING_SUBMITTED = "Screening_submitted";
    String API_SCREENING_SUBMITTED = "submitted";

    String ELIGIBLE = "Eligible";
    String API_ELIGIBLE = "eligible";

    String INELIGIBLE = "Ineligible";
    String API_INELIGIBLE = "ineligible";

    String LOAN_APPLICATION_NEW = "Loan_application_new";
    String API_LOAN_APPLICATION_NEW = "loan_application_new";

    String LOAN_APPLICATION_IN_PROGRESS = "Loan_application_in_progress";
    String API_LOAN_APPLICATION_IN_PROGRESS = "loan_application_inprogress";

    String LOAN_APPLICATION_SUBMITTED = "Loan_application_submitted";
    String API_LOAN_APPLICATION_SUBMITTED = "loan_application_submitted";

    String LOAN_APPLICATION_ACCEPTED = "Loan_application_accepted";
    String API_LOAN_APPLICATION_ACCEPTED = "loan_application_accepted";

    String LOAN_APPLICATION_DECLINED = "Loan_application_declined";
    String API_LOAN_APPLICATION_DECLINED = "loan_application_declined";

    String ACAT_NEW = "ACAT_NEW";
    String API_ACAT_NEW = "ACAT_New";

    String ACAT_IN_PROGRESS = "ACAT_in_progress";
    String API_ACAT_IN_PROGRESS = "ACAT_IN_PROGRESS";

    String ACAT_SUBMITTED = "ACAT_SUBMITTED";
    String API_ACAT_SUBMITTED = "ACAT-Submitted";

    String ACAT_RESUBMITTED = "ACAT_RESUBMITTED";
    String API_ACAT_RESUBMITTED = "ACAT-Resubmitted";

    String ACAT_DECLINED_FOR_REVIEW = "ACAT_DECLINED_FOR_REVIEW";
    String API_ACAT_DECLINED_FOR_REVIEW = "ACAT_Declined_For_Review";

    String ACAT_AUTHORIZED = "ACAT_AUTHORIZED";
    String API_ACAT_AUTHORIZED = "ACAT-Authorized";

    String LOAN_GRANTED = "LOAN_GRANTED";
    String API_LOAN_GRANTED = "loan_granted";

    String LOAN_APPRAISAL_IN_PROGRESS = "LOAN_APPRAISAL_IN_PROGRESS";
    String API_LOAN_APPRAISAL_IN_PROGRESS = "appraisal_in_progress";

    String LOAN_PAYMENT_IN_PROGRESS = "LOAN_PAYMENT_IN_PROGRESS";
    String API_LOAN_PAYMENT_IN_PROGRESS = "payment_in_progress";

    String LOAN_PAID = "LOAN_PAID";
    String API_LOAN_PAID = "loan_paid";

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
     *
     * @param local status the local implementation accepts/uses
     * @return api implementation
     */
    String getApiStatus(@NonNull String local);

    /**
     * Parse the given object and return a {@link Group} object.
     * @throws Exception
     */
    Group parse(@NonNull JsonObject object) throws Exception;
}
