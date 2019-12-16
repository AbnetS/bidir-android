package com.gebeya.mobile.bidir.ui.register;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemote;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemoteSource;
import com.gebeya.mobile.bidir.data.client.remote.RegisterDto;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepo;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.GpsManager;
import com.gebeya.mobile.bidir.impl.util.location.GpsStateCallback;
import com.gebeya.mobile.bidir.impl.util.location.android.DefaultGpsManager;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesApi;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesGpsManager;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Presenter implementation for {@link RegisterContract.Presenter}
 */
public class RegisterPresenter implements RegisterContract.Presenter, GpsStateCallback {

    private RegisterContract.View view;

    @Inject ClientRemoteSource clientRemote;
    @Inject ClientRepoSource clientRepo;
    @Inject UserLocalSource userLocal;
    @Inject ComplexScreeningRepositorySource screeningRepo;

    @Inject DefaultGpsManager defaultGpsManager;
    @Inject PlayServicesGpsManager playServicesGpsManager;
    private GpsManager gpsManager;

    @Inject ClientParser clientParser;
    @Inject PlayServicesApi playServicesApi;
    private Client client;
    @Inject SchedulersProvider schedulers;
    private RequestState state;
    private RegisterData data;
    private String groupId;
    private Group group;

    @Inject GroupRepoSource groupRepo;
    @Inject RegisterPicturesData picturesData;
    @Inject GroupedComplexScreeningRepo groupedScreeningRepo;

    public RegisterPresenter(@Nullable Client client,
                             @NonNull RegisterData data,
                             @NonNull RequestState state,
                             @Nullable String groupId) {
        Tooth.inject(this, Scopes.SCOPE_STATES);
        this.client = client;
        this.data = data;
        this.state = state;
        this.groupId = groupId;
    }

    @SuppressLint("CheckResult")
    @Override
    public void start() {
        if (groupId != null) {
            groupRepo.fetch(groupId)
                    .subscribeOn(schedulers.background())
                    .subscribe(response -> group = response);
        }
        view.toggleTakePictureButton(view.canTakePictures());

        initializeGpsManager();

        gpsManager.addGpsStateCallback(this);
        checkGooglePlayServices();

        view.toggleLocationUpdate(false);
        view.toggleMenuLocation(false);

        view.toggleSpouseDetails(data.maritalStatus == MaritalStatus.MARRIED);

        boolean editing = client != null;
        view.setTitle(editing);
        if (editing) {
            view.setPersonalPhoto(client.photoUrl);

            data.name = client.firstName;
            view.setName(client.firstName);

            data.fatherName = client.lastName;
            view.setFatherName(client.lastName);

            data.grandfatherName = client.surname;
            view.setGrandfatherName(client.surname);

            data.gender = client.gender;
            view.setGender(client.gender.equals(ClientParser.GENDER_MALE));

            data.day = client.dateOfBirth.getDayOfMonth();
            data.month = client.dateOfBirth.getMonthOfYear();
            data.year = client.dateOfBirth.getYear();
            final String dateOfBirth = String.format(Locale.getDefault(),
                    Constants.DATE_DISPLAY_FORMAT, data.day, data.month, data.year
            );
            view.setDateOfBirth(dateOfBirth, data.day, data.month, data.year);

            final MaritalStatus maritalStatus = clientParser.getMaritalStatusType(client.maritalStatus);
            data.maritalStatus = maritalStatus;
            view.setMaritalStatus(maritalStatus);

            data.householdMemberCount = client.householdMemberCount;
            view.setHouseholdMemberCount(client.householdMemberCount);

            view.setIdCardPhoto(client.idCardUrl);

            data.idNumber = client.idNumber;
            view.setIdNumber(client.idNumber);

            data.woreda = client.woreda;
            view.setWoreda(client.woreda);

            data.kebele = client.kebele;
            view.setKebele(client.kebele);

            data.houseNumber = client.houseNumber;
            view.setHouseNumber(client.houseNumber);

            data.phoneNumber = client.phoneNumber;
            view.setPhoneNumber(client.phoneNumber);

            view.toggleSpouseDetails(maritalStatus == MaritalStatus.MARRIED);
            if (maritalStatus == MaritalStatus.MARRIED) {
                view.setSpouseIdNumber(client.spouseIdNumber);
                view.setSpouseName(client.spouseFirstName);
                view.setSpouseFatherName(client.spouseLastName);
                view.setSpouseGrandfatherName(client.spouseSurname);
            }

            view.toggleLocationContainer(true);
            if (Utils.hasLocation(client.latitude, client.longitude)) {
                final String location = String.format(Locale.getDefault(), Constants.GPS_FORMAT,
                        client.latitude, client.longitude);
                view.showPreviousLocation(location);
            } else {
                view.showNoLocationAvailable();
            }
        } else {
            view.toggleLocationContainer(false);

            data.personalPictureFile = picturesData.personalPictureFile;
            if (data.personalPictureFile != null)
                view.updatePersonalPicture(data.personalPictureFile);

            data.idCardPictureFile = picturesData.idCardPictureFile;
            if (data.idCardPictureFile != null) view.updateIdCardPicture(data.idCardPictureFile);
        }

        if (state.loading()) {
            if (client == null) {
                view.showRegisterProgress();
            } else {
                view.showUpdateProgress();
            }
            return;
        }

        if (state.complete()) {
            if (client == null) {
                onRegisterComplete();
            } else {
                onUpdateComplete();
            }
            return;
        }

        if (state.error()) {
            if (client == null) {
                onRegisterFailed(state.getError());
            } else {
                onUpdateFailed(state.getError());
            }
        }
    }

