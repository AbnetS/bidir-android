package com.gebeya.mobile.bidir.impl.util.location.play_services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.GpsStateCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.annotations.NonNull;

/**
 * Implementation for the {@link PlayServicesGpsManager} interface, using the Google Play Services
 * fused location provider.
 */
public class BasePlayServicesGpsManager implements PlayServicesGpsManager {

    private final LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult result) {
            if (result == null) return;

            final Location location = result.getLastLocation();
            if (location == null) return;

            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            coordinates[0] = latitude;
            coordinates[1] = longitude;

            if (callback != null) {
                callback.onGpsLocationUpdated(latitude, longitude);
            }
            Utils.d(BasePlayServicesGpsManager.this, "Latitude: %f, Longitude: %f", latitude, longitude);
        }

        @Override
        public void onLocationAvailability(LocationAvailability availability) {
            final boolean available = availability.isLocationAvailable();
            Utils.d(BasePlayServicesGpsManager.this, "Location available: %s", available);
        }
    };

    private final LocationManager manager;
    private final LocationRequest request; // TODO: Use LocationSettingsRequest.Builder to test
    private final FusedLocationProviderClient client;
    private GpsStateCallback callback;
    private boolean running;
    private final double coordinates[] = new double[2];

    public BasePlayServicesGpsManager(@NonNull Context context) {
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        client = LocationServices.getFusedLocationProviderClient(context);
        running = false;
        request = new LocationRequest();
        initializeLocationRequest();
    }

    private void initializeLocationRequest() {
        request.setInterval(Constants.GPS_UPDATE_INTERVAL);
        request.setFastestInterval(Constants.GPS_UPDATE_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void start() {
        if (running) return;

        client.requestLocationUpdates(request, locationCallback, null);
        running = true;

        if (callback != null) callback.onGpsStateChanged(true);
    }

    @Override
    public void stop() {
        if (!running) return;

        client.removeLocationUpdates(locationCallback);
        running = false;

        if (callback != null) callback.onGpsStateChanged(false);
    }

    @Override
    public boolean running() {
        return running;
    }

    @Override
    public boolean enabled() {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
