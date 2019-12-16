package com.gebeya.mobile.bidir.data.base.remote.backend.sync.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.BaseSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncService;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemoteSource;
import com.gebeya.mobile.bidir.data.client.remote.RegisterDto;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Synchronization service used to upload {@link Client} data to
 * the remote API.
 */
public class ClientSyncService extends BaseSyncService {

    @Inject BoxStore store;
    private final Box<User> userBox;

    @Inject ClientLocalSource clientLocal;
    @Inject ClientRemoteSource clientRemote;
    @Inject ClientRepoSource clientRepo;

    @Inject ClientSyncState state;

    public ClientSyncService() {
        super(ClientSyncService.class.getSimpleName());

        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
        userBox = store.boxFor(User.class);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        d("Service Started");
        if (state.busy()) {
            d("-> Service is already running");
            return;
        }

        final List<User> users = userBox.getAll();
        if (users.isEmpty()) {
            e("-> Cannot upload data: User is currently logged out");
            return;
        }

        final User user = users.get(0);

        d("Getting a list of modified clients to upload");
        final List<Client> modifiedClients = clientLocal.getAllModifiedNonUploaded();
        final int modifiedLength = modifiedClients.size();
        if (modifiedLength == 0) {
            d("-> No modified clients to upload found");
        } else {
            d("-> Found %d modified client%s to upload", modifiedLength, modifiedLength == 1 ? "" : "s");
            d("Uploading data...");
            state.setBusy();
            for (int i = 0; i < modifiedLength; i++) {
                final Client client = modifiedClients.get(i);
                d("-> Uploading client: " + client._id);
                clientRemote
                        .upload(client)
                        .subscribe(uploaded -> {
                            d("-> Client %s uploaded", client._id);
                            uploaded.uploaded = true;
                            uploaded.modified = false;
                            uploaded.updatedAt = new DateTime();
                            clientLocal.put(uploaded);
                        }, throwable -> {
                            e("-> Uploading client %s failed:", client._id);
                            throwable.printStackTrace();
                        });
            }
        }

        d("Getting a list of created clients to upload");
        final List<Client> createdClients = clientLocal.getAllPendingCreationNonUploaded();
        final int createdLength = createdClients.size();
        if (createdLength == 0) {
            d("-> No created clients to upload found");
        } else {
            d("-> Found %d created client%s to upload", createdLength, createdLength == 1 ? "" : "s");
            d("Uploading data...");
            state.setBusy();

            for (int i = 0; i < createdLength; i++) {
                final Client client = createdClients.get(i);
                final RegisterDto register = createDto(client);
                clientRemote
                        .register(register, user)
                        .subscribe(uploaded -> {
                            d("-> Client %s created", uploaded._id);
                            clientLocal.put(uploaded);
                        }, throwable -> {
                            // TODO: Handle case where there is an API specific error, such as a client duplicate error.
                            e("-> Creating client %s failed: ", client.idNumber);
                            throwable.printStackTrace();
                        });
            }
        }

        if (createdLength > 0) {
            d("-> %d newly created client%s found. Starting up the ComplexScreeningSyncService to download their screenings...",
                    createdLength, createdLength == 1 ? "" : "s"
            );
            final Context context = getApplicationContext();
            context.startService(new Intent(context, ComplexScreeningSyncService.class));
        }

        state.setIdle();
    }

    private RegisterDto createDto(@NonNull Client client) {
        final RegisterDto register = new RegisterDto();

        register.firstName = client.firstName;
        register.photoFile = new File(client.photoUrl);
        register.lastName = client.lastName;
        register.surname = client.surname;
        register.gender = client.gender;

        register.day = client.dateOfBirth.getDayOfMonth();
        register.month = client.dateOfBirth.getMonthOfYear();
        register.year = client.dateOfBirth.getYear();

        register.latitude = client.latitude;
        register.longitude = client.longitude;

        register.maritalStatus = client.maritalStatus;
        register.householdMemberCount = String.valueOf(client.householdMemberCount);

        register.idCardFile = new File(client.idCardUrl);
        register.idNumber = client.idNumber;
        register.woreda = client.woreda;
        register.kebele = client.kebele;
        register.houseNumber = client.houseNumber;
        register.phoneNumber = client.phoneNumber;

        register.spouseIdNumber = client.spouseIdNumber;
        register.spouseFirstName = client.spouseFirstName;
        register.spouseLastName = client.spouseLastName;
        register.spouseSurname = client.spouseSurname;

        return register;
    }
}