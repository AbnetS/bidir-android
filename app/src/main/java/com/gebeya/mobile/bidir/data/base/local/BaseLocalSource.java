package com.gebeya.mobile.bidir.data.base.local;

import javax.inject.Inject;

import io.objectbox.BoxStore;

/**
 * Class for acting as the base local source for all other local source classes.
 */
public abstract class BaseLocalSource {
    /**
     * Used for dependency injection by {@link toothpick.Toothpick}
     */
    @Inject
    public BoxStore store;
}
