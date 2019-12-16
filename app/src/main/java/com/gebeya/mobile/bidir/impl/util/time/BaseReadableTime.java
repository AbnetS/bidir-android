package com.gebeya.mobile.bidir.impl.util.time;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Locale;

/**
 * Concrete implementation for the {@link ReadableTime} interface
 */
public class BaseReadableTime implements ReadableTime {

    private static ReadableTime instance;

    public static ReadableTime getInstance() {
        return instance == null ? instance = new BaseReadableTime() : instance;
    }

    @Override
    public String toReadable(@NonNull DateTime dateTime) {
        final DateTime now = new DateTime();
        final Period period = new Period(dateTime, now);

        final int days = period.getDays();

        final StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(dateTime.monthOfYear().getAsShortText());
            builder.append(" ");
            builder.append(dateTime.getDayOfMonth());
        } else {
            builder.append(String.format(Locale.getDefault(), "%02d", dateTime.hourOfDay().get()));
            builder.append(":");
            builder.append(String.format(Locale.getDefault(), "%02d",dateTime.getMinuteOfHour()));
        }

        return builder.toString();
    }
}