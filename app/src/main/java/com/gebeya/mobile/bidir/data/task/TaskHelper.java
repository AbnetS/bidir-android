package com.gebeya.mobile.bidir.data.task;

import android.support.annotation.NonNull;

/**
 * Utility functions for a Task helper, parsing the status and the types
 */
public interface TaskHelper {

    String TYPE_APPROVE = "approve";
    String API_TYPE_APPROVE = "approve";
    String TYPE_DECLINE = "decline";
    String API_TYPE_DECLINE = "decline";
    String TYPE_REVIEW = "review";
    String API_TYPE_REVIEW = "review";
    String UNKNOWN_TYPE = "unknown_type";

    String REF_SCREENING = "screening";
    String API_REF_SCREENING = "screening";
    String REF_ACCOUNT = "account";
    String API_REF_ACCOUNT = "account";
    String REF_LOAN = "loan";
    String API_REF_LOAN = "loan";
    String REF_ACAT = "ACAT";
    String API_REF_ACAT = "ACAT";
    String REF_CLIENT_ACAT = "CLIENT_ACAT";
    String API_REF_CLIENT_ACAT = "clientACAT";
    String UNKNOWN_REF = "unknown_ref";

    String STATUS_PENDING = "pending";
    String API_STATUS_PENDING = "pending";
    String STATUS_APPROVED = "approved";
    String API_STATUS_APPROVED = "approved";
    String STATUS_AUTHORIZED = "authorized";
    String API_STATUS_AUTHORIZED = "authorized";
    String STATUS_DONE = "done";
    String API_STATUS_DONE = "done";
    String STATUS_COMPLETED = "completed";
    String API_STATUS_COMPLETED = "completed";
    String UNKNOWN_STATUS = "unknown_status";

    /**
     * Retrieve the API type given the local type.
     */
     String getApiType(@NonNull String local);

    /**
     * Retrieve the local type given the API type.
     */
     String getLocalType(@NonNull String api);

    /**
     * Retrieve the API ref given the local type
     */
     String getApiRef(@NonNull String local);

    /**
     * Retrieve the local ref given the API type
     */
     String getLocalRef(@NonNull String api);

    /**
     * Retrieve the API status given the local version
     */
     String getApiStatus(@NonNull String local);

    /**
     * Retrieve the local status given the API version
     */
     String getLocalStatus(@NonNull String api);
}