package com.gebeya.mobile.bidir.data.loanProposal.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.google.gson.JsonObject;

/**
 *
 * An interface for parsing {@link com.gebeya.mobile.bidir.data.loanProposal.LoanProposal}
 *
 */

public interface LoanProposalParser {

    String STATUS_LOCAL_NEW = "NEW";
    String STATUS_API_NEW = "new";

    String STATUS_LOCAL_IN_PROGRESS = "IN_PROGRESS";
    String STATUS_API_IN_PROGRESS = "inprogress";

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

    String STATUS_UNKNOWN = "STATUS_UNKNOWN";

    /**
     * Parse the given object and return {@link LoanProposal} Object.
     *
     * @param jsonObject Input object to parse the {@link LoanProposal} from.
     *
     * @return Parsed {@link LoanProposal} Object.
     * @throws Exception Thrown if there was an error during parsing.
     */

    LoanProposal parse(@NonNull JsonObject jsonObject) throws Exception;

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
