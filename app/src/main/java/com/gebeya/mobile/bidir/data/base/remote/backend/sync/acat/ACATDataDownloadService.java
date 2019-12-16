package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.BaseSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader.ACATApplicationDataDownloader;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader.ACATFormDataDownloader;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader.DataDownloader;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ACATDataDownloadService extends BaseSyncService  {

    private static final String TAG = ACATDataDownloadService.class.getSimpleName();
    private static List<ACATDataDownloadServiceCallback> callbacks = new ArrayList<>();

    @Inject ACADataDownloadState state;

    private int acatFormCurrentPage = 0;
    private int acatFormTotalPages = 0;
    private DataDownloader.Callback acatFormDataDownloaderCallback = new DataDownloader.Callback() {
        @Override
        public void onPaginationDataDownloaded(int totalPages) {
            acatFormTotalPages = totalPages;
        }

        @Override
        public void onUpdated(int currentPage, int totalPages) {
            acatFormCurrentPage++;
            acatFormTotalPages = totalPages;

            notifyCallbacksUpdate();
        }
    };
    private DataDownloader acatFormDataDownloader;

    private int acatApplicationCurrentPage = 0;
    private int acatApplicationTotalPages = 0;
    private DataDownloader.Callback acatApplicationDownloaderCallback = new DataDownloader.Callback() {
        @Override
        public void onPaginationDataDownloaded(int totalPages) {
            acatApplicationTotalPages = totalPages;
        }

        @Override
        public void onUpdated(int currentPage, int totalPages) {
            acatApplicationCurrentPage++;
            acatApplicationTotalPages = totalPages;

            notifyCallbacksUpdate();
        }
    };
    private DataDownloader acatApplicationDataDownloader;

    public ACATDataDownloadService() {
        super(ACATDataDownloadService.class.getSimpleName());
        acatFormDataDownloader = new ACATFormDataDownloader(
                "ACATForm",
                Service.ACAT_FORM
        );

        acatApplicationDataDownloader = new ACATApplicationDataDownloader(
                "ACATApplication",
                Service.ACAT_PROCESSOR
        );

        Tooth.inject(this, Scopes.SCOPE_ROOT);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        d("Service started...");
        if (state.busy()) {
            d("-> Service is already running");
            return;
        }
        state.setBusy();

        acatFormDataDownloader.addCallback(acatFormDataDownloaderCallback);
        acatApplicationDataDownloader.addCallback(acatApplicationDownloaderCallback);

        notifyCallbacksDownloadStarted();

        try {
            acatFormDataDownloader.downloadPaginationData();
            acatApplicationDataDownloader.downloadPaginationData();
        } catch (Exception e) {
            e("Downloading pagination data failed:");
            e.printStackTrace();
        }

        acatFormDataDownloader.start();
        acatApplicationDataDownloader.start();

        acatFormDataDownloader.removeCallback(acatFormDataDownloaderCallback);
        acatApplicationDataDownloader.removeCallback(acatApplicationDownloaderCallback);

        d("Downloading ACAT Form data completed.");
        d("Service stopped.");

        state.setIdle();
        notifyCallbacksDownloadStopped();
    }

    public static void addCallback(@NonNull ACATDataDownloadServiceCallback callback) {
        callbacks.remove(callback);
        callbacks.add(callback);
        Utils.d(TAG, "Added Service Callback: " + callback);
    }

    public static void removeCallback(@NonNull ACATDataDownloadServiceCallback callback) {
        callbacks.remove(callback);
        Utils.d(TAG, "Removed Service Callback: " + callback);
    }

    private int getTotalPages() {
        return acatFormTotalPages + acatApplicationTotalPages;
    }

    private int getCurrentPages() {
        return acatFormCurrentPage + acatApplicationCurrentPage;
    }

    private void notifyCallbacksDownloadStarted() {
        if (callbacks.isEmpty()) return;
        final int totalPages = getTotalPages();

        final int length = callbacks.size();
        for (int i = 0; i < length; i++) {
            callbacks.get(i).onDownloadStarted(totalPages);
        }
    }

    private void notifyCallbacksUpdate() {
        if (callbacks.isEmpty()) return;

        final int totalPages = getTotalPages();
        final int currentPages = getCurrentPages();

        final int length = callbacks.size();
        for (int i = 0; i < length; i++) {
            callbacks.get(i).onUpdate(currentPages, totalPages);
        }
    }

    private void notifyCallbacksDownloadStopped() {
        if (callbacks.isEmpty()) return;
        final int length = callbacks.size();
        for (int i = 0; i < length; i++) {
            callbacks.get(i).onDownloadStopped();
        }
    }
}
