package com.gebeya.mobile.bidir.data.base.remote.backend.sync;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.util.Loggable;
import com.gebeya.mobile.bidir.impl.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation for the sync state.
 */
public class BaseSyncState implements SyncState, Loggable {

    private boolean busy;

    private final List<SyncCallback> callbacks;

    public BaseSyncState() {
        callbacks = new ArrayList<>();
    }

    @Override
    public void setBusy() {
        if (busy) return;
        busy = true;
        notifyCallbacks();
    }

    @Override
    public boolean busy() {
        return busy;
    }

    @Override
    public void setIdle() {
        if (!busy) return;
        busy = false;
        notifyCallbacks();
    }

    @Override
    public boolean idle() {
        return !busy;
    }

    @Override
    public void addSyncCallback(@NonNull final SyncCallback callback) {
        d("Adding SyncCallback: " + callback);
        callbacks.add(callback);
        d("-> Callback count: %d", callbacks.size());
    }

    @Override
    public void removeSyncCallback(@NonNull final SyncCallback callback) {
        d("Removing SyncCallback: " + callback);
        callbacks.remove(callback);
        d("-> Callback count: %d", callbacks.size());
    }

    @Override
    public void notifyCallbacks() {
        if (callbacks.isEmpty()) return;
        final int length = callbacks.size();
        for (int i = 0; i < length; i++) {
            final SyncCallback callback = callbacks.get(i);
            if (busy) {
                callback.onSyncStarted();
            } else {
                callback.onSyncStopped();
            }
        }
    }

    @Override
    public void d(final String message) {
        Utils.d(this, message);
    }

    @Override
    public void d(final String message, final Object... formatArgs) {
        Utils.d(this, message, formatArgs);
    }

    @Override
    public void e(final String message) {
        Utils.e(this, message);
    }

    @Override
    public void e(final String message, final Object... formatArgs) {
        Utils.e(this, message, formatArgs);
    }
}
