package com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSectionResponse;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostItemUpdater;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostSectionDto;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATCostSectionUpdater;
import com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib.ACATItemDto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

//import java.util.Observable;

/**
 * Created by abuti on 6/10/2018.
 */

public class ACATUpdater {

    @Inject ACATCostItemUpdater acatCostItemUpdater;

    @Inject ACATCostSectionUpdater acatCostSectionUpdater;

    @Inject
    ACATCostSectionLocalSource acatCostSectionLocalSource;

    @Inject
    CashFlowLocalSource cashFlowLocalSource;

    @Inject
    SchedulersProvider schedulers;

    public ACATUpdater() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    public void updateEstimatedValueForACATItem(ACATItemDto acatItemDto) {
        ACATItemResponse acatItemResponse = acatCostItemUpdater.updateEstimatedValuesForACATItem(acatItemDto);

        //Update the sections...
        String sectionToBeUpdatedId = acatItemDto.sectionId;

        while (sectionToBeUpdatedId != null) {
            final ACATCostSectionDto acatCostSectionDto = new ACATCostSectionDto();

            returnSection(sectionToBeUpdatedId)
                    .subscribe(response -> {
                        acatCostSectionDto.section = response;
                    });
            returnSectionCashFlow(acatCostSectionDto.section, CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE)
                    .subscribe(response -> {
                        acatCostSectionDto.estimatedCashFlow = response;
                    });

            ACATCostSectionResponse updated = aggregateEstimatedValuesToSections(acatItemResponse,
                    acatCostSectionDto.section, acatCostSectionDto.estimatedCashFlow, acatItemDto.firstExpenseMonth);

            acatCostSectionDto.section = updated.sections.get(0);
            acatCostSectionDto.estimatedCashFlow = updated.estimatedCashFlows.get(0);

            acatCostSectionUpdater.updateEstimatedValuesForACATCostSection(acatCostSectionDto);

            sectionToBeUpdatedId = acatCostSectionDto.section.parentSectionID;
        }

        //Update the cropACAT and ACATApplication...

    }

    private Observable<ACATCostSection> returnSection(String sectionId) {
        return acatCostSectionLocalSource.get(sectionId)
                .flatMap(section -> {
                    return Observable.just(section.get());
                });

    }


