package com.gebeya.mobile.bidir.ui.questions.acat.ACATupdaterlib;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.cashflow.CashFlowHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abuti on 6/8/2018.
 */

public class ACATItemDto {

    //Basic Item Info
    public String acatItemId;
    public String item;
    public String unit;
    public String remark;

    //Estimated Values
    public double estimatedValue;
    public double estimatedUnitPrice;
    public double estimatedTotalPrice;
    public CashFlow estimatedCashFlow;

    //Actual Values
    public double actualValue;
    public double actualUnitPrice;
    public double actualTotalPrice;
    public CashFlow actualCashFlow;

    public String firstExpenseMonth;

    //References
    public String sectionId;
    public String costListId;
    public String groupListId;
    public String cropACATId;
    public String acatApplicationId;

    public String variety;
    public List<String> seedSource;

    public boolean other;
    public boolean pendingCreation;
    //Dates
    public DateTime createdAt;
    public DateTime updatedAt;

    public ACATItemDto(){
        acatItemId = "";
        item = "";
        unit = "";
        remark = "";

        estimatedValue = 0;
        estimatedUnitPrice = 0;
        estimatedTotalPrice = 0;

        actualValue = 0;
        actualUnitPrice = 0;
        actualTotalPrice = 0;

        firstExpenseMonth = "";

        sectionId = "";
        costListId = "";
        groupListId = "";

        variety = "";
        seedSource = new ArrayList<>();

        other = false;
        pendingCreation = false;

        createdAt = new DateTime();
        updatedAt = new DateTime();

        estimatedCashFlow = initializeEstimatedCashFlow();
        actualCashFlow = initializeActualCashFlow();


    }

    private CashFlow initializeEstimatedCashFlow(){
        CashFlow cashFlow = new CashFlow();

        cashFlow.referenceId = "";
        cashFlow.type = CashFlowHelper.ESTIMATED_CASH_FLOW_TYPE;
        cashFlow.classification = CashFlowHelper.ITEM_CASH_FLOW;

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

    private CashFlow initializeActualCashFlow(){
        CashFlow cashFlow = new CashFlow();

        cashFlow.referenceId = "";
        cashFlow.type = CashFlowHelper.ACTUAL_CASH_FLOW_TYPE;
        cashFlow.classification = CashFlowHelper.ITEM_CASH_FLOW;

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
