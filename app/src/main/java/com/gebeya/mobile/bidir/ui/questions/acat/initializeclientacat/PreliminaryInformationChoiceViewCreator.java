package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.util.Constants;

import java.util.List;

/**
 * Helper class whose functionality is to generate crop choices.
 *
 */

public class PreliminaryInformationChoiceViewCreator {

    private LinearLayout container;
    private LayoutInflater inflater;
    private PreliminaryInformationContract.Presenter presenter;

    public void setContainer(LinearLayout container, PreliminaryInformationContract.Presenter presenter) {
        this.container = container;
        inflater = LayoutInflater.from(container.getContext());
        this.presenter = presenter;
    }

    public void generateMultipleChoice(final int position, @NonNull List<String> choices, @NonNull List<String> answers) {
        if (choices.isEmpty()) return;

        final int size = choices.size();
        final int columns = Constants.PRELIMINARY_INFORMATION_CHOICE_COLUMN_COUNT;
        final int rows = (int) Math.round((double) size / columns);

        for (int r = 0; r < rows; r++) {
            final  LinearLayout row = (LinearLayout) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_row_layout, container, false);

            for (int c = 0; c < columns; c++) {
                int index = r * columns + c;
                if (index >= size) continue;

                final String answer = choices.get(index);
                final CompoundButton check = generateChoiceCheck(answer, row);
                final String text = check.getText().toString().trim();
                check.setChecked(answers.contains(text));
                check.setOnClickListener(view -> presenter.onCropToggled(position));
                row.addView(check);
            }
            container.addView(row);
        }
    }

    private CompoundButton generateChoiceCheck(@NonNull String text, LinearLayout row) {
        CompoundButton check = (CompoundButton) inflater.inflate(R.layout.choice_item_multiple_choice_multiple_check, row, false);
        check.setText(text);
        return check;
    }
}
