package com.gebeya.mobile.bidir.data.base.local.crud.base;

import java.util.List;

import io.reactivex.Observable;

/**
 * Represents read operations for many T items.
 */
public interface ReadMany<T> {

    /**
     * Return a list of all the T items stored locally.
     *
     * @return Observable that contains a list of all the T items.
     */
    Observable<List<T>> getAll();
}