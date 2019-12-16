package com.gebeya.mobile.bidir.ui.common.dialogs.message;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Dialog for displaying a simple confirmation message
 */
public class MessageDialog extends BaseDialog {

    public static final String ARG_TITLE = "TITLE";
    public static final String ARG_MESSAGE = "MESSAGE";

    public static MessageDialog newInstance(@Nullable String title, @NonNull String message) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    private MessageDialogCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_message);

        Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args cannot be null");

        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MESSAGE);

        if (message == null) throw new NullPointerException("MessageDialog message cannot be null");

        TextView titleLabel = getTv(R.id.messageDialogTitleLabel);
        if (title == null) {
            titleLabel.setVisibility(View.GONE);
        } else {
            titleLabel.setText(title);
        }

        getTv(R.id.messageDialogMessageLabel).setText(message);

        getBt(R.id.messageDialogOkayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMessageDialogDismissed();
                dismiss();
            }
        });

        return createDialog();
    }

    public void setCallback(@NonNull MessageDialogCallback callback) {
        this.callback = callback;
    }
}
