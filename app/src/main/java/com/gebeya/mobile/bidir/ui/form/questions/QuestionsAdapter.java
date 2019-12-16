package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.ui.form.questions.values.BaseChoiceViewCreator;
import com.gebeya.mobile.bidir.ui.form.questions.values.BaseQuestionChildViewCreator;
import com.gebeya.mobile.bidir.ui.form.questions.values.ChoiceViewCreator;
import com.gebeya.mobile.bidir.ui.form.questions.values.QuestionChildViewCreator;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} implementation for for the list of
 * {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion} items.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_GROUPED = 1;

    private final QuestionPresenter presenter;
    private final LayoutInflater inflater;
    private final ChoiceViewCreator creator;
    private final QuestionChildViewCreator childCreator;

    public QuestionsAdapter(@NonNull LayoutInflater inflater,
                            @NonNull QuestionPresenter presenter) {
        this.presenter = presenter;
        this.inflater = inflater;
        creator = new BaseChoiceViewCreator(inflater, presenter);
        this.childCreator = new BaseQuestionChildViewCreator(inflater, presenter);
    }

    @Override
    public int getItemViewType(int position) {
        final String type = presenter.getQuestionType(position);
        return type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED) ? TYPE_GROUPED : TYPE_NORMAL;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final boolean normal = viewType == TYPE_NORMAL;

        final int layout = normal ?
                R.layout.question_item_normal_three_column_layout :
                R.layout.question_item_grouped_three_column_layout;
        final View itemView = inflater.inflate(layout, parent, false);
        return normal ? new QuestionNormalViewHolder(itemView, presenter, creator) :
                new QuestionGroupedViewHolder(itemView, presenter, childCreator);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        presenter.onBindRowView(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getQuestionsCount();
    }
}