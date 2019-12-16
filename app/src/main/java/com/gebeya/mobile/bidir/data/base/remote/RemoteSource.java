package com.gebeya.mobile.bidir.data.base.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

/**
 * Interface describing the basic behavior of a remote source, which is the main
 * entry point to connection to the API/Internet.
 */
public interface RemoteSource<T> {

    /**
     * Builds up Retrofit and all its components, including the service interface class
     * implementation.
     */
    RemoteSource<T> build();

    /**
     * Initiates a network call. This method checks (in the process) to ensure that
     * there is an Internet connection available, and also any errors returned from the
     * API.
     *
     * @param request The initiating caller/source of request. This could be any Retrofit
     *                service interface that returns an Observable.
     */
    Observable<JsonObject> call(@NonNull Observable<JsonObject> request);

    /**
     * Takes in a service type (from {@link Service}), along with the retrofit Service interface
     * and uses Retrofit to build up and return the service implementation in the process.
     * The returned service implementation is returned wrapped around in an Observable.
     *
     * @param type         the Service type to use, such as {@link Service#AUTH}.
     * @param serviceClass Retrofit service {@link Class} instance.
     */
    void setParams(@NonNull Service type, @NonNull Class<T> serviceClass);
}