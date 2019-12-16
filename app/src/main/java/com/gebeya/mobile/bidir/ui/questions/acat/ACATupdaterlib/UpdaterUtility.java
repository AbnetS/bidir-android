package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import android.util.Log;

import com.gebeya.mobile.bidir.data.acatapplication.ACATApplication;
import com.gebeya.mobile.bidir.data.acatapplication.local.ACATApplicationLocalSource;
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.acatcostsection.local.ACATCostSectionLocalSource;
import com.gebeya.mobile.bidir.data.acatitem.ACATItem;
import com.gebeya.mobile.bidir.data.acatitem.local.ACATItemLocalSource;
import com.gebeya.mobile.bidir.data.acatitemvalue.local.ACATItemValueLocalSource;
import com.gebeya.mobile.bidir.data.acatrevenuesection.ACATRevenueSection;
import com.gebeya.mobile.bidir.data.acatrevenuesection.local.ACATRevenueSectionLocalSource;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;
import com.gebeya.mobile.bidir.data.cashflow.local.CashFlowLocalSource;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.data.cropacat.local.CropACATLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * Created by abuti on 6/22/2018.
 */

public class UpdaterUtility {
    @Inject ACATCostSectionLocalSource acatCostSectionLocalSource;
    @Inject ACATRevenueSectionLocalSource acatRevenueSectionLocalSource;
    @Inject CashFlowLocalSource cashFlowLocalSource;
    @Inject CropACATLocalSource cropACATLocalSource;
    @Inject ACATApplicationLocalSource acatApplicationLocalSource;
    @Inject ACATItemValueLocalSource acatItemValueLocalSource;
    @Inject ACATItemLocalSource acatItemLocalSource;

