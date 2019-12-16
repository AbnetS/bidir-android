package com.gebeya.mobile.bidir.ui.crop_summary_list;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValueHelper;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumption;
import com.gebeya.mobile.bidir.data.yieldconsumption.local.YieldConsumptionLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationUtlity;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryListPresenter implements CropSummaryListContract.Presenter {

    private static final String INPUTS_AND_ACTIVITY_COST = "Inputs And Activity Costs";
    private static final String INPUT = "INPUT";
    private static final String LABOUR_COST = "LABOUR COST";
    private static final String OTHER_COST = "OTHER COSTS";

    private final String clientId;
    private final String cropId;

    private CropSummaryListContract.View view;

    private CropACAT cropACAT;
    private ACATCostSection acatCostSection;
    private ACATRevenueSection revenueSection;
    private ACATItem acatItem;
    private ACATItemValue acatItemValue;
    private CashFlow cashFlow;
    @Nullable private YieldConsumption yieldConsumption;
    private final List<ACATItemValue> acatItemValueList;

    private final List<ACATRevenueSection> yieldSections;
    private final List<ACATCostSection> sections;
    private final List<ACATCostSection> acatCostSections;
    private final List<SummaryCashFlowItem> cashFlowItems;

    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATRevenueSectionLocalSource revenueSectionLocal;
    @Inject YieldConsumptionLocalSource yieldConsumptionLocal;
    @Inject ACATItemLocalSource acatItemLocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;
    @Inject ACATCostSectionLocalSource acatCostSectionLocal;
    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject SchedulersProvider schedulers;

    public CropSummaryListPresenter(@NonNull String clientId, @NonNull String cropId) {
        this.clientId = clientId;
        this.cropId = cropId;
        sections = new ArrayList<>();
        acatCostSections = new ArrayList<>();
        yieldSections = new ArrayList<>();
        acatItemValueList = new ArrayList<>();
        cashFlowItems = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
        cropACATLocal.getClientCropItem(clientId, cropId)
                .flatMap(data -> {
                    cropACAT = data.get();
                    return acatCostSectionLocal.getAll();
                })
                .flatMap(list -> {
                    sections.clear();
                    sections.addAll(list);
                    return acatCostSectionLocal.get(cropACAT.costSectionId);
                })
                .flatMap(data -> {
                    acatCostSection = data.get();
                    acatCostSections.clear();
                    acatCostSections.addAll(ACATCostEstimationUtlity.getCostSectionList(acatCostSection, sections));
                    acatCostSections.add(acatCostSection);
                    return revenueSectionLocal.get(cropACAT.revenueSectionId);
                })
                .flatMap(data -> {
                    revenueSection = data.get();
                    return cashFlowLocal.get(cropACAT._id, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE, CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE);
                })
                .flatMap(cashFlows -> {
                    cashFlow = cashFlows.get(0);
                    return revenueSectionLocal.getAllByIds(revenueSection.subSectionIDs);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                            loadData();
                            if (view == null) return;
                            yieldSections.clear();
                            yieldSections.addAll(list);

                            if (yieldSections.isEmpty()) return;
                            generateCashFlowData(cashFlow);
                            loadYieldData(yieldSections);

                        },
                        Throwable::printStackTrace
                );
    }

    private void generateCashFlowData(@NonNull CashFlow cashFlow) {
        final List<String> months = Arrays.asList(new DateFormatSymbols().getShortMonths());
        cashFlowItems.clear();

        cashFlowItems.add(createItem(months.get(0), cashFlow.january));
        cashFlowItems.add(createItem(months.get(1), cashFlow.february));
        cashFlowItems.add(createItem(months.get(2), cashFlow.march));
        cashFlowItems.add(createItem(months.get(3), cashFlow.april));
        cashFlowItems.add(createItem(months.get(4), cashFlow.may));
        cashFlowItems.add(createItem(months.get(5), cashFlow.june));
        cashFlowItems.add(createItem(months.get(6), cashFlow.july));
        cashFlowItems.add(createItem(months.get(7), cashFlow.august));
        cashFlowItems.add(createItem(months.get(8), cashFlow.september));
        cashFlowItems.add(createItem(months.get(9), cashFlow.october));
        cashFlowItems.add(createItem(months.get(10), cashFlow.november));
        cashFlowItems.add(createItem(months.get(11), cashFlow.december));

        view.loadCashFlowData();
    }

    private SummaryCashFlowItem createItem(@NonNull String month, double value) {
        return new SummaryCashFlowItem(month, String.valueOf(value));
    }

    private void loadYieldData(@Nullable List<ACATRevenueSection> yieldSections) {
        loadYield(yieldSections, true);

        loadYieldConsumption(yieldSections, true);
    }

    @SuppressLint("CheckResult")
    private void loadYieldConsumption(List<ACATRevenueSection> sections, boolean estimated) {
        ACATRevenueSection section = sections.get(0);
        yieldConsumptionLocal.get(section.yieldConsumptionID == null ? "-" : section.yieldConsumptionID)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (view == null) return;

                    if (data.empty()) {
                        yieldConsumption = null;
                    } else {
                        yieldConsumption = data.get();

                        view.setOwnCons(estimated ? yieldConsumption.estimatedOwnCons : 0, estimated);
                        view.setSeedRes(estimated ? yieldConsumption.estimatedSeedReserve : 0, estimated);
                        view.setForMarket(estimated ? yieldConsumption.estimatedForMarket : 0, estimated);
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void loadYield(List<ACATRevenueSection> sections, boolean estimated) {
        acatItemValueList.clear();
        acatItemLocal.get(sections.get(0).yieldID)
                .flatMap(data -> {
                    acatItem = data.get();
                    return acatItemValueLocal.getByType(
                            acatItem._id, estimated ?
                                    ACATItemValueHelper.ESTIMATED_VALUE_TYPE :
                                    ACATItemValueHelper.ACTUAL_VALUE_TYPE
                    );
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    acatItemValue = data.get();
                    view.setProbableYield(acatItemValue.totalPrice, estimated);
                });

        acatItemLocal.get(sections.get(1).yieldID)
                .flatMap(data -> {
                    acatItem = data.get();
                    return acatItemValueLocal.getByType(
                            acatItem._id, estimated ?
                                    ACATItemValueHelper.ESTIMATED_VALUE_TYPE :
                                    ACATItemValueHelper.ACTUAL_VALUE_TYPE
                    );
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    acatItemValue = data.get();
                    view.setMaximumYield(acatItemValue.totalPrice, estimated);
                });

        acatItemLocal.get(sections.get(2).yieldID)
                .flatMap(data -> {
                    acatItem = data.get();
                    return acatItemValueLocal.getByType(
                            acatItem._id, estimated ?
                                    ACATItemValueHelper.ESTIMATED_VALUE_TYPE :
                                    ACATItemValueHelper.ACTUAL_VALUE_TYPE
                    );
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    acatItemValue = data.get();
                    view.setMinimumYield(acatItemValue.totalPrice, estimated);
                });


    }

    private void loadData() {
        final String inputEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(INPUT, acatCostSections));
        final String labourCostEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(LABOUR_COST, acatCostSections));
        final String otherCostEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(OTHER_COST, acatCostSections));
        final String totalEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(INPUTS_AND_ACTIVITY_COST, acatCostSections));
        final String actual = String.valueOf(0.0);

        view.setInputs(inputEstimated, actual);
        view.setLabourCosts(labourCostEstimated, actual);
        view.setOtherCosts(otherCostEstimated, actual);
        view.setTotal(totalEstimated, actual);
    }

    @Override
    public void attachView(CropSummaryListContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public CropSummaryListContract.View getView() {
        return view;
    }

    @Override
    public void onBindItemView(@NonNull CropSummaryListContract.CashFlowItemView holder, int position) {
        final SummaryCashFlowItem item = cashFlowItems.get(position);
        holder.setName(item.name);
        holder.setValue(item.value);
    }

    @Override
    public int getItemCount() {
        return cashFlowItems.size();
    }
}
