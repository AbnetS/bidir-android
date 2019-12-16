package com.gebeya.mobile.bidir.ui.home.tasks;

import com.gebeya.mobile.bidir.impl.util.network.BaseRequestState;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;

/**
 * State implementation for the Tasks
 */
public class TasksState extends BaseRequestState {
    private static RequestState instance;

    public static RequestState getInstance() {
        if (instance == null) instance = new TasksState();
        return instance;
    }
}
