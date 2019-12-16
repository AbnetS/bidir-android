package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.answer.local.AnswerLocal;
import com.gebeya.mobile.bidir.data.answer.local.AnswerLocalSource;
import com.gebeya.mobile.bidir.data.user.local.UserLocal;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 1/9/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanApplicationQuestionsForTaskActivity extends BaseActivity {

    public static final String ARG_LOAN_APPLICATION_ID = "LOAN_APPLICATION_ID";
    public static final String ARG_LOAN_APPLICATION_TASK_ID = "LOAN_APPLICATION_TASK_ID";
    public static final String ARG_EDITABLE = "EDITABLE";

    private LoanApplicationQuestionsFragment fragment;
    private LoanApplicationQuestionsContract.Presenter presenter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application_questions);

        Intent intent = getIntent();
        final String loanApplicationId = intent.getStringExtra(ARG_LOAN_APPLICATION_ID);
        if (loanApplicationId == null) throw new NullPointerException("Loan application _id is null");

        final String loanApplciationTaskId = intent.getStringExtra(ARG_LOAN_APPLICATION_TASK_ID);
        final boolean editable = intent.getBooleanExtra(ARG_EDITABLE, false);

        fragment = (LoanApplicationQuestionsFragment) getFragment(R.id.questionsFragmentContainer);
        if (fragment == null) {
            fragment = LoanApplicationQuestionsFragment.newInstance();
            addFragment(fragment, R.id.questionsFragmentContainer);
        }

        final UserLocalSource userLocal = new UserLocal();
        final AnswerLocalSource answerLocal = new AnswerLocal();

/*        presenter = new LoanApplicationQuestionsForTaskPresenter(
                answerLocal,
                loanApplicationId,
                editable,
                ClientRepo.getInstance(
                        ClientLocal.getInstance(store),
                        ClientRemote.getInstance(),
                        userLocal
                ),
                ClientLocal.getInstance(store),
                LoanApplicationQuestionsState.getInstance(),
                PermissionLocal.getInstance(store),
                BaseSchedulersProvider.getInstance(),
                loanApplciationTaskId
        );*/

        fragment.attachPresenter(presenter);
    }

    @Override
    public void onBackPressed() {

    }
}
