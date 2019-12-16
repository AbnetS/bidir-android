package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.ui.form.questions.values.QuestionChildViewCreator;

import java.util.List;

public class QuestionGroupedViewHolder extends QuestionViewHolder implements
        QuestionGroupedItemView, View.OnClickListener {

    private final TextView numberLabel;
    private final TextView textLabel;
    private final TextView requiredLabel;
    private final TextView remarkLabel;
    private final TextView sectionLabel;
    private final View shield;
    private final View selectionIndicator;

    private final LinearLayout questionsContainer;
    private final QuestionChildViewCreator childCreator;

    public QuestionGroupedViewHolder(@NonNull View itemView,
                                     @NonNull QuestionPresenter presenter,
                                     @NonNull QuestionChildViewCreator childCreator) {
        super(itemView, presenter);

        this.childCreator = childCreator;
        numberLabel = BaseActivity.getTv(R.id.questionItemGroupedNumberLabel, itemView);
        textLabel = BaseActivity.getTv(R.id.questionItemGroupedTextLabel, itemView);
        requiredLabel = BaseActivity.getTv(R.id.questionItemGroupedRequiredLabel, itemView);
        remarkLabel = BaseActivity.getTv(R.id.questionItemGroupedRemarkLabel, itemView);
        shield = itemView.findViewById(R.id.questionItemGroupedShield);
        sectionLabel = BaseActivity.getTv(R.id.questionItemSectionLabel, itemView);
        selectionIndicator = itemView.findViewById(R.id.questionItemNormalSelectionIndicator);

        questionsContainer = itemView.findViewById(R.id.questionItemGroupedQuestionsContainer);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onQuestionSelected(getAdapterPosition());
    }

    @Override
    public void setNumber(@NonNull String number) {
        String text = number;
        try {
            int n = Integer.parseInt(number) + 1;
            text = String.valueOf(n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        numberLabel.setText(text);
    }

    @Override
    public void setText(@NonNull String text) {
        textLabel.setText(text);
    }

    @Override
    public void setRequired(boolean required) {
        requiredLabel.setVisibility(required ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setLocked(boolean locked) {
        shield.setVisibility(locked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRemark(@NonNull String remark) {
        remarkLabel.setText(remark);
    }

    @Override
    public void setSelected(boolean selected) {
        final int color = selected ? R.color.gray_super_light : R.color.white;
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
        selectionIndicator.setVisibility(selected ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleSection(boolean visible) {
        sectionLabel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSection(@NonNull String title) {
        sectionLabel.setText(title);
    }

    @Override
    public void loadQuestions(@NonNull ComplexQuestion parentQuestion, int parentPosition, @NonNull List<ComplexQuestion> childQuestions) {
        childCreator.setQuestionsContainer(questionsContainer);
        childCreator.clearContainerQuestions();
        childCreator.createChildQuestionViews(parentQuestion, childQuestions, parentPosition);
    }
}
