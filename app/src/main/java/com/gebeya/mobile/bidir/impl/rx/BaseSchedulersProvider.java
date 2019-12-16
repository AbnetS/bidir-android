package com.gebeya.mobile.bidir.impl.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Production implementation of the {@link SchedulersProvider} scheduler provider
 */
public class BaseSchedulersProvider implements SchedulersProvider {

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler background() {
        return Schedulers.io();
    }
}
