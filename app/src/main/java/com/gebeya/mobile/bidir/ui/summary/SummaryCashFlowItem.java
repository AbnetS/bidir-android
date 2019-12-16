package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;

/**
 * Class to represent a single cash flow item with its month name and value.
 */
public class SummaryCashFlowItem {

    public final String name;
    public final String value;

    public SummaryCashFlowItem(@NonNull String name, @NonNull String value) {
        this.name = name;
        this.value = value;
    }
}