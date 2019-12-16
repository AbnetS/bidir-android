package com.gebeya.mobile.bidir.impl.rx;

import io.reactivex.Scheduler;

/**
 * Interface for returning custom scheduler used for RxJava and also for testing
 */
public interface SchedulersProvider {

    /**
     * Returns a UI Scheduler implementation
     */
    Scheduler ui();

    /**
     * Returns a background Scheduler
     */
    Scheduler background();
}
