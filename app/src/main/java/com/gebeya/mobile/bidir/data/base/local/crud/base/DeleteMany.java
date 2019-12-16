package com.gebeya.mobile.bidir.data.base.local.crud.base;

import io.reactivex.Completable;

/**
 * Represents delete operations for many T items.
 */
public interface DeleteMany {

    /**
     * Remove all the T items and return a Completable.
     *
     * @return Completable indicating operation completed.
     */
    Completable removeAll();
}
