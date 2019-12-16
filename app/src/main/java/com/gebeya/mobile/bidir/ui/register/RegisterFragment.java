package com.gebeya.mobile.bidir.ui.register;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.ui.common.dialogs.dateselector.DateSelectorCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.dateselector.DateSelectorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.message.MessageDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.message.MessageDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.question.QuestionDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.question.QuestionDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.GoogleApi;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * RegisterDto View implementation via Fragment.
 */
public class RegisterFragment extends BaseFragment implements
        RegisterContract.View,
        DateSelectorCallback,
        MessageDialogCallback,
        ErrorDialogCallback,
        QuestionDialogCallback {

    public static final int REQUEST_PERSONAL_PICTURE = 1000;
    public static final int REQUEST_ID_CARD_PICTURE = 1001;
    public static final int REQUEST_GPS_PERMISSION = 1002;
    private static final int REQUEST_PLAY_SERVICES = 2000;

    private static final int QUESTION_MODE_REQUEST_GPS_PERMISSION = 100;
    private static final int QUESTION_MODE_REQUEST_GPS_ENABLE = 101;
    private static final int QUESTION_MODE_COMPLETE_REGISTRATION = 102;
    private static final int QUESTION_MODE_CANCEL_UPDATE_REGISTRATION = 103;

    private static final String ARG_SHOWN_PLAY_SERVICES_PROMPT = "SHOWN_PLAY_SERVICES_PROMPT";

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    private RegisterContract.Presenter presenter;

    private Toolbar toolbar;
    private ImageView menuToggleLocationIcon;
    private View locationUpdateContainer;
    private TextView gpsLocationLabel;
    private View locationUpdateIcon;

    private ImageView personalImageView;
    private Button personalDetailsTakePictureButton;
    private Button addressDetailTakeIdPictureButton;
    private EditText personalNameInput;
    private EditText personalFatherNameInput;
    private EditText personalGrandfatherNameInput;
    private RadioGroup genderGroup;
    private TextView dateOfBirthLabel;

    private LinearLayout maritalStatusGroup;
    private EditText householdMemberInput;

    private ImageView idCardImageView;
    private EditText idNumberInput;
    private EditText woredaInput;
    private EditText kebeleInput;
    private EditText houseNumberInput;
    private EditText phoneNumberInput;
    private View locationContainer;
    private TextView locationValueLabel;

    private EditText spouseIdNumberInput;
    private EditText spouseNameInput;
    private EditText spouseFatherNameInput;
    private EditText spouseGrandfatherNameInput;
    private TextView mandatoryIndicatorId;
    private TextView mandatoryIndicatorName;
    private TextView mandatoryIndicatorFatherName;
    private TextView mandatoryIndicatorGrandfatherName;

    private WaitingDialog waitingDialog;
    private MessageDialog messageDialog;
    private ErrorDialog errorDialog;
    private QuestionDialog questionDialog;

    @Nullable private String personalPicturePath = null;
    @Nullable private String idCardPicturePath = null;

    private int pictureRequestCode;
    private int permissionRequestCode;
    private int[] grantResults;
    private int questionMode;
    private boolean fromGpsSettings;

    private boolean shownPlayServicesPrompt = false;

    // TODO: Add tapping on image (profile and id) to show larger version
    private Handler handler;

    private Runnable task = () -> {
        boolean visible = locationUpdateIcon.getVisibility() == View.VISIBLE;
        locationUpdateIcon.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        handler.postDelayed(this.task, Constants.ANIMATION_DURATION);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        if (savedInstanceState == null) return;
        shownPlayServicesPrompt = savedInstanceState.getBoolean(ARG_SHOWN_PLAY_SERVICES_PROMPT, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ARG_SHOWN_PLAY_SERVICES_PROMPT, true);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register, container, false);

        toolbar = getView(R.id.registerFragmentToolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        locationUpdateContainer = getView(R.id.registerLocationUpdateContainer);
        gpsLocationLabel = getTv(R.id.registerLocationUpdateLabel);
        locationUpdateIcon = getView(R.id.registerLocationUpdateIcon);

        toolbar.setNavigationOnClickListener(v -> presenter.onBackPressed());
        getTv(R.id.registerMenuSave, toolbar).setOnClickListener(v -> presenter.onSavePressed());

        menuToggleLocationIcon = getView(R.id.registerMenuToggleLocation);
        menuToggleLocationIcon.setOnClickListener(v -> presenter.onMenuToggleLocationPressed());

        initializePersonalDetailsUI();
        initializeFamilyStatusUI();
        initializeAddressUI();
        initializeSpouseDetailsUI();


        return root;
    }

    @Override
    public void setTitle(boolean editing) {
        toolbar.setTitle(editing ? R.string.register_fragment_edit_title : R.string.register_fragment_title);
    }

    @Override
    public boolean hasGpsPermission() {
        int check = ContextCompat.checkSelfPermission(getParent(), Manifest.permission.ACCESS_FINE_LOCATION);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestGpsPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_GPS_PERMISSION
        );
    }

    @Override
    public boolean needsGpsPermissionExplanation() {
        return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showGpsPermissionExplanation() {
        final String title = getString(R.string.gps_request_explanation_title);
        final String message = getString(R.string.gps_request_explanation_message);
        questionMode = QUESTION_MODE_REQUEST_GPS_PERMISSION;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    public void hideGpsExplanation() {
        if (questionDialog != null) {
            try {
                questionDialog.dismiss();
                questionDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showGpsPermissionDeniedError() {
        toast(R.string.gps_request_denied_message);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void requestGpsEnable() {
        final String title = getString(R.string.gps_enable_title);
        final String message = getString(R.string.gps_enable_message);
        questionMode = QUESTION_MODE_REQUEST_GPS_ENABLE;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    public void openGpsSettings() {
        fromGpsSettings = true;
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void toggleLocationUpdate(boolean animate) {
        if (animate) {
            handler.postDelayed(task, Constants.ANIMATION_DURATION);
        } else {
            handler.removeCallbacks(task);
        }

        locationUpdateContainer.setVisibility(animate ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setGpsCoordinates(double latitude, double longitude) {
        final String location = getString(R.string.personal_details_updating_location_label,
                latitude, longitude
        );
        gpsLocationLabel.setText(location);
    }

    @Override
    public void toggleLocationContainer(boolean show) {
        locationContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoLocationAvailable() {
        locationValueLabel.setText(R.string.profile_no_location_text);
    }

    @Override
    public void showPreviousLocation(@NonNull String location) {
        locationValueLabel.setText(location);
    }

    @Override
    public void toggleMenuLocation(boolean enabled) {
        menuToggleLocationIcon.setImageResource(enabled ?
                R.drawable.register_location_enabled_icon :
                R.drawable.register_location_disabled_icon);
    }

    @Override
    public void onQuestionPositiveClicked() {
        switch (questionMode) {
            case QUESTION_MODE_REQUEST_GPS_PERMISSION:
                presenter.onGpsPermissionExplanationReturned(true);
                break;
            case QUESTION_MODE_REQUEST_GPS_ENABLE:
                presenter.onGpsEnableRequestReturned(true);
                break;
            case QUESTION_MODE_COMPLETE_REGISTRATION:
                presenter.onCompleteRegistrationPromptReturned(true);
                break;
            case QUESTION_MODE_CANCEL_UPDATE_REGISTRATION:
                presenter.onCancelRegistrationDismissed();
                break;
        }
    }

    @Override
    public void onQuestionNegativeClicked() {
        switch (questionMode) {
            case QUESTION_MODE_REQUEST_GPS_PERMISSION:
                presenter.onGpsPermissionExplanationReturned(false);
                break;
            case QUESTION_MODE_REQUEST_GPS_ENABLE:
                presenter.onGpsEnableRequestReturned(false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionRequestCode = requestCode;
        this.grantResults = grantResults;
    }

    private MaritalStatus getMaritalStatusType() {
        for (int r = 0; r < maritalStatusGroup.getChildCount(); r++) {
            LinearLayout row = (LinearLayout) maritalStatusGroup.getChildAt(r);
            for (int c = 0; c < row.getChildCount(); c++) {
                CompoundButton button = (CompoundButton) row.getChildAt(c);
                if (button.isChecked()) {
                    switch (button.getId()) {
                        case R.id.familyStatusMarriedChoice:
                            return MaritalStatus.MARRIED;
                        case R.id.familyStatusDivorcedChoice:
                            return MaritalStatus.DIVORCED;
                        case R.id.familyStatusWidowedChoice:
                            return MaritalStatus.WIDOWED;
                        default:
                            return MaritalStatus.SINGLE;
                    }
                }
            }
        }
        return MaritalStatus.SINGLE;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showRegistrationSuccess() {
        final String title = getString(R.string.register_success_title);
        final String message = getString(R.string.register_success_message);
        messageDialog = MessageDialog.newInstance(title, message);
        messageDialog.setCallback(this);
        messageDialog.show(getFragmentManager(), null);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showUpdateSuccess() {
        final String title = getString(R.string.update_success_title);
        final String message = getString(R.string.update_success_message);
        messageDialog = MessageDialog.newInstance(title, message);
        messageDialog.setCallback(this);
        messageDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onMessageDialogDismissed() {
        presenter.onSuccessDismissed();
    }

    @Override
    public void hideSuccess() {
        if (messageDialog != null) {
            try {
                messageDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializePersonalDetailsUI() {
        getTv(R.id.personalDetailsSectionTitleLabel);
        Button personalDetailsUploadPhotoButton = getBt(R.id.personalDetailsUploadPhotoButton);
        personalDetailsUploadPhotoButton.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(getParent(), R.drawable.register_upload_icon),
                null,
                null,
                null
        );

        personalDetailsUploadPhotoButton.setOnClickListener(v -> presenter.onUploadPhotoPressed());

        personalImageView = getView(R.id.personalDetailsPhotoIcon);
        personalDetailsTakePictureButton = getBt(R.id.personalDetailTakePictureButton);
        personalDetailsTakePictureButton.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(getParent(), R.drawable.register_take_picture_icon),
                null,
                null,
                null
        );

        personalDetailsTakePictureButton.setOnClickListener(v -> presenter.onTakePicturePressed());

        personalNameInput = getEd(R.id.personalDetailsNameInput);
        personalNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onNameChanged(s.toString().trim());
            }
        });
        personalFatherNameInput = getEd(R.id.personalDetailsFatherNameInput);
        personalFatherNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onFatherNameChanged(s.toString().trim());
            }
        });
        personalGrandfatherNameInput = getEd(R.id.personalDetailsGrandfatherNameInput);
        personalGrandfatherNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onGrandfatherNameChanged(s.toString().trim());
            }
        });

        genderGroup = getView(R.id.personalDetailsGenderGroup);
        genderGroup.setOnCheckedChangeListener((group, checkedId) ->
                presenter.onGenderChanged(checkedId == R.id.personalDetailsGenderMaleChoice)
        );
        dateOfBirthLabel = getTv(R.id.personalDetailsDateOfBirthLabel);
        getBt(R.id.personalDetailsSelectDobButton).setOnClickListener(v -> presenter.onSelectDatePressed());
    }

    @Override
    public void openDateSelector(int day, int month, int year) {
        DateSelectorDialog dialog = DateSelectorDialog.newInstance(day, month, year);
        dialog.setCallback(RegisterFragment.this);
        dialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onDateSelected(int day, int month, int year) {
        presenter.onDateOfBirthChanged(day, month, year);
    }

    @Override
    public void updateDateOfBirth(@NonNull String date) {
        dateOfBirthLabel.setText(date);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showRegisterProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.register_progress_label));
        waitingDialog.show(getFragmentManager(), null);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showUpdateProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.register_progress_update_label));
        waitingDialog.show(getFragmentManager(), null);
    }

    @Override
    public void hideProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showRegisterError(@NonNull Result result) {
        String message = getMessage(result.message);
        if (message == null) {
            switch (result.message) {
                case ApiErrors.REGISTER_ERROR_CLIENT_ALREADY_EXISTS:
                    message = getString(R.string.register_error_client_already_exists);
                    break;
                default:
                    message = getString(R.string.register_error_generic);
            }
        }
        errorDialog = ErrorDialog.newInstance(getString(R.string.register_error_title), message, result.extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showUpdateError(@NonNull Result result) {
        String message = getMessage(result.message);
        if (message == null) {
            switch (result.message) {
                case ApiErrors.UPDATE_ERROR_GENERIC:
                    message = getString(R.string.register_error_update_generic);
                    break;
                default:
                    message = getString(R.string.register_error_update_generic);
            }
        }
        errorDialog = ErrorDialog.newInstance(getString(R.string.register_error_update_title), message, result.extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void hideError() {
        if (errorDialog != null) {
            try {
                errorDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeFamilyStatusUI() {
        maritalStatusGroup = getView(R.id.familyStatusMaritalGroup);

        getTv(R.id.familyStatusSingleChoice).setOnClickListener(maritalCheckListener);
        getTv(R.id.familyStatusMarriedChoice).setOnClickListener(maritalCheckListener);
        getTv(R.id.familyStatusDivorcedChoice).setOnClickListener(maritalCheckListener);
        getTv(R.id.familyStatusWidowedChoice).setOnClickListener(maritalCheckListener);

        householdMemberInput = getEd(R.id.familyStatusHouseholdMemberInput);
        householdMemberInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onHouseholdMemberCountChanged(s.toString().trim());
            }
        });
    }

    private final View.OnClickListener maritalCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int r = 0; r < maritalStatusGroup.getChildCount(); r++) {
                LinearLayout row = (LinearLayout) maritalStatusGroup.getChildAt(r);
                for (int c = 0; c < row.getChildCount(); c++) {
                    CompoundButton button = (CompoundButton) row.getChildAt(c);
                    button.setChecked(button.getId() == v.getId());
                }
            }
            final MaritalStatus maritalStatus = getMaritalStatusType();
            presenter.onMaritalStatusChanged(maritalStatus);
        }
    };

    @Override
    public void toggleSpouseDetails(boolean enabled) {
        int visibility = enabled ? View.VISIBLE : View.INVISIBLE;

        mandatoryIndicatorId.setVisibility(visibility);
        spouseIdNumberInput.setVisibility(visibility);

        mandatoryIndicatorName.setVisibility(visibility);
        spouseNameInput.setVisibility(visibility);

        mandatoryIndicatorFatherName.setVisibility(visibility);
        spouseFatherNameInput.setVisibility(visibility);

        mandatoryIndicatorGrandfatherName.setVisibility(visibility);
        spouseGrandfatherNameInput.setVisibility(visibility);
    }

    private void initializeAddressUI() {
        idCardImageView = getView(R.id.addressUploadIdCardIcon);
        Button addressUploadIdCardButton = getBt(R.id.addressUploadIdCardButton);

        addressUploadIdCardButton.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(getParent(), R.drawable.register_upload_icon),
                null,
                null,
                null
        );
        addressUploadIdCardButton.setOnClickListener(v -> presenter.onUploadIdCardPressed());

        addressDetailTakeIdPictureButton = getBt(R.id.addressTakeIdPictureButton);

        addressDetailTakeIdPictureButton.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(getParent(), R.drawable.register_take_picture_icon),
                null,
                null,
                null);
        addressDetailTakeIdPictureButton.setOnClickListener(v -> presenter.onTakeIdPicturePressed());
        idNumberInput = getEd(R.id.addressIdNumberInput);
        idNumberInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onIdNumberChanged(s.toString().trim());
            }
        });
        woredaInput = getEd(R.id.addressWoredaInput);
        woredaInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onWoredaChanged(s.toString().trim());
            }
        });
        kebeleInput = getEd(R.id.addressKebeleInput);
        kebeleInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onKebeleChanged(s.toString().trim());
            }
        });
        houseNumberInput = getEd(R.id.addressHouseNumberInput);
        houseNumberInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onHouseNumberChanged(s.toString().trim());
            }
        });
        phoneNumberInput = getEd(R.id.addressPhoneNumberInput);
        phoneNumberInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onPhoneNumberChanged(s.toString().trim());
            }
        });

        getTv(R.id.addressCurrentLocationLabel);
        locationContainer = getView(R.id.addressCurrentLocationContainer);
        locationValueLabel = getTv(R.id.addressCurrentLocationValueLabel);
    }

    @Override
    public void openIdCardPicker() {
        pictureRequestCode = REQUEST_ID_CARD_PICTURE;
        openImagePicker(getString(R.string.register_select_id_card_picker_prompt));
    }

    @Override
    public void openUploadPhotoPicker() {
        pictureRequestCode = REQUEST_PERSONAL_PICTURE;
        openImagePicker(getString(R.string.register_select_photo_picker_prompt));
    }

    @Override
    public void openTakePictureCamera() {
        pictureRequestCode = REQUEST_PERSONAL_PICTURE;
        openCamera();
    }

    @Override
    public void openTakeIdPictureCamera() {
        pictureRequestCode = REQUEST_ID_CARD_PICTURE;
        openCamera();
    }

    private void openImagePicker(@NonNull String title) {
        ImagePicker
                .create(this)
                .single()
                .toolbarImageTitle(title)
                .showCamera(false)
                .theme(R.style.ImagePickerTheme)
                .start();
    }

    private void openCamera() {
        ImagePicker
                .create(this)
                .single()
                .theme(R.style.ImagePickerTheme)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            final Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                final String path = image.getPath();
                final boolean isPersonal = pictureRequestCode == REQUEST_PERSONAL_PICTURE;
                personalPicturePath = isPersonal ? path : null;
                idCardPicturePath = isPersonal ? null : path;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.start();

        if (personalPicturePath != null) {
            presenter.onPersonalPictureReturned(personalPicturePath);
            personalPicturePath = null;
        }

        if (idCardPicturePath != null) {
            presenter.onIdCardPictureReturned(idCardPicturePath);
            idCardPicturePath = null;
        }

        pictureRequestCode = 0;

        if (permissionRequestCode == REQUEST_GPS_PERMISSION) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            presenter.onGpsPermissionRequestReturned(granted);
            permissionRequestCode = 0;
        }

        if (fromGpsSettings) {      // I think there might be a better way to do this
            presenter.startGps();
            fromGpsSettings = false;
        }

        presenter.start();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void updatePersonalPicture(@NonNull File file) {
        Picasso
                .with(getActivity())
                .load(file)
                .fit()
                .centerCrop()
                .transform(new RoundedTransformationBuilder()
                        .borderWidthDp(1)
                        .borderColor(
                                ContextCompat.getColor(
                                        getActivity(),
                                        R.color.gray_dark
                                ))
                        .cornerRadiusDp(80)
                        .oval(false)
                        .build())
                .error(R.drawable.register_personal_details_picture_sample)
                .into(personalImageView);
    }

    @Override
    public void updateIdCardPicture(@NonNull File file) {
        Picasso.with(getActivity())
                .load(file)
                .fit()
                .into(idCardImageView);
    }

    private void initializeSpouseDetailsUI() {
        spouseIdNumberInput = getEd(R.id.spouseDetailsIdNumberInput);
        spouseIdNumberInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onSpouseIdNumberChanged(s.toString().trim());
            }
        });
        spouseNameInput = getEd(R.id.spouseDetailsNameInput);
        spouseNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onSpouseNameChanged(s.toString().trim());
            }
        });
        spouseFatherNameInput = getEd(R.id.spouseDetailsFatherNameInput);
        spouseFatherNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onSpouseFatherNameChanged(s.toString().trim());
            }
        });
        spouseGrandfatherNameInput = getEd(R.id.spouseDetailsGrandfatherNameInput);
        spouseGrandfatherNameInput.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.onSpouseGrandfatherNameChanged(s.toString().trim());
            }
        });

        mandatoryIndicatorId = getTv(R.id.spouseDetailsMandatoryIndicatorId);
        mandatoryIndicatorName = getTv(R.id.spouseDetailsMandatoryIndicatorName);
        mandatoryIndicatorFatherName = getTv(R.id.spouseDetailsMandatoryIndicatorFatherName);
        mandatoryIndicatorGrandfatherName = getTv(R.id.spouseDetailsMandatoryIndicatorGrandfatherName);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setPersonalPhoto(@NonNull String url) {
        Picasso
                .with(getActivity())
                .load(url)
                .fit()
                .centerCrop()
                .transform(new RoundedTransformationBuilder()
                        .borderWidthDp(1)
                        .borderColor(
                                ContextCompat.getColor(
                                        getActivity(),
                                        R.color.gray_dark
                                ))
                        .cornerRadiusDp(80)
                        .oval(false)
                        .build())
                .error(R.drawable.register_personal_details_picture_sample)
                .into(personalImageView);
    }

    @Override
    public void setName(@NonNull String name) {
        personalNameInput.setText(name);
    }

    @Override
    public void setFatherName(@NonNull String fatherName) {
        personalFatherNameInput.setText(fatherName);
    }

    @Override
    public void setGrandfatherName(@NonNull String grandfatherName) {
        personalGrandfatherNameInput.setText(grandfatherName);
    }

    @Override
    public void setGender(boolean male) {
        genderGroup.check(male ? R.id.personalDetailsGenderMaleChoice :
                R.id.personalDetailsGenderFemaleChoice);
    }

    @Override
    public void setDateOfBirth(@NonNull String dateOfBirth, int day, int month, int year) {
        dateOfBirthLabel.setText(dateOfBirth);
    }

    @Override
    public void setMaritalStatus(@NonNull MaritalStatus maritalStatus) {
        for (int r = 0; r < maritalStatusGroup.getChildCount(); r++) {
            final LinearLayout row = (LinearLayout) maritalStatusGroup.getChildAt(r);
            for (int c = 0; c < row.getChildCount(); c++) {
                final CompoundButton button = (CompoundButton) row.getChildAt(c);
                switch (button.getId()) {
                    case R.id.familyStatusMarriedChoice:
                        button.setChecked(maritalStatus == MaritalStatus.MARRIED);
                        break;
                    case R.id.familyStatusDivorcedChoice:
                        button.setChecked(maritalStatus == MaritalStatus.DIVORCED);
                        break;
                    case R.id.familyStatusWidowedChoice:
                        button.setChecked(maritalStatus == MaritalStatus.WIDOWED);
                        break;
                    default:
                        button.setChecked(maritalStatus == MaritalStatus.SINGLE);
                }
            }
        }
    }

    @Override
    public void setHouseholdMemberCount(int householdMemberCount) {
        householdMemberInput.setText(String.valueOf(householdMemberCount));
    }

    @Override
    public void setIdCardPhoto(@NonNull String url) {
        Picasso.with(getActivity())
                .load(url)
                .fit()
                .error(R.drawable.register_address_id_sample)
                .into(idCardImageView);
    }

    @Override
    public void setIdNumber(@NonNull String idNumber) {
        idNumberInput.setText(idNumber);
    }

    @Override
    public void setWoreda(@Nullable String woreda) {
        woredaInput.setText(woreda);
    }

    @Override
    public void setKebele(@Nullable String kebele) {
        kebeleInput.setText(kebele);
    }

    @Override
    public void setHouseNumber(@Nullable String houseNumber) {
        houseNumberInput.setText(houseNumber);
    }

    @Override
    public void setPhoneNumber(@Nullable String phoneNumber) {
        phoneNumberInput.setText(phoneNumber);
    }

    @Override
    public void setSpouseIdNumber(@NonNull String idNumber) {
        spouseIdNumberInput.setText(idNumber);
    }

    @Override
    public void setSpouseName(@NonNull String name) {
        spouseNameInput.setText(name);
    }

    @Override
    public void setSpouseFatherName(@NonNull String fatherName) {
        spouseFatherNameInput.setText(fatherName);
    }

    @Override
    public void setSpouseGrandfatherName(@NonNull String grandfatherName) {
        spouseGrandfatherNameInput.setText(grandfatherName);
    }

    @Override
    public void showMissingNameError() {
        personalNameInput.setError(getString(R.string.register_error_name_prompt));
        personalNameInput.requestFocus();
    }

    @Override
    public void showMissingFatherNameError() {
        personalFatherNameInput.setError(getString(R.string.register_error_father_name_prompt));
        personalFatherNameInput.requestFocus();
    }

    @Override
    public void showMissingGrandfatherNameError() {
        personalGrandfatherNameInput.setError(getString(R.string.register_error_grandfather_name_prompt));
        personalGrandfatherNameInput.requestFocus();
    }

    @Override
    public void showMissingDateOfBirthError() {
        toast(R.string.register_error_date_of_birth_prompt);
    }

    @Override
    public void showMissingHouseholdMembersError(boolean valid) {
        householdMemberInput.setError(getString(valid ? R.string.register_error_valid_household_number_prompt :
                R.string.register_error_household_number_prompt));
        householdMemberInput.requestFocus();
    }

    @Override
    public void showMissingIdNumberError() {
        idNumberInput.setError(getString(R.string.register_error_id_number_prompt));
        idNumberInput.requestFocus();
    }

    @Override
    public void showMissingWoredaError() {
        woredaInput.setError(getString(R.string.register_error_woreda_prompt));
        woredaInput.requestFocus();
    }

    @Override
    public void showMissingKebeleError() {
        kebeleInput.setError(getString(R.string.register_error_kebele_prompt));
        kebeleInput.requestFocus();
    }

    @Override
    public void showMissingPhoneNumberError(boolean valid) {
        phoneNumberInput.setError(getString(valid ? R.string.register_error_valid_phone_number_prompt :
                R.string.register_error_phone_number_prompt));
        phoneNumberInput.requestFocus();
    }

    @Override
    public void showMissingSpouseIdNumberError() {
        spouseIdNumberInput.setError(getString(R.string.register_error_spouse_id_number_prompt));
        spouseIdNumberInput.requestFocus();
    }

    @Override
    public void showMissingSpouseNameError() {
        spouseNameInput.setError(getString(R.string.register_error_spouse_name_prompt));
        spouseNameInput.requestFocus();
    }

    @Override
    public void showMissingSpouseFatherNameError() {
        spouseFatherNameInput.setError(getString(R.string.register_error_spouse_father_name_prompt));
        spouseFatherNameInput.requestFocus();
    }

    @Override
    public void showMissingSpouseGrandfatherNameError() {
        spouseGrandfatherNameInput.setError(getString(R.string.register_error_spouse_grandfather_name_prompt));
        spouseGrandfatherNameInput.requestFocus();
    }

    @Override
    public void showPersonalSpouseIdNumberMatchError() {
        toast(R.string.register_error_spouse_personal_id_numbers_match_message, Toast.LENGTH_LONG);
        idNumberInput.setError(getString(R.string.register_error_id_match_prompt));
        spouseIdNumberInput.setError(getString(R.string.register_error_id_match_prompt));
        spouseIdNumberInput.requestFocus();
    }

    @Override
    public void showMissingPersonalPhotoError() {
        toast(R.string.register_error_missing_photo_prompt);
    }

    @Override
    public void showMissingIdCardPhotoError() {
        toast(R.string.register_error_missing_id_card_prompt);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showCancelRegistrationPrompt(boolean updating) {
        final String title = getString(updating ? R.string.register_cancel_update_title : R.string.register_cancel_title);
        final String message = getString(updating ? R.string.register_cancel_update_message : R.string.register_cancel_message);
        questionMode = QUESTION_MODE_CANCEL_UPDATE_REGISTRATION;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void showCompleteRegistrationPrompt(boolean updating) {
        final String title = getString(updating ? R.string.register_complete_update_title : R.string.register_complete_title);
        final String message = getString(updating ? R.string.register_complete_update_message : R.string.register_complete_message);
        questionMode = QUESTION_MODE_COMPLETE_REGISTRATION;

        questionDialog = QuestionDialog.newInstance(title, message);
        questionDialog.setCallback(this);
        questionDialog.show(getFragmentManager(), null);
    }

    @Override
    public void showRegistrationCanceledMessage(boolean updating) {
        toast(updating ? R.string.register_cancel_update_confirmation: R.string.register_cancel_confirmation);
    }

    @Override
    public void toggleTakePictureButton(boolean show) {
        personalDetailsTakePictureButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean canTakePictures() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return takePictureIntent.resolveActivity(getParent().getPackageManager()) != null;
    }

    @Override
    public void showPlayServicesErrorDialog(int status) {
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final Dialog errorDialog = api.getErrorDialog(getActivity(), status, REQUEST_PLAY_SERVICES);
        errorDialog.show();
        shownPlayServicesPrompt = true;
    }

    @Override
    public boolean hasAlreadyShownPlayServicesPrompt() {
        return shownPlayServicesPrompt;
    }

    @Override
    public void attachPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
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