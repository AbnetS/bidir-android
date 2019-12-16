package com.gebeya.mobile.bidir.ui.summary_costs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationUtlity;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowItem;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;
import com.gebeya.mobile.bidir.ui.summary_inputs.SummaryInputsContract;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link SummaryCostsContract.Presenter} interface
 */
public class SummaryCostsPresenter implements SummaryCostsContract.Presenter {

    private SummaryCostsContract.View view;

    private final SummaryMenuItem.Label label;
    private final String clientId;
    private final String cropACATId;

    private CropACAT cropACAT;
    private ACATCostSection acatCostSection;
    private final List<ACATCostSection> sections;
    private final List<ACATCostSection> acatCostSections;
    private final List<CashFlow> cashFlowList;
    private final List<SummaryCashFlowItem> cashFlowItems;
    private final List<ACATItem> acatItemList;
    private final List<ACATItemValue> acatItemValueList;

    private String sectionId;


    @Inject CashFlowLocalSource cashFlowLocal;
    @Inject CropACATLocalSource cropACATLocal;
    @Inject ACATCostSectionLocalSource acatCostSectionLocal;
    @Inject SchedulersProvider schedulers;
    @Inject ACATItemLocalSource acatitemLocal;
    @Inject ACATItemValueLocalSource acatItemValueLocal;

    public SummaryCostsPresenter(@NonNull SummaryMenuItem.Label label,
                                 @NonNull String clientId,
                                 @NonNull String cropACATId) {
        this.label = label;
        this.clientId = clientId;
        this.cropACATId = cropACATId;

        sections = new ArrayList<>();
        acatCostSections = new ArrayList<>();
        cashFlowItems = new ArrayList<>();
        cashFlowList = new ArrayList<>();
        acatItemList = new ArrayList<>();
        acatItemValueList = new ArrayList<>();

        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
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

                    sectionId = ACATCostEstimationUtlity.getACATItemSectionId(view.getTitle(label), acatCostSections);
                    return acatitemLocal.getItemBySectionId(sectionId);
//    return Observable.just(true);
                })
                .flatMap(acatItems -> {
                    acatItemList.clear();
                    acatItemList.addAll(acatItems);
                    loadCashFlowData();
                    return acatItemValueLocal.getAll();
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(acatItemValues -> {
                            acatItemValueList.clear();
                            acatItemValueList.addAll(acatItemValues);
                            view.loadEstimatedData();
                            view.loadActualData();
                            view.setTitle(label);
                        },
                        Throwable::printStackTrace
                );
    }

    private ACATItemValue getEstimatedACATItemValue(String acatItemId, List<ACATItemValue> acatItemValues) {
        for (ACATItemValue value : acatItemValues) {
            if (acatItemId.equalsIgnoreCase(value.acatItemId) && value.type.equalsIgnoreCase("ESTIMATED"))
                return value;
        }
        return null;
    }

    private ACATItemValue getActualACATItemValue(String acatItemId, List<ACATItemValue> acatItemValues) {
        for (ACATItemValue value : acatItemValues) {
            if (acatItemId.equalsIgnoreCase(value.acatItemId) && value.type.equalsIgnoreCase("ACTUAL")) {
                return value;
            }
        }
        return null;
    }

    @SuppressLint("CheckResult")
    private void loadCashFlowData() {
        cashFlowLocal.getAll()
                .flatMap(list -> {
                    cashFlowList.clear();
                    cashFlowList.addAll(list);
                    final CashFlow cashFlow = ACATCostEstimationUtlity.getCostEstimationItemCashFlow(
                            view.getTitle(label),
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
    public void onBindEstimatedItemView(@NonNull SummaryCostsContract.SummaryCostsItemView holder, int position) {
        final ACATItem item = acatItemList.get(position);
        holder.setName(item.item);
        holder.setUnit(item.unit);
        if (getEstimatedACATItemValue(item._id, acatItemValueList) != null) {
            holder.setQuantity(String.valueOf(getEstimatedACATItemValue(item._id, acatItemValueList).value));
            holder.setUnitPrice(String.valueOf(getEstimatedACATItemValue(item._id, acatItemValueList).unitPrice));
            holder.setTotalPrice(String.valueOf(getEstimatedACATItemValue(item._id, acatItemValueList).totalPrice));
        } else {
            holder.setQuantity("0");
            holder.setUnitPrice("0");
            holder.setTotalPrice("0");
        }

    }

    @Override
    public int getEstimatedCount() {
        return acatItemList.size();
    }

    @Override
    public void onBindActualItemView(@NonNull SummaryCostsContract.SummaryCostsItemView holder, int position) {
        final ACATItem item = acatItemList.get(position);
        holder.setName(item.item);
        holder.setUnit(item.unit);
        if (getActualACATItemValue(item._id, acatItemValueList) != null) {
            holder.setQuantity(String.valueOf(getActualACATItemValue(item._id, acatItemValueList).value));
            holder.setUnitPrice(String.valueOf(getActualACATItemValue(item._id, acatItemValueList).unitPrice));
            holder.setTotalPrice(String.valueOf(getActualACATItemValue(item._id, acatItemValueList).totalPrice));
        } else {
            holder.setQuantity("0");
            holder.setUnitPrice("0");
            holder.setTotalPrice("0");
        }
    }

    @Override
    public int getActualCount() {
        return acatItemList.size();
    }

    @Override
    public void onMonitorButtonClicked(@NonNull Bundle bundle) {
        view.openFragment(bundle, clientId, cropACATId, label, sections, cropACAT, sectionId);
    }

    @Override
    public void attachView(SummaryCostsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public SummaryCostsContract.View getView() {
        return view;
    }



}
