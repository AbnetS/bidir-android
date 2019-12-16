package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;

/**
 * Class to represent a cash flow list item.
 */
public class CashFlowItem {

    public String month;
    public String value;

    public CashFlowItem(@NonNull String month, @NonNull String value) {
        this.month = month;
        this.value = value;
    }
}