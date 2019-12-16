package com.gebeya.mobile.bidir.data.acatitem;

import com.gebeya.mobile.bidir.data.acatitemvalue.ACATItemValue;
import com.gebeya.mobile.bidir.data.cashflow.CashFlow;

/**
 * Created by abuti on 5/13/2018.
 */

public class ACATItemResponse {
    public ACATItem acatItem;
    public CashFlow estimatedCashFlow;
    public CashFlow actualCashFlow;
    public ACATItemValue estimatedAcatItemValue;
    public ACATItemValue actualAcatItemValue;
}