    private Observable<CashFlow> returnSectionCashFlow(ACATCostSection section, String type) {
        return cashFlowLocalSource.get(section._id, type,
                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                .flatMap(cashFlow -> {
                    return Observable.just(cashFlow.get(0));
                });
    }

    private ACATCostSectionResponse aggregateEstimatedValuesToSections(ACATItemResponse acatItemResponse,
                                                                       ACATCostSection section, CashFlow sectionCashFlow,
                                                                       String firstExpenseMonth) {

        final ACATCostSectionResponse acatCostSectionResponse = new ACATCostSectionResponse();
        acatCostSectionResponse.sections = new ArrayList<>();
        acatCostSectionResponse.estimatedCashFlows = new ArrayList<>();

        //Update section values to be affected
        section.estimatedSubTotal += acatItemResponse.estimatedAcatItemValue.totalPrice;

        CashFlow estimatedSectionCashFlow = computeSectionCumulativeCashFlow(acatItemResponse.estimatedCashFlow,
                sectionCashFlow, firstExpenseMonth);

        estimatedSectionCashFlow.referenceId = sectionCashFlow.referenceId;
        estimatedSectionCashFlow.type = sectionCashFlow.type;
        estimatedSectionCashFlow.classification = sectionCashFlow.classification;

        acatCostSectionResponse.sections.add(section);
        acatCostSectionResponse.estimatedCashFlows.add(estimatedSectionCashFlow);

        return acatCostSectionResponse;
    }


    private CashFlow computeSectionNetCashFlow(CashFlow itemCashFlow, CashFlow sectionCashFlow) {
        sectionCashFlow.classification = CashFlowHelper.NET_SECTION_CASH_FLOW_TYPE;

        //Add Up the item cash flow values of each month to the section CashFlow
        sectionCashFlow.january += itemCashFlow.january;
        sectionCashFlow.february += itemCashFlow.february;
        sectionCashFlow.march += itemCashFlow.march;
        sectionCashFlow.april += itemCashFlow.april;
        sectionCashFlow.may += itemCashFlow.may;
        sectionCashFlow.june += itemCashFlow.june;
        sectionCashFlow.july += itemCashFlow.july;
        sectionCashFlow.august += itemCashFlow.august;
        sectionCashFlow.september += itemCashFlow.september;
        sectionCashFlow.october += itemCashFlow.october;
        sectionCashFlow.november += itemCashFlow.november;
        sectionCashFlow.december += itemCashFlow.december;

        return sectionCashFlow;
    }

    private CashFlow computeSectionCumulativeCashFlow(CashFlow itemCashFlow, CashFlow cumulativeSectionCashFlow,
                                                      String firstExpenseMonth) {

        //Compute the cumulative cash flow

        CashFlow response = new CashFlow();
        response.referenceId = cumulativeSectionCashFlow.referenceId;
        response.type = cumulativeSectionCashFlow.type;
        response.classification = CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE;

        List<Double> cumulativeCashFlowValues = returnCashFlowValues(cumulativeSectionCashFlow);
        List<Double> itemCashFlowValues = returnCashFlowValues(itemCashFlow);

        return computeCumulativeCashFlow(cumulativeCashFlowValues, itemCashFlowValues, firstExpenseMonth);

    }

    private List<Double> returnCashFlowValues(CashFlow cashFlow) {
        List<Double> cashFlowValues = new ArrayList<>();

        cashFlowValues.add(cashFlow.january);
        cashFlowValues.add(cashFlow.february);
        cashFlowValues.add(cashFlow.march);
        cashFlowValues.add(cashFlow.april);
        cashFlowValues.add(cashFlow.may);
        cashFlowValues.add(cashFlow.june);
        cashFlowValues.add(cashFlow.july);
        cashFlowValues.add(cashFlow.august);
        cashFlowValues.add(cashFlow.september);
        cashFlowValues.add(cashFlow.october);
        cashFlowValues.add(cashFlow.november);
        cashFlowValues.add(cashFlow.december);

        return cashFlowValues;
    }

    private CashFlow returnCashFlowFromValues(List<Double> cashFlowValues) {
        CashFlow cashFlow = new CashFlow();

        cashFlow.january = cashFlowValues.get(0);
        cashFlow.february = cashFlowValues.get(1);
        cashFlow.march = cashFlowValues.get(2);
        cashFlow.april = cashFlowValues.get(3);
        cashFlow.may = cashFlowValues.get(4);
        cashFlow.june = cashFlowValues.get(5);
        cashFlow.july = cashFlowValues.get(6);
        cashFlow.august = cashFlowValues.get(7);
        cashFlow.september = cashFlowValues.get(8);
        cashFlow.october = cashFlowValues.get(9);
        cashFlow.november = cashFlowValues.get(10);
        cashFlow.december = cashFlowValues.get(11);

        return cashFlow;
    }

    private int returnMonthPosition(String month) {
        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return -1;
        }
    }


    private CashFlow computeCumulativeCashFlow(List<Double> cumulativeCashFlowValues, List<Double> itemCashFlowValues,
                                               String firstExpenseMonth) {

        List<Double> responseCashFlowValues = new ArrayList<>(12);

        for (int i = 0; i < 12; i++) {
            Double d = 0.0;
            responseCashFlowValues.add(d);
        }

        int indexFirstMonth = returnMonthPosition(firstExpenseMonth);
        int index = indexFirstMonth;
        boolean stop = false;
        int nextIndex;
        double newValue;

        //Compute the first expense month cumulative cash flow
        newValue = cumulativeCashFlowValues.get(indexFirstMonth) + itemCashFlowValues.get(indexFirstMonth);
        responseCashFlowValues.set(indexFirstMonth, newValue);

        while (!stop) {
            if (index == 11) {
                nextIndex = 0;
            } else {
                nextIndex = index + 1;
            }


            newValue = (cumulativeCashFlowValues.get(nextIndex) - cumulativeCashFlowValues.get(index))
                    + itemCashFlowValues.get(nextIndex) + responseCashFlowValues.get(index);

            responseCashFlowValues.set(nextIndex, newValue);

            if (index == 11) {
                index = 0;
            } else index = index + 1;

            if ((nextIndex + 1) == indexFirstMonth)
                stop = true;
        }

        CashFlow responseCashFlow = returnCashFlowFromValues(responseCashFlowValues);
        return responseCashFlow;
    }


}
