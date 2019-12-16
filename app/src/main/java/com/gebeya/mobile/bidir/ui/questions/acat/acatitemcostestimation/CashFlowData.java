package com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation;

/**
 * Created by Samuel K. on 6/13/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CashFlowData {

    private static CashFlowData cashFlowData;

    public static CashFlowData getInstance() {
        if (cashFlowData == null) {
            cashFlowData = new CashFlowData();
        }
        return cashFlowData;
    }

    public String referenceId;
    public String type;
    public String classification;

    public double january;
    public double february;
    public double march;
    public double april;
    public double may;
    public double june;
    public double july;
    public double august;
    public double september;
    public double october;
    public double november;
    public double december;

    private CashFlowData() {
        reset();
    }

    public void reset() {
        referenceId = "";
        january = 0;
        february = 0;
        march = 0;
        april = 0;
        may = 0;
        june = 0;
        july = 0;
        august = 0;
        september = 0;
        october = 0;
        november = 0;
        december = 0;
    }
}