    private void initializeGpsManager() {
        final boolean missing = playServicesApi.missingPlayServices();
        gpsManager = missing ? defaultGpsManager : playServicesGpsManager;
    }

    private void checkGooglePlayServices() {
        if (playServicesApi.missingPlayServices()) {
            if (view.hasAlreadyShownPlayServicesPrompt()) return;

            if (playServicesApi.isUserResolvableError()) {
                final int status = playServicesApi.getStatus();
                view.showPlayServicesErrorDialog(status);
            }
        }
    }

    @Override
    public void onNameChanged(@NonNull String name) {
        data.name = name;
    }

    @Override
    public void onFatherNameChanged(@NonNull String fatherName) {
        data.fatherName = fatherName;
    }

    @Override
    public void onGrandfatherNameChanged(@NonNull String grandfatherName) {
        data.grandfatherName = grandfatherName;
    }

    @Override
    public void onGenderChanged(boolean male) {
        data.gender = male ? ClientParser.GENDER_MALE : ClientParser.GENDER_FEMALE;
    }

    @Override
    public void onDateOfBirthChanged(int day, int month, int year) {
        data.day = day;
        data.month = month;
        data.year = year;

        String date = String.format(Locale.getDefault(), Constants.DATE_DISPLAY_FORMAT, day, month, year);
        view.updateDateOfBirth(date);
    }

    @Override
    public void onMaritalStatusChanged(@NonNull MaritalStatus maritalStatus) {
        data.maritalStatus = maritalStatus;
        view.toggleSpouseDetails(maritalStatus == MaritalStatus.MARRIED);
    }

    @Override
    public void onHouseholdMemberCountChanged(@NonNull String householdMember) {
        final String s = householdMember.trim();
        data.householdMemberCount = Integer.parseInt(s.isEmpty() ? "0" : s);
    }

    @Override
    public void onIdNumberChanged(@NonNull String idNumber) {
        data.idNumber = idNumber;
    }

    @Override
    public void onWoredaChanged(@NonNull String woreda) {
        data.woreda = woreda;
    }

    @Override
    public void onKebeleChanged(@NonNull String kebele) {
        data.kebele = kebele;
    }

    @Override
    public void onHouseNumberChanged(@NonNull String houseNumber) {
        data.houseNumber = houseNumber;
    }

    @Override
    public void onPhoneNumberChanged(@NonNull String phoneNumber) {
        data.phoneNumber = phoneNumber;
    }

    @Override
    public void onSpouseIdNumberChanged(@NonNull String idNumber) {
        data.spouseIdNumber = idNumber;
    }

    @Override
    public void onSpouseNameChanged(@NonNull String name) {
        data.spouseName = name;
    }

    @Override
    public void onSpouseFatherNameChanged(@NonNull String fatherName) {
        data.spouseFatherName = fatherName;
    }

    @Override
    public void onSpouseGrandfatherNameChanged(@NonNull String grandfatherName) {
        data.spouseGrandfatherName = grandfatherName;
    }

    @Override
    public void onMenuToggleLocationPressed() {
        boolean running = gpsManager.running();
        if (running) {
            gpsManager.stop();
        } else {
            startGpsPermission();
        }
    }

    private void startGpsPermission() {
        boolean hasGpsPermission = view.hasGpsPermission();
        if (!hasGpsPermission) {
            if (view.needsGpsPermissionExplanation()) {
                view.showGpsPermissionExplanation();
            } else {
                view.requestGpsPermission();
            }
        } else {
            startGps();
        }
    }

