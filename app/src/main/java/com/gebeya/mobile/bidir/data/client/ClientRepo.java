package com.gebeya.mobile.bidir.data.client;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.client.remote.ClientRemoteSource;
import com.gebeya.mobile.bidir.data.client.remote.RegisterDto;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Concrete implementation for the {@link ClientRepoSource}
 */
public class ClientRepo implements ClientRepoSource {

    @Inject ClientLocalSource local;
    @Inject ClientRemoteSource remote;

    @Inject UserLocalSource userLocal;

    public ClientRepo() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
    }

    @Override
    public Completable register(@NonNull RegisterDto register, @NonNull Client client) {
        return userLocal.first()
                .flatMap(data -> {
                    final User user = data.get();
                    client.createdBy = user._id;
                    return local.markForCreation(client)
                            .andThen(Observable.just(data));
                })
                .flatMap(data -> remote.register(register, data.get()))
                .flatMapCompletable(created -> {
                    local.put(created);
                    return Completable.complete();
                });
    }

    @Override
    public Completable update(@NonNull RegisterDto register, @NonNull Client client) {
        return local.markForUpload(client)
                .flatMap(marked -> remote.update(register, marked))
                .flatMapCompletable(updated -> {
                    updated.uploaded = true;
                    updated.modified = false;
                    local.put(updated);
                    return Completable.complete();
                });
    }

    @Override
    public Observable<Client> updateStatus(@NonNull Client client) {
        return remote.upload(client)
                .flatMap(c -> {
                    local.put(c);
                    return Observable.just(client);
                });
    }

    @Override
    public Observable<List<Client>> fetchAll() {
        return userLocal.first()
                .flatMap(data -> local.getAll(data.get()._id))
                .flatMap(clients ->
                        clients.isEmpty() ? fetchForceAll() : Observable.just(clients)
                );
    }

    @Override
    public Observable<List<Client>> fetchForceAll() {
        return remote.getAll()
                .flatMap(clients -> {
                    local.putAll(clients);
                    return Observable.just(clients);
                });
    }

    @Override
    public Observable<Client> fetch(@NonNull String id) {
        return local.get(id).flatMap(data -> {
            if (data.empty()) {
                return remote.getOne(id)
                        .flatMap(client -> {
                            local.put(client);
                            return Observable.just(client);
                        });
            } else {
                return Observable.just(data.get());
            }
        });
    }

    @Override
    public Observable<Client> fetchForce(@NonNull String id) {
        return remote.getOne(id)
                .flatMap(client -> {
                    local.put(client);
                    return Observable.just(client);
                });
    }

    @Override
    public int size() {
        return local.size();
    }
}