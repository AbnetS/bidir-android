package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;
import com.gebeya.mobile.bidir.ui.home.main.SyncablePresenter;
import com.gebeya.mobile.bidir.ui.home.main.SyncableView;

/**
 * Contractual interface for the loan applications.
 */
public interface LoanApplicationsContract {

    /**
     * Loan applications presenter interface contract
     */
    interface Presenter extends BasePresenter<View>, SyncablePresenter {
        void onBindRowView(LoanApplicationItemView holder, int position);
        void onLoanApplicationSelected(int position);
        int getLoanApplicationCount();

        void onIndividualButtonPressed();
        void onGroupButtonPressed();

        int getGroupCount();
        void onGroupSelected(int position);
        void onGroupBindRowView(GroupItemView holder, int position);

        void onLoansFilterMenuClicked();

        void loadLoanApplication();
        void loadNewApplication();
        void loadInprogressLoan();
        void loadSubmittedLoan();
        void loadApprovedLoan();
        void loanDeclinedLoan();

        void loadGroupApplication();
        void loadGroupedNewApplication();
        void loadGroupedInprogressLoan();
        void loadGroupedSubmittedLoan();
        void loadGroupedApprovedLoan();
        void loanGroupedDeclinedLoan();
    }

    /**
     * LoanApplication view interface contract
     */
    interface View extends BaseView<Presenter>, SyncableView {
        void showLoanApplications();
        void showGrouped();

        void toggleNoLoanApplications(boolean show);
        void toggleNoGroupLoanApplications(boolean show);

        void openLoanApplication(@NonNull String loanApplicationId);
        void openGroupedLoanApplication(@NonNull Group group, @NonNull String title);

        void toggleIndividualButton(boolean isEnabled);
        void toggleGroupButton(boolean isEnabled);

        void openIndividualFilterMenu();
        void openGroupFilterMenu();
    }

    /**
     * Interface representing a loan application item view
     */
    interface LoanApplicationItemView extends BaseMainItemView {

    }

    /**
     * Interface representing grouped complex loan application item view
     */
    interface GroupItemView {
        void setGroupName(@NonNull String groupName);
        void setLeaderName(@Nullable String leaderName);
        void setStatus(@NonNull String status);
        void setImage(@NonNull String pictureUrl);
        void setGroupCount(@NonNull int groupCount);
        void toggleCreatedIndicator(boolean show);
    }
}

