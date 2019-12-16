package com.gebeya.mobile.bidir.ui.loan_proposal_summary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryActivity;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalSummaryFragment extends BaseFragment implements LoanProposalSummaryContract.View {

    private LoanProposalSummaryContract.Presenter presenter;

    public static LoanProposalSummaryFragment newInstance(@NonNull String clientId) {
        final LoanProposalSummaryFragment fragment = new LoanProposalSummaryFragment();

        final Bundle args = new Bundle();
        args.putString(CropSummaryActivity.ARG_CLIENT_ID, clientId);
        fragment.setArguments(args);

        return fragment;
    }

    private EditText totalRevenueValueLabel;
    private EditText totalCostValueLabel;
    private EditText netIncomeValueLabel;
    private EditText loanRequestedValueLabel;
    private EditText loanProposedValueLabel;
    private EditText loanApprovedValueLabel;
    private EditText totalDeductibleValueLabel;
    private EditText totalLoanCostValueLabel;
    private EditText cashAtHandValueLabel;
    private EditText repayableValueLabel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null.");

        final String clientId = args.getString(CropSummaryActivity.ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID is null.");

        presenter = new LoanProposalSummaryPresenter(clientId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_loan_proposal_summary_layout, container, false);

        initView();
        return root;
    }

    private void initView() {
        totalRevenueValueLabel = getEd(R.id.loanProposalSummaryTotalRevenueValueLabel);
        totalRevenueValueLabel.setEnabled(false);

        totalCostValueLabel = getEd(R.id.loanProposalSummaryTotalCostValueLabel);
        totalCostValueLabel.setEnabled(false);

        netIncomeValueLabel = getEd(R.id.loanProposalSummaryNetIncomeValueLabel);
        netIncomeValueLabel.setEnabled(false);

        loanRequestedValueLabel = getEd(R.id.loanProposalSummaryLoanRequestedValueLabel);
        loanRequestedValueLabel.setEnabled(false);

        loanProposedValueLabel = getEd(R.id.loanProposalSummaryLoanProposedValueLabel);
        loanProposedValueLabel.setEnabled(false);

        loanApprovedValueLabel = getEd(R.id.loanProposalSummaryLoanApprovedValueLabel);
        loanApprovedValueLabel.setEnabled(false);

        totalDeductibleValueLabel = getEd(R.id.loanProposalSummaryTotalDeductibleValueLabel);
        totalDeductibleValueLabel.setEnabled(false);

        totalLoanCostValueLabel = getEd(R.id.loanProposalSummaryTotalLoanCostValueLabel);
        totalLoanCostValueLabel.setEnabled(false);

        cashAtHandValueLabel = getEd(R.id.loanProposalSummaryCashAtHandValueLabel);
        cashAtHandValueLabel.setEnabled(false);

        repayableValueLabel = getEd(R.id.loanProposalSummaryRepayableValueLabel);
        repayableValueLabel.setEnabled(false);
    }

    @Override
    public void setTotalRevenue(double value) {
        totalRevenueValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setTotalCost(double value) {
        totalCostValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setNetIncome(double value) {
        netIncomeValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setLoanRequested(double value) {
        loanRequestedValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setLoanProposed(double value) {
        loanProposedValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setLoanApproved(double value) {
        loanApprovedValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setTotalDeductible(double value) {
        totalDeductibleValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setTotalLoanCost(double value) {
        totalLoanCostValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setCashAtHand(double value) {
        cashAtHandValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void setRepayable(double value) {
        repayableValueLabel.setText(String.valueOf(value));
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void attachPresenter(LoanProposalSummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
