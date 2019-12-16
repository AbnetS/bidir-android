package com.gebeya.mobile.bidir.ui.common.dialogs.dateselector;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;
import com.gebeya.mobile.bidir.impl.util.Constants;

import org.joda.time.DateTime;

public class DateSelectorDialog extends BaseDialog implements TextWatcher {

    private static final String ARG_DAY = "DAY";
    private static final String ARG_MONTH = "MONTH";
    private static final String ARG_YEAR = "YEAR";

    public static DateSelectorDialog newInstance(int day, int month, int year) {
        DateSelectorDialog dialog = new DateSelectorDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_YEAR, year);
        dialog.setArguments(args);
        return dialog;
    }

    private EditText dayInput;
    private EditText monthInput;
    private EditText yearInput;

    private DateSelectorCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_date_selector);

        Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Args cannot be null");

        final int day = args.getInt(ARG_DAY, 0);
        final int month = args.getInt(ARG_MONTH, 0);
        final int year = args.getInt(ARG_YEAR, 0);

        boolean isNew = day == 0 || month == 0 || year == 0;

        String title = getString(isNew ? R.string.date_selector_add_title : R.string.date_selector_edit_title);
        getTv(R.id.dateSelectorTitleLabel).setText(title);

        dayInput = getEd(R.id.dateSelectorDayInput);
        monthInput = getEd(R.id.dateSelectorMonthInput);
        yearInput = getEd(R.id.dateSelectorYearInput);

        getTv(R.id.dateSelectorAutoFillAgeLabel);
        final EditText autoFillAgeInput = getEd(R.id.dateSelectorAutoFillAgeInput);
        autoFillAgeInput.addTextChangedListener(this);

        if (!isNew) {
            dayInput.setText(String.valueOf(day));
            monthInput.setText(String.valueOf(month));
            yearInput.setText(String.valueOf(year));
        }

        getBt(R.id.dateSelectorCancelButton).setOnClickListener(v -> dismiss());

        getBt(R.id.dateSelectorSaveButton).setOnClickListener(v -> {
            String dayString = dayInput.getText().toString().trim();
            if (dayString.isEmpty()) {
                toast(R.string.date_selector_day_prompt);
                dayInput.requestFocus();
                return;
            }

            String monthString = monthInput.getText().toString().trim();
            if (monthString.isEmpty()) {
                toast(R.string.date_selector_month_prompt);
                monthInput.requestFocus();
                return;
            }

            String yearString = yearInput.getText().toString().trim();
            if (yearString.isEmpty()) {
                toast(R.string.date_selector_year_prompt);
                yearInput.requestFocus();
            }

            int day1 = Integer.parseInt(dayString);
            int month1 = Integer.parseInt(monthString);
            int year1 = Integer.parseInt(yearString);

            if (day1 > 30) {
                toast(R.string.date_selector_valid_day_prompt);
                dayInput.requestFocus();
                return;
            }
            if (month1 > 13) {
                toast(R.string.date_selector_valid_month_prompt);
                monthInput.requestFocus();
                return;
            }
            if (year1 <= 0) {
                toast(R.string.date_selector_valid_year_prompt);
                yearInput.requestFocus();
            }

            callback.onDateSelected(day1, month1, year1);
            dismiss();
        });

        return createDialog();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        final String ageString = s.toString().trim();
        if (ageString.isEmpty()) return;

        String dayString = dayInput.getText().toString().trim();
        if (dayString.isEmpty()) dayString = "1";

        String monthString = monthInput.getText().toString().trim();
        if (monthString.isEmpty()) monthString = "1";

        int age = Integer.parseInt(ageString);
        if (age > Constants.MAX_CLIENT_REGISTRATION_AGE) return;

        int ethiopianYear = new DateTime().getYear() - 7;             // ugly, but it should work.

        int birthYear = ethiopianYear - age;
        String yearString = String.valueOf(birthYear);

        dayInput.setText(dayString);
        monthInput.setText(monthString);
        yearInput.setText(yearString);
    }

    public void setCallback(@NonNull DateSelectorCallback callback) {
        this.callback = callback;
    }
}