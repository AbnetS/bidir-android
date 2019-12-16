package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.gpslocation.GPSLocation;

/**
 * Simple data class to encapsulate a single GPS location's position data along with other useful
 * properties, such as position.
 */
public class LocationItem {

    public final GPSLocation location;
    public int position;

    public LocationItem(@NonNull GPSLocation location, int position) {
        this.location = location;
        this.position = position;
    }
}