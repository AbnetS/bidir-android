package com.gebeya.mobile.bidir.ui.profile;

import com.gebeya.mobile.bidir.impl.util.network.BaseRequestState;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;


/**
 * Concrete implementation for the {@link RequestState} profile state.
 * <p>
 * samkura47@gmail.com
 */

public class ProfileState extends BaseRequestState {
    private static RequestState instance;

    public static RequestState getInstance() {
        if (instance == null) instance = new ProfileState();
        return instance;
    }
}
