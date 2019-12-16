package com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.downloader;

import com.gebeya.apps.framework.util.Loggable;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.pagination.Page;
import com.gebeya.mobile.bidir.data.pagination.local.BasePageManipulator;
import com.gebeya.mobile.bidir.data.pagination.local.PageLocalSource;
import com.gebeya.mobile.bidir.data.pagination.local.PageManipulator;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.impl.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * Base abstract implementation for the {@link DataDownloader} interface.
 */
public abstract class BaseDataDownloader implements DataDownloader, Loggable {

    @Inject PageLocalSource pageLocal;

    protected final String label;
    protected final Service type;

    protected final PageManipulator manipulator;
    private final List<Callback> callbacks;

    public BaseDataDownloader(@NonNull String label, @NonNull Service type) {
        this.label = label;
        this.type = type;
        manipulator = new BasePageManipulator();
        callbacks = new ArrayList<>();
    }

    @Override
    public void addCallback(@NonNull Callback callback) {
        d("Added callback: " + callback);
        callbacks.add(callback);
    }

    @Override
    public void removeCallback(@NonNull Callback callback) {
        d("Removed callback: " + callback);
        callbacks.remove(callback);
    }

    @Override
    public void start() {
        d("Retrieving pagination information...");
        final Data<Page> pageData = pageLocal.getPageByType(type);
        if (pageData.empty()) {
            d("-> No pagination information found");
            d("-> Will be created on the first request attempt");
        } else {
            manipulator.setPage(pageData.get());
            d("-> Found existing pagination information");
        }
        d("Starting download...");
    }

    @Override
    public void notifyCallbacks() {
        final int size = callbacks.size();
        if (size == 0) return;

        final Page page = manipulator.getPage();
        if (page == null) {
            e("-> Could not notify callbacks - manipulator has no page");
            return;
        }

        for (int i = 0; i < size; i++) {
            final Callback callback = callbacks.get(i);
            callback.onUpdated(page.currentPage, page.totalPages);
        }
    }

    @Override
    public void notifyCallbacksPaginationDataDownloaded() {
        final int size = callbacks.size();
        if (size == 0) return;

        final Page page = manipulator.getPage();
        if (page == null) {
            e("-> Could not notify callbacks - manipulator has no page");
            return;
        }

        for (int i = 0; i < size; i++) {
            final Callback callback = callbacks.get(i);
            callback.onPaginationDataDownloaded(page.totalPages);
        }
    }

    @Override
    public void d(String message) {
        Utils.d(this, label + " - " + message);
    }

    @Override
    public void d(String message, Object... formatArgs) {
        Utils.d(this, label + " - " + message, formatArgs);
    }

    @Override
    public void e(String message) {
        Utils.e(this, label + " - " + message);
    }

    @Override
    public void e(String message, Object... formatArgs) {
        Utils.e(this, label + " - " + message, formatArgs);
    }
}