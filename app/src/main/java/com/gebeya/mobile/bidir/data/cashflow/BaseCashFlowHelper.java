package com.gebeya.mobile.bidir.data.cashflow;

import android.support.annotation.NonNull;

/**
 * Concrete class for the {@link CashFlowHelper} interface.
 */

public class BaseCashFlowHelper implements CashFlowHelper {


    @Override
    public String getApiMonth(@NonNull String local) {
        switch (local) {
            case API_JANUARY:
                return JANUARY;
            case API_FEBRUARY:
                return FEBRUARY;
            case API_MARCH:
                return MARCH;
            case API_APRIL:
                return APRIL;
            case API_MAY:
                return MAY;
            case API_JUNE:
                return JUNE;
            case API_JULY:
                return JULY;
            case API_AUGUST:
                return AUGUST;
            case API_SEPTEMBER:
                return SEPTEMBER;
            case API_OCTOBER:
                return OCTOBER;
            case API_NOVEMBER:
                return NOVEMBER;
            case API_DECEMBER:
                return DECEMBER;
        }
        return UNKNOWN_MONTH;
    }

    @Override
    public String getLocalMonth(@NonNull String api) {
        switch (api) {
            case JANUARY:
                return API_JANUARY;
            case FEBRUARY:
                return API_FEBRUARY;
            case MARCH:
                return API_MARCH;
            case APRIL:
                return API_APRIL;
            case MAY:
                return API_MAY;
            case JUNE:
                return API_JUNE;
            case JULY:
                return API_JULY;
            case AUGUST:
                return API_AUGUST;
            case SEPTEMBER:
                return API_SEPTEMBER;
            case OCTOBER:
                return API_OCTOBER;
            case NOVEMBER:
                return API_NOVEMBER;
            case DECEMBER:
                return API_DECEMBER;
        }
        return UNKNOWN_MONTH;
    }
}
