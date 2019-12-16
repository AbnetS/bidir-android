package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class whose functionality is to generate question choices and their View from the type.
 */
public class LoanApplicationChoiceViewCreator {

    private LinearLayout container;
    private LayoutInflater inflater;
    private LoanApplicationQuestionsContract.Presenter presenter;

    private int type;

    public void setContainer(LinearLayout container, LoanApplicationQuestionsContract.Presenter presenter) {
        this.container = container;
        inflater = LayoutInflater.from(container.getContext());
        this.presenter = presenter;
    }

    public void generateInputChoice(final int position, final int type, @Nullable String text) {
        final ArrayList<String> temp = new ArrayList<>();
        this.type = type;
        EditText input = (EditText) inflater.inflate(R.layout.choice_item_input_layout, container, false);
        if (text != null) input.setText(text);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                temp.clear();
                temp.add(s.toString().trim());
                presenter.onAnswerProvided(position, temp);
            }
        });

        LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);
        row.addView(input);
        container.addView(row);
    }

    public void generateSingleChoice(final int position, @NonNull List<String> choices, @NonNull String answer) {
        if (choices.isEmpty()) return;

        final ArrayList<String> temp = new ArrayList<>();
        if (!answer.isEmpty()) {
            temp.add(answer);
        }

        final int size = choices.size();
        final int columns = Constants.SCREENING_CHOICE_COLUMN_COUNT;
        final int rows = (int) Math.round((double) size / columns);

        for (int r = 0; r < rows; r++) {
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);

            for (int c = 0; c < columns; c++) {
                int index = r * columns + c;
                if (index >= size) continue;

                final CompoundButton check = generateChoiceCheck(choices.get(index), row, false);
                final String text = check.getText().toString().trim();
                if (answer.equals(text)) {
                    check.setChecked(true);
                } else {
                    check.setChecked(false);
                }
                check.setOnClickListener(v -> {
                    temp.clear();
                    temp.add(text);
                    presenter.onAnswerProvided(position, temp);
                });
                row.addView(check);
            }
            container.addView(row);
        }
    }

    public void generateMultipleChoice(final int position, final int type, @NonNull List<String> choices, @NonNull List<String> answers) {
        if (choices.isEmpty()) return;

        final ArrayList<String> temp = new ArrayList<>();

//        this.type = type;
        final int size = choices.size();
        final int columns = Constants.LOAN_APPLICATION_CHOICE_COLUMN_COUNT;
        final int rows = (int) Math.round((double) size / columns);

        if (!answers.isEmpty()) {
            temp.addAll(answers);
        }

        for (int r = 0; r < rows; r++) {
            final LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);

            for (int c = 0; c < columns; c++) {
                int index = r * columns + c;
                if (index >= size) continue;
                final CompoundButton check = generateChoiceCheck(choices.get(index), row, true);
                final String text = check.getText().toString().trim();
                if (temp.contains(text)) check.setChecked(true);
                check.setOnClickListener(v -> {
                    CompoundButton button = (CompoundButton)v;
                    boolean checked = button.isChecked();
                    if (checked) {
                        temp.add(text);
                    } else {
                        temp.remove(text);
                    }
                    presenter.onAnswerProvided(position, temp);
                });
                row.addView(check);
            }
            container.addView(row);
        }
    }

    private CompoundButton generateChoiceCheck(@NonNull String text, LinearLayout row, boolean multiple) {
        CompoundButton check = (CompoundButton) inflater.inflate(
                multiple ? R.layout.choice_item_multiple_choice_multiple_check :
                        R.layout.choice_item_multiple_choice_single_radio,
                row, false
        );
        check.setText(text);
        return check;
    }
}