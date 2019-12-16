package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;

/**
 * Base view interface for complex questions.
 */
public interface QuestionItemView {
    void setNumber(@NonNull String number);
    void setText(@NonNull String text);
    void setRequired(boolean required);
    void setLocked(boolean locked);
    void setRemark(@NonNull String remark);
    void setSelected(boolean selected);
    void toggleSection(boolean visible);
    void setSection(@NonNull String title);
}
