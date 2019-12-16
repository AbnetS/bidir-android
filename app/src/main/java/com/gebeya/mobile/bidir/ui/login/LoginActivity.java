package com.gebeya.mobile.bidir.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.impl.rx.BaseSchedulersProvider;

/**
 * Login Activity for initializing the login components (view and presenter)
 */
public class LoginActivity extends BaseActivity {

    private LoginFragment fragment;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragment = (LoginFragment) getFragment(R.id.loginFragmentContainer);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            addFragment(fragment, R.id.loginFragmentContainer);
        }

        presenter = new LoginPresenter(LoginState.getInstance());

        fragment.attachPresenter(presenter);
    }
}