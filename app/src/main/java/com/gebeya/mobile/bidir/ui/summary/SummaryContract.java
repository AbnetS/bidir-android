package com.gebeya.mobile.bidir.ui.summary;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;

/**
 * Contract interface for the summary screen
 */
public interface SummaryContract {

    /**
     * Interface definition for the View
     */
    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void loadMenu();
        void refreshMenu();
        void setCropName(@NonNull String name);
        void setCropFirstExpenseMonth(@NonNull String month);
        void setCroppingArea(@NonNull String area);
        void loadInputs(@NonNull String clientId, @NonNull String cropACATId);
        void loadCosts(@NonNull SummaryMenuItem.Label label, @NonNull String clientId, @NonNull String cropACATId);
        void loadYields(@NonNull SummaryMenuItem.Label label, @NonNull String clientId, @NonNull String cropACATId);

        void toggleApproveButton(boolean show);
        void toggleDeclineButton(boolean show);

        void showUpdatingProgress();
        void hideUpdatingProgress();
        void showError(@NonNull Result result);

        void openDeclineDialog();

        void showUpdateSuccessfulMessage();
    }

    /**
     * Interface definition for the Presenter
     */
    interface Presenter extends BasePresenter<View> {
        void onBindViewHolder(@NonNull MenuItemView holder, int position);
        int itemCount();
        int getType(int position);
        void onMenuItemSelected(int position);
        void onBackButtonPressed();

        void onApproveClicked();
        void onDeclineClicked();

        void onDeclineReturned(@NonNull String remark, boolean isFinal);
    }

    /**
     * Interface definition for the menu item view
     */
    interface MenuItemView {
        void setLabel(int type, @NonNull SummaryMenuItem.Label label);
        void setSelected(boolean selected);
    }
}