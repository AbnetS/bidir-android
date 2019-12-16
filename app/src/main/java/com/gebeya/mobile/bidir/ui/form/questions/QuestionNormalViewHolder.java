package com.gebeya.mobile.bidir.ui.form.questions;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.ui.form.questions.values.ChoiceViewCreator;

/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder} implementation for the complex
 * questions visible on the RecyclerView.
 */
public class QuestionNormalViewHolder extends QuestionViewHolder implements
        QuestionNormalItemView,
        View.OnClickListener {

    private final TextView numberLabel;
    private final TextView textLabel;
    private final TextView requiredLabel;
    private final TextView remarkLabel;
    private final TextView sectionLabel;
    private final View shield;
    private final View selectionIndicator;

    private final LinearLayout valuesContainer;
    private final ChoiceViewCreator creator;

    public QuestionNormalViewHolder(@NonNull View itemView,
                                    @NonNull QuestionPresenter presenter,
                                    @NonNull ChoiceViewCreator creator) {
        super(itemView, presenter);
        this.creator = creator;

        numberLabel = BaseActivity.getTv(R.id.questionItemNormalNumberLabel, itemView);
        textLabel = BaseActivity.getTv(R.id.questionItemNormalTextLabel, itemView);
        requiredLabel = BaseActivity.getTv(R.id.questionItemNormalRequiredLabel, itemView);
        remarkLabel = BaseActivity.getTv(R.id.questionItemNormalRemarkLabel, itemView);
        valuesContainer = itemView.findViewById(R.id.questionItemNormalLayoutValuesContainer);
        sectionLabel = BaseActivity.getTv(R.id.questionItemSectionLabel, itemView);
        shield = itemView.findViewById(R.id.questionItemNormalShield);
        selectionIndicator = itemView.findViewById(R.id.questionItemNormalSelectionIndicator);

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
    public void toggleSection(boolean visible) {
        sectionLabel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSection(@NonNull String title) {
        sectionLabel.setText(title);
    }

    @Override
    public void loadValues(@NonNull ComplexQuestion question) {
        valuesContainer.removeAllViews();
        creator.setContainer(valuesContainer);

        final int position = getAdapterPosition();
        final String type = question.type;

        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_YES_NO)) {
            generateYesNoViews(position, question);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_FILL_IN_BLANK)) {
            generateInputView(position, question);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_MULTIPLE_CHOICE)) {
            generateMultipleChoiceViews(position, question);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_SINGLE_CHOICE)) {
            generateSingleChoiceViews(position, question);
        }
    }

    private void generateYesNoViews(int position, @NonNull ComplexQuestion question) {
        final String preselectedChoice = question.values.isEmpty() ? "" : question.values.get(0);
        creator.createSingleChoiceView(position, question.options, preselectedChoice, question);
    }

    private void generateInputView(int position, @NonNull ComplexQuestion question) {
        final String initialText = question.values.isEmpty() ? null : question.values.get(0);
        creator.createInputChoiceView(position, question.validationFactor, initialText, question);
    }

    private void generateMultipleChoiceViews(int position, @NonNull ComplexQuestion question) {
        creator.createMultipleChoiceView(position, question.options, question.values, question);
    }

    private void generateSingleChoiceViews(int position, @NonNull ComplexQuestion question) {
        final String preselectedChoice = question.values.isEmpty() ? "" : question.values.get(0);
        creator.createSingleChoiceView(position, question.options, preselectedChoice, question);
    }

    @Override
    public void setRemark(@NonNull String remark) {
        remarkLabel.setText(remark);
    }

    @Override
    public void setLocked(boolean locked) {
        shield.setVisibility(locked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSelected(boolean selected) {
        final int color = selected ? R.color.gray_super_light : R.color.white;
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
        selectionIndicator.setVisibility(selected ? View.VISIBLE : View.GONE);
    }
}
