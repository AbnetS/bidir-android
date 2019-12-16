package com.gebeya.mobile.bidir.impl.base;

import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gebeya.apps.framework.base.AppBaseActivity;
import com.gebeya.apps.framework.base.AppBaseFragment;
import com.gebeya.mobile.bidir.impl.util.Fonts;
import com.gebeya.mobile.bidir.impl.util.Utils;

public class BaseActivity extends AppBaseActivity {

    protected static Typeface bold;

    static {
        normal = Fonts.normal;
        bold = Fonts.bold;
    }

    protected AppBaseFragment getFragment(int containerId) {
        return (AppBaseFragment) getSupportFragmentManager().findFragmentById(containerId);
    }

    protected void addFragment(AppBaseFragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    protected void replaceFragment(AppBaseFragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
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
