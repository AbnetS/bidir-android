package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.local.LoanProposalLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProduct;
import com.gebeya.mobile.bidir.data.loanproduct.local.LoanProductLocalSource;
import com.gebeya.mobile.bidir.data.loanproductitem.LoanProductItem;
import com.gebeya.mobile.bidir.data.loanproductitem.local.LoanProductItemLocalSource;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalFragment extends BaseFragment implements OnLoanProposalChangedListener, ErrorDialogCallback {

    private TextView noCostOfLoanLabel, noDeductibleLabel;
    RecyclerView deductibleList, costOfLoanList;
    LinearLayout totalDeductableContainer, totalCostOfLoanContainer;

    private String clientId;
    private String acatId;

    private String lRequested;
    private String lProposed;
    private String lApproved;
    ACATApplication acatApplication;
    LoanProduct loanProduct;
    List<LoanProductItem> deductibles;
    List<LoanProductItem> costOfLoan;

    DeductibleAdapter deductibleAdapter;
    CostOfLoanAdapter costOfLoanAdapter;

    public static OnMenuButtonClickListener listener;
    private LoanProposal loanProposal;

    String loanRequested;
    String loanProposed;
    double totalDeductible;
    double totalCostOfLoan;
    double cashDisbursed;
    double totalRepayable;

    @Inject ACATApplicationLocalSource acatApplicationLocal;
    @Inject ACATLoanProposalLoadState loadState;
    @Inject LoanProductItemLocalSource loanProductItemLocal;
    @Inject LoanProductLocalSource loanProductLocal;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject LoanProposalLocalSource loanProposalLocal;
    @Inject SchedulersProvider scheduler;
    EditText loanRequestedInput, loanProposedInput;
    TextView totalDeductibleLabel, totalCostOfLoanLabel;
    TextView cashDisbursedLabel, totalRepayableLabel;
    TextView netIncomeValueLabel;
    TextView maxAmountLabel;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    public static LoanProposalFragment newInstance(@NonNull OnMenuButtonClickListener l) {
        LoanProposalFragment fragment = new LoanProposalFragment();
        listener = l;

        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deductibles = new ArrayList<>();
        costOfLoan = new ArrayList<>();
        loanProposal = new LoanProposal();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.loan_proposal_layout, container, false);

        clientId = getArguments().getString(ACATLoanProposalActivity.CLIENT_ID);
        acatId = getArguments().getString(ACATLoanProposalActivity.ACAT_ID);
        LinearLayoutManager costOfLoanManager = new LinearLayoutManager(getContext());
        LinearLayoutManager deductibleManager = new LinearLayoutManager(getContext());

        deductibleList = getView(R.id.deductible_item_recycler_view);
        costOfLoanList = getView(R.id.cost_of_loan_item_recycler_view);

        totalCostOfLoanContainer = getView(R.id.total_cost_of_loan_container);
        totalDeductableContainer = getView(R.id.total_deductibles_container);
        deductibleList.setLayoutManager(deductibleManager);
        costOfLoanList.setLayoutManager(costOfLoanManager);

        totalDeductibleLabel = getTv(R.id.total_deductible);
        totalCostOfLoanLabel = getTv(R.id.total_cost_of_loan);

        cashDisbursedLabel = getTv(R.id.cash_disbursed);
        totalRepayableLabel = getTv(R.id.repayable);

        noCostOfLoanLabel = getTv(R.id.loan_no_cost_of_loan_label);
        noDeductibleLabel = getTv(R.id.loan_no_deductible_lable);

        netIncomeValueLabel = getTv(R.id.loan_proposal_net_income_value_label);
        maxAmountLabel = getTv(R.id.loan_proposal_max_amount_value_label);

        initEditText();
        fetchLoanProductItems(clientId);

        return root;
    }

    private void initEditText() {
        loanRequestedInput = getEd(R.id.loan_requested_input);
        loanProposedInput = getEd(R.id.loan_proposed_input);


        loanProposedInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                lProposed = editable.toString().trim();
                if (!lProposed.isEmpty()) {
                    loanProposed = editable.toString().trim();

                    if (costOfLoanAdapter != null) {
                        costOfLoanAdapter.notify(loanProposed);
                    }
                    if (deductibleAdapter != null)
                        deductibleAdapter.notify(loanProposed);
                    loanProposal.loanProposed = Double.parseDouble(loanProposed);
                } else {
                    loanProposal.loanProposed = 0;
                }
            }
        });

        loanRequestedInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                lRequested = editable.toString().trim();
                if (!lRequested.isEmpty()) {
                    loanRequested = editable.toString().trim();
                    loanProposal.loanRequested = Double.parseDouble(loanRequested);
                } else {
                    loanProposal.loanRequested = Double.parseDouble(loanRequested);
                }
            }
        });


    }

    @SuppressLint("CheckResult")
    private void fetchLoanProductItems(String clientId) {
//        loadState.setLoading();
        showLoadingProgress();

        loanProposalRepo.fetchByClient(clientId)
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(loanProposal -> {
                    if (loanProposal != null) {
                        this.loanProposal = loanProposal;
                        loanRequested = String.format(Locale.ENGLISH, "%.2f", loanProposal.loanRequested);
                        loanProposed = String.format(Locale.ENGLISH, "%.2f", loanProposal.loanProposed);

                        maxAmountLabel.setText(String.format(Locale.ENGLISH, "%.2f", loanProposal.maxAmount));
                        loanProposedInput.setText(String.format(Locale.ENGLISH, "%.2f", loanProposal.loanProposed));
                        loanRequestedInput.setText(String.format(Locale.ENGLISH, "%.2f", loanProposal.loanRequested));
                        maxAmountLabel.setText(String.valueOf(loanProposal.maxAmount));
                    }
                }, this::showErrorMessage);
        acatApplicationLocal.getACATByClient(clientId)
                .flatMap(acatApplicationData -> {
                    acatApplication = acatApplicationData.get();
                    return loanProductLocal.get(acatApplication.loanProductID);
                })
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(loanProductData -> {
                    loanProduct = loanProductData.get();
                    loadLoanProductItems();
                    netIncomeValueLabel.setText(String.valueOf(acatApplication.estimatedNetIncome));
                    onLoadingComplete();
                }, this::showErrorMessage);
    }




    @SuppressLint("CheckResult")
    private void loadLoanProductItems() {
//        for (int i = 0; i < loanProduct.costOfLoanIDs.size(); i++) {
//            loanProductItemLocal.getById(loanProduct.costOfLoanIDs.getById(i))
//                    .subscribeOn(scheduler.background())
//                    .subscribe(loanProductItemData -> costOfLoan.add(loanProductItemData.getById()));
//        }

        Observable.fromIterable(loanProduct.costOfLoanIDs)
                .flatMap(s -> loanProductItemLocal.get(s))
                .subscribe(loanProductItemData -> costOfLoan.add(loanProductItemData.get()));

        Observable.fromIterable(loanProduct.deductibleIDs)
                .flatMap(s -> loanProductItemLocal.get(s))
                .subscribe(loanProductItemData -> deductibles.add(loanProductItemData.get()));
//        for (int i = 0; i < loanProduct.deductibleIDs.size(); i++) {
//            loanProductItemLocal.getById(loanProduct.deductibleIDs.getById(i))
//                    .subscribeOn(scheduler.background())
//                    .observeOn(scheduler.ui())
//                    .subscribe(loanProductItemData -> {
//                        deductibles.add(loanProductItemData.getById());
//
//                    });
//        }
    }

    @Override
    public void onDeductibleChanged(double totalDeductible) {
        totalDeductibleLabel.setText(String.format(Locale.getDefault(), "%.2f", totalDeductible));
        this.totalDeductible = totalDeductible;
    }

    @Override
    public void onCostOfLoanChanged(double totalCostOfLoan) {
        totalCostOfLoanLabel.setText(String.format(Locale.getDefault(), "%.2f", totalCostOfLoan));
        this.totalCostOfLoan = totalCostOfLoan;
    }

    @Override
    public void onLoanProposedChanged(double loanProposed) {
        if (loanProposed == 0) return;
        cashDisbursed = loanProposed - totalDeductible;
        totalRepayable = loanProposed + totalCostOfLoan;
        cashDisbursedLabel.setText(String.format(Locale.getDefault(), "%.2f", cashDisbursed));
        totalRepayableLabel.setText(String.format(Locale.getDefault(), "%.2f", totalRepayable));
        loanProposal.loanRequested = Double.parseDouble(loanRequested);
        loanProposal.totalCostOfLoan = totalCostOfLoan;
        loanProposal.totalDeductible = totalDeductible;
        loanProposal.repayable = totalRepayable;
        loanProposal.cashAtHand = cashDisbursed;


        loanProposalLocal.put(loanProposal)
                .subscribeOn(scheduler.background())
                .subscribe();

        if (listener != null) {
            listener.onLoanProposalDataReturned(loanProposal);
        }

    }

    private void onLoadingComplete() {
        hideLoadingProgress();

        deductibleAdapter = new DeductibleAdapter(this, deductibles, loanProposed);
        costOfLoanAdapter = new CostOfLoanAdapter(this, costOfLoan, loanProposed);

        showNoCostOfLoanLabel(costOfLoan.isEmpty());
        showNoDeductibleLabel(deductibles.isEmpty());

        deductibleList.setAdapter(deductibleAdapter);
        costOfLoanList.setAdapter(costOfLoanAdapter);
    }

    private void showNoDeductibleLabel(boolean show) {
        noDeductibleLabel.setVisibility(show ? View.VISIBLE : View.GONE);
        totalDeductableContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showNoCostOfLoanLabel(boolean show) {
        noCostOfLoanLabel.setVisibility(show ? View.VISIBLE : View.GONE);
        totalCostOfLoanContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void hideLoadingProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(
                getString(R.string.loan_proposal_loading_state)
        );
        waitingDialog.show(getChildFragmentManager(), null);
    }

    private void showError(Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.acat_questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.questions_error_title),
                message,
                extra
        );

        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }
    private void showErrorMessage(Throwable throwable) {
        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)) {
            hideUpdatingProgress();
            return;
        }

        final String message = ApiErrors.CLIENT_ACAT_UPDATE_GENERIC_ERROR;
        hideUpdatingProgress();
        showError(Result.createError(message, error));
    }

    private void hideUpdatingProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorDialogDismissed() {

    }
}
