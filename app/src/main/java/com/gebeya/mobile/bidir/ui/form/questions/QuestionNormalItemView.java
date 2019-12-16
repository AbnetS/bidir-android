package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;

/**
 * Item view for a normal complex question
 */
public interface QuestionNormalItemView extends QuestionItemView {
    void loadValues(@NonNull ComplexQuestion question);
}
