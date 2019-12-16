package com.gebeya.mobile.bidir.impl.util;

import android.content.Context;
import android.graphics.Typeface;

public final class Fonts {

    public static Typeface normal;
    public static Typeface bold;

    public static void initialize(Context context) {
        normal = Typeface.createFromAsset(context.getAssets(), "fonts/normal.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/bold.ttf");
    }
}