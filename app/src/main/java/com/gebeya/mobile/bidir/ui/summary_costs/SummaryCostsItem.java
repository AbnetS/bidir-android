package com.gebeya.mobile.bidir.ui.summary_costs;

import android.support.annotation.NonNull;

/**
 * Simple class to represent a single item on the summary costs list.
 */
public class SummaryCostsItem {

    public final String unit;
    public final String quantity;
    public final String unitPrice;
    public final String totalPrice;

    public SummaryCostsItem(@NonNull String unit, @NonNull String quantity, @NonNull String unitPrice, @NonNull String totalPrice) {
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
}