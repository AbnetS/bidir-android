package com.gebeya.mobile.bidir.data.loanProposal.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/17/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface LoanProposalRemoteSource {

    /**
     * Get all the {@link LoanProposal} objects.
     *
     * @return List of parsed Loan Proposal pageResponse objects from the API.
     */
    Observable<List<LoanProposal>> getAll();

    /**
     * Get a specific client loan proposal with client id.
     * @param clientId Client id to be taken from.
     * @return One Loan proposal for the given client id.
     */
    Observable<LoanProposal> getOneByClient(@NonNull String clientId);

    /**
     * Get Loan proposal with a given id;
     * @param loanProposalId Loan proposal to getById the ID from.
     * @return Loan Proposal.
     */
    Observable<LoanProposal> get(@NonNull String loanProposalId);

    /**
     * Create a Loan Proposal to a specific client
     *
     * @param acatApplication Client's ACAT application to getById infos like client Id and application Id.
     * @param loanProduct Needed to provide the max loan amount.
     * @return A specific Loan Proposal for a client.
     */
    Observable<LoanProposal> create(@NonNull ACATApplication acatApplication, @NonNull LoanProduct loanProduct);

    /**
     * Update a Loan Proposal. This can be either changing status or value.
     *
     * @param loanProposal Loan Proposal containing the data to be updated.
     *@param cashFlow To getById the net cash flow from.
     * @param acatApplication to getById total revenue and cost.
     * @return An updated Client Loan proposal.
     */
    Observable<LoanProposal> saveLoanProposal(@NonNull LoanProposal loanProposal, @NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication);

    Observable<LoanProposal> updateLoanProposal(@NonNull LoanProposal loanProposal);
    /**
     * Update status for a specific client loan proposal;
     *
     * @param loanProposal Client specific loan proposal
     * @param status status to be sent to the API.
     * @return
     */
    Observable<LoanProposal> updateApiStatus(@NonNull LoanProposal loanProposal, @NonNull String status);

}
