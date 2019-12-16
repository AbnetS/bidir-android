package com.gebeya.mobile.bidir.ui.questions.acat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel K. on 3/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsLoanProposalFragment extends BaseFragment {

    Toolbar toolbar;
    BaseExpandableListAdapter listAdapter;
    ExpandableListView listView;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;

    RecyclerView netCashFlowRecyclerView, cumulativeCashFlowRecyclerView, cropCashFlowRecyclerView;
    ACATQuestionsNetCashFlowAdapter adapter;
    public static ACATQuestionsLoanProposalFragment newInstance() {
        return new ACATQuestionsLoanProposalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_loan_proposal_layout, container, false);

        toolbar = getView(R.id.acat_loan_proposal_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setTitle("Abebe Kebede");
        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        netCashFlowRecyclerView = getView(R.id.acat_net_cash_flow_recycler_view);
        cumulativeCashFlowRecyclerView = getView(R.id.acat_cumulative_cash_flow_section_items);
        cropCashFlowRecyclerView = getView(R.id.acat_crop_cash_flow_recycer_view);

        LinearLayoutManager netCashFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager cumulativeCashFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager cropCashFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        netCashFlowRecyclerView.setLayoutManager(netCashFlowLayoutManager);
        netCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

        cumulativeCashFlowRecyclerView.setLayoutManager(cumulativeCashFlowLayoutManager);
        cumulativeCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

//        cropCashFlowRecyclerView.setLayoutManager(cropCashFlowLayoutManager);
//        cropCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

        adapter = new ACATQuestionsNetCashFlowAdapter("", new ArrayList<>());

        netCashFlowRecyclerView.setAdapter(adapter);
        cumulativeCashFlowRecyclerView.setAdapter(adapter);
//        cropCashFlowRecyclerView.setAdapter(adapter);

//        initList();

        return root;
    }

    private void initList() {

        listView = root.findViewById(R.id.acat_expandable_list_view);

        listView.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition) {
                listView.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        });

        prepareInputAndActivitiesCostList();
//        listAdapter = new ACATCostEstimationExpandableListViewAdapter(getContext(), listDataHeader, listDataChild);



        listView.setAdapter(listAdapter);

    }

    private void prepareInputAndActivitiesCostList() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Crop CashFlow");

        List<String> childList = new ArrayList<>();
        childList.add("crop");
        childList.add("crop");


        listDataChild.put(listDataHeader.get(0), childList);

    }
}
