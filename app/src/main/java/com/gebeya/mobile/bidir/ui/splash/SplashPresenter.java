package com.gebeya.mobile.bidir.ui.splash;

import android.annotation.SuppressLint;

import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Implementation of the SplashContract.Presenter as a presenter
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;

    @Inject UserLocalSource userLocal;

    public SplashPresenter() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
        userLocal.first().subscribe(data -> {
            if (data.empty()) {
                view.loadLogo(Constants.LOGO_URL);
            } else {
                view.openHomeScreen();
                view.close();
            }
        });
    }

    @Override
    public void onLogoLoaded() {
        if (view == null) return;
        view.startLogoDisplayTimeout();
    }

    @Override
    public void onLogoDisplayTimeout() {
        if (view == null) return;
        view.openLoginScreen();
        view.close();
    }

    @Override
    public void attachView(SplashContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view.stopLogoDisplayTimeout();
        view = null;
    }

    @Override
    public SplashContract.View getView() {
        return view;
    }
}
