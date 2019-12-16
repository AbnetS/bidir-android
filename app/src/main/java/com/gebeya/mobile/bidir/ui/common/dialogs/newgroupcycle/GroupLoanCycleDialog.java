package com.gebeya.mobile.bidir.ui.common.dialogs.newgroupcycle;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class GroupLoanCycleDialog extends BaseDialog {
    public static final String ARG_GROUP_ID = "GROUP_ID";

    public static GroupLoanCycleDialog newInstance(@NonNull String groupId) {
        final GroupLoanCycleDialog dialog = new GroupLoanCycleDialog();

        final Bundle args = new Bundle();
        args.putString(ARG_GROUP_ID, groupId);

        dialog.setArguments(args);
        return dialog;
    }

    private GroupLoanCycleCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_group_cycle);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null.");

        final String groupId = args.getString(ARG_GROUP_ID);

        final EditText loanAmount = getEd(R.id.groupCycleDialogLoanAmountInput);
        getBt(R.id.groupCycleDialogOKButton).setOnClickListener(v -> {
            final String amount = loanAmount.getText().toString().trim();

            if (amount.isEmpty()) {
                toast(R.string.group_loan_amount_input_prompt);
                loanAmount.requestFocus();
                return;
            }

            callback.onGroupLoanCycleCallback(groupId, amount);
            dismiss();
        });

        getBt(R.id.groupCycleDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallback(@NonNull GroupLoanCycleCallback callback) {
        this.callback = callback;
    }
}
