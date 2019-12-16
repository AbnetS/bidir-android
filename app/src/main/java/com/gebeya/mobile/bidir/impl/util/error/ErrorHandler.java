package com.gebeya.mobile.bidir.impl.util.error;

import android.support.annotation.Nullable;

import com.google.gson.JsonObject;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Error handler interface contract for network errors.
 */
public interface ErrorHandler {
    /**
     * Intercept the observable coming down the chain and retrieve its {@link Throwable} error
     * object, if any.
     */
    Function<Throwable, ObservableSource<JsonObject>> checkErrorObject();

    /**
     * Retrieve the error String message from the given JsonObject object.
     */
    @Nullable String getError(JsonObject object) throws Exception;

    /**
     * Retrieve the JsonObject from the given {@link Throwable} object (if any).
     */
    @Nullable JsonObject getBody(Throwable throwable) throws Exception;
}