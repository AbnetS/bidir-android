package com.gebeya.mobile.bidir.ui.loan_proposal_summary;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalSummaryPresenter implements LoanProposalSummaryContract.Presenter {

    private final String clientId;

    private ACATApplication acatApplication;
    private LoanProposal loanProposal;

    private LoanProposalSummaryContract.View view;

    @Inject ACATApplicationLocalSource applicationLocal;
    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject SchedulersProvider schedulers;

    public LoanProposalSummaryPresenter(@NonNull String clientId) {
        this.clientId = clientId;

        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
        applicationLocal.getACATByClient(clientId)
                .flatMap(data -> {
                    acatApplication = data.get();
                    return loanProposalLocal.getProposalByClient(clientId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                            loanProposal = data.get();
                            loadData();
                        },
                        Throwable::printStackTrace
                );
    }

    private void loadData() {
        view.setTotalRevenue(acatApplication.estimatedTotalRevenue);
        view.setTotalCost(acatApplication.estimatedTotalCost);
        view.setNetIncome(acatApplication.estimatedNetIncome);
        view.setLoanProposed(loanProposal.loanProposed);
        view.setLoanRequested(loanProposal.loanRequested);
        view.setLoanApproved(loanProposal.loanApproved);
        view.setTotalDeductible(loanProposal.totalDeductible);
        view.setTotalLoanCost(loanProposal.totalCostOfLoan);
        view.setCashAtHand(loanProposal.cashAtHand); // TODO: Add Cash At hand.
        view.setRepayable(loanProposal.repayable);
    }

    @Override
    public void attachView(LoanProposalSummaryContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public LoanProposalSummaryContract.View getView() {
        return view;
    }
}
