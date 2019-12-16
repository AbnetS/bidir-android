package com.gebeya.mobile.bidir.data.complexloanapplication.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.google.gson.JsonObject;

/**
 * Interface definition for {@link com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication}
 * parser.
 */
public interface ComplexLoanApplicationParser {

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

    String LAYOUT_LOCAL_TWO_COLUMNS = "TWO_COLUMNS";
    String LAYOUT_API_TWO_COLUMNS = "TWO_COLUMNS";

    String LAYOUT_LOCAL_THREE_COLUMNS = "THREE_COLUMNS";
    String LAYOUT_API_THREE_COLUMNS = "THREE_COLUMNS";

    String LAYOUT_UNKNOWN = "LAYOUT_UNKNOWN";

    /**
     * Parses the given jsonObject and returns a {@link ComplexLoanApplication} object.
     *
     * @param object Input object to parseACATApplication from.
     * @return Parsed ComplexLoanApplication object.
     * @throws Exception thrown if there was an error during parsing.
     */
    ComplexLoanApplication parse(@NonNull JsonObject object) throws Exception;

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

    /**
     * Get the local application layout, given the API version.
     *
     * @param apiLayout API layout whose local version is needed.
     * @return local layout version for the given API layout version.
     */
    String getLocalLayout(@NonNull String apiLayout);

    /**
     * Get the API application layout, given the local version.
     * @param localLayout local layout whose API version is needed.
     * @return API layout version for the given local layout version.
     */
    String getApiLayout(@NonNull String localLayout);
}