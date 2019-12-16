package com.gebeya.mobile.bidir.ui.home.main;

/**
 * Extension of View implementations that support syncing of data.
 */
public interface SyncableView {
    void showSyncInProgress();
    void showSyncIdle(boolean hasUnSyncedData);
    void startSyncService();
}
