package com.gebeya.mobile.bidir.data.groupedcomplexloan.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.google.gson.JsonObject;

public interface GroupedComplexLoanParser {

    String STATUS_LOCAL_NEW = "NEW";
    String STATUS_API_NEW = "new";

    String STATUS_LOCAL_IN_PROGRESS = "IN_PROGRESS";
    String STATUS_API_IN_PROGRESS = "inprogress";

    String STATUS_LOCAL_SUBMITTED = "SUBMITTED";
    String STATUS_API_SUBMITTED = "submitted";

    String STATUS_LOCAL_ACCEPTED = "ACCEPTED";
    String STATUS_API_ACCEPTED = "accepted";

    String STATUS_LOCAL_DECLINED_FINAL = "DECLINED_FINAL";
    String STATUS_API_DECLINED_FINAL = "rejected";

    String STATUS_LOCAL_DECLINED_UNDER_REVIEW = "DECLINED_UNDER_REVIEW";
    String STATUS_API_DECLINED_UNDER_REVIEW = "declined_under_review";

    String STATUS_UNKNOWN = "STATUS_UNKNOWN";

    /**
     * Parses the given JsonObject and returns a {@link GroupedComplexLoan} object.
     *
     * @param object JsonObject to parseResponse from.
     * @return parsed ComplexScreening object.
     * @throws Exception thrown if there was an error during the parsing.
     */
    GroupedComplexLoan parse(@NonNull JsonObject object) throws Exception;

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
