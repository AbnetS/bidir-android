package com.gebeya.mobile.bidir.ui.common.dialogs.decline;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Dialog used to provide reason (remark) for declining a screening.
 *
 * Created by Samuel K. on 12/14/2017.
 * <p>
 * samkura47@gmail.com
 */
public class DeclineDialog extends BaseDialog {

    private static final String ARG_IS_SCREENING = "IS_SCREENING";
    private static final String ARG_IS_ACAT = "IS_ACAT";

    public static DeclineDialog newInstance(boolean isScreening, boolean isACAT) {
        final DeclineDialog dialog = new DeclineDialog();

        final Bundle args = new Bundle();
        args.putBoolean(ARG_IS_SCREENING, isScreening);
        args.putBoolean(ARG_IS_ACAT, isACAT);
        dialog.setArguments(args);

        return dialog;
    }

    private DeclineDialogCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_decline);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null");

        final boolean isScreening = args.getBoolean(ARG_IS_SCREENING);
        final boolean isACAT = args.getBoolean(ARG_IS_ACAT);

        final TextView titleLabel = getTv(R.id.declineDialogTitleLabel);
        int titleId = isACAT ? R.string.decline_acat_title :
                (isScreening ? R.string.decline_screening_title : R.string.decline_loan_application_title);
        titleLabel.setText(titleId);

        final EditText remarkInput = getEd(R.id.declineDialogRemarkInput);
        final RadioGroup radioGroup = getView(R.id.declineDialogRadioGroup);

        if (isACAT) {
             declineACAT(remarkInput, radioGroup);
        } else {
            declineOther(remarkInput, radioGroup);
        }

        getBt(R.id.declineDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    private void declineACAT(EditText remarkInput, RadioGroup radioGroup) {
        radioGroup.setVisibility(View.GONE);

        getBt(R.id.declineDialogSendButton).setOnClickListener(view -> {
            final String remark = remarkInput.getText().toString().trim();

            if (remark.isEmpty()) {
                toast(getString(R.string.decline_remark_empty_prompt));
                remarkInput.requestFocus();
                return;
            }

            boolean isFinal = false;

            callback.onDecline(remark, isFinal);
            dismiss();
        });
    }

    private void declineOther(EditText remarkInput, RadioGroup radioGroup) {
        getBt(R.id.declineDialogSendButton).setOnClickListener(view -> {
            final String remark = remarkInput.getText().toString().trim();

            if (remark.isEmpty()) {
                toast(getString(R.string.decline_remark_empty_prompt));
                remarkInput.requestFocus();
                return;
            }

            boolean isFinal = radioGroup.getCheckedRadioButtonId() == R.id.declineDialogDeclineFinalChoice;

            callback.onDecline(remark, isFinal);
            dismiss();
        });
    }
    public void setCallback(@NonNull DeclineDialogCallback callback) {
        this.callback = callback;
    }
}
