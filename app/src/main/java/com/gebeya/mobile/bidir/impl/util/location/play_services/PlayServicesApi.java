package com.gebeya.mobile.bidir.impl.util.location.play_services;

/**
 * Interface that defines the availability of the Google Play Services.
 * This is necessary if we need to be able to access location services via Google Play Services.
 */
public interface PlayServicesApi {
    boolean missingPlayServices();
    boolean isUserResolvableError();
    int getStatus();
}