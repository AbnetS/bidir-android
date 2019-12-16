package com.gebeya.mobile.bidir.impl.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.impl.util.Utils;

import io.reactivex.Observable;

/**
 * Base/concrete implementation for the network watcher
 */
public class BaseNetworkWatcher implements NetworkWatcher {

    private Context context;

    public BaseNetworkWatcher(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public Observable<Boolean> connected() {
        final boolean connected = Utils.connected(context);
        return connected ? Observable.just(true) : Observable.error(new Exception(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION));
    }
}