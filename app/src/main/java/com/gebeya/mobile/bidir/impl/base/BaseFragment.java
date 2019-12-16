package com.gebeya.mobile.bidir.impl.base;

import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.apps.framework.base.AppBaseFragment;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.impl.util.Fonts;
import com.gebeya.mobile.bidir.impl.util.Utils;

public class BaseFragment extends AppBaseFragment {

    protected static Typeface bold;

    static {
        normal = Fonts.normal;
        bold = Fonts.bold;
    }

    protected String getMessage(String error) {
        switch (error) {
            case ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION:
                return getString(R.string.error_no_internet_connectivity);
            default:
                return null;
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