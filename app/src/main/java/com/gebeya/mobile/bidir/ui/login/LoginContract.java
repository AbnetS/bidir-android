package com.gebeya.mobile.bidir.ui.login;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.impl.util.location.mvp.LocationApiView;

/**
 * Interface contract for the login screen.
 */
public interface LoginContract {

    /**
     * Interface for the login presenter
     */
    interface Presenter extends BasePresenter<View> {
        void onLoginPressed(@NonNull String username, @NonNull String password);
        void onForgotPasswordPressed();
        void onLoginComplete();
        void onLoginFailed(@NonNull Throwable throwable);
    }

    /**
     * Interface for the login view
     */
    interface View extends BaseView<Presenter>, LocationApiView {
        void showUsernameMissingError();
        void showPasswordMissingError();
        void loadLogo(String url);
        void showLoginProgress();
        void hideLoginProgress();
        void showError(@NonNull Result result);
        void hideError();
        void openHomeScreen();
        void startDownloadService();
        void showSuccessMessage();
        void openForgotPasswordScreen();
    }
}