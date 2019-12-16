package com.gebeya.mobile.bidir.impl.util.location;

import android.support.annotation.Nullable;

/**
 * Interface for the location/GPS management
 */
public interface GpsManager {

    /**
     * Start listening for GPS coordinates
     */
    void start();

    /**
     * Stop listening for GPS coordinates
     */
    void stop();

    /**
     * Returns whether the GPS tracking is running or not
     */
    boolean running();

    /**
     * Returns whether GPS is enabled or not
     */
    boolean enabled();

    /**
     * Retrieves the current GSP position
     *
     * @return combination of the latitude and longitude for the current position.
     */
    double[] getPosition();

    /**
     * Set the {@link GpsStateCallback} callback object.
     */
    void addGpsStateCallback(@Nullable GpsStateCallback callback);

    /**
     * Remove the {@link GpsStateCallback} callback object.
     */
    void removeGspStateCallback();
}