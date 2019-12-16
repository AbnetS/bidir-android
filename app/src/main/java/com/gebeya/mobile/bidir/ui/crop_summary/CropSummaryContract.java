package com.gebeya.mobile.bidir.ui.crop_summary;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;

/**
 * Contract interface for the crop summary screen.
 */

public interface CropSummaryContract {

    /**
     * Interface definition for the view.
     */
    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void loadMenu();
        void refreshMenu();
        void loadCrops(@NonNull String clientId, @NonNull String cropId);
        void loadLoanProposal(@NonNull String clientId);

        void showUpdatingProgress();
        void hideUpdatingProgress();
        void showError(@NonNull Result result);

        void showLoadingMessage();
        void hideLoadingProgress();

        void toggleApproveButton(boolean show);
        void toggleDeclineButton(boolean show);

        void toggleMenuButton(boolean show);

        void openDeclineDialog();
    }

    /**
     * Interface definition for the presenter.
     */
    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull MenuItemView holder, int position);
        int getMenuItemCount();
        void onMenuItemSelected(int position);
        void onLoanProposalClicked();
        void onBackButtonPressed();

        void onApproveClicked();
        void onDeclineClicked();

        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onDeclineReturned(@NonNull String remark, boolean isFinal);
    }

    /**
     * Interface definition for the menu item view.
     */
    interface MenuItemView {
        void setCropMenuLabel(@NonNull String cropName);
        void setSelected(boolean selected);
    }
}
