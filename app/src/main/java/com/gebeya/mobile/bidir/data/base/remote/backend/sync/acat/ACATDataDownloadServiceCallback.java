package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat;

/***
 * Callback interface for objects interested in the download progress of the
 * {@link ACATDataDownloadService} service.
 */
public interface ACATDataDownloadServiceCallback {

    /***
     * Invoked when the download has started.
     * @param totalPages The total pages of all the elements to download.
     */
    void onDownloadStarted(int totalPages);

    /**
     * Invoked when a page download has finished.
     *
     * @param totalProgress The total progress of all the pages downloaded so far.
     * @param totalPages The updated total of all the pages.
     */
    void onUpdate(int totalProgress, int totalPages);

    /**
     * Invoked when the download has stopped.
     */
    void onDownloadStopped();
}
