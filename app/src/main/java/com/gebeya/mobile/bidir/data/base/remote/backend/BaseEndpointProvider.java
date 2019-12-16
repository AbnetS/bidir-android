package com.gebeya.mobile.bidir.data.base.remote.backend;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.BuildConfig;

/**
 * Production implementation for the {@link EndpointProvider} interface class.
 * This is necessary to avoid running into issues during unit testing on the
 * JVM side, when there are no {@link com.gebeya.mobile.bidir.BuildConfig} files available.
 */
public class BaseEndpointProvider implements EndpointProvider {

    @Override
    public String getScheme() {
        return BuildConfig.SCHEME;
    }

    @Override
    public String getAuthority() {
        return BuildConfig.AUTHORITY;
    }

    @Override
    public String getServiceName(@NonNull Service service) {
        switch (service) {
            case AUTH:
                return BuildConfig.SERVICE_AUTH;
            case MFI:
                return BuildConfig.SERVICE_MFI;
            case SCREENINGS:
                return BuildConfig.SERVICE_SCREENINGS;
            case USERS:
                return BuildConfig.SERVICE_USERS;
            case TASKS:
                return BuildConfig.SERVICE_TASKS;
            case FORMS:
                return BuildConfig.SERVICE_FORMS;
            case LOAN_APPLICATIONS:
                return BuildConfig.SERVICE_LOAN_APPLICATIONS;
            case CLIENTS:
                return BuildConfig.SERVICE_CLIENTS;
            case ACAT:
                return BuildConfig.SERVICE_ACAT;
            case ACAT_FORM:
                return BuildConfig.SERVICE_ACAT_FORM;
            case LOAN_PRODUCT:
                return BuildConfig.SERVICE_LOAN_PRODUCT;
            case ACAT_PROCESSOR:
                return BuildConfig.SERVICE_ACAT_PROCESSOR;
            case LOAN_PROPOSAL:
                return BuildConfig.SERVICE_LOAN_PROPOSAL;
            case GROUPS:
                return BuildConfig.SERVICE_GROUPS;
        }
        return null;
    }
}