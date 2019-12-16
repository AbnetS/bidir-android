package com.gebeya.mobile.bidir.data.acatitem;

import com.gebeya.mobile.bidir.data.cashflow.CashFlow;

/**
 * Simple model to hold cost list item data for updating a cost list item through the api.
 */

public class ACATItemDto {
    //TODO: Replace this class with a request class that will have a ACATItemResponse attribute inside it.

    //Basic Item Info
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

    //References
    public String sectionId;
    public String costListId;
    public String groupListId;

}
