package com.gebeya.mobile.bidir.impl.util.network;

import io.reactivex.Observable;

/**
 * Contract for a wrapper to tell about the network status
 */
public interface NetworkWatcher {
    /**
     * Returns Observable determining if there is Internet connection. Throws an error otherwise.
     */
    Observable<Boolean> connected();
}