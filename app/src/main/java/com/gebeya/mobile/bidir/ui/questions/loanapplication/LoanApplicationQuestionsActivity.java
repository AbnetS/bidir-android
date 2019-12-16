package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Activity for the loan applications questions
 */
public class LoanApplicationQuestionsActivity extends BaseActivity {

    public static final String ARG_LOAN_APPLICATION_ID = "LOAN_APPLICATION_ID";
    public static final String ARG_EDITABLE = "EDITABLE";
    public static final String ARG_LOAN_APPLICATION_TASK_ID = "LOAN_APPLICATION_TASK_ID";

    private LoanApplicationQuestionsFragment fragment;
    private LoanApplicationQuestionsContract.Presenter presenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application_questions);

        Intent intent = getIntent();
        final String loanApplicationId = intent.getStringExtra(ARG_LOAN_APPLICATION_ID);
        if (loanApplicationId == null) throw new NullPointerException("Loan application _id is null");

        final String loanApplicationTaskId = intent.getStringExtra(ARG_LOAN_APPLICATION_TASK_ID);
        final boolean editable = intent.getBooleanExtra(ARG_EDITABLE, false);

        fragment = (LoanApplicationQuestionsFragment) getFragment(R.id.questionsFragmentContainer);
        if (fragment == null) {
            fragment = LoanApplicationQuestionsFragment.newInstance();
            addFragment(fragment, R.id.questionsFragmentContainer);
        }

        presenter = new LoanApplicationQuestionsPresenter(
                loanApplicationId,
                editable,
                LoanApplicationQuestionsState.getInstance(),
                loanApplicationTaskId
        );

        fragment.attachPresenter(presenter);
    }
}