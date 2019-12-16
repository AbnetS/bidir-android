package com.gebeya.mobile.bidir.ui.profile;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;

/**
 * Contract for Profile
 * <p>
 * samkura47@gmail.com
 */

public interface ProfileContract {

    /**
     * Profile presenter contract
     */
    interface Presenter extends BasePresenter<View> {
        void onEditClientPressed();
        void onBackPressed();
    }

    /**
     * Profile View Contract
     */
    interface View extends BaseView<Presenter> {
        void toggleEditClientButton(boolean visible);

        void setTitle(@NonNull String title);
        void setPersonalPhoto(@NonNull String photoUrl);
        void setName(@NonNull String name);
        void setLoanCount(int count);
        void setLocation(@NonNull String location);
        void showNoLocationAvailable();

        void setFullName(@NonNull String fullName);
        void setGender(boolean male);
        void setBirthDate(@NonNull String birthDate);

        void setMaritalStatus(@NonNull MaritalStatus type);
        void setNumberOfHousehold(int household);
        void setSpouseIdNumber(@NonNull String idNumber);
        void setSpouseFullName(@NonNull String fullName);
        void toggleSpouseDetails(boolean enabled);

        void setIdCardPhoto(@NonNull String idCardPhotoUrl);
        void setIdNumber(@NonNull String idNumber);
        void setWoreda(@NonNull String woreda);
        void setHouseNumber(@NonNull String houseNumber);
        void setKebele(@NonNull String kebele);
        void setPhoneNumber(@NonNull String phoneNumber);

        void openEditClientScreen(@NonNull Client client);
    }
}
