package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;

import java.util.List;

/**
 * Presenter contract for displaying questions.
 */
public interface QuestionPresenter {
    void onQuestionSelected(int position);
    void onInputAnswerProvided(@NonNull String answer, int position, @NonNull ComplexQuestion question);
    void onChoiceAnswerProvided(@NonNull List<String> answers, int position, @NonNull ComplexQuestion question);
    @NonNull String getQuestionType(int position);
    void onBindRowView(@NonNull QuestionItemView holder, int position);
    int getQuestionsCount();
    @NonNull QuestionStatus getQuestionStatus(int position);
    void onIndicatorClicked(int position);
}