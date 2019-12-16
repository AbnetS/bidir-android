package com.gebeya.mobile.bidir.impl.rx;

import android.support.annotation.Nullable;

/**
 * Data class for representing a value that might exist or not. This is a simple port of the
 * {@link java.util.Optional} class, that is only available in Java 8
 */
public final class Data<T> {

    private final T value;

    public Data(@Nullable T value) {
        this.value = value;
    }

    public boolean empty() {
        return value == null;
    }

    public T get() {
        if (value == null) throw new NullPointerException("Value is null");
        return value;
    }
}