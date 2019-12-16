package com.gebeya.mobile.bidir.ui.login;

import com.gebeya.mobile.bidir.impl.util.network.BaseRequestState;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;

/**
 * Maintains the state of the Login process in order to successfully restore it when there is
 * a configuration change.
 */
public class LoginState extends BaseRequestState {

    private static RequestState instance;

    public static RequestState getInstance() {
        if (instance == null) instance = new LoginState();
        return instance;
    }
}