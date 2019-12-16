package com.gebeya.mobile.bidir.ui.form.questions.values;

import android.text.TextWatcher;

/**
 * Simple helper class for inputs that only leaves the afterTextChanged method non-overridden.
 */
public abstract class InputWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
