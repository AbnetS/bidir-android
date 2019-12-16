package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;

/**
 * Created by Samuel K. on 8/18/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface OnMenuButtonClickListener {
    void onLoanProposalDataReturned(@NonNull LoanProposal loanProposal);

    void onNetCashFlowReturned(@NonNull CashFlow cashFlow, @NonNull ACATApplication acatApplication);
}
