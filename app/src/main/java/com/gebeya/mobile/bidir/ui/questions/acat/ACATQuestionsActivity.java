package com.gebeya.mobile.bidir.ui.questions.acat;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationFragment;

/**
 * Created by Samuel K. on 3/9/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATQuestionsActivity extends BaseActivity {

    private ACATCostEstimationFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_questions);

        fragment = (ACATCostEstimationFragment) getFragment(R.id.questionsACATContainer);
        fragment = ACATCostEstimationFragment.newInstance();
        addFragment(fragment, R.id.questionsACATContainer);

    }
}
