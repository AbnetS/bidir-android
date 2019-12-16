package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATLoanProposalActivity extends BaseActivity {

    public static final String CLIENT_ID = "client_id";
    public static final String ACAT_ID = "acat_id";
    private ACATLoanProposalFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_loan_proposal);

        final Intent intent = getIntent();

        final String clientId = intent.getStringExtra(CLIENT_ID);
        final String acatId = intent.getStringExtra(ACAT_ID);

        if (clientId == null) throw new NullPointerException("Client Id is null.");
        if (acatId == null) throw new NullPointerException("ACAT Id is null.");

        final Bundle args = new Bundle();
        args.putString(CLIENT_ID, clientId);
        args.putString(ACAT_ID, acatId);



        fragment = (ACATLoanProposalFragment) getFragment(R.id.questionsLoanProposalContainer);
        fragment = ACATLoanProposalFragment.newInstance();
        fragment.setArguments(args);
        addFragment(fragment, R.id.questionsLoanProposalContainer);
    }

}
