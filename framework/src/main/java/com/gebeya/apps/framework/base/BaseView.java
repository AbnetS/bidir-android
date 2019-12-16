package com.gebeya.apps.framework.base;

/**
 * Base contract interface for all view components (Activity, Fragment and Dialogs).
 *
 * @param <T> Any type that extends the BasePresenter
 */
public interface BaseView<T extends BasePresenter> {

    /**
     * Attach the Presenter to the view
     */
    void attachPresenter(T presenter);

    /**
     * Dismiss the View from the UI
     */
    void close();
}