package com.gebeya.mobile.bidir.data.acatitem;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;

/**
 * Created by abuti on 7/15/2018.
 */

public class ACATItemRequest {
    public String acatApplicationId;
    public ACATItem acatItem;
    public ACATItemValue estimatedAcatItemValue;
    public ACATItemValue actualAcatItemValue;
    public CashFlow estimatedCashFlow;
    public CashFlow actualCashFlow;
}
