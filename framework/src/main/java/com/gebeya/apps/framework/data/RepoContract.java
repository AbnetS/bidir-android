package com.gebeya.apps.framework.data;

import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;

import java.util.List;

public interface RepoContract<T> {

    interface ReloadCallback<T> {
        void onReloaded();
        void onReloadFailed(List<String> messages);
        void onReloadFailed(MediaBrowserService.Result result);
    }

    void forceReload(@NonNull ReloadCallback<T> callback);
}
