package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.question.Status;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionItemView;

import java.util.List;

/**
 * Interface contract for the loan application screen and its components
 */
public interface LoanApplicationQuestionsContract {

    interface Presenter extends BasePresenter<View> {
        void onSaveClicked();
        void onSubmitClicked();
        void onAcceptClicked();
        void onBackButtonPressed();
        Status getAnswerStatus(Answer answer);
        int getMandatoryIncomplete();
        int getIncompleteQuestions();

        void onBindRowView(QuestionItemView view, int position);
        void onAnswerSelected(int position);
        void onAnswerProvided(int position, List<String> answers);
        int getAnswerCount();
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onDeclineClicked();
        void onDeclineReturned(@NonNull String remark, boolean isFinal);
    }

    interface View extends BaseView<Presenter> {
        void showRemark(@NonNull String comment);
        void hideRemark(boolean hide);
        void setTitle(@NonNull String title);
        void toggleDisableShield(boolean show);
        void populateIndicators(List<Answer> answers);
        void updateIndicators(List<Answer> answers);
        void scrollToAnswer(int position);
        void refreshAnswers();
        void showAnswers();
        void showMandatoryMissingError();
        void showUpdatingScreeningProgress();
        void hideUpdatingScreeningProgress();
        void showError(@NonNull Result result);
        void hideError();
        void showUpdateSuccessMessage();

        void toggleSaveButton(boolean show);
        void toggleSubmitButton(boolean show);
        void toggleAcceptButton(boolean show);
        void toggleDeclineButton(boolean show);
        void openDeclineDialog();
    }
}