    @Override
    public void onGpsPermissionExplanationReturned(boolean granted) {
        if (granted) {
            view.requestGpsPermission();
        } else {
            view.showGpsPermissionDeniedError();
        }
    }

    @Override
    public void onGpsPermissionRequestReturned(boolean granted) {
        if (granted) {
            startGps();
        } else {
            view.showGpsPermissionDeniedError();
        }
    }

    @Override
    public void startGps() {
        if (!view.hasGpsPermission()) return;

        if (gpsManager.enabled()) {
            view.setGpsCoordinates(0, 0);
            gpsManager.start();
        } else {
            view.requestGpsEnable();
        }
    }

    @Override
    public void onGpsEnableRequestReturned(boolean granted) {
        if (granted) {
            view.openGpsSettings();
        }
    }

    @Override
    public void onGpsStateChanged(boolean listening) {
        view.toggleLocationUpdate(listening);
        view.toggleMenuLocation(listening);
    }

    @Override
    public void onGpsLocationUpdated(double latitude, double longitude) {
        view.setGpsCoordinates(latitude, longitude);
    }

    @Override
    public void onUploadPhotoPressed() {
        if (view == null) return;
        view.openUploadPhotoPicker();
    }

    @Override
    public void onUploadIdCardPressed() {
        if (view == null) return;
        view.openIdCardPicker();
    }

    @Override
    public void onTakeIdPicturePressed() {
        if (view == null) return;
        view.openTakeIdPictureCamera();
    }

    @Override
    public void onTakePicturePressed() {
        if (view == null) return;
        view.openTakePictureCamera();
    }

    @Override
    public void onSelectDatePressed() {
        view.openDateSelector(data.day, data.month, data.year);
    }

    @Override
    public void onPersonalPictureReturned(@NonNull String path) {
        picturesData.personalPictureFile = new File(path);
        data.personalPictureFile = picturesData.personalPictureFile;
        view.updatePersonalPicture(picturesData.personalPictureFile);
    }

    @Override
    public void onIdCardPictureReturned(@NonNull String path) {
        picturesData.idCardPictureFile = new File(path);
        data.idCardPictureFile = picturesData.idCardPictureFile;
        view.updateIdCardPicture(picturesData.idCardPictureFile);
    }

