package com.gebeya.mobile.bidir.ui.register;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.data.client.remote.RegisterDto;
import com.gebeya.mobile.bidir.impl.util.location.mvp.LocationApiView;

import java.io.File;

/**
 * Contract for the registration processes
 */
public interface RegisterContract {

    /**
     * RegisterDto presenter contract
     */
    interface Presenter extends BasePresenter<View> {
        void onNameChanged(@NonNull String name);
        void onFatherNameChanged(@NonNull String fatherName);
        void onGrandfatherNameChanged(@NonNull String grandfatherName);
        void onGenderChanged(boolean male);
        void onDateOfBirthChanged(int day, int month, int year);

        void onMaritalStatusChanged(@NonNull MaritalStatus maritalStatus);
        void onHouseholdMemberCountChanged(@NonNull String householdMember);

        void onIdNumberChanged(@NonNull String idNumber);
        void onWoredaChanged(@NonNull String woreda);
        void onKebeleChanged(@NonNull String kebele);
        void onHouseNumberChanged(@NonNull String houseNumber);
        void onPhoneNumberChanged(@NonNull String phoneNumber);

        void onSpouseIdNumberChanged(@NonNull String idNumber);
        void onSpouseNameChanged(@NonNull String name);
        void onSpouseFatherNameChanged(@NonNull String fatherName);
        void onSpouseGrandfatherNameChanged(@NonNull String grandfatherName);

        void onMenuToggleLocationPressed();
        void onUploadPhotoPressed();
        void onGpsPermissionExplanationReturned(boolean granted);
        void onGpsPermissionRequestReturned(boolean granted);
        void startGps();
        void onGpsEnableRequestReturned(boolean granted);
        void onTakePicturePressed();
        void onSelectDatePressed();

        void onPersonalPictureReturned(@NonNull String path);
        void onIdCardPictureReturned(@NonNull String path);

        void onRegisterComplete();
        void onRegisterFailed(@NonNull Throwable throwable);

        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onUploadIdCardPressed();
        void onTakeIdPicturePressed();

        void onSavePressed();

        void onBackPressed();
        void onSuccessDismissed();
        void onCancelRegistrationDismissed();
        void onCompleteRegistrationPromptReturned(boolean approved);



        RegisterDto toDto();
    }

    /**
     * RegisterDto view contract
     */
    interface View extends BaseView<Presenter>, LocationApiView {
        void setTitle(boolean editing);

        void setPersonalPhoto(@NonNull String url);
        void setName(@NonNull String name);
        void setFatherName(@NonNull String fatherName);
        void setGrandfatherName(@NonNull String grandfatherName);
        void setGender(boolean male);
        void setDateOfBirth(@NonNull String dateOfBirth, int day, int month, int year);

        void setMaritalStatus(@NonNull MaritalStatus maritalStatus);
        void setHouseholdMemberCount(int householdMemberCount);

        void setIdCardPhoto(@NonNull String url);
        void setIdNumber(@NonNull String idNumber);
        void setWoreda(@Nullable String woreda);
        void setKebele(@Nullable String kebele);
        void setHouseNumber(@Nullable String houseNumber);
        void setPhoneNumber(@Nullable String phoneNumber);

        void setSpouseIdNumber(@NonNull String idNumber);
        void setSpouseName(@NonNull String name);
        void setSpouseFatherName(@NonNull String fatherName);
        void setSpouseGrandfatherName(@NonNull String grandfatherName);

        boolean hasGpsPermission();
        void requestGpsPermission();
        boolean needsGpsPermissionExplanation();
        void showGpsPermissionExplanation();
        void hideGpsExplanation();
        void showGpsPermissionDeniedError();
        void requestGpsEnable();
        void openGpsSettings();
        void toggleMenuLocation(boolean enabled);
        void toggleLocationUpdate(boolean animate);
        void setGpsCoordinates(double latitude, double longitude);
        void toggleLocationContainer(boolean show);
        void showNoLocationAvailable();
        void showPreviousLocation(@NonNull String location);

        void openUploadPhotoPicker();
        void openTakePictureCamera();
        void openTakeIdPictureCamera();
        void openDateSelector(int day, int month, int year);
        void updateDateOfBirth(@NonNull String date);
        boolean canTakePictures();
        void toggleTakePictureButton(boolean show);
        void showMissingNameError();
        void showMissingFatherNameError();
        void showMissingGrandfatherNameError();
        void showMissingDateOfBirthError();
        void showMissingHouseholdMembersError(boolean invalid);
        void showMissingIdNumberError();
        void showMissingWoredaError();
        void showMissingKebeleError();
        void showMissingPhoneNumberError(boolean invalid);
        void showMissingSpouseIdNumberError();
        void showMissingSpouseNameError();
        void showMissingSpouseFatherNameError();
        void showMissingSpouseGrandfatherNameError();
        void showPersonalSpouseIdNumberMatchError();
        void showMissingPersonalPhotoError();
        void showMissingIdCardPhotoError();

        void showRegisterProgress();
        void showUpdateProgress();
        void hideProgress();

        void showRegisterError(@NonNull Result result);
        void showUpdateError(@NonNull Result result);
        void hideError();

        void showRegistrationSuccess();
        void showUpdateSuccess();
        void hideSuccess();

        void toggleSpouseDetails(boolean enabled);

        void openIdCardPicker();
        void updatePersonalPicture(@NonNull File file);
        void updateIdCardPicture(@NonNull File file);

        void showCancelRegistrationPrompt(boolean updating);
        void showRegistrationCanceledMessage(boolean updating);
        void showCompleteRegistrationPrompt(boolean updating);
        boolean hasAlreadyShownPlayServicesPrompt();
    }
}