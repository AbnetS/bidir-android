package com.gebeya.mobile.bidir.ui.gps_location_selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * GPS Selector activity.
 */
public class GPSSelectorActivity extends BaseActivity {

    public static final String ARG_CROP_ACAT_ID = "CROP_ACAT_ID";
    public static final String ARG_ACAT_APPLICATION_ID = "ACAT_APPLICATION_ID";

    private GPSSelectorFragment fragment;
    private GPSSelectorContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_selector);

        final Intent intent = getIntent();
        final String cropACATId = intent.getStringExtra(ARG_CROP_ACAT_ID);
        if (cropACATId == null) throw new NullPointerException("Crop ACAT _id is null");

        final String acatApplicationId = intent.getStringExtra(ARG_ACAT_APPLICATION_ID);
        if (acatApplicationId == null) throw new NullPointerException("ACAT Application _id is null");

        fragment = (GPSSelectorFragment) getFragment(R.id.gpsSelectorFragmentContainer);
        if (fragment == null) {
            fragment = new GPSSelectorFragment();       // TODO: Change the initialization
            addFragment(fragment, R.id.gpsSelectorFragmentContainer);
        }
        presenter = new GPSSelectorPresenter(cropACATId, acatApplicationId);
        fragment.attachPresenter(presenter);
    }
}