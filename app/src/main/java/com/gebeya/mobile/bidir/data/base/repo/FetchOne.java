package com.gebeya.mobile.bidir.data.base.repo;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Represents fetch operations that can be performed on item T.
 */
public interface FetchOne<T> {

    /**
     * Attempts to fetch an item from either the local or the remote source.
     * If the item exists in the local source, it is returned.
     *
     * If it does not exist locally, an
     * attempt to retrieve it from the remote (by calling the fetchForce(String) is performed,
     * then stored locally for future usages.
     */
    Observable<T> fetch(@NonNull String id);

    /**
     * Force fetches an item from the remote source and overwrites the one stored inside the local
     * source.
     */
    Observable<T> fetchForce(@NonNull String id);
}