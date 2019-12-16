package com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationSync;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemHelper;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemRequest;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.remote.ACATItemRemoteSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.groupedlist.GroupedList;
import com.gebeya.mobile.bidir.data.groupedlist.local.GroupedListLocalSource;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.TempErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.otheritem.OtherCallBack;
import com.gebeya.mobile.bidir.ui.common.dialogs.otheritem.OtherDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostUpdater;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATItemDto;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;
import com.gebeya.mobile.bidir.ui.summary_costs.SummaryCostsFragment;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 6/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATItemCostEstimationFragment extends BaseFragment implements ErrorDialogCallback,
        OnCashFlowChangedListener,
        OtherCallBack{

    public static final String CLIENT_ID = "client";
    public static final String SECTION_ID = "section_id";
    private static final String SUB_TOTAL = "sub_total";
    public static final String TITLE_LABEL = "title_label";
    private static final String IS_SEED = "isSeed";
    private static final String SEED_VARIETY = "Variety";
    private static final String SEED_SOURCE = "Seed Source";
    public static final String SUBTOTAL_LABEL = "subtotalLabel";
    private static final String IS_EDITING = "isEditing";
    public static final String CROP_ACAT_ID = "cropACATId";

    private static final String UNION_OPTION = "Union";
    private static final String FACTORY_OPTION = "Factory/ Institution";
    private static final String TRADER_OPTION = "Trader";

    @Nullable public static final String label = "Label";

    Toolbar toolbar;
    View acatItemShield, estimatedYieldShield, actualYieldShield;
    LinearLayout remarkContainer, seedVarietyContainer, seedSourceContainer, acatItemInputContainer;

    EditText estimatedACATItemUnitInput, actualACATItemUnitInput, acatItemCashFlowInput;
    TextView menuDone;
    TextView subTotalLabel, acatItemCashFlowLabel, acatItemRemainingLabel;
    EditText estimatedQuantityInput, estimatedUnitPriceInput, actualQuantityInput, actualUnitPriceInput;
    EditText remarkInput;
    EditText seedVarietyInput;
    TextView subTotalItemLabel;
    TextView estimatedTotalPriceLabel, actualTotalPriceLabel;
    RecyclerView  cashOutFlowRecyclerView;
    LinearLayout titleIndicatorContainer;
    ACATItemCashFlowAdapter acatQuestionsCashFlowAdapter;
    BaseExpandableListAdapter expandableListAdapter;
    ExpandableListView listView;
    CheckBox unionOption;
    CheckBox factoryOption;
    CheckBox traderOption;

    ACATItem newACATItem;


    private double remainingValue;

    private String sectionId;
    private String subTotal;
    private boolean isEditing;
    private boolean isMonitoring;
    private String cropACATId;
    private String other;

    private String estimatedQuantity, actualQuantity;
    private String estimatedUnitPrice, actualUnitPrice;
    private String remark;
    private String variety;
    private List<String> seedSource;
    private boolean isSeed;
    private String itemLabel;
    private Client c;
    private String clientId;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<ACATItem> acatItems;
    private List<GroupedList> groupedLists;
    private CropACAT cropACAT;
    private ACATItem acatItem;
    private List<ACATItemValue> acatItemValueList;
    private ACATItemValue estimatedACATItemValue;
    private ACATItemValue actualACATItemValue;
    private List<CashFlow> cashFlowList;
    private CashFlow estimatedCashFlow;
    private CashFlow actualCashFlow;
    private List<String> cashFlowData;
    private ErrorDialog errorDialog;
    private WaitingDialog waitingDialog;
    private OtherDialog otherDialog;
    private TempErrorDialog tempErrorDialog;
    private ArrayList<String> items;
    private ArrayList<String> parentLables;
    private ArrayList<String> acatItemLabels;
    private ArrayList<String> seedItems;
    private int lastExpandedPosition = -1;
    private double estimatedTotalPrice;
    private double actualTotalPrice;
    private int index;
    private boolean isChild;
    private List<String> groupedListId;
    private ACATCostSection section;

    @Inject CropACATLocalSource cropACATLocalSource;
    @Inject ACATItemLocalSource acatItemLocal;
    @Inject ACATItemCostEstimationLoadState loadState;
    @Inject SchedulersProvider schedulers;
    @Inject GroupedListLocalSource groupedLocalSource;
    @Inject ClientRepoSource clientRepo;
    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;
    @Inject ACATCostSectionLocalSource costSectionLocal;
    @Inject ACATApplicationSyncLocalSource localSource;
    @Inject ACATItemRemoteSource acatItemRemoteSource;
    @Inject ACATApplicationLocalSource acatLocal;

    private String firstExpenceMonth;
    private ACATItemDto acatItemDto;
    private ACATCostUpdater updater;
    private ACATApplicationSync sync;
    private ACATApplication acatApplication;
    private ACATItemResponse response;

    private String menuLabel;


    public static ACATItemCostEstimationFragment newInstance() {
        return new ACATItemCostEstimationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acatItems = new ArrayList<>();
        items = new ArrayList<>();
        groupedLists = new ArrayList<>();
        listDataHeader = new ArrayList<>();
        listDataHeader.clear();
        listDataChild = new HashMap<>();
        listDataChild.clear();
        parentLables = new ArrayList<>();
        acatItemLabels = new ArrayList<>();
        seedItems = new ArrayList<>();
        cashFlowList = new ArrayList<>();
        estimatedCashFlow = new CashFlow();
        cashFlowData = new ArrayList<>();
        acatItemDto = new ACATItemDto();
        updater = new ACATCostUpdater();
        acatItemValueList = new ArrayList<>();
        seedSource = new ArrayList<>();
        sync = new ACATApplicationSync();
        groupedListId = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_question_step1_data_entry, container, false);

        clientId = getArguments().getString(CLIENT_ID);
        sectionId = getArguments().getString(SECTION_ID);
        subTotal = getArguments().getString(SUB_TOTAL);
        itemLabel = getArguments().getString(SUBTOTAL_LABEL);
        isEditing = getArguments().getBoolean(IS_EDITING, false);
        isMonitoring = getArguments().getBoolean(ACATCostEstimationActivity.IS_MONITORING, false);
        cropACATId = getArguments().getString(CROP_ACAT_ID);
        menuLabel = getArguments().getString(label);


        unionOption = getView(R.id.seed_source_union);
        factoryOption = getView(R.id.seed_source_factory);
        traderOption = getView(R.id.seed_source_trader);

        parentLables = getArguments().getStringArrayList(TITLE_LABEL);
        isSeed = getArguments().getBoolean(IS_SEED, false);
        toolbar = getView(R.id.acat_step_1_data_entry_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener((View view) -> {
//            if (remainingValue != 0) {
//                showCashFlowErrorMessage();
//                return;
//            }

            if (acatItemDto.acatItemId != null && acatItem != null && !acatItemDto.acatItemId.isEmpty() && acatItemDto.estimatedCashFlow.type != null && acatItem._id != null) {

                showUpdateProgress();
                if (isMonitoring) {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.actualCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.sectionId = sectionId;
                    acatItemDto.actualCashFlow.classification = actualCashFlow.classification;
                    acatItemDto.actualCashFlow.type = actualCashFlow.type;
                    acatItemDto.actualCashFlow.referenceId = acatItemDto.acatItemId;
                    acatItemDto.cropACATId = cropACAT._id;
                    acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
                    acatItemDto.firstExpenseMonth = firstExpenceMonth;
                    updater.updateActualValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe( () -> {
                                hideLoadingProgress();
                                getParent().onBackPressed();
//                                getFragmentManager().popBackStackImmediate();
//                                return;
                            }, throwable -> {
                                onUpdateFailed(throwable);
//                                getFragmentManager().popBackStackImmediate()    ;
//                                return;
                                getParent().onBackPressed();
                            });
                } else {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.estimatedCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.sectionId = sectionId;
                    acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification;
                    acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type;
                    acatItemDto.estimatedCashFlow.referenceId = acatItemDto.acatItemId;
                    acatItemDto.cropACATId = cropACAT._id;
                    acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
                    acatItemDto.firstExpenseMonth = firstExpenceMonth;
                    updater.updateEstimatedValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe(() -> {
                                hideLoadingProgress();
//                                getParent().onBackPressed();
                                getFragmentManager().popBackStack();
                            }, throwable -> {
                                onUpdateFailed(throwable);
//                                getParent().onBackPressed();
                                getFragmentManager().popBackStack();
                            });
                }
            } else {
                getParent().onBackPressed();
            }

        });

        acatItemInputContainer = getView(R.id.acat_item_cost_estimation_container);
        remarkContainer = getView(R.id.acat_item_cost_estimation_remark_container);
        seedVarietyContainer = getView(R.id.seed_variety_input_container);
        seedSourceContainer = getView(R.id.seed_source_choice_container);

        acatItemShield = getView(R.id.acat_item_yield_read_only_shield);
        acatItemShield.setOnClickListener(view -> {});
        estimatedYieldShield = getView(R.id.estimated_yield_read_only_shield);
        estimatedYieldShield.setOnClickListener(view -> {});
        actualYieldShield = getView(R.id.actual_yield_read_only_shield);
        actualYieldShield.setOnClickListener(view -> {});

        subTotalItemLabel = getTv(R.id.acat_item_subtotal_item_label);
        subTotalItemLabel.setText(itemLabel);
        menuDone = getTv(R.id.acat_menu_done);
        menuDone.setOnClickListener(v -> {
//            if (remainingValue != 0) {
//                showCashFlowErrorMessage();
//                return;
//            }

            if (acatItemDto.acatItemId != null && acatItem != null && !acatItemDto.acatItemId.isEmpty() && (isMonitoring ? acatItemDto.actualCashFlow.type != null : acatItemDto.estimatedCashFlow.type != null) && acatItem._id != null) {
                showUpdateProgress();
                if (isMonitoring) {

                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.actualCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.sectionId = sectionId;
                    acatItemDto.actualCashFlow.classification = actualCashFlow.classification;
                    acatItemDto.actualCashFlow.type = actualCashFlow.type;
                    acatItemDto.actualCashFlow.referenceId = acatItemDto.acatItemId;
                    acatItemDto.cropACATId = cropACAT._id;
                    acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
                    acatItemDto.firstExpenseMonth = firstExpenceMonth;

                    updater.updateActualValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe( () -> {
                                hideLoadingProgress();
//////                                getFragmentManager().popBackStackImmediate();
//////                                return;
//                                openACATSummary(clientId, cropACATId);
                                getParent().onBackPressed();
                            }, throwable -> {
                                onUpdateFailed(throwable);
////                                getFragmentManager().popBackStackImmediate();
////                                return;
//                                openACATSummary(clientId, cropACATId);
                                getParent().onBackPressed();
                            });

                } else {

                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.estimatedCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.sectionId = sectionId;
                    acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification;
                    acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type;
                    acatItemDto.estimatedCashFlow.referenceId = acatItemDto.acatItemId;
                    acatItemDto.cropACATId = cropACAT._id;
                    acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
                    acatItemDto.firstExpenseMonth = firstExpenceMonth;

                    updater.updateEstimatedValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe( () -> {
                                hideLoadingProgress();
                                getFragmentManager().popBackStack();
//                                getParent().onBackPressed();
                            }, throwable -> {
                                onUpdateFailed(throwable);
                                getFragmentManager().popBackStack();
//                                getParent().finish();
                            });
                }

            }
            //else
                //getActivity().onBackPressed();
        });

        estimatedACATItemUnitInput = getEd(R.id.estimated_yield_unit_input);
        actualACATItemUnitInput = getEd(R.id.actual_yield_unit_input);
        subTotalLabel = getTv(R.id.acat_item_subtotal_value);
        subTotalLabel.setText(Integer.toString(0));
        acatItemCashFlowLabel = getTv(R.id.acat_item_cash_flow_label);
        acatItemRemainingLabel = getTv(R.id.acat_item_cash_flow_remaining_label);
        titleIndicatorContainer= getView(R.id.acat_item_indicator_layout_container);

        cashOutFlowRecyclerView = getView(R.id.cash_out_flow_recycler_view);

        LinearLayoutManager cashOutFlowLayoutManager = new LinearLayoutManager(getContext());

        cashOutFlowRecyclerView.setLayoutManager(cashOutFlowLayoutManager);

        fetchACATItemFromCropId();



        return root;
    }

    public void openACATSummary(@NonNull String clientId, @NonNull String cropACATId) {
        final SummaryCostsFragment fragment = SummaryCostsFragment.newInstance(SummaryMenuItem.Label.INPUTS, clientId, cropACATId);
        final Fragment manager = getParentFragment();
        manager.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.summaryInnerFragmentContainer, fragment)
                .commit();
    }

    private SummaryMenuItem.Label getLabel(String label) {
        switch (label) {
            case "LABOUR COST":
                return SummaryMenuItem.Label.LABOR_COST;
            case "OTHER COSTS":
                return SummaryMenuItem.Label.OTHER_COSTS;
            case "Chemicals":
                return SummaryMenuItem.Label.CHEMICALS;
            case "Fertilizers":
                return SummaryMenuItem.Label.FERTILIZERS;
            case "Seed":
                return SummaryMenuItem.Label.SEEDS;
        }
        return SummaryMenuItem.Label.INPUTS;
    }
    private void openOtherDialog(@Nullable String groupName, boolean isChild) {
        otherDialog = OtherDialog.newInstance(groupName, isChild);
        otherDialog.setCallBack(this);
        otherDialog.show(getChildFragmentManager(), null);
    }

    @SuppressLint("CheckResult")
    private void fetchACATItemFromCropId() {
        loadState.setLoading();
        showLoadingProgress();

        cropACATLocalSource.get(cropACATId)
                .flatMap(cropACAT -> {
                    this.cropACAT = cropACAT.get();
                    return acatItemLocal.getItemBySectionId(sectionId);
                }).flatMap(acatItem -> {
                    acatItems.clear();
                    acatItems.addAll(acatItem);
                    return cashFlowLocal.getAll();
                }).flatMap(cashFlows -> {
                    cashFlowList.clear();
                    cashFlowList.addAll(cashFlows);
                    return acatItemValueLocal.getAll();
                }).flatMap(acatItemValues -> {
                    acatItemValueList.clear();
                    acatItemValueList.addAll(acatItemValues);
                    return costSectionLocal.get(sectionId);
                }).flatMap(acatCostSectionData -> {
                    section = acatCostSectionData.get();
                    return groupedLocalSource.getByCostList(section.costListID);
                })
                .flatMap(groupedList -> {
                    groupedLists.clear();
                    groupedLists.addAll(groupedList);
                    return localSource.get(cropACAT.acatApplicationID);
                })
                .flatMap(acatApplicationSyncData -> {
                    sync = acatApplicationSyncData.get();
                    return acatLocal.get(cropACAT.acatApplicationID);
                })
                .flatMap(acatApplicationData -> {
                    acatApplication = acatApplicationData.get();
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

    private List<String> getSeedSource() {
        unionOption.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (seedSource.contains(UNION_OPTION)) {
                    seedSource.remove(UNION_OPTION);
                } else {
                    seedSource.add(UNION_OPTION);
                }
            } else {
                if (seedSource.contains(UNION_OPTION))
                    seedSource.remove(UNION_OPTION);
            }
        });

        factoryOption.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (seedSource.contains(FACTORY_OPTION)) {
                    seedSource.remove(FACTORY_OPTION);
                } else {
                    seedSource.add(FACTORY_OPTION);
                }
            } else {
                if (seedSource.contains(FACTORY_OPTION))
                    seedSource.remove(FACTORY_OPTION);
            }
        });

        traderOption.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (seedSource.contains(TRADER_OPTION)) {
                    seedSource.remove(TRADER_OPTION);
                } else {
                    seedSource.add(TRADER_OPTION);
                }
            } else {
                if (seedSource.contains(TRADER_OPTION))
                    seedSource.remove(TRADER_OPTION);
            }
        });

        return seedSource;
    }

    private void initializeInputFields() {
        estimatedQuantityInput = getEd(R.id.estimated_yield_quantity_input);
        estimatedUnitPriceInput = getEd(R.id.estimated_yield_unit_price_input);
        actualQuantityInput = getEd(R.id.actual_yield_quantity_input);
        actualUnitPriceInput = getEd(R.id.actual_yield_unit_price_input);

        estimatedTotalPriceLabel = getTv(R.id.estimated_yield_total_label);
        actualTotalPriceLabel = getTv(R.id.actual_yield_total_label);

        remarkInput = getEd(R.id.acatItemRemarkInput);
        remarkInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                remark = editable.toString().trim();

                if (!remark.isEmpty()) {
                    acatItemDto.remark = remark;
                }
            }
        });

        seedVarietyInput = getEd(R.id.seed_variety_input);
        seedVarietyInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                variety = editable.toString().trim();

                if(!variety.isEmpty()) {
                    acatItemDto.variety = variety;
                }
            }
        });

        estimatedQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                estimatedQuantity = editable.toString().trim();
                if (!estimatedQuantity.isEmpty()) {
                    acatItemDto.estimatedValue = Double.parseDouble(estimatedQuantity);
                    acatItemDto.acatItemId = acatItem._id;
                    acatItemDto.item = acatItem.item;
                    acatItemDto.unit = acatItem.unit;
                    acatItemDto.costListId = acatItem.costListId;
                    acatItemDto.groupListId = acatItem.groupedListId;
                    acatItemDto.createdAt = acatItem.createdAt;
                    acatItemDto.updatedAt = acatItem.updatedAt;
                    acatItemDto.pendingCreation = acatItem.pendingCreation;
                }

                estimatedTotalPrice = calculateTotalPrice(acatItemDto.estimatedValue, acatItemDto.estimatedUnitPrice);

                acatItemDto.estimatedTotalPrice = estimatedTotalPrice;
                setEstimatedTotals(estimatedTotalPrice);
            }
        });

        estimatedUnitPriceInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                estimatedUnitPrice = editable.toString().trim();
                if (!estimatedUnitPrice.isEmpty()) {
                    acatItemDto.estimatedUnitPrice = Double.parseDouble(estimatedUnitPrice);
                    acatItemDto.acatItemId = acatItem._id;
                    acatItemDto.item = acatItem.item;
                    acatItemDto.unit = acatItem.unit;
                    acatItemDto.costListId = acatItem.costListId;
                    acatItemDto.groupListId = acatItem.groupedListId;
                    acatItemDto.createdAt = acatItem.createdAt;
                    acatItemDto.updatedAt = acatItem.updatedAt;
                    acatItemDto.pendingCreation = acatItem.pendingCreation;
                }

                estimatedTotalPrice = calculateTotalPrice(acatItemDto.estimatedValue, acatItemDto.estimatedUnitPrice);

                acatItemDto.estimatedTotalPrice = estimatedTotalPrice;
                setEstimatedTotals(estimatedTotalPrice);
            }
        });

        actualQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                actualQuantity = editable.toString().trim();
                if (!actualQuantity.isEmpty()) {
                    acatItemDto.actualValue = Double.parseDouble(actualQuantity);
                    acatItemDto.acatItemId = acatItem._id;
                    acatItemDto.unit = acatItem.unit;
                    acatItemDto.costListId = acatItem.costListId;
                    acatItemDto.groupListId = acatItem.groupedListId;
                    acatItemDto.createdAt = acatItem.createdAt;
                    acatItemDto.updatedAt = acatItem.updatedAt;
                }
                actualTotalPrice = calculateTotalPrice(acatItemDto.actualValue, acatItemDto.actualUnitPrice);
                acatItemDto.actualTotalPrice = actualTotalPrice;
                setActualTotals(actualTotalPrice);
            }
        });

        actualUnitPriceInput.addTextChangedListener(new InputWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                actualUnitPrice = editable.toString().trim();
                if (!actualUnitPrice.isEmpty()) {
                    acatItemDto.actualUnitPrice = Double.parseDouble(actualUnitPrice);
                    acatItemDto.acatItemId = acatItem._id;
                    acatItemDto.unit = acatItem.unit;
                    acatItemDto.costListId = acatItem.costListId;
                    acatItemDto.groupListId = acatItem.groupedListId;
                    acatItemDto.createdAt = acatItem.createdAt;
                    acatItemDto.updatedAt = acatItem.updatedAt;
                }

                actualTotalPrice = calculateTotalPrice(acatItemDto.actualValue, acatItemDto.actualUnitPrice);
                acatItemDto.actualTotalPrice = actualTotalPrice;
                setActualTotals(actualTotalPrice);

            }
        });
    }

    private double calculateTotalPrice(double quantity, double unitPrice) {
        return quantity * unitPrice;
    }

    private void setEstimatedTotals(Double totalPrice){
        if (isMonitoring) return;
        Double prevSubTotal = Double.parseDouble(subTotalLabel.getText().toString().trim());
        Double updatedSubTotal = (prevSubTotal - (Double.parseDouble(estimatedTotalPriceLabel.getText().toString().trim())))
                                    + totalPrice;
        subTotalLabel.setText(Double.toString(updatedSubTotal));
        estimatedTotalPriceLabel.setText(Double.toString(totalPrice));
        acatItemRemainingLabel.setText(Double.toString(totalPrice));
    }

    private void setActualTotals(Double totalPrice){
        Double prevSubTotal = Double.parseDouble(subTotalLabel.getText().toString().trim());
        Double updatedSubTotal = (prevSubTotal - (Double.parseDouble(actualTotalPriceLabel.getText().toString().trim())))
                + totalPrice;
        subTotalLabel.setText(Double.toString(updatedSubTotal));
        actualTotalPriceLabel.setText(Double.toString(totalPrice));
        acatItemRemainingLabel.setText(Double.toString(totalPrice));
    }
    private void setTitle(String title) {
        toolbar.setTitle(title);
    }

    private void clearData() {
        if (!estimatedUnitPriceInput.getText().toString().isEmpty()) {
            estimatedUnitPriceInput.getText().clear();
        }
        if (!estimatedQuantityInput.getText().toString().isEmpty()) {
            estimatedQuantityInput.getText().clear();
        }
        estimatedTotalPriceLabel.setText(Integer.toString(0));
        if (!actualUnitPriceInput.getText().toString().isEmpty()) {
            actualUnitPriceInput.getText().clear();
        }
        if (!actualQuantityInput.getText().toString().isEmpty()) {
            actualQuantityInput.getText().clear();
        }
        if (!remarkInput.getText().toString().isEmpty()) {
            remarkInput.getText().clear();
        }

        variety = null;
        seedSource.clear();
        actualTotalPriceLabel.setText(Integer.toString(0));
        acatItemRemainingLabel.setText(Integer.toString(0));
    }

    @SuppressLint("CheckResult")
    private void initExpandableList() {
        listView = root.findViewById(R.id.acat_item_detail_expandable_list_view);

        expandableListAdapter = new ACATItemExpandableListViewAdapter(getContext(), listDataHeader, listDataChild);

        listView.setAdapter(expandableListAdapter);
        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

        listView.setSelector(R.color.acat_item_selected);

        listView.setOnGroupExpandListener(groupPosition -> {

            if (isChild) {
                acatItem = null;
                isChild = false;
                showUpdateProgress();
                if (!isMonitoring && estimatedCashFlow != null) {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.estimatedCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification;
                    acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type;
                    acatItemDto.estimatedCashFlow.referenceId = acatItemDto.acatItemId;

                    updater.updateEstimatedValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe(this::hideLoadingProgress,
                                    this::onUpdateFailed);

                } else if (isMonitoring && actualCashFlow != null) {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.actualCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }
                    acatItemDto.actualCashFlow.classification = actualCashFlow.classification;
                    acatItemDto.actualCashFlow.type = actualCashFlow.type;
                    acatItemDto.actualCashFlow.referenceId = acatItemDto.acatItemId;

                    updater.updateActualValueForACATItem(acatItemDto)
                            .subscribeOn(schedulers.background())
                            .observeOn(schedulers.ui())
                            .subscribe(this::hideLoadingProgress,
                                    this::onUpdateFailed);
                }

            }

            clearData();
            String selected = (String) expandableListAdapter.getGroup(groupPosition);
            acatItemCashFlowLabel.setText(selected);

            if (selected.equalsIgnoreCase("Other")) {
                listView.setSelector(R.color.transparent);
                openOtherDialog(null, isChild);
                acatItemInputContainerVisible(false);
                return;
            } else {
                listView.setSelector(R.color.acat_item_selected);
            }
            if (isSeed) {
                switchSeedItemView(selected);
                // TODO: Test below code before uncommenting.
                acatItemDto.seedSource = getSeedSource();
            } else {
                switchView(selected);
            }



            if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition) {
                listView.collapseGroup(lastExpandedPosition);
            }


            lastExpandedPosition = groupPosition;
            refreshIndicators(listDataHeader.get(groupPosition));
            populateIndicators(acatItemLabels);
            acatItemDto.sectionId = sectionId;
            acatItemDto.cropACATId = cropACAT._id;
            acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
            acatItemDto.firstExpenseMonth = firstExpenceMonth;

            if (acatItemDto.acatItemId != null && acatItem != null && !acatItemDto.acatItemId.isEmpty()) {
                showUpdateProgress();
                updateValues(selected);

            } else if (acatItem != null && !selected.equalsIgnoreCase(SEED_VARIETY) && !selected.equalsIgnoreCase(SEED_SOURCE)) {
                estimatedACATItemValue = getEstimatedACATItemValue(acatItem._id, acatItemValueList);
                actualACATItemValue = getActualACATItemValue(acatItem._id, acatItemValueList);

                    if (estimatedACATItemValue != null) {
                        estimatedQuantity = String.valueOf(estimatedACATItemValue.value);
                        estimatedUnitPrice = String.valueOf(estimatedACATItemValue.unitPrice);

                        estimatedQuantityInput.setText(estimatedQuantity);
                        estimatedUnitPriceInput.setText(estimatedUnitPrice);
                    }
                    if (actualACATItemValue != null) {
                        actualQuantity = String.valueOf(actualACATItemValue.value);
                        actualUnitPrice = String.valueOf(actualACATItemValue.unitPrice);

                        actualQuantityInput.setText(actualQuantity);
                        actualUnitPriceInput.setText(actualUnitPrice);
                    }

                }

        });


        listView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {

            if (!isChild) {
                acatItemDto.estimatedCashFlow.type = null;
            }

            isChild = true;
            final String selected = (String) expandableListAdapter.getChild(groupPosition, childPosition);

            if (!selected.equals(other)) {
                clearData();
            }

            if (selected.equals(other)) {
                String groupName = listDataHeader.get(groupPosition);
                listView.setSelector(R.color.transparent);
                estimatedYieldShield(true);
                openOtherDialog(groupName, isChild);
            } else {
                listView.setSelector(R.color.acat_item_selected);
                acatItemCashFlowLabel.setText(selected);
                acatItemRemainingLabel.setText(Integer.toString(0));
                if (isMonitoring) {
                    actualYieldShield(false);
                    estimatedYieldShield(true);
                } else {
                    actualYieldShield(true);
                    estimatedYieldShield(false);
                }

                refreshIndicators(listDataHeader.get(groupPosition));
                acatItemLabels.add(selected);
                populateIndicators(acatItemLabels);
                estimatedACATItemUnitInput.setText(getACATItemUnit(selected, acatItems));
                actualACATItemUnitInput.setText(getACATItemUnit(selected, acatItems));

                acatItem = getACATItem(selected, acatItems, sectionId);

                if (isMonitoring) {
                    actualCashFlow = getActualCashFlow(acatItem._id, cashFlowList);
                } else {
                    estimatedCashFlow = getEstimatedCashFlow(acatItem._id, cashFlowList);
                }

                estimatedACATItemValue = getEstimatedACATItemValue(acatItem._id, acatItemValueList);
                actualACATItemValue = getActualACATItemValue(acatItem._id, acatItemValueList);


                if (isMonitoring) {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.actualCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }

                    acatItemDto.actualCashFlow.classification = actualCashFlow.classification;
                    acatItemDto.actualCashFlow.type = actualCashFlow.type;
                    acatItemDto.actualCashFlow.referenceId = acatItemDto.acatItemId;
                    acatQuestionsCashFlowAdapter.notify(actualCashFlow);
                } else {
                    if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                        acatItemDto.estimatedCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                    }

                    acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification;
                    acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type;
                    acatItemDto.estimatedCashFlow.referenceId = acatItemDto.acatItemId;
                    acatQuestionsCashFlowAdapter.notify(estimatedCashFlow);
                }
                acatItemDto.sectionId = sectionId;
