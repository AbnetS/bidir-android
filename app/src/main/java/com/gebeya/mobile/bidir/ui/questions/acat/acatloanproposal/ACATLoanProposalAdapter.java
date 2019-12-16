package com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal;

import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATLoanProposalAdapter extends FragmentPagerAdapter {

    private final String[] titles;
    private String clientId;
    private String acatId;
    OnMenuButtonClickListener listener;

    public ACATLoanProposalAdapter(OnMenuButtonClickListener listener, @NonNull FragmentManager manager, @NonNull String[] titles, @NonNull String clientId, @NonNull String acatId) {
        super(manager);
        this.listener = listener;
        this.titles = titles;
        this.clientId = clientId;
        this.acatId = acatId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = new Bundle();
        switch (position) {
            case 0:
                LoanAssessmentFragment loanAssessmentFragment = LoanAssessmentFragment.newInstance(listener);
                arg.putString(ACATLoanProposalActivity.CLIENT_ID, clientId);
                arg.putString(ACATLoanProposalActivity.ACAT_ID, acatId);
                loanAssessmentFragment.setArguments(arg);
                return loanAssessmentFragment;
            case 1:
                LoanProposalFragment loanProposalFragment = LoanProposalFragment.newInstance(listener);
                arg.putString(ACATLoanProposalActivity.CLIENT_ID, clientId);
                arg.putString(ACATLoanProposalActivity.ACAT_ID, acatId);
                loanProposalFragment.setArguments(arg);
                return loanProposalFragment;
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return ACATLoanProposalFragment.FRAGMENT_COUNT;
    }
}
