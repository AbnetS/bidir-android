package com.gebeya.mobile.bidir.data.loanProposal.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.google.gson.JsonObject;

/**
 *
 * Concrete implementation for the {@link LoanProposalParser} interface.
 */

public class BaseLoanProposalParser implements LoanProposalParser {
    @Override
    public LoanProposal parse(@NonNull JsonObject jsonObject) throws Exception {
        try {
            final LoanProposal loanProposal = new LoanProposal();

            final JsonObject loanDetail = jsonObject.get("loan_detail").getAsJsonObject();

            loanProposal.clientId = jsonObject.get("client").getAsString();
            loanProposal.clientACATId = jsonObject.get("client_acat").getAsString();

            loanProposal.totalCostOfLoan = loanDetail.get("total_cost_of_loan").getAsDouble();
            loanProposal.totalDeductible = loanDetail.get("total_deductibles").getAsDouble();
            loanProposal.maxAmount = loanDetail.get("max_amount").getAsDouble();

            loanProposal._id = jsonObject.get("_id").getAsString();
            loanProposal.loanApproved = jsonObject.get("loan_approved").getAsDouble();
            loanProposal.loanRequested = jsonObject.get("loan_requested").getAsDouble();
            loanProposal.loanProposed = jsonObject.get("loan_proposed").getAsDouble();
            loanProposal.repayable = jsonObject.get("repayable").getAsDouble();
            loanProposal.totalRevenue = jsonObject.get("total_revenue").getAsDouble();
            loanProposal.totalCost = jsonObject.get("total_cost").getAsDouble();

            final String apiStatus = jsonObject.get("status").getAsString();
            final String localStatus = getLocalStatus(apiStatus);
            if (localStatus.equals(LoanProposalParser.STATUS_UNKNOWN)) {
                throw new Exception("Unknown API Loan Proposal status: " + apiStatus);
            }
            loanProposal.status = localStatus;

            return loanProposal;
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error Parsing Loan Proposal: " + e.getMessage());
        }
    }

    @Override
    public String getLocalStatus(@NonNull String apiStatus) {
        return getStatus(apiStatus, false);
    }

    @Override
    public String getApiStatus(@NonNull String localStatus) {
        return getStatus(localStatus, true);
    }

    private String getStatus(@NonNull String value, boolean localValue) {
        switch (value) {
            case STATUS_LOCAL_NEW:
            case STATUS_API_NEW:
                return localValue ? STATUS_API_NEW : STATUS_LOCAL_NEW;
            case STATUS_LOCAL_IN_PROGRESS:
            case STATUS_API_IN_PROGRESS:
                return localValue ? STATUS_API_IN_PROGRESS : STATUS_LOCAL_IN_PROGRESS;
            case STATUS_LOCAL_SUBMITTED:
            case STATUS_API_SUBMITTED:
                return localValue ? STATUS_API_SUBMITTED : STATUS_LOCAL_SUBMITTED;
            case STATUS_LOCAL_RESUBMITTED:
            case STATUS_API_RESUBMITTED:
                return localValue ? STATUS_API_RESUBMITTED : STATUS_LOCAL_RESUBMITTED;
            case STATUS_LOCAL_AUTHORIZED:
            case STATUS_API_AUTHORIZED:
                return localValue ? STATUS_API_AUTHORIZED : STATUS_LOCAL_AUTHORIZED;
            case STATUS_LOCAL_DECLINED_FOR_REVIEW:
            case STATUS_API_DECLINED_FOR_REVIEW:
                return localValue ? STATUS_API_DECLINED_FOR_REVIEW : STATUS_LOCAL_DECLINED_FOR_REVIEW;
            case STATUS_LOCAL_LOAN_PAID:
            case STATUS_API_LOAN_PAID:
                return localValue ? STATUS_API_LOAN_PAID : STATUS_LOCAL_LOAN_PAID;
            default:
                return STATUS_UNKNOWN;
        }
    }
}
