package com.gebeya.mobile.bidir.ui.common.dialogs.addgroupmember;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

public class AddGroupMember extends BaseDialog {

    public static AddGroupMember newInstance() {
        return new AddGroupMember();
    }

    private AddGroupMemberCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_add_group_member);

        final RadioGroup radioGroup = getView(R.id.addGroupMemberRadioGroup);

        getBt(R.id.addGroupMemberDialogCancelButton).setOnClickListener(view -> dismiss());

        getBt(R.id.addGroupMemberDialogOKButton).setOnClickListener(view -> {
            boolean isNew = radioGroup.getCheckedRadioButtonId() == R.id.addGroupMemberNewChoice;

            callback.onAdd(isNew);
            dismiss();
        });

        return createDialog();
    }

    public void setCallback(@NonNull AddGroupMemberCallback callback) {
        this.callback = callback;
    }
}
