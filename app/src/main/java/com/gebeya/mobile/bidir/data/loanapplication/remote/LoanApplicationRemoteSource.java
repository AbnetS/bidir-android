package com.gebeya.mobile.bidir.data.loanapplication.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract for the {@link com.gebeya.mobile.bidir.data.loanapplication.LoanApplication} remote API
 */
public interface LoanApplicationRemoteSource {

    /**
     * Update a loan application on the remote API server.
     *
     * @param response pageResponse containing the data to saveLoanProposal the API with.
     * @return LoanApplication that has been updated.
     */
    Observable<LoanApplication> update(@NonNull LoanApplicationResponse response);

    /**
     * Update the {@link LoanApplication} status on the API side.
     *
     * @param application {@link LoanApplication} to saveLoanProposal.
     * @param status      new status for the loan application
     * @param remark      Any possible comment/remark for the {@link LoanApplication}
     */
    Observable<LoanApplication> updateApiStatus(@NonNull LoanApplication application,
                                                @NonNull String status, @Nullable String remark);

    /**
     * Decline or approve a loan application.
     *
     * @param application Loan Application whose status need to be declined or approved
     * @param status      status to saveLoanProposal the application.
     */
    Observable<LoanApplication> declineAcceptFinal(@NonNull LoanApplication application, @NonNull String status);

    /**
     * Download all the {@link LoanApplication} objects.
     *
     * @return List of parsed LoanApplication pageResponse objects from the API.
     */
    Observable<List<LoanApplicationResponse>> getAll();

    /**
     * Download a single application with a given Id
     *
     * @param applicationId Id of the application to be retrieved.
     * @return Loan application pageResponse object
     */
    Observable<LoanApplicationResponse> downloadApplication(@NonNull String applicationId);

    /**
     * Parse the given JSON object into a {@link LoanApplicationResponse} object.
     *
     * @param object input JSON object to parseResponse from.
     * @return Parsed {@link LoanApplicationResponse} object
     * @throws Exception if there was a problem during the parsing process.
     */
    LoanApplicationResponse parse(@NonNull JsonObject object) throws Exception;

    /**
     * Parse the given JSON object into a {@link LoanApplication} object.
     *
     * @param object input JSON object to parseResponse from.
     * @return Parsed {@link LoanApplication} object
     * @throws Exception if there was a problem during the parsing process.
     */
    LoanApplication parseLoanApplicationAndClient(@NonNull JsonObject object) throws Exception;

    /**
     * Parse the given JSON object
     *
     * @param object input JSON object to parseResponse from.
     * @return Parsed {@link LoanApplication} object.
     * @throws Exception if there was a problem with parsing.
     */
    LoanApplication parseLoanApplicationOnly(@NonNull JsonObject object) throws Exception;

    /**
     * Create a Loan Application. This is called once the screening process is approved.
     *
     * @param client Client to use for the creation purpose.
     */
    Observable<LoanApplicationResponse> create(@NonNull Client client);
}