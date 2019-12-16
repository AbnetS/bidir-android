package com.gebeya.mobile.bidir.data.base.local.crud.base;

/**
 * Represents an operation for reading the size of data.
 */
public interface ReadSize {

    /**
     * Get the size of the items stored locally.
     *
     * @return int size of all the items.
     */
    int size();
}