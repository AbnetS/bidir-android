package com.gebeya.mobile.bidir.ui.home.main;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;

/**
 * Contract for the Main component on the home screen
 */
public interface MainContract {

    /**
     * Interface for the main component View
     */
    interface View extends BaseView<Presenter> {
        void openSearch();
        void openNotificationsScreen();
        void openFilterMenu();
        void activateTab(int position);
        void showLoggedOutMessage();
        void openLoginScreen();

        void showProgressContainer();
        void updateProgress(int progress);
        void hideProgressContainer();
        void showDownloadCompleteMessage();
    }

    /**
     * Interface for the main component Presenter
     */
    interface Presenter extends BasePresenter<View> {
        void onNavLogOutPressedPressed();
        void onSearchIconPressed();
        void onNotificationsIconPressed();
        void onFilterIconPressed();
        void onTabSelected(int position);
    }
}
