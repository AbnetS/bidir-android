package com.gebeya.mobile.bidir.ui.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class SummaryActivity extends BaseActivity {

    public static final String ARG_CROP_ACAT_ID = "CROP_ACAT_ID";
    public static final String ARG_CLIENT_ID = "CLIENT_ID";

    private SummaryFragment fragment;
    private SummaryContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        final Intent intent = getIntent();

        final String cropACATId = intent.getStringExtra(ARG_CROP_ACAT_ID);
        if (cropACATId == null) throw new NullPointerException("crop ACAT ID arg is null");

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID arg is null");

        fragment = (SummaryFragment) getFragment(R.id.summaryFragmentContainer);
        if (fragment == null) {
            fragment = new SummaryFragment();
            addFragment(fragment, R.id.summaryFragmentContainer);
        }

        presenter = new SummaryPresenter(cropACATId, clientId);
        fragment.attachPresenter(presenter);
    }
}