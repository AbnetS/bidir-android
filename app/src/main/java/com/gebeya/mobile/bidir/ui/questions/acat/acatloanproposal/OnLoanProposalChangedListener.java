package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

/**
 * Created by Samuel K. on 7/2/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface OnLoanProposalChangedListener {
    void onDeductibleChanged(double totalDeductible);
    void onCostOfLoanChanged(double totalCostOfLoan);
    void onLoanProposedChanged(double loanProposed);
}
