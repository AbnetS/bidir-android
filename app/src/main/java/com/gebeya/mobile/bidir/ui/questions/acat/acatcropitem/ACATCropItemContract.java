package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

/**
 * Created by Samuel K. on 7/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATCropItemContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull ACATCropItemView holder, int position);
        void onACATCropItemSelected(int position);
        int getACATCropItemCount();
        void onBackButtonPressed();
        void onOptionMenuClicked(android.view.View view, int position);
        void onLoanProposalClicked();

        void onLoadingFailed(@NonNull Throwable throwable);

        int optionMenuVisibility();

        void onApproveButtonClicked();
        void onDeclineButtonClicked();

        void onDeclineReturned(@NonNull String remark, boolean isFinal);
    }

    interface View extends BaseView<Presenter> {
        void showACATCropItems();
        void openACATCostEstimation(@NonNull String clientId, @NonNull String cropACATId, boolean monitor);
        void setTitle(@NonNull String title);
        void openPopUpMenu(android.view.View view, @NonNull String clientId, @NonNull String cropID);
        void openACATSummary(@NonNull String clientId, @NonNull String cropACATId);

        void toggleLoanProposal(boolean show);
        void toggleOptionMenu(boolean show);

        void toggleApproveButton(boolean show);
        void toggleDeclineButton(boolean show);

        void setLoanStatus(@NonNull LoanProposal loanProposal);

        void openLoanProposalSummary(@NonNull String clientId, @Nullable Task task);

        void showLoadingProgress();
        void hideLoadingProgress();
        void showUpdatingProgress();
        void showError(@NonNull Result result);

        void openDeclineDialog();

        void showUpdatingSuccessMessage();

        void hideUpdatingProgress();

        void refreshList();

        void showDataNotSyncedMessage();
    }

    interface ACATCropItemView extends BaseMainItemView {

    }
}
