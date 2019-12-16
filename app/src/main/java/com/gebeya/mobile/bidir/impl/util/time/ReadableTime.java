package com.gebeya.mobile.bidir.impl.util.time;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

/**
 * Interface for adding implementations for readable date and time.
 */
public interface ReadableTime {

    /**
     * Returns a readable time (friendly for humans)
     */
    String toReadable(@NonNull DateTime dateTime);
}