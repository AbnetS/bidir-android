package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATQuestionsNetCashFlowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


public class LoanAssessmentFragment extends BaseFragment {

    private RecyclerView netCashFlowView;

    private ACATCropItemExpandableListAdapter cropCashFlowAdapter;
    private ExpandableListView cropCashFlow;

    private String clientId;
    private String acatId;

    List<String> cropCashFlowHeader;
    HashMap<String, List<CropACAT>> cropCashFlowChild;
    private List<CropACAT> cropACATList;
    private CashFlow applicationCashFlow;
    private ACATApplication acatApplication;
    private List<CashFlow> cumulativeCropCashFlow;
    private LoanProposal loanProposal;

    TextView appTotalCost, appTotalRevenue, appNetIncome;


    private ACATQuestionsNetCashFlowAdapter adapter;
    private WaitingDialog waitingDialog;


    public static OnMenuButtonClickListener listener;

    @Inject ACATLoanProposalLoadState loadState;
    @Inject CropACATLocalSource cropACATLocalSource;
    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject ACATCostSectionLocalSource costSectionLocal;
    @Inject SchedulersProvider scheduler;
    @Inject ACATApplicationLocalSource acatApplicationLocal;

    public static LoanAssessmentFragment newInstance(OnMenuButtonClickListener l) {
        LoanAssessmentFragment fragment = new LoanAssessmentFragment();
        listener = l;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cropACATList = new ArrayList<>();
        cumulativeCropCashFlow = new ArrayList<>();
        loanProposal = new LoanProposal();

        clientId = getArguments().getString(ACATLoanProposalActivity.CLIENT_ID);
        acatId = getArguments().getString(ACATLoanProposalActivity.ACAT_ID);

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.assesment_result_layout, container, false);


        netCashFlowView = getView(R.id.acat_net_cash_flow_recycler_view);

        LinearLayoutManager netCashFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        netCashFlowView.setLayoutManager(netCashFlowLayoutManager);
        netCashFlowView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

        appTotalCost = getTv(R.id.loan_assessment_total_cost_label);
        appTotalRevenue = getTv(R.id.loan_assessment_total_revenue_label);
        appNetIncome = getTv(R.id.loan_assessment_net_income_label);

        fetchACATItem();


        return root;
    }

    @SuppressLint("CheckResult")
    private void fetchACATItem() {
//        loadState.setLoading();
        showLoadingProgress();

        cropACATLocalSource.getCropACATByApp(acatId)
                .flatMap(cropACATs -> {
                    this.cropACATList.clear();
                    cropACATList.addAll(cropACATs);
                    return acatApplicationLocal.get(acatId);
                })
                .flatMap(acatApplicationData -> {
                    acatApplication = acatApplicationData.get();
                    return cashFlowLocal.get(acatApplication._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.NET_ACATAPP_CASH_FLOW_TYPE);
                })
                .subscribeOn(scheduler.background())
                .observeOn(scheduler.ui())
                .subscribe(applicationCashFlowData -> {
                    applicationCashFlow = applicationCashFlowData.get(0);
                    onLoadingComplete();
                });
//                .flatMap(applicationCashFlowData -> {
//                    applicationCashFlow = applicationCashFlowData.getById(0);
//                    return cashFlowLocal.getById(cropACAT._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);
//                })
//                .subscribeOn(scheduler.background())
//                .observeOn(scheduler.ui())
//                .subscribe( -> {
//                    cumulativeCropCashFlow = cropCashFlowData.getById(0);
//                    onLoadingComplete();
//                });

    }

    private void onLoadingComplete() {
        hideLoadingProgress();

        for (int i = 0; i < cropACATList.size(); i++) {
            cashFlowLocal.get(cropACATList.get(i)._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                    .subscribeOn(scheduler.background())
                    .observeOn(scheduler.ui())
                    .subscribe(cashFlows -> cumulativeCropCashFlow.add(cashFlows.get(0)));
        }

        initExpandableList();
        appTotalCost.setText(String.valueOf(acatApplication.estimatedTotalCost));
        appTotalRevenue.setText(String.valueOf(acatApplication.estimatedTotalRevenue));
        appNetIncome.setText(String.valueOf(acatApplication.estimatedNetIncome));

        loanProposal.totalCost = acatApplication.estimatedTotalCost;
        loanProposal.totalRevenue = acatApplication.estimatedTotalRevenue;

        if (listener != null) {
            listener.onNetCashFlowReturned(applicationCashFlow, acatApplication);
        }

        adapter = new ACATQuestionsNetCashFlowAdapter("September", new ArrayList<>());
        netCashFlowView.setAdapter(adapter);
        adapter.notify(applicationCashFlow);

//        loadState.reset();
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

    private void initExpandableList() {
        cropCashFlow = root.findViewById(R.id.cropCashFlowExpandableView);

//        cropCashFlow.setOnGroupExpandListener(groupPosition -> {
//            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
//                cropCashFlow.collapseGroup(lastExpandedPosition);
//            }
//            lastExpandedPosition = groupPosition;
//        });

        prepareExpandableListView();

        cropCashFlowAdapter = new ACATCropItemExpandableListAdapter(getContext(), cropCashFlowHeader, cropCashFlowChild, cumulativeCropCashFlow);

        cropCashFlow.setAdapter(cropCashFlowAdapter);
    }

    private void prepareExpandableListView() {
        cropCashFlowHeader = new ArrayList<>();
        cropCashFlowChild = new HashMap<>();

        cropCashFlowHeader.add("Crops CashFlow");
        cropCashFlowHeader.add("Crops Income");

        List<CropACAT> childList = new ArrayList<>();
        childList.addAll(cropACATList);

        cropCashFlowChild.put(cropCashFlowHeader.get(0), childList);
        cropCashFlowChild.put(cropCashFlowHeader.get(1), childList);

    }
}
