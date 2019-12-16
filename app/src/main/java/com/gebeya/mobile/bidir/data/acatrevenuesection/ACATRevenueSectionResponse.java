package com.gebeya.mobile.bidir.data.acatrevenuesection;

import com.gebeya.mobile.bidir.data.acatitem.ACATItemResponse;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.yieldconsumption.YieldConsumptionResponse;

import java.util.List;

/**
 * Created by abuti on 5/15/2018.
 */

public class ACATRevenueSectionResponse {
    public List<ACATRevenueSection> sections;

    public List<CashFlow> estimatedCashFlows;
    public List<CashFlow> actualCashFlows;

    public List<ACATItemResponse> yields;

    //made it an array for the purpose of expandability though we know that there is only one yieldConsumption
    public List<YieldConsumptionResponse> yieldConsumptions;


}
