package com.gebeya.mobile.bidir.ui.common.dialogs.question;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Dialog used to pose a confirmation message in form of a question.
 */
public class QuestionDialog extends BaseDialog {

    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_MESSAGE = "MESSAGE";
    private static final String ARG_POSITIVE = "POSITIVE";
    private static final String ARG_NEGATIVE = "NEGATIVE";

    public static QuestionDialog newInstance(@NonNull String title, @NonNull String message) {
        return newInstance(title, message, null, null);
    }

    public static QuestionDialog newInstance(@NonNull String title, @NonNull String message,
                                             @Nullable String positive) {
        return newInstance(title, message, positive, null);
    }

    private QuestionDialogCallback callback;

    public static QuestionDialog newInstance(@NonNull String title,
                                             @NonNull String message,
                                             @Nullable String positive,
                                             @Nullable String negative) {
        QuestionDialog dialog = new QuestionDialog();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE, positive);
        args.putString(ARG_NEGATIVE, negative);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_question);

        final Bundle bundle = getArguments();
        if (bundle == null) throw new NullPointerException("Bundle is null");
        final String title = bundle.getString(ARG_TITLE);
        if (title == null) throw new NullPointerException("Title is null");
        final String message = bundle.getString(ARG_MESSAGE);
        if (message == null) throw new NullPointerException("Message is null");
        final String positive = bundle.getString(ARG_POSITIVE);
        final String negative = bundle.getString(ARG_NEGATIVE);

        getTv(R.id.questionDialogTitleLabel).setText(title);
        getTv(R.id.questionDialogMessageLabel).setText(message);

        final Button positiveButton = getBt(R.id.questionDialogPositiveButton);
        if (positive != null) positiveButton.setText(positive);
        positiveButton.setOnClickListener(v -> {
            callback.onQuestionPositiveClicked();
            dismiss();
        });

        final Button negativeButton = getBt(R.id.questionDialogNegativeButton);
        if (negative != null) negativeButton.setText(negative);
        negativeButton.setOnClickListener(v -> {
            callback.onQuestionNegativeClicked();
            dismiss();
        });

        return createDialog();
    }

    public void setCallback(@NonNull QuestionDialogCallback callback) {
        this.callback = callback;
    }
}