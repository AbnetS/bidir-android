package com.gebeya.mobile.bidir.data.client;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.client.remote.RegisterDto;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract implementation for the client.
 */
public interface ClientRepoSource extends
        FetchMany<Client>,
        FetchOne<Client>,
        ReadSize {

    /**
     * RegisterDto a client.
     *
     * @param register Registration information for a client.
     * @param client   Temporary client to create with.
     * @return Completable for the operation.
     */
    Completable register(@NonNull RegisterDto register, @NonNull Client client);

    /**
     * Update a given client parameter with the new register data.
     *
     * @param register new data for a client to saveLoanProposal on the API.
     * @param client   old data for a client to saveLoanProposal.
     */
    Completable update(@NonNull RegisterDto register, @NonNull Client client);

    /**
     * Update the status of a given Client
     * @param client to getById the current client info.
     */
    Observable<Client> updateStatus(@NonNull Client client);
}