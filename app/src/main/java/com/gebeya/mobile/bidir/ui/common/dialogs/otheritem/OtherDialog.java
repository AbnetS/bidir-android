package com.gebeya.mobile.bidir.ui.common.dialogs.otheritem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Dialog used to put ACAT Item that was not available during the ACAT Init.
 *
 */
public class OtherDialog extends BaseDialog {

    private static final String ARG_IS_CHILD = "IS CHILD";
    private static final String ARG_GROUP_NAME = "GROUP_NAME";

    public static OtherDialog newInstance(@Nullable String groupName, boolean isChild) {
        final OtherDialog dialog = new OtherDialog();

        final Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CHILD, isChild);
        args.putString(ARG_GROUP_NAME, groupName);

        dialog.setArguments(args);
        return dialog;
    }

    private OtherCallBack callBack;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_other);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null");

        final boolean isChild = args.getBoolean(ARG_IS_CHILD);
        final String groupName = args.getString(ARG_GROUP_NAME);

        final EditText acatItemInput = getEd(R.id.otherDialogACATItemInput);
        final EditText acatItemUnitInput = getEd(R.id.otherDialogACATItemUnitInput);

        getBt(R.id.otherDialogOKButton).setOnClickListener(v -> {
            final String item = acatItemInput.getText().toString().trim();
            final String itemUnit = acatItemUnitInput.getText().toString().trim();

            if (item.isEmpty()) {
                toast(getString(R.string.other_acat_item_empty_prompt));
                acatItemInput.requestFocus();
                return;
            } else if (itemUnit.isEmpty()) {
                toast(getString(R.string.other_acat_item_unit_empty_prompt));
                acatItemUnitInput.requestFocus();
                return;
            }

            callBack.onOtherReturned(item, itemUnit, groupName, isChild);
            dismiss();
        });

        getBt(R.id.otherDialogCancelButton).setOnClickListener(v -> dismiss());
        return createDialog();
    }

    public void setCallBack(@NonNull OtherCallBack callBack) {
        this.callBack = callBack;
    }
}
