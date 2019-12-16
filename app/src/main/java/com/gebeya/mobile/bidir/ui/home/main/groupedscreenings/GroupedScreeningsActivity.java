package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class GroupedScreeningsActivity extends BaseActivity {

    public static final String ARG_GROUP_ID = "GROUP_ID";
    public static final String ARG_TITLE = "TITLE";

    private GroupedScreeningsContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouped_screening);

        final Intent intent = getIntent();

        final String groupId = intent.getStringExtra(ARG_GROUP_ID);
        if (groupId == null) throw new NullPointerException("Group Id is null.");



        final String title = intent.getStringExtra(ARG_TITLE);

        GroupedScreeningsFragment fragment = (GroupedScreeningsFragment) getFragment(R.id.groupedScreeningsFragmentContainer);
        if (fragment == null) {
            fragment = new GroupedScreeningsFragment();
            addFragment(fragment, R.id.groupedScreeningsFragmentContainer);
        }

        presenter = new GroupedScreeningsPresenter(groupId, title);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
