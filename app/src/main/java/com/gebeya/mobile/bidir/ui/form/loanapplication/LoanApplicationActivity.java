package com.gebeya.mobile.bidir.ui.form.loanapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

public class LoanApplicationActivity extends BaseActivity {

    public static final String ARG_APPLICATION_ID = "APPLICATION_ID";
    public static final String ARG_TASK = "TASK";
    public static final String ARG_GROUP_ID = "GROUP_ID";

    private LoanApplicationContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        final Intent intent = getIntent();

        final String applicationId = intent.getStringExtra(ARG_APPLICATION_ID);
        if (applicationId == null) throw new NullPointerException("LoanApplication _id is null");

        final Task task = (Task) intent.getSerializableExtra(ARG_TASK);
        final String groupId = intent.getStringExtra(ARG_GROUP_ID);

        LoanApplicationFragment fragment = (LoanApplicationFragment) getFragment(R.id.loanApplicationFragmentContainer);
        if (fragment == null) {
            fragment = new LoanApplicationFragment();
            addFragment(fragment, R.id.loanApplicationFragmentContainer);
        }
        presenter = new LoanApplicationPresenter(applicationId, task, groupId);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}
