package com.gebeya.mobile.bidir.data.base.local.crud.base;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

/**
 * Represents read operations for a single T item.
 */
public interface ReadOne<T> {

    /**
     * Return a T item with the given id, wrapped in a {@link Data} object, with null safety
     * checking.
     *
     * @param id String id used to look up the item.
     * @return Observable that contains a {@link Data} item wrapper.
     */
    Observable<Data<T>> get(@NonNull String id);

    /**
     * Return a T item at the given position, wrapped in a {@link Data} object, with null
     * safety checking.
     *
     * @param position int used to retrieve the item.
     * @return Observable that contains a {@link Data} item wrapper.
     */
    Observable<Data<T>> get(int position);

    /**
     * Return the first T item, at position zero, if any, wrapped in a {@link Data} object,
     * with null safety checking.
     *
     * @return Observable that contains a {@link Data} item wrapper.
     */
    Observable<Data<T>> first();
}