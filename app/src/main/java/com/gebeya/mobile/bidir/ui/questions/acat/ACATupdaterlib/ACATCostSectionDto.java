package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;

/**
 * Created by abuti on 6/9/2018.
 */

public class ACATCostSectionDto {
    public String acatApplicationId;
    public ACATCostSection section;
    public CashFlow estimatedCashFlow;
    public CashFlow actualCashFlow;


    public ACATCostSectionDto(){
        estimatedCashFlow = initializeEstimatedCashFlow();
        actualCashFlow = initializeActualCashFlow();
    }
    private CashFlow initializeEstimatedCashFlow() {
        CashFlow cashFlow = new CashFlow();

        cashFlow.referenceId = "";
        cashFlow.type = CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        cashFlow.classification = CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE;

        cashFlow.january = 0;
        cashFlow.february = 0;
        cashFlow.march = 0;
        cashFlow.april = 0;
        cashFlow.may = 0;
        cashFlow.june = 0;
        cashFlow.july = 0;
        cashFlow.august = 0;
        cashFlow.september = 0;
        cashFlow.october = 0;
        cashFlow.november = 0;
        cashFlow.december = 0;

        return cashFlow;

    }

    private CashFlow initializeActualCashFlow() {
        CashFlow cashFlow = new CashFlow();

        cashFlow.referenceId = "";
        cashFlow.type = CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
        cashFlow.classification = CashFlowHelper.CUMULATIVE_SECTION_CASH_FLOW_TYPE;

        cashFlow.january = 0;
        cashFlow.february = 0;
        cashFlow.march = 0;
        cashFlow.april = 0;
        cashFlow.may = 0;
        cashFlow.june = 0;
        cashFlow.july = 0;
        cashFlow.august = 0;
        cashFlow.september = 0;
        cashFlow.october = 0;
        cashFlow.november = 0;
        cashFlow.december = 0;

        return cashFlow;

    }
}
