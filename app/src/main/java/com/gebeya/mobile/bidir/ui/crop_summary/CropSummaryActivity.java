package com.gebeya.mobile.bidir.ui.crop_summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "CLIENT_ID";
    public static final String ARG_CROP_ID = "CROP_ID";
    public static final String ARG_TASK = "TASK";

    private CropSummaryFragment fragment;
    private CropSummaryContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_summary);

        final Intent intent = getIntent();

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID arg is null");

        final Task task = (Task) intent.getSerializableExtra(ARG_TASK);

        fragment = (CropSummaryFragment) getFragment(R.id.cropSummaryFragmentContainer);
        if (fragment == null) {
            fragment = new CropSummaryFragment();
            addFragment(fragment, R.id.cropSummaryFragmentContainer);
        }

        presenter = new CropSummaryPresenter(clientId, task);
        fragment.attachPresenter(presenter);
    }

}
