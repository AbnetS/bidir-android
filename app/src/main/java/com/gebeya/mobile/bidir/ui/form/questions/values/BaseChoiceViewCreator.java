package com.gebeya.mobile.bidir.ui.form.questions.values;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Fonts;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation for the {@link ChoiceViewCreator} interface.
 */
public class BaseChoiceViewCreator implements ChoiceViewCreator {

    private LinearLayout container;
    private final LayoutInflater inflater;
    private final QuestionPresenter presenter;

    public BaseChoiceViewCreator(@NonNull LayoutInflater inflater,
                                 @NonNull QuestionPresenter presenter) {
        this.inflater = inflater;
        this.presenter = presenter;
    }

    @Override
    public void setContainer(@NonNull LinearLayout container) {
        this.container = container;
    }

    @Override
    public void createInputChoiceView(int position, @NonNull String validationFactor, @Nullable String initialText, @NonNull ComplexQuestion question) {
        final int inputLayout = validationFactor.equals(ComplexQuestionParser.VALIDATION_FACTOR_LOCAL_NUMERIC) ?
                R.layout.choice_item_input_number_layout :
                R.layout.choice_item_input_layout;

        final EditText input = (EditText) inflater.inflate(inputLayout, container, false);
        input.setTypeface(Fonts.normal);

        if (initialText != null) input.setText(initialText);
        input.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                final String answer = s.toString().trim();
                presenter.onInputAnswerProvided(answer, position, question);
            }
        });

        final LinearLayout inputContainer = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);
        inputContainer.addView(input);
        container.addView(inputContainer);
    }

    @Override
    public int getInputViewType(@NonNull String validationFactor) {
        switch (validationFactor) {
            case ComplexQuestionParser.VALIDATION_FACTOR_LOCAL_NUMERIC:
                return InputType.TYPE_NUMBER_VARIATION_NORMAL;
            case ComplexQuestionParser.VALIDATION_FACTOR_LOCAL_ALPHABETIC:
            case ComplexQuestionParser.VALIDATION_FACTOR_LOCAL_ALPHANUMERIC:
            case ComplexQuestionParser.VALIDATION_FACTOR_LOCAL_NONE:
                return InputType.TYPE_TEXT_VARIATION_NORMAL;
            default:
                return InputType.TYPE_TEXT_VARIATION_NORMAL;
        }
    }

    @Override
    public void createSingleChoiceView(int position, @NonNull List<String> choices, @NonNull String preSelectedChoice, @NonNull ComplexQuestion question) {
        if (choices.isEmpty()) return;

        final List<String> answers = new ArrayList<>();
        if (!preSelectedChoice.isEmpty()) {
            answers.add(preSelectedChoice);
        }

        final int size = choices.size();
        final int columns = Constants.SCREENING_CHOICE_COLUMN_COUNT;
        final int rows = (int) Math.round((double) size / columns);

        for (int r = 0; r < rows; r++) {
            final LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);

            for (int c = 0; c < columns; c++) {     // I know. This is Java and not C++
                final int index = r * columns + c;
                if (index >= size) continue;

                final String text = choices.get(index);
                final CompoundButton radioButton = generateCompoundButton(text, row, false);
                radioButton.setChecked(preSelectedChoice.equals(text));
                radioButton.setOnClickListener(v -> {
                    answers.clear();
                    answers.add(text);
                    presenter.onChoiceAnswerProvided(answers, position, question);
                });
                row.addView(radioButton);
            }
            container.addView(row);
        }
    }

    @Override
    public void createMultipleChoiceView(int position, @NonNull List<String> choices, @NonNull List<String> preSelectedChoices, @NonNull ComplexQuestion question) {
        if (choices.isEmpty()) return;

        final List<String> answers = new ArrayList<>();
        if (!preSelectedChoices.isEmpty()) {
            answers.addAll(preSelectedChoices);
        }

        final int size = choices.size();
        final int columns = Constants.SCREENING_CHOICE_COLUMN_COUNT;
        final int rows = (int) Math.round((double) size / columns);

        for (int r = 0; r < rows; r++) {
            final LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);

            for (int c = 0; c < columns; c++) {
                final int index = r * columns + c;
                if (index >= size) continue;

                final String text = choices.get(index);
                final CompoundButton check = generateCompoundButton(text, row, true);
                if (answers.contains(text)) {
                    check.setChecked(true);
                }
                check.setOnClickListener(v -> {
                    final CompoundButton button = (CompoundButton) v;
                    final boolean checked = button.isChecked();
                    if (checked) {
                        answers.add(text);
                    } else {
                        answers.remove(text);
                    }
                    presenter.onChoiceAnswerProvided(answers, position, question);
                });
                row.addView(check);
            }
            container.addView(row);
        }
    }

    private CompoundButton generateCompoundButton(@NonNull String text, @NonNull LinearLayout row, boolean isMultipleChoice) {
        final int layout = isMultipleChoice ? R.layout.choice_item_multiple_choice_multiple_check :
                R.layout.choice_item_multiple_choice_single_radio;
        final CompoundButton check = (CompoundButton) inflater.inflate(layout, row, false);
        check.setText(text);
        return check;
    }
}