    @Override
    public void onSavePressed() {
        final boolean editing = client != null;
        if (editing) {
            if (client.photoUrl.equals("-") && (data.personalPictureFile == null || !data.personalPictureFile.exists())) {
                view.showMissingPersonalPhotoError();
                return;
            }
        } else {
            if (data.personalPictureFile == null || !data.personalPictureFile.exists()) {
                view.showMissingPersonalPhotoError();
                return;
            }
        }

        if (data.name.isEmpty()) {
            view.showMissingNameError();
            return;
        }
        if (data.fatherName.isEmpty()) {
            view.showMissingFatherNameError();
            return;
        }
        if (data.grandfatherName.isEmpty()) {
            view.showMissingGrandfatherNameError();
            return;
        }

        if (data.day <= 0 || data.month <= 0 || data.year <= 0) {
            view.showMissingDateOfBirthError();
            view.openDateSelector(data.day, data.month, data.year);
            return;
        }

        if (data.householdMemberCount == 0) {
            view.showMissingHouseholdMembersError(true);
            return;
        }

        if (editing) {
            if (client.idCardUrl.equals("-") && (data.idCardPictureFile == null || !data.idCardPictureFile.exists())) {
                view.showMissingIdCardPhotoError();
                return;
            }
        } else {
            if (data.idCardPictureFile == null || !data.idCardPictureFile.exists()) {
                view.showMissingIdCardPhotoError();
                return;
            }
        }
        if (data.idNumber.isEmpty()) {
            view.showMissingIdNumberError();
            return;
        }
        if (data.woreda.isEmpty()) {
            view.showMissingWoredaError();
            return;
        }
        if (data.kebele.isEmpty()) {
            view.showMissingKebeleError();
            return;
        }
        final String phoneNumber = data.phoneNumber;
        if (phoneNumber.isEmpty()) {
            view.showMissingPhoneNumberError(false);
            return;
        }
        if (phoneNumber.length() != Constants.PHONE_NUMBER_LENGTH) {
            view.showMissingPhoneNumberError(true);
            return;
        }
        if (!phoneNumber.startsWith(Constants.PHONE_NUMBER_SUFFIX)) {
            view.showMissingPhoneNumberError(true);
            return;
        }

        if (data.maritalStatus == MaritalStatus.MARRIED) {
            if (data.spouseIdNumber.isEmpty()) {
                view.showMissingSpouseIdNumberError();
                return;
            }
            if (data.spouseName.isEmpty()) {
                view.showMissingSpouseNameError();
                return;
            }
            if (data.spouseFatherName.isEmpty()) {
                view.showMissingSpouseFatherNameError();
                return;
            }
            if (data.spouseGrandfatherName.isEmpty()) {
                view.showMissingSpouseGrandfatherNameError();
                return;
            }
            if (data.spouseIdNumber.equals(data.idNumber)) {
                view.showPersonalSpouseIdNumberMatchError();
                return;
            }
        }

        if (gpsManager.running()) {
            final double[] position = gpsManager.getPosition();
            data.latitude = position[0];
            data.longitude = position[1];
        }

        view.showCompleteRegistrationPrompt(client != null);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onCompleteRegistrationPromptReturned(boolean approved) {
        if (approved) {
            state.setLoading();

            final boolean registering = client == null;
            final RegisterDto dto = toDto();

            if (group != null) {
                view.showRegisterProgress();
                userLocal.first()
                        .flatMap(userData -> {
                            final User user = userData.get();
                            dto.createdBy = user._id;
                            dto.for_group = true;
                            return clientRemote.register(dto, user);
                        })
                        .flatMapCompletable(clientCreated -> {
                            String clientId = clientCreated._id;
                            return groupRepo.updateMembers(group, Collections.singletonList(clientId));
                        })
                        .andThen(groupRepo.fetchForce(group._id))
                        .flatMap(done -> screeningRepo.fetchForceAll())
                        .flatMap(groups -> groupedScreeningRepo.fetchForce(group._id))
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(done -> {
                                    state.setComplete();
                                    onRegisterComplete();
                                },
                                throwable -> {
                                    state.setError(throwable);
                                    onRegisterFailed(throwable);
                                });
            } else {
                final Completable completable =
                        registering ?
                                clientRepo.register(dto, createTempClient(dto))
                                        .andThen(screeningRepo.fetchForceAll().flatMapCompletable(ignore -> Completable.complete())) :
                                clientRepo.update(dto, client);
                if (registering) {
                    view.showRegisterProgress();
                } else {
                    view.showUpdateProgress();
                }

                completable
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.ui())
                        .subscribe(() -> {
                                    state.setComplete();
                                    if (registering) {
                                        onRegisterComplete();
                                    } else {
                                        onUpdateComplete();
                                    }
                                },
                                throwable -> {
                                    state.setError(throwable);
                                    if (registering) {
                                        onRegisterFailed(throwable);
                                    } else {
                                        onUpdateFailed(throwable);
                                    }
                                });
                }
            }

    }

    private Client createTempClient(@NonNull RegisterDto register) {
        final Client client = new Client();

        client._id = UUID.randomUUID().toString();
        client.firstName = register.firstName;
        client.lastName = register.lastName;
        client.surname = register.surname;
        client.gender = register.gender;
        client.idNumber = register.idNumber;
        client.idCardUrl = register.idCardFile.getAbsolutePath();
        client.photoUrl = register.photoFile.getAbsolutePath();

        final DateTime now = DateTime.now();
        client.dateOfBirth = new DateTime(
                register.year,
                register.month,
                register.day,
                now.getHourOfDay(),
                now.getMinuteOfHour(),
                now.getSecondOfMinute()
        );

        client.maritalStatus = register.maritalStatus;
        client.woreda = register.woreda;
        client.kebele = register.kebele;
        client.houseNumber = register.houseNumber;
        client.phoneNumber = register.phoneNumber;
        client.status = ClientParser.NEW;

        client.householdMemberCount = Integer.parseInt(register.householdMemberCount);
        client.latitude = register.latitude;
        client.longitude = register.longitude;

        client.spouseFirstName = register.spouseFirstName;
        client.spouseLastName = register.spouseLastName;
        client.spouseSurname = register.spouseSurname;
        client.spouseIdNumber = register.spouseIdNumber;

        return client;
    }

