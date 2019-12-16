package com.gebeya.mobile.bidir.impl.util.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface representing a request state
 */
public interface RequestState {

    enum State {
        LOADING, COMPLETED, ERROR
    }

    void setLoading();
    boolean loading();
    boolean error();
    void setComplete();
    boolean complete();
    void setState(@Nullable State state);
    State getState();
    void setError(@NonNull Throwable throwable);
    Throwable getError();
    void reset();
}
