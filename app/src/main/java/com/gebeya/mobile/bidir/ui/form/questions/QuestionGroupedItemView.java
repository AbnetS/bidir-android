package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;

import java.util.List;

/**
 * Item view for a grouped complex question
 */
public interface QuestionGroupedItemView {
    void loadQuestions(@NonNull ComplexQuestion parentQuestion, int parentPosition, @NonNull List<ComplexQuestion> childQuestions);
}
