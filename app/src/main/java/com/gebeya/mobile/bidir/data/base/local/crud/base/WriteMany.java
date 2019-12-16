package com.gebeya.mobile.bidir.data.base.local.crud.base;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

/**
 * Represents write operations for many T items.
 */
public interface WriteMany<T> {

    /**
     * Store/saveLoanProposal a given List of objects T into local storage, and return an observable of the
     * objects for chaining.
     *
     * @param items List of T items to store/saveLoanProposal.
     * @return Observable of the same list of items, used for chaining.
     */
    Observable<List<T>> putAll(@NonNull List<T> items);
}