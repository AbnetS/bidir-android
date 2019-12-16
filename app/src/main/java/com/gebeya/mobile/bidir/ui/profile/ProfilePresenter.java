package com.gebeya.mobile.bidir.ui.profile;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.loanapplication.local.LoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Samuel K. on 12/28/2017.
 * <p>
 * samkura47@gmail.com
 */
public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;

    @Inject ClientLocalSource clientLocal;
    @Inject LoanApplicationLocalSource loanApplicationLocal;
    @Inject PermissionLocalSource permissionLocal;
    private String clientId;
    private Client client;
    @Inject ClientParser clientParser;

    public ProfilePresenter(@NonNull String clientId) {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        this.clientId = clientId;
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        clientLocal
                .get(clientId)
                .subscribe(data -> {
                    client = data.get();
                    final String name = Utils.formatName(
                            client.firstName,
                            client.surname,
                            client.lastName
                    );

                    view.setTitle(name);
                    view.setPersonalPhoto(client.photoUrl);
                    view.setName(name);
                    if (Utils.hasLocation(client.latitude, client.longitude)) {
                        final String location = String.format(Locale.getDefault(), Constants.GPS_FORMAT,
                                client.latitude, client.longitude);
                        view.setLocation(location);
                    } else {
                        view.showNoLocationAvailable();
                    }

                    view.setFullName(name);
                    view.setGender(client.gender.equals(ClientParser.GENDER_MALE));
                    String dateOfBirth = String.format(Locale.getDefault(), Constants.DATE_DISPLAY_FORMAT,
                            client.dateOfBirth.getDayOfMonth(),
                            client.dateOfBirth.getMonthOfYear(),
                            client.dateOfBirth.getYear()
                    );
                    view.setBirthDate(dateOfBirth);

                    final MaritalStatus maritalStatus = clientParser.getMaritalStatusType(client.maritalStatus);
                    view.setMaritalStatus(maritalStatus);
                    view.setNumberOfHousehold(client.householdMemberCount);

                    final boolean married = maritalStatus == MaritalStatus.MARRIED;
                    view.toggleSpouseDetails(married);

                    if (married) {

                        final String spouseName = Utils.formatName(
                                client.spouseFirstName,
                                client.spouseSurname,
                                client.spouseLastName
                        );
                        view.setSpouseFullName(spouseName);
                        view.setSpouseIdNumber(client.spouseIdNumber);
                    }

                    view.setIdCardPhoto(client.idCardUrl);
                    view.setIdNumber(client.idNumber);
                    view.setWoreda(client.woreda);
                    view.setKebele(client.kebele);
                    view.setHouseNumber(client.houseNumber);
                    view.setPhoneNumber(client.phoneNumber);
                    view.setLoanCount(client.loanCycleCount);

                    permissionLocal.hasPermission(PermissionHelper.ENTITY_CLIENT, PermissionHelper.OPERATION_UPDATE)
                            .subscribe(view::toggleEditClientButton);

                });
    }

    @Override
    public void onEditClientPressed() {
        view.openEditClientScreen(client);
    }

    @Override
    public void onBackPressed() {
        view.close();
    }

    @Override
    public void attachView(ProfileContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ProfileContract.View getView() {
        return view;
    }
}