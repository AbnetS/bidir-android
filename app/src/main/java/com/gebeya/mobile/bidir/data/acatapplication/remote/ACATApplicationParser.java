package com.gebeya.mobile.bidir.data.acatapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by abuti on 5/17/2018.
 */

public interface ACATApplicationParser {

    String STATUS_LOCAL_NEW = "NEW";
    String STATUS_API_NEW = "new";

    String STATUS_LOCAL_IN_PROGRESS = "ACAT_IN_PROGRESS";
    String STATUS_API_IN_PROGRESS = "inprogress";

    String STATUS_LOCAL_ACAT_IN_PROGRESS = "ACAT_INPROGRESS";
    String STATUS_API_ACAT_IN_PROGRESS = "acat_inprogress";


    String STATUS_LOCAL_SUBMITTED = "SUBMITTED";
    String STATUS_API_SUBMITTED = "submitted";

    String STATUS_LOCAL_RESUBMITTED = "RESUBMITTED";
    String STATUS_API_RESUBMITTED = "resubmitted";

    String STATUS_LOCAL_AUTHORIZED = "AUTHORIZED";
    String STATUS_API_AUTHORIZED = "authorized";

    String STATUS_LOCAL_DECLINED_FOR_REVIEW = "DECLINED_FOR_REVIEW";
    String STATUS_API_DECLINED_FOR_REVIEW = "declined_for_review";

    String STATUS_LOCAL_LOAN_PAID = "LOAN_PAID";
    String STATUS_API_LOAN_PAID = "loan_paid";

    String STATUS_LOCAL_LOAN_GRANTED = "LOAN_GRANTED";
    String STATUS_API_LOAN_GRANTED = "loan_granted";

    String STATUS_UNKNOWN = "STATUS_UNKNOWN";


    /**
     * Parse for an ACAT Application
     *
     * @param object to parseRespose from.
     * @return ACAT Application object parsed.
     * @throws Exception throws error if one occur during parsing.
     */
    ACATApplication parse(@NonNull JsonObject object) throws Exception;

    List<String> toList(@NonNull JsonArray array);

    /**
     * Return the local status given the API version.
     * @param apiStatus API status whose local value to look up.
     * @return local version of the status.
     */
    String getLocalStatus(@NonNull String apiStatus);

    /**
     * Return the API status given the local version.
     * @param localStatus local status whose API value to look up.
     * @return API version for the given local status.
     */
    String getApiStatus(@NonNull String localStatus);
}
