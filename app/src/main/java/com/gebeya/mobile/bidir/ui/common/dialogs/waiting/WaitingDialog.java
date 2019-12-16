package com.gebeya.mobile.bidir.ui.common.dialogs.waiting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class WaitingDialog extends BaseDialog {

    private static final String ARG_MESSAGE = "MESSAGE";

    public static WaitingDialog newInstance(@NonNull String message) {
        WaitingDialog dialog = new WaitingDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_waiting);

        Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args cannot be null");

        String message = args.getString(ARG_MESSAGE);
        if (message == null) throw new NullPointerException("Dialog message cannot be null");

        getTv(R.id.waitingProgressLabel).setText(message);

        return createDialog();
    }
}
