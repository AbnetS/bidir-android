package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.loanapplication.ComplexLoanApplicationSyncService;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.form.loanapplication.LoanApplicationActivity;
import com.gebeya.mobile.bidir.ui.home.main.groupedloans.GroupedLoansActivity;

/**
 * View implementation for the Application view contract
 */
public class LoanApplicationsFragment extends BaseFragment implements LoanApplicationsContract.View {

    private LoanApplicationsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView noLoanApplicationsLabel;
    private TextView noGroupedLoanApplicationsLabel;

    private LoanApplicationsRecyclerViewAdapter adapter;
    private GroupedLoanApplicationRecyclerViewAdapter groupedAdapter;

    private TextView individualLabel;
    private TextView groupLabel;

    private TextView loansFilter;

    private PopupMenu individualPopupMenu, groupPopupMenu;
    private Context wrapper;

    private View syncRoot;
    private View syncIcon;
    private View syncProgress;
    private View syncStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter(new LoanApplicationsPresenter());

        // Allows loading of Vector images on pre API 21 devices
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_loan_applications, container, false);

        recyclerView = getView(R.id.loanApplicationRecyclerView);
        noLoanApplicationsLabel = getTv(R.id.loanApplicationsNoApplicationsLabel);
        noGroupedLoanApplicationsLabel = getTv(R.id.loanApplicationsNoGroupApplicationsLabel);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LoanApplicationsRecyclerViewAdapter(presenter);
        groupedAdapter = new GroupedLoanApplicationRecyclerViewAdapter(presenter);

        individualLabel = getTv(R.id.loanTypeIndividualLabel);
        groupLabel = getTv(R.id.loanTypeGroupLabel);

//        individualLabel.setVisibility(View.GONE);
//        groupLabel.setVisibility(View.GONE);

        loansFilter = getTv(R.id.loansFragmentFilterLabel);
        loansFilter.setOnClickListener(v -> presenter.onLoansFilterMenuClicked());

        wrapper = new ContextThemeWrapper(getContext(), R.style.popupMenuStyle);
        individualPopupMenu = new PopupMenu(wrapper, loansFilter);
        individualPopupMenu.inflate(R.menu.loans_filter_menu);

        groupPopupMenu = new PopupMenu(wrapper, loansFilter);
        groupPopupMenu.inflate(R.menu.group_loans_filter_menu);

        individualLabel.setOnClickListener(view -> {
            presenter.onIndividualButtonPressed();
            individualPopupMenu.getMenu().getItem(0).setChecked(true);
        });
        groupLabel.setOnClickListener(view -> {
            presenter.onGroupButtonPressed();
            groupPopupMenu.getMenu().getItem(0).setChecked(true);
        });

        syncRoot = getView(R.id.syncIndicatorRoot);
        syncIcon = getView(R.id.syncIndicatorIcon);
        syncProgress = getView(R.id.syncIndicatorProgressWheel);
        syncStatus = getView(R.id.syncIndicatorStatusView);
        syncRoot.setOnClickListener(v -> presenter.onSyncPressed());

        return root;
    }

    @Override
    public void showSyncInProgress() {
        syncIcon.setVisibility(View.GONE);
        syncProgress.setVisibility(View.VISIBLE);
        syncStatus.setVisibility(View.GONE);
    }

    @Override
    public void showSyncIdle(boolean hasUnSyncedData) {
        syncIcon.setVisibility(View.VISIBLE);
        syncProgress.setVisibility(View.GONE);
        syncStatus.setVisibility(hasUnSyncedData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startSyncService() {
        final Context context = getContext();
        if (context == null) return;

        final Intent intent = new Intent(context, ComplexLoanApplicationSyncService.class);
        context.startService(intent);
    }

    @Override
    public void openLoanApplication(@NonNull String loanApplicationId) {
        final Intent intent = new Intent(getActivity(), LoanApplicationActivity.class);
        intent.putExtra(LoanApplicationActivity.ARG_APPLICATION_ID, loanApplicationId);
        startActivity(intent);
    }

    @Override
    public void openGroupedLoanApplication(@NonNull Group group, @NonNull String title) {
        final Intent intent = new Intent(getContext(), GroupedLoansActivity.class);
        intent.putExtra(GroupedLoansActivity.ARG_GROUP_ID, group._id);
        intent.putExtra(GroupedLoansActivity.ARG_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void openIndividualFilterMenu() {
        individualPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadLoanApplication();
                case R.id.newLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadNewApplication();
                    return true;
                case R.id.inProgressLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadInprogressLoan();
                    return true;
                case R.id.submittedLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadSubmittedLoan();
                    return true;
                case R.id.approvedLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadApprovedLoan();
                    return true;
                case R.id.declinedLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loanDeclinedLoan();
                    return true;
            }

            return false;
        });

        individualPopupMenu.show();
    }

    @Override
    public void openGroupFilterMenu() {
        groupPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupApplication();
                    return true;
                case R.id.newGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedNewApplication();
                    return true;
                case R.id.inProgressGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedInprogressLoan();
                    return true;
                case R.id.submittedGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedSubmittedLoan();
                    return true;
                case R.id.approvedGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedApprovedLoan();
                    return true;
                case R.id.declinedGroupLoansMenu:
                    menuItem.setChecked(true);
                    presenter.loanGroupedDeclinedLoan();
                    return true;
            }

            return false;
        });

        groupPopupMenu.show();
    }

    @Override
    public void toggleIndividualButton(boolean isEnabled) {
        if (isEnabled) {
            individualLabel.setTextColor(getResources().getColor(R.color.green));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_selected, 0, 0);
        } else {
            individualLabel.setTextColor(getResources().getColor(R.color.gray_light));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_default, 0, 0);
        }
    }

    @Override
    public void toggleGroupButton(boolean isEnabled) {
        if (isEnabled) {
            groupLabel.setTextColor(getResources().getColor(R.color.green));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_selected, 0, 0);
        } else {
            groupLabel.setTextColor(getResources().getColor(R.color.gray_light));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_default, 0, 0);
        }
    }

    @Override
    public void attachPresenter(LoanApplicationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        individualPopupMenu.getMenu().getItem(0).setChecked(true);
        groupPopupMenu.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public void showLoanApplications() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showGrouped() {
        recyclerView.setAdapter(groupedAdapter);
    }

    @Override
    public void toggleNoLoanApplications(boolean show) {
        noLoanApplicationsLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleNoGroupLoanApplications(boolean show) {
        noGroupedLoanApplicationsLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}