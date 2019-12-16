package com.gebeya.mobile.bidir.ui.form.questions.values;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionPresenter;

import java.util.List;

/**
 * Implementation for the {@link QuestionChildViewCreator} interface.
 */
public class BaseQuestionChildViewCreator implements QuestionChildViewCreator {

    private LinearLayout container;
    private final ChoiceViewCreator creator;
    private final LayoutInflater inflater;
    private final QuestionPresenter presenter;

    public BaseQuestionChildViewCreator(@NonNull LayoutInflater inflater,
                                        @NonNull QuestionPresenter presenter) {
        this.creator = new BaseChoiceViewCreator(inflater, presenter);
        this.inflater = inflater;
        this.presenter = presenter;
    }

    @Override
    public void setQuestionsContainer(@NonNull LinearLayout container) {
        this.container = container;
    }

    @Override
    public void clearContainerQuestions() {
        final int count = container.getChildCount();
        if (count <= 1) return;

        container.removeViews(1, count - 1);
    }

    @Override
    public void createChildQuestionViews(@NonNull ComplexQuestion parentQuestion, @NonNull List<ComplexQuestion> childQuestions, int parentPosition) {
        final int length = childQuestions.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion childQuestion = childQuestions.get(i);
            final LinearLayout questionItemView = createChildQuestionItem();

            populateChildQuestionItemView(parentQuestion, childQuestion, i + 1, questionItemView);
            populateChildQuestionValues(childQuestion, questionItemView, parentPosition);

            container.addView(questionItemView);
        }
    }

    private void populateChildQuestionValues(@NonNull ComplexQuestion childQuestion, @NonNull LinearLayout questionItemView, int parentPosition) {
        final LinearLayout valuesContainer = questionItemView.findViewById(R.id.complexQuestionItemChildValuesContainer);
        creator.setContainer(valuesContainer);
        final String type = childQuestion.type;

        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_YES_NO)) {
            generateYesNoViews(parentPosition, childQuestion);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_FILL_IN_BLANK)) {
            generateInputView(parentPosition, childQuestion);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_MULTIPLE_CHOICE)) {
            generateMultipleChoiceViews(parentPosition, childQuestion);
            return;
        }
        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_SINGLE_CHOICE)) {
            generateSingleChoiceViews(parentPosition, childQuestion);
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

    private void populateChildQuestionItemView(@NonNull ComplexQuestion parentQuestion, @NonNull ComplexQuestion childQuestion, int position, @NonNull LinearLayout questionItemView) {
        final TextView numberLabel = BaseActivity.getTv(R.id.complexQuestionItemChildNumberLabel, questionItemView);
        final TextView requiredLabel = questionItemView.findViewById(R.id.complexQuestionItemChildRequiredLabel);
        final TextView textLabel = BaseActivity.getTv(R.id.complexQuestionItemChildTextLabel, questionItemView);

        final String number = (parentQuestion.number + 1) + "." + position;
        numberLabel.setText(number);

        requiredLabel.setVisibility(childQuestion.required ? View.VISIBLE : View.INVISIBLE);
        textLabel.setText(childQuestion.text);
    }

    private LinearLayout createChildQuestionItem() {
        return (LinearLayout) inflater.inflate(R.layout.complex_question_item_child_three_column_layout, container, false);
    }
}