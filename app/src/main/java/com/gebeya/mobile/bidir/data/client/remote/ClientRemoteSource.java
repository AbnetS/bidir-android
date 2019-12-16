package com.gebeya.mobile.bidir.data.client.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.user.User;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface contract definition for the Client's remote source of data.
 */
public interface ClientRemoteSource {

    /**
     * Register a client.
     *
     * @param register Registration information for a client.
     * @param user     used for the creator ID and branch ID.
     * @return Completable for the operation.
     */
    Observable<Client> register(@NonNull RegisterDto register, @NonNull User user);

    /**
     * Update a given client parameter with the new register data.
     *
     * @param register new data for a client to saveLoanProposal on the API.
     * @param client   old data for a client to saveLoanProposal.
     */
    Observable<Client> update(@NonNull RegisterDto register, @NonNull Client client);

    /**
     * Upload the given client's data to the API.
     *
     * @param client Client whose data to upload to the API.
     * @return Same client.
     */
    Observable<Client> upload(@NonNull Client client);

    /**
     * Retrieve a list of all the Clients from the API.
     *
     * @return List of all the clients as an Observable list.
     */
    Observable<List<Client>> getAll();

    /**
     * Get a single client from the API.
     *
     * @param clientId String ID for the client to retrieve.
     * @return Observable of a single client returned from the API.
     */
    Observable<Client> getOne(@NonNull String clientId);

}