package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATPreliminaryInfoItemHolder extends RecyclerView.ViewHolder implements ACATPreliminaryInfoContract.QuestionItemView {

    private final ACATPreliminaryInfoContract.Presenter presenter;

    // Crop main information
    private TextView cropNameLabel;
    private Spinner firstExpenseMonthSpinner;
    private EditText croppingAreaInput;
    private SwitchCompat nonFinancialResourceSwitch;
    private Button nonFinancialResourceDetailsButton;

    // Crop location/GPS information
    private ImageView gpsLocationIcon;
    private TextView gpsLocationLabel;
    private Button addEditLocationButton;

    public ACATPreliminaryInfoItemHolder(View itemView, ACATPreliminaryInfoContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;

        cropNameLabel = itemView.findViewById(R.id.acatPreliminaryInfoCropLabel);
        firstExpenseMonthSpinner = itemView.findViewById(R.id.acatFirstExpenseMonthSpinner);
        firstExpenseMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) parent.getItemAtPosition(position);
                presenter.onFirstExpenseMonthSelected(text, getAdapterPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        croppingAreaInput = itemView.findViewById(R.id.acatCroppingAreaInput);
        croppingAreaInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                final String answer = editable.toString().trim();
                presenter.onCroppingAreaChanged(answer, getAdapterPosition());
            }
        });
        nonFinancialResourceSwitch = itemView.findViewById(R.id.acatSwitchCompatToggle);
        nonFinancialResourceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            final boolean checked = nonFinancialResourceSwitch.isChecked();
            presenter.onNonFinancialServiceToggled(checked, getAdapterPosition());
        });
//        nonFinancialResourceSwitch.setOnClickListener(v -> {
//            final boolean checked = nonFinancialResourceSwitch.isChecked();
//            presenter.onNonFinancialServiceToggled(checked, getAdapterPosition());
//        });

        nonFinancialResourceDetailsButton = itemView.findViewById(R.id.financial_resource_detail_button);
        nonFinancialResourceDetailsButton.setOnClickListener(v -> presenter.onDetailButtonPressed(getAdapterPosition()));

        gpsLocationIcon = itemView.findViewById(R.id.acatGPSLocationIcon);
        gpsLocationLabel = itemView.findViewById(R.id.acatGPSLocationLabel);
        addEditLocationButton = itemView.findViewById(R.id.acatGPSLocationAddEditButton);
        addEditLocationButton.setOnClickListener(v -> presenter.onGPSLocationPressed(getAdapterPosition()));
    }

    @Override
    public void setCropName(@NonNull String cropName) {
        cropNameLabel.setText(cropName);
    }

    @Override
    public void setFirstExpenseMonth(int position) {
        firstExpenseMonthSpinner.setSelection(position);
    }

    @Override
    public void setCroppingArea(@NonNull String croppingAreaValue) {
        croppingAreaInput.setText(croppingAreaValue);
    }

    @Override
    public void toggleNonFinServices(boolean enabled) {
        nonFinancialResourceSwitch.setOnCheckedChangeListener(null);
        nonFinancialResourceSwitch.setChecked(enabled);
        nonFinancialResourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final boolean checked = nonFinancialResourceSwitch.isChecked();
                presenter.onNonFinancialServiceToggled(checked, getAdapterPosition());
            }
        });
        nonFinancialResourceDetailsButton.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showDetailButton(boolean show) {
        nonFinancialResourceDetailsButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleButtonLocation(boolean enabled) {
        gpsLocationIcon.setImageResource(enabled ?
                R.drawable.register_location_enabled_icon :
                R.drawable.register_location_disabled_icon);
    }

    @Override
    public void setLocationInfo(int count) {
        if (count == 0) {
            gpsLocationLabel.setText(R.string.acat_gps_location_label);
        } else {
            final String text = itemView.getContext().getString(
                    R.string.acat_gps_locations_present_label, count, count == 1 ? "" : "s");
            gpsLocationLabel.setText(text);
        }
    }

    @Override
    public void toggleLocationAddEditButton(boolean edit) {
        addEditLocationButton.setText(edit ? R.string.acat_gps_location_edit_button : R.string.acat_gps_location_add_button);
    }
}
