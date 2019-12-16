package com.gebeya.mobile.bidir.data.base.remote.backend.sync;

import android.support.annotation.NonNull;

/**
 * State interface for sync services.
 */
public interface SyncState {

    /**
     * Update the state of the sync service to busy.
     */
    void setBusy();

    /**
     * Determines whether the sync service is busy or not.
     */
    boolean busy();

    /**
     * Update the state of the sync service to idle.
     */
    void setIdle();

    /**
     * Determines whether the sync service is idle or not.
     */
    boolean idle();

    /**
     * Add the provided callback to the list of callbacks.
     *
     * @param callback Callback to add.
     */
    void addSyncCallback(@NonNull SyncCallback callback);

    /**
     * Remove the provided callback from the list of callbacks.
     *
     * @param callback Callback to remove.
     */
    void removeSyncCallback(@NonNull SyncCallback callback);

    /**
     * Notify all callbacks when sync service state has changed.
     */
    void notifyCallbacks();
}