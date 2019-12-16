package com.gebeya.mobile.bidir.ui.questions.acat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield.ACATEstimatedYieldAdapter;

import java.util.ArrayList;

/**
 * Created by Samuel K. on 3/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsEstimatedPriceFragment extends BaseFragment {

    Toolbar toolbar;

    TextView menuDone;
    Button nextButton, previousButton;
    RecyclerView netCashFlowRecyclerView, cashOutFlowRecyclerView;
    ACATQuestionsNetCashFlowAdapter acatQuestionsNetCashFlowAdapter;
    ACATEstimatedYieldAdapter acatQuestionsCashFlowAdapter;


    public static ACATQuestionsEstimatedPriceFragment newInstance() {
        return new ACATQuestionsEstimatedPriceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_estimated_price_layout, container, false);

        toolbar = getView(R.id.acat_estimated_yield_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setTitle("Abebe Kebede");
        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        nextButton = getBt(R.id.acat_estimated_yield_next_step_button);
        nextButton.setOnClickListener(view -> {
            Fragment fragment = ACATQuestionsLoanProposalFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.questionsACATContainer, fragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        previousButton = getBt(R.id.acat_estimated_yield_previous_step_button);
        previousButton.setOnClickListener(view -> {
            Fragment fragment = ACATQuestionsMarketableYieldFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.questionsACATContainer, fragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        menuDone = getTv(R.id.acat_menu_done);
        menuDone.setOnClickListener(view -> {

        });

        netCashFlowRecyclerView = getView(R.id.acat_net_cash_flow_recycler_view);
        cashOutFlowRecyclerView = getView(R.id.cash_out_flow_recycler_view);

        LinearLayoutManager netCashFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager cashOutFlowLayoutManage = new LinearLayoutManager(getContext());

        netCashFlowRecyclerView.setLayoutManager(netCashFlowLayoutManager);
        cashOutFlowRecyclerView.setLayoutManager(cashOutFlowLayoutManage);

        netCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

        acatQuestionsNetCashFlowAdapter = new ACATQuestionsNetCashFlowAdapter("", new ArrayList<>());
        acatQuestionsCashFlowAdapter = new ACATEstimatedYieldAdapter("");

        netCashFlowRecyclerView.setAdapter(acatQuestionsNetCashFlowAdapter);
        cashOutFlowRecyclerView.setAdapter(acatQuestionsCashFlowAdapter);

        return root;
    }
}
