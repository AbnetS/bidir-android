package com.gebeya.mobile.bidir.ui.common.dialogs.error;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;


/**
 * Created by Samuel K. on 8/16/2018.
 * <p>
 * samkura47@gmail.com
 */

public class TempErrorDialog extends BaseDialog {
    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_MESSAGE = "MESSAGE";
    private static final String ARG_EXTRA = "EXTRA";

    public static TempErrorDialog newInstance(@NonNull String title, @NonNull String message) {
        return newInstance(title, message, null);
    }

    public static TempErrorDialog newInstance(@NonNull  String title, @NonNull String message, @Nullable String extra) {
        TempErrorDialog dialog = new TempErrorDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_EXTRA, extra);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_error);

        Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args cannot be null");
        final String title = args.getString(ARG_TITLE);

        if (title == null) throw new NullPointerException("ErrorDialog title cannot be null");
        final String message = args.getString(ARG_MESSAGE);

        if (message == null) throw new NullPointerException("ErrorDialog message cannot be null");
        final String extra = args.getString(ARG_EXTRA);

        getTv(R.id.errorDialogTitleLabel).setText(title);
        getTv(R.id.errorDialogMessageLabel).setText(message);

        final View extraContainer = getView(R.id.errorDialogExtraContainer);
        if (extra == null) {
            extraContainer.setVisibility(View.GONE);
        } else {
            final TextView viewLabel = getTv(R.id.errorDialogViewLabel);
            final TextView extraLabel = getTv(R.id.errorDialogExtraLabel);
            extraContainer.setOnClickListener(v -> {
                boolean visible = extraLabel.getVisibility() == View.VISIBLE;
                final int text = visible ? R.string.error_view_more_label : R.string.error_view_less_label;
                viewLabel.setText(text);
                extraLabel.setVisibility(visible ? View.GONE : View.VISIBLE);
            });

            extraLabel.setText(extra);
            extraLabel.setVisibility(View.VISIBLE);
        }

        getBt(R.id.errorDialogOkayButton).setOnClickListener(v -> dismiss());
        return createDialog();
    }
}

