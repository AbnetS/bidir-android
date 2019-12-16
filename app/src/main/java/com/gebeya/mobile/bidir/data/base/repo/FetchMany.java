package com.gebeya.mobile.bidir.data.base.repo;

import java.util.List;

import io.reactivex.Observable;

/**
 * Represents a data source of T items that can be fetched. A FetchMany data source is first retrieved from local
 * storage (if it exists). If it does not, an attempt to retrieve one from a remote source is made
 * and if successful, the retrieved data is stored locally (for future usages).
 */
public interface FetchMany<T> {
    /**
     * Fetch many objects and cache them locally if they do not exist.
     */
    Observable<List<T>> fetchAll();

    /**
     * Fetch one object remotely and overwrite the local cache.
     */
    Observable<List<T>> fetchForceAll();
}
