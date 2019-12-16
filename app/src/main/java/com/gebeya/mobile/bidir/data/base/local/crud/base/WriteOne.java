package com.gebeya.mobile.bidir.data.base.local.crud.base;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Represents write operations for a single T item.
 */
public interface WriteOne<T> {

    /**
     * Store/saveLoanProposal a given object T into local storage, and immediately return an observable of the
     * object for chaining.
     *
     * @param item T item to store/saveLoanProposal.
     * @return Observable of the same item, used for chaining.
     */
    Observable<T> put(@NonNull T item);
}