    @Override
    public RegisterDto toDto() {
        final RegisterDto dto = new RegisterDto();
        if (client == null) {   // creating a new client
            dto.photoFile = data.personalPictureFile;
            dto.firstName = data.name;
            dto.lastName = data.fatherName;
            dto.surname = data.grandfatherName;
            dto.gender = data.gender;

            dto.day = data.day;
            dto.month = data.month; // TODO: Check for storing on API to support Amharic calendar
            dto.year = data.year;

            dto.latitude = data.latitude;
            dto.longitude = data.longitude;

            dto.maritalStatus = clientParser.getLocalMaritalStatus(data.maritalStatus);
            dto.householdMemberCount = String.valueOf(data.householdMemberCount);

            dto.idCardFile = data.idCardPictureFile;
            dto.idNumber = data.idNumber;
            dto.woreda = data.woreda;
            dto.kebele = data.kebele;
            dto.houseNumber = data.houseNumber;
            dto.phoneNumber = data.phoneNumber;

            final boolean married = data.maritalStatus == MaritalStatus.MARRIED;
            dto.spouseIdNumber = married ? data.spouseIdNumber : null;
            dto.spouseFirstName = married ? data.spouseName : null;
            dto.spouseLastName = married ? data.spouseFatherName : null;
            dto.spouseSurname = married ? data.spouseGrandfatherName : null;
        } else {    // editing an existing client
            // Fields that have changed have data. Those that didn't are set to null instead
            if (data.personalPictureFile != null) dto.photoFile = data.personalPictureFile;        // TODO: Figure out how to fix this
            dto.firstName = !data.name.equals(client.firstName) ? data.name : null;
            client.firstName = data.name.equals(client.firstName) ? client.firstName : data.name;

            dto.lastName = !data.fatherName.equals(client.lastName) ? data.fatherName : null;
            client.lastName = data.fatherName.equals(client.lastName) ? client.lastName : data.fatherName;

            dto.surname = !data.grandfatherName.equals(client.surname) ? data.grandfatherName : null;
            client.surname = data.grandfatherName.equals(client.surname) ? client.surname : data.grandfatherName;

            dto.gender = !data.gender.equals(client.gender) ? data.gender : null;
            client.gender = data.gender.equals(client.gender) ? client.gender : data.gender;

            final boolean modified = data.day != client.dateOfBirth.getDayOfMonth() ||
                    data.month != client.dateOfBirth.getMonthOfYear() ||
                    data.year != client.dateOfBirth.getYear();

            dto.day = modified ? data.day : 0;
            dto.month = modified ? data.month : 0;
            dto.year = modified ? data.year : 0;

            final DateTime now = DateTime.now();
            client.dateOfBirth = modified ? new DateTime(
                    data.year,
                    data.month,
                    data.day,
                    now.getHourOfDay(),
                    now.getMinuteOfHour(),
                    now.getSecondOfMinute()
            ) : client.dateOfBirth;

            if (data.latitude != 0 && data.longitude != 0) {
                dto.latitude = data.latitude;
                dto.longitude = data.longitude;
            }
            if (client.latitude != data.latitude) client.latitude = data.latitude;
            if (client.longitude != data.longitude) client.longitude = data.longitude;

            final String localMaritalStatus = clientParser.getLocalMaritalStatus(data.maritalStatus);
            final boolean maritalStatusChanged = !localMaritalStatus.equals(client.maritalStatus);
            if (maritalStatusChanged) dto.maritalStatus = localMaritalStatus;

            if (data.householdMemberCount != client.householdMemberCount) {
                dto.householdMemberCount = String.valueOf(data.householdMemberCount);
                client.householdMemberCount = data.householdMemberCount;
            } else {
                dto.householdMemberCount = null;
            }

            if (data.idCardPictureFile != null) dto.idCardFile = data.idCardPictureFile;        // TODO: Figure out how to fix this

            dto.idNumber = !data.idNumber.equals(client.idNumber) ? data.idNumber : null;
            client.idNumber = data.idNumber.equals(client.idNumber) ? client.idNumber: data.idNumber;

            dto.woreda = !data.woreda.equals(client.woreda) ? data.woreda : null;
            client.woreda = data.woreda.equals(client.woreda) ? client.woreda: data.woreda;

            dto.kebele = !data.kebele.equals(client.kebele) ? data.kebele : null;
            client.kebele = data.kebele.equals(client.kebele) ? client.kebele: data.kebele;

            dto.houseNumber = !data.houseNumber.equals(client.houseNumber) ? data.houseNumber : null;
            client.houseNumber = data.houseNumber.equals(client.houseNumber) ? client.houseNumber: data.houseNumber;

            dto.phoneNumber = !data.phoneNumber.equals(client.phoneNumber) ? data.phoneNumber : null;
            client.phoneNumber = data.phoneNumber.equals(client.phoneNumber) ? client.phoneNumber: data.phoneNumber;

            final boolean married = data.maritalStatus == MaritalStatus.MARRIED;
            if (maritalStatusChanged) {
                dto.spouseIdNumber = married ? data.spouseIdNumber : null;
                client.spouseIdNumber = data.spouseIdNumber.equals(client.spouseIdNumber) ? client.spouseIdNumber: data.spouseIdNumber;

                dto.spouseFirstName = married ? data.spouseName : null;
                client.spouseFirstName = data.spouseName.equals(client.spouseFirstName) ? client.spouseFirstName: data.spouseName;

                dto.spouseLastName = married ? data.spouseFatherName : null;
                client.spouseLastName = data.spouseFatherName.equals(client.spouseLastName) ? client.spouseLastName: data.spouseFatherName;

                dto.spouseSurname = married ? data.spouseGrandfatherName : null;
                client.spouseSurname = data.spouseGrandfatherName.equals(client.spouseSurname) ? client.spouseSurname: data.spouseGrandfatherName;
            } else {
                if (married) {
                     dto.spouseIdNumber = !data.spouseIdNumber.equals(client.spouseIdNumber) ? data.spouseIdNumber : null;
                    dto.spouseFirstName = !data.spouseName.equals(client.spouseFirstName) ? data.spouseName : null;
                    dto.spouseLastName = !data.spouseFatherName.equals(client.spouseLastName) ? data.spouseFatherName : null;
                    dto.spouseSurname = !data.spouseGrandfatherName.equals(client.spouseSurname) ? data.spouseGrandfatherName : null;
                }
            }
        }

        return dto;
    }

