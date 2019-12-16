package com.gebeya.mobile.bidir.data.client.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Implementation for the {@link ClientRemoteSource} interface contract.
 */
public class ClientRemote extends BaseRemoteSource<ClientService> implements ClientRemoteSource {

    @Inject ClientParser clientParser;

    public ClientRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.CLIENTS, ClientService.class);
    }

    @Override
    public Observable<Client> register(@NonNull RegisterDto register, @NonNull User user) {
        final JsonObject geolocation = new JsonObject();
        geolocation.addProperty("latitude", register.latitude);
        geolocation.addProperty("longitude", register.longitude);

        return build()
                .call(service.register(
                        create(register.firstName),
                        create(register.lastName),
                        create(register.surname),
                        create(clientParser.getApiGender(register.gender)),
                        create(register.idNumber),
                        create(createDateOfBirth(register)),
                        create(clientParser.getApiMaritalStatus(register.maritalStatus)),
                        create(register.woreda),
                        create(register.kebele),
                        create(register.houseNumber),
                        create(register.phoneNumber),
                        create(String.valueOf(register.householdMemberCount)),
                        create(user.branchId),
                        create(user._id),
                        create(createSpouse(
                                register.spouseFirstName,
                                register.spouseLastName,
                                register.spouseSurname,
                                register.spouseIdNumber,
                                register.maritalStatus
                        )),
                        create(geolocation.toString()),
                        create(register.idCardFile),
                        create(register.photoFile),
                        create(String.valueOf(register.for_group))
                ))
                .map(clientParser::parse);
    }

    private RequestBody create(String value) {
        if (value != null) {
            return RequestBody.create(MediaType.parse("text/plain"), value);
        }
        return null;
    }

    private RequestBody create(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    @Override
    public Observable<Client> upload(@NonNull final Client client) {
        final JsonObject request = new JsonObject();

        request.addProperty("first_name", client.firstName);
        request.addProperty("last_name", client.lastName);
        request.addProperty("grandfather_name", client.surname);
        request.addProperty("gender", clientParser.getApiGender(client.gender));
        request.addProperty("date_of_birth", client.dateOfBirth.toString());
        request.add("geolocation", createGeolocation(client.latitude, client.longitude));
        request.addProperty("civil_status", clientParser.getApiMaritalStatus(client.maritalStatus));
        request.addProperty("household_members_count", client.householdMemberCount);
        request.addProperty("national_id_no", client.idNumber);
        request.addProperty("woreda", client.woreda);
        request.addProperty("kebele", client.kebele);
        request.addProperty("house_no", client.houseNumber);
        request.addProperty("phone", client.phoneNumber);
        request.addProperty("status", client.status);
        request.addProperty("for_group", client.forGroup);
        request.add("spouse", createSpouseJson(
                client.spouseFirstName,
                client.spouseLastName,
                client.spouseSurname,
                client.spouseIdNumber,
                client.maritalStatus
        ));

        return build()
                .call(service.update(client._id, request))
                .map(clientParser::parse);
    }

    @Override
    public Observable<Client> update(@NonNull RegisterDto register, @NonNull Client client) {
        final JsonObject request = new JsonObject();
        // TODO: Add modified personal photo (if any)
        if (register.firstName != null) request.addProperty("first_name", register.firstName);
        if (register.lastName != null) request.addProperty("last_name", register.lastName);
        if (register.surname != null) request.addProperty("grandfather_name", register.surname);
        if (register.gender != null)
            request.addProperty("gender", clientParser.getApiGender(register.gender));
        if (register.day + register.month + register.year != 0) {
            request.addProperty("date_of_birth", createDateOfBirth(register));
        }
        if (Utils.hasLocation(register.latitude, register.longitude)) {
            request.add("geolocation", createGeolocation(register.latitude, register.longitude));
        }

        if (register.maritalStatus != null) {
            request.addProperty("civil_status", clientParser.getApiMaritalStatus(register.maritalStatus));
        }

        if (register.householdMemberCount != null) {
            request.addProperty("household_members_count", register.householdMemberCount);
        }
        // TODO: Add modified id card (if any)
        if (register.idNumber != null) request.addProperty("national_id_no", register.idNumber);
        if (register.woreda != null) request.addProperty("woreda", register.woreda);
        if (register.kebele != null) request.addProperty("kebele", register.kebele);
        if (register.houseNumber != null) request.addProperty("house_no", register.houseNumber);
        if (register.phoneNumber != null) request.addProperty("phone", register.phoneNumber);


        if (register.spouseIdNumber != null) request.add("spouse", createSpouseJson(
                register.spouseFirstName,
                register.spouseLastName,
                register.spouseSurname,
                register.spouseIdNumber,
                register.maritalStatus
        ));

        request.addProperty("for_group", client.forGroup);
        return build()
                .call(service.update(client._id, request))
                .map(clientParser::parse);
    }

    @Override
    public Observable<Client> getOne(@NonNull String clientId) {
        return build()
                .call(service.getOne(clientId))
                .map(clientParser::parse);
    }

    @Override
    public Observable<List<Client>> getAll() {
        return build()
                .call(service.getAll())
                .map(object -> {
                    final List<Client> clients = new ArrayList<>();
                    final JsonArray array = object.getAsJsonArray("docs");
                    final int size = array.size();
                    for (int i = 0; i < size; i++) {
                        Client client = clientParser.parse(array.get(i).getAsJsonObject());
                        if (client != null) {
                            clients.add(client);
                        }
                    }
                    return clients;
                });
    }

    private JsonObject createSpouseJson(@NonNull String firstName, @NonNull String lastName,
                                @NonNull String surname, @NonNull String idNumber,
                                @Nullable String maritalStatus) {
        final JsonObject object = new JsonObject();

        if (maritalStatus == null || !maritalStatus.equals(ClientParser.API_MARITAL_STATUS_MARRIED)) {
            object.addProperty("first_name", "");
            object.addProperty("last_name", "");
            object.addProperty("grandfather_name", "");
            object.addProperty("national_id_no", "");

            return object;
        }

        object.addProperty("first_name", firstName);
        object.addProperty("last_name", lastName);
        object.addProperty("grandfather_name", surname);
        object.addProperty("national_id_no", idNumber);

        return object;
    }

    private String createSpouse(@NonNull String firstName, @NonNull String lastName,
                                @NonNull String surname, @NonNull String idNumber,
                                @Nullable String maritalStatus) {
        final JsonObject object = new JsonObject();

        if (maritalStatus == null || !maritalStatus.equals(ClientParser.API_MARITAL_STATUS_MARRIED)) {
            object.addProperty("first_name", "");
            object.addProperty("last_name", "");
            object.addProperty("grandfather_name", "");
            object.addProperty("national_id_no", "");

            return object.toString();
        }

        object.addProperty("first_name", firstName);
        object.addProperty("last_name", lastName);
        object.addProperty("grandfather_name", surname);
        object.addProperty("national_id_no", idNumber);

        return object.toString();
    }

    private String createDateOfBirth(@NonNull RegisterDto register) {
        return String.format(Locale.getDefault(), Constants.DATE_REGISTER_FORMAT,
                register.year, register.day, register.month);
    }

    private JsonObject createGeolocation(double latitude, double longitude) {
        final JsonObject object = new JsonObject();

        object.addProperty("latitude", latitude);
        object.addProperty("longitude", longitude);

        return object;
    }
}