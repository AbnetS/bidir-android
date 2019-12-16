package com.gebeya.mobile.bidir.data.client.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.google.gson.JsonObject;

/**
 * Interface definition for a class responsible for parsing a {@link com.gebeya.mobile.bidir.data.client.Client}
 * object.
 */
public interface ClientParser {

    String NEW = "new";
    String API_NEW = "new";

    String SCREENING_IN_PROGRESS = "screening_in_progress";
    String API_SCREENING_IN_PROGRESS = "screening_inprogress";

    String ELIGIBLE = "eligible";
    String API_ELIGIBLE = "eligible";

    String INELIGIBLE = "ineligible";
    String API_INELIGIBLE = "ineligible";

    String LOAN_APPLICATION_NEW = "loan_application_new";
    String API_LOAN_APPLICATION_NEW = "loan_application_new";

    String LOAN_APPLICATION_IN_PROGRESS = "loan_application_in_progress";
    String API_LOAN_APPLICATION_IN_PROGRESS = "loan_application_inprogress";

    String LOAN_APPLICATION_ACCEPTED = "loan_application_accepted";
    String API_LOAN_APPLICATION_ACCEPTED = "loan_application_accepted";

    String LOAN_APPLICATION_REJECTED = "loan_application_rejected";
    String API_LOAN_APPLICATION_REJECTED = "loan_application_rejected";

    String LOAN_APPLICATION_DECLINED = "loan_application_declined";
    String API_LOAN_APPLICATION_DECLINED = "loan_application_declined";

    String ACAT_NEW = "ACAT_NEW";
    String API_ACAT_NEW = "new";

    String ACAT_IN_PROGRESS = "ACAT_IN_PROGRESS";
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
    String API_LOAN_GRANTED_NEW = "Loan-Granted";

    String LOAN_PAID = "LOAN_PAID";
    String API_LOAN_PAID = "loan_paid";

    String UNKNOWN_STATUS = "unknown_status";

    String GENDER_MALE = "male";
    String API_GENDER_MALE = "male";
    String API_GENDER_MALE_CAMEL_CASE = "Male";

    String GENDER_FEMALE = "female";
    String API_GENDER_FEMALE = "female";
    String API_GENDER_FEMALE_CAMEL_CASE = "Female";
    String UNKNOWN_GENDER = "unknown_gender";

    String MARITAL_STATUS_SINGLE = "single";
    String API_MARITAL_STATUS_SINGLE = "single";

    String MARITAL_STATUS_MARRIED = "married";
    String API_MARITAL_STATUS_MARRIED = "married";

    String MARITAL_STATUS_DIVORCED = "divorced";
    String API_MARITAL_STATUS_DIVORCED = "divorced";

    String MARITAL_STATUS_WIDOWED = "widowed";
    String API_MARITAL_STATUS_WIDOWED = "widowed";
    String UNKNOWN_MARITAL_STATUS = "unknown_marital_status";

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
     * Retrieve the API gender, given a local version.
     */
    String getApiGender(@NonNull String local);

    /**
     * Retrieve the local gender, given a API version.
     */
    String getLocalGender(@NonNull String api);

    /**
     * Retrieve the API marital status, given the local version.
     */
    String getApiMaritalStatus(@NonNull String local);

    /**
     * Retrieve the API marital status, given the type.
     */
    String getApiMaritalStatus(@NonNull MaritalStatus maritalStatus);

    /**
     * Retrieve the local marital status, given the API version.
     */
    String getLocalMaritalStatus(@NonNull String api);

    /**
     * Retrieve the local marital status, given the type.
     */
    String getLocalMaritalStatus(@NonNull MaritalStatus maritalStatus);
    /**
     * Get the {@link MaritalStatus} enum type given the local version, for UI purposes.
     */
    MaritalStatus getMaritalStatusType(@NonNull String local);

    /**
     * Parse the given JsonObject and return a Client object.
     *
     * @param object JsonObject to parseACATApplication from.
     * @return parsed Client object.
     * @throws Exception thrown if there was an error during the parsing process.
     */
    Client parse(@NonNull JsonObject object) throws Exception;
}