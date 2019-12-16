package com.gebeya.mobile.bidir.data.base.remote.backend;

import android.support.annotation.NonNull;

/**
 * Defines responsibility for returning endpoint URLs by the implementing class.
 */
public interface EndpointProvider {

    /**
     * Returns the HTTP Scheme (such as http/https) of the implementation.
     */
    String getScheme();

    /**
     * Returns the HTTP authority (such as api.dev.bidir.gebeya.io) for the underlying
     * implementation.
     */
    String getAuthority();

    /**
     * Returns a service URL, given the {@link Service} type that has been specified.
     */
    String getServiceName(@NonNull Service service);
}