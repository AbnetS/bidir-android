package com.gebeya.mobile.bidir.ui.register;

import com.gebeya.mobile.bidir.impl.util.network.BaseRequestState;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;

/**
 * Maintains the state of the registration instance
 */
public class RegisterState extends BaseRequestState {

    private static RequestState instance;

    public static RequestState getInstance() {
        if (instance == null) instance = new RegisterState();
        return instance;
    }
}