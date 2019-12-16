package com.gebeya.mobile.bidir.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * RegisterDto Activity for holding all other components
 */
public class RegisterActivity extends BaseActivity {

    public static final String ARG_CLIENT = "CLIENT";
    public static final String ARG_GROUP_ID = "GROUP_ID";

    private RegisterFragment fragment;
    private RegisterContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        final Client client = (Client) intent.getSerializableExtra(ARG_CLIENT);
        final String groupId = intent.getStringExtra(ARG_GROUP_ID);

        presenter = new RegisterPresenter(
                client,
                RegisterData.getInstance(),
                RegisterState.getInstance(),
                groupId
        );

        fragment = (RegisterFragment) getFragment(R.id.registerFragmentContainer);
        if (fragment == null) {
            fragment = RegisterFragment.newInstance();
            addFragment(fragment, R.id.registerFragmentContainer);
        }
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }
}