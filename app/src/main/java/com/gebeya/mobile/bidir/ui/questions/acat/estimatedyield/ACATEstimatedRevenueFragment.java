package com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.ACATApplicationResponse;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationSyncLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSectionRequest;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.repo.ACATRevenueRepoSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.repo.CropACATRepo;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionRequest;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.repo.YieldConsumptionRepo;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATUtility;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostItemUpdater;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostUpdater;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATItemDto;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield.ACATEstimatedYieldDto;
import com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal.ACATLoanProposalActivity;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ACATEstimatedRevenueFragment extends BaseFragment implements
        ErrorDialogCallback,
        OnCashFlowChangedListener{

    public static ACATEstimatedRevenueFragment newInstance() {
        return new ACATEstimatedRevenueFragment();
    }

    private Toolbar toolbar;
    private LinearLayout yieldListContainer;

    private RecyclerView yieldCashOutflowRecyclerView;
    private ACATEstimatedRevenueCashFlowAdapter yieldCashOutflowAdapter;
    private Button nextButton;
    private Button previousButton;
    private TextView saveButton;

    private View yieldCashFlowContainer;
    private View yieldIndicatorContainer;

    private EditText estimatedUnitInput;
    private EditText estimatedUnitPriceInput;
    private EditText estimatedQuantityInput;
    private TextView estimatedTotalLabel;
    private View estimatedYieldConsumptionInputsContainer;
    private EditText estimatedOwnConsInput;
    private EditText estimatedSeedResInput;
    private EditText estimatedForMarketInput;
    private EditText yieldRemarkInput;

    private EditText actualUnitInput;
    private EditText actualQuantityInput;
    private EditText actualUnitPriceInput;
    private TextView actualTotalLabel;
    private TextView cropLabel;
    private View actualYieldConsumptionInputsContainer;
    private EditText actualOwnConsInput;
    private EditText actualSeedResInput;
    private EditText actualForMarketInput;
    private EditText yieldConsumptionRemarkInput;
    private Button marketDetailsButton;

    private View actualRemarkContainer;

    private TextView probableRevenueLabel;
    private TextView maxRevenueLabel;
    private TextView minRevenueLabel;

    private RecyclerView netCashFlowRecyclerView;
    private ACATEstimatedRevenueNetCashFlowAdapter netCashFlowAdapter;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    private int activePosition;
    private int index;

    private String firstExpenseMonth = "";
    private List<String> cashFlowData;
    private List<ACATRevenueSection> yieldSections;
    private String clientId;
    private String cropACATId;
    private boolean isEditing;
    private boolean isMonitoring;
    private CropACAT cropACAT;
    private ACATRevenueSection revenueSection;
    private ACATApplication application;
    private CashFlow yieldCashFlow;
    private CashFlow parentCashFlow;
    private YieldConsumption yieldConsumption;
    private ACATItemValue estimatedValue;
    private ACATItemValue actualValue;
    private ACATItem acatItem;
    private ACATItem previousItem;
    private ACATItemValue estimatedItemValue;
    private ACATItemValue actualItemValue;
    private double totalProbableRevenue;
    private double totalForMarketRevenue;
    private double actualTotal;
    private double probableUnitPrice;

    private double averageRevenue;
    private double maximumRevenue;
    private double minimumRevenue;

    private CashFlow estimatedCashFlow;
    private CashFlow actualCashFlow;

    private boolean toolbarMenu;


    private ACATApplicationResponse acatApplicationResponse;
    private ACATItemDto acatItemDto;
    private ACATCostItemUpdater updater;

    @Inject ClientRepoSource clientRepo;
    @Inject ACATApplicationLocalSource acatApplicationLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATRevenueSectionLocalSource sectionLocal;
    @Inject YieldConsumptionLocalSource yieldConsumptionLocal;
    @Inject ACATItemLocalSource acatItemLocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;
    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject ACATCostUpdater acatCostUpdater;
    @Inject CropACATRepo cropACATRepo;
    @Inject ACATRevenueRepoSource revenueRepo;
    @Inject YieldConsumptionRepo yieldConsumptionRepo;
    @Inject SchedulersProvider schedulers;
    @Inject ACATApplicationSyncLocalSource acatApplicationSyncLocalSource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cashFlowData = new ArrayList<>();
        yieldSections = new ArrayList<>();
        activePosition = 0;

        averageRevenue = 0;
        maximumRevenue = 0;
        minimumRevenue = 0;

        toolbarMenu = false;

        acatItemDto = new ACATItemDto();
        updater = new ACATCostItemUpdater();

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_estimated_yield_layout, container, false);

        clientId = getArguments().getString(ACATEstimatedRevenueActivity.ARG_CLIENT_ID);
        cropACATId = getArguments().getString(ACATEstimatedRevenueActivity.ARG_CROP_ACAT_ID);
        isEditing = getArguments().getBoolean(ACATEstimatedRevenueActivity.IS_EDITING);
        isMonitoring = getArguments().getBoolean(ACATEstimatedRevenueActivity.ARG_MONITOR);

        toolbar = getView(R.id.acat_estimated_yield_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        cropLabel = getTv(R.id.acat_estimated_yield_crop_item_label);

        saveButton = getTv(R.id.acat_menu_done);
        saveButton.setVisibility(isMonitoring ? View.VISIBLE : View.GONE);
        saveButton.setOnClickListener(view -> {
//            toolbarMenu = true;
            showUpdatingMessage();
            ACATEstimatedYieldDto dto = gatherData();
            try {
                onYieldSelected(-1, dto, isMonitoring)
                        .andThen(saveYieldNetCashFlow(isMonitoring))
                        .flatMap(done -> {
                            CashFlow cashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                            cashFlow.referenceId = yieldCashFlow.referenceId;
                            cashFlow.classification = yieldCashFlow.classification;
                            cashFlow.type = yieldCashFlow.type;
                            return cashFlowLocal.put(cashFlow);
                        })
                        .flatMap(done -> acatCostUpdater.computeActualNetIncomeAndCashFlow(cropACAT._id, Double.valueOf(dto.actualForMarket), probableUnitPrice))
                        .flatMap(done -> uploadActualRevenue())
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(done -> {
                            hideUpdatingMessage();
                            getActivity().onBackPressed();
                        }, this::onUpdateFailed);
            } catch (Exception e) {
                e.printStackTrace();
                hideUpdatingMessage();
            }

        });

        nextButton = getBt(R.id.acat_estimated_yield_next_step_button);
        nextButton.setVisibility(isMonitoring ? View.GONE : View.VISIBLE);
        nextButton.setOnClickListener(view -> {
            try {
                toolbarMenu = true;
                showUpdatingMessage();
                ACATEstimatedYieldDto dto = gatherData();
                onYieldSelected(activePosition, dto, isMonitoring)
                        .andThen(saveYieldNetCashFlow(isMonitoring))
                        .flatMap(done -> {
                            CashFlow cashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                            cashFlow.referenceId = yieldCashFlow.referenceId;
                            cashFlow.classification = yieldCashFlow.classification;
                            cashFlow.type = yieldCashFlow.type;
                            return cashFlowLocal.put(cashFlow);
                        })
                        .flatMap(done -> acatCostUpdater.computeEstimatedNetIncomeAndCashFlow(cropACAT._id, Double.valueOf(dto.estimatedForMarket), probableUnitPrice))
                        .flatMap(response -> {
                            this.acatApplicationResponse = response;
                            return cropACATRepo.updateProgress(cropACAT._id, cropACAT.acatApplicationID);
                        })
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(this::onUpdateComplete,
                                this::onUpdateFailed);
            } catch (Exception e) {
                e.printStackTrace();
                hideUpdatingMessage();
            }
        });

        previousButton = getBt(R.id.acat_estimated_yield_previous_step_button);
        previousButton.setVisibility(isMonitoring ? View.GONE : View.VISIBLE);
        previousButton.setOnClickListener(view ->  {
            try {
                toolbarMenu = true;
                showUpdatingMessage();
                ACATEstimatedYieldDto dto = gatherData();
                acatItemDto.acatItemId = acatItem._id;
                onYieldSelected(activePosition, dto, isMonitoring)
                        .andThen(saveYieldNetCashFlow(isMonitoring))
                        .flatMap(done -> {
                            CashFlow cashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                            cashFlow.referenceId = yieldCashFlow.referenceId;
                            cashFlow.classification = yieldCashFlow.classification;
                            cashFlow.type = yieldCashFlow.type;
                            return cashFlowLocal.put(cashFlow);
                        })
                        .flatMap(done -> acatCostUpdater.computeEstimatedNetIncomeAndCashFlow(cropACAT._id, Double.valueOf(dto.estimatedForMarket), probableUnitPrice))
                        .flatMap(response -> {
                            this.acatApplicationResponse = response;
                            return cropACATRepo.updateProgress(cropACAT._id, cropACAT.acatApplicationID);
                        })
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(response -> openPreviousScreen(clientId),
                                this::onUpdateFailed);
            } catch (Exception e) {
                e.printStackTrace();
                hideUpdatingMessage();
            }
        });

        yieldListContainer = getView(R.id.data_entry_item_layout);
        yieldIndicatorContainer = getView(R.id.revenueYieldInputIndicatorsContainer);

        if (isMonitoring) {
            disableMinMax();
        }

        initializeYieldListContainer();
        initializeRevenueInputs(isMonitoring);

        probableRevenueLabel = getTv(R.id.loan_assessment_probable_revenue_label);
        maxRevenueLabel = getTv(R.id.loan_assessment_maximum_revenue_label);
        minRevenueLabel = getTv(R.id.loan_assessment_minimum_revenue_label);

        yieldCashOutflowRecyclerView = getView(R.id.cash_out_flow_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        yieldCashOutflowRecyclerView.setLayoutManager(manager);


        netCashFlowRecyclerView = getView(R.id.acat_net_cash_flow_recycler_view);

        LinearLayoutManager netCashFlowManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        netCashFlowRecyclerView.setLayoutManager(netCashFlowManager);
        netCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));


        loadCropItemRevenueSection();

        return root;
    }

    private void disableMinMax() {
        yieldListContainer.getChildAt(1).setVisibility(View.GONE);
        yieldListContainer.getChildAt(2).setVisibility(View.GONE);
    }

    private void initializeRevenueInputs(boolean isMonitoring) {
        estimatedUnitInput = getEd(R.id.revenueEstimatedUnitInput);
        estimatedUnitInput.setEnabled(!isMonitoring);
        estimatedQuantityInput = getEd(R.id.revenueEstimatedQuantityInput);
        estimatedQuantityInput.setEnabled(!isMonitoring);
        estimatedQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(isMonitoring);

                if (!s.toString().isEmpty()) {
                    acatItemDto.estimatedValue = parse(s.toString().trim());
                    if (activePosition == 0) estimatedForMarketInput.setText(s.toString().trim());
                }

            }
        });
        estimatedUnitPriceInput = getEd(R.id.revenueEstimatedUnitPriceInput);
        estimatedUnitPriceInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(isMonitoring);
                if (!s.toString().isEmpty()) {
                    acatItemDto.estimatedUnitPrice = parse(s.toString().trim());
                }
            }
        });
        estimatedUnitPriceInput.setEnabled(!isMonitoring);
        estimatedTotalLabel = getTv(R.id.revenueEstimatedTotalLabel);
        estimatedYieldConsumptionInputsContainer = getView(R.id.revenueEstimatedYieldConsumptionInputsContainer);
        estimatedOwnConsInput = getEd(R.id.revenueEstimatedOwnConsInput);
        estimatedOwnConsInput.setEnabled(!isMonitoring);
        estimatedSeedResInput = getEd(R.id.revenueEstimatedSeedResInput);
        estimatedSeedResInput.setEnabled(!isMonitoring);
        estimatedForMarketInput = getEd(R.id.revenueEstimatedForMarketInput);
        estimatedForMarketInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshMarketableRevenue(isMonitoring);
            }
        });

        estimatedForMarketInput.setEnabled(!isMonitoring);


        yieldRemarkInput = getEd(R.id.revenueYieldRemarkInput);

        actualUnitInput = getEd(R.id.revenueActualUnitInput);
        actualUnitInput.setEnabled(isMonitoring);

        actualQuantityInput = getEd(R.id.revenueActualQuantityInput);
        actualQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(isMonitoring);
                if (!s.toString().isEmpty()) {
                    acatItemDto.actualValue = parse(s.toString().trim());
                    actualForMarketInput.setText(s.toString().trim());
                }
            }
        });

        actualQuantityInput.setEnabled(isMonitoring);

        actualUnitPriceInput = getEd(R.id.revenueActualUnitPriceInput);
        actualUnitPriceInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(isMonitoring);
                if (!s.toString().isEmpty()) {
                    acatItemDto.actualUnitPrice = parse(s.toString().trim());
                }
            }
        });

        actualUnitPriceInput.setEnabled(isMonitoring);

        actualTotalLabel = getTv(R.id.revenueActualTotalLabel);
        actualYieldConsumptionInputsContainer = getView(R.id.revenueActualYieldConsumptionInputsContainer);
        actualOwnConsInput = getEd(R.id.revenueActualOwnConsInput);
        actualOwnConsInput.setEnabled(isMonitoring);
        actualSeedResInput = getEd(R.id.revenueActualSeedResInput);
        actualSeedResInput.setEnabled(isMonitoring);
        actualForMarketInput = getEd(R.id.revenueActualForMarketInput);

        actualForMarketInput.setEnabled(isMonitoring);
        yieldConsumptionRemarkInput = getEd(R.id.revenueYieldConsumptionRemarkInput);
        yieldConsumptionRemarkInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                refreshMarketableRevenue(isMonitoring);
            }
        });

        actualRemarkContainer = getView(R.id.item_expense_value_container);
    }

    private void refreshTotal(boolean isMonitoring) {
        String estimatedQuantityString = estimatedQuantityInput.getText().toString().trim();
        if (estimatedQuantityString.isEmpty()) {
            estimatedQuantityString = "0";
        }
        final double estimatedQuantity = Double.parseDouble(estimatedQuantityString);

        String actualQuantityString = actualQuantityInput.getText().toString().trim();
        if (actualQuantityString.isEmpty()) {
            actualQuantityString = "0";
        }
        final double actualQuantity = Double.parseDouble(actualQuantityString);

        String estimatedUnitPriceString = estimatedUnitPriceInput.getText().toString().trim();
        if (estimatedUnitPriceString.isEmpty()) {
            estimatedUnitPriceString = "0";
        }
        final double estimatedUnitPrice = Double.parseDouble(estimatedUnitPriceString);

        String actualUnitPriceString = actualUnitPriceInput.getText().toString().trim();
        if (actualUnitPriceString.isEmpty()) {
            actualUnitPriceString = "0";
        }
        final double actualUnitPrice = Double.parseDouble(actualUnitPriceString);

        final double estimatedTotal = estimatedQuantity * estimatedUnitPrice;
        final double actualTotal = actualQuantity * actualUnitPrice;
        setTotal(estimatedTotal, actualTotal);

        if (isMonitoring) {
            this.actualTotal = actualTotal;
            acatItemDto.actualTotalPrice = actualTotal;
        } else {
            acatItemDto.estimatedTotalPrice = estimatedTotal;
        }
//        setRespectiveRevenue(Double.toString(isMonitoring ? actualTotal : estimatedTotal));

        switch (activePosition) {
            case 0:
                averageRevenue = isMonitoring ? actualTotal : estimatedTotal;
                break;
            case 1:
                maximumRevenue = isMonitoring ? actualTotal : estimatedTotal;
                break;
            case 2:
                minimumRevenue = isMonitoring ? actualTotal : estimatedTotal;
                break;
        }

        setProbableRevenue(String.valueOf(averageRevenue));
        setMaximumRevenue(String.valueOf(maximumRevenue));
        setMinimumRevenue(String.valueOf(minimumRevenue));
    }

    private void setRespectiveRevenue(String value) {
        switch (activePosition) {
            case 0:
                refreshMarketableRevenue(isMonitoring);
                break;
            case 1:
                setMaximumRevenue(value);
                break;
            case 2:
                setMinimumRevenue(value);
                break;
        }
    }

    private void setProbableRevenue(@NonNull String value) {
        probableRevenueLabel.setText(value);
    }

    private void setMaximumRevenue(@NonNull String value) {
        maxRevenueLabel.setText(value);
    }

    private void setMinimumRevenue(@NonNull String value) {
        minRevenueLabel.setText(value);
    }

    private void refreshMarketableRevenue(boolean isMonitoring) {
        String estimatedQuantityString = estimatedForMarketInput.getText().toString().trim();
        if (estimatedQuantityString.isEmpty()) {
            estimatedQuantityString = "0";
        }
        final double estimatedQuantity = Double.parseDouble(estimatedQuantityString);

        String actualQuantityString = actualForMarketInput.getText().toString().trim();
        if (actualQuantityString.isEmpty()) {
            actualQuantityString = "0";
        }
        final double actualQuantity = Double.parseDouble(actualQuantityString);

        String estimatedUnitPriceString = estimatedUnitPriceInput.getText().toString().trim();
        if (estimatedUnitPriceString.isEmpty()) {
            estimatedUnitPriceString = "0";
        }
        final double estimatedUnitPrice = Double.parseDouble(estimatedUnitPriceString);

        String actualUnitPriceString = actualUnitPriceInput.getText().toString().trim();
        if (actualUnitPriceString.isEmpty()) {
            actualUnitPriceString = "0";
        }
        final double actualUnitPrice = Double.parseDouble(actualUnitPriceString);


        final double estimatedTotal = estimatedQuantity * estimatedUnitPrice;
        final double actualTotal = actualQuantity * actualUnitPrice;
        totalForMarketRevenue = estimatedTotal;

        setProbableRevenue(Double.toString(isMonitoring ? actualTotal : estimatedTotal));
        if (isMonitoring) {
            revenueSection.actualRevenue = revenueSection.actualSubTotal = actualTotal;
        } else {
            revenueSection.estimatedProbRevenue = estimatedTotal;
        }

    }

    @SuppressLint("CheckResult")
    private void loadCropItemRevenueSection() {
        cropACATLocal.get(cropACATId)
                .flatMap(cropACATData -> {
                    cropACAT = cropACATData.get();
                    firstExpenseMonth = cropACAT.firstExpenseMonth;
                    acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
                    acatItemDto.firstExpenseMonth = firstExpenseMonth;
                    acatItemDto.cropACATId = cropACATId;
                    index = ACATUtility.getMonthIndex(firstExpenseMonth);
                    return acatApplicationLocal.get(cropACAT.acatApplicationID);
                })
                .flatMap(acatApplicationData -> {
                    application = acatApplicationData.get();
                    return sectionLocal.get(cropACAT.revenueSectionId);
                })
                .flatMap(data -> {
                    revenueSection = data.get();
                    return sectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {

                    setCropLabel(cropACAT.cropName);
                    yieldSections.clear();
                    yieldSections.addAll(list);

                    if (yieldSections.isEmpty()) return;

                    highlightYieldTitle(activePosition);
                    generateCashFlow();
                    loadNetCashFlow(yieldSections.get(0), isMonitoring);
                    loadData(activePosition, isMonitoring);
                }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void loadNetCashFlow(@NonNull ACATRevenueSection section, boolean isMonitoring) {
        final String type = isMonitoring ? CashFlowHelper.ACTUAL_CASH_FLOW_TYPE : CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        acatItemLocal.get(section.yieldID)
                .flatMap(data ->{
                    ACATItem acatItem = data.get();
                    return cashFlowLocal.get(acatItem._id, type, CashFlowHelper.ITEM_CASH_FLOW);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (yieldCashFlow == null) {
                        return;
                    }
                    yieldCashFlow = list.get(0);
                    netCashFlowAdapter.notify(yieldCashFlow);
                });
    }

    @SuppressLint("CheckResult")
    private void loadCashFlow(@NonNull String referenceId, boolean isMonitoring) {
        final String type = isMonitoring ? CashFlowHelper.ACTUAL_CASH_FLOW_TYPE : CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        cashFlowLocal.get(referenceId, type, CashFlowHelper.ITEM_CASH_FLOW)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    yieldCashFlow = list.get(0);
                    yieldCashOutflowAdapter.notify(yieldCashFlow);
                });
    }


    private void generateCashFlow() {
        cashFlowData.clear();
        for (int i = 0; i < 12; i++) {
            cashFlowData.add(String.valueOf(0));
        }

        yieldCashOutflowAdapter = new ACATEstimatedRevenueCashFlowAdapter(this, firstExpenseMonth, cashFlowData);
        yieldCashOutflowRecyclerView.setAdapter(yieldCashOutflowAdapter);
        netCashFlowAdapter = new ACATEstimatedRevenueNetCashFlowAdapter(firstExpenseMonth, cashFlowData);
        netCashFlowRecyclerView.setAdapter(netCashFlowAdapter);
        yieldCashOutflowAdapter.notifyDataSetChanged();
        netCashFlowAdapter.notifyDataSetChanged();
    }

    private void setCropLabel(String cropLabel) {
        this.cropLabel.setText(cropLabel);
    }

    private void highlightYieldTitle(int position) {
        activePosition = position;
        final int length = yieldListContainer.getChildCount();
        for (int i = 0; i < length; i++) {
            final View yieldItem = yieldListContainer.getChildAt(i);
            final int background = i == position ? R.drawable.acat_item_selected_green : R.drawable.acat_green_custom_border;
            yieldItem.setBackgroundResource(background);
        }
    }

    private void initializeYieldListContainer() {
        final int length = yieldListContainer.getChildCount();
        for (int i = 0; i < length; i++) {
            final View yieldItem = yieldListContainer.getChildAt(i);
            yieldItem.setOnClickListener(yieldItemListener);
        }
    }

    private final View.OnClickListener yieldItemListener = v -> {
        final int position = yieldListContainer.indexOfChild(v);
        final ACATEstimatedYieldDto dto = gatherData();
        onYieldSelected(position, dto, isMonitoring);
    };

    @SuppressLint("CheckResult")
    private Completable onYieldSelected(int position, @NonNull ACATEstimatedYieldDto dto, boolean isMonitoring) {

        if (!toolbarMenu) {
            if (position == activePosition) return Completable.complete();
        }

        if (isMonitoring ? dto.actualUnit.isEmpty() : dto.estimatedUnit.isEmpty()) {
            showMissingUnitError(isMonitoring);
            return Completable.complete();
        }

        if (isMonitoring ? dto.actualQuantity.isEmpty() : dto.estimatedQuantity.isEmpty()) {
            showMissingQuantityError(isMonitoring);
            return Completable.complete();
        }

        if (isMonitoring ? parse(dto.actualQuantity) == 0: parse(dto.estimatedQuantity) == 0) {
            showInvalidQuantityError(isMonitoring);
            return Completable.complete();
        }

        if (isMonitoring ? dto.actualUnitPrice.isEmpty() : dto.estimatedUnitPrice.isEmpty()) {
            showMissingUnitPriceError(isMonitoring);
            return Completable.complete();
        }
        if (isMonitoring ? parse(dto.actualUnitPrice) == 0 : parse(dto.estimatedUnitPrice) == 0) {
            showInvalidUnitPriceError(isMonitoring);
            return Completable.complete();
        }

        if (yieldConsumption != null) {

            if (isMonitoring ? parse(dto.actualForMarket) == 0 : parse(dto.estimatedForMarket) == 0) {
                showMissingForMarketError(isMonitoring);
                return Completable.complete();
            }

            final double totalYieldConsumption =
                            parse(isMonitoring ? dto.actualOwnCons : dto.estimatedOwnCons) +
                                    parse(isMonitoring ? dto.actualSeedRes : dto.estimatedSeedRes) +
                                    parse(isMonitoring ? dto.actualForMarket : dto.estimatedForMarket);

            if (totalYieldConsumption != parse(isMonitoring ? dto.actualQuantity : dto.estimatedQuantity)) {
                showInvalidYieldConsumptionInputsError(isMonitoring);
                return Completable.complete();
            }
        }

        if (yieldCashOutflowAdapter.cashFlowList.size() != 0) {
            if (isMonitoring) {
                acatItemDto.actualCashFlow = actualCashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                acatItemDto.actualCashFlow.classification = actualCashFlow.classification = yieldCashFlow.classification;
                acatItemDto.actualCashFlow.type = actualCashFlow.type = yieldCashFlow.type;
                acatItemDto.actualCashFlow.referenceId = actualCashFlow.referenceId = acatItem._id;
                acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
            } else {
                acatItemDto.estimatedCashFlow = estimatedCashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                acatItemDto.estimatedCashFlow.classification = estimatedCashFlow.classification = yieldCashFlow.classification;
                acatItemDto.estimatedCashFlow.type = estimatedCashFlow.type = yieldCashFlow.type;
                acatItemDto.estimatedCashFlow.referenceId = estimatedCashFlow.referenceId = acatItem._id;
                acatItemDto.acatApplicationId = cropACAT.acatApplicationID;
            }
        }


        switch (activePosition) {
            case 0:
                e("WARNING");
                saveYieldData(dto, isMonitoring, activePosition);
                if (isMonitoring) {
                    probableUnitPrice = parse(dto.actualUnitPrice);
                } else {
                    probableUnitPrice = parse(dto.estimatedUnitPrice);
                }
//                saveYieldCashOutflowData(); Todo Save cash flow later.
//                saveProbableRevenueData(isMonitoring, activePosition);
                break;
            case 1:
                saveYieldData(dto, isMonitoring, activePosition);
//                saveYieldCashOutflowData(); Todo Save cash flow later.
//                saveMaxRevenueData(isMonitoring, activePosition);
                break;
            case 2:
                saveYieldData(dto, isMonitoring, activePosition);
//                saveYieldCashOutflowData(); Todo Save cash flow later.
//                saveMinRevenueData(isMonitoring, activePosition);
                break;

        }

        activePosition = position;
        highlightYieldTitle(position);

        if (!isMonitoring && !toolbarMenu) loadData(position, isMonitoring);

        toolbarMenu = false;

        return Completable.complete();
    }

//    @SuppressLint("CheckResult")
//    private Completable saveProbableRevenueData(boolean isMonitoring, int position) {
//        return saveProbableRevenueSectionData(isMonitoring, position)
//                .flatMapCompletable(this::saveProbableParentSectionData)
//                .subscribeOn(schedulers.background());
//    }

//    @SuppressLint("CheckResult")
//    private Completable saveMaxRevenueData(boolean isMonitoring, int position) {
//        return saveMaxRevenueSectionData(isMonitoring, position)
//                .flatMapCompletable(this::saveMaxParentSectionData)
//                .subscribeOn(schedulers.background());
//
//    }

//    @SuppressLint("CheckResult")
//    private Completable saveMinRevenueData(boolean isMonitoring, int position) {
//        return saveMinRevenueSectionData(isMonitoring, position)
//                .flatMapCompletable(this::saveMinParentSectionData)
//                .subscribeOn(schedulers.background());
//    }

    private Observable<ACATRevenueSection> saveProbableRevenueSectionData(boolean isMonitoring, int position) {
        final List<ACATRevenueSection> sectionList = new ArrayList<>();
        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();
        return sectionLocal.getAllByIds(revenueSection.subSectionIDs)
                .flatMap(list -> {
                    sectionList.clear();
                    sectionList.addAll(list);
                    return acatItemLocal.get(sectionList.get(position).yieldID); //was ._id
                })
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, isMonitoring ? ACATItemValueHelper.ACTUAL_VALUE_TYPE : ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
                    final ACATItemValue value = data.get();

                    if (isMonitoring) {
                        sectionList.get(position).actualSubTotal = value.totalPrice;
                        sectionList.get(position).actualRevenue = value.totalPrice;
                    } else {
                        sectionList.get(position).estimatedSubTotal = value.totalPrice;
                        sectionList.get(position).estimatedProbRevenue = value.totalPrice;
                    }

                    totalForMarketRevenue = value.totalPrice;
                    return sectionLocal.put(sectionList.get(position));

                })
                .flatMap(done -> {
                    request.section = new ACATRevenueSection();
                    request.section = sectionList.get(position);
                    request.acatApplicationId = cropACAT.acatApplicationID;

                    return cashFlowLocal.get(sectionList.get(position)._id,
                            isMonitoring ? CashFlowHelper.ACTUAL_CASH_FLOW_TYPE : CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    if (isMonitoring)
                        request.actualCashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                    else {
                        request.estimatedCashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                    }
                    parentCashFlow = getCashFlowData(yieldCashOutflowAdapter.cashFlowList);
                    return revenueRepo.updateACATRevenueSection(request.section._id, request);
                })
                .flatMap(done -> yieldConsumptionLocal.get(sectionList.get(position).yieldConsumptionID))
                .flatMap(yieldConsumptionData -> {
                    YieldConsumptionRequest yieldRequest = new YieldConsumptionRequest();
                    yieldRequest.yieldConsumption = yieldConsumptionData.get();
                    return yieldConsumptionRepo.update(yieldConsumptionData.get()._id, yieldRequest);
                })
                .flatMap(done -> sectionLocal.get(cropACAT.revenueSectionId))
                .flatMap(data -> {
                    request.section = data.get();
                    if (isMonitoring) {
                        request.section.actualRevenue = sectionList.get(position).actualSubTotal;
                        request.section.actualSubTotal = sectionList.get(position).actualSubTotal;
                        request.actualCashFlow = parentCashFlow;
                    } else {
                        request.section.estimatedProbRevenue = sectionList.get(position).estimatedSubTotal;
                        request.section.estimatedSubTotal = sectionList.get(position).estimatedSubTotal;
                        request.estimatedCashFlow = parentCashFlow;
                    }

                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                });
    }

    private Observable<ACATRevenueSection> saveMaxRevenueSectionData(boolean isMonitoring, int position) {
        final List<ACATRevenueSection> sectionList = new ArrayList<>();
        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();

        return sectionLocal.getAllByIds(revenueSection.subSectionIDs)
                .flatMap(list -> {
                    sectionList.clear();
                    sectionList.addAll(list);
                    return acatItemLocal.get(sectionList.get(position).yieldID); //was ._id
                })
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, isMonitoring ? ACATItemValueHelper.ACTUAL_VALUE_TYPE : ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
                    final ACATItemValue value = data.get();

                    if (isMonitoring) {
                        sectionList.get(position).actualSubTotal = value.totalPrice;
                    } else {
                        sectionList.get(position).estimatedSubTotal = value.totalPrice;
                        sectionList.get(position).estimatedMaxRevenue = value.totalPrice;
                    }

                    return sectionLocal.put(sectionList.get(position));
                })
                .flatMap(revenueSection -> {
                    request.section = new ACATRevenueSection();
                    request.section = sectionList.get(position);
                    request.acatApplicationId = cropACAT.acatApplicationID;
                    return cashFlowLocal.get(sectionList.get(position)._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(request.section._id, request);
                })
                .flatMap(revenueSection -> sectionLocal.get(cropACAT.revenueSectionId))
                .flatMap(data -> {
                    request.section = new ACATRevenueSection();
                    request.section = data.get();
                    request.section.estimatedProbRevenue = sectionList.get(0).estimatedSubTotal;
                    request.section.estimatedMaxRevenue = sectionList.get(1).estimatedSubTotal;
                    request.estimatedCashFlow = parentCashFlow;
                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                });
    }

    private Observable<ACATRevenueSection> saveMinRevenueSectionData(boolean isMonitoring, int position) {
        final List<ACATRevenueSection> sectionList = new ArrayList<>();
        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();
        return sectionLocal.getAllByIds(revenueSection.subSectionIDs)
                .flatMap(list -> {
                    sectionList.clear();
                    sectionList.addAll(list);
                    return acatItemLocal.get(sectionList.get(position).yieldID); //was ._id
                })
                .flatMap(data -> {
                    final ACATItem item = data.get();
                    return acatItemValueLocal.getByType(item._id, isMonitoring ? ACATItemValueHelper.ACTUAL_VALUE_TYPE : ACATItemValueHelper.ESTIMATED_VALUE_TYPE); // TODO: Consider making it to work with actual values as well
                })
                .flatMap(data -> {
                    final ACATItemValue value = data.get();
                    if (isMonitoring) {
                        sectionList.get(position).actualSubTotal = value.totalPrice;
                    } else {
                        sectionList.get(position).estimatedSubTotal = value.totalPrice;
                        sectionList.get(position).estimatedMinRevenue = value.totalPrice;
                    }

                    return sectionLocal.put(sectionList.get(position));
                })
                .flatMap(revenueSection -> {
                    request.section = new ACATRevenueSection();
                    request.section = sectionList.get(position);
                    request.acatApplicationId = cropACAT.acatApplicationID;
                    return cashFlowLocal.get(sectionList.get(position)._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(request.section._id, request);
                })
                .flatMap(revenueSection -> sectionLocal.get(cropACAT.revenueSectionId))
                .flatMap(data -> {
                    request.section = new ACATRevenueSection();
                    request.section = data.get();
                    request.section.estimatedProbRevenue = sectionList.get(0).estimatedSubTotal;
                    request.section.estimatedMaxRevenue = sectionList.get(1).estimatedSubTotal;
                    request.section.estimatedMinRevenue = sectionList.get(2).estimatedSubTotal;
                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                });
    }

    private Observable<ACATRevenueSection> saveProbableParentSectionData(@NonNull ACATItemValue value) {
        revenueSection.estimatedProbRevenue = totalForMarketRevenue;
        revenueSection.estimatedSubTotal = value.totalPrice;

        if (isMonitoring) {
            revenueSection.actualRevenue = revenueSection.actualSubTotal = value.totalPrice;
        }
        return sectionLocal.put(revenueSection)
                .subscribeOn(schedulers.background());
    }

    private Completable saveMaxParentSectionData(@NonNull ACATItemValue value) {
        revenueSection.estimatedMaxRevenue = value.totalPrice;
        revenueSection.estimatedSubTotal = value.totalPrice;
        return sectionLocal.put(revenueSection)
                .flatMapCompletable(updated -> {
                    Observable.just(value);
                    return Completable.complete();
                })
                .subscribeOn(schedulers.background());
    }

    private Completable saveMinParentSectionData(@NonNull ACATItemValue value) {
        revenueSection.estimatedMinRevenue = value.totalPrice;
        revenueSection.estimatedSubTotal = value.totalPrice;
        return sectionLocal.put(revenueSection)
                .flatMapCompletable(updated -> {
                    Observable.just(updated);
                    return Completable.complete();
                })
                .subscribeOn(schedulers.background());
    }

    @SuppressLint("CheckResult")
    private void saveYieldData(@NonNull ACATEstimatedYieldDto dto, boolean isMonitoring, int position){
        showUpdatingMessage();

        acatItemDto.acatItemId = acatItem._id;
        updateACATItemData(dto, isMonitoring)
                .flatMap(done ->  isMonitoring ? updateActualACATItemValueData(actualValue, dto) : updateEstimatedACATItemValueData(estimatedValue, dto))
                .flatMapCompletable(done -> updateYieldConsumption(yieldConsumption, dto))
                .andThen(isMonitoring ? updater.uploadActualRevenueForACATItem(acatItemDto) : updater.uploadEstimatedRevenueForACATItem(acatItemDto))

                .flatMap(done -> saveRevenueData(isMonitoring, position))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    hideUpdatingMessage();
                }, this::onUpdateFailed);
    }

    private Observable<CashFlow> saveYieldNetCashFlow(boolean isMonitoring) {
        if (isMonitoring) {
            final ACATRevenueSection activeSection = yieldSections.get(0);
            return cashFlowLocal.get(activeSection._id,
                    CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                    .flatMap(list -> {
                        if (list.isEmpty()) {
                            return Observable.error(new Throwable("Could not load Cashflows from reference ID: " + activeSection._id));
                        } else {
//                            final CashFlow cashFlow = list.get(0);
                            final CashFlow cashFlow = getCashFlowData(netCashFlowAdapter.cashFlowList);
                            cashFlow.type = list.get(0).type;
                            cashFlow.id = list.get(0).id;
                            cashFlow.referenceId = list.get(0).referenceId;
                            cashFlow.classification = list.get(0).classification;
                            cashFlowLocal.put(cashFlow);
                            return cashFlowLocal.get(revenueSection._id,
                                    CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                        }
                    })
                    .flatMap(list -> {
                        if (list.isEmpty()) {
                            return Observable.error(new Throwable("Could not load CashFlows from reference ID: " + revenueSection._id));
                        } else {
                            if (activeSection.yieldConsumptionID != null) {     // Copy the probable.estimated_cash_flow to revenue.estimated_cash_flow (only if the current yield is a probable yield)
//                                final CashFlow cashFlow = list.get(0);
                                final CashFlow cashFlow = getCashFlowData(netCashFlowAdapter.cashFlowList);
                                cashFlow.type = list.get(0).type;
                                cashFlow.id = list.get(0).id;
                                cashFlow.referenceId = list.get(0).referenceId;
                                cashFlow.classification = list.get(0).classification;
                                return cashFlowLocal.put(cashFlow);
                            } else {                            // Maximum or minimum yield
                                return Observable.just(actualCashFlow);
                            }
                        }
                    });

        } else {
            final ACATRevenueSection activeSection = yieldSections.get(activePosition);
            return cashFlowLocal.get(activeSection._id,
                    CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                    .flatMap(list -> {
                        if (list.isEmpty()) {
                            return Observable.error(new Throwable("Could not load Cashflows from reference ID: " + activeSection._id));
                        } else {
                            final CashFlow cashFlow = getCashFlowData(netCashFlowAdapter.cashFlowList);
                            cashFlow.type = list.get(0).type;
                            cashFlow.id = list.get(0).id;
                            cashFlow.referenceId = list.get(0).referenceId;
                            cashFlow.classification = list.get(0).classification;

                            cashFlowLocal.put(cashFlow);
                            return cashFlowLocal.get(revenueSection._id,
                                    CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                                    CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                        }
                    })
                    .flatMap(list -> {
                        if (list.isEmpty()) {
                            return Observable.error(new Throwable("Could not load CashFlows from reference ID: " + revenueSection._id));
                        } else {
                            if (activeSection.yieldConsumptionID != null) {     // Copy the probable.estimated_cash_flow to revenue.estimated_cash_flow (only if the current yield is a probable yield)
//                                final CashFlow cashFlow = list.get(0);
                                final CashFlow cashFlow = getCashFlowData(netCashFlowAdapter.cashFlowList);
                                cashFlow.type = list.get(0).type;
                                cashFlow.id = list.get(0).id;
                                cashFlow.referenceId = list.get(0).referenceId;
                                cashFlow.classification = list.get(0).classification;
                                return cashFlowLocal.put(cashFlow);
                            } else {                            // Maximum or minimum yield
                                return Observable.just(estimatedCashFlow);
                            }
                        }
                    });

        }
    }

    private Observable<ACATRevenueSection> saveRevenueData(boolean isMonitoring, int position) {
        switch (position) {
            case 0:
                return saveProbableRevenueSectionData(isMonitoring, position);
            case 1:
                return saveMaxRevenueSectionData(isMonitoring, position);
            case 2:
                return saveMinRevenueSectionData(isMonitoring, position);
        }

        return Observable.just(revenueSection);
    }

    private Observable<ACATItem> updateACATItemData(@NonNull ACATEstimatedYieldDto dto, boolean isMonitoring) {
        if (isMonitoring) {
            acatItemDto.unit = acatItem.unit = dto.actualUnit;
            acatItemDto.remark = acatItem.remark = dto.yieldRemark;
        } else {
            acatItemDto.unit = acatItem.unit = dto.estimatedUnit;
            acatItemDto.remark = acatItem.remark = dto.yieldRemark;
        }

        return acatItemLocal.put(acatItem)
                .subscribeOn(schedulers.background());
     }

     private Observable<ACATItemValue> updateEstimatedACATItemValueData(@NonNull ACATItemValue value, @NonNull ACATEstimatedYieldDto dto) {
        value.acatItemId = estimatedValue.acatItemId;
        value.type = ACATItemValueHelper.ESTIMATED_VALUE_TYPE;
        value.value = parse(dto.estimatedQuantity);
        value.unitPrice = parse(dto.estimatedUnitPrice);
         if (!dto.estimatedForMarket.isEmpty() && activePosition == 0)//Compute revenue from the "For Market" estimatedItemValue
             value.totalPrice = Double.parseDouble(dto.estimatedForMarket) * value.unitPrice;
         else
             value.totalPrice = value.value * value.unitPrice;

         return acatItemValueLocal.put(value)
                 .subscribeOn(schedulers.background());
     }

    private Observable<ACATItemValue> updateActualACATItemValueData(@NonNull ACATItemValue value, @NonNull ACATEstimatedYieldDto dto) {
        value.acatItemId = actualValue.acatItemId;
        value.type = ACATItemValueHelper.ACTUAL_VALUE_TYPE;
        value.value = parse(dto.actualQuantity);
        value.unitPrice = parse(dto.actualUnitPrice);

        if (!dto.actualForMarket.isEmpty() && activePosition == 0)
            value.totalPrice = Double.parseDouble(dto.actualForMarket) * value.unitPrice;
        else
            value.totalPrice = value.value * value.unitPrice;

        return acatItemValueLocal.put(value)
                .subscribeOn(schedulers.background());
    }

    private Completable updateYieldConsumption(@Nullable YieldConsumption consumption, @NonNull ACATEstimatedYieldDto dto) {
        if (consumption == null) return Completable.complete();

        consumption.estimatedOwnCons = parse(dto.estimatedOwnCons);
        consumption.estimatedSeedReserve = parse(dto.estimatedSeedRes);
        consumption.estimatedForMarket = parse(dto.estimatedForMarket);

        consumption.actualOwnCons = parse(dto.actualOwnCons);
        consumption.actualSeedReserve = parse(dto.actualSeedRes);
        consumption.actualForMarket = parse(dto.actualForMarket);

        consumption.remark = dto.yieldConsumptionRemark;

        return yieldConsumptionLocal.put(consumption)
                .flatMapCompletable(done -> Completable.complete())
                .subscribeOn(schedulers.background());
    }

    public ACATEstimatedYieldDto gatherData() {
        final ACATEstimatedYieldDto dto = new ACATEstimatedYieldDto();

        dto.estimatedUnit = getText(estimatedUnitInput);
        dto.estimatedQuantity = getText(estimatedQuantityInput);
        dto.estimatedUnitPrice = getText(estimatedUnitPriceInput);
        dto.estimatedOwnCons = getText(estimatedOwnConsInput);
        dto.estimatedSeedRes = getText(estimatedSeedResInput);
        dto.estimatedForMarket = getText(estimatedForMarketInput);
        dto.yieldRemark = getText(yieldRemarkInput);

        dto.actualUnit = getText(actualUnitInput);
        dto.actualQuantity = getText(actualQuantityInput);
        dto.actualUnitPrice = getText(actualUnitPriceInput);
        dto.actualOwnCons = getText(actualOwnConsInput);
        dto.actualSeedRes = getText(actualSeedResInput);
        dto.actualForMarket = getText(actualForMarketInput);
        dto.yieldConsumptionRemark = getText(yieldConsumptionRemarkInput);

        return dto;
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

    private String getText(@NonNull EditText input) {
        return input.getText().toString().trim();
    }

    private double parse(@Nullable String value) {
        if (value == null) return 0;
        if (value.trim().isEmpty()) return 0;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @SuppressLint("CheckResult")
    private void loadData(int position, boolean isMonitoring) {
        final ACATRevenueSection section = yieldSections.get(position);

        // ACATItem -> "yield"
        loadYieldAndConsumption(section, isMonitoring);
        loadRevenues();
    }

    @SuppressLint("CheckResult")
    private void loadYieldAndConsumption(@NonNull ACATRevenueSection section, boolean isMonitoring) {
        acatItemDto.sectionId = section.yieldID;
        acatItemLocal.get(section.yieldID)
                .flatMap(data -> {
                    acatItem = new ACATItem();
                    acatItem = data.get();
                    acatItemDto.unit = acatItem.unit;
                    acatItemDto.costListId = acatItem.costListId;
                    return acatItemValueLocal.getByType(acatItem._id, ACATItemValueHelper.ESTIMATED_VALUE_TYPE);
                })
                .flatMap(data -> {
                    estimatedItemValue = data.get();
                    return acatItemValueLocal.getByType(acatItem._id, ACATItemValueHelper.ACTUAL_VALUE_TYPE);
                })
                .flatMap(data -> {
                    actualItemValue = data.get();
                    return yieldConsumptionLocal.get(section.yieldConsumptionID == null ? "-" : section.yieldConsumptionID);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    setUnit(acatItem.unit);
                    setQuantity(estimatedItemValue.value, actualItemValue.value);
                    setUnitPrice(estimatedItemValue.unitPrice, actualItemValue.unitPrice);
                    setTotal(estimatedItemValue.value * estimatedItemValue.unitPrice, actualItemValue.value * actualItemValue.unitPrice);
                    if (activePosition == 0) {
                        totalProbableRevenue = estimatedItemValue.value * estimatedItemValue.unitPrice;
                        probableUnitPrice = estimatedItemValue.unitPrice;
                    }
                    //view.setTotal(estimatedItemValue.totalPrice, estimated);
                    setYieldRemark(acatItem.remark);

                    toggleYieldConsumptionInputs(!data.empty());

                    if (data.empty()) {
                        yieldConsumption = null;
                    } else {
                        yieldConsumption = data.get();

                        setOwnCons(yieldConsumption.estimatedOwnCons, yieldConsumption.actualOwnCons);
                        setSeedRes(yieldConsumption.estimatedSeedReserve, yieldConsumption.actualSeedReserve);
                        setForMarket(yieldConsumption.estimatedForMarket, yieldConsumption.actualForMarket);

                        setYieldConsumptionRemark(yieldConsumption.remark);
                    }
                    if (isMonitoring) {
                        actualValue = actualItemValue;
                    } else {
                        estimatedValue = estimatedItemValue;
                    }


                    loadCashFlow(acatItem._id, isMonitoring);
                });
    }

    private void loadRevenues() {
        setProbableRevenue(String.valueOf(revenueSection.estimatedProbRevenue));
        setMaximumRevenue(String.valueOf(revenueSection.estimatedMaxRevenue));
        setMinimumRevenue(String.valueOf(revenueSection.estimatedMinRevenue));
    }

    @SuppressLint("CheckResult")
    public void onUpdateComplete(@NonNull CropACAT response) {
        if (acatApplicationResponse != null) {
            response.complete = true;
            response.active = false;
            cropACATLocal.put(response)
                    .flatMap(done -> isMonitoring ? uploadActualRevenue() : uploadEstimatedRevenueSection())
                    .flatMap(done -> cropACATLocal.getActiveClientCropACAT(clientId, cropACAT.acatApplicationID))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(cropACATData -> {
                                hideUpdatingMessage();
                                if (!cropACATData.empty() && !isEditing) {
                                    openCropList(clientId);
                                } else {
                                    getActivity().finish();
                                    openLoanProposal(clientId, cropACAT.acatApplicationID);
                                }
                            },
                            this::onUpdateFailed);
        }

    }

    private Observable<ACATRevenueSection> uploadEstimatedRevenueSection() {
        ACATRevenueSection section = new ACATRevenueSection();

        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();
        request.acatApplicationId = cropACAT.acatApplicationID;
        return sectionLocal.get(cropACAT.revenueSectionId)
                .flatMap(data -> {
                    revenueSection = data.get();
                    return sectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .flatMap(list -> {
                    yieldSections.clear();
                    yieldSections.addAll(list);
                    request.section = list.get(0);
                    return cashFlowLocal.get(list.get(0)._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(0)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(1);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(1)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(2);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return revenueRepo.updateACATRevenueSection(yieldSections.get(2)._id, request);
                })
                .flatMap(response -> {
                    request.section = yieldSections.get(0);
                    return cashFlowLocal.get(request.section._id,
                            CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.estimatedCashFlow = list.get(0);
                    return sectionLocal.get(cropACAT.revenueSectionId);
                })
                .flatMap(data -> {
                    revenueSection = data.get();
                    request.section = revenueSection;
                    switch (activePosition) {
                        case 0:
                            request.section.estimatedProbRevenue = acatItemDto.estimatedTotalPrice;
                            break;
                        case 1:
                            request.section.estimatedMaxRevenue = acatItemDto.estimatedTotalPrice;
                            break;
                        case 2:
                            request.section.estimatedMinRevenue = acatItemDto.estimatedTotalPrice;
                            break;
                    }

                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                })
                .onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends ACATRevenueSection>>) throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
                        return Observable.just(section);
                    }
                    else
                        throw new Exception(throwable);
                });
    }

    private Observable<ACATRevenueSection> uploadActualRevenue() {
        ACATRevenueSection section = new ACATRevenueSection();

        ACATRevenueSectionRequest request = new ACATRevenueSectionRequest();
        request.acatApplicationId = cropACAT.acatApplicationID;
        return  sectionLocal.get(cropACAT.revenueSectionId)
                .flatMap(data-> {
                    revenueSection = data.get();
                    return cashFlowLocal.get(revenueSection._id,
                            CashFlowHelper.ACTUAL_CASH_FLOW_TYPE,
                            CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE);
                })
                .flatMap(list -> {
                    request.actualCashFlow = list.get(0);
                    request.section = revenueSection;
                    request.section.estimatedProbRevenue = totalProbableRevenue;
                    request.section.actualRevenue = request.section.actualSubTotal = actualTotal;
                    return revenueRepo.updateACATRevenueSection(revenueSection._id, request);
                })
                .onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends ACATRevenueSection>>) throwable -> {
                    String error = throwable.getMessage();
                    if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                            error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
                        acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
                        return Observable.just(section);
                    }
                    else
                        throw new Exception(throwable);
                });
    }
    public void onUpdateFailed(@NonNull Throwable throwable) {
        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION) ||
                error.equals(ApiErrors.NO_ROUTE_TO_HOST) || (throwable instanceof UnknownHostException)) {
            acatApplicationSyncLocalSource.markForUpload(cropACAT.acatApplicationID);
            hideUpdatingMessage();
            return;
        }

        final String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;
        throwable.printStackTrace();
        hideUpdatingMessage();
        showError(Result.createError(message, error));
    }

    public void openLoanProposal(@NonNull String clientId, @NonNull String acatApplicationId) {
        final Intent intent = new Intent(getActivity(), ACATLoanProposalActivity.class);
        intent.putExtra(ACATLoanProposalActivity.CLIENT_ID, clientId);
        intent.putExtra(ACATLoanProposalActivity.ACAT_ID, acatApplicationId);
        startActivity(intent);
    }

    public void openCropList(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    public void openPreviousScreen(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCostEstimationActivity.class);
//        intent.putExtra(ACATCostEstimationActivity.IS_EDITING, true);
        intent.putExtra(ACATCostEstimationActivity.ARG_CROP_ACAT_ID, cropACATId);
        intent.putExtra(ACATCostEstimationActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATCostEstimationActivity.IS_MONITORING, isMonitoring);
        startActivity(intent);
        getActivity().finish();
    }

    public void showUpdatingMessage() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_acat_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    public void hideUpdatingMessage() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showError(@NonNull Result result) {
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


    private void setTotal(double estimatedTotal, double actualTotal) {
        estimatedTotalLabel.setText(String.valueOf(estimatedTotal));
        actualTotalLabel.setText(String.valueOf(actualTotal));
    }

    public void setUnit(@NonNull String unit) {
        estimatedUnitInput.setText(unit);
        actualUnitInput.setText(unit);
    }

    public void setQuantity(double estimatedQuantity, double actualQuantity) {
        estimatedQuantityInput.setText(String.valueOf(estimatedQuantity));
        actualQuantityInput.setText(String.valueOf(actualQuantity));
    }

    public void setUnitPrice(double estimatedUnitPrice, double actualUnitPrice) {
        estimatedUnitPriceInput.setText(String.valueOf(estimatedUnitPrice));
        actualUnitPriceInput.setText(String.valueOf(actualUnitPrice));
    }

    public void setOwnCons(double estimatedValue, double actualValue) {
        estimatedOwnConsInput.setText(String.valueOf(estimatedValue));
        actualOwnConsInput.setText(String.valueOf(actualValue));
    }

    public void setSeedRes(double estimatedValue, double actualValue) {
        estimatedSeedResInput.setText(String.valueOf(estimatedValue));
        actualSeedResInput.setText(String.valueOf(actualValue));
    }

    public void setForMarket(double estimatedValue, double actualValue) {
        estimatedForMarketInput.setText(String.valueOf(estimatedValue));
        actualForMarketInput.setText(String.valueOf(actualValue));
    }

    public void setYieldRemark(@NonNull String remark) {
        yieldRemarkInput.setText(remark);
    }

    public void setYieldConsumptionRemark(@NonNull String remark) {
        yieldConsumptionRemarkInput.setText(remark);
    }

    public void showMissingUnitError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualUnitInput : estimatedUnitInput);
        target.setError(getString(R.string.error_missing_unit_error));
        target.requestFocus();
    }

    public void showMissingQuantityError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualQuantityInput: estimatedQuantityInput);
        target.setError(getString(R.string.error_missing_quantity_error));
        target.requestFocus();
    }

    public void showInvalidQuantityError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualQuantityInput: estimatedQuantityInput);
        target.setError(getString(R.string.error_invalid_quantity_error));
        target.requestFocus();
    }

    public void showMissingUnitPriceError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualUnitPriceInput: estimatedUnitPriceInput);
        target.setError(getString(R.string.error_missing_unit_price_error));
        target.requestFocus();
    }

    public void showInvalidUnitPriceError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualUnitPriceInput: estimatedUnitPriceInput);
        target.setError(getString(R.string.error_invalid_unit_price_error));
        target.requestFocus();
    }

    public void showMissingOwnConsError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualOwnConsInput : estimatedOwnConsInput);
        target.setError(getString(R.string.error_missing_own_cons_error));
        target.requestFocus();
    }

    public void showMissingSeedResError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualSeedResInput : estimatedSeedResInput);
        target.setError(getString(R.string.error_missing_seed_res_error));
        target.requestFocus();
    }

    public void showMissingForMarketError(boolean isMonitoring) {
        final EditText target = (isMonitoring ? actualForMarketInput : estimatedForMarketInput);
        target.setError(getString(R.string.error_missing_for_market_error));
        target.requestFocus();
    }

    public void showInvalidYieldConsumptionInputsError(boolean isMonitoring) {
        final String error = getString(R.string.error_invalid_yield_consumption_quantity_error);

        (isMonitoring ? actualQuantityInput: estimatedQuantityInput).setError(error);
        (isMonitoring ? actualOwnConsInput : estimatedOwnConsInput).setError(error);
        (isMonitoring ? actualSeedResInput : estimatedSeedResInput).setError(error);
        (isMonitoring ? actualForMarketInput : estimatedForMarketInput).setError(error);
    }

    public void toggleYieldConsumptionInputs(boolean show) {
        final int visibility = show ? View.VISIBLE : View.GONE;
        estimatedYieldConsumptionInputsContainer.setVisibility(visibility);
        actualYieldConsumptionInputsContainer.setVisibility(visibility);
        yieldIndicatorContainer.setVisibility(visibility);
        actualRemarkContainer.setVisibility(visibility);
    }

    @Override
    public void onErrorDialogDismissed() {
        if (isMonitoring) {
            getActivity().onBackPressed();
        }

        hideUpdatingMessage();
    }

    @Override
    public void onCashFlowChanged(List<String> cashFlow) {
        netCashFlowAdapter.notify(getCashFlowData(refreshNetCashFlow(cashFlow)));
    }

    private List<String> refreshNetCashFlow(List<String> cashFlowData) {
        List<String> netCashFlow = new ArrayList<>();
        final int length = cashFlowData.size();
        for (int i = 0; i < length; i++) {

            final double currentMonthValue = cashFlowData.get(i).isEmpty() ? 0 : Double.parseDouble(cashFlowData.get(i));


            double previousNetMonthValue = 0;

            if (i > 0) {
                previousNetMonthValue = Double.parseDouble(netCashFlow.get(i - 1));
            }
            String newValue = String.valueOf(currentMonthValue + previousNetMonthValue);
            netCashFlow.add(i, newValue);
        }
        return netCashFlow;
    }
}
