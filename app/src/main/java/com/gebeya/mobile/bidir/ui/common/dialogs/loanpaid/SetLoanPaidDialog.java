package com.gebeya.mobile.bidir.ui.common.dialogs.loanpaid;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class SetLoanPaidDialog extends BaseDialog {

    private static final String ARG_CLIENT = "CLIENT";
    private static final String ARG_GROUP = "GROUP";


    public static SetLoanPaidDialog newInstance(@Nullable Client client, @Nullable Group group) {
        final SetLoanPaidDialog dialog = new SetLoanPaidDialog();

        final Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT, client);
        args.putSerializable(ARG_GROUP, group);

        dialog.setArguments(args);

        return dialog;
    }

    private SetLoanPaidCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_loan_paid);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null.");

        final Client client = (Client) args.getSerializable(ARG_CLIENT);
        final Group group = (Group) args.getSerializable(ARG_GROUP);

        getBt(R.id.setLoanPaidDialogYesButton).setOnClickListener(view -> {
            callback.onLoanPaidCallBack(client, group);
            dismiss();
        });

        getBt(R.id.setLoanPaidDialogNoButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallback(SetLoanPaidCallback callback) {
        this.callback = callback;
    }
}
