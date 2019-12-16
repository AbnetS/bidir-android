package com.gebeya.mobile.bidir.data.client;

/**
 * Represents the internal state of the marital status of a {@link Client}.
 * These states are there instead of storing and displaying the API given ones, as those are
 * not language agnostic.
 */
public enum MaritalStatus {
    SINGLE, MARRIED, DIVORCED, WIDOWED
}