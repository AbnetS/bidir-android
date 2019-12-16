package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;

public class SummaryMenuItem {

    public enum Label {
        COST_ESTIMATION,
        INPUTS_AND_ACTIVITIES_COST,
        INPUTS,
        SEEDS,
        FERTILIZERS,
        CHEMICALS,
        LABOR_COST,
        OTHER_COSTS,
        ESTIMATED_YIELD,
        PROBABLE_YIELD,
        MAXIMUM_YIELD,
        MINIMUM_YIELD
    }

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_SUBTITLE = 2;
    public static final int TYPE_PARENT = 3;
    public static final int TYPE_CHILD = 4;

    public final int type;
    public final Label label;

    public SummaryMenuItem(int type, @NonNull Label label) {
        this.type = type;
        this.label = label;
    }
}
