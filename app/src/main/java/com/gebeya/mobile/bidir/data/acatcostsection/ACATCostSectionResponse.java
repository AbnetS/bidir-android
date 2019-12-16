package com.gebeya.mobile.bidir.data.acatcostsection;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;
import com.gebeya.mobile.bidir.data.costlist.CostListResponse;

import java.util.List;

/**
 * Created by abuti on 5/12/2018.
 */

public class ACATCostSectionResponse {
    public List<ACATCostSection> sections;

    public List<CashFlow> estimatedCashFlows;
    public List<CashFlow> actualCashFlows;

    public List<CostListResponse> costLists;

}
