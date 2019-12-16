package com.gebeya.mobile.bidir.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Splash Activity for initializing the Presenter and View code.
 */
public class SplashActivity extends BaseActivity {

    private SplashFragment fragment;
    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fragment = (SplashFragment) getFragment(R.id.splashFragmentContainer);
        if (fragment == null) {
            fragment = SplashFragment.newInstance();
            addFragment(fragment, R.id.splashFragmentContainer);
        }

        presenter = new SplashPresenter();
        fragment.attachPresenter(presenter);
    }
}