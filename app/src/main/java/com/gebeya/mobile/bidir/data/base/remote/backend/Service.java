package com.gebeya.mobile.bidir.data.base.remote.backend;

/**
 * Outlines the various service types that are hosted on the API side. The abstraction
 * into service types is necessary in order to avoid returning service names directly from the
 * {@link com.gebeya.mobile.bidir.BuildConfig} class, whose values are not available during unit
 * tests thus breaking the CI builds.
 */
public enum Service {

    /**
     * Authentication service.
     */
    AUTH,

    /**
     * MFI service.
     */
    MFI,

    /**
     * Screening service.
     */
    SCREENINGS,

    /**
     * User service.
     */
    USERS,

    /**
     * Task service.
     */
    TASKS,

    /**
     * Form service.
     */
    FORMS,

    /**
     * Loan applications service.
     */
    LOAN_APPLICATIONS,

    /**
     * Clients service.
     */
    CLIENTS,

    /**
     * ACAT service.
     */
    ACAT,
    /**
     * ACAT form service.
     */
    ACAT_FORM,

    /**
     * LOAN PRODUCT service.
     */
    LOAN_PRODUCT,

    /**
     * ACAT PROCESSOR
     */
    ACAT_PROCESSOR,

    /**
     * LOAN PROPOSAL
     */
    LOAN_PROPOSAL,

    /**
     * GROUP SERVICE
     */
    GROUPS
}