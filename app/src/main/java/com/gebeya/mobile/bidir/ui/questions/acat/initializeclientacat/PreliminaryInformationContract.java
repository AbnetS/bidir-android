package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;

/**
 * Created by Samuel K. on 5/5/2018.
 * <p>
 * samkura47@gmail.com
 */

public interface PreliminaryInformationContract {

    interface Presenter extends BasePresenter<View> {
        void onNextClicked();
        void onCropToggled(int position);
        void onBindRowView(PreliminaryInformationContract.PreliminaryQuestionItemView view, int position);
        int getAnswerCount();

        void onBindProductRowItem(@NonNull LoanProductItemView holder, int position);
        int getLoanProductCount();
        void onLoanProductItemSelected(int position);

        void onUpdateComplete();
        void onLoadingComplete();

        void onUpdateFailed(@NonNull Throwable throwable);
        void onLoadingFailed(@NonNull Throwable throwable);

        void onBackButtonPressed();

        void onErrorDismissed();

    }

    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void showLoadingProgress();
        void hideProgress();
        void refreshAnswers();
        void showAnswers();
        void showLoanProducts();
        void showAnswerMissingError();
        void showInitializingACATProgress();
        void showError(@NonNull Result result);
        void showLoadingError(@NonNull Result result);
        void hideError();
        void showUpdateSuccessMessage();
        void openACATPreliminaryInfo(@NonNull String clientId, @Nullable String groupId);
        void toggleNextButton(boolean show);
        void updateSelectedCropsCount(int count);
    }

    interface PreliminaryQuestionItemView {
        void setChoice(@NonNull String choice, boolean checked);
    }

    interface LoanProductItemView {
        void setName(@NonNull String name);
    }
}
