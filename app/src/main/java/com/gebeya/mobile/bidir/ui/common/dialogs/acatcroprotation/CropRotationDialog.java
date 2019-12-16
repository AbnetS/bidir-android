package com.gebeya.mobile.bidir.ui.common.dialogs.acatcroprotation;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Created by Samuel K. on 7/4/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropRotationDialog extends BaseDialog {

    private static final String ARG_PREVIOUS_CROP_NAME = "previousCropName";
    private static final String ARG_NEXT_CROP_NAME = "nextCropName";

    public static CropRotationDialog newInstance(String previousCropName, String nextCropName) {
        CropRotationDialog dialog = new CropRotationDialog();
        Bundle arg = new Bundle();
        arg.putString(ARG_PREVIOUS_CROP_NAME, previousCropName);
        arg.putString(ARG_NEXT_CROP_NAME, nextCropName);
        dialog.setArguments(arg);
        return dialog;
    }

    private final static String FIRST_SENTENCE = "You have successfully filled  ACAT for ";
    private final static String SECOND_SENTENCE = " Please proceed with filling ACAT for ";
    private TextView cropLabel, dialogTitleLabel;
    private CropRotationCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_crop_rotation_layout);

        Bundle arg = getArguments();
        if (arg == null) throw new NullPointerException("Arg cannot be null.");

        final String previousCropItem = arg.getString(ARG_PREVIOUS_CROP_NAME).toUpperCase();
        final String nextCropItem = arg.getString(ARG_NEXT_CROP_NAME).toUpperCase();
        final String dialogText = FIRST_SENTENCE + previousCropItem + SECOND_SENTENCE + nextCropItem;

        cropLabel = getTv(R.id.dialog_label);
        dialogTitleLabel = getTv(R.id.dialog_crop_title);

        cropLabel.setText(dialogText);
        dialogTitleLabel.setText(previousCropItem);

        getBt(R.id.acatDialogOkButton).setOnClickListener(view -> {
            callback.onRotationCallBack();
            dismiss();
        });

        getBt(R.id.acatDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallback(@NonNull CropRotationCallback callback) {
        this.callback = callback;
    }
}
