package com.gebeya.mobile.bidir.impl.util.location;

/**
 * Interface for the state of GPS (when it is registered or not registered)
 */
public interface GpsStateCallback {
    void onGpsStateChanged(boolean listening);
    void onGpsLocationUpdated(double latitude, double longitude);
}
