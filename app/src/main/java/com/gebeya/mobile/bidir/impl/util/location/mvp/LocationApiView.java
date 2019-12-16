package com.gebeya.mobile.bidir.impl.util.location.mvp;

/**
 * Base interface for Google Play Services API availability checks.
 */
public interface LocationApiView {
    void showPlayServicesErrorDialog(int status);
}