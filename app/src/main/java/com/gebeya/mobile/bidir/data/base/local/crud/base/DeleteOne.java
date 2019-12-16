package com.gebeya.mobile.bidir.data.base.local.crud.base;

import android.support.annotation.NonNull;

import io.reactivex.Completable;

/**
 * Represents delete operations for a single T item.
 */
public interface DeleteOne {

    /**
     * Remove a T item at the given position.
     *
     * @param position int used to remove the item.
     * @return Completable indicating operation finished.
     */
    Completable remove(int position);

    /**
     * Remove and return a T item with the given id.
     *
     * @param id String id used to remove the item.
     * @return Completable indicating operation finished.
     */
    Completable remove(@NonNull String id);
}