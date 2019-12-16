package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader;

import io.reactivex.annotations.NonNull;

/**
 * Defines download operations given a specific entity.
 */
public interface DataDownloader {

    /**
     * Callback for the data downloader
     */
    interface Callback {

        /**
         * Invoked when the pagination data has been downloaded.
         *
         * @param totalPages The total number of pages to now download.
         */

        void onPaginationDataDownloaded(int totalPages);

        /**
         * Invoked when fresh data has been downloaded and the current page, along with the total
         * number of pages, have also been updated.
         *
         * @param currentPage Current page that the downloader just finished working on.
         * @param totalPages Total number of pages for the downloader.
         */
        void onUpdated(int currentPage, int totalPages);
    }

    /**
     * Add a new {@link Callback} object.
     *
     * @param callback Callback implementation to add.
     */
    void addCallback(@NonNull Callback callback);

    /**
     * Remove a {@link Callback} object.
     *
     * @param callback Callback implementation to remove.
     */
    void removeCallback(@NonNull Callback callback);

    /**
     * Downloads the pagination information. This method is invoked before the entire download
     * process in order to determine and store how many pages there are ahead of time.
     *
     * @throws Exception if there was an error during the download process.
     */
    void downloadPaginationData() throws Exception;

    /***
     * Download the data.
     *
     * @throws Exception thrown, if there was an error during the download.
     */
    void download() throws Exception;

    /**
     * Start the downloader.
     */
    void start();

    /**
     * Update all the callbacks of data changes.
     */
    void notifyCallbacks();

    /**
     * Update all the callbacks to indicate that the pagination data has been downloaded.
     */
    void notifyCallbacksPaginationDataDownloaded();
}
