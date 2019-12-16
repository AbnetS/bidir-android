package com.gebeya.mobile.bidir.impl.util.location.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.GpsStateCallback;

/**
 * Implementation of the {@link DefaultGpsManager} interface, using the default android location
 * providers.
 */
public class BaseDefaultGpsManager implements DefaultGpsManager {

    private final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            coordinates[0] = latitude;
            coordinates[1] = longitude;

            if (callback != null) {
                callback.onGpsLocationUpdated(latitude, longitude);
            }

            Utils.d(BaseDefaultGpsManager.this, "Lat: %f, Long: %f", latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            stop();
        }
    };

    private LocationManager locationManager;
    private GpsStateCallback callback;
    private boolean running;
    private final double[] coordinates = new double[2];

    public BaseDefaultGpsManager(@NonNull Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        running = false;
    }

    @Override
    @SuppressLint("MissingPermission")
    public void start() {
        if (running) return;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        running = true;

        if (callback != null) callback.onGpsStateChanged(true);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void stop() {
        if (!running) return;

        locationManager.removeUpdates(listener);
        running = false;

        if (callback != null) callback.onGpsStateChanged(false);
    }

    @Override
    public boolean running() {
        return running;
    }

    @Override
    public boolean enabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public double[] getPosition() {
        return coordinates;
    }

    @Override
    public void addGpsStateCallback(@Nullable GpsStateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void removeGspStateCallback() {
        callback = null;
        stop();
    }
}