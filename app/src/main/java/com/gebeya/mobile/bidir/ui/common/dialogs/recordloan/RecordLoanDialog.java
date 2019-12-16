package com.gebeya.mobile.bidir.ui.common.dialogs.recordloan;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class RecordLoanDialog extends BaseDialog {

    private static final String ARG_CLIENT_ID = "client_id";

    public static RecordLoanDialog newInstance(@NonNull String clientId) {
        final RecordLoanDialog dialog = new RecordLoanDialog();

        final Bundle args = new Bundle();
        args.putString(ARG_CLIENT_ID, clientId);

        dialog.setArguments(args);
        return dialog;
    }

    private RecordLoanCallBack callBack;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_loan_record);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null.");

        final String clientId = args.getString(ARG_CLIENT_ID);

        final EditText loanApprovedInput = getEd(R.id.recordDialogLoanAmountInput);
        getBt(R.id.recordDialogOKButton).setOnClickListener(v -> {
            final String item = loanApprovedInput.getText().toString().trim();

            if (item.isEmpty()) {
                toast(R.string.record_loan_amount_input_prompt);
                loanApprovedInput.requestFocus();
                return;
            }

            callBack.onRecord(item, clientId);
            dismiss();
        });

        getBt(R.id.recordDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallBack(@NonNull RecordLoanCallBack callBack) {
        this.callBack = callBack;
    }
}
