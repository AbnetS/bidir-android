package com.gebeya.mobile.bidir.ui.common.dialogs.gender;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Dialog used to provide gender status
 * <p>
 * samkura47@gmail.com
 */

public class GenderDialog extends BaseDialog{

    public static GenderDialog newInstance() {
        return new GenderDialog();
    }

    private GenderSelectorCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_gender);

        final RadioGroup radioGroup = getView(R.id.gender_dialog_radio_group);

        getBt(R.id.gender_dialog_done_button).setOnClickListener(view -> {
            boolean gender = radioGroup.getCheckedRadioButtonId() == R.id.gender_dialog_male_choice;

            callback.onGenderSelected(gender);
            dismiss();
        });

        return createDialog();
    }

    public void setCallback(@NonNull GenderSelectorCallback callback) {
        this.callback = callback;
    }
}
