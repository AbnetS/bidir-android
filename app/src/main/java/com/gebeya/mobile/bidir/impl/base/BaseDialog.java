package com.gebeya.mobile.bidir.impl.base;

import android.app.Dialog;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.apps.framework.base.AppBaseDialog;
import com.gebeya.mobile.bidir.impl.util.Fonts;
import com.gebeya.mobile.bidir.impl.util.Utils;

public class BaseDialog extends AppBaseDialog {

    protected static Typeface bold;

    static {
        normal = Fonts.normal;
        bold = Fonts.bold;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
    }

    @Override
    protected TextView getTv(int id) {
        return getTv(id, normal);
    }

    @Override
    protected EditText getEd(int id) {
        return getEd(id, normal);
    }

    @Override
    public void d(String message) {
        Utils.d(this, message);
    }

    @Override
    public void d(String message, Object... formatArgs) {
        Utils.d(this, message, formatArgs);
    }

    @Override
    public void e(String message) {
        Utils.e(this, message);
    }

    @Override
    public void e(String message, Object... formatArgs) {
        Utils.d(this, message, formatArgs);
    }
}
