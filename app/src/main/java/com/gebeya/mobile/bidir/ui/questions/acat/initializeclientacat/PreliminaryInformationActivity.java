package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Activity for holding the preliminary information
 */

public class PreliminaryInformationActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "CLIENT_ID";
    public static final String ARG_GROUP_ID = "GROUP_ID";

    private PreliminaryInformationFragment fragment;
    private PreliminaryInformationContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preliminary_info);

        final Intent intent = getIntent();

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client id is null");
        final String groupId = intent.getStringExtra(ARG_GROUP_ID);

        fragment = (PreliminaryInformationFragment) getFragment(R.id.preliminaryInformationContainer);
        if (fragment == null) {
            fragment = PreliminaryInformationFragment.newInstance();
            addFragment(fragment, R.id.preliminaryInformationContainer);
        }

        presenter = new PreliminaryInformationPresenter(clientId, groupId);

        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
