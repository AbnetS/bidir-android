package com.gebeya.mobile.bidir.ui.common.dialogs.creategroup;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class CreateGroupDialog extends BaseDialog{

    public static CreateGroupDialog newInstance() {
        return new CreateGroupDialog();
    }

    private CreateGroupCallback callBack;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_create_group);

        final EditText groupCodeInput = getEd(R.id.groupDialogGroupCodeInput);
        final EditText groupMemberCountInput = getEd(R.id.groupDialogMemberCountInput);
        final EditText groupLoanAmountInput = getEd(R.id.groupDialogLoanAmountInput);

        getBt(R.id.groupDialogOKButton).setOnClickListener(view -> {
            final String groupCode = groupCodeInput.getText().toString().trim();

            if (groupCode.isEmpty()) {
                toast(R.string.group_code_input_prompt);
                groupCodeInput.requestFocus();
                return;
            }

            final String memberCount = groupMemberCountInput.getText().toString().trim();

            if (memberCount.isEmpty()) {
                toast(R.string.group_member_count_input_prompt);
                groupMemberCountInput.requestFocus();
                return;
            }

            final String loanAmount = groupLoanAmountInput.getText().toString().trim();

            if (loanAmount.isEmpty()) {
                toast(R.string.group_loan_amount_input_prompt);
                groupLoanAmountInput.requestFocus();
                return;
            }

            callBack.onCreate(groupCode, Integer.parseInt(memberCount), Double.parseDouble(loanAmount));
            dismiss();

        });

        getBt(R.id.groupDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallBack(@NonNull CreateGroupCallback callBack) {
        this.callBack = callBack;
    }
}
