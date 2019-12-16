package com.gebeya.mobile.bidir.ui.home.main.groupedclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class GroupedClientActivity extends BaseActivity {

    public static final String ARG_GROUP_ID = "GROUP_ID";
    public static final String ARG_TITLE = "TITLE";

    private GroupedClientContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouped);

        final Intent intent = getIntent();

        final String groupId = intent.getStringExtra(ARG_GROUP_ID);
        if (groupId == null) throw new NullPointerException("Group Id is null.");

        final String title = intent.getStringExtra(ARG_TITLE);

        GroupedClientFragment fragment = (GroupedClientFragment) getFragment(R.id.groupedApplicationFragmentContainer);
        if (fragment == null) {
            fragment = new GroupedClientFragment();
            addFragment(fragment, R.id. groupedApplicationFragmentContainer);
        }

        presenter = new GroupClientPresenter(groupId, title);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
