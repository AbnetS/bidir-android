package com.gebeya.mobile.bidir.impl.rx;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Test implementation of the {@link SchedulersProvider} for testing
 */
public class TestSchedulersProvider implements SchedulersProvider {

    private static SchedulersProvider instance;

    public static SchedulersProvider getInstance() {
        if (instance == null) {
            instance = new TestSchedulersProvider();
        }
        return instance;
    }

    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler background() {
        return Schedulers.trampoline();
    }
}
