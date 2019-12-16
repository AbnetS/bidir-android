package com.gebeya.mobile.bidir.data.base.remote.backend.sync;

import android.app.IntentService;

import com.gebeya.apps.framework.util.Loggable;
import com.gebeya.mobile.bidir.impl.util.Utils;

/**
 * Base service used for synchronization with the remote API for data.
 */
public abstract class BaseSyncService extends IntentService implements Loggable {

    public BaseSyncService(final String name) {
        super(name);
    }

    @Override
    public void d(final String message) {
        Utils.d(this, message);
    }

    @Override
    public void d(final String message, final Object... formatArgs) {
        Utils.d(this, message, formatArgs);
    }

    @Override
    public void e(final String message) {
        Utils.e(this, message);
    }

    @Override
    public void e(final String message, final Object... formatArgs) {
        Utils.e(this, message, formatArgs);
    }
}