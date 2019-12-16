package com.gebeya.mobile.bidir.data.base.remote.backend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.user.User;

import retrofit2.Retrofit;

/**
 * Responsible for providing backend connection resources, that are needed to send and receive
 * data over the network.
 */
public interface ConnectionProvider {

    /**
     * Create and return a {@link Retrofit} service with the given interface service class.
     *
     * @param scheme       HTTP scheme of the API.
     * @param authority    HTTP authority of the API.
     * @param user         Used to automatically inject an authentication token (if provided).
     * @param service      Full service URL name to connect to, as a base URL.
     * @param serviceClass service class to initializeACAT interface from.
     * @param <T>          type for service.
     * @return created service class instance.
     */
    <T> T createService(@NonNull String scheme,
                        @NonNull String authority,
                        @NonNull String service,
                        @Nullable User user,
                        @NonNull Class<T> serviceClass);
}