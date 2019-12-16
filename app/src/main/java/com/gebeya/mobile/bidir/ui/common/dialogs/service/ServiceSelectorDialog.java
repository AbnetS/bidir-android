package com.gebeya.mobile.bidir.ui.common.dialogs.service;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel K. on 5/31/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ServiceSelectorDialog extends BaseDialog {

    private static final String ARG_POSITION = "POSITION";
    private static final String ARG_SERVICE_LIST = "SERVICES";

    public static ServiceSelectorDialog newInstance(int position, @NonNull ArrayList<String> serviceList) {
        final ServiceSelectorDialog dialog = new ServiceSelectorDialog();

        final Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putStringArrayList(ARG_SERVICE_LIST, serviceList);

        dialog.setArguments(args);
        return dialog;
    }

    private List<String> checkedServices;
    private LinearLayout checkBoxContainer;
    private ServiceSelectorCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkedServices = new ArrayList<>();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_services);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null.");

        final int position = args.getInt(ARG_POSITION, 0);

        final ArrayList<String> serviceList = args.getStringArrayList(ARG_SERVICE_LIST);
        if (serviceList == null) throw new NullPointerException("Services list is null");
        checkedServices.clear();
        checkedServices.addAll(serviceList);

        checkBoxContainer = getView(R.id.servicesCheckBoxContainer);
        initializeCheckBoxes();

        getBt(R.id.serviceDialogCancelButton).setOnClickListener(view -> {
            callback.onServiceSelectionCanceled(position);
            dismiss();
        });

        getBt(R.id.serviceDialogOKButton).setOnClickListener(view -> {
            callback.onServicesSelected(checkedServices, position);
            dismiss();
        });

        return createDialog();
    }

    private void initializeCheckBoxes() {
        final int length = checkBoxContainer.getChildCount();
        for (int i = 0; i < length; i++) {
            final CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
            final String text = checkBox.getText().toString().toLowerCase().trim();
            checkBox.setChecked(hasService(text));
            checkBox.setOnCheckedChangeListener(listener);
        }
    }

    private final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            final String text = buttonView.getText().toString().toLowerCase().trim();
            if (hasService(text)) {
                checkedServices.remove(text);
            } else {
                checkedServices.add(text);
            }
        }
    };

    private boolean hasService(@NonNull String text) {
        for (String s : checkedServices) {
            if (s.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    public void setCallback(@NonNull ServiceSelectorCallback callback) {
        this.callback = callback;
    }
}
