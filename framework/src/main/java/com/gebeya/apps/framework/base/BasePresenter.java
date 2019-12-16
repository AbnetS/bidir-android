package com.gebeya.apps.framework.base;

/**
 * Base interface for all presenter objects.
 */
public interface BasePresenter<V> {
    /**
     * Start the presenter
     */
    void start();

    /**
     * Attach the view
     */
    void attachView(V view);

    /**
     * Detach the view
     */
    void detachView();

    /**
     * Get the view
     */
    V getView();
}
