package com.gebeya.mobile.bidir.data.base.remote.backend.sync;

/**
 * Callback interface for sync services.
 */
public interface SyncCallback {
    /**
     * Invoked when the sync has started.
     */
    void onSyncStarted();

    /**
     * Invoked when the sync has stopped.
     */
    void onSyncStopped();
}
