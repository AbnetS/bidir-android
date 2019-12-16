package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;


/**
 * Created by Samuel K. on 7/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCropItemActivity extends BaseActivity{
    public static final String ARG_CLIENT_ID = "CLIENT_ID";
    public static final String ARG_GROUP_ID = "GROUP_ID";
    public static final String ARG_TASK = "TASK";
    public static final String ARG_CLIENT_ACAT_ID = "CLIENT_ACAT_ID";
    public static final String IS_MONITORING = "isMonitoring";


    private ACATCropItemContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_crop_item);

        final Intent intent = getIntent();
        final Task task = (Task) intent.getSerializableExtra(ARG_TASK);

        final String clientACATId = intent.getStringExtra(ARG_CLIENT_ACAT_ID);

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        final String groupId = intent.getStringExtra(ARG_GROUP_ID);

        final boolean isMonitoring = intent.getBooleanExtra(IS_MONITORING, false);

//        if (clientId == null) throw new NullPointerException("Client Id is null.");

        final Bundle args = new Bundle();
        args.putString(ARG_CLIENT_ID, clientId);
        args.putSerializable(ARG_TASK, task);
        args.putString(ARG_CLIENT_ACAT_ID, clientACATId);
        args.putBoolean(IS_MONITORING, isMonitoring);

        ACATCropItemFragment fragment = (ACATCropItemFragment) getFragment(R.id.acatCropItemFragmentContainer);

        if (fragment == null) {
            fragment = new ACATCropItemFragment();
            addFragment(fragment, R.id.acatCropItemFragmentContainer);
        }
        presenter = new ACATCropItemPresenter(clientId, task, clientACATId, isMonitoring, groupId);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
