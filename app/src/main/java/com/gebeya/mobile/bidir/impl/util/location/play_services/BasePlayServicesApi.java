package com.gebeya.mobile.bidir.impl.util.location.play_services;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.reactivex.annotations.NonNull;

/**
 * Implementation for the {@link PlayServicesApi} interface.
 */
public class BasePlayServicesApi implements PlayServicesApi {

    private final Context context;
    private final GoogleApiAvailability apiAvailability;

    public BasePlayServicesApi(@NonNull Context context, @NonNull GoogleApiAvailability apiAvailability) {
        this.context = context;
        this.apiAvailability = apiAvailability;
    }

    @Override
    public boolean missingPlayServices() {
        final int status = apiAvailability.isGooglePlayServicesAvailable(context);
        return status != ConnectionResult.SUCCESS;
    }

    @Override
    public boolean isUserResolvableError() {
        final int status = apiAvailability.isGooglePlayServicesAvailable(context);
        return apiAvailability.isUserResolvableError(status);
    }

    @Override
    public int getStatus() {
        return apiAvailability.isGooglePlayServicesAvailable(context);
    }
}
