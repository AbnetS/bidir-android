package com.gebeya.mobile.bidir.ui.home.main.clientlists;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class ClientListActivity extends BaseActivity {

    public static final String ARG_GROUP_ID = "Group_id";
    private ClientListContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        final Intent intent = getIntent();

        final String groupId = intent.getStringExtra(ARG_GROUP_ID);
        if (groupId == null) throw new NullPointerException("Group Id is null.");

        ClientListFragment fragment = (ClientListFragment) getFragment(R.id.clientListFragmentContainer);
        if (fragment == null) {
            fragment = new ClientListFragment();
            addFragment(fragment, R.id.clientListFragmentContainer);
        }
        presenter = new ClientListPresenter(groupId);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }

}
