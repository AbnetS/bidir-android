package com.gebeya.mobile.bidir.data.groupedacat.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedacat.GroupedACAT;
import com.google.gson.JsonObject;

public interface GroupedACATParser {

    String STATUS_LOCAL_NEW = "NEW";
    String STATUS_API_NEW = "new";

    String STATUS_LOCAL_IN_PROGRESS = "ACAT_IN_PROGRESS";
    String STATUS_API_IN_PROGRESS = "inprogress";

    String STATUS_LOCAL_ACAT_IN_PROGRESS = "ACAT_INPROGRESS";
    String STATUS_API_ACAT_IN_PROGRESS = "acat_inprogress";


    String STATUS_LOCAL_SUBMITTED = "ACAT_SUBMITTED";
    String STATUS_API_SUBMITTED = "submitted";

    String STATUS_LOCAL_RESUBMITTED = "ACAT_RESUBMITTED";
    String STATUS_API_RESUBMITTED = "resubmitted";

    String STATUS_LOCAL_AUTHORIZED = "ACAT_AUTHORIZED";
    String STATUS_API_AUTHORIZED = "authorized";

    String STATUS_LOCAL_DECLINED_FOR_REVIEW = "ACAT_DECLINED_FOR_REVIEW";
    String STATUS_API_DECLINED_FOR_REVIEW = "declined_for_review";

    String STATUS_LOCAL_LOAN_PAID = "LOAN_PAID";
    String STATUS_API_LOAN_PAID = "loan_paid";

    String STATUS_LOCAL_LOAN_GRANTED = "LOAN_GRANTED";
    String STATUS_API_LOAN_GRANTED = "loan_granted";

    String STATUS_UNKNOWN = "STATUS_UNKNOWN";

    /**
     * Parses the given JsonObject and returns a {@link GroupedACAT} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return parsed ComplexScreening object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    GroupedACAT parse(@NonNull JsonObject object) throws Exception;

    /**
     * Return the local status given the API version.
     *
     * @param apiStatus API status whose local value to lookup.
     * @return local version for the given API status.
     */
    String getLocalStatus(@NonNull String apiStatus);

    /**
     * Return the API status given the local version.
     *
     * @param localStatus local status whose API value to lookup.
     * @return API version for the given local status.
     */
    String getApiStatus(@NonNull String localStatus);
}
