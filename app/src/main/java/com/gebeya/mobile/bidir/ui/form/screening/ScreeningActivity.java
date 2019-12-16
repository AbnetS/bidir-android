package com.gebeya.mobile.bidir.ui.form.screening;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Activity class dedicated to handling
 * {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion} question modules.
 */
public class ScreeningActivity extends BaseActivity {

    public static final String ARG_SCREENING_ID = "SCREENING_ID";
    public static final String ARG_TASK = "TASK";
    public static final String ARG_GROUP_ID = "GROUP_ID";

    private ScreeningContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        final Intent intent = getIntent();

        final String screeningId = intent.getStringExtra(ARG_SCREENING_ID);
        if (screeningId == null) throw new NullPointerException("Screening _id is null");

        final Task task = (Task) intent.getSerializableExtra(ARG_TASK);

        final String groupId = intent.getStringExtra(ARG_GROUP_ID);


        ScreeningFragment fragment = (ScreeningFragment) getFragment(R.id.screeningFragmentContainer);
        if (fragment == null) {
            fragment = new ScreeningFragment();
            addFragment(fragment, R.id.screeningFragmentContainer);
        }
        presenter = new ScreeningPresenter(screeningId, task, groupId);
        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }
}