package com.gebeya.mobile.bidir.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;


/**
 * Activity for profile.
 * <p>
 * samkura47@gmail.com
 */

public class ProfileActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "CLIENT_ID";

    private ProfileFragment fragment;
    private ProfileContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client _id is null");

        fragment = (ProfileFragment) getFragment(R.id.profileFragmentContainer);
        if (fragment == null) {
            fragment = ProfileFragment.newInstance();
            addFragment(fragment, R.id.profileFragmentContainer);
        }

        presenter = new ProfilePresenter(clientId);

        fragment.attachPresenter(presenter);
    }
}
