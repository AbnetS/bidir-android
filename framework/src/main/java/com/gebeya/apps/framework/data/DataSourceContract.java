package com.gebeya.apps.framework.data;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.util.Result;

import java.util.List;

/**
 * Contract for a data source object (remote, local etc)
 * @param <T> Type of data to return.
 */
public interface DataSourceContract<T> {

    /**
     * Callback for when a single item has been loaded.
     * @param <T> Item to load.
     */
    interface LoadItemCallback<T> {
        /**
         * Invoked when an item has successfully been loaded.
         * @param item loaded item.
         */
        void onItemLoaded(T item);

        /**
         * Invoked when the item does not exist.
         */
        void onDataUnavailable();

        /**
         * Invoked when loading a particular item failed.
         * @param result
         */
        void onLoadingFailed(Result result);
    }

    /**
     * Retrieve a given Item T with the provided String id value.
     * @param id String id value to lookup for the object.
     * @param callback Callback invoked when the retrieval has completed.
     */
    void getItem(@NonNull final String id, @NonNull final LoadItemCallback<T> callback);

    /**
     *
     * Retrieve a given Item T with the provided int position value.
     * @param position int value to lookup for the object.
     * @param callback Callback invoked when the retrieval has completed.
     */
    void getItem(int position, @NonNull final LoadItemCallback<T> callback);

    /**
     * Retrieve the size of the data.
     * @return int size for the data.
     */
    int size();

    /**
     * Callback for when a collection of items have been loaded.
     * @param <T> Item type to load.
     */
    interface LoadItemsCallback<T> {

        /**
         * Invoked when the collection of items have been loaded.
         * @param items data passed back to the callback.
         */
        void onItemsLoaded(List<T> items);

        /**
         * Invoked when data is unavailable.
         */
        void onDataUnavailable();

        /**
         * Invoked when loading the collection of items failed.
         * @param result Error containing the result.
         */
        void onLoadingFailed(Result result);
    }

    /**
     * Retrieve a collection of items.
     * @param callback Callback to invoke when the operation completes.
     */
    void getItems(@NonNull final LoadItemsCallback<T> callback);

    /**
     * Callback for when a collection of items is added.
     * @param <T> Item type in the collection to add/
     */
    interface AddItemsCallback<T> {
        /**
         * Invoked when the items have been added successfully.
         * @param items data loaded.
         */
        void onItemsAdded(List<T> items);

        /**
         * Invoked when adding the items has failed.
         * @param messages Error messages explaining why/how each item failed.
         */
        void onFailed(List<String> messages);
    }

    /**
     * Add a collection of items.
     * @param items list of items to add.
     * @param callback Callback to invoke when the operation completes.
     */
    void addItems(@NonNull final List<T> items, @NonNull final AddItemsCallback<T> callback);

    /**
     * Callback for when an item is removed.
     *
     * @param <T> Type for the item to remove.
     */
    interface RemoveItemCallback<T> {
        /**
         * Invoked when an item has been successfully removed.
         * @param item removed item.
         */
        void onItemRemoveSuccess(T item);

        /**
         * Invoked when the removal of the object has failed.
         * @param message message explaining why/how the removal failed.
         */
        void onItemRemoveFailed(String message);
    }

    /**
     * Remove a single item.
     * @param item Item to remove.
     * @param callback Callback to invoke when the operation has completed.
     */
    void removeItem(@NonNull final T item, @NonNull final RemoveItemCallback<T> callback);

    /**
     * Callback for when multiple items are being removed.
     *
     * @param <T> Type for the item in the collections being removed.
     */
    interface RemoveItemsCallback<T> {
        /**
         * Invoked when items have been removed.
         */
        void onItemsRemoved();

        /**
         * Invoked when the removal of items has failed.
         * @param messages List of messages explaining how each item failed to be removed.
         */
        void onItemsRemoveFailed(List<String> messages);
    }

    /**
     * Remove a collection of items.
     * @param items items to remove.
     * @param callback Callback invoked when the operation completes/
     */
    void removeItems(@NonNull final List<T> items, @NonNull final RemoveItemsCallback<T> callback);

    /**
     * Remove all the data in this data source object.
     * @param callback Callback invoked when the operation completes.
     */
    void removeAll(@NonNull final RemoveItemsCallback<T> callback);
}