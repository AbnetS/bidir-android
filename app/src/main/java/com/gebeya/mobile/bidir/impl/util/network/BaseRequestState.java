package com.gebeya.mobile.bidir.impl.util.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Base implementation for a {@link RequestState} object.
 */
public class BaseRequestState implements RequestState {

    private State state;
    private Throwable throwable;

    public BaseRequestState() {
        reset();
    }

    @Override
    public void setLoading() {
        state = State.LOADING;
    }

    @Override
    public boolean loading() {
        return state == State.LOADING;
    }

    @Override
    public boolean error() {
        return state == State.ERROR;
    }

    @Override
    public void setComplete() {
        state = State.COMPLETED;
    }

    @Override
    public boolean complete() {
        return state == State.COMPLETED;
    }

    @Override
    public void setState(@Nullable State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setError(@NonNull Throwable throwable) {
        state = State.ERROR;
        this.throwable = throwable;
    }

    @Override
    public Throwable getError() {
        return throwable;
    }

    @Override
    public void reset() {
        state = null;
        throwable = null;
    }
}
