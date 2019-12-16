package com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation;

/**
 * Created by Samuel K. on 6/13/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATItemData {

    private static ACATItemData acatItemData;

    public static ACATItemData getInstance() {
        if (acatItemData == null) {
            acatItemData = new ACATItemData();
        }
        return acatItemData;
    }

    public String acatItemId;
    public String item;
    public String unit; // I guess this shouldn't be here since it is fetched from the API and can't be altered.
    public String remark;

    //Estimated Values
    public double estimatedValue;
    public double estimatedUnitPrice;
    public double estimatedTotalPrice;
    public CashFlowData estimatedCashFlow;

    //Actual Values
    public double actualValue;
    public double actualUnitPrice;
    public double actualTotalPrice;
    public CashFlowData actualCashFlow;

    public String firstExpenseMonth;

    //References
    public String sectionId;
    public String costListId;
    public String groupListId;

    private ACATItemData() {
        reset();
    }

    public void reset() {
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

        estimatedCashFlow = null;
        actualCashFlow = null;
    }
}
