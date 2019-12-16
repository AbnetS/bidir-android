package com.gebeya.mobile.bidir.ui.form.loanapplication;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionPresenter;

import java.util.List;

/**
 * Contract interface for a loan application's question components.
 */
public interface LoanApplicationContract {

    interface View extends BaseView<Presenter> {
        void showLoadingProgress();
        void hideLoadingProgress();
        void showUpdateProgress();
        void hideUpdateProgress();
        void showUpdateSuccessfulMessage();
        void showError(@NonNull Result result);
        void showMandatoryMissingError();
        void openDeclineDialog();

        void toggleSaveButton(boolean show);
        void toggleSubmitButton(boolean show);
        void toggleApproveButton(boolean show);
        void toggleDeclineButton(boolean show);
        void toggleRemark(boolean show);
        void toggleReadOnlyModeShield(boolean show);

        void setRemark(@NonNull String remark);
        void setTitle(@NonNull String title);
        void generateQuestionIndicators(@NonNull List<ComplexQuestion> questions);
        void loadQuestions();
        void refreshQuestion(int position);
        void refreshQuestions();
        void refreshIndicators(@NonNull List<ComplexQuestion> questions);
        void scrollToItem(int position);
    }

    interface Presenter extends BasePresenter<View>, QuestionPresenter {
        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onBackButtonPressed();

        void onSaveClicked();
        void onSubmitClicked();
        void onApproveClicked();
        void onDeclineClicked();
        void onDeclineReturned(@NonNull String remark, boolean isFinal);
    }
}