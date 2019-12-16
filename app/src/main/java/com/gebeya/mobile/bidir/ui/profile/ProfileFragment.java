package com.gebeya.mobile.bidir.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.register.RegisterActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

/**
 * Created by Samuel K. on 12/26/2017.
 * <p>
 * samkura47@gmail.com
 */

public class ProfileFragment extends BaseFragment implements ProfileContract.View {

    @NonNull
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private ProfileContract.Presenter presenter;

    private ImageView personalImageView;
    private TextView profileClientName;
    private TextView profileLoanCount;
    private TextView profileLatLocationLabel;
    private ImageView idCardImageView;

    private TextView fullNameValue;
    private TextView genderValue;
    private TextView birthDateValue;
    private TextView idValue;
    private TextView woredaValue;
    private TextView houseNumberValue;
    private TextView kebeleValue;
    private TextView phoneNumberValue;

    private TextView maritalStatusValue;
    private TextView houseHoldCountValue;

    private View spouseInfoContainer;
    private TextView spouseIdValue;
    private TextView spouseFullNameValue;

    private Toolbar toolbar;
    private View editButtonContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = getView(R.id.profileFragmentToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);
        toolbar.setNavigationOnClickListener(v -> presenter.onBackPressed());

        getView(R.id.profile_edit_button).setOnClickListener(fabClickListener);

        editButtonContainer = getView(R.id.profileEditFabContainer);
        editButtonContainer.setOnClickListener(fabClickListener);

        initializePersonalDetailsUI();
        initializeFamilyStatusUI();

        return root;
    }

    private View.OnClickListener fabClickListener = view -> presenter.onEditClientPressed();

    private void initializePersonalDetailsUI() {
        fullNameValue = getTv(R.id.profile_label_full_name_value);
        genderValue = getTv(R.id.profile_label_gender_value);
        birthDateValue = getTv(R.id.profile_label_birth_date_value);
        idValue = getTv(R.id.profile_label_id_value);
        woredaValue = getTv(R.id.profile_label_woreda_value);
        houseNumberValue = getTv(R.id.profile_label_house_number_value);
        kebeleValue = getTv(R.id.profile_label_kebele_value);
        phoneNumberValue = getTv(R.id.profile_label_phone_number_value);

        personalImageView = getView(R.id.profile_image_view);
        profileLoanCount = getTv(R.id.profileLoanCount);
        profileClientName = getTv(R.id.profileClientName);
        idCardImageView = getView(R.id.profile_id_holder);
        profileLatLocationLabel = getTv(R.id.profileLatLocationLabel);
    }

    private void initializeFamilyStatusUI() {
        maritalStatusValue = getTv(R.id.profile_label_marital_status_value);
        houseHoldCountValue = getTv(R.id.profile_label_household_count_value);

        spouseInfoContainer = getView(R.id.profile_spouse_info_container);
        spouseIdValue = getTv(R.id.profile_label_spouse_id_value);
        spouseFullNameValue = getTv(R.id.profile_label_spouse_full_name_value);
    }

    @Override
    public void openEditClientScreen(@NonNull Client client) {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.putExtra(RegisterActivity.ARG_CLIENT, client);
        startActivity(intent);
    }

    @Override
    public void toggleEditClientButton(boolean visible) {
        editButtonContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void toggleSpouseDetails(boolean enabled) {
        spouseInfoContainer.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setName(@NonNull String name) {
        profileClientName.setText(name);
    }

    @Override
    public void setLoanCount(int count) {
        int resId = count != 1 ? R.string.profile_loan_count_many : R.string.profile_loan_count;
        final String text = getString(resId, count);
        profileLoanCount.setText(text);
    }

    @Override
    public void setLocation(@NonNull String location) {
        profileLatLocationLabel.setText(location);
    }

    @Override
    public void showNoLocationAvailable() {
        profileLatLocationLabel.setText(R.string.profile_no_location_text);
    }

    @Override
    public void setFullName(@NonNull String fullName) {
        fullNameValue.setText(fullName);
    }

    @Override
    public void setGender(boolean male) {
        genderValue.setText(male ? R.string.personal_details_gender_male_choice :
                R.string.personal_details_gender_female_choice);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setPersonalPhoto(@NonNull String photoUrl) {
        Picasso                             // TODO: Move this into utility class
                .with(getContext())
                .load(photoUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedTransformationBuilder()
                        .borderWidthDp(1)
                        .borderColor(
                                ContextCompat.getColor(
                                        getActivity(),
                                        R.color.gray_dark
                                ))
                        .cornerRadiusDp(60)
                        .oval(false)
                        .build())
                .error(R.drawable.register_personal_details_picture_sample)
                .into(personalImageView);
    }

    @Override
    public void setBirthDate(@NonNull String birthDate) {
        birthDateValue.setText(birthDate);
    }

    @Override
    public void setMaritalStatus(@NonNull MaritalStatus type) {
        int resId = getMaritalStatusString(type);
        maritalStatusValue.setText(resId);
    }

    @StringRes
    private int getMaritalStatusString(@NonNull MaritalStatus type) {
        switch (type) {
            case MARRIED:
                return R.string.family_status_married_choice;
            case DIVORCED:
                return R.string.family_status_divorced_choice;
            case WIDOWED:
                return R.string.family_status_widowed_choice;
            default:
                return R.string.family_status_single_choice;
        }
    }

    @Override
    public void setNumberOfHousehold(int household) {
        houseHoldCountValue.setText(String.valueOf(household));
    }

    @Override
    public void setSpouseIdNumber(@NonNull String idNumber) {
        spouseIdValue.setText(idNumber);
    }

    @Override
    public void setSpouseFullName(@NonNull String fullName) {
        spouseFullNameValue.setText(fullName);
    }

    @Override
    public void setIdCardPhoto(@NonNull String idCardPhotoUrl) {
        Picasso
                .with(getContext())
                .load(idCardPhotoUrl)
                .fit()
                .centerCrop()
                .into(idCardImageView);
    }

    @Override
    public void setIdNumber(@NonNull String idNumber) {
        idValue.setText(idNumber);
    }

    @Override
    public void setWoreda(@NonNull String woreda) {
        woredaValue.setText(woreda);
    }

    @Override
    public void setHouseNumber(@NonNull String houseNumber) {
        houseNumberValue.setText(houseNumber);
    }

    @Override
    public void setKebele(@NonNull String kebele) {
        kebeleValue.setText(kebele);
    }

    @Override
    public void setPhoneNumber(@NonNull String phoneNumber) {
        phoneNumberValue.setText(phoneNumber);
    }

    @Override
    public void attachPresenter(ProfileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
