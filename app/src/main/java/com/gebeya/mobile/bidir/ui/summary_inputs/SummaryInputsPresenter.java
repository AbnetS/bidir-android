package com.gebeya.mobile.bidir.ui.summary_inputs;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationUtlity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.CostEstimationController;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Presenter implementation for the {@link SummaryInputsContract.Presenter} interface.
 */
public class SummaryInputsPresenter implements SummaryInputsContract.Presenter {

    private static final String INPUTS_AND_ACTIVITY_COST = "Inputs And Activity Costs";
    private static final String INPUT = "INPUT";
    private static final String SEED = "Seed";
    private static final String FERTILIZER = "Fertilizers";
    private static final String CHEMICALS = "Chemicals";

    private SummaryInputsContract.View view;

    private final String clientId;
    private final String cropACATId;

    private CropACAT cropACAT;

    private ACATCostSection acatCostSection;
    private final List<ACATCostSection> sections;
    private final List<ACATCostSection> acatCostSections;
    private final List<CashFlow> cashFlowList;
    private final List<SummaryCashFlowItem> cashFlowItems;

    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATCostSectionLocalSource acatCostSectionLocal;
    @Inject SchedulersProvider schedulers;

    public SummaryInputsPresenter(@NonNull String clientId, @NonNull String cropACATId) {
        this.clientId = clientId;
        this.cropACATId = cropACATId;
        sections = new ArrayList<>();
        acatCostSections = new ArrayList<>();
        cashFlowItems = new ArrayList<>();
        cashFlowList = new ArrayList<>();
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @Override
    public void start() {
        loadCropData();
    }

    @SuppressLint("CheckResult")
    private void loadCropData() {
        cropACATLocal.get(cropACATId)
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

                    loadCashFlowData();

                    return Observable.just(true);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> loadData(),
                        Throwable::printStackTrace
                );
    }

    @SuppressLint("CheckResult")
    private void loadCashFlowData() {
        cashFlowLocal.getAll()
                .flatMap(list -> {
                    cashFlowList.clear();
                    cashFlowList.addAll(list);
                    final CashFlow cashFlow = ACATCostEstimationUtlity.getCostEstimationItemCashFlow(
                            INPUT,
                            acatCostSections,
                            cashFlowList
                    );
                    return Observable.just(cashFlow);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(
                        this::generateCashFlowData,
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

    private void loadData() {
        // TODO: Work on actual values
        final String seedEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(SEED, acatCostSections));
        final String fertilizerEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(FERTILIZER, acatCostSections));
        final String chemicalsEstimated = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(CHEMICALS, acatCostSections));
        final String estimatedTotal = String.valueOf(ACATCostEstimationUtlity.getACATItemEstimatedSubtotal(INPUT, acatCostSections));

        final String seedActual = String.valueOf(ACATCostEstimationUtlity.getACATItemActualSubtotal(SEED, acatCostSections));
        final String fertilizerActual = String.valueOf(ACATCostEstimationUtlity.getACATItemActualSubtotal(FERTILIZER, acatCostSections));
        final String chemicalsActual = String.valueOf(ACATCostEstimationUtlity.getACATItemActualSubtotal(CHEMICALS, acatCostSections));
        final String actualTotal = String.valueOf(ACATCostEstimationUtlity.getACATItemActualSubtotal(INPUT, acatCostSections));

        view.setSeedData(seedEstimated, seedActual);
        view.setFertilizerData(fertilizerEstimated, fertilizerActual);
        view.setChemicalsData(chemicalsEstimated, chemicalsActual);
        view.setSubtotalData(estimatedTotal, actualTotal);
    }

    @Override
    public void onBindItemView(@NonNull SummaryInputsContract.CashFlowItemView holder, int position) {
        final SummaryCashFlowItem item = cashFlowItems.get(position);
        holder.setName(item.name);
        holder.setValue(item.value);
    }

    @Override
    public int getItemCount() {
        return cashFlowItems.size();
    }

    @Override
    public void attachView(SummaryInputsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public SummaryInputsContract.View getView() {
        return view;
    }
}
