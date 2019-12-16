package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATEstimatedYieldActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "CLIENT_ID";
    public static final String IS_EDITING = "isEditing";
    public static final String ARG_CROP_ACAT_ID = "cropACATId";
    public static final String ARG_MONITOR = "isMonitoring";

    private ACATEstimatedYieldFragment fragment;
    private ACATEstimatedYieldContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_estimated_yield);

        final Intent intent = getIntent();
        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        final String cropACATId = intent.getStringExtra(ARG_CROP_ACAT_ID);

        if (clientId == null) throw new NullPointerException("Client ID is null");

        final boolean isEditing = intent.getBooleanExtra(IS_EDITING, false);
        final boolean isMonitoring = intent.getBooleanExtra(ARG_MONITOR, false);

        fragment = (ACATEstimatedYieldFragment) getFragment(R.id.acatEstimatedRevenueContainer);
        if (fragment == null) {
            fragment = ACATEstimatedYieldFragment.newInstance();
            addFragment(fragment, R.id.acatEstimatedRevenueContainer);
        }

        presenter = new ACATEstimatedYieldPresenter(clientId, isEditing, cropACATId, isMonitoring);
        fragment.attachPresenter(presenter);
    }

}