    public UpdaterUtility(){
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    public CashFlow computeSectionNetCashFlow(CashFlow itemCashFlow, CashFlow sectionCashFlow){
        sectionCashFlow.classification = CashFlowHelper.NET_SECTION_CASH_FLOW_TYPE;

        //Add Up the item cash flow values of each month to the section CashFlow
        sectionCashFlow.january +=  itemCashFlow.january;
        sectionCashFlow.february +=  itemCashFlow.february;
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

    public CashFlow computeSectionCumulativeCashFlow(CashFlow itemCashFlow, CashFlow cumulativeSectionCashFlow, CashFlow oldItemCashFlow,
                                                     String firstExpenseMonth){

        //Compute the cumulative cash flow

        CashFlow response =  new CashFlow();
        response.referenceId = cumulativeSectionCashFlow.referenceId;
        response.type = cumulativeSectionCashFlow.type;
        response.classification = CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE;

        List<Double> cumulativeCashFlowValues = returnCashFlowValues (cumulativeSectionCashFlow);
        List<Double> itemCashFlowValues = returnCashFlowValues (itemCashFlow);
        List<Double> oldItemCashFlowValues = returnCashFlowValues(oldItemCashFlow);

        return computeCumulativeCashFlow(cumulativeCashFlowValues, itemCashFlowValues, oldItemCashFlowValues, firstExpenseMonth);


    }



    public CashFlow computeCumulativeCashFlow(List<Double> cumulativeCashFlowValues, List<Double> itemCashFlowValues,
                                              List<Double> oldItemCashFlowValues, String firstExpenseMonth){

        List<Double> responseCashFlowValues = new ArrayList<>(12);

        for (int i=0;i < 12;i++){
            Double d = 0.0;
            responseCashFlowValues.add(d);
        }

        int indexFirstMonth = returnMonthPosition(firstExpenseMonth);
        int index = indexFirstMonth;
        boolean stop = false;
        int nextIndex;
        double newValue;


        oldItemCashFlowValues = computeCumulativeCashFlow(oldItemCashFlowValues, firstExpenseMonth);

        //Compute the first expense month cumulative cash flow
        newValue = cumulativeCashFlowValues.get(indexFirstMonth) - oldItemCashFlowValues.get(indexFirstMonth);
        cumulativeCashFlowValues.set(indexFirstMonth, newValue);
        newValue = newValue + itemCashFlowValues.get(indexFirstMonth);
        //newValue = cumulativeCashFlowValues.getById(indexFirstMonth) +  itemCashFlowValues.getById(indexFirstMonth);
        responseCashFlowValues.set(indexFirstMonth,newValue);

        if (index == 0){
            for (int k = 0; k < 11; k++){
                cumulativeCashFlowValues.set(k + 1,cumulativeCashFlowValues.get(k+1) - oldItemCashFlowValues.get(k+1));

                newValue = (cumulativeCashFlowValues.get(k+1) - cumulativeCashFlowValues.get(k))
                        + itemCashFlowValues.get(k+1) + responseCashFlowValues.get(k);

                responseCashFlowValues.set(k+1, newValue);
            }

        }
        else {
            while (!stop) {
                if (index == 11) {
                    nextIndex = 0;
                } else {
                    nextIndex = index + 1;
                }


                cumulativeCashFlowValues.set(nextIndex, cumulativeCashFlowValues.get(nextIndex) - oldItemCashFlowValues.get(nextIndex));

                newValue = (cumulativeCashFlowValues.get(nextIndex) - cumulativeCashFlowValues.get(index))
                        + itemCashFlowValues.get(nextIndex) + responseCashFlowValues.get(index);

                responseCashFlowValues.set(nextIndex, newValue);

                if (index == 11) {
                    index = 0;
                } else index = index + 1;

                if ((nextIndex + 1) == indexFirstMonth)
                    stop = true;
            }
        }

        CashFlow responseCashFlow = returnCashFlowFromValues(responseCashFlowValues);
        return responseCashFlow;
    }

    public CashFlow computeCumulativeCropACATCashFlow(List<Double> revenueCashFlowValues, List<Double> costCashFlowValues,
                                                      String firstExpenseMonth){

        List<Double> responseCashFlowValues = new ArrayList<>(12);

        for (int i=0;i < 12;i++){
            Double d = 0.0;
            responseCashFlowValues.add(d);
        }

        int indexFirstMonth = returnMonthPosition(firstExpenseMonth);
        int index = indexFirstMonth;
        boolean stop = false;
        int nextIndex;
        double newValue;

        //Compute the first expense month cumulative cash flow
        newValue = revenueCashFlowValues.get(indexFirstMonth) -  costCashFlowValues.get(indexFirstMonth);
        responseCashFlowValues.set(indexFirstMonth,newValue);

        if (index == 0){
            for (int k = 0; k < 11; k++){
                newValue = (revenueCashFlowValues.get(k+1) - costCashFlowValues.get(k+1));
                responseCashFlowValues.set(k+1, newValue);
            }

        }
        else {
            while (!stop) {
                if (index == 11) {
                    nextIndex = 0;
                } else {
                    nextIndex = index + 1;
                }


                newValue = (revenueCashFlowValues.get(nextIndex) - costCashFlowValues.get(nextIndex));

//            newValue = (revenueCashFlowValues.getById(nextIndex) - costCashFlowValues.getById(nextIndex))
//                    + revenueCashFlowValues.getById(nextIndex);

                responseCashFlowValues.set(nextIndex, newValue);

                if (index == 11) {
                    index = 0;
                } else index = index + 1;

                if ((nextIndex + 1) == indexFirstMonth)
                    stop = true;
            }
        }

        CashFlow responseCashFlow = returnCashFlowFromValues(responseCashFlowValues);
        return responseCashFlow;

    }



    public int returnMonthPosition (String month){
        switch (month){
            case "January": return 0;
            case "February": return 1;
            case "March": return 2;
            case "April": return 3;
            case "May": return 4;
            case "June": return 5;
            case "July": return 6;
            case "August": return 7;
            case "September": return 8;
            case "October": return 9;
            case "November": return 10;
            case "December": return 11;
            default: return -1;
        }
    }

    public List<Double> computeCumulativeCashFlow (List<Double> cashFlow, String firstExpenseMonth){
        List<Double> response = new ArrayList<>(12);

        for (int i=0;i < 12;i++){
            Double d = 0.0;
            response.add(d);
        }

        int indexFirstMonth = returnMonthPosition(firstExpenseMonth);
        int index = indexFirstMonth;
        response.set(index, cashFlow.get(index));

        boolean stop = false;
        int nextIndex;
        double newValue;

        if (index == 0){
            for (int k = 0; k < 11; k++){
                newValue = response.get(k) + cashFlow.get(k+1);
                response.set(k + 1, newValue);
            }
            return response;
        }

        while (!stop) {
            if (index == 11) {
                nextIndex = 0;
            } else {
                nextIndex = index + 1;
            }

            newValue = response.get(index) + cashFlow.get(nextIndex);
            response.set(nextIndex,newValue);

            if(index == 11){
                index = 0;
            }
            else index = index + 1;

            if ((nextIndex + 1) == indexFirstMonth)
                stop = true;
        }

        return response;
    }

    public List<Double> returnCashFlowValues (CashFlow cashFlow){
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
    public CashFlow returnCashFlowFromValues (List<Double> cashFlowValues){
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

    public CashFlow computeSumOfCashFlows(List<CashFlow> cashFlows){
        List<Double> sumCashFlowValues = new ArrayList<>();
        List<Double> cashFlowValues = new ArrayList<>();

        CashFlow sumCashFlow = new CashFlow();

        for (int i = 0; i < cashFlows.size(); i++){
            sumCashFlow.january += cashFlows.get(i).january;
            sumCashFlow.february += cashFlows.get(i).february;
            sumCashFlow.march += cashFlows.get(i).march;
            sumCashFlow.april += cashFlows.get(i).april;
            sumCashFlow.may += cashFlows.get(i).may;
            sumCashFlow.june += cashFlows.get(i).june;
            sumCashFlow.july += cashFlows.get(i).july;
            sumCashFlow.august += cashFlows.get(i).august;
            sumCashFlow.september += cashFlows.get(i).september;
            sumCashFlow.october += cashFlows.get(i).october;
            sumCashFlow.november += cashFlows.get(i).november;
            sumCashFlow.december += cashFlows.get(i).december;
        }

        return sumCashFlow;

    }

    public Observable<Double> returnItemTotal(String acatItemId, String type){
        return acatItemValueLocalSource.getByType(acatItemId, type)
                .flatMap(acatItemValue -> {
                    Log.e("ACATItem", acatItemId);
                    return Observable.just(acatItemValue.get().totalPrice);
                });
    }
    public Observable<ACATCostSection> returnCostSection(String sectionId){
        return acatCostSectionLocalSource.get(sectionId)
                .flatMap(section -> {
                    return Observable.just(section.get());
                });
    }

    public Observable<ACATRevenueSection> returnRevenueSection(String sectionId){
        return acatRevenueSectionLocalSource.get(sectionId)
                .flatMap(section -> {
                    return Observable.just(section.get());
                });
    }

    public Observable<List<ACATItem>> returnACATItems(String sectionId){
        return acatItemLocalSource.getItemBySectionId(sectionId)
                .flatMap(acatItems -> {
                    return Observable.just(acatItems);
                });
    }

    public Observable<List<ACATCostSection>> returnSubSections(String sectionId){
        return acatCostSectionLocalSource.getByParent(sectionId)
                .flatMap(sections -> {
                    return Observable.just(sections);
                });
    }


    public Observable<CashFlow> returnCashFlow(String id, String type, String classification){
        return cashFlowLocalSource.get(id, type,classification)
                .flatMap(cashFlow -> {
                    return Observable.just(cashFlow.get(0));
                });
    }
    public Observable<CashFlow> returnSectionCashFlow(String sectionId, String type){
        return cashFlowLocalSource.get(sectionId, type,
                CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE)
                .flatMap(cashFlow -> {
                    return Observable.just(cashFlow.get(0));
                });
    }

    public Observable<CashFlow> returnCropACATCashFlow(String cropACATId, String type){
        return cashFlowLocalSource.get(cropACATId, type,
                CashFlowHelper.CUMULATIVE_CROPACAT_CASH_FLOW_TYPE)
                .flatMap(cashFlow -> {
                    return Observable.just(cashFlow.get(0));
                });
    }

    public Observable<CropACAT> returnCropACAT(String cropACATId){
        return cropACATLocalSource.get(cropACATId)
                .flatMap(cropACATData -> {
                    return Observable.just(cropACATData.get());
                });
    }

    public Observable<List<CropACAT>> returnCropACATByApp(String acatApplicationId){
        return cropACATLocalSource.getCropACATByApp(acatApplicationId)
                .flatMap(cropACATs -> {
                    return Observable.just(cropACATs);
                });
    }

    public Observable<ACATApplication> returnACATApplication(String acatApplicationId){
        return acatApplicationLocalSource.get(acatApplicationId)
                .flatMap(acatApplicationData -> {
                    return Observable.just(acatApplicationData.get());
                });
    }

}
