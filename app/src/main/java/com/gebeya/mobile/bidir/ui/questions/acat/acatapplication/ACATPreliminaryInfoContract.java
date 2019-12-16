package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;

import java.util.List;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface ACATPreliminaryInfoContract {

    interface Presenter extends BasePresenter<View> {
        void onSaveClicked();
        void onBackButtonPressed();
        void onBindRowView(ACATPreliminaryInfoContract.QuestionItemView view, int position);
        int getCropCount();
        void onNonFinancialServiceToggled(boolean toggled, int position);
        void onDetailButtonPressed(int position);
        void onNonFinancialResourceChanged(@NonNull List<String> services, int position);
        void onNonFinancialResourcesDismissed(int position);
        void onFirstExpenseMonthSelected(@NonNull String expenseMonth, int position);
        void onCroppingAreaChanged(@NonNull String croppingArea, int position);
        void onSuccessDismissed();
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);
        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);
        void onGPSLocationPressed(int position);
    }

    interface View extends BaseView<Presenter> {

        void setTitle(@NonNull String title);
        void showCrops();
        void setCroppingArea(@NonNull String croppingArea);
        void setFirstExpenseMonth(@NonNull String firstExpenseMonth);
        void showMissingCroppingAreaError();
        void showUpdatingProgress();
        void hideUpdatingProgress();
        void showError(@NonNull Result result);
        void hideError();
        void showUpdateSuccessMessage();
        void showLoadingProgress();
        void hideLoadingProgress();
        void toggleSaveButton(boolean show);
        void openCostEstimationActivity(@NonNull String clientId);
        void openNonFinancialServiceDialog(int position, @NonNull List<String> services);

        void refreshItem(int position);
        void refreshList();

        void openGPSSelector(@NonNull String cropACATId, @NonNull String acatApplicationId);

        void openCropListActivity(@NonNull String clientId, @NonNull String clientACATId, @Nullable String groupId);
    }

    interface QuestionItemView {
        void setCropName(@NonNull String cropName);
        void setFirstExpenseMonth(int position);
        void setCroppingArea(@NonNull String croppingAreaValue);
        void toggleNonFinServices(boolean nonFinServices);
        void showDetailButton(boolean show);
        void toggleButtonLocation(boolean enabled);
        void setLocationInfo(int count);
        void toggleLocationAddEditButton(boolean edit);
    }
}