//            acatItemDto.unit = acatItem.unit;
                acatItemDto.firstExpenseMonth = firstExpenceMonth;

                if (acatItem != null && !acatItemDto.acatItemId.isEmpty() && (acatItemDto.estimatedCashFlow.type != null || acatItemDto.actualCashFlow.type != null)) {
                    showUpdateProgress();
                    updateValues(selected);

//
                } else {
                    estimatedACATItemValue = getEstimatedACATItemValue(acatItem._id, acatItemValueList);
                    actualACATItemValue = getActualACATItemValue(acatItem._id, acatItemValueList);

                    if (estimatedACATItemValue != null) {
                        estimatedQuantity = String.valueOf(estimatedACATItemValue.value);
                        estimatedUnitPrice = String.valueOf(estimatedACATItemValue.unitPrice);

                        estimatedQuantityInput.setText(estimatedQuantity);
                        estimatedUnitPriceInput.setText(estimatedUnitPrice);
                    }

                    if (actualACATItemValue != null) {
                        actualQuantity = String.valueOf(actualACATItemValue.value);
                        actualUnitPrice = String.valueOf(actualACATItemValue.unitPrice);

                        actualQuantityInput.setText(actualQuantity);
                        actualUnitPriceInput.setText(actualUnitPrice);
                    }
                }
            }


            return true;
        });

    }

    @SuppressLint("CheckResult")
    private void updateValues(String selected) {
        if (isMonitoring) {
            updater.updateActualValueForACATItem(acatItemDto)
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(this::hideLoadingProgress,
                            this::onUpdateFailed);
        } else {
            updater.updateEstimatedValueForACATItem(acatItemDto)
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(this::hideLoadingProgress,
                            this::onUpdateFailed);
        }

        acatItemValueLocal.getAll()
                .flatMap(acatItemValues -> {
                    acatItemValueList.clear();
                    acatItemValueList.addAll(acatItemValues);
                    return cashFlowLocal.getAll();
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(cashFlows -> {
                    cashFlowList.clear();
                    cashFlowList.addAll(cashFlows);

                    if (!selected.equalsIgnoreCase(SEED_SOURCE) && !selected.equalsIgnoreCase(SEED_VARIETY) && !selected.equalsIgnoreCase("Other")) {
                        setItemValues();
                    }
                    hideLoadingProgress();
                });
    }

    private void setItemValues() {

        actualCashFlow = getActualCashFlow(acatItem._id, cashFlowList);
        actualACATItemValue = getActualACATItemValue(acatItem._id, acatItemValueList);
        estimatedCashFlow = getEstimatedCashFlow(acatItem._id, cashFlowList);
        estimatedACATItemValue = getEstimatedACATItemValue(acatItem._id, acatItemValueList);

        if (isMonitoring) {
            acatQuestionsCashFlowAdapter.notify(actualCashFlow);
        } else {
            acatQuestionsCashFlowAdapter.notify(estimatedCashFlow);
        }


        if (estimatedACATItemValue != null) {
            estimatedQuantity = String.valueOf(estimatedACATItemValue.value);
            estimatedUnitPrice = String.valueOf(estimatedACATItemValue.unitPrice);
            estimatedQuantityInput.setText(estimatedQuantity);
            estimatedUnitPriceInput.setText(estimatedUnitPrice);
        }

        if (actualACATItemValue != null) {
            actualQuantity = String.valueOf(actualACATItemValue.value);
            actualUnitPrice = String.valueOf(actualACATItemValue.unitPrice);

            actualQuantityInput.setText(actualQuantity);
            actualUnitPriceInput.setText(actualUnitPrice);
        }

    }

    private void switchSeedItemView(String item) {

        acatItem = getACATItem(item, acatItems, sectionId);

        if (item.equalsIgnoreCase(SEED_VARIETY)) {
            seedVarietyVisible(true);
            seedSourceVisible(false);
            acatItemInputContainerVisible(false);
        } else if (item.equalsIgnoreCase(SEED_SOURCE)) {
            seedSourceVisible(true);
            seedVarietyVisible(false);
            acatItemInputContainerVisible(false);
        } else {
            switchView(item);
        }
    }

    private void switchView(String item) {
        actualYieldShield(true);
        estimatedYieldShield(true);
        seedVarietyVisible(false);
        seedSourceVisible(false);
        acatItemInputContainerVisible(true);
        actualYieldShield(true);
        if (listDataChild.get(item).size() > 0) {
            actualYieldShield(true);
            estimatedYieldShield(true);
        } else {
            if (isMonitoring) {
                actualYieldShield(false);
            } else {
                estimatedYieldShield(false);
            }
            acatItem = getACATItem(item, acatItems, sectionId);//This is to getByType linear cost list ACATItem id.
            if (isMonitoring) {
                actualCashFlow = getActualCashFlow(acatItem._id, cashFlowList);
            } else {
                estimatedCashFlow = getEstimatedCashFlow(acatItem._id, cashFlowList);
            }
//            estimatedACATItemValue = getEstimatedACATItemValue(acatItem._id, acatItemValueList);
            acatItemDto.sectionId = sectionId;

            if (isMonitoring) {
                if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                    acatItemDto.actualCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                }
                acatItemDto.actualCashFlow.classification = actualCashFlow.classification;
                acatItemDto.actualCashFlow.type = actualCashFlow.type;
                acatItemDto.actualCashFlow.referenceId = acatItemDto.acatItemId;
                acatQuestionsCashFlowAdapter.notify(actualCashFlow);
            } else {
                if (acatQuestionsCashFlowAdapter.cashFlowList.size() != 0) {
                    acatItemDto.estimatedCashFlow = getCashFlowData(acatQuestionsCashFlowAdapter.cashFlowList);
                }
                acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification;
                acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type;
                acatItemDto.estimatedCashFlow.referenceId = acatItemDto.acatItemId;
                acatQuestionsCashFlowAdapter.notify(estimatedCashFlow);
            }
        }
        estimatedACATItemUnitInput.setText(getACATItemUnit(item, acatItems));
        actualACATItemUnitInput.setText(getACATItemUnit(item, acatItems));
    }

    private void acatItemYieldShield(boolean on) {
        acatItemShield.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    private void estimatedYieldShield(boolean on) {
        estimatedYieldShield.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    private void actualYieldShield(boolean on) {
        actualYieldShield.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    private void acatItemInputContainerVisible(boolean isVisible) {
        acatItemInputContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        remarkContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void seedVarietyVisible(boolean isVisible) {
        seedVarietyContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void seedSourceVisible(boolean isVisible) {
        seedSourceContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


    private ACATItem getACATItem(String acatItemName, List<ACATItem> acatItems, String sectionID) {
        ACATItem item = new ACATItem();
        for (ACATItem acatItem : acatItems) {
            if (acatItem.item.equalsIgnoreCase(acatItemName) && acatItem.sectionId.equalsIgnoreCase(sectionID))
                item = acatItem;
        }
        return item;
    }

    private CashFlow getEstimatedCashFlow(String acatItemId, List<CashFlow> cashFlows) {
        CashFlow cashFlow = new CashFlow();
        for (CashFlow c : cashFlows) {
            if (acatItemId.equalsIgnoreCase(c.referenceId) && c.type.equalsIgnoreCase(CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE))
                cashFlow = c;
        }
        return cashFlow;
    }

    private CashFlow getActualCashFlow(String acatItemId, List<CashFlow> cashFlows) {
        CashFlow cashFlow = new CashFlow();
        for (CashFlow c : cashFlows) {
            if (acatItemId.equalsIgnoreCase(c.referenceId) && c.type.equalsIgnoreCase(CashFlowHelper.ACTUAL_CASH_FLOW_TYPE))
                cashFlow = c;
        }
        return cashFlow;
    }


    private ACATItemValue getEstimatedACATItemValue(String acatItemId, List<ACATItemValue> acatItemValues) {
//        ACATItemValue itemValue = new ACATItemValue();
        for (ACATItemValue value : acatItemValues) {
            if (acatItemId.equalsIgnoreCase(value.acatItemId) && value.type.equalsIgnoreCase(ACATItemValueHelper.ESTIMATED_VALUE_TYPE))
//                itemValue = value;
                return value;
        }
        return null;
    }

    private ACATItemValue getActualACATItemValue(String acatItemId, List<ACATItemValue> acatItemValues) {
        for (ACATItemValue value : acatItemValues) {
            if (acatItemId.equalsIgnoreCase(value.acatItemId) && value.type.equalsIgnoreCase(ACATItemValueHelper.ACTUAL_VALUE_TYPE))
                return value;
        }
        return null;
    }

    private CashFlow getCashFlowData(List<String> cashFlowData) {
        CashFlow cashFlow = new CashFlow();
        Collections.rotate(cashFlowData, index);

        cashFlow.january = Double.parseDouble(cashFlowData.get(0).isEmpty() ? "0" : cashFlowData.get(0));
        cashFlow.february = Double.parseDouble(cashFlowData.get(1).isEmpty() ? "0" : cashFlowData.get(1));
        cashFlow.march = Double.parseDouble(cashFlowData.get(2).isEmpty() ? "0" : cashFlowData.get(2));
        cashFlow.april = Double.parseDouble(cashFlowData.get(3).isEmpty() ? "0" : cashFlowData.get(3));
        cashFlow.may = Double.parseDouble(cashFlowData.get(4).isEmpty() ? "0" : cashFlowData.get(4));
        cashFlow.june = Double.parseDouble(cashFlowData.get(5).isEmpty() ? "0" : cashFlowData.get(5));
        cashFlow.july = Double.parseDouble(cashFlowData.get(6).isEmpty() ? "0" : cashFlowData.get(6));
        cashFlow.august = Double.parseDouble(cashFlowData.get(7).isEmpty() ? "0" : cashFlowData.get(7));
        cashFlow.september = Double.parseDouble(cashFlowData.get(8).isEmpty() ? "0" : cashFlowData.get(8));
        cashFlow.october = Double.parseDouble(cashFlowData.get(9).isEmpty() ? "0" : cashFlowData.get(9));
        cashFlow.november = Double.parseDouble(cashFlowData.get(10).isEmpty() ? "0" : cashFlowData.get(10));
        cashFlow.december = Double.parseDouble(cashFlowData.get(11).isEmpty() ? "0" : cashFlowData.get(11));

        return cashFlow;
    }

    private void showLoadingProgress() {
        waitingDialog = WaitingDialog.newInstance(
                getString(R.string.cost_estimation_loading_state)
        );
        waitingDialog.show(getChildFragmentManager(), null);
    }

    private void showUpdateProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_acat_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    private void populateIndicators(List<String> acatItems) {
        titleIndicatorContainer.removeAllViews();
        for (int i = 0; i < acatItems.size(); i++) {
            String title = acatItems.get(i);
            int background = R.drawable.acat_item_indicator_background;
            TextView indicator = (TextView) getLayoutInflater().inflate(R.layout.acat_item_indicator_layout, titleIndicatorContainer, false);

            indicator.setText(title);
            indicator.setBackgroundResource(background);
            titleIndicatorContainer.addView(indicator);
        }
    }

    private void refreshIndicators(String label) {
        acatItemLabels.clear();
        acatItemLabels.addAll(parentLables);
        acatItemLabels.add(label);
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
        if(message == null) {
            message = getString(R.string.acat_questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.questions_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    private void onLoadingComplete() {
        hideLoadingProgress();

        initializeInputFields();

        setTitle(Utils.formatName(
                c.firstName,
                c.surname,
                c.lastName
        ));

        populateList(acatItems);

        populateIndicators(parentLables);
        initExpandableList();
        firstExpenceMonth = cropACAT.firstExpenseMonth;
        index = ACATUtility.getMonthIndex(firstExpenceMonth);
        cashFlowData.clear();
        for (int i = 0; i < 12; i++) {
            cashFlowData.add(String.valueOf(0));
        }

        acatQuestionsCashFlowAdapter = new ACATItemCashFlowAdapter(this, firstExpenceMonth, cashFlowData);
        cashOutFlowRecyclerView.setAdapter(acatQuestionsCashFlowAdapter);
        expandableListAdapter.notifyDataSetChanged();
        acatQuestionsCashFlowAdapter.notifyDataSetChanged();
        loadState.reset();
    }

    private void populateList(@NonNull List<ACATItem> acatItems) {
        List<ACATItem> acatItemList = new ArrayList<>();


        for (ACATItem acatItem : acatItems) {
            if (acatItem.groupedListId == null) {
                addSeedItems(isSeed);
                listDataHeader.add(acatItem.item);
            } else {
                acatItemList.add(acatItem);
                if (!groupedListId.contains(acatItem.groupedListId) )
                    groupedListId.add(acatItem.groupedListId);
            }
        }

        if (groupedListId.size() > 0) {
            for (int j = 0; j < groupedListId.size(); j++) {
                listDataHeader.add(getGroupName(groupedListId.get(j), groupedLists));
                listDataChild.put(listDataHeader.get(j), getChildName(groupedListId.get(j), acatItemList));
            }

        } else {
            listDataHeader.add("Other");
            for (int i = 0; i < listDataHeader.size(); i++) {
                listDataChild.put(listDataHeader.get(i), new ArrayList<>());
            }
        }
    }

    private void addSeedItems(boolean isSeed) {
        if (isSeed) {
            if (!listDataHeader.contains(SEED_VARIETY)) {
                listDataHeader.add(SEED_VARIETY);
            }
            if (!listDataHeader.contains(SEED_SOURCE)) {
                listDataHeader.add(SEED_SOURCE);
            }
        }
    }
    private List<String> getChildName(String groupId, List<ACATItem> acatItems) {
        List<String> childName = new ArrayList<>();
        for (ACATItem acatItem : acatItems) {
            if (groupId.equalsIgnoreCase(acatItem.groupedListId))
                childName.add(acatItem.item);
        }
        other = String.valueOf(Html.fromHtml("<b> Other + </b>"));
        childName.add(other);
        return childName;

    }

    private ACATItem getACATFromGroupId(String groupId, List<ACATItem> acatItemList) {
        for (ACATItem acatItem : acatItemList) {
            if (acatItem.groupedListId.equalsIgnoreCase(groupId))
                return acatItem;
        }
        return null;
    }

    private String getGroupId(String groupName, List<GroupedList> groupedLists) {
        for (GroupedList groupedList : groupedLists) {
            if (groupedList.title.equalsIgnoreCase(groupName))
                return groupedList._id;
        }
        return null;
    }
    private String getGroupName(String groupId, List<GroupedList> groupedLists) {
        String title = null;
        for (GroupedList groupedList : groupedLists) {
            if (groupId.equalsIgnoreCase(groupedList._id))
                title = groupedList.title;
        }
        return title;
    }

    private String getACATItemUnit(String childName, List<ACATItem> acatItems) {
        for (ACATItem acatItem : acatItems) {
            if (childName.equalsIgnoreCase(acatItem.item))
                return acatItem.unit;
        }
        return null;
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
    public void onCashFlowChanged(double totalCashFlow) {
        if (isMonitoring) {
            remainingValue = acatItemDto.actualTotalPrice - totalCashFlow;
            acatItemRemainingLabel.setText(String.valueOf(remainingValue));
        } else {
            remainingValue = acatItemDto.estimatedTotalPrice - totalCashFlow;
            acatItemRemainingLabel.setText(String.valueOf(remainingValue));
        }
    }

    public void onUpdateFailed(@NonNull Throwable throwable) {

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
            hideLoadingProgress();
            return;
        }

        final String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;

        showError(Result.createError(message, error));

    }

    @SuppressLint("CheckResult")
    @Override
    public void onOtherReturned(@NonNull String acatItemName, @NonNull String unit, @Nullable String groupName, boolean isChild) {

        if (isChild) {
            ACATItemRequest request = new ACATItemRequest();
            String groupId = getGroupId(groupName, groupedLists);

            ACATItem item = getACATFromGroupId(groupId, acatItems);
            ACATItem i = new ACATItem();
            i.item = acatItemName;
            i.unit = unit;
            i.groupedListId = item.groupedListId;
            i.category = item.category;
            i.sectionId = item.sectionId;
            i.costListId = item.costListId;
            i.referenceId = item.referenceId;
            showUpdateProgress();
            acatItemLocal.putNewACATItem(i)
                    .flatMap(acatItem -> {
                        acatItem._id = String.valueOf(acatItem.id);
                        acatItem.pendingCreation = true;
                        return acatItemLocal.putNewACATItem(acatItem);
                    })
                    .flatMap(acatItem -> {
                        newACATItem = acatItem;
                        sync.acatItemIds.add(newACATItem._id);
                        return localSource.put(sync);

                    })
                    .flatMap(done -> {
                        request.acatItem = newACATItem;
                        return acatItemRemoteSource.registerACATItem(request);
                    })
                    .flatMap(acatItemResponse -> {
                        acatItem = acatItemResponse.acatItem;
                        return acatItemLocal.updateACATItemId(newACATItem, acatItem._id);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(acatItem -> {
                        refreshList(acatItem);
                        createCashFlow(acatItem._id);
                        hideLoadingProgress();
                    }, throwable -> {
                        onRegisterACATItemFailed(newACATItem, throwable);
                    });
        } else {
            showUpdateProgress();
            ACATItem item = new ACATItem();
            ACATItemRequest request = new ACATItemRequest();
            item.item = acatItemName;
            item.unit = unit;
            item.sectionId = sectionId;
            item.category = ACATItemHelper.COST_CATEGORY;

            acatItemLocal.putNewACATItem(item)
                    .flatMap(acatItem -> {
                        acatItem._id = String.valueOf(acatItem.id);
                        acatItem.costListId = getCostListId(acatItems);
                        acatItem.pendingCreation = true;
                        return acatItemLocal.putNewACATItem(item);
                    })
                    .flatMap(acatItem -> {
                        newACATItem = acatItem;
                        sync.acatItemIds.add(acatItem._id);
                        return localSource.put(sync);
                    })
                    .flatMap(done -> {
                        request.acatItem = newACATItem;
                        return acatItemRemoteSource.registerACATItem(request);
                    })
                    .flatMap(acatItemResponse -> {
                        acatItem = acatItemResponse.acatItem;
                        return acatItemLocal.updateACATItemId(newACATItem, acatItem._id);
                    })
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(acatItem -> {
                        refreshList(acatItem);
                        createCashFlow(acatItem._id);
                        hideLoadingProgress();
                    }, throwable -> {
                        onRegisterACATItemFailed(newACATItem, throwable);
                        throwable.printStackTrace();
                    });
        }

    }

    @SuppressLint("CheckResult")
    private void onRegisterACATItemFailed(ACATItem item, Throwable throwable) {
        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
            acatApplication.pendingCreation = true;
            acatLocal.put(acatApplication)
                    .subscribeOn(schedulers.background());
            refreshList(item);
            createCashFlow(String.valueOf(item.id));
            hideLoadingProgress();
            listView.requestFocus();
            return;
        }

        final String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;

        showError(Result.createError(message, error));

    }

    private void refreshList(ACATItem item) {
        listDataHeader.clear();
        listDataChild.clear();
        acatItems.add(item);
        createACATItemValue(item);
    }
    private String getCostListId(List<ACATItem> acatItemList) {
        for (ACATItem item : acatItemList) {
            if (item.costListId != null)
                return item.costListId;
        }
        return null;
    }

    @SuppressLint("CheckResult")
    private void createCashFlow(@NonNull String referenceId) {
        CashFlow estimatedCashFlow = new CashFlow();
        CashFlow actualCashFlow = new CashFlow();

        estimatedCashFlow.referenceId = referenceId;
        actualCashFlow.referenceId = referenceId;

        estimatedCashFlow.type = CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        actualCashFlow.type = CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;

        estimatedCashFlow.classification = CashFlowHelper.ITEM_CASH_FLOW;
        actualCashFlow.classification = CashFlowHelper.ITEM_CASH_FLOW;

        cashFlowLocal.putNewCashFlow(estimatedCashFlow)
                .flatMap(done -> {
                    cashFlowList.add(done);
                    return cashFlowLocal.putNewCashFlow(actualCashFlow);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(value -> cashFlowList.add(value));

    }

    @SuppressLint("CheckResult")
    private void createACATItemValue(@NonNull ACATItem acatItem) {
        ACATItemValue estimatedItemValue = new ACATItemValue();
        ACATItemValue actualItemValue = new ACATItemValue();

        estimatedItemValue.acatItemId = acatItem._id;
        estimatedItemValue.value = 0;
        estimatedItemValue.unitPrice = 0;
        estimatedItemValue.totalPrice = 0;
        estimatedItemValue.type = ACATItemValueHelper.ESTIMATED_VALUE_TYPE;


        actualItemValue.acatItemId = acatItem._id;
        actualItemValue.value = 0;
        actualItemValue.unitPrice = 0;
        actualItemValue.totalPrice = 0;
        actualItemValue.type = ACATItemValueHelper.ACTUAL_VALUE_TYPE;

        acatItemValueLocal.putNewACATItemValue(estimatedItemValue)
                .flatMap(estimatedACATItemValue -> {
                    acatItemValueList.add(estimatedACATItemValue);
                    return acatItemValueLocal.putNewACATItemValue(actualItemValue);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(actualACATItemValue -> {
                    acatItemValueList.add(actualACATItemValue);
                    populateList(acatItems);
                    expandableListAdapter.notifyDataSetChanged();
                });
    }

}
