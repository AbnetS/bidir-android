package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Parent view holder for the screening questions view holder, for both the normal and grouped
 * questions.
 */
public abstract class QuestionViewHolder extends RecyclerView.ViewHolder implements QuestionItemView {

    protected final QuestionPresenter presenter;

    public QuestionViewHolder(@NonNull View itemView, @NonNull QuestionPresenter presenter) {
        super(itemView);
        this.presenter = presenter;
    }
}