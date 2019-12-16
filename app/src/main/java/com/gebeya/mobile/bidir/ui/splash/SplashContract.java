package com.gebeya.mobile.bidir.ui.splash;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;

/**
 * Interface contract for the splash screen.
 */
public interface SplashContract {

    /**
     * Splash screen View interface
     */
    interface View extends BaseView<Presenter> {
        void openLoginScreen();
        void openHomeScreen();
        void startLogoDisplayTimeout();
        void stopLogoDisplayTimeout();
        void loadLogo(String url);
    }

    /**
     * Splash screen Presenter interface
     */
    interface Presenter extends BasePresenter<View> {
        void onLogoLoaded();
        void onLogoDisplayTimeout();
    }
}