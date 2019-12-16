package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.ConnectionProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.EndpointProvider;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.impl.util.error.ErrorHandler;
import com.gebeya.mobile.bidir.impl.util.network.NetworkWatcher;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.BoxStore;
import io.reactivex.Observable;

/**
 * Implementation for a {@link RemoteSource}. This class has members that are injected in via the
 * {@link toothpick.Toothpick} library.
 */
public abstract class BaseRemoteSource<T> implements RemoteSource<T> {

    @Inject NetworkWatcher watcher;
    @Inject ErrorHandler handler;
    @Inject EndpointProvider endpointProvider;
    @Inject ConnectionProvider connectionProvider;

    @Inject BoxStore store;

    protected T service;
    protected Class<T> serviceClass;
    protected Service type;

    @Override
    public RemoteSource<T> build() {
        final List<User> users = store.boxFor(User.class).getAll();
        service = connectionProvider.createService(
                endpointProvider.getScheme(),
                endpointProvider.getAuthority(),
                endpointProvider.getServiceName(type),
                users.isEmpty() ? null : users.get(0),
                serviceClass
        );
        return this;
    }

    @Override
    public Observable<JsonObject> call(@NonNull Observable<JsonObject> source) {
        return watcher.connected()
                .flatMap(connected -> source)
                .onErrorResumeNext(handler.checkErrorObject());
    }

    @Override
    public void setParams(@NonNull Service type, @NonNull Class<T> serviceClass) {
        this.type = type;
        this.serviceClass = serviceClass;
    }
}