    @Override
    public void onRegisterComplete() {
        if (view == null) return;
        view.hideProgress();
        view.showRegistrationSuccess();
    }

    @Override
    public void onRegisterFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = null;
        String extra = null;

        if (error.contains(ApiErrors.REGISTER_PART_ALREADY_EXISTS)) {
            message = ApiErrors.REGISTER_ERROR_CLIENT_ALREADY_EXISTS;
        }

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)) {
            onUpdateComplete();
            return;
        }

        if (message == null) {
            message = ApiErrors.REGISTER_ERROR_GENERIC;
            extra = error;
        }

        view.hideProgress();
        view.showRegisterError(Result.createError(message, extra));

        state.reset();
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;
        view.hideProgress();
        view.showUpdateSuccess();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)) {
            onUpdateComplete();
            return;
        }

        String message = ApiErrors.UPDATE_ERROR_GENERIC;
        String extra = error;

        view.hideProgress();
        view.showUpdateError(Result.createError(message, extra));
    }

    @Override
    public void onSuccessDismissed() {
        state.reset();
        data.reset();
        picturesData.reset();
        view.close();
    }

    @Override
    public void onCancelRegistrationDismissed() {
        view.showRegistrationCanceledMessage(client != null);
        state.reset();
        data.reset();
        picturesData.reset();
        view.close();
    }

    @Override
    public void onBackPressed() {
        if (hasRegistrationData()) {
            view.showCancelRegistrationPrompt(client != null);
            return;
        }
        view.close();
    }

    private boolean hasRegistrationData() {
        if (data.personalPictureFile != null && data.personalPictureFile.exists()) return true;
        if (!data.name.isEmpty() || !data.fatherName.isEmpty() || !data.grandfatherName.isEmpty())
            return true;
        if (data.day + data.month + data.year != 0) return true;

        if (data.householdMemberCount > 0) return true;

        if (data.idCardPictureFile != null && data.idCardPictureFile.exists()) return true;
        if (!data.idNumber.isEmpty()) return true;
        if (!data.woreda.isEmpty()) return true;
        if (!data.kebele.isEmpty()) return true;
        if (!data.houseNumber.isEmpty()) return true;
        if (!data.phoneNumber.isEmpty()) return true;

        if (!data.spouseIdNumber.isEmpty()) return true;
        if (!data.spouseName.isEmpty()) return true;
        if (!data.spouseFatherName.isEmpty()) return true;
        return !data.spouseGrandfatherName.isEmpty();
    }

    public void attachView(RegisterContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view.hideProgress();
        view.hideSuccess();
        view.hideError();
        view.hideGpsExplanation();

        gpsManager.stop();
        gpsManager.removeGspStateCallback();

        view = null;
    }

    @Override
    public RegisterContract.View getView() {
        return view;
    }
}