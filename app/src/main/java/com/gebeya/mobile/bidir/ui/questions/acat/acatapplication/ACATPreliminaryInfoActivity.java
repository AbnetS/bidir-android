package com.gebeya.mobile.bidir.ui.questions.acat.acatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 5/13/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATPreliminaryInfoActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "CLIENT_ID";
    public static final String ARG_GROUP_ID = "GROUP_ID";

    private ACATPreliminaryInfoFragment fragment;
    private ACATPreliminaryInfoContract.Presenter presenter;
    private String[] expenseMonth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_preliminary_info);

        final Intent intent = getIntent();

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        expenseMonth = getResources().getStringArray(R.array.expense_month);
        final String groupId = intent.getStringExtra(ARG_GROUP_ID);

        if (clientId == null) throw new NullPointerException("Client Id is null");

        fragment = (ACATPreliminaryInfoFragment) getFragment(R.id.ACATpreliminaryInformationContainer);
        if (fragment == null) {
            fragment = ACATPreliminaryInfoFragment.newInstance();
            addFragment(fragment, R.id.ACATpreliminaryInformationContainer);
        }

        presenter = new ACATPreliminaryInfoPresenter(clientId, expenseMonth, groupId);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
