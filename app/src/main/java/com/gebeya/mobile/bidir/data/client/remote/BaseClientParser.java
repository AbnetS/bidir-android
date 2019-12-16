package com.gebeya.mobile.bidir.data.client.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.MaritalStatus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

/**
 * Concrete implementation for the {@link ClientParser} interface.
 */
public class BaseClientParser implements ClientParser {

    @Override
    public Client parse(@NonNull JsonObject object) throws Exception {
        try {
            final Client client = new Client();

            JsonElement branchObject = object.get("branch");
            if (branchObject.isJsonNull()) return null;

            if (object.get("created_by").isJsonObject()) {
                JsonObject createdByObject = object.get("created_by").getAsJsonObject();
                client.createdBy = createdByObject.get("_id").getAsString();
            } else {
                client.createdBy = object.get("created_by").getAsString();
            }

            client._id = object.get("_id").getAsString();

            client.firstName = object.get("first_name").getAsString();
            client.lastName = object.get("last_name").getAsString();
            client.surname = object.get("grandfather_name").getAsString();

            final String apiGender = object.get("gender").getAsString();
            final String localGender = getLocalGender(apiGender);
            if (localGender.equals(UNKNOWN_GENDER)) {
                throw new Exception("Unknown API gender: " + apiGender);
            }
            client.gender = localGender;

            client.idNumber = object.get("national_id_no").getAsString();

            String idCardUrl = object.get("national_id_card").getAsString();
            if (idCardUrl.trim().isEmpty()) idCardUrl = "-";
            client.idCardUrl = idCardUrl;

            String photoUrl = object.get("picture").getAsString();
            if (photoUrl.trim().isEmpty()) photoUrl = "-";
            client.photoUrl = photoUrl;

            JsonElement dateOfBirth = object.get("date_of_birth");
            final DateTime empty = DateTime.parse("0000-01-01T00:00:00.003Z");
            client.dateOfBirth = dateOfBirth.isJsonNull() ? empty : new DateTime(dateOfBirth.getAsString());

            final String apiMaritalStatus = object.get("civil_status").getAsString();
            final String localMaritalStatus = getLocalMaritalStatus(apiMaritalStatus);
            if (localMaritalStatus.equals(UNKNOWN_MARITAL_STATUS)) {
                throw new Exception("Unknown API marital status: " + apiMaritalStatus);
            }
            client.maritalStatus = localMaritalStatus;

            client.woreda = object.get("woreda").getAsString();
            client.kebele = object.get("kebele").getAsString();
            client.houseNumber = object.get("house_no").getAsString();
            client.phoneNumber = object.get("phone").getAsString();
            client.householdMemberCount = object.get("household_members_count").getAsInt();
            client.phoneNumber = object.get("phone").getAsString();
            client.loanCycleCount = object.get("loan_cycle_number").getAsInt();

            if (object.has("for_group")) {
                client.forGroup = Boolean.parseBoolean(object.get("for_group").getAsString());
            }

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = getLocalStatus(apiStatus);
            if (localStatus.equals(UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Client status: " + apiStatus);
            }
            client.status = localStatus;

            final String updatedAt = object.get("last_modified").getAsString();
            client.updatedAt = new DateTime(updatedAt);

            final JsonObject geolocationObject = object.get("geolocation").getAsJsonObject();
            final double latitude = geolocationObject.get("latitude").getAsDouble();
            final double longitude = geolocationObject.get("longitude").getAsDouble();
            client.latitude = latitude;
            client.longitude = longitude;

            if (!client._id.equals("5a7465db2d20cb0001822d00")) {                       // TODO: Wait till the API has fixed this
                final JsonObject spouseObject = object.get("spouse").getAsJsonObject();
                final String spouseIdNumber = spouseObject.get("national_id_no").getAsString().trim();
                final String spouseFirstName = spouseObject.get("first_name").getAsString().trim();
                final String spouseLastName = spouseObject.get("last_name").getAsString().trim();
                final String spouseMiddleName = spouseObject.get("grandfather_name").getAsString().trim();
                client.spouseIdNumber = spouseIdNumber.isEmpty() ? "-" : spouseIdNumber;
                client.spouseFirstName = spouseFirstName.isEmpty() ? "-" : spouseFirstName;
                client.spouseLastName = spouseLastName.isEmpty() ? "-" : spouseLastName;
                client.spouseSurname = spouseMiddleName.isEmpty() ? "-" : spouseMiddleName;
            } else {
                client.spouseIdNumber = "-";
                client.spouseFirstName = "-";
                client.spouseLastName = "-";
                client.spouseSurname = "-";
            }

            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
//            throw new Exception("Error parsing Client: " + e.getMessage());
        }
    }

    @Override
    public String getLocalStatus(@NonNull String api) {
        switch (api) {
            case API_NEW:
                return NEW;
            case API_SCREENING_IN_PROGRESS:
                return SCREENING_IN_PROGRESS;
            case API_ELIGIBLE:
                return ELIGIBLE;
            case API_INELIGIBLE:
                return INELIGIBLE;
            case API_LOAN_APPLICATION_NEW:
                return LOAN_APPLICATION_NEW;
            case API_LOAN_APPLICATION_IN_PROGRESS:
                return LOAN_APPLICATION_IN_PROGRESS;
            case API_LOAN_APPLICATION_ACCEPTED:
                return LOAN_APPLICATION_ACCEPTED;
            case API_LOAN_APPLICATION_REJECTED:
                return LOAN_APPLICATION_REJECTED;
            case API_LOAN_APPLICATION_DECLINED:
                return LOAN_APPLICATION_DECLINED;
            case API_ACAT_IN_PROGRESS:
                return ACAT_IN_PROGRESS;
            case API_ACAT_SUBMITTED:
                return ACAT_SUBMITTED;
            case API_ACAT_RESUBMITTED:
                return ACAT_RESUBMITTED;
            case API_ACAT_AUTHORIZED:
                return ACAT_AUTHORIZED;
            case API_ACAT_DECLINED_FOR_REVIEW:
                return ACAT_DECLINED_FOR_REVIEW;
            case API_LOAN_GRANTED:
            case API_LOAN_GRANTED_NEW:
                return LOAN_GRANTED;
            case API_LOAN_PAID:
                return LOAN_PAID;
        }
        return UNKNOWN_STATUS;
    }

    @Override
    public String getApiStatus(@NonNull String local) {
        switch (local) {
            case NEW:
                return API_NEW;
            case SCREENING_IN_PROGRESS:
                return API_SCREENING_IN_PROGRESS;
            case ELIGIBLE:
                return API_ELIGIBLE;
            case INELIGIBLE:
                return API_INELIGIBLE;
            case LOAN_APPLICATION_NEW:
                return API_LOAN_APPLICATION_NEW;
            case LOAN_APPLICATION_IN_PROGRESS:
                return API_LOAN_APPLICATION_IN_PROGRESS;
            case LOAN_APPLICATION_ACCEPTED:
                return API_LOAN_APPLICATION_ACCEPTED;
            case LOAN_APPLICATION_REJECTED:
                return API_LOAN_APPLICATION_REJECTED;
            case LOAN_APPLICATION_DECLINED:
                return API_LOAN_APPLICATION_DECLINED;
            case ACAT_NEW:
                return API_ACAT_NEW;
            case ACAT_IN_PROGRESS:
                return API_ACAT_IN_PROGRESS;
            case ACAT_SUBMITTED:
                return API_ACAT_SUBMITTED;
            case ACAT_RESUBMITTED:
                return API_ACAT_RESUBMITTED;
            case ACAT_AUTHORIZED:
                return API_ACAT_AUTHORIZED;
            case ACAT_DECLINED_FOR_REVIEW:
                return API_ACAT_DECLINED_FOR_REVIEW;
            case LOAN_GRANTED:
                return API_LOAN_GRANTED;
            case LOAN_PAID:
                return API_LOAN_PAID;

        }
        return UNKNOWN_STATUS;
    }

    @Override
    public String getApiGender(@NonNull String local) {
        switch (local) {
            case GENDER_MALE:
                return API_GENDER_MALE;
            case GENDER_FEMALE:
                return API_GENDER_FEMALE;
        }
        return UNKNOWN_GENDER;
    }

    @Override
    public String getLocalGender(@NonNull String api) {
        switch (api) {
            case API_GENDER_MALE:
            case API_GENDER_MALE_CAMEL_CASE:
                return GENDER_MALE;
            case API_GENDER_FEMALE:
            case API_GENDER_FEMALE_CAMEL_CASE:
                return GENDER_FEMALE;
        }
        return UNKNOWN_GENDER;
    }

    @Override
    public String getApiMaritalStatus(@NonNull String local) {
        switch (local) {
            case MARITAL_STATUS_SINGLE:
                return API_MARITAL_STATUS_SINGLE;
            case MARITAL_STATUS_MARRIED:
                return API_MARITAL_STATUS_MARRIED;
            case MARITAL_STATUS_DIVORCED:
                return API_MARITAL_STATUS_DIVORCED;
            case MARITAL_STATUS_WIDOWED:
                return API_MARITAL_STATUS_WIDOWED;
        }
        return UNKNOWN_MARITAL_STATUS;
    }

    @Override
    public String getApiMaritalStatus(@NonNull MaritalStatus maritalStatus) {
        switch (maritalStatus) {
            case SINGLE:
                return API_MARITAL_STATUS_SINGLE;
            case MARRIED:
                return API_MARITAL_STATUS_MARRIED;
            case DIVORCED:
                return API_MARITAL_STATUS_DIVORCED;
            case WIDOWED:
                return API_MARITAL_STATUS_WIDOWED;
        }
        return UNKNOWN_MARITAL_STATUS;
    }

    @Override
    public String getLocalMaritalStatus(@NonNull String api) {
        switch (api) {
            case API_MARITAL_STATUS_SINGLE:
                return MARITAL_STATUS_SINGLE;
            case API_MARITAL_STATUS_MARRIED:
                return MARITAL_STATUS_MARRIED;
            case API_MARITAL_STATUS_DIVORCED:
                return MARITAL_STATUS_DIVORCED;
            case API_MARITAL_STATUS_WIDOWED:
                return MARITAL_STATUS_WIDOWED;
        }
        return UNKNOWN_MARITAL_STATUS;
    }

    @Override
    public String getLocalMaritalStatus(@NonNull MaritalStatus maritalStatus) {
        switch (maritalStatus) {
            case SINGLE:
                return MARITAL_STATUS_SINGLE;
            case MARRIED:
                return MARITAL_STATUS_MARRIED;
            case DIVORCED:
                return MARITAL_STATUS_DIVORCED;
            case WIDOWED:
                return MARITAL_STATUS_WIDOWED;
        }
        return UNKNOWN_MARITAL_STATUS;
    }

    @Override
    public MaritalStatus getMaritalStatusType(@NonNull String local) {
        switch (local) {
            case MARITAL_STATUS_MARRIED:
                return MaritalStatus.MARRIED;
            case MARITAL_STATUS_DIVORCED:
                return MaritalStatus.DIVORCED;
            case MARITAL_STATUS_WIDOWED:
                return MaritalStatus.WIDOWED;
            default:
                return MaritalStatus.SINGLE;
        }
    }
}
