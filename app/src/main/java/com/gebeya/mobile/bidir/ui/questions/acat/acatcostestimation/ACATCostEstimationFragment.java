package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepository;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATQuestionsNetCashFlowAdapter;
import com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield.ACATEstimatedYieldActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation.ACATItemCostEstimationFragment;
import com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield.ACATEstimatedRevenueActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Samuel K. on 2/21/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationFragment extends BaseFragment implements
        ErrorDialogCallback {

    private static final String INPUTS_AND_ACTIVITY_COST = "Inputs And Activity Costs";
    private static final String INPUT = "INPUT";
    private static final String LABOUR_COST = "LABOUR COST";
    private static final String OTHER_COSTS = "OTHER COSTS";
    private static final String SEED = "Seed";
    private static final String FERTILIZER = "Fertilizers";
    private static final String CHEMICALS = "Chemicals";
    private static final String SECTION_ID = "section_id";
    private static final String SUB_TOTAL = "sub_total";
    private static final String TITLE_LABEL = "title_label";
    private static final String CLIENT_ID = "client";
    private static final String CROP_ACAT_ID = "cropACATId";
    private static final String IS_SEED = "isSeed";
    private static final String SUBTOTAL_LABEL = "subtotalLabel";
    Toolbar toolbar;
    BaseExpandableListAdapter listAdapter;
    ExpandableListView listView;
    TextView totalLabel;

    Button menuNext;
    RecyclerView cashOutFlowRecyclerView, netCashOutFlowRecyclerView;
    ACATQuestionsNetCashFlowAdapter acatQuestionsNetCashFlowAdapter;
    ACATCostEstimationCashFlowAdapter acatCostEstimationCashFlowAdapter;
    private TextView cropLabel;


    List<String> listDataHeader, headerSubtotal;
    HashMap<String, List<String>> listDataChild, childSubtotal;
    private int lastExpandedPosition = -1;
    private String clientId;
    private ArrayList<String> titleLabels;

    private ACATApplication acatApplication;
    private List<ACATItem> acatItems;
    private Client c;
    private List<ACATCostSection> costSectionList, costSections;
    private ACATCostSection costSection;
    private List<String> cropIds;
    private ErrorDialog errorDialog;
    private String clientName;
    private String firstExpenceMonth = "";
    private CropACAT cropACAT;
    private List<String> cashFlowData;
    private List<CashFlow> cashFlowList;
    private CashFlow cashFlow;

    private String childSubTotal;
    private String costEstimationId;

    private boolean isEditing;
    private boolean isMonitoring;

    private String cropACATId;

    @Inject
    ClientRepoSource clientRepo;
    @Inject
    ClientLocalSource clientLocal;
    @Inject
    ACATCostEstimationLoadState loadState;
    @Inject
    ACATCostEstimationUpdateState updateState;
    @Inject
    ACATApplicationRepository applicationRepo;
    @Inject
    CropACATLocalSource cropACATLocalSource;
    @Inject
    ACATCostSectionLocalSource costSectionLocal;
    @Inject
    ACATItemLocalSource acatItemLocal;
    @Inject
    SchedulersProvider schedulers;
    @Inject CashFlowLocalSource cashFlowLocal;

    private WaitingDialog waitingDialog;


    public static ACATCostEstimationFragment newInstance() {
        return new ACATCostEstimationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acatItems = new ArrayList<>();
        cropIds = new ArrayList<>();
        costSectionList = new ArrayList<>();
        costSections = new ArrayList<>();
        titleLabels = new ArrayList<>();
        cashFlowData = new ArrayList<>();
        cashFlowList = new ArrayList<>();
        cashFlow = new CashFlow();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_question_step1, container, false);

        clientId = getArguments().getString(ACATCostEstimationActivity.ARG_CLIENT_ID);
        isEditing = getArguments().getBoolean(ACATCostEstimationActivity.IS_EDITING);
        isMonitoring = getArguments().getBoolean(ACATCostEstimationActivity.IS_MONITORING);

        cropACATId = getArguments().getString(ACATCostEstimationActivity.ARG_CROP_ACAT_ID);
        toolbar = getView(R.id.acat_step_1_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());
        cropLabel = getTv(R.id.acat_crop_item_label);

        totalLabel = getView(R.id.acat_cost_estimation_total_cost_label);
        menuNext = getBt(R.id.acat_cost_estimation_next_button);

        menuNext.setOnClickListener(v -> {
            final Intent intent = new Intent(getActivity(), ACATEstimatedRevenueActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(ACATEstimatedYieldActivity.ARG_MONITOR, isMonitoring);
            intent.putExtra(ACATEstimatedYieldActivity.ARG_CLIENT_ID, clientId);
            intent.putExtra(ACATEstimatedYieldActivity.ARG_CROP_ACAT_ID, cropACATId);
            getParent().finish();
            startActivity(intent);
        });

        netCashOutFlowRecyclerView = getView(R.id.acat_net_cash_flow_recycler_view);
        cashOutFlowRecyclerView = getView(R.id.cash_out_flow_recycler_view);

        LinearLayoutManager netCashOutFlowLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager cashOutFlowLayoutManager = new LinearLayoutManager(getContext());

        netCashOutFlowRecyclerView.setLayoutManager(netCashOutFlowLayoutManager);
        netCashOutFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        cashOutFlowRecyclerView.setLayoutManager(cashOutFlowLayoutManager);

        getClientCropACAT(clientId, cropACATId);

        return root;
    }

    @SuppressLint("CheckResult")
    private void getClientCropACAT(String clientId, String cropACATId) {
        loadState.setLoading();
        showLoadingProgress();

        applicationRepo.fetch(clientId)
                .flatMap(application -> {
                    acatApplication = application;
                    return cropACATLocalSource.get(cropACATId);
                })
                .flatMap(cropACATs -> {
                    cropACAT = cropACATs.get();
                    return costSectionLocal.getAll();
                })
                .flatMap(acatCostSection -> {
                    costSectionList.clear();
                    costSectionList.addAll(acatCostSection);
                    return costSectionLocal.get(cropACAT.costSectionId);
                })
                .flatMap(acatCostSectionData -> {
                    costSection = acatCostSectionData.get();
                    // costSections.add(costSection);
                    return cashFlowLocal.getAll();
                }).flatMap(cashFlows -> {
                    cashFlowList.clear();
                    cashFlowList.addAll(cashFlows);
                    return acatItemLocal.getAll();
                })
                .flatMap(acatItem -> {
                    acatItems.clear();
                    acatItems.addAll(acatItem);
                    return clientRepo.fetch(clientId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(client -> {
                            c = client;
                            loadState.setComplete();
                            onLoadingComplete();
                        },
                        throwable -> {
                            loadState.setError(throwable);
                            onLoadingFailed(throwable);
                        });
    }

    private void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    private void initList() {

        listView = root.findViewById(R.id.acat_expandable_list_view);

        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {

            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                switch (groupPosition) {
                    case 0:
                        acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(INPUT, costSections, cashFlowList));
                        break;
                    case 1:
                        acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(LABOUR_COST, costSections, cashFlowList));
                        break;
                    case 2:
                        acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(OTHER_COSTS, costSections, cashFlowList));
                        break;
                }
                return true;
            } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                int childPosition = ExpandableListView.getPackedPositionChild(id);

                if (groupPosition == 0) {
                    switch (childPosition) {
                        case 0:
                            acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(SEED, costSections, cashFlowList));
                            break;
                        case 1:
                            acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(FERTILIZER, costSections, cashFlowList));
                            break;
                        case 2:
                            acatCostEstimationCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(CHEMICALS, costSections, cashFlowList));
                            break;

                    }
                }
                return true;
            }
            return false;
        });
        listView.setOnGroupExpandListener(groupPosition -> {
            final Bundle arg = new Bundle();
            final String sectionId;
            clientName = clientId;

            if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition) {
                listView.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;

            if (groupPosition == 1) {
                titleLabels.clear();
                titleLabels.add(cropACAT.cropName);
                titleLabels.add(LABOUR_COST);
                sectionId = ACATCostEstimationUtlity.getACATItemSectionId(LABOUR_COST, costSections);
                arg.putString(SECTION_ID, sectionId);
                arg.putString(SUBTOTAL_LABEL, LABOUR_COST);
                arg.putStringArrayList(TITLE_LABEL, titleLabels);

                openFragment(arg);
            } else if (groupPosition == 2) {
                titleLabels.clear();
                titleLabels.add(cropACAT.cropName);
                titleLabels.add(OTHER_COSTS);
                sectionId = ACATCostEstimationUtlity.getACATItemSectionId(OTHER_COSTS, costSections);
                arg.putString(SECTION_ID, sectionId);
                arg.putString(SUBTOTAL_LABEL, OTHER_COSTS);
                arg.putStringArrayList(TITLE_LABEL, titleLabels);
                openFragment(arg);
            }
        });

        prepareInputAndActivitiesCostList();
        listAdapter = new ACATCostEstimationExpandableListViewAdapter(getContext(), listDataHeader, listDataChild, headerSubtotal, childSubtotal);

        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        listView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {

            final String selected = (String) listAdapter.getChild(groupPosition, childPosition);
            final Bundle arg = new Bundle();
            final String sectionId;
            final String subTotal;
            titleLabels.add(listDataHeader.get(0));
            clientName = clientId;
            switch (selected) {
                case SEED:
                    titleLabels.clear();
                    titleLabels.add(cropACAT.cropName);
                    titleLabels.add(INPUT);
                    titleLabels.add(SEED);
                    arg.putBoolean(IS_SEED, true);
                    sectionId = ACATCostEstimationUtlity.getACATItemSectionId(SEED, costSections);
                    subTotal = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(SEED, costSections));
                    arg.putString(SECTION_ID, sectionId);
                    arg.putString(SUB_TOTAL, subTotal);
                    arg.putString(SUBTOTAL_LABEL, SEED);
                    arg.putStringArrayList(TITLE_LABEL, titleLabels);
                    break;
                case FERTILIZER:
                    titleLabels.clear();
                    titleLabels.add(cropACAT.cropName);
                    titleLabels.add(INPUT);
                    titleLabels.add(FERTILIZER);
                    sectionId = ACATCostEstimationUtlity.getACATItemSectionId(FERTILIZER, costSections);
                    subTotal = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(FERTILIZER, costSections));
                    arg.putString(SECTION_ID, sectionId);
                    arg.putString(SUB_TOTAL, subTotal);
                    arg.putString(SUBTOTAL_LABEL, FERTILIZER);
                    arg.putStringArrayList(TITLE_LABEL, titleLabels);
                    break;
                case CHEMICALS:
                    titleLabels.clear();
                    titleLabels.add(cropACAT.cropName);
                    titleLabels.add(INPUT);
                    titleLabels.add(CHEMICALS);
                    sectionId = ACATCostEstimationUtlity.getACATItemSectionId(CHEMICALS, costSections);
                    subTotal = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(CHEMICALS, costSections));
                    arg.putString(SECTION_ID, sectionId);
                    arg.putString(SUB_TOTAL, subTotal);
                    arg.putString(SUBTOTAL_LABEL, CHEMICALS);
                    arg.putStringArrayList(TITLE_LABEL, titleLabels);
                    break;
                default:
                    break;
            }

            ACATCostEstimationFragment.this.openFragment(arg);
            return true;
        });
    }

    private void openFragment(@NonNull Bundle arg) {
        Fragment fragment = ACATItemCostEstimationFragment.newInstance();
        arg.putString(CLIENT_ID, clientName);
        arg.putString(CROP_ACAT_ID, cropACATId);
        arg.putBoolean(ACATCostEstimationActivity.IS_MONITORING, isMonitoring);
        fragment.setArguments(arg);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.questionsACATContainer, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    private void prepareInputAndActivitiesCostList() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        headerSubtotal = new ArrayList<>();
        childSubtotal = new HashMap<>();

        headerSubtotal.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(INPUT, costSections)));
        headerSubtotal.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(LABOUR_COST, costSections)));
        headerSubtotal.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(OTHER_COSTS, costSections)));

        listDataHeader.add(INPUT);
        listDataHeader.add(LABOUR_COST);
        listDataHeader.add(OTHER_COSTS);
        addInput(INPUT, costSections);
        addLabourCost(LABOUR_COST, costSections);
        addOtherCosts(OTHER_COSTS, costSections);

        List<String> childSubTotalList = new ArrayList<>();
        childSubTotalList.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(SEED, costSections)));
        childSubTotalList.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(FERTILIZER, costSections)));
        childSubTotalList.add(String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(CHEMICALS, costSections)));

        //totalLabel.setText(ACATCostEstimationUtlity.getACATItemSectionId(INPUTS_AND_ACTIVITY_COST,costSections));

        childSubtotal.put(headerSubtotal.get(0), childSubTotalList);
        childSubtotal.put(headerSubtotal.get(1), childSubTotalList);
        childSubtotal.put(headerSubtotal.get(2), childSubTotalList);
    }

    private void addInput(String parent, List<ACATCostSection> costSections) {
        List<String> childList = new ArrayList<>();
        ACATCostSection costSection = ACATCostEstimationUtlity.getcostSection(parent, costSections);

        List<ACATCostSection> costSectionList = ACATCostEstimationUtlity.getCostSubSection(parent, costSection, costSections);
        for (ACATCostSection acatCostSection : costSectionList) {
            childList.add(acatCostSection.title);
        }
        listDataChild.put(listDataHeader.get(0), childList);
    }

    private void addLabourCost(String parent, List<ACATCostSection> costSections) {
        List<String> childList = new ArrayList<>();
        ACATCostSection costSection = ACATCostEstimationUtlity.getcostSection(parent, costSections);

        List<ACATCostSection> costSectionList = ACATCostEstimationUtlity.getCostSubSection(parent, costSection, costSections);
        for (ACATCostSection acatCostSection : costSectionList) {
            childList.add(acatCostSection.title);
        }
        listDataChild.put(listDataHeader.get(1), childList);

    }

    private void addOtherCosts(String parent, List<ACATCostSection> costSections) {
        List<String> childList = new ArrayList<>();
        ACATCostSection costSection = ACATCostEstimationUtlity.getcostSection(parent, costSections);

        List<ACATCostSection> costSectionList = ACATCostEstimationUtlity.getCostSubSection(parent, costSection, costSections);
        for (ACATCostSection acatCostSection : costSectionList) {
            childList.add(acatCostSection.title);
        }
        listDataChild.put(listDataHeader.get(2), childList);

    }

    @SuppressLint("CheckResult")
    private List<ACATCostSection> getGroupCostSection(ACATCostSection costSection) {
        List<ACATCostSection> costSectionList = new ArrayList<>();
        int size = costSection.subSectionIDs.size();

        for (int i = 0; i < size; i++) {
            costSectionLocal.get(costSection.subSectionIDs.get(i))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(acatCostSectionData ->
                            costSectionList.add(acatCostSectionData.get()));
        }

        return costSectionList;
    }

    @SuppressLint("CheckResult")
    private List<ACATCostSection> getChildCostSection(ACATCostSection costSection) {
        List<ACATCostSection> costSectionList = new ArrayList<>();
        int size = costSection.subSectionIDs.size();

        for (int i = 0; i < size; i++) {
            costSectionLocal.get(costSection.subSectionIDs.get(i))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(acatCostSectionData ->
                            costSectionList.add(acatCostSectionData.get()));
        }

        return costSectionList;
    }

    private void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(
                getString(R.string.cost_estimation_loading_state)
        );
        waitingDialog.show(getChildFragmentManager(), null);
    }

    private void onLoadingFailed(Throwable throwable) {
        throwable.printStackTrace();
        hideLoadingProgress();

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.ACAT_FORM_LOAD_GENERIC_ERROR;
        showError(Result.createError(message, error));

        loadState.reset();
    }

    private void showError(Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;
        if (message == null) {
            message = getString(R.string.preliminary_info_update_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    private void onLoadingComplete() {
        hideLoadingProgress();
        setTitle(Utils.formatName(
                c.firstName,
                c.surname,
                c.lastName
        ));

        costSections = ACATCostEstimationUtlity.getCostSectionList(costSection, costSectionList);
        costSections.add(costSection);
        cropLabel.setText(cropACAT.cropName);
        firstExpenceMonth = cropACAT.firstExpenseMonth;

        totalLabel.setText(String.valueOf(costSection.estimatedSubTotal));
        cashFlowData.clear();
        for (int i = 0; i < 12; i++) {
            cashFlowData.add(" - ");
        }

        acatCostEstimationCashFlowAdapter = new ACATCostEstimationCashFlowAdapter(firstExpenceMonth, cashFlowData);
        cashOutFlowRecyclerView.setAdapter(acatCostEstimationCashFlowAdapter);
        acatQuestionsNetCashFlowAdapter = new ACATQuestionsNetCashFlowAdapter(firstExpenceMonth, cashFlowData);
        acatQuestionsNetCashFlowAdapter.notify(ACATCostEstimationUtlity.getCostEstimationItemCashFlow(INPUTS_AND_ACTIVITY_COST, costSections, cashFlowList));
        netCashOutFlowRecyclerView.setAdapter(acatQuestionsNetCashFlowAdapter);
        acatCostEstimationCashFlowAdapter.notifyDataSetChanged();

        initList();
        loadState.reset();
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

    @Override
    public void onErrorDialogDismissed() {
        getParent().finish();
    }
}
